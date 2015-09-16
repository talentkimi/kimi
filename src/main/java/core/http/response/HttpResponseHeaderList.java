package core.http.response;

import core.http.HttpMessageHeaderList;

/**
 * A list of HTTP response headers.
 */
public interface HttpResponseHeaderList extends HttpMessageHeaderList {

	/** Age. The number of seconds since the response was generated. **/
	String HEADER_AGE = "Age"; 

	/** Allow. The methods allowed in a request. **/
	String HEADER_ALLOW = "Allow";

	/** Content Range. The range for partial responses. **/
	String HEADER_CONTENT_RANGE = "Content-Range";

	/** Etag. The entity tag used to uniquely identify a response. **/
	String HEADER_ETAG = "ETag";

	/** Expires. The time after which the response is considered stale. **/
	String HEADER_EXPIRES = "Expires";

	/** Last Modified. The HTTP date at which the content was last modified. **/
	String HEADER_LAST_MODIFIED = "Last-Modified";

	/** Location. The URI to redirect the next request to. **/
	String HEADER_LOCATION = "Location";

	/** Location. Wait for a number of seconds then redirect. **/
	String HEADER_REFRESH = "Refresh";

	/** Pragma. The conditions imposed on the content (e.g. no-cache). **/
	String HEADER_PRAGMA = "Pragma";

	/** Range. The range of bytes in the response. **/
	String HEADER_RANGE = "Range";

	/** Retry After. The HTTP date or seconds after which a retry can be made. **/
	String HEADER_RETRY_AFTER = "Retry-After";

	/** Server. The server details. **/
	String HEADER_SERVER = "Server";

	/** Set Cookie. A cookie to set. **/
	String HEADER_SET_COOKIE = "Set-Cookie";

	/** Vary. The request header fields that determine if a cache may respond with this response rather than pass it on to the server. **/
	String HEADER_VARY = "Vary";

	/** WWW Authenticate. The request was unauthorized.  **/
	String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";

}
