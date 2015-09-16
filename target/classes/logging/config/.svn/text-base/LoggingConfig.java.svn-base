package logging.config;

/**
 * Configuration for logging.
 * @author Dimitrijs
 *
 */
public class LoggingConfig {
	private static LoggerConfig root;
	private static int serverPort;
	private static String serverHost;
	private static String logPath;
	private static String shutdownPassword;
	private static boolean ignoreSystemVariables;
	private static int minFreeSpace;
	
	/**
	 * Configuration for root logger. Holds configurations hierarchy. 
	 * @return
	 */
	public static LoggerConfig getRoot() {
		return root;
	}

	/**
	 * Default port where logging server should listen.
	 * @return
	 */
	public static int getServerPort() {
		return serverPort;
	}

	/**
	 * Default host where logging server should listen.
	 * @return
	 */
	public static String getServerHost() {
		return serverHost;
	}

	/**
	 * Default path where log-files are stored.
	 * @return
	 */
	public static String getLogPath() {
		return logPath;
	}

	/**
	 * Shutdown password to use to shutdown logging server. 
	 * It must be the same as in logging server configuration.
	 * @return
	 */
	public static String getShutdownPassword() {
		return shutdownPassword;
	}

	public static boolean isIgnoreSystemVariables() {
		return ignoreSystemVariables;
	}
	
	/**
	 * Minimum free diskspace (mb). After reach this value logging will be stopped to store logs.
	 * @return
	 */
	public static int getMinFreeSpace() {
		return minFreeSpace;
	}
}
