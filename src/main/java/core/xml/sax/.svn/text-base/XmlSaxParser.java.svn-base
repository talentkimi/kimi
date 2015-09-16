package core.xml.sax;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import core.xml.Xml;

public class XmlSaxParser extends SaxParser {

	private static final Logger log = LoggerFactory.getLogger(XmlSaxParser.class);
	
	protected DefaultHandler newHandler() {
		return new XmlDefaultHandler();
	}

	public Xml parseXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		XmlDefaultHandler handler = (XmlDefaultHandler) parse(xml);
		return handler.getXml();
	}

	public Xml parseXml(File file) throws ParserConfigurationException, SAXException, IOException {
		XmlDefaultHandler handler = (XmlDefaultHandler) parse(file);
		return handler.getXml();
	}

	public static void main(String[] args) {
		String xml = "<Test><FredList fred=\"2\"><Fred>yo</Fred><Fred>yo</Fred></FredList></Test>";
		try {
			XmlSaxParser parser = new XmlSaxParser();
			System.out.println(parser.parseXml(xml));
		} catch (Exception e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
	}

}