package core.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A Weak Reference List.
 */
public final class WeakReferenceList<T> {

	/** The list. */
	private final ArrayList<WeakReference<T>> list = new ArrayList<WeakReference<T>>();

	/**
	 * Returns the size.
	 * @return the size.
	 */
	public synchronized final int size() {
		return list.size();
	}

	/**
	 * Add an object.
	 * @param object the object to add.
	 */
	public synchronized final void add(T object) {
		if (object == null) {
			throw new NullPointerException();
		}
		
		// Replace existing expired references where possible
		for (int i = 0; i < list.size(); i++) {
			WeakReference<T> reference = list.get(i);
			if (reference.get() == null) {
				reference = new WeakReference(object);
				list.set(i, reference);
				return;
			}
		}
		list.add(new WeakReference(object));
	}

	/**
	 * Returns the object at the given index (could return null).
	 * @param index the index.
	 * @return the object.
	 */
	public synchronized final T get(int index) {
		WeakReference<T> reference = list.get(index);
		return reference.get();
	}

	/**
	 * Returns the objects in this as an array.
	 * @return the objects in this as an array.
	 */
	public synchronized Object[] toArray() {
		ArrayList<T> newList = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			WeakReference<T> reference = list.get(i);
			T object = reference.get();
			if (object != null) {
				newList.add(object);
			}
		}
		return newList.toArray();
	}
}
