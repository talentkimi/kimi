package core.http.browser;

import core.http.HttpVersionList;
import core.http.request.HttpRequest;
import core.http.request.HttpRequestHeaderList;

/**
 * The FireFox Browser.
 */
public class FireFox extends HttpBrowser {

	/**
	 * Sets the default headers for the given request.
	 * @param request the request.
	 */
	public HttpRequest setHeaders(HttpRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		// Headers
		request.addHeader(HEADER_USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.8) Gecko/20050511 Firefox/1.0.4");
		request.addHeader(HEADER_ACCEPT, "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		request.addHeader(HEADER_ACCEPT_LANGUAGE, "en-us,en;q=0.5");
		request.addHeader(HEADER_ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		request.addHeader(HEADER_CACHE_CONTROL, "max-age=0");
		
		// Host
		setHostHeader(request, 80);

		// Done
		return request;
	}

	public String urlEncode(String toEncode, String charset) {
		throw new IllegalStateException("method not implemented");
	}

	public String urlDecode(String toDecode, String charset) {
		throw new IllegalStateException("method not implemented");
	}

}
