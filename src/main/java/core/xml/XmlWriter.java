package core.xml;

import core.io.file.TextFile;

/**
 * An XML Writer.
 */
public class XmlWriter {

	/** Format the xml. */
	private final boolean format;

	/**
	 * Creates a new XmlWriter.
	 * @param format true to format the output.
	 */
	public XmlWriter(boolean format) {
		this.format = format;
	}

	/**
	 * Creates a new XmlWriter.
	 */
	public XmlWriter() {
		this(true);
	}

	/**
	 * Append the given xml.
	 * @param xml the xml.
	 * @param buffer the buffer.
	 * @param indent the indent.
	 */
	private void appendXml(Xml xml, StringBuilder buffer, int indent) throws XmlException {

		// Indent
		if (format) {
			for (int i = 0; i < indent; i++) {
				buffer.append('\t');
			}
		}

		// CDATA
		if (xml instanceof XmlCdata) {
			appendCdata(buffer, (XmlCdata) xml);
			return;
		}

		// Name
		buffer.append('<');
		buffer.append(xml.getName());

		// Attributes
		int attributes = xml.attributes();
		for (int i = 0; i < attributes; i++) {
			Xml attribute = xml.getAttribute(i);
			if (!attribute.hasAValue()) {
				if (attribute.isOptional())
					continue;
				XmlException xe = new XmlException("Attribute \"" + attribute.getName() + "\" not initialised and not optional");
				// xe.getNodeList().add( node );
				throw xe;
			}

			// Append
			buffer.append(' ');
			buffer.append(attribute.getName());
			buffer.append('=');
			buffer.append('\"');
			appendValue(buffer, attribute, true);
			buffer.append('\"');
		}

		// No value or children (empty)
		int children = xml.children();
		if (xml.getValue() == null && children == 0) {
			if (!xml.isOptional()) {
				throw new XmlException("Tag <" + xml.getName() + "> not initialised and not optional");
			}
			buffer.append('/');
			buffer.append('>');
			if (format) {
				buffer.append(TextFile.newLine);
			}
			return;
		}

		// Value
		if (xml.hasAValue()) {
			buffer.append('>');
			appendValue(buffer, xml, false);
		}

		// Children
		else {
			if (children == 0) {
				buffer.append('/');
				buffer.append('>');
				if (format) {
					buffer.append(TextFile.newLine);
				}
				return;
			}
			buffer.append('>');
			if (format) {
				buffer.append(TextFile.newLine);
			}
			try {
				for (int i = 0; i < children; i++) {
					Xml child = xml.getChild(i);
					appendXml(child, buffer, indent + 1);
				}
			} catch (XmlException xe) {
				// xe.getNodeList().add( node );
				throw xe;
			}

			// Indent
			if (format) {
				for (int i = 0; i < indent; i++) {
					buffer.append('\t');
				}
			}
		}

		// Close
		buffer.append('<');
		buffer.append('/');
		buffer.append(xml.getName());
		buffer.append('>');
		if (format) {
			buffer.append(TextFile.newLine);
		}
	}

	/**
	 * Append CDATA to the given buffer.
	 * @param buffer the buffer.
	 * @param cdata the cdata.
	 */
	private void appendCdata(StringBuilder buffer, XmlCdata cdata) {
		cdata.getValue();
		buffer.append("<![CDATA[");
		buffer.append(cdata.getValue());
		buffer.append("]]>");
	}

	/**
	 * Append the value of the given xml to the buffer.
	 * @param buffer the buffer.
	 * @param xml the xml.
	 * @param attribute true if the xml is an attribute.
	 */
	private void appendValue(StringBuilder buffer, Xml xml, boolean attribute) {
		// if (log.isDebugEnabled()) log.debug ("<" + xml.getName() + "> " + xml.getClass().getName() + "... " + xml.hasAValue() + "? " + xml.getValue().getClass());
		Object value = xml.getValue();
		if (value instanceof XmlCdata) {
			appendCdata(buffer, (XmlCdata) value);
			return;
		}
		String text = value.toString();
		if (text == null) {
			text = "null";
		}
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			
			// never write out invalid chars
			if ((c >= 0x0 && c <= 0x8) || (c >= 0xB && c <= 0xC) || (c >= 0xE && c <= 0x1F) ||
				(c >= 0xD800 && c <= 0xDFFF) || (c >= 0xFFFE && c <= 0xFFFF)) {
				continue;
			}
			
			switch (c) {
				case '&' :
					buffer.append("&amp;");
					break;
				case '<' :
					buffer.append("&lt;");
					break;
				case '>' :
					buffer.append("&gt;");
					break;
				case '\"' :
					buffer.append("&quot;");
					break;
				case '\'' :
					buffer.append("&apos;");
					break;
				default :
					buffer.append(c);
			}
			// }
		}
	}

	/**
	 * Write the given node to an XML string.
	 * @return the XML string.
	 */
	public String write(Xml xml) throws XmlException {
		StringBuilder buffer = new StringBuilder();
		appendXml(xml, buffer, 0);
		return buffer.toString();
	}

}
