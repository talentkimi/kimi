package core.http.request;

import core.http.HttpHeader;

/**
 * An Internet Explorer HTTP Request.
 */
public class HttpRequestExplorer extends HttpRequest {

	/**
	 * Creates a new Internet Explorer HTTP request.
	 * @param method the method.
	 * @param url the url.
	 */
	public HttpRequestExplorer( String method, String url ) {
		super( method, url );

		// Headers
		getHeaderList().add( new HttpHeader( HEADER_ACCEPT, "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg" ) );
		getHeaderList().add( new HttpHeader( HEADER_ACCEPT_LANGUAGE, "en-gb" ) );
		getHeaderList().add( new HttpHeader( HEADER_USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)" ) );

		// Host
		String host = getUrl().getNetUrl().getHost();
		if( host != null ) {
			int port = getUrl().getNetUrl().getPort();
			if( port != 80 ) host = host + ':' + port;
			getHeaderList().add( new HttpHeader( HEADER_HOST, host ) );
		}
	}

	/**
	 * Creates a new Internet Explorer HTTP request.
	 * @param url the url.
	 */
	public HttpRequestExplorer( String url ) {
		this( METHOD_GET, url );
	}
	
}
