package core.http.response;

import core.http.HttpTitle;

/**
 * An HTTP Response Reason.
 */
public class HttpReason extends HttpTitle {

	/**
	 * Sets the title.
	 * @param title the title.
	 */
	public void set( String title ) {
		setTitle( title );
	}

	/**
	 * Returns an array of supported values.
	 * @return an array of supported values.
	 */
	public final Object[] getSupported() {
		return null;
	}

}
