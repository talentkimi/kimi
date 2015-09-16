package core.nonblocking;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/** Used to identify classes that can use non-blocking I/O functionality. */
public interface NonBlockingRequestHandler 
{
	// Note - in the below methods the exceptions thrown are mostly very generic - Exception,
	// Throwable, etc, - this is because this is what is thrown by the WebSpider methods that 
	// have to be called to execute the business logic. Not ideal but, short of a massive
	// redesign effort, what can you do?
	
	/** Used by the to kick off the request. */
	public void startRequest() throws Throwable;
	
	/** 
	 * Completes all channel closing operations plus anything that would normally have
	 * have occurred in the runStage method finally block in the WebSpider.
	 */ 
	public void finishRequest() throws Exception;
	
	/** Adds the Selector that will be used to multiplex the request. */ 
	public void register(Selector p_selector,
						 NonBlockingRequestThread p_manager) 
	throws ClosedChannelException;
	
	/** Method to handle connect to external server. */
	public void connectToServer(SelectionKey p_key) throws Exception;
	
	/** Method to read data from the Channel. */
	public void readData(SelectionKey p_key) throws Throwable;
	
	/** Method to write data to the Channel. */
	public void writeData(SelectionKey p_key) throws IOException;
	
	/** Wrappers the Task.setThrowable method, making it acessible to the manager thread. */
	public void setThrowableOnTask(Throwable p_throwable);
	
	/** Stores the "read timeout" - actually the socket timeout - used in the download. */
	public void setSocketTimeout(int p_socketTimeout);
	
	/** 
	 * Checks to see whether a read or connection timeout has occurred and takes 
	 * appropriate action.
	 */
	public void checkForTimeout();
	
	/** Stats method - marks beginning of request execution period. */
	public void startTimer();
	
	/** Stats method - marks end of request execution period. */
	public void stopTimer();
	
	/** Stats method for measuring any latency prior to starting the request.*/
	public void setRequestAdded(long p_time);
	
	/** Gets latency prior to starting request for logging in quickstats. */
	public long getRequestLatency();
}