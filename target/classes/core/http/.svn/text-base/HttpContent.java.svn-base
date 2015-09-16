package core.http;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.text.Charsets;

/**
 * HTTP Message Content.
 */
public class HttpContent implements HttpQueryContainer {
	
	private static final Logger log = LoggerFactory.getLogger(HttpContent.class);
	
	/** The content. * */
	private byte[] content = new byte[0];
	/** The character set. */
	private String charset = Charsets.HTTP;

	/**
	 * Clear this content.
	 */
	public final void clear() {
		content = new byte[0];
	}

	/**
	 * Returns the charset.
	 * @return the charset.
	 */
	public final String getCharset() {
		return charset;
	}

	/**
	 * The character set.
	 * @param charset the character set.
	 */
	public final void setCharset(String charset) {
		if (charset == null) {
			throw new NullPointerException();
		}
		this.charset = charset;
	}

	/**
	 * Returns the length of the content.
	 * @return the length of the content.
	 */
	public int length() {
		return content.length;
	}

	/**
	 * Returns a byte array representation of this content.
	 * @return a byte array representation of this content.
	 */
	public byte[] toByteArray() {
		return content;
	}

	/**
	 * Returns a string representation of this content.
	 * @return a string representation of this content.
	 */
	public String toString() {
		try {
			return new String(content, charset);
		} catch (UnsupportedEncodingException e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
			return new String(content);
		}
	}

	/**
	 * Sets the content.
	 * @param content the content.
	 */
	public void set(byte[] content) {
		if (content == null) {
			throw new NullPointerException();
		}
		this.content = content;
	}

	/**
	 * Sets the content.
	 * @param content the content.
	 */
	public void set(String content, String charset) throws UnsupportedEncodingException {
		setCharset(charset);
		set(content.getBytes(charset));
	}

	/**
	 * Sets the content.
	 * @param content the content.
	 */
	public void set(String content) throws UnsupportedEncodingException {
		set(content.getBytes(charset));
	}

	/**
	 * Set the query in the container.
	 * @param query the query.
	 */
	public void setQuery(String query) {
		try {
			set(query, Charsets.HTTP);
		} catch (UnsupportedEncodingException uee) {
			if (log.isErrorEnabled()) log.error(uee.getMessage(),uee);
		}
	}

	/**
	 * Returns the query in the container.
	 * @return the query in the container.
	 */
	public String getQuery() {
		return toString();
	}

}