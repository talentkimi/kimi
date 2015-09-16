package core.xml.sax;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import core.text.Charsets;

public class SaxParser {

	protected SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		return factory.newSAXParser();
	}

	protected DefaultHandler newHandler() {
		return new StringDefaultHandler();
	}

	public final DefaultHandler parse(File file) throws ParserConfigurationException, SAXException, IOException {
		DefaultHandler handler = newHandler();
		SAXParser saxParser = newSAXParser();
		saxParser.parse(file, handler);
		return handler;
	}

	public final DefaultHandler parse(InputStream input) throws ParserConfigurationException, SAXException, IOException {
		DefaultHandler handler = newHandler();
		SAXParser saxParser = newSAXParser();
		saxParser.parse(input, handler);
		return handler;
	}

	public final DefaultHandler parse(byte[] bytes) throws ParserConfigurationException, SAXException, IOException {
		InputStream input = new ByteArrayInputStream(bytes);
		return parse(input);
	}

	public final DefaultHandler parse(String text) throws ParserConfigurationException, SAXException, IOException {
		byte[] bytes;
		try {
			bytes = text.getBytes(Charsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			bytes = text.getBytes();
		}
		return parse(bytes);
	}

}
