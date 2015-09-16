package core.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Utility Map. It can be given an initial capacity, ordered and synchronized when created.
 */
public class UtilMap implements Map, Serializable {

	/** The map. * */
	private volatile Map map = null;
	private final int initialCapacity;
	private final boolean synchronize;
	private final boolean ordered;

	/**
	 * Returns the underlying map.
	 * @return the underlying map.
	 */
	public final Map getMap() {
		if (map == null) {
			synchronized (this) {
				if (map == null) {
					Map result;
					if (ordered) {
						if (initialCapacity > 0) {
							result = new LinkedHashMap(initialCapacity);
						} else {
							result = new LinkedHashMap();
						}
					} else {
						if (initialCapacity > 0) {
							result = new HashMap(initialCapacity);
						} else {
							result = new HashMap();
						}
					}
					if (synchronize) {
						map = Collections.synchronizedMap(result);
					} else {
						map = result;
					}
				}
			}
		}
		return map;
	}

	/**
	 * Returns the values as a collection.
	 */
	public Collection values() {
		return map == null ? Collections.EMPTY_SET : getMap().values();
	}

	/**
	 * Returns the entries as a set.
	 */
	public Set entrySet() {
		return map == null ? Collections.EMPTY_SET : getMap().entrySet();
	}

	/**
	 * Returns the keys as a set.
	 */
	public Set keySet() {
		return map == null ? Collections.EMPTY_SET : getMap().keySet();
	}

	/**
	 * Returns the value stored on the given key.
	 * @param key the key.
	 * @return the value.
	 */
	public Object get(Object key) {
		return map == null ? null : getMap().get(key);
	}

	/**
	 * Puts the value in this map on the given key.
	 * @param key the key.
	 * @param value the value.
	 * @return the value replaced.
	 */
	public Object put(Object key, Object value) {
		if (key == null || value == null) {
			throw new NullPointerException();
		}
		return getMap().put(key, value);
	}

	/**
	 * Removes the value on the given key.
	 * @param key the key.
	 * @return the removed value.
	 */
	public Object remove(Object key) {
		return map == null ? null : getMap().remove(key);
	}

	/**
	 * Puts all keys and values from the given map into this one.
	 * @param map the map.
	 */
	public void putAll(Map map) {
		if (map.size() > 0) {
			getMap().putAll(map);
		}
	}

	/**
	 * Returns the size of the map.
	 * @return the size of the map.
	 */
	public int size() {
		return map == null ? 0 : getMap().size();
	}

	/**
	 * Returns true if this map is empty.
	 * @return true if this map is empty.
	 */
	public boolean isEmpty() {
		return map == null || getMap().isEmpty();
	}

	/**
	 * Returns true if this map contains the given key.
	 * @param key the key.
	 * @return true if this map contains the given key.
	 */
	public boolean containsKey(Object key) {
		return map == null ? false : getMap().containsKey(key);
	}

	/**
	 * Returns true if this map contains the given value.
	 * @param value the value.
	 * @return true if this map contains the given value.
	 */
	public boolean containsValue(Object value) {
		return map == null ? false : getMap().containsValue(value);
	}

	/**
	 * Clear the map.
	 */
	public void clear() {
		if (map != null) {
			getMap().clear();
		}
	}

	/**
	 * Returns a string representation of this map.
	 * @return a string representation of this map.
	 */
	public String toString() {
		return getMap().toString();
	}

	/**
	 * Creates a new map.
	 * @param initialCapacity the initial capacity of the new map.
	 * @param ordered true to make this map ordered.
	 * @param synchronize true to synchronize access to all Map interface methods.
	 */
	public UtilMap(int initialCapacity, boolean ordered, boolean synchronize) {
		this.initialCapacity = initialCapacity;
		this.ordered = ordered;
		this.synchronize = synchronize;
	}

	/**
	 * Creates a new unsynchronized map.
	 * @param initialCapacity the initial capacity of the new map.
	 * @param ordered true to keep the keys ordered.
	 */
	public UtilMap(int initialCapacity, boolean ordered) {
		this(initialCapacity, ordered, false);
	}

	/**
	 * Creates a new unordered unsynchronized map.
	 * @param initialCapacity the initial capacity of the new map.
	 */
	public UtilMap(int initialCapacity) {
		this(initialCapacity, true, true);
	}

	/**
	 * Creates a new unordered unsynchronized map.
	 */
	public UtilMap() {
		this(0, true, true);
	}

}
