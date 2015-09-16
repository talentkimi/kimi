package core.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class StringDefaultHandler extends DefaultHandler {

	private static final int STATE_NONE = 0;
	private static final int STATE_START_ELEMENT = 1;
	private static final int STATE_END_ELEMENT = 2;
	private static final int STATE_DATA = 3;

	private int indent = 0;
	private int state = STATE_NONE;
	private final StringBuilder xml = new StringBuilder();

	public String toString() {
		return xml.toString();
	}

	private void appendIndent() {
		if (indent > 0) {
			for (int i = 0; i < indent; i++) {
				xml.append('\t');
			}
		}
	}

	public void characters(char[] chars, int offset, int length) {
		if (length > 0) {
			xml.append(chars, offset, length);
		}
		state = STATE_DATA;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (state == STATE_START_ELEMENT) {
			xml.append('\r').append('\n');
		}
		appendIndent();
		xml.append('<').append(qName);
		
		// Attributes
		for (int i = 0; i < attributes.getLength(); i++) {
			xml.append(' ');
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);
			xml.append(name).append('=').append('"').append(value).append('"');
		}
		
		xml.append('>');
		indent++;
		state = STATE_START_ELEMENT;
	}

	public void endElement(String uri, String localName, String qName) {
		indent--;
		if (state != STATE_DATA) {
			appendIndent();
		}
		xml.append('<').append('/').append(qName).append('>').append('\r').append('\n');
		state = STATE_END_ELEMENT;
	}
}