package core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A Utility List. It can be given an initial capacity and synchronized when created.
 */
public class UtilList implements List, Serializable {

	private volatile List list = null;
	private final boolean synchronize;
	private final int initialCapacity;

	public final List getList() {
		if (list == null) {
			synchronized (this) {
				if (list == null) {
					list = new ArrayList(initialCapacity);
					if (synchronize) {
						list = Collections.synchronizedList(list);
					}
				}
			}
		}
		return list;
	}

	/**
	 * Removes and returns the last object in the list.
	 * @return the last object in the list.
	 */
	public Object removeLast() {
		return remove(size() - 1);
	}

	/**
	 * Removes duplicates from this list.
	 * @return the number of duplicates removed.
	 */
	public int removeDuplicates() {
		int removed = 0;
		for (int i = 0; i < size(); i++) {
			for (int k = i + 1; k < size(); k++) {
				Object object1 = get(i);
				Object object2 = get(k);
				if (object1.equals(object2)) {
					remove(k--);
				}
			}
		}
		return removed;
	}

	/**
	 * Returns the first object.
	 * @return the first object.
	 */
	public Object getFirst() {
		return get(0);
	}

	/**
	 * Returns the last object.
	 * @return the last object.
	 */
	public Object getLast() {
		return get(size() - 1);
	}

	/**
	 * Returns a random index within this list.
	 * @return a random index.
	 */
	public int randomIndex() {
		return Random.getRandom().nextIndex(this);
	}

	/**
	 * Returns a random object from this list.
	 * @return a random object.
	 */
	public Object random() {
		return Random.getRandom().nextElement(this);
	}

	/**
	 * Returns a copy of this list.
	 * @return a copy of this list.
	 */
	public UtilList copy() {
		UtilList list = new UtilList(size(), synchronize);
		list.addAll(this);
		return list;
	}

	/**
	 * Returns the size of the list.
	 * @return the size of the list.
	 */
	public int size() {
		return list == null ? 0 : getList().size();
	}

	/**
	 * Clears the list.
	 */
	public void clear() {
		if (list != null) {
			getList().clear();
		}
	}

	/**
	 * Returns true if the list is empty.
	 * @return true if the list is empty.
	 */
	public boolean isEmpty() {
		return list == null ? true : getList().isEmpty();
	}

	/**
	 * Returns this list as object array.
	 * @return this list as object array.
	 */
	public Object[] toArray() {
		return list == null ? new Object[0] : getList().toArray();
	}

	/**
	 * Returns the object at the given index.
	 * @param index the index.
	 * @return the object.
	 */
	public Object get(int index) {
		if (list == null) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
		}
		return getList().get(index);
	}

	/**
	 * Removes the object at the given index.
	 * @param index the index.
	 * @return the object.
	 */
	public Object remove(int index) {
		if (list == null) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
		}
		return getList().remove(index);
	}

	/**
	 * Adds the object at the given index.
	 * @param index the index.
	 * @param o the object.
	 */
	public void add(int index, Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (list == null && index > 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
		}
		getList().add(index, o);
	}

	/**
	 * Returns the index of the given object.
	 * @param o the object.
	 * @return the index.
	 */
	public int indexOf(Object o) {
		return list == null ? -1 : getList().indexOf(o);
	}

	/**
	 * Returns the last index of the given object.
	 * @param o the object.
	 * @return the last index.
	 */
	public int lastIndexOf(Object o) {
		return list == null ? -1 : getList().lastIndexOf(o);
	}

	/**
	 * Adds the given object.
	 * @param o the object.
	 * @return true if added.
	 */
	public boolean add(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		return getList().add(o);
	}

	/**
	 * Returns true if the given object is in the list.
	 * @param o the object.
	 * @return true if the given object is in the list.
	 */
	public boolean contains(Object o) {
		return list == null ? false : getList().contains(o);
	}

	/**
	 * Removes the given object.
	 * @param o the object.
	 * @return the given object.
	 */
	public boolean remove(Object o) {
		return list == null ? false : getList().remove(o);
	}

	/**
	 * Add all objects from the given collection, starting at the given index.
	 * @param index the index.
	 * @param c the collection.
	 * @return true if the list has changed.
	 */
	public boolean addAll(int index, Collection c) {
		if (list == null) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
		}
		if (!c.isEmpty()) {
			return getList().addAll(index, c);
		}
		return false;
	}

	/**
	 * Add all objects from the given collection to the end of the list.
	 * @param c the collection.
	 * @return true if the list has changed.
	 */
	public boolean addAll(Collection c) {
		if (!c.isEmpty()) {
			return getList().addAll(c);
		}
		return false;
	}

	/**
	 * Returns true if this list contains all the objects in the given collection.
	 * @param c the collection.
	 * @return true if this list contains all the objects in the given collection.
	 */
	public boolean containsAll(Collection c) {
		if (!c.isEmpty()) {
			return getList().containsAll(c);
		}
		return true;
	}

	/**
	 * Remove all objects contained in the given collection from this list.
	 * @param c the collection.
	 * @return true if the list has changed.
	 */
	public boolean removeAll(Collection c) {
		if (!c.isEmpty()) {
			return getList().removeAll(c);
		}
		return true;
	}

	/**
	 * Retain all objects contained in the given collection that are in this list.
	 * @param c the collection.
	 * @return true if the list has changed.
	 */
	public boolean retainAll(Collection c) {
		if (!c.isEmpty()) {
			return getList().retainAll(c);
		}
		return true;
	}

	/**
	 * Returns an iterator across this list.
	 * @return an iterator across this list.
	 */
	public Iterator iterator() {
		if (list == null) {
			return new EmptyIterator();
		}
		return getList().iterator();
	}

	/**
	 * Returns a sub list of this list.
	 * @param fromIndex the beginning index of the sub list.
	 * @param toIndex the end index of the sub list.
	 * @return the sub list.
	 */
	public List subList(int fromIndex, int toIndex) {
		UtilList list = new UtilList();
		list.addAll(getList().subList(fromIndex, toIndex));
		return list;
	}

	/**
	 * Returns an iterator across this list.
	 * @return an iterator across this list.
	 */
	public ListIterator listIterator() {
		if (list == null) {
			return new EmptyListIterator();
		}
		return getList().listIterator();
	}

	/**
	 * Returns an iterator across this list, starting at the given index.
	 * @param index the index.
	 * @return an iterator across this list.
	 */
	public ListIterator listIterator(int index) {
		if (list == null) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
		}
		return getList().listIterator(index);
	}

	/**
	 * Sets the object at the given index.
	 * @param index the index.
	 * @param o the object.
	 * @return the object replaced.
	 */
	public Object set(int index, Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (list == null) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
		}
		return getList().set(index, o);
	}

	/**
	 * Returns the given array filled in from this list.
	 * @param a the array.
	 * @return the array.
	 */
	public Object[] toArray(Object[] a) {
		if (list == null && a.length == 0) {
			return a;
		}
		return getList().toArray(a);
	}

	/**
	 * Returns this as a string.
	 * @return this as a string.
	 */
	public String toString() {
		return getList().toString();
	}

	/**
	 * Swap the objects at the given indexes.
	 * @param index1 the first index.
	 * @param index2 the second index.
	 */
	public void swap(int index1, int index2) {
		if (index1 != index2) {
			Object object1 = get(index1);
			Object object2 = get(index2);
			set(index2, object1);
			set(index1, object2);
		}
	}

	/**
	 * Returns true if this is equal to the given object.
	 * @param object the object.
	 * @return true if this is equal to the given object.
	 */
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof List) {
			List list = (List) object;
			if (size() == list.size()) {
				for (int i = 0; i < size(); i++) {
					Object element1 = get(i);
					Object element2 = list.get(i);
					if (element1 == null) {
						if (element2 != null) {
							return false;
						}
					} else {
						if (element2 == null) {
							return false;
						}
						if (!element1.equals(element2)) {
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates a new list.
	 * @param initialCapacity the initial capacity of the list.
	 */
	public UtilList(int initialCapacity, boolean synchronize) {
		this.initialCapacity = initialCapacity;
		this.synchronize = synchronize;
	}

	/**
	 * Creates a new list.
	 * @param initialCapacity the initial capacity of the list.
	 */
	public UtilList(int initialCapacity) {
		this(initialCapacity, true);
	}

	/**
	 * Creates a new list.
	 */
	public UtilList() {
		this(11, true);
	}
}