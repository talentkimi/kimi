package core.http.request;

import core.http.HttpTitle;

/**
 * An HTTP Request Method.
 */
public final class HttpMethod extends HttpTitle implements HttpRequestMethodList {

	/**
	 * Sets the title.
	 * @param title the title.
	 */
	public void set( String title ) {
		setTitle( title );
	}

	/** The supported methods. **/
	private String[] SUPPORTED = {
		METHOD_GET, METHOD_POST, METHOD_CONNECT
	};

	/**
	 * Returns an array of supported values.
	 * @return an array of supported values.
	 */
	public final Object[] getSupported() {
		return SUPPORTED;
	}

}
