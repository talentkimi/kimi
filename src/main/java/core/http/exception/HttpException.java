package core.http.exception;

import core.http.response.HttpCode;
import core.http.response.HttpResponseCodeList;
import core.mime.MimeTypes;

/**
 * An HTTP Exception.
 */
public class HttpException extends Exception implements MimeTypes, HttpResponseCodeList {

	/** The response code. * */
	private final Integer code;
	/** The response reason. * */
	private final String reason;
	/** The response content type. * */
	private final String contentType;

	/**
	 * Returns the code.
	 * @return the code.
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * Returns the reason.
	 * @return the reason.
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Returns the content type.
	 * @return the content type.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Gets the content message.
	 * @param content the content.
	 * @return the content message.
	 */
	private static String getContentMessage(Object content) {
		if (content == null)
			return null;
		return content.toString();
	}

	/**
	 * Creates a new HTTP exception
	 * @param code the code.
	 * @param reason the reason.
	 * @param content the content.
	 * @param contentType the content type.
	 */
	public HttpException(Integer code, String reason, Object content, String contentType) {
		super(getContentMessage(content));
		if (code == null) {
			throw new NullPointerException();
		}
		if (reason == null) {
			reason = HttpCode.getReason(code);
		}
		if (contentType == null) {
			contentType = TEXT_PLAIN;
		}
		this.code = code;
		this.reason = reason;
		this.contentType = contentType;
	}

	/**
	 * Returns the content.
	 * @return the content.
	 */
	public String getContent() {
		return getMessage();
	}

	public boolean hasContent() {
		return getContent() != null;
	}

}