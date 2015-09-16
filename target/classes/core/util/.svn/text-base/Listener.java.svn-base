package core.util;

/** Implement this interface to create a concrete Listener class. */
public interface Listener 
{
	// Useful event name type constants - feel free to add more.
	public static final int C_EVENT_STARTED = 0;
	public static final int C_EVENT_INFO = 1;
	public static final int C_EVENT_FAILURE = 2;
	public static final int C_EVENT_SUCCESSFUL_COMPLETION = 3;
	public static final int C_EVENT_RESOURCE_AVAILABLE = 4;
	public static final int C_EVENT_RESOURCE_UNAVAILABLE = 5;
	public static final int C_EVENT_RESOURCE_NOT_IDLE = 6;
	public static final int C_EVENT_RESOURCE_IDLE = 7;

	/** 
	 * Used by the Listenable class to communicate with the Listener. 
	 * @param p_objectName A String indicating which object left this message.
	 * @param p_eventType Indicates which type of event has occurred. 
	 * @param p_message Contains details of the event.
	 */
	public void notify(String p_objectName,
			   		   int    p_eventType, 
			   		   String p_message);	
}
