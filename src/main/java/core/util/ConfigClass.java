package core.util;

import core.xml.Xml;
import core.xml.XmlException;

/**
 * A Config Class.
 */
public interface ConfigClass {

	/**
	 * Parse from the given xml.
	 * @param xml the xml.
	 */
	void parseFrom(Xml xml) throws XmlException;

}
