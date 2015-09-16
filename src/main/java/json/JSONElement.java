package json;

import java.util.Hashtable;
import core.xml.Xml;

/**
 * JSON Element
 * 
 * @author vesko
 */
final public class JSONElement implements JSONObject {
	final private Hashtable<String, JSONObject> objects = new Hashtable<String, JSONObject>();

	/**
	 * constructor
	 * 
	 * @param source
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public JSONElement(JSONSource source) throws NullPointerException, IllegalArgumentException {
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
	public JSONElement(JSONSource source, boolean initial) throws NullPointerException, IllegalArgumentException {
		this.parse(source);
		if (initial) {
			while (true) {
				char character = source.nextCharacter();
				if (character == ',' || character == ';') {
					if (source.nextCharacter() == '}') {
						break;
					}
					source.back();
					this.parse(source);
				} else {
					break;
				}
			}
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
		if (character != '{') {
			throw new IllegalArgumentException("Expected '{' instead '" + character + "'");
		}
		for (;;) {
			character = source.nextCharacter();
			String key = null;
			switch (character) {
			case 0:
				throw new IllegalArgumentException("Expected '}' instead '" + character + "'");
			case '}':
				return;
			default:
				source.back();
				JSONObject object = source.nextObject();
				if (!object.isString()) {
					throw new IllegalArgumentException("Unexpected key object type");
				}
				key = ((JSONString) object).get();
				break;
			}

			character = source.nextCharacter();
			if (character == '=') {
				if (source.next() != '>') {
					source.back();
				}
			} else if (character != ':') {
				throw new IllegalArgumentException("Expected ':' instead '" + character + "'");
			}
			
			if (objects.containsKey(key)) {
				throw new IllegalArgumentException("Overriding already existing object key: '" + key + "'");
			}
			
			objects.put(key, source.nextObject());

			character = source.nextCharacter();
			switch (character) {
			case ';':
			case ',':
				if (source.nextCharacter() == '}') {
					return;
				}
				source.back();
				break;
			case '}':
				return;
			default:
				throw new IllegalArgumentException("Expected a ',' or '}' instead '" + character + "'");
			}
		}
	}

	/**
	 * get object
	 * 
	 * @param key
	 * @return JSONObject
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	final public JSONObject get(String key) throws NullPointerException, IllegalArgumentException {
		return this.get(key, false);
	}

	/**
	 * get object
	 * 
	 * @param key
	 * @param allowNull
	 * @return JSONObject
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	final public JSONObject get(String key, boolean allowNull) throws NullPointerException, IllegalArgumentException {
		if (key == null) {
			throw new NullPointerException();
		}
		JSONObject object = objects.get(key);
		if (object == null && !allowNull) {
			throw new IllegalArgumentException("Key not found: " + key);
		}
		return object;
	}

	/**
	 * contains object key
	 * 
	 * @param key
	 * @return boolean [true if contains the object key]
	 * @throws NullPointerException
	 */
	final public boolean contains(String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException();
		}
		return objects.containsKey(key);
	}

	/**
	 * get objects keys
	 * 
	 * @return Object[] [list of objects keys]
	 */
	final public Object[] keys() {
		return objects.keySet().toArray();
	}

	/**
	 * get object size
	 * 
	 * @return int [object size]
	 */
	final public int size() {
		return objects.size();
	}

	/**
	 * is object element
	 * 
	 * @return boolean [true if its JSONElement]
	 */
	final public boolean isElement() {
		return true;
	}

	/**
	 * is object element list
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
		return false;
	}

	/**
	 * close object
	 * 
	 */
	final public void close() {
		Object[] keys = objects.keySet().toArray();
		for (int x = 0; x < keys.length; x++) {
			objects.get(keys[x]).close();
		}
		objects.clear();
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
		Object[] keys = objects.keySet().toArray();
		for (int x = 0; x < keys.length; x++) {
			JSONObject object = objects.get(keys[x]);
			if (object.isList()) {
				((JSONElementList) object).toXml(xml, (String) keys[x]);
			} else {
				object.toXml(xml.newChild((String) keys[x]));
			}
		}
	}
}