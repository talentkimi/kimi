package json;

import core.xml.Xml;

/**
 * JSON Object interface
 * 
 * @author vesko
 */
public interface JSONObject {
	/**
	 * get object size
	 * 
	 * @return int [object size]
	 */
	abstract public int size();

	/**
	 * is object an element
	 * 
	 * @return boolean [true if its JSONElement]
	 */
	abstract public boolean isElement();

	/**
	 * is object an element list
	 * 
	 * @return boolean [true if its JSONElementList]
	 */
	abstract public boolean isList();

	/**
	 * is object an string
	 * 
	 * @return boolean [true if its JSONString]
	 */
	abstract public boolean isString();

	/**
	 * close object
	 * 
	 */
	abstract public void close();

	/**
	 * convert object to Xml presentation
	 * 
	 * @param xml
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	abstract public void toXml(Xml xml) throws NullPointerException, IllegalArgumentException;
}