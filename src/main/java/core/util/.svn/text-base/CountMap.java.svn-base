package core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountMap<K> {

	private long total = 0;
	private final Map<K, Long> map = new LinkedHashMap<K, Long>();

	public void increment(K key) {
		increment(key, 1);
	}

	public long getTotal() {
		return total;
	}

	public void increment(K key, long amount) {
		if (amount < 1) {
			throw new IllegalArgumentException("amount=" + amount);
		}
		synchronized (map) {
			Long value = map.get(key);
			if (value == null) {
				value = new Long(amount);
			} else {
				value = Long.valueOf(value.longValue() + amount);
			}
			map.put(key, value);
			total += amount;
		}
	}

	public long get(K key) {
		Long value = map.get(key);
		if (value == null) {
			return 0;
		}
		return value.longValue();
	}

	public boolean contains(K key) {
		return map.containsKey(key);
	}

	public List<K> keys(boolean sort) {
		List<K> keyList = new ArrayList<K>(map.keySet());
		if (sort) {
			boolean changed = true;
			while (changed) {
				changed = false;
				for (int i = 0; i < keyList.size() - 1; i++) {
					K key0 = keyList.get(i + 0);
					K key1 = keyList.get(i + 1);
					if (get(key1) > get(key0)) {
						changed = true;
						Collections.swap(keyList, i + 0, i + 1);
					}
				}
			}
		}
		return keyList;
	}
	
	public String toString() {
		return map.toString();
	}
}
