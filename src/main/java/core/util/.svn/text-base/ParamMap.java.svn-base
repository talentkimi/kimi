package core.util;

import java.util.Iterator;
import java.util.Set;

import core.text.Text;

/**
 * A Parameter Map.
 */
public class ParamMap {

	/** The params. */
	private final UtilMap params = new UtilMap(2, false);

	/**
	 * Clear the params.
	 */
	public final void clear() {
		params.clear();
	}

	/**
	 * Set the value on the given key.
	 * @param key the key.
	 * @param value the value.
	 */
	public void put(String key, String value) {
		// The third parameter requests a new string. Not sure why though. Maybe to absolutely ensure there are not substring memory issues - talk to james
		key = Text.intern(key, 4, true);
		// The third parameter requests a new string. Not sure why though. Maybe to absolutely ensure there are not substring memory issues - talk to james
		value = Text.intern(value, 4, true);
		params.put(key, value);
	}

	/**
	 * Get the value stored on the given key.
	 * @param key the key.
	 * @return the value.
	 */
	public String get(String key) {
		return (String) params.get(key);
	}

	/**
	 * returns a copy of the current map, warning this is shallow copy
	 */
	public ParamMap copy() {
		ParamMap result = new ParamMap();
		Set keys = params.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) params.get(key);
			result.put(key, value);
		}
		return result;

	}

	/**
	 * Turns this map into a copy of the given map, warning this is shallow copy
	 */
	public ParamMap copyFrom(ParamMap map) {
		this.clear();
		this.params.putAll(map.params.getMap());
		return this;

	}

	/**
	 * Returns a string representation of these params.
	 * @return a string representation of these params.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		String[] keys = new String[params.size()];
		params.keySet().toArray(keys);
		for (int i = 0; i < params.size(); i++) {
			String key = keys[i];
			String value = get(keys[i]);
			append(buffer, key, '=');
			buffer.append('=');
			append(buffer, value, '&');
			buffer.append('&');
		}
		return buffer.toString();
	}

	/**
	 * Append a string to the given buffer.
	 * @param buffer the buffer.
	 * @param toAppend the string to append.
	 * @param escape the character to escape.
	 */
	private void append(StringBuffer buffer, String toAppend, char escape) {
		for (int i = 0; i < toAppend.length(); i++) {
			char c = toAppend.charAt(i);
			if (c == escape) {
				buffer.append('\\');
			}
			buffer.append(c);
		}
	}

	/**
	 * Parse this from the given string.
	 * @param s the string.
	 */
	public void fromString(String s) {
		int index = 0;
		while (index != s.length()) {
			int equalsIndex = indexOf(s, '=', index);
			if (equalsIndex == -1) {
				break;
			}
			int andIndex = indexOf(s, '&', equalsIndex + 1);
			if (andIndex == -1) {
				break;
			}
			String key = new String(s.substring(index, equalsIndex));
			String value = new String(s.substring(equalsIndex + 1, andIndex));
			params.put(key, value);
			index = andIndex + 1;
		}
	}

	/**
	 * Returns the index of the given character in a string.
	 * @param s the string.
	 * @param find the character.
	 * @param index the index.
	 * @return the index.
	 */
	private int indexOf(String s, char find, int index) {
		for (int i = index; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				i++;
			} else {
				if (c == find) {
					return i;
				}
			}
		}
		return -1;
	}
}