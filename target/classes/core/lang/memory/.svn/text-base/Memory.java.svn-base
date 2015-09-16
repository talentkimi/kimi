package core.lang.memory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Memory {

	private static final Logger log = LoggerFactory.getLogger(Memory.class);


	/**
	 * Returns the maximum memory.
	 * @return the maximum memory.
	 */
	public static final long maximum() {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * Returns the percentage of memory used.
	 * @return the percentage of memory used.
	 */
	public static final int percent() {
		return (int) (used() * 100l / maximum());
	}

	/**
	 * Returns the total memory.
	 * @return the total memory.
	 */
	public static final long total() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * Returns the free memory.
	 * @return the free memory.
	 */
	public static final long free() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * Returns the used memory.
	 * @return the used memory.
	 */
	public static final long used() {
		return total() - free();
	}
}
