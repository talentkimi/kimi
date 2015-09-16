package core.xml.dom;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import core.text.Charsets;

public class DomParser {
	private static final Logger log = LoggerFactory.getLogger(DomParser.class);
	protected void configure(DocumentBuilderFactory factory) {
	}

	protected DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		configure(factory);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder;
	}

	public final Document parse(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = newDocumentBuilder();
		return builder.parse(file);
	}

	public final Document parse(InputStream input) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = newDocumentBuilder();
		return builder.parse(input);
	}

	public final Document parse(byte[] bytes) throws ParserConfigurationException, SAXException, IOException {
		InputStream input = new ByteArrayInputStream(bytes);
		return parse(input);
	}

	public final Document parse(String text) throws ParserConfigurationException, SAXException, IOException {
		byte[] bytes;
		try {
			bytes = text.getBytes(Charsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			bytes = text.getBytes();
		}
		return parse(bytes);
	}

	public static void main(String[] args) {
		String xml = "<Test>fred<Craig></Craig>fred</Test>";
		try {
			Document d = new DomParser().parse(xml);
			print(d);
		} catch (Exception e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
	}

	private static void print(Node node) {
		String value = node.getNodeValue();
		if (value != null) {
			System.out.println("CHILD <" + node.getNodeName() + ">" + value + "</" + node.getNodeName() + ">");
		} else {
			System.out.println("CHILD <" + node.getNodeName() + "/>");
		}
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attribute = attributes.item(i);
				System.out.println("ATTRIBUTE " + node.getNodeName() + "=" + node.getNodeValue());
			}
		}
		NodeList childList = node.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			print(childList.item(i));
		}
	}
}
