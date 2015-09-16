package core.http.request;

import core.http.HttpMessage;
import core.http.HttpTitle;
import core.http.HttpVersion;

/**
 * An HTTP Request.
 */
public class HttpRequest extends HttpMessage implements HttpRequestHeaderList, HttpRequestMethodList {

	/**
	 * Returns a new method title.
	 * @return a new method title.
	 */
	protected final HttpTitle newHttpTitle1() {
		return new HttpMethod();
	}

	/**
	 * Returns a new URL title.
	 * @return a new URL title.
	 */
	protected final HttpTitle newHttpTitle2() {
		return new HttpUrl();
	}

	/**
	 * Returns a new version title.
	 * @return a new version title.
	 */
	protected final HttpTitle newHttpTitle3() {
		return new HttpVersion();
	}

	/**
	 * Returns the method.
	 * @return the method.
	 */
	public HttpMethod getMethod() {
		return (HttpMethod) getTitle1();
	}

	/**
	 * Returns the version.
	 * @return the version.
	 */
	public HttpVersion getVersion() {
		return (HttpVersion) getTitle3();
	}

	/**
	 * Returns the url.
	 * @return the url.
	 */
	public HttpUrl getUrl() {
		return (HttpUrl) getTitle2();
	}

	/**
	 * Reads and returns the length of the content. Returns 0 if the length is unknown, but content is present. Returns -1 if there is no content.
	 * @return the content length.
	 */
	protected int readFromContentLength() {
		if (getMethod().equals(METHOD_POST)) {
			return super.readFromContentLength();
		} else {
			return -1;
		}
	}

	/**
	 * Creates a new empty HTTP request.
	 */
	public HttpRequest() {
		initialiseMultibyteWrite();
	}

	/**
	 * Creates a new HTTP request with the given method and url.
	 * @param method the method.
	 * @param url the url.
	 * @param version the version.
	 */
	public HttpRequest(String method, String url, String version) {
		getMethod().set(method);
		getUrl().set(url);
		getVersion().set(version);
		initialiseMultibyteWrite();
	}

	/**
	 * Creates a new HTTP request with the given method and url.
	 * @param method the method.
	 * @param url the url.
	 */
	public HttpRequest(String method, String url) {
		this(method, url, HttpVersion.VERSION_10);
	}

}