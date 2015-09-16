package core.http.browser;

/**
 * An HTTP Browser Factory.
 */
public abstract class HttpBrowserFactory {

	/** The default factory. */
	private static final InternetExplorerFactory defaultFactory = new InternetExplorerFactory();

	/**
	 * Returns the default browser factory.
	 * @return the default browser factory.
	 */
	public static HttpBrowserFactory getDefault() {
		return defaultFactory;
	}

	/**
	 * Creates a new HTTP browser factory.
	 */
	protected HttpBrowserFactory() {
	}

	/**
	 * Creates a new browser.
	 * @return a new browser.
	 */
	public abstract HttpBrowser getBrowser();

}