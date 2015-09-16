package core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Global Config.
 */
public final class GlobalConfig {

	private static final Logger log = LoggerFactory.getLogger(GlobalConfig.class);


	/** The singleton map. */
	private static final GlobalConfig GLOBAL_CONFIG = new GlobalConfig();

	/**
	 * Returns the singleton map.
	 * @return the singleton map.
	 */
	public static final GlobalConfig getConfigMap() {
		return GLOBAL_CONFIG;
	}

	/** The map. */
	private final HashMap map = new HashMap();

	/**
	 * Inaccessible constructor.
	 */
	private GlobalConfig() {
	}

	/**
	 * Returns the value on the given key.
	 * @param key the key.
	 * @return the value.
	 */
	public final String get(String key) {
		String value = (String) map.get(key.toLowerCase());
		if (value == null) {
			if (log.isDebugEnabled()) log.debug ("[Warning] Key Not Found: '" + key + "'");
		}
		return value;
	}

	/**
	 * Returns the value on the given key.
	 * @param key the key.
	 * @return the value.
	 */
	public final boolean isTrue(String key, boolean defaultValue) {
		return getBoolean(key, defaultValue);
	}

	/**
	 * Returns the value on the given key.
	 * @param key the key.
	 * @return the value.
	 */
	public final boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(value);
	}

	/**
	 * Returns the value on the given key.
	 * @param key the key.
	 * @return the value.
	 */
	public final long getLong(String key, long defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * Returns the value on the given key.
	 * @param key the key.
	 * @return the value.
	 */
	public final String put(String key, String value) {
		if (key == null || value == null) {
			throw new NullPointerException();
		}
		if (key.length() < 1 || key.length() > 100) {
			throw new IllegalArgumentException("key: '" + key + "'");
		}
		return (String) map.put(key.toLowerCase(), value);
	}
	
	
	/**
	 * Queries all entries in the global config map for specific string occurrences
	 * @param keyQuery	string to match key on (null == don't query on key)
	 * @param valueQuery string to match value on (null == don't query on value)
	 * @return entries in the global config map matching the criteria
	 */
	public Map<String, String> queryGlobalConfig(String keyQuery, String valueQuery) {
		Map<String,String> matchingResults = new TreeMap<String,String>();
		
		boolean queryByKey = (keyQuery != null);
		boolean queryByValue = (valueQuery != null);
		
		for(Object keyObj : map.keySet()) {
			String key = keyObj.toString();
			String value = map.get(key).toString();
			boolean keyMatch = (!queryByKey || key.contains(keyQuery));
			boolean valueMatch = (!queryByValue || value.contains(valueQuery));
			if(keyMatch && valueMatch) {
				matchingResults.put(key,value);
			}
		}
		
		return matchingResults;
	}
}
