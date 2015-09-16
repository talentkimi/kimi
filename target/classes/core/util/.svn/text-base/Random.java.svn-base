package core.util;

import java.lang.reflect.Array;
import java.util.List;

/**
 * A collection of methods for retrieving random values.
 */
public final class Random {

	/** The random. */
	private static final Random RANDOM = new Random();

	/**
	 * Returns the default random.
	 * @return the default random.
	 */
	public static final Random getRandom() {
		return RANDOM;
	}

	/** The random. */
	private final java.util.Random random;

	/**
	 * Random.
	 */
	public Random() {
		random = new java.util.Random();
	}

	/**
	 * Random.
	 * @param seed the seed.
	 */
	public Random(long seed) {
		random = new java.util.Random(seed);
	}

	/**
	 * Generates random bytes and places them in the given byte array.
	 * @param bytes the bytes.
	 */
	public final void nextBytes(byte[] bytes) {
		random.nextBytes(bytes);
	}

	/**
	 * Returns a random boolean.
	 * @return a random boolean.
	 */
	public final boolean nextBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Returns a random float.
	 * @return a random float.
	 */
	public final float nextFloat() {
		return random.nextFloat();
	}

	/**
	 * Returns a random double.
	 * @return a random double.
	 */
	public final double nextDouble() {
		return random.nextDouble();
	}

	/**
	 * Returns a random byte.
	 * @return a random byte.
	 */
	public final short nextByte() {
		return (byte) nextInt();
	}

	/**
	 * Returns a random short.
	 * @return a random short.
	 */
	public final short nextShort() {
		return (short) nextInt();
	}

	/**
	 * Returns a random integer.
	 * @return a random integer.
	 */
	public final int nextInt() {
		return random.nextInt();
	}

	/**
	 * Returns a random long.
	 * @return a random long.
	 */
	public final long nextLong() {
		return random.nextLong();
	}

	/**
	 * Returns a random integer from 0 (inclusive) to n (exclusive).
	 * @param n the upper limit.
	 * @return a random integer.
	 */
	public final int nextInt(int n) {
		return random.nextInt(n);
	}

	/**
	 * Returns a random integer from the minimum to the maximum (inclusive).
	 * @param min the minimum.
	 * @param max the maximum.
	 * @return a random integer.
	 */
	public final int nextInt(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("min is greater than max (" + min + ">" + max + ")");
		}
		if (min == max) {
			return min;
		}
		return nextInt((max - min) + 1) + min;
	}

	/**
	 * Returns the next percentage.
	 * @return the next percentage.
	 */
	public final int nextPercent() {
		return nextInt(100) + 1;
	}

	/**
	 * Returns the next sequence of letters with a length.
	 * @param length the length.
	 * @return the next sequence.
	 */
	public final String nextLetters(int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length=" + length);
		}
		char[] sequence = new char[length];
		for (int i = 0; i < length; i++) {
			sequence[i] = (char) nextInt('A', 'Z');
		}
		return new String(sequence);
	}

	/**
	 * Returns the next unique string.
	 * @param length the length.
	 * @param base the base (maximum 36).
	 * @return the next unique string.
	 */
	public final String nextUnique(int length, int base) {
		if (length < 1) {
			throw new IllegalArgumentException("length=" + length);
		}
		if (base < 1 || base > 36) {
			throw new IllegalArgumentException("base=" + base);
		}
		StringBuilder unique = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int c = nextInt(base);
			c += (c < 10 ? 48 : 55);
			unique.append((char) c);
		}
		return unique.toString();
	}

	/**
	 * Returns the next unique string.
	 * @param length the length.
	 * @return the next unique string.
	 */
	public final String nextUnique(int length) {
		return nextUnique(length, 36);
	}

	/**
	 * Returns a random index within the given array.
	 * @param array the array.
	 * @return the index.
	 */
	public final int nextIndex(Object array) {
		return random.nextInt(Array.getLength(array));
	}

	/**
	 * Returns a random object from the given array.
	 * @param array the array.
	 * @return the random object.
	 */
	public final Object nextElement(Object array) {
		return Array.get(array, nextIndex(array));
	}

	/**
	 * Returns a random object from the given array.
	 * @param array the array.
	 * @return the random object.
	 */
	public final <E> E nextElement(E[] array) {
		return array[nextIndex(array)];
	}

	/**
	 * Returns a random index within the given list.
	 * @param list the list.
	 * @return the index.
	 */
	public final int nextIndex(List list) {
		return random.nextInt(list.size());
	}

	/**
	 * Returns a random object from the given list.
	 * @param list the list.
	 * @return the random object.
	 */
	public final <E> E nextElement(List<E> list) {
		return list.get(nextIndex(list));
	}

	/**
	 * Returns a random long from 0 (inclusive) to n (exclusive).
	 * @param n the upper limit.
	 * @return a random long.
	 */
	public final long nextLong(long n) {
		double d = nextDouble();
		return (long) (n * d);
	}

	/**
	 * Returns a random long from 0 (inclusive) to n (exclusive).
	 * @param n the upper limit.
	 * @return a random long.
	 */
	public final long nextLong(long min, long max) {
		if (min == max) {
			return min;
		}
		if (min > max) {
			throw new IllegalArgumentException(min + ">" + max);
		}
		long range = (max - min) + 1;
		long value = nextLong(range);
		return value + min;
	}

	/**
	 * Returns a random integer array.
	 * @param length the length.
	 * @param max the maximum.
	 * @return the array.
	 */
	public int[] nextIntArray(int length, int max) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = nextInt(max);
		}
		return array;
	}

	public static void main(String[] args) {
		System.out.println(getRandom().nextLetters(16));
	}

}