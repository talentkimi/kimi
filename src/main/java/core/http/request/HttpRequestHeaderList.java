package core.http.request;

import core.http.HttpMessageHeaderList;

/**
 * A list of HTTP request headers.
 */
public interface HttpRequestHeaderList extends HttpMessageHeaderList {

	/** Accept. The media types allowed in the response. **/
	String HEADER_ACCEPT = "Accept";
	/** Accept Charset. The character sets allowed in the response. **/
	String HEADER_ACCEPT_CHARSET = "Accept-Charset";
	/** Accept Encoding. The encoding allowed in the response. **/
	String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	/** Accept Language. The languages allowed in the response. **/
	String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
	/** Accept Ranges. The ranges allowed in the response. **/
	String HEADER_ACCEPT_RANGES = "Accept-Ranges";

	/** Authorization. The authentication details to use a secure server. **/
	String HEADER_AUTHORIZATION = "Authorization";

	/** Cookie. The list of cookies. **/
	String HEADER_COOKIE = "Cookie";

	/** Expect. The behaviours expected of the server. **/
	String HEADER_EXPECT = "Expect";

	/** From. The email address of the request creator. **/
	String HEADER_FROM = "From";

	/** Host. The host and port number of the server. **/
	String HEADER_HOST = "Host";

	/** If Match. An entity related conditional request. **/
	String HEADER_IF_MATCH = "If-Match";
	/** If Modified Since. An HTTP date related conditional request. **/
	String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
	/** If None Match. An entity related conditional request. **/
	String HEADER_IF_NONE_MATCH = "If-None-Match";
	/** If Range. A range related conditional request. **/
	String HEADER_IF_RANGE = "If-Range";
	/** If Unmodified Since. An HTTP date related conditional request. **/
	String HEADER_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

	/** Proxy Authorization. The authentication details to use a secure proxy. **/
	String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";

	/** Referer. The URI of the previous request. **/
	String HEADER_REFERER = "Referer";

	/** TE. The transfer encodings accepted. **/
	String HEADER_TE = "TE";

	/** Upgrade. The protocols supported and would be used if the server changes. **/
	String HEADER_UPGRADE = "Upgrade";

	/** User Agent. The user agent sending the request. **/
	String HEADER_USER_AGENT = "User-Agent";

}
