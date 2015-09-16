package core.xml.sax;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import core.xml.Xml;
import core.xml.XmlAttribute;
import core.xml.XmlNode;

public class XmlDefaultHandler extends DefaultHandler {

	private ArrayList<Xml> stack = new ArrayList<Xml>();
	private Xml xml = null;

	public Xml getXml() {
		return xml;
	}

	public void characters(char[] chars, int offset, int length) {
		boolean whitespace = false;
		for (int i = offset; i < offset + length; i++) {
			char c = chars[i];
			if (Character.isWhitespace(c)) {
				whitespace = true;
			} else {
				whitespace = false;
				break;
			}
		}
		if (whitespace) {
			return;
		}
		String value = new String(chars, offset, length);
		xml.setValue(value);
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		Xml xml = new XmlNode(qName);
		if (this.xml != null) {
			this.xml.addChild(xml);
		}
		this.xml = xml;
		stack.add(xml);

		// Attributes
		for (int i = 0; i < attributes.getLength(); i++) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);
			XmlAttribute attribute = new XmlAttribute(name, value);
			xml.addAttribute(attribute);
		}
	}

	public void endElement(String uri, String localName, String qName) {
		if (stack.size() == 1) {
			xml = stack.get(0);

			// Cleanup
			stack.remove(0);
			stack = null;
		} else {
			stack.remove(stack.size() - 1);
			xml = stack.get(stack.size() - 1);
		}
	}
}