package core.util;

import java.util.IdentityHashMap;

/**
 * A Lock Map.
 */
public class LockMap {

	/** The identity map. */
	private final IdentityHashMap lockMap = new IdentityHashMap();

	/**
	 * Returns true if locked objects exist.
	 * @return true if locked objects exist.
	 */
	public synchronized boolean isLocked() {
		return lockMap.size() > 0;
	}

	/**
	 * Returns the number of locked objects.
	 * @return the number of locked objects.
	 */
	public synchronized int locked() {
		return lockMap.size();
	}

	/**
	 * Lock the given object.
	 * @return true if this object is not already locked.
	 */
	public synchronized boolean lock(Object object) {
		return lockMap.put(object, object) == null;
	}

	/**
	 * Unlock the given object.
	 * @param object the object to unlock.
	 * @return true if the object was locked.
	 */
	public synchronized boolean unlock(Object object) {
		return lockMap.remove(object) != null;
	}
}
