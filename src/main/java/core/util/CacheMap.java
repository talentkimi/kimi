package core.util;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A highly versatile map for caching.
 * <p>
 * For efficiency the map expires entries when possible:
 * <p>
 * If the map has a maximum size it guarantees not to grow above that size, it
 * will force-expire the entry at the back of the linked list for each put()
 * when it reaches its limit.
 * <p>
 * If the map has time-expired entries, they will only be removed as accessed or
 * when sufficient put() operations have forced them to the back of the linked
 * list.
 * <p>
 * A time-limited map that is also size-limited does not guarantee to expire the
 * oldest timed entry when its size limit is reached, and may remove unexpired
 * entries to maintain its maximum size.
 * <p>
 * Entry removal is a constant time operation along with get() and put() to
 * ensure that map performance does not significantly degrade with size. 
 * <p>
 * This map is an effective clone of {@LinkedHashMap} merged and with small
 * ammendments.
 * 
 * @param <K> the key type.
 * @param <V> the value type.
 * 
 * @see LinkedHashMap
 */
public class CacheMap<K, V> implements Map<K, V>, Cloneable {

	/** The default initial capacity - MUST be a power of two. */
	static final int DEFAULT_INITIAL_CAPACITY = 16;
	/** The maximum capacity. MUST be a power of two <= 1<<30. */
	static final int MAXIMUM_CAPACITY = 1 << 30;
	/** The load factor used when none specified in constructor. */
	static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/** Value representing null keys inside tables. */
	private static final Object NULL_KEY = new Object();

	/**
	 * Returns internal representation for key. Use NULL_KEY if key is null.
	 */
	private static final <T> T maskNull(T key) {
		return key == null ? (T) NULL_KEY : key;
	}

	/**
	 * Returns key represented by specified internal representation.
	 */
	private static final <T> T unmaskNull(T key) {
		return (key == NULL_KEY ? null : key);
	}

	/**
	 * Applies a supplemental hash function to a given hashCode, which defends
	 * against poor quality hash functions. This is critical because CacheMap
	 * uses power-of-two length hash tables, that otherwise encounter collisions
	 * for hashCodes that do not differ in lower bits.
	 */
	private static final int hash(int h) {
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	/**
	 * Check for equality of non-null reference x and possibly-null y.
	 */
	private static final boolean eq(Object x, Object y) {
		return x == y || x.equals(y);
	}

	/**
	 * Returns index for hash code h.
	 */
	private static final int indexFor(int h, int length) {
		return h & (length - 1);
	}

	private static final int getTimeNow() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	/** The table, resized as necessary. Length MUST Always be a power of two. */
	private transient Entry[] table;
	/** The number of key-value mappings contained in this identity hash map. */
	private transient int size;
	/** * The next size value at which to resize (capacity * load factor). */
	private int threshold;
	/** The load factor for the hash table. */
	private final float loadFactor;
	/** The number of times this CacheMap has been structurally modified. */
	private transient volatile int modCount;

	/** The head of the doubly linked list. */
	private transient Entry<K, V> header;

	/** The state-less key set view. */
	private transient volatile Set<K> keySet = null;
	/** The state-less value collection view. */
	private transient volatile Collection<V> values = null;
	/** The state-less entry set view. */
	private transient Set<Map.Entry<K, V>> entrySet = null;

	/** The maximum size (zero for unlimited). */
	private int maximumSize = 0;
	/** The timeout. */
	private int timeout = 0;

	// internal utilities

	/**
	 * Constructs an empty <tt>CacheMap</tt> with the specified initial
	 * capacity and load factor.
	 * 
	 * @param initialCapacity The initial capacity.
	 * @param loadFactor The load factor.
	 * @throws IllegalArgumentException if the initial capacity is negative or
	 * the load factor is nonpositive.
	 */
	public CacheMap(int initialCapacity, float loadFactor, int maximumSize, long timeout) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		}
		if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
			throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		}
		if (maximumSize < 0) {
			throw new IllegalArgumentException("maximumSize=" + maximumSize);
		}
		if (timeout < 0) {
			throw new IllegalArgumentException("timeout=" + timeout);
		}
		if (initialCapacity > MAXIMUM_CAPACITY) {
			initialCapacity = MAXIMUM_CAPACITY;
		}

		// Find a power of 2 >= initialCapacity
		int capacity = 1;
		while (capacity < initialCapacity)
			capacity <<= 1;

		this.maximumSize = maximumSize;
		this.timeout = (int) (timeout / 1000);
		this.loadFactor = loadFactor;
		this.threshold = (int) (capacity * loadFactor);
		this.table = new Entry[capacity];
		init();
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 * 
	 * @return the number of key-value mappings in this map.
	 */
	public synchronized int collisions() {
		int nonCollisions = 0;
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				nonCollisions++;
			}
		}
		return size - nonCollisions;
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 * 
	 * @return the number of key-value mappings in this map.
	 */
	public synchronized float collisionPercent() {
		return ((float) collisions() / (float) size) * 100f;
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 * 
	 * @return the number of key-value mappings in this map.
	 */
	public synchronized int size() {
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 * 
	 * @return <tt>true</tt> if this map contains no key-value mappings.
	 */
	public synchronized boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified
	 * key.
	 * 
	 * @param key The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 * key.
	 */
	public synchronized boolean containsKey(Object key) {
		Object k = maskNull(key);
		int hash = hash(k.hashCode());
		int index = indexFor(hash, table.length);
		Entry entry = table[index];
		while (entry != null) {
			if (entry.hash == hash && eq(k, entry.key)) {
				if (timeout != 0) {
					if (entry.hasExpired(timeout, getTimeNow())) {
						//System.out.println("removing expired entry: " + entry.key + " (" + (getTimeNow() - entry.timeCreated) + " seconds)");
						removeEntryForKey(key);
						return false;
					}
				}
				return true;
			}
			entry = entry.next;
		}
		return false;
	}

	/**
	 * Returns the entry associated with the specified key in the CacheMap.
	 * Returns null if the CacheMap contains no mapping for this key.
	 */
	private final Entry<K, V> getEntry(Object key) {
		Object k = maskNull(key);
		int hash = hash(k.hashCode());
		int i = indexFor(hash, table.length);
		Entry<K, V> entry = table[i];
		while (entry != null && !(entry.hash == hash && eq(k, entry.key)))
			entry = entry.next;
		return entry;
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for this key, the old value is
	 * replaced.
	 * 
	 * @param key key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 * @return previous value associated with specified key, or <tt>null</tt>
	 * if there was no mapping for key. A <tt>null</tt> return can also
	 * indicate that the CacheMap previously associated <tt>null</tt> with the
	 * specified key.
	 */
	public synchronized V put(K key, V value) {
		if (key == null)
			return putForNullKey(value);
		int hash = hash(key.hashCode());
		int index = indexFor(hash, table.length);
		for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
			Object k;
			if (entry.hash == hash && ((k = entry.key) == key || key.equals(k))) {
				V oldValue = entry.value;
				entry.value = value;
				entry.recordAccess(this, true, getTimeNow());
				return oldValue;
			}
		}

		modCount++;
		addEntry(hash, key, value, index);
		return null;
	}

	private final V putForNullKey(V value) {
		int hash = hash(NULL_KEY.hashCode());
		int i = indexFor(hash, table.length);

		for (Entry<K, V> entry = table[i]; entry != null; entry = entry.next) {
			if (entry.key == NULL_KEY) {
				V oldValue = entry.value;
				entry.value = value;
				entry.recordAccess(this, true, getTimeNow());
				return oldValue;
			}
		}

		modCount++;
		addEntry(hash, (K) NULL_KEY, value, i);
		return null;
	}

	/**
	 * This method is used instead of put by constructors and pseudoconstructors
	 * (clone, readObject). It does not resize the table, check for
	 * comodification, etc. It calls createEntry rather than addEntry.
	 */
	private final void putForCreate(K key, V value) {
		K k = maskNull(key);
		int hash = hash(k.hashCode());
		int index = indexFor(hash, table.length);

		/**
		 * Look for preexisting entry for key. This will never happen for clone
		 * or deserialize. It will only happen for construction if the input Map
		 * is a sorted map whose ordering is inconsistent w/ equals.
		 */
		for (Entry<K, V> e = table[index]; e != null; e = e.next) {
			if (e.hash == hash && eq(k, e.key)) {
				e.value = value;
				return;
			}
		}

		createEntry(hash, k, value, index);
	}

	private final void putAllForCreate(Map<? extends K, ? extends V> m) {
		for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m.entrySet().iterator(); i.hasNext();) {
			Map.Entry<? extends K, ? extends V> e = i.next();
			putForCreate(e.getKey(), e.getValue());
		}
	}

	/**
	 * Rehashes the contents of this map into a new array with a larger
	 * capacity. This method is called automatically when the number of keys in
	 * this map reaches its threshold. If current capacity is MAXIMUM_CAPACITY,
	 * this method does not resize the map, but sets threshold to
	 * Integer.MAX_VALUE. This has the effect of preventing future calls.
	 * 
	 * @param newCapacity the new capacity, MUST be a power of two; must be
	 * greater than current capacity unless current capacity is MAXIMUM_CAPACITY
	 * (in which case value is irrelevant).
	 */
	private final void resize(int newCapacity) {
		//System.out.println("resize -> " + newCapacity);
		Entry[] oldTable = table;
		int oldCapacity = oldTable.length;
		if (oldCapacity == MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}

		Entry[] newTable = new Entry[newCapacity];
		transfer(newTable);
		table = newTable;
		threshold = (int) (newCapacity * loadFactor);
	}

	/**
	 * Copies all of the mappings from the specified map to this map These
	 * mappings will replace any mappings that this map had for any of the keys
	 * currently in the specified map.
	 * 
	 * @param m mappings to be stored in this map.
	 * @throws NullPointerException if the specified map is null.
	 */
	public synchronized void putAll(Map<? extends K, ? extends V> m) {
		int numKeysToBeAdded = m.size();
		if (numKeysToBeAdded == 0)
			return;

		/*
		 * Expand the map if the map if the number of mappings to be added is
		 * greater than or equal to threshold. This is conservative; the obvious
		 * condition is (m.size() + size) >= threshold, but this condition could
		 * result in a map with twice the appropriate capacity, if the keys to
		 * be added overlap with the keys already in this map. By using the
		 * conservative calculation, we subject ourself to at most one extra
		 * resize.
		 */
		if (numKeysToBeAdded > threshold) {
			int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
			if (targetCapacity > MAXIMUM_CAPACITY)
				targetCapacity = MAXIMUM_CAPACITY;
			int newCapacity = table.length;
			while (newCapacity < targetCapacity)
				newCapacity <<= 1;
			if (newCapacity > table.length)
				resize(newCapacity);
		}

		for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m.entrySet().iterator(); i.hasNext();) {
			Map.Entry<? extends K, ? extends V> e = i.next();
			put(e.getKey(), e.getValue());
		}
	}

	/**
	 * Removes the mapping for this key from this map if present.
	 * 
	 * @param key key whose mapping is to be removed from the map.
	 * @return previous value associated with specified key, or <tt>null</tt>
	 * if there was no mapping for key. A <tt>null</tt> return can also
	 * indicate that the map previously associated <tt>null</tt> with the
	 * specified key.
	 */
	public synchronized V remove(Object key) {
		Entry<K, V> e = removeEntryForKey(key);
		return (e == null ? null : e.value);
	}

	/**
	 * Removes and returns the entry associated with the specified key in the
	 * CacheMap. Returns null if the CacheMap contains no mapping for this key.
	 */
	protected Entry<K, V> removeEntryForKey(Object key) {
		Object k = maskNull(key);
		int hash = hash(k.hashCode());
		int i = indexFor(hash, table.length);
		Entry<K, V> prev = table[i];
		Entry<K, V> e = prev;

		while (e != null) {
			Entry<K, V> next = e.next;
			if (e.hash == hash && eq(k, e.key)) {
				modCount++;
				size--;
				if (prev == e)
					table[i] = next;
				else
					prev.next = next;
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	/**
	 * Special version of remove for EntrySet.
	 */
	private final Entry<K, V> removeMapping(Object o) {
		if (!(o instanceof Map.Entry))
			return null;

		Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
		Object k = maskNull(entry.getKey());
		int hash = hash(k.hashCode());
		int i = indexFor(hash, table.length);
		Entry<K, V> prev = table[i];
		Entry<K, V> current = prev;

		while (current != null) {
			Entry<K, V> next = current.next;
			if (current.hash == hash && current.equals(entry)) {
				modCount++;
				size--;
				if (prev == current)
					table[i] = next;
				else
					prev.next = next;
				current.recordRemoval(this);
				return current;
			}
			prev = current;
			current = next;
		}

		return current;
	}

	/**
	 * Returns a shallow copy of this <tt>CacheMap</tt> instance: the keys and
	 * values themselves are not cloned.
	 * 
	 * @return a shallow copy of this map.
	 */
	public final Object clone() {
		CacheMap<K, V> result = null;
		try {
			result = (CacheMap<K, V>) super.clone();
		} catch (CloneNotSupportedException e) {
			// assert false;
		}
		result.table = new Entry[table.length];
		result.entrySet = null;
		result.modCount = 0;
		result.size = 0;
		result.init();
		result.putAllForCreate(this);

		return result;
	}

	private final static class Entry<K, V> implements Map.Entry<K, V> {
		private final K key;
		private V value;
		private final int hash;
		private Entry<K, V> next;

		private Entry<K, V> before, after;

		private int accessCount;
		private int timeCreated;

		/**
		 * Create new entry.
		 */
		private Entry(int h, K k, V v, Entry<K, V> n) {
			value = v;
			next = n;
			key = k;
			hash = h;
			timeCreated = getTimeNow();
			accessCount = 0;
		}

		public final boolean hasExpired(int timeout, int timeNow) {
			return timeout != 0 && timeCreated < timeNow - timeout;
		}

		public final K getKey() {
			return CacheMap.<K> unmaskNull(key);
		}

		public final V getValue() {
			return value;
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		public final boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry e = (Map.Entry) o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && k1.equals(k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;
		}

		public final int hashCode() {
			return (key == NULL_KEY ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
		}

		public final String toString() {
			return getKey() + "=" + getValue();
		}

		// These fields comprise the doubly linked list used for iteration.

		/**
		 * Remove this entry from the linked list.
		 */
		private final void remove() {
			before.after = after;
			after.before = before;
		}

		/**
		 * Insert this entry before the specified existing entry in the list.
		 */
		private final void addBefore(Entry<K, V> existingEntry) {
			after = existingEntry;
			before = existingEntry.before;
			before.after = this;
			after.before = this;
		}

		/**
		 * This method is invoked by the superclass whenever the value of a
		 * pre-existing entry is read by Map.get or modified by Map.set. If the
		 * enclosing Map is access-ordered, it moves the entry to the end of the
		 * list; otherwise, it does nothing.
		 */
		private final void recordAccess(CacheMap<K, V> map, boolean newEntry, int timeNow) {
			accessCount++;

			// Update time
			if (newEntry) {
				this.timeCreated = timeNow;
			}

			// Move to back of queue
			if (newEntry) {
				map.modCount++;
				remove();
				addBefore(map.header);
			}
		}

		private final void recordRemoval(CacheMap<K, V> map) {
			remove();
		}
	}

	// Views

	/**
	 * Returns a set view of the keys contained in this map. The set is backed
	 * by the map, so changes to the map are reflected in the set, and
	 * vice-versa. The set supports element removal, which removes the
	 * corresponding mapping from this map, via the <tt>Iterator.remove</tt>,
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
	 * <tt>clear</tt> operations. It does not support the <tt>add</tt> or
	 * <tt>addAll</tt> operations.
	 * 
	 * @return a set view of the keys contained in this map.
	 */
	public synchronized Set<K> keySet() {
		Set<K> ks = keySet;
		return (ks != null ? ks : (keySet = new KeySet()));
	}

	private final class KeySet extends AbstractSet<K> {
		public Iterator<K> iterator() {
			return newKeyIterator();
		}

		public synchronized int size() {
			return size;
		}

		public boolean contains(Object o) {
			return containsKey(o);
		}

		public boolean remove(Object o) {
			return CacheMap.this.removeEntryForKey(o) != null;
		}

		public void clear() {
			CacheMap.this.clear();
		}
	}

	/**
	 * Returns a collection view of the values contained in this map. The
	 * collection is backed by the map, so changes to the map are reflected in
	 * the collection, and vice-versa. The collection supports element removal,
	 * which removes the corresponding mapping from this map, via the
	 * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
	 * operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
	 * operations.
	 * 
	 * @return a collection view of the values contained in this map.
	 */
	public synchronized Collection<V> values() {
		Collection<V> vs = values;
		return (vs != null ? vs : (values = new Values()));
	}

	private final class Values extends AbstractCollection<V> {
		public Iterator<V> iterator() {
			return newValueIterator();
		}

		public int size() {
			return size;
		}

		public boolean contains(Object o) {
			return containsValue(o);
		}

		public void clear() {
			CacheMap.this.clear();
		}
	}

	/**
	 * Returns a collection view of the mappings contained in this map. Each
	 * element in the returned collection is a <tt>Map.Entry</tt>. The
	 * collection is backed by the map, so changes to the map are reflected in
	 * the collection, and vice-versa. The collection supports element removal,
	 * which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
	 * operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
	 * operations.
	 * 
	 * @return a collection view of the mappings contained in this map.
	 */
	public final Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> es = entrySet;
		return (es != null ? es : (entrySet = (Set<Map.Entry<K, V>>) (Set) new EntrySet()));
	}

	private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
		public Iterator<Map.Entry<K, V>> iterator() {
			return newEntryIterator();
		}

		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<K, V> e = (Map.Entry<K, V>) o;
			Entry<K, V> candidate = getEntry(e.getKey());
			return candidate != null && candidate.equals(e);
		}

		public boolean remove(Object o) {
			return removeMapping(o) != null;
		}

		public int size() {
			return size;
		}

		public void clear() {
			CacheMap.this.clear();
		}
	}

	/**
	 * Save the state of the <tt>CacheMap</tt> instance to a stream (i.e.,
	 * serialize it).
	 * 
	 * @serialData The <i>capacity</i> of the CacheMap (the length of the
	 * bucket array) is emitted (int), followed by the <i>size</i> of the
	 * CacheMap (the number of key-value mappings), followed by the key (Object)
	 * and value (Object) for each key-value mapping represented by the CacheMap
	 * The key-value mappings are emitted in the order that they are returned by
	 * <tt>entrySet().iterator()</tt>.
	 */
	private final void writeObject(java.io.ObjectOutputStream s) throws IOException {
		Iterator<Map.Entry<K, V>> i = entrySet().iterator();

		// Write out the threshold, loadfactor, and any hidden stuff
		s.defaultWriteObject();

		// Write out number of buckets
		s.writeInt(table.length);

		// Write out size (number of Mappings)
		s.writeInt(size);

		// Write out keys and values (alternating)
		while (i.hasNext()) {
			Map.Entry<K, V> e = i.next();
			s.writeObject(e.getKey());
			s.writeObject(e.getValue());
		}
	}

	/**
	 * Reconstitute the <tt>CacheMap</tt> instance from a stream (i.e.,
	 * deserialize it).
	 */
	private final void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
		// Read in the threshold, loadfactor, and any hidden stuff
		s.defaultReadObject();

		// Read in number of buckets and allocate the bucket array;
		int numBuckets = s.readInt();
		table = new Entry[numBuckets];

		init(); // Give subclass a chance to do its thing.

		// Read in size (number of Mappings)
		int size = s.readInt();

		// Read the keys and values, and put the mappings in the CacheMap
		for (int i = 0; i < size; i++) {
			K key = (K) s.readObject();
			V value = (V) s.readObject();
			putForCreate(key, value);
		}
	}

	// These methods are used when serializing HashSets
	int capacity() {
		return table.length;
	}

	float loadFactor() {
		return loadFactor;
	}

	// Views

	// Comparison and hashing

	/**
	 * Compares the specified object with this map for equality. Returns
	 * <tt>true</tt> if the given object is also a map and the two maps
	 * represent the same mappings. More formally, two maps <tt>t1</tt> and
	 * <tt>t2</tt> represent the same mappings if
	 * <tt>t1.keySet().equals(t2.keySet())</tt> and for every key <tt>k</tt>
	 * in <tt>t1.keySet()</tt>, <tt> (t1.get(k)==null ? t2.get(k)==null :
	 * t1.get(k).equals(t2.get(k))) </tt>.
	 * This ensures that the <tt>equals</tt> method works properly across
	 * different implementations of the map interface.
	 * <p>
	 * This implementation first checks if the specified object is this map; if
	 * so it returns <tt>true</tt>. Then, it checks if the specified object
	 * is a map whose size is identical to the size of this set; if not, it
	 * returns <tt>false</tt>. If so, it iterates over this map's
	 * <tt>entrySet</tt> collection, and checks that the specified map
	 * contains each mapping that this map contains. If the specified map fails
	 * to contain such a mapping, <tt>false</tt> is returned. If the iteration
	 * completes, <tt>true</tt> is returned.
	 * 
	 * @param o object to be compared for equality with this map.
	 * @return <tt>true</tt> if the specified object is equal to this map.
	 */
	public final boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Map))
			return false;
		Map<K, V> t = (Map<K, V>) o;
		if (t.size() != size())
			return false;

		try {
			Iterator<Map.Entry<K, V>> i = entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry<K, V> e = i.next();
				K key = e.getKey();
				V value = e.getValue();
				if (value == null) {
					if (!(t.get(key) == null && t.containsKey(key)))
						return false;
				} else {
					if (!value.equals(t.get(key)))
						return false;
				}
			}
		} catch (ClassCastException unused) {
			return false;
		} catch (NullPointerException unused) {
			return false;
		}

		return true;
	}

	/**
	 * Returns the hash code value for this map. The hash code of a map is
	 * defined to be the sum of the hash codes of each entry in the map's
	 * <tt>entrySet()</tt> view. This ensures that <tt>t1.equals(t2)</tt>
	 * implies that <tt>t1.hashCode()==t2.hashCode()</tt> for any two maps
	 * <tt>t1</tt> and <tt>t2</tt>, as required by the general contract of
	 * Object.hashCode.
	 * <p>
	 * This implementation iterates over <tt>entrySet()</tt>, calling
	 * <tt>hashCode</tt> on each element (entry) in the Collection, and adding
	 * up the results.
	 * 
	 * @return the hash code value for this map.
	 * @see Object#hashCode()
	 * @see Object#equals(Object)
	 * @see Set#equals(Object)
	 */
	public final int hashCode() {
		int h = 0;
		Iterator<Map.Entry<K, V>> i = entrySet().iterator();
		while (i.hasNext())
			h += i.next().hashCode();
		return h;
	}

	/**
	 * Returns a string representation of this map. The string representation
	 * consists of a list of key-value mappings in the order returned by the
	 * map's <tt>entrySet</tt> view's iterator, enclosed in braces (<tt>"{}"</tt>).
	 * Adjacent mappings are separated by the characters <tt>", "</tt> (comma
	 * and space). Each key-value mapping is rendered as the key followed by an
	 * equals sign (<tt>"="</tt>) followed by the associated value. Keys and
	 * values are converted to strings as by <tt>String.valueOf(Object)</tt>.
	 * <p>
	 * This implementation creates an empty string buffer, appends a left brace,
	 * and iterates over the map's <tt>entrySet</tt> view, appending the
	 * string representation of each <tt>map.entry</tt> in turn. After
	 * appending each entry except the last, the string <tt>", "</tt> is
	 * appended. Finally a right brace is appended. A string is obtained from
	 * the stringbuffer, and returned.
	 * 
	 * @return a String representation of this map.
	 */
	public final String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{");

		Iterator<Map.Entry<K, V>> i = entrySet().iterator();
		boolean hasNext = i.hasNext();
		while (hasNext) {
			Map.Entry<K, V> e = i.next();
			K key = e.getKey();
			V value = e.getValue();
			if (key == this)
				buf.append("(this Map)");
			else
				buf.append(key);
			buf.append("=");
			if (value == this)
				buf.append("(this Map)");
			else
				buf.append(value);
			hasNext = i.hasNext();
			if (hasNext)
				buf.append(", ");
		}

		buf.append("}");
		return buf.toString();
	}

	/**
	 * Called by superclass constructors and pseudoconstructors (clone,
	 * readObject) before any entries are inserted into the map. Initializes the
	 * chain.
	 */
	private final void init() {
		header = new Entry<K, V>(-1, null, null, null);
		header.before = header.after = header;
	}

	/**
	 * Transfer all entries to new table array. This method is called by
	 * superclass resize. It is overridden for performance, as it is faster to
	 * iterate using our linked list.
	 */
	private final void transfer(CacheMap.Entry[] newTable) {
		int newCapacity = newTable.length;
		for (Entry<K, V> entry = header.after; entry != header; entry = entry.after) {
			int index = indexFor(entry.hash, newCapacity);
			entry.next = newTable[index];
			newTable[index] = entry;
		}
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the
	 * specified value.
	 * 
	 * @param value value whose presence in this map is to be tested.
	 * @return <tt>true</tt> if this map maps one or more keys to the
	 * specified value.
	 */
	public synchronized boolean containsValue(Object value) {
		// Overridden to take advantage of faster iterator
		if (value == null) {
			for (Entry entry = header.after; entry != header; entry = entry.after)
				if (entry.value == null)
					return true;
		} else {
			for (Entry entry = header.after; entry != header; entry = entry.after)
				if (value.equals(entry.value))
					return true;
		}
		return false;
	}

	/**
	 * Returns the value to which this map maps the specified key. Returns
	 * <tt>null</tt> if the map contains no mapping for this key. A return
	 * value of <tt>null</tt> does not <i>necessarily</i> indicate that the
	 * map contains no mapping for the key; it's also possible that the map
	 * explicitly maps the key to <tt>null</tt>. The <tt>containsKey</tt>
	 * operation may be used to distinguish these two cases.
	 * 
	 * @return the value to which this map maps the specified key.
	 * @param key key whose associated value is to be returned.
	 */
	public synchronized V get(Object key) {
		return get(key, true);
	}

	public synchronized V get(Object key, boolean recordAccess) {
		Entry<K, V> entry = getEntry(key);
		if (entry == null) {
			return null;
		}

		// Check the timeouts
		int timeNow = getTimeNow();
		if (timeout != 0) {
			if (entry.hasExpired(timeout, timeNow)) {
				//System.out.println("removing expired entry: " + entry.key + " (" + (getTimeNow() - entry.timeCreated) + " seconds)");
				removeEntryForKey(key);
				return null;
			}
		}

		if (recordAccess) {
			entry.recordAccess(this, false, timeNow);
		}
		return entry.value;
	}

	/**
	 * Removes all mappings from this map.
	 */
	public synchronized void clear() {
		modCount++;
		Entry[] tab = table;
		for (int i = 0; i < tab.length; i++)
			tab[i] = null;
		size = 0;

		header.before = header.after = header;
	}

	private abstract class HashIterator<T> implements Iterator<T> {
		Entry<K, V> nextEntry = header.after;
		Entry<K, V> lastReturned = null;

		/**
		 * The modCount value that the iterator believes that the backing List
		 * should have. If this expectation is violated, the iterator has
		 * detected concurrent modification.
		 */
		int expectedModCount = modCount;

		public boolean hasNext() {
			return nextEntry != header;
		}

		public void remove() {
			if (lastReturned == null)
				throw new IllegalStateException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();

			CacheMap.this.remove(lastReturned.key);
			lastReturned = null;
			expectedModCount = modCount;
		}

		Entry<K, V> nextEntry() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			if (nextEntry == header)
				throw new NoSuchElementException();

			Entry<K, V> entry = lastReturned = nextEntry;
			nextEntry = entry.after;
			return entry;
		}
	}

	private final class KeyIterator extends HashIterator<K> {
		public K next() {
			return nextEntry().getKey();
		}
	}

	private final class ValueIterator extends HashIterator<V> {
		public V next() {
			return nextEntry().value;
		}
	}

	private final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
		public Map.Entry<K, V> next() {
			return nextEntry();
		}
	}

	// These Overrides alter the behavior of superclass view iterator() methods
	private final Iterator<K> newKeyIterator() {
		return new KeyIterator();
	}

	private final Iterator<V> newValueIterator() {
		return new ValueIterator();
	}

	private final Iterator<Map.Entry<K, V>> newEntryIterator() {
		return new EntryIterator();
	}

	/**
	 * This override alters behavior of superclass put method. It causes newly
	 * allocated entry to get inserted at the end of the linked list and removes
	 * the eldest entry if appropriate.
	 */
	private final void addEntry(int hash, K key, V value, int bucketIndex) {
		createEntry(hash, key, value, bucketIndex);

		// Remove entries on addition to prevent map growing larger if poss.
		if (removeEntries() == 0 && size >= threshold) {
			resize(2 * table.length);
		}
	}

	/**
	 * This override differs from addEntry in that it doesn't resize the table
	 * or remove the eldest entry.
	 */
	private final void createEntry(int hash, K key, V value, int bucketIndex) {
		CacheMap.Entry<K, V> old = table[bucketIndex];
		Entry<K, V> entry = new Entry<K, V>(hash, key, value, old);
		table[bucketIndex] = entry;
		entry.addBefore(header);
		size++;
	}

	private final int removeEntries() {

		// The maximum size
		if (maximumSize != 0) {
			if (size > maximumSize && header.after != null) {
				//System.out.println("removing maximum entry: " + header.after.key + " (" + size + ">" + maximumSize + ")");
				removeEntryForKey(header.after.key);
				return 1;
			}
		}

		// The timeouts
		if (timeout != 0) {
			int timeNow = getTimeNow();
			while (header.after != header && header.after.hasExpired(timeout, timeNow)) {
				//System.out.println("removing expired entry: " + header.after.key + " (" + (getTimeNow() - header.after.timeCreated) + " seconds)");
				removeEntryForKey(header.after.key);
			}
		}
		return 0;
	}

}
