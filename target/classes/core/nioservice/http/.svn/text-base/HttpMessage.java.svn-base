package core.nioservice.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.text.Charsets;

public abstract class HttpMessage {

	public static final String HTTP = "HTTP";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final byte CR = 13;
	public static final byte LF = 10;
	public static final byte[] eohPattern = {CR, LF, CR, LF};

	protected static final Pattern headerContentLength = Pattern.compile("^.*content-length: *(\\d*).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	protected static final Pattern headerContentType = Pattern.compile("^.*content-type: *(.*)\r\n", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	protected static final Pattern headerAcceptEncoding = Pattern.compile("^.*accept-encoding: *(.*)\r\n", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	protected static final Pattern headerContentEncoding = Pattern.compile("^.*content-encoding: *(.*)\r\n", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	protected static final Pattern headerInternetAddress = Pattern.compile("^.*internet-address: *(.*)\r\n", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	protected static final Pattern contentLengthPattern = Pattern.compile("content-length: *\\d*\r\n", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	protected static final Pattern contentEncodingPattern = Pattern.compile("content-encoding: *.*\r\n", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	protected String startLine;
	protected String headers;
	protected boolean complete;
	protected int pos;
	protected int contentLength;
	protected String contentType;
	protected String acceptEncoding;
	protected String contentEncoding;
	protected String internetAddress;
	// This data stream contains the entire message (titles, headers, content, etc; everything)
	protected ByteArrayOutputStream data;

	public HttpMessage() {
		init();
		data = new ByteArrayOutputStream();
	}

	private void init() {
		startLine = null;
		headers = null;
		complete = false;
		pos = -1;
		contentLength = -1;
		contentType = null;
		acceptEncoding = null;
		contentEncoding = null;
		internetAddress = null;
	}

	public int getContentLength() {
		return contentLength;
	}

	public ByteArrayOutputStream get() {
		return data;
	}

	public byte[] toByteArray() {
		return data.toByteArray();
	}

	public void reset() {
		init();
		data.reset();
	}

	public synchronized void close() throws IOException {
		if (data == null) {
			init();
		} else {
			reset();
			data.close();
			data = null;
		}
	}

	public String getStartLine() {
		return startLine;
	}

	public String getHeaders() {
		return headers;
	}

	public boolean isComplete() {
		return this.complete;
	}

	public String getContentType() {
		return contentType;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public String getInternetAddress() {
		return internetAddress;
	}

	public String getString() throws Exception {
		StringBuilder sb = new StringBuilder(startLine);
		sb.append("\r\n");
		sb.append(headers);
		sb.append("\r\n");
		if (contentLength > 0) {
			sb.append(new String(getContent(), Charsets.HTTP));
		}
		return sb.toString();
	}

	public byte[] getContent() throws Exception {
		if (this.complete && contentLength > 0) {
			byte[] content = new byte[contentLength];
			System.arraycopy(data.toByteArray(), pos, content, 0, contentLength);
			return content;
		} else {
			throw new Exception("HTTP Message not complete or content lenght is zero");
		}
	}

	public String getContentString() throws Exception {
		return new String(getContent(), Charsets.HTTP);
	}

	public void setContent(byte[] content) throws IOException {
		setContentLength(content.length);
		StringBuilder sb = new StringBuilder(startLine);
		sb.append("\r\n");
		sb.append(headers);
		sb.append("\r\n");
		data.reset();
		data.write(sb.toString().getBytes(Charsets.SYSTEM));
		data.write(content);
	}

	public void setContentLength(int contentLength) {
		Matcher m = contentLengthPattern.matcher(headers);
		if (m.find()) {
			headers = m.replaceFirst("Content-Length: " + contentLength + "\r\n");
		} else {
			headers = headers + "Content-Length: " + contentLength + "\r\n";
		}
		this.contentLength = contentLength;
	}

	public void setContentEncoding(String contentEncoding) {
		Matcher m = contentEncodingPattern.matcher(headers);
		if (m.find()) {
			headers = m.replaceFirst("Content-Encoding: " + contentEncoding + "\r\n");
		} else {
			headers = headers + "Content-Encoding: " + contentEncoding + "\r\n";
		}
		this.contentEncoding = contentEncoding;
	}

	public void parse() throws HttpMsgParseException {

		if (complete) {
			return;
		}

		byte[] byteArray = null;
		boolean parseSuccess = false;

		// HTTP request line

		if (startLine == null) {
			if (data.size() < 8) {
				return;
			} else {
				byteArray = data.toByteArray();
				parseSuccess = parseStartLine(byteArray);
			}
		} else {
			parseSuccess = true;
		}

		// HTTP headers

		if (parseSuccess && headers == null) {
			if (byteArray == null) {
				byteArray = data.toByteArray();
			}
			parseSuccess = parseHeaders(byteArray);
		}

		// HTTP content

		if (parseSuccess && !complete) {
			if (contentLength > 0) {
				// All data has been read; this request is completed
				if ((data.size() - pos) == contentLength) {
					complete = true;
				}
			} else {
				complete = true;
			}
		}
	}

	private boolean parseStartLine(byte[] byteArray) throws HttpMsgParseException {
		byte prevByte = 0;
		for (int i = 0; i < byteArray.length; i++) {
			// CRLF sequence found
			if (prevByte == CR && byteArray[i] == LF) {
				// Get the position ready for parsing the headers
				this.pos = i - 1;
				try {
					startLine = new String(byteArray, 0, pos, Charsets.SYSTEM);
				} catch (UnsupportedEncodingException e) {
					throw new HttpMsgParseException(e.getMessage(), e);
				}
				setStartLineInfo();
				return true;
			}
			prevByte = byteArray[i];
		}
		return false;
	}

	protected abstract void setStartLineInfo() throws HttpMsgParseException;

	private boolean parseHeaders(byte[] byteArray) throws HttpMsgParseException {
		int oldPos = pos;
		int j = 0;
		for (int i = oldPos; i < byteArray.length; i++) {
			if (byteArray[i] == eohPattern[j++]) {
				// end of headers
				if (j == 4) {
					// This is now the position content should start
					pos = i + 1;
					try {
						headers = new String(byteArray, (oldPos + 2), (i - oldPos - 3), Charsets.SYSTEM);
					} catch (UnsupportedEncodingException e) {
						throw new HttpMsgParseException(e.getMessage(), e);
					}
					setHeadersOfInterest();
					return true;
				}
			} else {
				j = 0;
			}
		}
		return false;
	}

	private void setHeadersOfInterest() throws HttpMsgParseException {
		Matcher m = headerContentLength.matcher(headers);
		if (m.find()) {
			if (m.groupCount() > 1) {
				throw new HttpMsgParseException("HTTP more than one Content-Length headers were found");
			}
			contentLength = Integer.parseInt(m.group(1));
		}
		m = headerContentType.matcher(headers);
		if (m.find()) {
			if (m.groupCount() > 1) {
				throw new HttpMsgParseException("HTTP more than one Content-Type headers were found");
			}
			contentType = m.group(1);
		}
		m = headerAcceptEncoding.matcher(headers);
		if (m.find()) {
			if (m.groupCount() > 1) {
				throw new HttpMsgParseException("HTTP more than one Accept-Encoding headers were found");
			}
			acceptEncoding = m.group(1);
		}
		m = headerContentEncoding.matcher(headers);
		if (m.find()) {
			if (m.groupCount() > 1) {
				throw new HttpMsgParseException("HTTP more than one Content-Encoding headers were found");
			}
			contentEncoding = m.group(1);
		}
		m = headerInternetAddress.matcher(headers);
		if (m.find()) {
			if (m.groupCount() > 1) {
				throw new HttpMsgParseException("HTTP more than one Internet-Address headers were found");
			}
			internetAddress = m.group(1);
		}
	}
}
