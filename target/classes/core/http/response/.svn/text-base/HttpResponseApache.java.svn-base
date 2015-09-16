package core.http.response;

import core.http.HttpHeader;
import core.util.UtilDate;

/**
 * An Apache HTTP Response.
 */
public class HttpResponseApache extends HttpResponse implements HttpResponseApacheHeaderList {

	/** A Windows XP Apache 1.3.28 **/
	private static final String APACHE_SERVER_WIN32 = "Apache/1.3.28 (Win32) mod_ssl/2.8.15 OpenSSL/0.9.7b";
	/** A Linux Apache 1.3.23 **/
	private static final String APACHE_SERVER_LINUX = "Apache/1.3.23 (Unix) mod_ssl/2.8.7 OpenSSL/0.9.6";

	/**
	 * Creates a new Apache response.
	 */
	public HttpResponseApache( Integer code, String reason ) {
		super( code, reason );

		// Headers
		UtilDate date = new UtilDate();
		String dateString = date.toString( DATE_FORMAT ) + " GMT";
		getHeaderList().add( new HttpHeader( HEADER_DATE, dateString ) );
		getHeaderList().add( new HttpHeader( HEADER_SERVER, APACHE_SERVER_LINUX ) );
		getHeaderList().add( new HttpHeader( HEADER_CONNECTION, "close" ) );
	}
	/**
	 * Creates a new Apache response.
	 */
	public HttpResponseApache( Integer code ) {
		this( code, HttpCode.getReason( code ) );
	}

}
