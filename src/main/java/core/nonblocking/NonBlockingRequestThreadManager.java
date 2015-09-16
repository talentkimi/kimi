package core.nonblocking;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.ClosableHandler;
import core.util.Listener;

/**
 * Creates and manages a pool of non-blocking request threads that handle, at least initially,
 * plane searches. With luck this will be extended to handle other thread-based operations  
 * with external I/O dependencies. 
 * 
 * Initially create a statically-sized pool since this is the easiest thing to manage, and 
 * allocate new requests to the first available thread in the pool, since we want to fill threads 
 * up to multiplex, rather than have a few requests running in many threads. This, in and of itself,
 * is a clear argument for dynamic pool-sizing but one step at a time to start with.
 *
 * Have left the option to convert to a round-robin strategy via properties.
 */

public class NonBlockingRequestThreadManager 
{
	private static Logger c_log = LoggerFactory.getLogger(NonBlockingRequestThreadManager.class);
	
	// So what kind of an object do we want to store the threads on? Bear in
	// mind that we have an unusual pool-handling strategy in which we want to 
	// load to maximum the minimum number of threads and leave as many as possible 
	// free. Start with a private object that allows us to do lots of stuff...
	private ThreadPool i_threadPool;
	
	/** 
	 * A thread to manage the pool, removing idle connections and taking snapshots
	 * statistics of pool size and number of requests.
	 */
	private ThreadPoolManager i_threadPoolManager;
	
	/** Configurable properties file for the non-blocking manager and threads. */
	private Properties i_properties = new Properties();
	
	/** The fully qualified class name used to qualify the properties. */
	private static String c_className = "core.nonblocking.NonBlockingRequestThreadManager";
	
	/** 
	 * Flag indicating whether the request allocation strategy is:
	 * <ul>
	 * 	<li>First Available (true) - add new requests to 1st available thread found. Dynamically
	 * 	grows and shrinks the threadPool to meet demand.
	 * 	<li>Round Robin (false) - distributes new requests evenly across a fixed number of
	 * 	available threads.
	 * </ul>   
	 */
	private boolean i_isFirstAvailable = true;
	
	
	public NonBlockingRequestThreadManager() throws Exception
	{
		super();
		init();
	}

	/** Creates and starts up the thread pool. */
	private void init() throws Exception
	{
		int l_minPoolSize;
		int l_maxPoolSize;
		int l_maxRequests;
		int l_selectorTimeout;
		int l_idleTimeout;
		int l_checkInterval;
		
		FileInputStream fis = null;
		// Get pool-size properties from the properties file.
		try {
			// Load the properties from the configuration file.
			fis = new FileInputStream(new File("java/config/properties/nonblocking.properties"));
			i_properties.load(fis);
		} catch (IOException l_e) {
			if (c_log.isErrorEnabled()) c_log.error(l_e.getMessage(), l_e);	
			throw l_e;
		} finally {
			ClosableHandler.closeSafely(fis);
		}

		// Set the minimum number of threads to be managed.
		l_minPoolSize = Integer.parseInt(i_properties.getProperty(c_className + ".minPoolSize", "5"));
		if (c_log.isDebugEnabled()) c_log.debug ("Minimum pool size set to " + l_minPoolSize);
		
		// Set the maximum number of threads to be managed - will not be used until dynamic pool-sizing
		// is introduced.
		l_maxPoolSize = Integer.parseInt(i_properties.getProperty(c_className + ".maxPoolSize", "10"));
		if (c_log.isDebugEnabled()) c_log.debug ("Maximum pool size set to " + l_maxPoolSize);

		// Set the request allocation strategy to either First Available (default) or Round Robin.
		i_isFirstAvailable = i_properties.getProperty(c_className + ".isFirstAvailable", "true").equals("true");
		if (c_log.isDebugEnabled()) c_log.debug ("Request allocation strategy set to " + 
												(i_isFirstAvailable ? "First Available" : "Round Robin"));

		// Set the maximum number of simultaneous requests that this thread can process.
		l_maxRequests = Integer.parseInt(i_properties.getProperty(c_className + ".maxRequests", "10"));
		if (c_log.isDebugEnabled()) c_log.debug ("Maximum requests per thread set to " + l_maxRequests);
		
		// Set the timeout for the select operation on the selector if no events occur. Default to 5 seconds.
		l_selectorTimeout = Integer.parseInt(i_properties.getProperty(c_className + ".selectorTimeout", "5000"));
		if (c_log.isDebugEnabled()) c_log.debug ("Selector Timeout set to " + l_selectorTimeout);
		
		// The timeout after which idle threads may be removed from the pool.
		l_idleTimeout = Integer.parseInt(i_properties.getProperty(c_className + ".idleTimeout", "5000"));
		if (c_log.isDebugEnabled()) c_log.debug ("Idle Timeout set to " + l_idleTimeout);
		
		// The pause between manager thread checks for idle connections. 
		l_checkInterval = Integer.parseInt(i_properties.getProperty(c_className + ".checkInterval", "10000"));
		if (c_log.isDebugEnabled()) c_log.debug ("Manager check interval set to " + l_checkInterval);
	
		// Create the thread pool from the properties values.
		i_threadPool = new ThreadPool(l_minPoolSize,
									  l_maxPoolSize,
									  l_maxRequests,
									  l_selectorTimeout,
									  l_idleTimeout);
		
		// Create and start the pool management thread.
		i_threadPoolManager = new ThreadPoolManager(l_checkInterval);
		i_threadPoolManager.start();
	}
	
	/** Adds the request to the thread pool. */
	public void addRequest(NonBlockingRequestHandler p_request) throws Exception
	{
		if (c_log.isDebugEnabled()) c_log.debug ("In NBRequestThreadManager.addRequest()");
		try{
			// Set the current time on the thread to log any latency prior to starting execution.
			p_request.setRequestAdded(System.currentTimeMillis());
			// Then fire it into the pool.
			i_threadPool.addRequest(p_request);
		}catch (Exception l_e){
			if (c_log.isErrorEnabled()) c_log.error(l_e.getMessage(), l_e);
//			QuickStatsEngine2.engine.MISC_ERRORS.logEvent("Error adding request to NIO Pool: " + l_e);
			throw l_e;
		}
 	}
	
	/** Shuts down the Thread pool and pool management threads */
	public void shutdown()
	{
		if (i_threadPool != null)
		{
			i_threadPool.shutdown();
		}
		
		if (i_threadPoolManager != null)
		{
			i_threadPoolManager.shutdown();
		}
	}

	/** Used by GetThreadSnapshot command to query the NIO thread pool. */
	public boolean containsThread(Thread p_thread)
	{		
		return i_threadPool.containsThread(p_thread);
	}

	/** 
	 * Inner class to handle pool management functionality, removing idle threads from the 
	 * pool and taking regular snapshots of pool size.
	 */
	
	class ThreadPoolManager extends Thread
	{
		// The pause interval between checks for idle connections.
		private int i_checkInterval;
		
		private boolean i_shutdown = false;
		
		ThreadPoolManager(int p_checkInterval)
		{
			i_checkInterval = p_checkInterval;
		}
		
		// Checks for idle threads and removes them from the pool, also takes a snapshot of
		// the current poolsize and number of requests being processed.
		public void run()
		{
			while (!i_shutdown)
			{
				try{
					sleep(i_checkInterval);
				}catch (InterruptedException l_ie){
					// Discard this exception - we have no interest in it.
				}
				// Check for idle threads
				i_threadPool.removeIdleThreads();
				
				// Logs a snapshot of current pool size and number of requests.
//				if (ScheduledQuickStatsLogger.quickStatsLoggingIsEnabled) i_threadPool.logStats();
			}
		}
		
		// Shuts the thread down.
		public void shutdown()
		{
			i_shutdown = true;
		}		
	}

	// Stores a bunch of ThreadState objects in a Vector to allow them to be both
	// retrieved by Thread Number (effectively a keyed lookup) or in order to find
	// the appropriate Thread to allocate a request to.
	//
	// As and when we handle dynamic pool-sizing, new elements will be added to the
	// end of the Vector at the i_noOfThreads element and only the last element may 
	// be removed if idle, implementing the KISS design principle.
	class ThreadPool implements Listener
	{		
		/** The minimum number of non-blocking threads in the pool. */
		private int i_minPoolSize;
		
		/** The maximum number of non-blocking threads in the pool. */
		private int i_maxPoolSize; 

		/** The maximum number of simultaneous requests per thread. */
		private int i_maxRequests;
		
		/** The timeout for the select operation on the selector if no events occur. */
		private int i_selectorTimeout;
		
		/** The period of time after which an idle thread may be removed from the pool. */
		private int i_idleTimeout;
		
		/** The current number of threads in the pool. */
		private volatile int i_noOfThreads = 0;
		
		/** The next thread to attempt to allocate to if using round-robin allocation. */
		private int i_nextThread = 0;
		
		/** Indicates whether thread pool has been told to close. */
		private boolean i_shutdown = false;
		
		/** Vector holding ThreadState objects. */
		private Vector<ThreadState> i_threads;
		
		/**
		 * Constructor - creates a new ThreadPool with minPoolSize initial threads
		 *
		 * @param p_minPoolSize	min number of non-blocking threads in the pool.
		 * @param p_maxPoolSize	max number of non-blocking threads in the pool.
		 * @param p_maxRequests	max number of simultaneous requests per thread.
		 * @param p_selectorTimeout timeout for the select operation on the selector if no events occur.
		 * @param p_idleTimeout period of time after which an idle thread may be removed from the pool.
		 */
		ThreadPool(int p_minPoolSize,
		 		   int p_maxPoolSize,
				   int p_maxRequests,
				   int p_selectorTimeout,
				   int p_idleTimeout)
		{
			i_minPoolSize = p_minPoolSize;
			i_maxPoolSize = p_maxPoolSize;
			i_maxRequests = p_maxRequests;
			i_selectorTimeout = p_selectorTimeout;
			i_idleTimeout = p_idleTimeout;
			
			i_threads = new Vector<ThreadState>(i_maxPoolSize);
			
			for (int i = 0; i < i_minPoolSize; i++)
			{
				addThread(i);
			}	
		}
		
		/** 
		 * Creates a new request handling thread and adds it to the pool.
		 * @param p_threadNumber index of this thread in the pool
		 */
		private void addThread(int p_threadNumber)
		{
			if (!i_shutdown)
			{
				NonBlockingRequestThread l_thread = new NonBlockingRequestThread(p_threadNumber,
					 															 i_maxRequests,
					 															 i_selectorTimeout);
				l_thread.register(this);
				l_thread.start();
				i_threads.add(new ThreadState(l_thread));
				i_noOfThreads++;
			}
		}
	
		/**
		 * Adds the request to a thread based either on first available thread or round robin
		 * allocation approach, depending on configuration. Synchronised with the notify method
		 * to ensure that the availability of a thread at the point of request allocation is
		 * accurately represented.
		 */
		synchronized public void addRequest(NonBlockingRequestHandler p_request) throws Exception
		{
			boolean l_addedRequest = false;
			
			if (!i_shutdown)
			{
				if (i_isFirstAvailable)
				{
					// Need to add request to first available Thread in Vector.
					for (int i = 0; i < i_noOfThreads; i++)
					{
						if (i_threads.get(i).isAvailable())
						{
							i_threads.get(i).getThread().addRequest(p_request);
							l_addedRequest = true;
							break;
						}
					}
				}else{
					// Use round robin approach instead...			
					for (int i = 0; i < i_noOfThreads && !l_addedRequest; i++)
					{
						if (i_threads.get(i_nextThread).isAvailable())
						{
							i_threads.get(i_nextThread).getThread().addRequest(p_request);
							l_addedRequest = true;
						}
						
						if (++i_nextThread == i_noOfThreads)
						{
							i_nextThread = 0;
						}
					}
				}
			}
			
			// We either need another thread or we throw an exception.			
			if (!l_addedRequest)
			{
				// Only implement dynamic pool-sizing if we are using the First Available approach.
				// Under round robin the pool will rarely shrink back from max size.
				if ((i_noOfThreads < i_maxPoolSize) && (i_isFirstAvailable))
				{
					// Add a new thread to the pool and recursively call this method.
					addThread(i_noOfThreads);
					addRequest(p_request);
				}else{
					throw new Exception("Could not add request to pool - no available thread found.");
				}
			}
		}
		
		/** 
		 * Removes threads that have been sitting idle in the pool for longer than i_idleTimeout.
		 */
		synchronized public void removeIdleThreads()
		{
			if (i_isFirstAvailable)
			{
				// Can remove idle threads starting at the end of the Vector.
				while ((i_noOfThreads > i_minPoolSize) && i_threads.lastElement().canBeRemoved())
				{
					i_threads.lastElement().getThread().shutdown();
					i_threads.removeElementAt(--i_noOfThreads);
				}
			}else{
				// Round robin - dynamic pool sizing is of limited value so 
				// don't implement it at the moment.
			}
			
		}
		
		/** 
		 * Notifies thread pool of changes in individual thread states.
		 */
		synchronized public void notify(String p_threadName,
				   		   				int p_event,
				   		   				String p_message)
		{
			try{
				// ThreadName corresponds to index in i_threads Vector.
				int l_threadNumber = Integer.parseInt(p_threadName);
				
				switch (p_event)  
				{
					case Listener.C_EVENT_RESOURCE_AVAILABLE:
						i_threads.get(l_threadNumber).setIsAvailable(true);
						break;
						
					case Listener.C_EVENT_RESOURCE_UNAVAILABLE:
						i_threads.get(l_threadNumber).setIsAvailable(false);
						break;
					
					case Listener.C_EVENT_RESOURCE_NOT_IDLE:
						i_threads.get(l_threadNumber).setIsIdle(false);
						break;				
					
					case Listener.C_EVENT_RESOURCE_IDLE:
						i_threads.get(l_threadNumber).setIsIdle(true);
						break;

					default:
						throw new Exception("Unknown thread state change notified:" +
								"\np_event=" + p_event + 
								"\np_threadName=" + p_threadName + 
								"\np_message=" + p_message);
				}
			}catch (Exception l_e){
				// If we end up here it's a programming error. if (log.isErrorEnabled()) log.error and continue.
				if (c_log.isErrorEnabled()) c_log.error(l_e.getMessage(), l_e);	
			}
		}
		
		
		/** Used by GetThreadSnapshot command to query the NIO thread pool. */
		public boolean containsThread(Thread thread)
		{
			boolean containsThread = false;
			
			for (ThreadState threadState : i_threads)
			{
				if (threadState.getThread().equals(thread))
				{
					containsThread = true;
					continue;
				}
			}

			return containsThread;
		}
		
		private void logStats()
		{
//			if (!ScheduledQuickStatsLogger.quickStatsLoggingIsEnabled) return;
			int l_noOfRequests = 0;
			
			for (int i = 0; i < i_noOfThreads; i++)
			{
				try{
					l_noOfRequests += i_threads.get(i).getThread().getNumberOfRequests();
				}catch (Exception l_e){
					// Possible in theory to get a runtime exception at this point
					// but it won't affect the validity of the stats and is, in any case,
					// massively unimportant.
				}
			}		
			if (c_log.isDebugEnabled()) c_log.debug ("Number of threads=" + i_noOfThreads +
													" Number of requests=" + l_noOfRequests);
//			QuickStatsEngine2.engine.NUMBER_OF_NON_BLOCKING_THREADS.logEvent(i_noOfThreads);
//			QuickStatsEngine2.engine.NUMBER_OF_NON_BLOCKING_REQUESTS.logEvent(l_noOfRequests);			
		}
	
		public synchronized void shutdown()
		{
			if (c_log.isDebugEnabled()) c_log.debug ("Shutting down thread pool");

			i_shutdown = true;
			
			for (int i = 0; i < i_threads.size(); i++)
			{
				try{
					i_threads.get(i).getThread().shutdown();
				}catch (Exception l_e){
					// Possible in theopry to get a runtime exception at this point
					// but we don't want to synchronise the shutdown method.
					if (c_log.isErrorEnabled()) c_log.error(l_e.getMessage(), l_e);	
				}				
			}
		}
		
		/** 
		 * Inner class within threadPool. Stores salient details of the state of the Thread
		 * and a reference to the Thread itself. 
		 */
		class ThreadState
		{
			private NonBlockingRequestThread i_thread;
			private boolean i_isAvailable;
			
			// These will be used if we implement dynamic pool sizing.
			private boolean i_isIdle;
			private long i_lastBusyDateTime;
			
			public ThreadState(NonBlockingRequestThread p_thread)
			{
				i_thread = p_thread;
				i_isAvailable = true;
				
				// These will be used if we implement dynamic pool sizing.
				i_isIdle = true;
				i_lastBusyDateTime = System.currentTimeMillis();
			}
			
			public NonBlockingRequestThread getThread()
			{
				return i_thread;
			}
			
			public boolean isAvailable()
			{
				return i_isAvailable;
			}
			
			public void setIsAvailable(boolean p_isAvailable)
			{
				i_isAvailable = p_isAvailable;
			}

			public void setIsIdle(boolean p_isIdle)
			{
				i_isIdle = p_isIdle;
				i_lastBusyDateTime = System.currentTimeMillis();
			}
			
			public boolean canBeRemoved()
			{
				return(i_isIdle && (System.currentTimeMillis() > (i_lastBusyDateTime + i_idleTimeout)));
			}
		}
	}
}
