package core.xml;


import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.text.StringParser;

/**
 * An XML Reader.
 */
public final class XmlReader {

	private static final Logger log = LoggerFactory.getLogger(XmlReader.class);


	/** Special name characters. * */
	private static final String SPECIAL_NAME_CHARACTERS = "._-!:";

	/**
	 * Creates a new XmlReader.
	 */
	public XmlReader() {
	}

	/**
	 * Throws an exception.
	 */
	private final void throwXmlException(StringParser parser, int index) throws XmlException {
		throwException(parser, index, null);
	}

	/**
	 * Throws an exception.
	 */
	private final void throwException(StringParser parser, int index, String detail) throws XmlException {
		int line = 1;
		int character = index;
		for (int i = 0; i < index; i++) {
			if (parser.charAt(i) == '\n') {
				line++;
				character = index - i;
			}
		}
		if (detail == null) {
			detail = "";
		} else {
			detail = ", " + detail;
		}
		throw new XmlException("Badly formatted XML on line " + line + ", character " + character + " (" + parser.charAt(index) + ")" + detail);
	}

	/**
	 * Read the xml into the given node.
	 * @param node the node.
	 * @param xml the xml.
	 */
	public final void read(Xml node, String xml) throws XmlException {
		if (node == null || xml == null)
			throw new NullPointerException();
		xml = removeComments(xml);
		StringParser parser = new StringParser(xml);
		readXml(null, node, parser);
		validate(node, false);
	}

	/**
	 * Read the xml from the given reader
	 * @param reader the reader
	 */
	public final Xml read(Reader reader) throws XmlException {
		try {
			StringBuilder builder = new StringBuilder(64);
			char[] buf = new char[64];
			for (int n; (n = reader.read(buf)) != -1;) {
				builder.append(buf, 0, n);
			}
			return read(builder.toString());
		} catch (IOException e) {
			throw new XmlException(e);
		}
	}
	
	private final void validate(Xml node, boolean attribute) throws XmlException {
		if (node.getValue() == null) {
			if (node.isOptional())
				return;
			if (attribute) {
				throw new XmlException("Attribute \"" + node.getName() + "\" not initialised and not optional");
			} else {
				throw new XmlException("Tag <" + node.getName() + "> not initialised and not optional");
			}
		}
		try {
			int attributes = node.attributes();
			int children = node.children();
			for (int i = 0; i < attributes; i++) {
				validate(node.getAttribute(i), true);
			}
			for (int i = 0; i < children; i++) {
				validate(node.getChild(i), false);
			}
		} catch (XmlException xe) {
			// xe.getNodeList().add(node);
			throw xe;
		}
	}

	/**
	 * Remove comments from the given xml.
	 * @param xml the xml.
	 * @return the resulting xml.
	 */
	private final String removeComments(String xml) {
		StringBuilder sb = new StringBuilder(xml);
		while (true) {
			int indexStart = sb.indexOf("<!--");
			if (indexStart == -1) {
				break;
			}
			int indexEnd = sb.indexOf("-->", indexStart + 4);
			if (indexEnd == -1) {
				break;
			}
			sb.replace(indexStart, indexEnd + 3, "");
		}
		return sb.toString();
	}

	/**
	 * Read the xml into the given node.
	 * @param xml the xml.
	 */
	public final Xml read(String xml) throws XmlException {
		xml = removeComments(xml);
		StringParser parser = new StringParser(xml);
		Xml node = readXml(null, null, parser);
		if (node == null) {
			throw new XmlException("Unable to parse XML from string: \"" + xml + "\"");
		}
		return node;
	}

	/**
	 * Creates a new attribute in the given node.
	 * @param name the name.
	 * @param value the value.
	 * @param parent the parent.
	 */
	private final void newXmlAttribute(Xml parent, Xml node, String name, String value) throws XmlException {
		if (parent == null) {
			XmlAttribute attribute = new XmlAttribute(name, value);
			node.addAttribute(attribute);
		} else {
			int attributes = node.attributes(name);
			Xml attribute = null;
			for (int i = 0; i < attributes; i++) {
				attribute = node.getAttribute(name, i);
				if (!attribute.hasAValue()) {
					attribute.setValue(attribute.parseValue(value));
					break;
				}
				attribute = null;
			}
			if (attribute == null) {
				throw new XmlException("Missing attribute: \"" + name + "\"");
			}
		}
	}

	/** The value. * */
	private static final String VALUE = "";

	/**
	 * Creates and returns a new xml node.
	 * @param name the name.
	 * @return parent new parent.
	 */
	private Xml newXml(Xml parent, Xml node, String name) throws XmlException {
		if (node != null) {
			if (!node.getName().equals(name)) {
				throw new XmlException("Unexpedected Tag <" + name + ">, expected Tag <" + node.getName() + ">");
			}
			node.setValue(VALUE);
		} else {
			if (parent == null) {
				node = new XmlNode(name);
			} else {
				int children = node.children(name);
				Xml child = null;
				for (int i = 0; i < children; i++) {
					child = node.getAttribute(name, i);
					if (!child.hasAValue()) {
						child.setValue(child.parseValue(VALUE));
						break;
					}
					child = null;
				}
				if (child == null) {
					throw new XmlException("Missing child: \"" + name + "\"");
				}
			}
		}
		return node;
	}

	/** End of Tag Characters. * */
	private static final char[] END_TAG_CHARS = {'>', '/'};

	/**
	 * Reads the XML attribute. Allows single/double quoted and name only attributes.
	 * @param parent the parent.
	 * @param node the node.
	 * @param parser the parser.
	 */
	private final boolean readXmlAttribute(Xml parent, Xml node, StringParser parser) throws XmlException {
		int mark = parser.getMark();
		int index = parser.skipWhitespace(mark);
		if (parser.isChar(index, END_TAG_CHARS)) {
			parser.setMark(index);
			return false;
		}
		// There must be at least one space.
		char c = parser.charAt(index);
		if (index == mark) {
			throwXmlException(parser, index);
		}
		// First character of name must be a letter.
		if (!Character.isLetter(c) && c != '_') {
			throwXmlException(parser, index);
		}
		// Name
		mark = index;
		index = parser.skipAlpha(index + 1, SPECIAL_NAME_CHARACTERS);
		String name = parser.substring(mark, index);
		// ROBIN: String name = parser.getString(mark, index);
		// Equals
		c = parser.charAt(index);
		if (c != '=') {
			if (!Character.isWhitespace(c) && !parser.isChar(index, END_TAG_CHARS)) {
				throwXmlException(parser, index);
			}
			parser.setMark(index);
			newXmlAttribute(parent, node, name, "");
			return true;
		}
		// Quoted
		c = parser.charAt(++index);
		if (c == '\"' || c == '\'') {
			char quote = c;
			mark = index + 1;
			index = parser.indexOf(c, mark, true);
			parser.setMark(index + 1);
		}
		// Unquoted
		else {
			mark = index;
			for (; index < parser.length(); index++) {
				c = parser.charAt(index);
				if (Character.isWhitespace(c) || parser.isChar(index, END_TAG_CHARS)) {
					break;
				}
			}
			parser.setMark(index);
		}
		// Value
		String value = parser.getXmlString(mark, index);
		newXmlAttribute(parent, node, name, value);
		return true;
	}

	/**
	 * Parse the contents of the given parent XML tag from the given parser.
	 * @param parent the parent tag.
	 * @param parser the parser.
	 */
	private final Xml readXml(Xml parent, Xml current, StringParser parser) throws XmlException {
		int index = parser.skipWhitespace(parser.getMark());
		if (index == parser.length()) {
			parser.setMark(index);
			return null;
		}
		char c = parser.charAt(index);
		if (c != '<') {
			throwXmlException(parser, index);
		}
		// Special
		c = parser.charAt(++index);

		// ROBIN: I AM A HACKING BASTARD - THIS IS BAD :p
		if (c == '?') {
			index = parser.indexOf('<', index);
			c = parser.charAt(++index);
		}
		if (c == '!') {
			if (parser.subSequence(index + 1, index + 8).equals("DOCTYPE")) {
				index = parser.indexOf('<', index);
				c = parser.charAt(++index);
			} else {
				if (parser.subSequence(index + 1, index + 8).equals("[CDATA[")) {
					int beginIndex = index + 8;
					int endIndex = parser.indexOf("]]>", beginIndex);
					if (endIndex == -1) {
						throwXmlException(parser, index);
					}
					String value = parser.getString(beginIndex, endIndex);
					parser.setMark(endIndex + 3);
					return new XmlCdata(value);
				} else {
					throwXmlException(parser, index);
				}
			}
		}

		// First character of name must be a letter.
		if (!Character.isLetterOrDigit(c)) {
			throwXmlException(parser, index);
		}
		// Name
		int mark = index;
		index = parser.skipAlpha(index + 1, SPECIAL_NAME_CHARACTERS);
		String name = parser.substring(mark, index);
		// ROBIN: String name = Text.intern(parser.getString(mark, index), 4, false);
		parser.setMark(index);
		// Xml
		Xml node = newXml(parent, current, name);
		// Attributes
		try {
			for (int i = 0; i < 1000; i++) {
				if (!readXmlAttribute(parent, node, parser))
					break;
			}
		} catch (XmlException xe) {
			// xe.getNodeList().add(node);
			throw xe;
		}
		// Empty Tag
		index = parser.getMark();
		c = parser.charAt(index);
		if (c == '/') {
			if (parser.charAt(index + 1) != '>') {
				throwXmlException(parser, index + 1);
			}
			parser.setMark(index + 2);
			if (parent != null) {
				if (!node.isOptional()) {
					throw new XmlException("Tag <" + name + "> empty and not optional");
				}
			}
			return node;
		}
		index++;
		// Children
		mark = index;
		index = parser.indexOf('<', mark, true);
		if (parser.charAt(index + 1) != '/') {
			while (true) {
				parser.setMark(index);
				try {
					if (parent == null && current == null) {
						Xml child = readXml(null, null, parser);
						if (child != null)
							node.addChild(child);
					} else {
						readXml(node, null, parser);
					}
				} catch (XmlException xe) {
					// xe.getNodeList().add(node);
					throw xe;
				}
				index = parser.indexOf('<', parser.getMark());
				if (parser.charAt(index + 1) == '/')
					break;
			}
		}
		// Value
		else {
			// Value
			String value = parser.getXmlString(mark, index);
			node.setValue(node.parseValue(value));
		}
		// End Tag
		mark = parser.indexOf('>', index + 2);
		if(mark < 0) {
			throwException(parser, index, "End tag incomplete: " + parser.substring(index));
		}
		String name2 = parser.substring(index + 2, mark);
		// ROBIN: String name2 = parser.getString(index + 2, mark);
		if (!name.equals(name2)) {
			throwException(parser, index, "Unexpected Tag </" + name2 + ">, expected Tag </" + name + ">");
		}
		parser.setMark(mark + 1);
		return node;
	}

}
