package core.nioservice.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpResponse extends HttpMessage {

	protected static final Pattern statusLine = Pattern.compile("^(HTTP.*) (\\d{3}) (.*$)", Pattern.DOTALL);

	private int statusCode;
	private String reasonPhrase;

	public HttpResponse() {
		super();
		statusCode = -1;
		reasonPhrase = null;
	}

	@Override
	protected void setStartLineInfo() throws HttpMsgParseException {
		Matcher m = statusLine.matcher(startLine);
		if (m.find()) {
			if (m.groupCount() != 3) {
				throw new HttpMsgParseException("HTTP Response status line error - " + startLine);
			}
			String g1 = m.group(1);
			String g2 = m.group(2);
			if ((g1 == null || g1.isEmpty()) || (g2 == null || g2.isEmpty())) {
				throw new HttpMsgParseException("HTTP Response status line error - " + startLine);
			}
			statusCode = Integer.parseInt(g2);
			reasonPhrase = g2;
		}
	}
}
