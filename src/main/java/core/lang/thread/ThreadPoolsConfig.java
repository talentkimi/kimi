package core.lang.thread;

/**
 * @see ThreadManager
 */
public class ThreadPoolsConfig {

	/*
	 * Configuration for MAIN thread pool.
	 */
	private static boolean mainEnabledOnStartup = true;
	private static int mainCorePoolSize = 50;
	private static int mainMaxPoolSize = 250;
	private static int mainKeepAliveTimeMillis = 3000;
	private static int mainQueueSize = 100;

	/*
	 * Configuration for SCHEDULED thread pool.
	 */
	private static boolean scheduledEnabledOnStartup = true;
	private static int scheduledPoolSize = 15;

	/*
	 * Configuration for QUEUED thread pool.
	 */
	private static boolean queuedEnabledOnStartup = true;
	private static int queuedCorePoolSize = 30;
	private static int queuedMaxPoolSize = 40;
	private static int queuedKeepAliveTimeMinutes = 2;
	private static int queuedQueueSize = 1000;

	// getters

	public static boolean isMainEnabledOnStartup() {
		return mainEnabledOnStartup;
	}

	public static int getMainCorePoolSize() {
		return mainCorePoolSize;
	}

	public static int getMainMaxPoolSize() {
		return mainMaxPoolSize;
	}

	public static int getMainKeepAliveTimeMillis() {
		return mainKeepAliveTimeMillis;
	}

	public static int getMainQueueSize() {
		return mainQueueSize;
	}

	public static boolean isScheduledEnabledOnStartup() {
		return scheduledEnabledOnStartup;
	}

	public static int getScheduledPoolSize() {
		return scheduledPoolSize;
	}

	public static boolean isQueuedEnabledOnStartup() {
		return queuedEnabledOnStartup;
	}

	public static int getQueuedCorePoolSize() {
		return queuedCorePoolSize;
	}

	public static int getQueuedMaxPoolSize() {
		return queuedMaxPoolSize;
	}

	public static int getQueuedKeepAliveTimeMinutes() {
		return queuedKeepAliveTimeMinutes;
	}

	public static int getQueuedQueueSize() {
		return queuedQueueSize;
	}
}
