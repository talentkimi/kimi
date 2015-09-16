package engine;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.http.HttpHeader;
import core.http.HttpHeaderList;
import core.http.HttpQuery;
import core.http.request.HttpRequest;
import core.mime.MimeTypes;
import core.net.NetUrl;
import core.text.Text;

/**
 * HTTP Request Writer.
 */
class HttpRequestWriter {
	
	private static final Logger log = LoggerFactory.getLogger(HttpRequestWriter.class);
	
	/**
	 * Handle the HTTP request.
	 */
	public final String write(HttpRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}
		StringBuffer sb = new StringBuffer();

		// Host
		HttpHeaderList headerList = request.getHeaderList();
		HttpHeader hostHeader = headerList.getHeader(HttpRequest.HEADER_HOST);
		if (hostHeader == null)
			return "Missing HOST header";
		String host = hostHeader.getValue();

		// URL
		NetUrl url = request.getUrl().getNetUrl();
		String path = url.getPath();
		if (path == null) {
			path = "";
		}

		// Method
		String method = request.getMethod().toString();

		// Header line
		if (method.equals(HttpRequest.METHOD_GET)) {
			sb.append("newHttpGetRequest( \"http://" + host + path + "\" );").append("\r\n");
		} else if (method.equals(HttpRequest.METHOD_POST)) {
			sb.append("newHttpPostRequest( \"http://" + host + path + "\" );").append("\r\n");
		} else
			return "Method not allowed: \"" + method + "\"";

		// Headers
		int maxLength = 0;
		for (int i = 0; i < headerList.size(); i++) {
			HttpHeader header = headerList.getHeader(i);
			if (header.hasName(HttpRequest.HEADER_PROXY_AUTHORIZATION))
				continue;
			int length = header.getName().length();
			if (length > maxLength)
				maxLength = length;
		}
		for (int i = 0; i < headerList.size(); i++) {
			HttpHeader header = headerList.getHeader(i);
			if (header.hasName(HttpRequest.HEADER_PROXY_AUTHORIZATION))
				continue;
			String name = header.getName();
			String value = header.getValue();
			value = Text.replace(value, "\r", "\\r");
			value = Text.replace(value, "\n", "\\n");
			String padding = getPadding(name, maxLength);
			sb.append("addHttpHeader( \"" + name + "\", " + padding + "\"" + value + "\" );").append("\r\n");
		}

		// Get Fields
		if (url.getQuery() != null) {
			try {
				HttpQuery query = new HttpQuery(url.getQuery());
				maxLength = 0;
				for (int i = 0; i < query.size(); i++) {
					int length = query.getKey(i).length();
					if (length > maxLength)
						maxLength = length;
				}
				for (int i = 0; i < query.size(); i++) {
					String key = query.getKey(i);
					String value = query.getValue(i);
					value = Text.replace(value, "\r", "\\r");
					value = Text.replace(value, "\n", "\\n");
					String padding = getPadding(key, maxLength);
					sb.append("addHttpGetField( \"" + key + "\", " + padding + "\"" + value + "\" );").append("\r\n");
				}
			} catch (UnsupportedEncodingException uee) {
				if (log.isErrorEnabled()) log.error(uee.getMessage(),uee);
			}
		}

		// Post Fields
		if (request.getContent().length() > 0) {
			boolean decode = request.getHeaderList().getHeader(HttpRequest.HEADER_CONTENT_TYPE).getValue().equals(MimeTypes.APPLICATION_X_WWW_FORM_URLENCODED);
			HttpQuery query = new HttpQuery(request.getContent(), decode);
			maxLength = 0;
			for (int i = 0; i < query.size(); i++) {
				int length = query.getKey(i).length();
				if (length > maxLength)
					maxLength = length;
			}
			for (int i = 0; i < query.size(); i++) {
				String key = query.getKey(i);
				String value = query.getValue(i);
				value = Text.replace(value, "\r", "\\r");
				value = Text.replace(value, "\n", "\\n");
				String padding = getPadding(key, maxLength);
				sb.append("addHttpPostField( \"" + key + "\", " + padding + "\"" + value + "\" );").append("\r\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Returns padding for the given key.
	 * @param key the key.
	 * @param maxLength the maximum length.
	 * @return padding for the given key.
	 */
	private String getPadding(String key, int maxLength) {
		StringBuffer padding = new StringBuffer();
		for (int i = key.length(); i < maxLength; i++) {
			padding.append(' ');
		}
		return padding.toString();
	}

}
