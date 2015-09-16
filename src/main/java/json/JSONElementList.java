package json;

import java.util.ArrayList;
import core.xml.Xml;

/**
 * JSON Element List
 * 
 * @author vesko
 */
final public class JSONElementList implements JSONObject {
	final private ArrayList<JSONObject> elements = new ArrayList<JSONObject>();

	/**
	 * constructor
	 * 
	 * @param source
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public JSONElementList(JSONSource source) throws NullPointerException, IllegalArgumentException {
		this(source, true);
	}

	/**
	 * constructor
	 * 
	 * @param source
	 * @param initial
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public JSONElementList(JSONSource source, boolean initial) throws NullPointerException, IllegalArgumentException {
		this.parse(source);
		if (initial) {
			if (!source.empty()) {
				StringBuffer restContent = new StringBuffer(source.restSource().trim());
				if (restContent.length() > 0) {
					throw new IllegalArgumentException("An unparsed JSON data left:\n" + restContent);
				}
			}
		}
	}

	/**
	 * parse object
	 * 
	 * @param source
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	final private void parse(JSONSource source) throws NullPointerException, IllegalArgumentException {
		if (source == null) {
			throw new NullPointerException();
		}
		char character = source.nextCharacter();
		if (character != '[') {
			throw new IllegalArgumentException("Expected '[' instead '" + character + "'");
		}
		if (source.nextCharacter() == ']') {
			return;
		}
		source.back();
		for (;;) {
			character = source.nextCharacter();
			source.back();
			if (character == ',') {
				elements.add(null);
			} else {
				elements.add(source.nextObject());
			}
			character = source.nextCharacter();
			switch (character) {
			case ';':
			case ',':
				if (source.nextCharacter() == ']') {
					return;
				}
				source.back();
				break;
			case ']':
				return;
			default:
				throw new IllegalArgumentException("Expected ',' or ']' instead '" + character + "'");
			}
		}
	}

	/**
	 * get element
	 * 
	 * @param index
	 * @return JSONObject
	 * @throws IllegalArgumentException
	 */
	final public JSONObject get(int index) throws IllegalArgumentException {
		return get(index, false);
	}

	/**
	 * get element
	 * 
	 * @param index
	 * @return JSONObject
	 * @throws IllegalArgumentException
	 */
	final public JSONObject get(int index, boolean allowNull) throws IllegalArgumentException {
		if (index < 0 || index >= this.size()) {
			throw new IllegalArgumentException("Invalid index: " + index);
		}
		JSONObject object = elements.get(index);
		if (object == null && !allowNull) {
			throw new IllegalArgumentException("Empty element index: " + index);
		}
		return object;
	}

	/**
	 * get object size
	 * 
	 * @return int [object size]
	 */
	final public int size() {
		return elements.size();
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
		return true;
	}

	/**
	 * is object an string
	 * 
	 * @return boolean [true if its JSONString]
	 */
	final public boolean isString() {
		return false;
	}

	/**
	 * close object
	 * 
	 */
	final public void close() {
		for (int x = 0; x < elements.size(); x++) {
			JSONObject object = elements.get(x);
			if (object != null) {
				object.close();
			}
		}
		elements.clear();
	}

	/**
	 * convert object to Xml presentation
	 * 
	 * @param xml
	 * @param key
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	final public void toXml(Xml xml, String key) throws NullPointerException, IllegalArgumentException {
		if (xml == null || key == null) {
			throw new NullPointerException();
		}
		if (elements.size() == 0) {
			xml.newChild(key);
		} else {
			for (int x = 0; x < elements.size(); x++) {
				JSONObject object = elements.get(x);
				Xml element = xml.newChild(key);
				if (object != null) {
					object.toXml(element);
				}
			}
		}
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
		if (elements.size() == 1) {
			JSONObject object = elements.get(0);
			if (object == null) {
				throw new IllegalArgumentException("Unexpected element structure");
			}
			object.toXml(xml);
		} else {
			throw new IllegalArgumentException("Unexpected xml structure");
		}
	}
}