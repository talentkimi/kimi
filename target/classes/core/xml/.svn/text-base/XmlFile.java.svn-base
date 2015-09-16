package core.xml;

import java.io.IOException;

import core.io.file.TextFile;

/**
 * An XML File.
 */
public class XmlFile extends TextFile {

	/**
	 * Creates a new XML file.
	 * @param filename the filename.
	 */
	public XmlFile(String filename) {
		super(filename);
	}

	/**
	 * Read this file to XML.
	 * @return the XML.
	 */
	public Xml readToXml() throws IOException {
		String text = readToString();
		Xml xml = Xml.READER.read(text);
		return xml;
	}

	/**
	 * Write the given XML to the file.
	 * @param xml the XML to write.
	 * @throws IOException
	 */
	public void write(Xml xml) throws IOException {
		String text = Xml.WRITER.write(xml);
		write(text);
	}

}