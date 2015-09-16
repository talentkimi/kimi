package json;

import core.xml.Xml;

/**
 * JSON String
 * 
 * @author vesko
 */
final public class JSONString implements JSONObject {
	final private StringBuffer object = new StringBuffer();

	/**
	 * constructor
	 * 
	 * @param source
	 * @throws NullPointerException
	 */
	public JSONString(StringBuffer source) throws NullPointerException {
		if (source == null) {
			throw new NullPointerException();
		}
		object.append(source.toString());
	}

	/**
	 * get object string
	 * 
	 * @return string [object string]
	 */
	final public String get() {
		return object.toString();
	}

	/**
	 * get object size
	 * 
	 * @return int [object size]
	 */
	final public int size() {
		return object.length();
	}

	/**
	 * is object an element
	 * 
	 * @return boolean [true if its JSONElement]
	 */
	final public boolean isElement() {
		return false;
	}

	/**
	 * is object an element list
	 * 
	 * @return boolean [true if its JSONElementList]
	 */
	final public boolean isList() {
		return false;
	}

	/**
	 * is object an string
	 * 
	 * @return boolean [true if its JSONString]
	 */
	final public boolean isString() {
		return true;
	}

	/**
	 * close object
	 * 
	 */
	final public void close() {
		object.setLength(0);
	}

	/**
	 * convert object to string presentation
	 * 
	 * @return string [converted object to string presentation]
	 */
	final public String toString() {
		return object.toString();
	}

	/**
	 * convert object to Xml presentation
	 * 
	 * @param xml
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	final public void toXml(Xml xml) throws NullPointerException, IllegalArgumentException {
		if (xml == null) {
			throw new NullPointerException();
		}
		xml.setValue(object.toString());
	}
}