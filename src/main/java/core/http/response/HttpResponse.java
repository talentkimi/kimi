package core.http.response;

import java.io.IOException;
import core.io.StreamReader;
import core.http.HttpMessage;
import core.http.HttpTitle;
import core.http.HttpVersion;

/**
 * An HTTP Response.
 */
public class HttpResponse extends HttpMessage implements HttpResponseHeaderList, HttpResponseCodeList {

	/**
	 * Returns a new version title.
	 * @return a new version title.
	 */
	protected final HttpTitle newHttpTitle1() {
		return new HttpVersion();
	}

	/**
	 * Returns a new code title.
	 * @return a new code title.
	 */
	protected final HttpTitle newHttpTitle2() {
		return new HttpCode();
	}

	/**
	 * Returns a new reason title.
	 * @return a new reason title.
	 */
	protected final HttpTitle newHttpTitle3() {
		return new HttpReason();
	}

	/**
	 * Returns the version.
	 * @return the version.
	 */
	public HttpVersion getVersion() {
		return (HttpVersion) getTitle1();
	}

	/**
	 * Returns the code.
	 * @return the code.
	 */
	public HttpCode getCode() {
		return (HttpCode) getTitle2();
	}

	/**
	 * Returns the reason.
	 * @return the reason.
	 */
	public HttpReason getReason() {
		return (HttpReason) getTitle3();
	}

	/**
	 * Creates a new empty HTTP response.
	 */
	public HttpResponse() {
		initialiseMultibyteWrite();
	}

	public void readContent(StreamReader in) throws IOException {
		readFromContent(in);
	}
	
	/**
	 * Creates a new HTTP response with the given code and reason.
	 * @param code the code.
	 * @param reason the reason.
	 * @param version the version.
	 */
	public HttpResponse(Integer code, String reason, String version) {
		getCode().set(code);
		getReason().set(reason);
		getVersion().set(version);
		initialiseMultibyteWrite();
	}

	/**
	 * Creates a new HTTP response with the given code and reason.
	 * @param code the code.
	 * @param reason the reason.
	 */
	public HttpResponse(Integer code, String reason) {
		this(code, reason, HttpVersion.VERSION_10);
	}

	/**
	 * Creates a new HTTP response with the given code.
	 * @param code the code.
	 */
	public HttpResponse(Integer code) {
		this(code, HttpCode.getReason(code));
	}

}