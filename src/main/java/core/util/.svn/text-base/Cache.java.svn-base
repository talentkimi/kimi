package core.util;

public interface Cache<K, V> {

	V put(K key, V value);
	
	V get(K key);

	void clear();
	
	/**
	 * @return current number of entries in the cache(may include obsoleted values if those are still in cache) 
	 */
	int size();
	
}
