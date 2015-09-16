package core.lang.basic.array;

import core.lang.basic.BasicArray;
import core.lang.basic.BasicIndexArray;
import core.lang.basic.BasicType;
import core.lang.basic.type.DoubleType;

/**
 * A Double Array.
 */
public class DoubleArray implements BasicArray {

	/** The array. */
	private double[] array;
	/** The size. */
	private int size = 0;

	/**
	 * Returns the memory size in bytes.
	 * @return the memory size in bytes.
	 */
	public final long getMemoryBytes() {
		return 32 + (array.length * 8);
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
	 * Creates a new integer array
	 * @param initialCapacity the initial capacity.
	 */
	public DoubleArray(int initialCapacity) {
		if (initialCapacity < 1) {
			initialCapacity = 1;
		}
		array = new double[initialCapacity];
	}

	/**
	 * Creates a new integer array
	 */
	public DoubleArray() {
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
			double[] newArray = new double[newLength];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array = newArray;
		}
	}

	/**
	 * Returns the integer at the given index.
	 * @param index the index.
	 * @return the integer.
	 */
	public final double get(int index) {
		return array[index];
	}

	/**
	 * Returns the byte at the given index.
	 * @param index the index.
	 * @return the byte.
	 */
	public final byte getByte(int index) {
		return (byte) array[index];
	}

	/**
	 * Returns the short at the given index.
	 * @param index the index.
	 * @return the short.
	 */
	public final short getShort(int index) {
		return (short) array[index];
	}

	/**
	 * Returns the integer at the given index.
	 * @param index the index.
	 * @return the integer.
	 */
	public final int getInt(int index) {
		return (int) array[index];
	}

	/**
	 * Returns the long at the given index.
	 * @param index the index.
	 * @return the long.
	 */
	public final long getLong(int index) {
		return (long) array[index];
	}

	/**
	 * Returns the float at the given index.
	 * @param index the index.
	 * @return the float.
	 */
	public final float getFloat(int index) {
		return (float) array[index];
	}

	/**
	 * Returns the double at the given index.
	 * @param index the index.
	 * @return the double.
	 */
	public final double getDouble(int index) {
		return array[index];
	}

	/**
	 * Sets the value at the given index.
	 * @param index the index.
	 * @param value the value.
	 */
	public final void set(int index, double value) {
		if (index >= size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		array[index] = value;
	}

	/**
	 * Add the given value.
	 * @param value the value.
	 */
	public final void add(double value) {
		ensureCapacity(size + 1);
		array[size] = value;
		size++;
	}

	/**
	 * Add the given value.
	 * @param value the value.
	 */
	public final void add(int value) {
		add((double) value);
	}

	/**
	 * Inserts the value at the given index.
	 * @param index the index.
	 * @param value the value.
	 */
	public final void insert(int index, double value) {
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
		System.arraycopy(array, index + 1, array, index, size - index - 1);
		size--;
	}

	/**
	 * Returns this array as a string.
	 * @return this array as a string.
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
	public final int count(double value) {
		int count = 0;
		for (int i = 0; i < size(); i++) {
			if (get(i) == value) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns the number of occurrences of the given type.
	 * @param type the type.
	 * @return the count.
	 */
	public final int count(BasicType type) {
		return count(((DoubleType) type).get());
	}

	/**
	 * Returns this as an array.
	 * @return this as an array.
	 */
	public final double[] toArray() {
		double[] array = new double[size];
		System.arraycopy(this.array, 0, array, 0, size);
		return array;
	}

	/**
	 * Returns this as an array.
	 * @param match return this array if sizes match.
	 * @return this as an array.
	 */
	public final double[] toArray(boolean match) {
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
	 * Returns the index of the largest value.
	 * @return the index of the largest value.
	 */
	public final int indexOfLargest() {
		if (size() == 0) {
			return -1;
		}
		int index = 0;
		for (int i = 1; i < size(); i++) {
			if (get(index) < get(i)) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * Returns the index of the smallest value.
	 * @return the index of the smallest value.
	 */
	public final int indexOfSmallest() {
		if (size() == 0) {
			return -1;
		}
		int index = 0;
		for (int i = 1; i < size(); i++) {
			if (get(index) > get(i)) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * Returns the first index of the given value.
	 * @param value the value.
	 * @return the first index.
	 */
	public final int indexOf(double value) {
		for (int i = 0; i < size(); i++) {
			if (get(i) == value) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns an array of all indexes of the given value.
	 * @param value the value.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(double value) {
		return arrayIndexOf(value, new IntegerArray());
	}

	/**
	 * Returns an array of all indexes of the given value.
	 * @param value the value.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(double value, BasicIndexArray array) {
		for (int i = 0; i < size(); i++) {
			if (get(i) == value) {
				array.add(i);
			}
		}
		return array;
	}

	/**
	 * Returns the index of the given type.
	 * @param type the type.
	 * @return the index.
	 */
	public final int indexOf(BasicType type) {
		return indexOf(((DoubleType) type).get());
	}

	/**
	 * Returns an array of all indexes of the given type.
	 * @param type the type.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(BasicType type) {
		return arrayIndexOf(((DoubleType) type).get());
	}

	/**
	 * Returns the largest value less than or equal to the given maximum.
	 * @param max the maximum.
	 * @return the largest value.
	 */
	public final double getLargestValue(double max) {
		double value = -1;
		for (int i = 0; i < size; i++) {
			if (array[i] <= max && array[i] > value) {
				value = array[i];
			}
		}
		if (value == -1) {
			throw new IllegalStateException("largest value not found");
		}
		return value;
	}

	/**
	 * Returns the largest value.
	 * @return the largest value.
	 */
	public final double getLargestValue() {
		return getLargestValue(Double.MAX_VALUE);
	}

	/**
	 * Add the given type.
	 * @param type the type.
	 */
	public void add(BasicType type) {
		add(((DoubleType) type).get());
	}

	/**
	 * Set the given index.
	 * @param index the index.
	 * @param type the type.
	 */
	public void set(int index, BasicType type) {
		set(index, ((DoubleType) type).get());
	}

}