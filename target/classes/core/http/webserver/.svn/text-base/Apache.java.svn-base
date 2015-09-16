package core.http.webserver;

import core.http.Http;
import core.http.response.HttpResponse;
import core.http.response.HttpResponseHeaderList;
import core.util.UtilDate;

/**
 * The Apache HTTP Web Server.
 */
class Apache implements HttpWebServer, Http, HttpResponseHeaderList {

	/** A Windows XP Apache 1.3.28 */
	private static final String APACHE_SERVER_WIN32 = "Apache/1.3.28 (Win32) mod_ssl/2.8.15 OpenSSL/0.9.7b";
	/** A Linux Apache 1.3.23 */
	private static final String APACHE_SERVER_LINUX = "Apache/1.3.23 (Unix) mod_ssl/2.8.7 OpenSSL/0.9.6";

	/** X Forwarded Server. The X forwarded server details. */
	private static final String HEADER_X_FORWARDED_SERVER = "X-Forwarded-Server";
	/** X Forwarded Host. The X forwarded host details. */
	private static final String HEADER_X_FORWARDED_HOST = "X-Forwarded-Host";

	/**
	 * Sets the default headers for the given response.
	 * @param response the response.
	 */
	public HttpResponse setHeaders(HttpResponse response) {

		// Headers
		UtilDate date = new UtilDate();
		String dateString = date.toString(DATE_FORMAT) + " GMT";
		response.addHeader(HEADER_DATE, dateString);
		response.addHeader(HEADER_SERVER, APACHE_SERVER_LINUX);
		response.addHeader(HEADER_CONNECTION, "close");
		return response;
	}

}