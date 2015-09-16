package engine;

/**
 * A WebSpider Config.
 */
public final class WebSpiderConfig {

	/** Debug enabled? */
	private static boolean debugEnabled = true;
	/** File writing enabled? */
	private static boolean fileWritingDisabled = false;
	/** The logs directory. */
	private static String logsDirectory = "logs/webspider/";
	/** Download from file enabled. */
	private static boolean downloadFromFileEnabled = false;
	/** Enable GZIP. */
	private static boolean gzipEnabled = false;
	/** the number of redirects webspider is allowed to follow for a single request */
	private static int maxRedirectsAllowed = 20;

	/**
	 * Returns true if GZIP is enabled.
	 * @return true if GZIP is enabled.
	 */
	public static boolean isGzipEnabled() {
		return gzipEnabled;
	}

	/**
	 * Returns the maximum redirects webspider is allowed to follow for a single request
	 * @return the number of redirects webspider is allowed to follow for a single request
	 */
	public static int getMaxRedirectsAllowed() {
		return maxRedirectsAllowed;
	}

	/**
	 * Returns true if debug is enabled.
	 * @return true if debug is enabled.
	 */
	public static boolean isDebugEnabled() {
		return debugEnabled;
	}

	/**
	 * Returns the logs directory.
	 * @return the logs directory.
	 */
	public static String getLogsDirectory() {
		return logsDirectory;
	}

	/**
	 * Returns true if file writing is disabled
	 * @return true if file writing is disabled
	 */
	public static boolean isFileWritingDisabled() {
		return fileWritingDisabled;
	}

	/**
	 * Returns true if download from file enabled.
	 * @return true if download from file enabled.
	 */
	public static boolean isDownloadFromFileEnabled() {
		return downloadFromFileEnabled;
	}
}