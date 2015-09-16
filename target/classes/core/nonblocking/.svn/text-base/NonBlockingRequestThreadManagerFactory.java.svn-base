package core.nonblocking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Factory class to provide singleton instance of the RouterThreadManager. */
public class NonBlockingRequestThreadManagerFactory 
{
	private static NonBlockingRequestThreadManager i_rtManager = null;
	
	private static Logger c_log = LoggerFactory.getLogger(NonBlockingRequestThreadManagerFactory.class);

	/** Default constructor - may yet do something useful. */
	public NonBlockingRequestThreadManagerFactory()
	{
		// Default constructor.
	}

	/** 
	  * Creates Singleton instance of the request thread manager. This method is synchronized to
	  * ensure that only a single instance of the Manager is created (although this should of
	  * course only be called from TripPlannerMain).
	  */
	synchronized public static void startManager() throws Exception
	{
		if (c_log.isDebugEnabled()) c_log.debug ("Starting NonBlockingRequestThreadManager");
			
		if (i_rtManager == null)
		{
			i_rtManager = new NonBlockingRequestThreadManager();
		}
	}
	/**
	 * Shuts down the request thread manager. Again this should be only be called from
	 * TripPlannerMain, this time in its death throes.
	 */
	public static void shutdownManager()
	{
		if (c_log.isDebugEnabled()) c_log.debug ("Shutting down NonBlockingRequestThreadManager");

		if (i_rtManager != null)
		{
			i_rtManager.shutdown();
		} 
	}
	
	/** Returns the singleton instance of the RouterThreadManager.*/
	public static NonBlockingRequestThreadManager getNonBlockingRequestThreadManager() throws Exception
	{
		if (i_rtManager == null)
		{
			throw new Exception("NonBlockingRequestThreadManager is unavailable");
		}

		return i_rtManager;
	}
}
