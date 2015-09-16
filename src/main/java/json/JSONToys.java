package json;

import core.xml.Xml;
import core.xml.XmlNode;

/**
 * JSON Toys
 * 
 * @author vesko
 */
final public class JSONToys {
	/**
	 * convert JSON data to xml object
	 * 
	 * @param content
	 * @return xml [converted JSON data to xml object]
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	final static public Xml toXml(StringBuffer content) throws NullPointerException, IllegalArgumentException {
		if (content == null) {
			throw new NullPointerException();
		}
		Xml response = new XmlNode("jsonxml");
		if (content.length() == 0) {
			return response;
		}
		JSONSource source = new JSONSource(content);
		JSONObject object = null;
		if (isList(content)) {
			object = new JSONElementList(source);
		} else if (isElement(content)) {
			object = new JSONElement(source);
		} else {
			throw new IllegalArgumentException("Unrecognized JSON data");
		}
		object.toXml(response);
		object.close();
		source.close();
		return response;
	}

	/**
	 * is JSON data single element
	 * 
	 * @param content
	 * @return boolean [true if JSON data is single element]
	 * @throws NullPointerException
	 */
	final static public boolean isElement(StringBuffer content) throws NullPointerException {
		if (content == null) {
			throw new NullPointerException();
		}
		return content.charAt(0) == '{' && content.charAt(content.length() - 1) == '}';
	}

	/**
	 * is JSON data element list
	 * 
	 * @param content
	 * @return boolean [true if JSON data is element list]
	 * @throws NullPointerException
	 */
	final static public boolean isList(StringBuffer content) throws NullPointerException {
		if (content == null) {
			throw new NullPointerException();
		}
		return content.charAt(0) == '[' && content.charAt(content.length() - 1) == ']';
	}
}