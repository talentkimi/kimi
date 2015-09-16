package core.util;

/**
 * A Key-Value Pair List.
 */
public class PairList extends UtilList {

	/**
	 * Returns the key at the given index.
	 * @param index the index.
	 * @return the key at the given index.
	 */
	public Object getKey(int index) {
		return getPair(index).getKey();
	}

	/**
	 * Set the given pair, replacing any existing one.
	 * @param pair the pair to set.
	 * @return the replaced pair.
	 */
	public Pair set(Pair pair) {
		int index = indexOfPair(pair.getKey());
		if (index == -1) {
			add(pair);
			return null;
		} else {
			return (Pair) set(index, pair);
		}
	}

	/**
	 * Set the values stored on the keys that are equal to the given key to the given value.
	 * @param key the key.
	 * @param value the value.
	 */
	public final void setAll(Object key, Object value) {
		if (key == null)
			throw new NullPointerException();

		for (int i = 0; i < size(); i++)
			if (key.equals(getKey(i)))
				set(i, new Pair(key, value));
	}

	/**
	 * Adds the given key-value pair to the query.
	 * @param key the key.
	 * @param value the value.
	 */
	public void addPair(Object key, Object value) {
		Pair pair = new Pair(key, value);
		add(pair);
	}

	/**
	 * Returns the pair at the given index.
	 * @param index the index.
	 * @return the pair at the given index.
	 */
	public Pair getPair(int index) {
		return (Pair) get(index);
	}

	/**
	 * Returns all the values stored on the given key.
	 * @param key the key.
	 * @return the values.
	 */
	public Pair[] getPairs(Object key) {
		if (key == null)
			throw new NullPointerException();
		UtilList values = new UtilList();
		for (int i = 0; i < size(); i++) {
			if (key.equals(getKey(i))) {
				values.add(getPair(i));
			}
		}
		Pair[] pairArray = new Pair[values.size()];
		values.toArray(pairArray);
		return pairArray;
	}

	/**
	 * Returns the index of the given pair key.
	 * @param key the key.
	 * @return the index of the given pair key.
	 */
	private int indexOfPair(Object key) {
		for (int i = 0; i < size(); i++) {
			Pair pair = getPair(i);
			if (pair.getKey().equals(key)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Finds and returns the pair with the given key.
	 * @param key the key.
	 * @param index the index of the key.
	 * @param remove true to remove the pair.
	 * @return the pair.
	 */
	private Pair findPair(Object key, int index, boolean remove) {
		if (key == null)
			throw new NullPointerException();
		for (int i = 0; i < size(); i++) {
			Pair pair = getPair(i);
			if (key.equals(pair.getKey())) {
				if (index == 0) {
					if (remove) {
						remove(i);
					}
					return pair;
				}
				index--;
			}
		}
		return null;
	}

	/**
	 * Removes the pair with the given key.
	 * @param key the key.
	 * @param index the index of the key.
	 * @return the pair with the given key.
	 */
	public Pair removePair(Object key, int index) {
		return findPair(key, index, true);
	}

	/**
	 * Removes the pair with the given key.
	 * @param key the key.
	 * @return the pair with the given key.
	 */
	public Pair removePair(Object key) {
		return removePair(key, 0);
	}

	/**
	 * Returns the first pair with the given key.
	 * @param key the key.
	 * @return the first pair with the given key.
	 */
	public Pair getPair(Object key, int index) {
		return findPair(key, index, false);
	}

	/**
	 * Returns the first pair with the given key.
	 * @param key the key.
	 * @return the first pair with the given key.
	 */
	public Pair getPair(Object key) {
		return getPair(key, 0);
	}

	/**
	 * Returns the value at the given index.
	 * @param index the index.
	 * @return the value at the given index.
	 */
	public Object getValue(int index) {
		return getPair(index).getValue();
	}

	/**
	 * Returns all the values stored on the given key.
	 * @param key the key.
	 * @return the values.
	 */
	public Object[] getValues(Object key) {
		if (key == null)
			throw new NullPointerException();
		UtilList values = new UtilList();
		for (int i = 0; i < size(); i++) {
			if (key.equals(getKey(i))) {
				values.add(getValue(i));
			}
		}
		return values.toArray();
	}

	/**
	 * Removes the pair with the given key.
	 * @param key the key.
	 * @param index the index of the key.
	 * @return the pair with the given key.
	 */
	public Object removeValue(Object key, int index) {
		Pair pair = findPair(key, index, true);
		if (pair == null)
			return null;
		return pair.getValue();
	}

	/**
	 * Removes the pair with the given key.
	 * @param key the key.
	 * @return the pair with the given key.
	 */
	public Object removeValue(Object key) {
		return removeValue(key, 0);
	}

	/**
	 * Returns the first value of the given key.
	 * @param key the key.
	 * @return the first value of the given key.
	 */
	public Object getValue(Object key, int index) {
		Pair pair = getPair(key, index);
		if (pair == null)
			return null;
		return pair.getValue();
	}

	/**
	 * Returns the first value of the given key.
	 * @param key the key.
	 * @return the first value of the given key.
	 */
	public Object getValue(Object key) {
		return getValue(key, 0);
	}

	/**
	 * Returns true if this list contains the given key.
	 * @param key the key.
	 * @return true if this list contains the given key.
	 */
	public boolean containsKey(Object key) {
		for (int i = 0; i < size(); i++) {
			if (getKey(i).equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this list contains the given value.
	 * @param value the value.
	 * @return true if this list contains the given value.
	 */
	public boolean containsValue(Object value) {
		for (int i = 0; i < size(); i++) {
			if (getValue(i).equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the number of values with the given key.
	 * @param key the key.
	 * @return the number of values.
	 */
	public int countKeys(Object key) {
		if (key == null)
			throw new NullPointerException();
		int count = 0;
		for (int i = 0; i < size(); i++) {
			if (key.equals(getKey(i)))
				count++;
		}
		return count;
	}

	/**
	 * Returns the number of values.
	 * @param value the value.
	 * @return the number of values.
	 */
	public int countValues(Object value) {
		if (value == null)
			throw new NullPointerException();
		int count = 0;
		for (int i = 0; i < size(); i++) {
			if (value.equals(getValue(i)))
				count++;
		}
		return count;
	}

}