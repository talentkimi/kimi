package core.http;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import core.http.browser.HttpBrowserFactory;
import core.text.Charsets;
import core.util.PairList;

/**
 * An HTTP Query String.
 */
public final class HttpQuery implements Closeable {

	/** The pairs. * */
	private final PairList pairList = new PairList();
	/** The character set. */
	private String charset = Charsets.ISO_8859_1;

	/**
	 * Sets the character set.
	 * @param charset the charset.
	 */
	public final void setCharset(String charset) {
		if (charset == null) {
			throw new NullPointerException();
		}
		this.charset = charset;
	}

	/**
	 * Close this query.
	 */
	public void close() {
		pairList.clear();
	}

	/**
	 * Returns the size of this query.
	 */
	public int size() {
		return pairList.size();
	}

	/**
	 * Returns the key at the given index.
	 * @param index the index.
	 * @return the key at the given index.
	 */
	public String getKey(int index) {
		return pairList.getKey(index).toString();
	}

	/**
	 * Returns the value at the given index.
	 * @param index the index.
	 * @return the value at the given index.
	 */
	public String getValue(int index) {
		return pairList.getValue(index).toString();
	}

	/**
	 * Adds the given key-value pair to the query.
	 * @param key the key.
	 * @param value the value.
	 */
	public void add(String key, String value) {
		pairList.addPair(key, value);
	}

	/**
	 * Returns true if the key is contained in this query.
	 * @param key the key.
	 * @return true if the key is contained in this query.
	 */
	public boolean containsKey(String key) {
		return pairList.getValue(key) != null;
	}

	/**
	 * Returns the first value of the given key.
	 * @param key the key.
	 * @return the first value of the given key.
	 */
	public String getValue(String key) {
		String value = (String) pairList.getValue(key);
		return value;
	}

	/**
	 * Creates a new query from the given container.
	 */
	public HttpQuery() {
	}

	/**
	 * Creates a new query from the given string .
	 * @param query the query string.
	 */
	public HttpQuery(String query) throws UnsupportedEncodingException {
		parseFrom(query, true);
	}
	
	/**
	 * Creates a new query from the given parameter map .
	 * @param parameters the parameter map.
	 */
	public HttpQuery(Map<String, String> parameters) {
		if (parameters == null) {
			throw new NullPointerException();
		}
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			pairList.addPair(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Creates a new query from the given container.
	 * @param container the container.
	 */
	public HttpQuery(HttpQueryContainer container) {
		this(container, Charsets.ISO_8859_1, true);
	}

	/**
	 * Creates a new query from the given container.
	 * @param container the container.
	 */
	public HttpQuery(HttpQueryContainer container, boolean decode) {
		this(container, Charsets.ISO_8859_1, decode);
	}

	/**
	 * Creates a new query from the given container.
	 * @param container the container.
	 */
	public HttpQuery(HttpQueryContainer container, String charset) {
		this(container, charset, true);
	}

	/**
	 * Creates a new query from the given container.
	 * @param container the container.
	 */
	public HttpQuery(HttpQueryContainer container, String charset, boolean decode) {
		setCharset(charset);
		parseFrom(container, decode);
	}

	/**
	 * Returns a string representation of this query.
	 * @param pairSeparator the pair separator string.
	 * @param keyValueSeparator the key-value separator string.
	 */
	public String toString(String pairSeparator, String keyValueSeparator) {
		if (pairList.isEmpty())
			return "";
		StringBuilder query = new StringBuilder();
		for (int i = 0; i < pairList.size(); i++) {
			if (i > 0)
				query.append(pairSeparator);
			String key = (String) pairList.getKey(i);
			String value = (String) pairList.getValue(i);
			key = HttpBrowserFactory.getDefault().getBrowser().urlEncode(key, charset);
			value = HttpBrowserFactory.getDefault().getBrowser().urlEncode(value, charset);
			query.append(key);
			query.append(keyValueSeparator);
			query.append(value);
		}
		return query.toString();
	}

	/**
	 * Returns a string representation of this query.
	 * @param pairSeparator the pair separator string.
	 */
	public String toString(String pairSeparator) {
		return toString(pairSeparator, "=");
	}

	/**
	 * Returns a string representation of this query.
	 */
	public String toString() {
		return toString("&", "=");
	}

	/**
	 * Parses the given container into the query.
	 * @param container the container.
	 */
	public void parseFrom(HttpQueryContainer container, boolean decode) {
		String query = container.getQuery();
		parseFrom(query, decode);
	}

	/**
	 * Parses the given string into the query.
	 * @param query the query string.
	 */
	public void parseFrom(String query, boolean decode) {
		query = query.trim();
		if (query.length() == 0)
			return;
		int indexBegin = 0;
		int indexEnd = 0;
		while (true) {
			indexEnd = query.indexOf('=', indexBegin);
			if (indexEnd == -1) {
				String key = new String(query.substring(indexBegin, query.length()));
				pairList.addPair(key, "");
				break;
			}
			String key = query.substring(indexBegin, indexEnd);
			if (decode) {
				key = HttpBrowserFactory.getDefault().getBrowser().urlDecode(key, charset);
			}
			indexBegin = indexEnd + 1;
			indexEnd = query.indexOf('&', indexBegin);
			if (indexEnd == -1)
				indexEnd = query.length();
			String value = query.substring(indexBegin, indexEnd);
			if (decode) {
				value = HttpBrowserFactory.getDefault().getBrowser().urlDecode(value, charset);
			}
			pairList.addPair(key, value);
			if (indexEnd == query.length())
				break;
			indexBegin = indexEnd + 1;
		}
	}

}
