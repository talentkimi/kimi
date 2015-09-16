package core.util;

import core.util.time.Milliseconds;
import core.util.time.Minutes;

/**
 * A simple cache which times out entries after have not been accessed for a certain period.
 */
public final class TimeoutCache<K, V> extends CacheMap<K, V> {

	public TimeoutCache(Minutes accessTimeout, Minutes totalTimeout) {
		super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, 0, totalTimeout.getMillis());
	}

	public TimeoutCache(Milliseconds totalTimeout, int maximumSize, boolean notUsed1) {
		super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, maximumSize, totalTimeout.getMillis());
	}

	public TimeoutCache(Milliseconds totalTimeout, int maximumSize, boolean notUsed1, boolean notUsed2) {
		super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, maximumSize, totalTimeout.getMillis());
	}

	public final boolean contains(K key) {
		return containsKey(key);
	}

	public final synchronized Object[] valuesArray() {
		return values().toArray();
	}

	public final synchronized Object[] keys() {
		return keySet().toArray();
	}

}