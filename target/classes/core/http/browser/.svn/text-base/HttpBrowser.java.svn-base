package core.http.browser;

import core.http.HttpVersionList;
import core.http.request.HttpRequest;
import core.http.request.HttpRequestHeaderList;

/**
 * An HTTP Browser.
 */
public abstract class HttpBrowser implements HttpRequestHeaderList, HttpVersionList {

	/**
	 * Set the host header.
	 * @param request the HTTP request.
	 */
	public final void setHostHeader(HttpRequest request, int defaultPort) {
		String host = request.getUrl().getNetUrl().getHost();
		if (host != null) {
			int port = request.getUrl().getNetUrl().getPort();
			if (port != defaultPort) {
				host = host + ':' + port;
			}
			request.addHeader(HEADER_HOST, host);
		}
	}

	/**
	 * Sets the browser dependent headers for the given request.
	 * @param request the request.
	 */
	public abstract HttpRequest setHeaders(HttpRequest request);

	/**
	 * Encodes the given string.
	 * @param toEncode the string to encode.
	 * @return the encoded string.
	 */
	public abstract String urlEncode(String toEncode, String charset);

	/**
	 * Decodes the given string.
	 * @param toDecode the string to decode.
	 * @return the decoded string.
	 */
	public abstract String urlDecode(String toDecode, String charset);

}