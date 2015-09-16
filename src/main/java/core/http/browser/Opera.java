package core.http.browser;

import core.http.HttpVersionList;
import core.http.request.HttpRequest;
import core.http.request.HttpRequestHeaderList;

/**
 * The FireFox Browser.
 */
public class Opera extends HttpBrowser {

	/**
	 * Sets the default headers for the given request.
	 * @param request the request.
	 */
	public HttpRequest setHeaders(HttpRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		// Headers
		request.addHeader(HEADER_USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 8.0");
		request.addHeader(HEADER_ACCEPT, "text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
		request.addHeader(HEADER_ACCEPT_LANGUAGE, "en");
		request.addHeader(HEADER_ACCEPT_CHARSET, "windows-1252, utf-8, utf-16, iso-8859-1;q=0.6, *;q=0.1");
		
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
