package core.http.webserver;

/**
 * An HTTP Server Factory.
 */
public abstract class HttpWebServerFactory {

	/** The default factory. */
	private static final ApacheFactory defaultFactory = new ApacheFactory();

	/**
	 * Returns the default server factory.
	 * @return the default server factory.
	 */
	public static HttpWebServerFactory getDefault() {
		return defaultFactory;
	}

	/**
	 * Creates a new HTTP server factory.
	 */
	protected HttpWebServerFactory() {
	}

	/**
	 * Creates a new server.
	 * @return a new server.
	 */
	public abstract HttpWebServer getServer();

}