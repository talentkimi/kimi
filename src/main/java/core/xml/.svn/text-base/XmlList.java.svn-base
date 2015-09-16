package core.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import core.lang.Closeable;

/**
 * An XML List.
 */
final class XmlList implements Closeable {

	private boolean closed = false;
	/** The list. * */
	private List<Xml> list = null;

	public void close() {
		for (Xml xml : list) {
			xml.close();
		}
		list = null;
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}

	/**
	 * Returns an iterator for the named node.
	 * @param name the node name.
	 * @return the iterator.
	 */
	public final Iterable<Xml> iterator(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		return new XmlIteratorable(name);
	}

	/**
	 * Returns this list as a list.
	 * @return this list as a list.
	 */
	public final List<Xml> unmodifiableList() {
		if (list == null) {
			return Xml.EMPTY_LIST;
		}
		return Collections.unmodifiableList(list);
	}

	/**
	 * Returns the size of the list.
	 * @return the size.
	 */
	public final int size() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	/**
	 * Adds the given xml.
	 * @param xml the xml.
	 */
	public final void add(Xml xml) {
		if (xml == null) {
			throw new NullPointerException();
		}
		if (list == null) {
			newList();
		}
		list.add(xml);
	}

	public final void add(int index, Xml xml) {
		if (xml == null) {
			throw new NullPointerException();
		}
		if (list == null) {
			newList();
		}
		list.add(index, xml);
	}

	/**
	 * Creates a new list (thread safe).
	 */
	private synchronized void newList() {
		if (list == null) {
			list = new ArrayList<Xml>();
		}
	}

	/**
	 * Returns the xml at the given index.
	 * @param index the index.
	 * @return the xml.
	 */
	public final Xml get(int index) {
		return (Xml) list.get(index);
	}

	/**
	 * Removes the xml at the given index.
	 * @param index the index.
	 * @return the xml removed.
	 */
	public final Xml remove(int index) {
		return (Xml) list.remove(index);
	}

	/**
	 * Sets the xml at the given index.
	 * @param index the index.
	 * @param xml the xml.
	 */
	public Xml set(int index, Xml xml) {
		if (xml == null) {
			throw new NullPointerException();
		}
		return (Xml) list.set(index, xml);
	}

	/**
	 * Returns a string representation of this list.
	 * @return a string representation of this list.
	 */
	public String toString() {
		if (list == null) {
			return "{}";
		}
		return list.toString();
	}

	/**
	 * Clear this list.
	 */
	public void clear() {
		list = null;
	}

	private final class XmlIteratorable implements Iterable<Xml>, Iterator<Xml> {

		private boolean found = false;
		private int index = -1;
		private final String name;

		public final boolean hasNext() {
			if (null == list) {
				return false;
			}
			if (found) {
				return true;
			}
			index++;
			while (index < list.size()) {
				if (list.get(index).getName().equals(name)) {
					found = true;
					return true;
				}
				index++;
			}
			return false;
		}

		public final Xml next() {
			if (hasNext()) {
				found = false;
				return list.get(index);
			}
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Iterator<Xml> iterator() {
			return this;
		}

		private XmlIteratorable(String name) {
			this.name = name;
		}
	}

}