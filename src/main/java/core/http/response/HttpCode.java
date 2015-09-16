package core.http.response;

import core.http.HttpTitle;

/**
 * An HTTP Response Code.
 */
public class HttpCode extends HttpTitle implements HttpResponseCodeList {

	/**
	 * Sets the title.
	 * @param title the title.
	 */
	public void set(String title) {
		set(new Integer(title));
	}

	/**
	 * Sets the code.
	 * @param code the code.
	 */
	public void set(Integer code) {
		setTitle(code);
	}

	/**
	 * Returns the code as an integer.
	 * @return the code as an integer.
	 */
	public Integer getInteger() {
		return (Integer) get();
	}

	/**
	 * Returns an array of supported values.
	 * @return an array of supported values.
	 */
	public final Object[] getSupported() {
		return null;
	}

	/**
	 * Returns the reason for this code.
	 * @return the reason for this code.
	 */
	public String getReason() {
		Integer code = (Integer) get();
		return getReason(code);
	}

	/**
	 * Returns the reason for the given code.
	 * @param code the code.
	 * @return the reason.
	 */
	public static String getReason(Integer code) {
		if (code.equals(CODE_100))
			return "Continue";
		if (code.equals(CODE_101))
			return "Switching Protocols";
		if (code.equals(CODE_200))
			return "OK";
		if (code.equals(CODE_201))
			return "Created";
		if (code.equals(CODE_202))
			return "Accepted";
		if (code.equals(CODE_203))
			return "Non-Authoritative Information";
		if (code.equals(CODE_204))
			return "No Content";
		if (code.equals(CODE_205))
			return "Reset Content";
		if (code.equals(CODE_206))
			return "Partial Content";
		if (code.equals(CODE_300))
			return "Multiple Choices";
		if (code.equals(CODE_301))
			return "Moved Permanently";
		if (code.equals(CODE_302))
			return "Found";
		if (code.equals(CODE_303))
			return "See Other";
		if (code.equals(CODE_304))
			return "Not Modified";
		if (code.equals(CODE_305))
			return "Use Proxy";
		if (code.equals(CODE_306))
			return "Temporary Redirect";
		if (code.equals(CODE_400))
			return "Bad Request";
		if (code.equals(CODE_401))
			return "Unauthorized";
		if (code.equals(CODE_402))
			return "Payment Required";
		if (code.equals(CODE_403))
			return "Forbidden";
		if (code.equals(CODE_404))
			return "Not Found";
		if (code.equals(CODE_405))
			return "Method Not Allowed";
		if (code.equals(CODE_406))
			return "Not Acceptable";
		if (code.equals(CODE_407))
			return "Proxy Authentication Required";
		if (code.equals(CODE_408))
			return "Request Time-out";
		if (code.equals(CODE_409))
			return "Conflict";
		if (code.equals(CODE_410))
			return "Gone";
		if (code.equals(CODE_411))
			return "Length Required";
		if (code.equals(CODE_412))
			return "Precondition Failed";
		if (code.equals(CODE_413))
			return "Request Entity Too Large";
		if (code.equals(CODE_414))
			return "Request-URI Too Large";
		if (code.equals(CODE_415))
			return "Unsupported Media Type";
		if (code.equals(CODE_416))
			return "Requested range not satisfiable";
		if (code.equals(CODE_417))
			return "Expectation Failed";
		if (code.equals(CODE_500))
			return "Internal Server Error";
		if (code.equals(CODE_501))
			return "Not Implemented";
		if (code.equals(CODE_502))
			return "Bad Gateway";
		if (code.equals(CODE_503))
			return "Service Unavailable";
		if (code.equals(CODE_504))
			return "Gateway Time-out";
		if (code.equals(CODE_505))
			return "HTTP Version not supported";
		throw new IllegalArgumentException("Unknown HTTP response code: " + code);
	}

}