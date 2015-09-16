package core.lang.basic.array;

import core.lang.basic.BasicIndexArray;
import core.lang.basic.BasicType;
import core.lang.basic.type.IntegerType;

/**
 * An Integer Array.
 */
public class IntegerArray implements BasicIndexArray {

	/** The array. */
	private int[] array;
	/** The size. */
	private int size = 0;

	/**
	 * Reclaim any unused space.
	 */
	public void reclaim() {
		if (size < array.length) {
			int[] newArray = new int[size];
			System.arraycopy(array, 0, newArray, 0, size);
			array = newArray;
		}
	}

	/**
	 * Returns the memory size in bytes.
	 * @return the memory size in bytes.
	 */
	public final long getMemoryBytes() {
		return 32 + (array.length * 4);
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
	 * @param array the array.
	 */
	public IntegerArray(int[] array) {
		if (array == null) {
			throw new NullPointerException();
		}
		this.array = array;
		this.size = array.length;
	}

	/**
	 * Creates a new integer array
	 * @param initialCapacity the initial capacity.
	 */
	public IntegerArray(int initialCapacity) {
		if (initialCapacity < 1) {
			initialCapacity = 1;
		}
		this.array = new int[initialCapacity];
	}

	/**
	 * Creates a new integer array
	 */
	public IntegerArray() {
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
			int[] newArray = new int[newLength];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array = newArray;
		}
	}

	/**
	 * Returns the integer at the given index.
	 * @param index the index.
	 * @return the integer.
	 */
	public final int get(int index) {
		if (index >= size) {
			throw new IllegalArgumentException("index=" + index);
		}
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
		return array[index];
	}

	/**
	 * Returns the long at the given index.
	 * @param index the index.
	 * @return the long.
	 */
	public final long getLong(int index) {
		return array[index];
	}

	/**
	 * Returns the float at the given index.
	 * @param index the index.
	 * @return the float.
	 */
	public final float getFloat(int index) {
		return array[index];
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
	public final void set(int index, int value) {
		if (index >= size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		array[index] = value;
	}

	/**
	 * Add the given value.
	 * @param value the value.
	 */
	public final void add(int value) {
		ensureCapacity(size + 1);
		array[size] = value;
		size++;
	}

	/**
	 * Inserts the value at the given index.
	 * @param index the index.
	 * @param value the value.
	 */
	public final void insert(int index, int value) {
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
	 * Removes the value at the given index.
	 * @param index the index.
	 */
	public final void add(int index, int value) {
		if (index < 0 || index >= size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		ensureCapacity(size + 1);
		System.arraycopy(array, index, array, index + 1, size - index);
		array[index] = value;
		size++;
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
	public final int count(int value) {
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
		return count(((IntegerType) type).get());
	}

	/**
	 * Returns this as an array.
	 * @return this as an array.
	 */
	public final int[] toArray() {
		int[] array = new int[size];
		System.arraycopy(this.array, 0, array, 0, size);
		return array;
	}

	/**
	 * Returns this as an array.
	 * @param match return this array if sizes match.
	 * @return this as an array.
	 */
	public final int[] toArray(boolean match) {
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
	public final int indexOf(int value) {
		for (int i = 0; i < size(); i++) {
			if (get(i) == value) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns true if this contains the given value.
	 * @param value the value.
	 * @return true if this contains the given value.
	 */
	public final boolean contains(int value) {
		return indexOf(value) != -1;
	}

	/**
	 * Returns an array of all indexes of the given value.
	 * @param value the value.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(int value) {
		IntegerArray array = new IntegerArray();
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
		return indexOf(((IntegerType) type).get());
	}

	/**
	 * Returns an array of all indexes of the given type.
	 * @param type the type.
	 * @return the array.
	 */
	public final BasicIndexArray arrayIndexOf(BasicType type) {
		return arrayIndexOf(((IntegerType) type).get());
	}

	/**
	 * Add the given type.
	 * @param type the type.
	 */
	public void add(BasicType type) {
		add(((IntegerType) type).get());
	}

	/**
	 * Set the given index.
	 * @param index the index.
	 * @param type the type.
	 */
	public void set(int index, BasicType type) {
		set(index, ((IntegerType) type).get());
	}

	/**
	 * Swap the values at the given indexes.
	 * @param index1 the first index.
	 * @param index2 the second index.
	 */
	public final void swap(int index1, int index2) {
		int temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}

	/**
	 * Sort the array.
	 * @param ascending true to sort in ascending order
	 */
	public void sort(boolean ascending) {
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (ascending) {
					if (array[i] > array[j]) {
						swap(i, j);
					}
				} else {
					if (array[j] > array[i]) {
						swap(i, j);
					}
				}
			}
		}
	}

	/**
	 * Remove duplicates.
	 */
	public final void removeDuplicates() {
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (array[i] == array[j]) {
					remove(j--);
				}
			}
		}
	}

	public static void main(String[] args) {
		IntegerArray array = new IntegerArray();
		for (int i = 0; i < 10; i++) {
			array.add(i);
		}
		array.add(2, 23);
	}

	/**
	 * Returns a sorted index array.
	 * @return a sorted index array.
	 */
	public final BasicIndexArray sortedIndexArray(int maxSize) {
		if (maxSize > size) {
			maxSize = size;
		}
		int[] sorted = new int[maxSize];
		int sortedIndex = 0;
		int sortedCount = 0;
		boolean currentValueSet = false;
		boolean largestValueSet = false;
		int currentValue = 0;
		int largestValue = 0;
		while (sortedIndex < sorted.length) {
			for (int index = 0; index < size; index++) {
				int value = array[index];
				if (!currentValueSet) {
					if (!largestValueSet || value < largestValue) {
						sorted[sortedIndex] = index;
						sortedIndex++;
						sortedCount++;
						currentValue = value;
						currentValueSet = true;
					}
				} else {
					if (currentValue == value) {
						if (sortedIndex < sorted.length) {
							sorted[sortedIndex] = index;
							sortedIndex++;
							sortedCount++;
						}
					} else {
						if (value > currentValue && (!largestValueSet || value < largestValue)) {
							sortedIndex -= sortedCount;
							sortedCount = 0;
							sorted[sortedIndex] = index;
							sortedIndex++;
							sortedCount++;
							currentValue = value;
						}
					}
				}
			}
			if (currentValue == -1) {
				break;
			}
			largestValue = currentValue;
			largestValueSet = true;
			currentValue = 0;
			currentValueSet = false;
			sortedCount = 0;
		}
		return new IntegerArray(sorted);
	}

	/**
	 * Returns a sorted index array.
	 * @return a sorted index array.
	 */
	public final BasicIndexArray sortedIndexArray() {
		return sortedIndexArray(size);
	}
}