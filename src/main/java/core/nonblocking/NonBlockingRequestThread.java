package core.nonblocking;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.Listenable;
import core.util.Listener;

/** 
 * Implements non-blocking I/o functionality to allow us to handle multiple 
 * search requests on a single thread.
 */
public class NonBlockingRequestThread extends Thread implements Listenable
{
	// The Logger.
	private static final Logger c_log = LoggerFactory.getLogger(NonBlockingRequestThread.class);
	
	/**
	 * The request handler that determines which thread to pass the next request to, ie in this
	 * case, the thread manager.
	 */
	private Listener i_listener = null;
	
	/** A unique identifier for this thread, used by the thread manager. */
	private String i_threadName;

	/** The maximum number of simultaneous requests that this thread can process. */
	private int i_maxRequests;

	/** The current number of requests being serviced. */
	private AtomicInteger i_noOfRequests = new AtomicInteger();
	
	/** The total number of requests on this thread from startup. */
	private long i_totalRequests = 0;
	
	/** The total number of errors on this thread since startup. */
	private long i_totalErrors = 0;
	
	/** The selector object that drives the multiplexing I/O process. */
	private Selector i_selector = null;

	/** The timeout in ms for the select operation on the selector if no events occur. */
	private int i_selectorTimeout;
	
	/** New requests that have arrived since the last time the thread checked. */
	private ConcurrentLinkedQueue<NonBlockingRequestHandler> i_newRequests =  new ConcurrentLinkedQueue<NonBlockingRequestHandler>();
	
	/** Flag to indicate whether shutdown of Thread has commenced. */
	private boolean i_shutdown = false;
	
	/**
	 * Constructor - creates a Runnable object capable of handling multiple non-blocking IO requests
	 * simultaneously.
	 * 
	 * @param p_threadNumber unique numeric identifier for this thread (index in the threadPool)	
	 * @param p_maxRequests	 max number of simultaneous requests that this thread can process. 
	 * @param p_selectorTimeout timeout in ms for the select operation on the selector if no events occur.
	 */
	public NonBlockingRequestThread(int p_threadNumber,
									int p_maxRequests,
									int p_selectorTimeout) 
	{
		super();
		
		i_threadName = Integer.toString(p_threadNumber);
		i_maxRequests = p_maxRequests;
		i_selectorTimeout = p_selectorTimeout;
	}

	/** 
	 * The main run method - multiplexes I/O requests via non-blocking channels until
	 * shutdown via the manager.
	 */
	public void run()
	{
		NonBlockingRequestHandler  l_requestHandler =  null;
		
		Set<SelectionKey>	   l_selectedKeys;
		Iterator<SelectionKey> l_iterator;
		HashSet<SelectionKey>  l_idleKeys;
		SelectionKey    	   l_key;

		try
		{
			i_selector = Selector.open();
		
			
			// Carry on processing until a shutdown command has been issued AND we 
			// no longer have any requests to process.
			while ((!i_shutdown )|| (i_noOfRequests.get() > 0))
			{
				try
				{
					// Handle new requests first
					while ((l_requestHandler = i_newRequests.poll()) != null)
					{
						l_requestHandler.startTimer();
//						QuickStatsEngine2.engine.NON_BLOCKING_REQUEST_LATENCY.logEvent(l_requestHandler.getRequestLatency());
						try {
							l_requestHandler.startRequest();
						} catch (Exception e) {
							// (MISCO-224) 
							// If we have an exception before registering the request with the 
							// the selector then we can instantly free up the slot on this thread
							removeRequest();
							throw e;
						}
						l_requestHandler.stopTimer();
						
						l_requestHandler.startTimer();
						l_requestHandler.register(i_selector,
											  	  this);
						l_requestHandler.stopTimer();
					}
					
					// Now query Selector to see what else we need to do.
					// This will wait until an event is available or until we wake the selector
					// up by adding a new request or until the selector times out.
					i_selector.select(i_selectorTimeout);
					
					// Create a set of events to handle and a set of inactive keys
					l_selectedKeys = i_selector.selectedKeys();
					l_idleKeys = new HashSet<SelectionKey>(i_selector.keys());
					
					if (l_selectedKeys.size() > 0)
					{
						// Remove the active keys from the set of idle keys.
						l_idleKeys.removeAll(l_selectedKeys);
						
						// Each key we select represents an outstanding I/O operation on a request. 
						l_iterator = l_selectedKeys.iterator();
						
						while (l_iterator.hasNext())
						{
							// Get the next key in the set (and remove it from the set).
							l_key = l_iterator.next();
							l_iterator.remove();
							
							// Disable the interest for the operation that is ready. This prevents
							// the same event from being raised multiple times if an exception is
							// thrown before the request handler can reset its interests.
							l_key.interestOps(l_key.interestOps() & ~l_key.readyOps());
						          
							// Retrieve the handler associated with this key
						    l_requestHandler = (NonBlockingRequestHandler)l_key.attachment();          
						        
						    // Now find out what kind of event we have and handle it appropriately.
						    // Since this is a client rather than a server we need to handle
						    // connect, read and write. Do all the ifs in series - in theory we might 
						    // have more than one event to handle. 
						    if (l_key.isConnectable())
						    {
						    	l_requestHandler.startTimer();
						    	l_requestHandler.connectToServer(l_key);
						    	l_requestHandler.stopTimer();
						    }
						    
						    if (l_key.isReadable())
						    {
						    	l_requestHandler.startTimer();
						    	l_requestHandler.readData(l_key);
								l_requestHandler.stopTimer();
						    }
						    
						    if (l_key.isValid() && l_key.isWritable())
						    {
						    	l_requestHandler.startTimer();
						    	l_requestHandler.writeData(l_key);
						    	l_requestHandler.stopTimer();
						    }
						}
					}
					
					// Now check for timeout on the inactive keys.
					l_iterator = l_idleKeys.iterator();
					
					while (l_iterator.hasNext())
					{
						// Get the next key in the set.
						l_key = l_iterator.next();
						
						// Retrieve the handler associated with this key
					    l_requestHandler = (NonBlockingRequestHandler)l_key.attachment();          
					    
					    //... and check for timeout.
					    l_requestHandler.startTimer();
					    l_requestHandler.checkForTimeout();
					    l_requestHandler.stopTimer();
					}
					
				} catch (Throwable l_t){
					// Store an error count on this thread.
					i_totalErrors++;
					
					try{
						// Now mark the search as crashed and tidy everything up. 
						if (l_requestHandler != null)
						{
							l_requestHandler.startTimer();
							l_requestHandler.setThrowableOnTask(l_t);
							l_requestHandler.finishRequest();
							l_requestHandler.stopTimer();
						}
					}catch (Exception l_e){
						if (c_log.isErrorEnabled()) c_log.error(l_e.getMessage(), l_e);
					}
				}	
			// End of main processing loop.
			}
		
		} catch (IOException l_iOE){
			if (c_log.isErrorEnabled()) c_log.error(l_iOE.getMessage(),l_iOE);
		} finally {
			if(i_selector != null){
				try {
					i_selector.close();
				} catch (IOException e) {
					if (c_log.isErrorEnabled()) c_log.error("failed to close selector", e);
				}
			}
		}
		
		if (c_log.isDebugEnabled()) c_log.debug("Thread shut down - " + i_threadName);
	}
	
	public void register(Listener p_listener)
	{
		i_listener = p_listener;
	}
	
	public void deregister(Listener p_listener)
	{
		i_listener = null;
	}

	// We don't need to synchronize this method since it is already synchronized 
	// at the manager level. The manager level is the correct place to synchronize
	// the adding of requests since it ensures that the total state of the thread 
	// pool is synchronized in order to select the correct thread for request
	// allocation.
	void addRequest(NonBlockingRequestHandler p_request) throws Exception
	{
		// Add new requests to a queue (if we haven't exceeded max requests).
		if (i_noOfRequests.get() < i_maxRequests)
		{
			if (c_log.isDebugEnabled()) c_log.debug ("Adding request to thread " + i_threadName);
			i_newRequests.add(p_request);
			i_totalRequests++;

			// If the request was previously empty, tell the manager that it is no longer idle.
			if (i_noOfRequests.getAndIncrement() == 0)
			{
				i_listener.notify(i_threadName,
								  Listener.C_EVENT_RESOURCE_NOT_IDLE,
								  "");
				if (c_log.isDebugEnabled()) c_log.debug ("Thread " + i_threadName + " is no longer empty");
			}
			
			// If we have reached max requests, tell the manager that this thread is no longer
			// available for new requests.
			if (i_noOfRequests.get() == i_maxRequests)
			{
				i_listener.notify(i_threadName,
								  Listener.C_EVENT_RESOURCE_UNAVAILABLE,
								  "");
				if (c_log.isDebugEnabled()) c_log.debug ("Thread " + i_threadName + " is unavailable");
			}
			
			// Wake up Selector to tell it that a new request has arrived. But make sure 
			// that Selector has already been created since there is in theory the outside 
			// possibility of a timing problem causing a NullPointerException to be thrown.
			if (i_selector != null)
			{
				i_selector.wakeup();
			}
		}else{
			// Should never have got here - throw an Exception. This will be caught and logged
			// in the manager.
			throw new Exception("Cannot add request to thread " + i_threadName + " which is already full");
		}
	}
	
	// Called by the Request Handler to indicate that the request has finished.
	// 
	// NOTE:
	// Do not synchronize this method - it might look like it needs to be synchronized but
	// a) It doesn't - the synchronization in the thread manager class and the use of an atomic 
	// integer to store the current number of requests prevents any concurrency issues, and
	// b) If you synchronize it we will eventually end up with deadlocks between the add and 
	// remove request methods due to the synchronization on the thread manager class.
	// 
	// Thanks for listening.
	public void removeRequest()
	{		
		// If we are on max requests, tell the manager that this thread is now
		// available for new requests.
		if (i_noOfRequests.getAndDecrement() == i_maxRequests)
		{
			i_listener.notify(i_threadName,
							  Listener.C_EVENT_RESOURCE_AVAILABLE,
							  "");
			if (c_log.isDebugEnabled()) c_log.debug ("Thread " + i_threadName + " is available");
		}
		
		// If the pool has become empty, tell the manager that the thread is idle.
		if (i_noOfRequests.get() == 0)
		{
			i_listener.notify(i_threadName,
				  			  Listener.C_EVENT_RESOURCE_IDLE,
				  			  "");
			if (c_log.isDebugEnabled()) c_log.debug ("Thread " + i_threadName + " is idle");
		}
	}

	// Returns the current number of threads in the pool.
	public int getNumberOfRequests()
	{
		return i_noOfRequests.get();
	}

	// Called by the manager to shutdown the thread.
	void shutdown()
	{
		i_shutdown = true;
	} 
}