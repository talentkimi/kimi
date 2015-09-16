package logging;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.http.request.HttpRequest;
import core.http.response.HttpResponse;
import core.text.DateUtil;
import core.util.UtilDate;
import core.xml.Xml;
import core.xml.XmlNode;


/**
 * This class used to logging xml-api commands.
 * @author Dimitrijs
 *
 */
public class HttpLogRecord extends TpLogRecord implements Externalizable {
	private static final long serialVersionUID = 2L;

	public static final String TYPE = "xmlapi";
	public static final String LOGGER_SUSPICIOUS = TYPE + "._SUSPICIOUS";
	public static final String LOGGER_UNDEFINED = TYPE + "._UNDEFINED";
	private static final Pattern loginIdPattern = Pattern.compile("(<LoginId>)(.*)(</LoginId>)", Pattern.DOTALL);
	private static final Pattern routingIdPattern = Pattern.compile("(<RoutingId>)(.*)(</RoutingId>)", Pattern.DOTALL);
	private static final Pattern cardNumberPattern = Pattern.compile("(<BillingDetails>.*<Number>)(.*)(</Number>.*</BillingDetails>)", Pattern.DOTALL);
	private static final Pattern cardSecurityCodePattern = Pattern.compile("(<BillingDetails>.*<SecurityCode>)(.*)(</SecurityCode>.*</BillingDetails>)", Pattern.DOTALL);
	private static final Pattern passwordPattern = Pattern.compile("(<Login>.*<Password>)(.*)(</Password>.*</Login>)", Pattern.DOTALL);
	private static final Pattern numbersPattern = Pattern.compile("\\d");
	private static final Pattern wordCharsPattern = Pattern.compile(".");
	private static final Pattern nonWordCharsPattern = Pattern.compile("\\W");
	
	private String loginId;
	private String routingId;
	private String requestHeaders;
	private String responseHeaders;
	private String requestContent;
	private String responseContent;
	private String loggerId;
	
	public HttpLogRecord() {
	}

	public HttpLogRecord(long time) {
		super(time);
	}

	/**
	 * Shortcut method to log xml-api request and response.
	 * @param request HttpRequest of this command
	 * @param requestContent extracted request content
	 * @param response HttpResponse for this command
	 * @param responseContent extracted response content
	 */
	public static void log(HttpRequest request, String requestContent, HttpResponse response, String responseContent) {
		final HttpLogRecord record = new HttpLogRecord();
		record.setRequestHeaders(request == null ? null : request.toString(false));
		record.setRequestContent(requestContent);
		record.setResponseHeaders(response == null ? null : response.toString(false));
		record.setResponseContent(responseContent);
		final TpLogger logger = TpLoggerManager.getLogger(record.getLoggerId());
		logger.log(record);
	}
	
	public TpLogIndexEntry[] createIndexEntries() {
		return new TpLogIndexEntry[] {new HttpTimeLogIndexEntry(this), new HttpRoutingLogIndexEntry(this)};
	}
	
	public String getRoutingId() {
		// search routingId in request if have not set yet
		if (routingId == null && requestContent != null) {
			final Matcher m = routingIdPattern.matcher(requestContent);
			if (m.find()) {
				routingId = requestContent.substring(m.start(2), m.end(2)).trim().intern();
			}
		}

		// search routingId in response if have not set yet and not found in request
		if (routingId == null && responseContent != null) {
			final Matcher m = routingIdPattern.matcher(responseContent);
			if (m.find()) {
				routingId = responseContent.substring(m.start(2), m.end(2)).trim().intern();
			}
		}

		return routingId;
	}

	public void setRoutingId(String routingId) {
		this.routingId = routingId;
	}

	public String getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(String request) {
		this.requestHeaders = request;
	}

	public String getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(String response) {
		this.responseHeaders = response;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	public String getLoginId() {
		// search loginId in request if didn't set yet
		if (loginId == null && requestContent != null) {
			final Matcher m = loginIdPattern.matcher(requestContent);
			if (m.find()) {
				loginId = requestContent.substring(m.start(2), m.end(2)).trim().intern();
			}
		}

		// search loginId in request if have not set yet and not found in request 
		if (loginId == null && responseContent != null) {
			final Matcher m = loginIdPattern.matcher(responseContent);
			if (m.find()) {
				loginId = responseContent.substring(m.start(2), m.end(2)).trim().intern();
			}
		}

		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Override
	public String getLoggerId() {

		if (loggerId == null) {
			String loginId = getLoginId();
			// if loginId is null or it is not 16 chars long or contains 
			// non-word characters - use UNDEFINED or SUSPICIOUS logger.  
			if (loginId == null) {
				loggerId = LOGGER_UNDEFINED;
			} else if (loginId.length() != 16) {
				loggerId = LOGGER_SUSPICIOUS;
			} else if (nonWordCharsPattern.matcher(loginId).find()) {
				loggerId = LOGGER_SUSPICIOUS;
			} else {
				loggerId = (TYPE + "." + loginId).intern();
			}
		}
		return loggerId;
	}

	private void mask(StringBuilder str, int start, int end, Pattern toMask) {
		final String numbers = str.substring(start, end);
		final Matcher m = toMask.matcher(numbers);
		final StringBuffer masked = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(masked, "*");
		}
		m.appendTail(masked);
		str.replace(start, end, masked.toString());
	}
	
	private void maskNumbers(StringBuilder str, int start, int end) {
		mask(str, start, end, numbersPattern);
	}
	
	private void maskWordChars(StringBuilder str, int start, int end) {
		mask(str, start, end, wordCharsPattern);
	}

	@Override
	public void maskPrivate() {
		if (requestContent == null) {
			return;
		}
		final StringBuilder str = new StringBuilder(requestContent);
		final Matcher m1 = cardNumberPattern.matcher(requestContent);
		while (m1.find()) {
			maskNumbers(str, m1.start(2), m1.end(2));
		}
		final Matcher m2= cardSecurityCodePattern.matcher(requestContent);
		while (m2.find()) {
			maskNumbers(str, m2.start(2), m2.end(2));
		}
		final Matcher m3= passwordPattern.matcher(requestContent);
		while (m3.find()) {
			maskWordChars(str, m3.start(2), m3.end(2));
		}
		requestContent = str.toString();
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.readExternal(in);
		int ver = in.readInt(); // version
		switch (ver) {
			case 2:
				requestHeaders = (String) in.readObject();
				requestContent = (String) in.readObject();
				responseHeaders = (String) in.readObject();
				responseContent = (String) in.readObject();
				loginId = (String) in.readObject();
				routingId = (String) in.readObject();
				loggerId = (String) in.readObject();
				break;
			default:
				throw new IllegalStateException("Unsupported version " + ver);	
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(2); // version
		out.writeObject(requestHeaders);
		out.writeObject(requestContent);
		out.writeObject(responseHeaders);
		out.writeObject(responseContent);
		out.writeObject(loginId);
		out.writeObject(routingId);
		out.writeObject(loggerId);
	}

	@Override
	public String toString() {
		return String.format("HttpLogRecord routingId=[%s] time=[%tc]", getRoutingId(), getTime());
	}
	
	@Override
	public Xml toXml() {
		final DateFormat fmt = DateUtil.getDateFormat(UtilDate.FORMAT_PATTERN_MILLIS);
		final Xml recXml = new XmlNode("HttpLogRecord");
		final Xml logTime = new XmlNode("Time", fmt.format(new Date(getTime())));
		recXml.addChild(logTime);
		
		final Xml logRoutingId = new XmlNode("RoutingId", getRoutingId());
		recXml.addChild(logRoutingId);

		final Xml logLoginId = new XmlNode("LoginId", getLoginId());
		recXml.addChild(logLoginId);

		final Xml logRequest = new XmlNode("Request");
		recXml.addChild(logRequest);

		final Xml logRequestHeader = new XmlNode("Header", getRequestHeaders());
		logRequest.addChild(logRequestHeader);
		
		final Xml logRequestContent = new XmlNode("Content", getRequestContent());
		logRequest.addChild(logRequestContent);
		
		final Xml logResponse = new XmlNode("Response");
		recXml.addChild(logResponse);
		
		final Xml logResponseHeader = new XmlNode("Header", getResponseHeaders());
		logResponse.addChild(logResponseHeader);

		final Xml logResponseContent = new XmlNode("Content", getResponseContent());
		logResponse.addChild(logResponseContent);
		return recXml;
	}
	
	public static HttpLogRecord fromXml(Xml xml) {
		final DateFormat fmt = DateUtil.getDateFormat(UtilDate.FORMAT_PATTERN_MILLIS);
		Date time;
		try {
			time = fmt.parse(xml.getChild("Time").getValue().toString());
		} catch (ParseException ex) {
			time = new Date();
		}
		
		final HttpLogRecord result = new HttpLogRecord(time.getTime());
		
		Xml routing = xml.getChild("RoutingId", true);
		if (routing != null) {
			result.routingId = routing.getValue().toString();
		}
		
		Xml login = xml.getChild("LoginId", true);
		if (login != null) {
			result.loginId = login.getValue().toString();
		}
		
		final Xml request = xml.getChild("Request");
		result.requestHeaders = request.getChild("Header").getValue().toString();
		result.requestContent = request.getChild("Content").getValue().toString();

		final Xml response = xml.getChild("Response");
		result.responseHeaders = response.getChild("Header").getValue().toString();
		
		Xml respc = response.getChild("Content", true);
		if (respc != null) {
			result.responseContent = respc.getValue().toString();
		}
		
		return result;
	}
	
}
