package core.lang.basic.array;

import core.lang.basic.BasicArray;
import core.lang.basic.BasicIndexArray;
import core.lang.basic.BasicType;
import core.lang.basic.type.StringType;

/**
 * A String Array.
 */
public class StringArray implements BasicArray {

	/** The array. */
	private String[] array;
	/** The size. */
	private int size = 0;
	/** The memory bytes. */
	private long memoryBytes = 0;

	/**
	 * Returns the memory size in bytes.
	 * @return the memory size in bytes.
	 */
	public final long getMemoryBytes() {
		return 32 + (array.length * 4) + memoryBytes;
	}

	/**
	 * Set the size.
	 * @param size the size.
	 */
	public void setSize(int size) {
		if (size < 0 || size > array.length) {
			throw new IllegalArgumentException("size=" + size);
		}
		this.size = size;
	}

	/**
	 * Creates a new StringArray.
	 * @param array the array.
	 */
	public StringArray(String[] array) {
		this.array = array;
		this.size = array.length;
	}

	/**
	 * Creates a new integer array
	 * @param initialCapacity the initial capacity.
	 */
	public StringArray(int initialCapacity) {
		if (initialCapacity < 1) {
			initialCapacity = 1;
		}
		array = new String[initialCapacity];
	}

	/**
	 * Creates a new integer array
	 */
	public StringArray() {
		this(32);
	}

	/**
	 * Returns the size of the array.
	 * @return the size.
	 */
	public final int size() {
		return size;
	}

	/**
	 * Returns the capacity of the array.
	 * @return the capacity.
	 */
	public final int capacity() {
		return array.length;
	}

	/**
	 * Grow the array.
	 * @param minimumCapacity the minimum capacity.
	 */
	public final void ensureCapacity(int minimumCapacity) {
		if (array.length < minimumCapacity) {
			int newLength = array.length;
			while (newLength < minimumCapacity) {
				newLength *= 2;
			}
			String[] newArray = new String[newLength];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array = newArray;
		}
	}

	/**
	 * Returns the integer at the given index.
	 * @param index the index.
	 * @return the integer.
	 */
	public final String get(int index) {
		return array[index];
	}

	/**
	 * Sets the value at the given index.
	 * @param index the index.
	 * @param value the value.
	 */
	public final void set(int index, String value) {
		if (index >= size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		String oldValue = array[index];
		array[index] = value;

		// Memory
		if (oldValue != null) {
			memoryBytes -= ((oldValue.length() * 2) + 40);
		}
		if (value != null) {
			memoryBytes += ((value.length() * 2) + 40);
		}
	}

	/**
	 * Add the given value.
	 * @param value the value.
	 */
	public final void add(String value) {
		ensureCapacity(size + 1);
		array[size] = value;
		size++;

		// Memory
		if (value != null) {
			memoryBytes += ((value.length() * 2) + 40);
		}
	}

	/**
	 * Inserts the value at the given index.
	 * @param index the index.
	 * @param value the value.
	 */
	public final void insert(int index, String value) {
		if (index > size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if (index == size) {
			add(value);
		} else {
			ensureCapacity(size + 1);
			System.arraycopy(array, index, array, index + 1, size - index);
			array[index] = value;
			size++;

			// Memory
			if (value != null) {
				memoryBytes += ((value.length() * 2) + 40);
			}
		}
	}

	/**
	 * Clear the array.
	 */
	public final void clear() {
		size = 0;
	}

	/**
	 * Removes the value at the given index.
	 * @param index the index.
	 */
	public final void remove(int index) {
		if (index < 0 || index >= size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		String value = array[index];
		System.arraycopy(array, index + 1, array, index, size - index - 1);
		size--;

		// Memory
		if (value != null) {
			memoryBytes -= ((value.length() * 2) + 40);
		}
	}

	/**
	 * Returns this array as a String.
	 * @return this array as a String.
	 */
	public final String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append(get(i));
		}
		return sb.toString();
	}

	/**
	 * Returns the number of occurrences of the given value.
	 * @param value the value.
	 * @return the count.
	 */
	public final int count(String value, boolean ignoreCase) {
		int count = 0;
		if (ignoreCase) {
			for (int i = 0; i < size(); i++) {
				if (get(i).equalsIgnoreCase(value)) {
					count++;
				}
			}
		} else {
			for (int i = 0; i < size(); i++) {
				if (get(i).equals(value)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Returns the number of occurrences of the given value.
	 * @param value the value.
	 * @return the count.
	 */
	public final int count(String value) {
		return count(value, false);
	}

	/**
	 * Returns the number of occurrences of the given type.
	 * @param type the type.
	 * @return the count.
	 */
	public final int count(BasicType type) {
		return count(((StringType) type).get());
	}

	/**
	 * Returns this as an array.
	 * @return this as an array.
	 */
	public final String[] toArray() {
		String[] array = new String[size];
		System.arraycopy(this.array, 0, array, 0, size);
		return array;
	}

	/**
	 * Returns this as an array.
	 * @param match return this array if sizes match.
	 * @return this as an array.
	 */
	public final String[] toArray(boolean match) {
		if (size == this.array.length) {
			return this.array;
		}
		return toArray();
	}

	/**
	 * Trim to the given maximum size.
	 * @param maxSize the maximum size
	 */
	public final void trim(int maxSize) {
		if (size > maxSize) {
			size = maxSize;
		}
	}

	/**
	 * Returns the first index of the given value.
	 * @param value the value.
	 * @return the first index.
	 */
	public final int indexOf(String value, boolean ignoreCase) {
		if (ignoreCase) {
			for (int i = 0; i < size(); i++) {
				if (get(i).equalsIgnoreCase(value)) {
					return i;
				}
			}

		} else {
			for (int i = 0; i < size(); i++) {
				if (get(i).equals(value)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns the first index of the given value.
	 * @param value the value.
	 * @return the first index.
	 */
	public final int indexOf(String value) {
		return indexOf(value, false);
	}

	/**
	 * Returns true if this contains the given value.
	 * @param value the value.
	 * @return true if this contains the given value.
	 */
	public final boolean contains(String value) {
		return indexOf(value, false) != -1;
	}

	/**
	 * Returns true if this contains the given value.
	 * @param value the value.
	 * @param ignoreCase true to ignore the case.
	 * @return true if this contains the given value.
	 */
	public final boolean contains(String value, boolean ignoreCase) {
		return indexOf(value, ignoreCase) != -1;
	}
	
	/**
	 * Returns an array of all indexes of the given value.
	 * @param value the value.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(String value) {
		return arrayIndexOf(value, new IntegerArray(), false);
	}

	/**
	 * Returns an array of all indexes of the given value.
	 * @param value the value.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(String value, boolean ignoreCase) {
		return arrayIndexOf(value, new IntegerArray(), ignoreCase);
	}

	/**
	 * Returns an array of all indexes of the given value.
	 * @param value the value.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(String value, BasicIndexArray array, boolean ignoreCase) {
		if (ignoreCase) {
			for (int i = 0; i < size(); i++) {
				if (get(i).equalsIgnoreCase(value)) {
					array.add(i);
				}
			}
		} else {
			for (int i = 0; i < size(); i++) {
				if (get(i).equals(value)) {
					array.add(i);
				}
			}
		}
		return array;
	}

	/**
	 * Returns an array of all indexes of the given value.
	 * @param value the value.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(String value, BasicIndexArray array) {
		return arrayIndexOf(value, array, false);
	}

	/**
	 * Returns the index of the given type.
	 * @param type the type.
	 * @return the index.
	 */
	public final int indexOf(BasicType type) {
		return indexOf(((StringType) type).get());
	}

	/**
	 * Returns the index of the given type.
	 * @param type the type.
	 * @return the index.
	 */
	public final int indexOf(BasicType type, boolean ignoreCase) {
		return indexOf(((StringType) type).get(), ignoreCase);
	}

	/**
	 * Returns an array of all indexes of the given type.
	 * @param type the type.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(BasicType type) {
		return arrayIndexOf(((StringType) type).get());
	}

	/**
	 * Add the given type.
	 * @param type the type.
	 */
	public void add(BasicType type) {
		add(((StringType) type).get());
	}

	/**
	 * Set the given index.
	 * @param index the index.
	 * @param type the type.
	 */
	public void set(int index, BasicType type) {
		set(index, ((StringType) type).get());
	}

}