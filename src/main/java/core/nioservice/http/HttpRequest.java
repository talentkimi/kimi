package core.nioservice.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest extends HttpMessage {

	protected static final Pattern requestLine = Pattern.compile("^(GET|POST) ((https?)://([^ :]*)(:([0-9]+))?)?(/[^ ]*) .*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	private String method;
	private String protocol;
	private String host;
	private int port;
	private String path;

	public HttpRequest() {
		super();
		method = null;
		protocol = null;
		host = null;
		port = -1;
		path = null;
	}

	public String getProtocol() {
		return protocol;
	}

	public int getPort() {
		return port;
	}

	public String getPath() {
		return path;
	}

	protected void setStartLineInfo() throws HttpMsgParseException {
		if (!startLine.contains(HTTP)) {
			throw new HttpMsgParseException("HTTP protocol not specified - " + startLine);
		}
		Matcher m = requestLine.matcher(startLine);
		if (m.find()) {
			method = m.group(1);
			protocol = m.group(3);
			host = m.group(4);
			if (m.group(6) != null) {
				port = Integer.parseInt(m.group(6));
			}
			path = m.group(7);
		} else {
			throw new HttpMsgParseException("HTTP request line invalid - " + startLine);
		}
	}
}
