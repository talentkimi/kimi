package core.http;

import core.util.UtilList;

/**
 * An HTTP Message Header List.
 */
public class HttpHeaderList {

	/** The list. * */
	private final UtilList list = new UtilList();

	/**
	 * Returns this list as a string.
	 * @return this list as a string.
	 */
	public String toString() {
		return list.toString();
	}

	/**
	 * Returns the size of the list.
	 * @return the size of the list.
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Clear the list.
	 */
	public void clear() {
		list.clear();
	}

	/**
	 * Returns the header at the given index.
	 * @param index the index.
	 * @return the header.
	 */
	public HttpHeader getHeader(int index) {
		return (HttpHeader) list.get(index);
	}

	/**
	 * Adds the given header.
	 * @param header the header to add.
	 */
	public void add(HttpHeader header) {
		if (header == null)
			throw new NullPointerException();
		list.add(header);
	}

	/**
	 * Remove the header at the given index.
	 * @param index the index.
	 */
	public void remove(int index) {
		list.remove(index);
	}

	/**
	 * Removes the first header with the given name.
	 * @param name the name.
	 * @return the header.
	 */
	public HttpHeader removeHeader(String name) {
		int index = indexOf(name);
		if (index == -1) {
			return null;
		} else {
			return (HttpHeader) list.remove(index);
		}
	}

	/**
	 * Returns the first header with the given name.
	 * @param name the name.
	 * @return the header.
	 */
	public HttpHeader getHeader(String name) {
		int index = indexOf(name);
		if (index == -1) {
			return null;
		} else {
			return (HttpHeader) list.get(index);
		}
	}

	/**
	 * Returns the first index of the header with the given name.
	 * @param name the header name.
	 * @return the index.
	 */
	public int indexOf(String name) {
		for (int i = 0; i < list.size(); i++) {
			if (getHeader(i).hasName(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the first index of the header.
	 * @param header the header.
	 * @return the index.
	 */
	public int indexOf(HttpHeader header) {
		return indexOf(header.getName());
	}

	/**
	 * Sets the given header.
	 * @param header the header.
	 */
	public void set(HttpHeader header) {
		int index = indexOf(header);
		if (index == -1) {
			list.add(header);
		} else {
			list.set(index, header);
		}
	}

	public boolean contains(String name) {
		return indexOf(name) != -1;
	}

}
