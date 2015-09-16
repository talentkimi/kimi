package core.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterator<E> implements Iterable<E>, Iterator<E> {

	public Iterator<E> iterator() {
		return this;
	}

	public final boolean hasNext() {
		return false;
	}

	public final E next() {
		throw new NoSuchElementException();
	}

	public final void remove() {
		throw new UnsupportedOperationException();
	}

}
