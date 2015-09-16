package core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A Field List. Created over a class, it stores the names and classes of the fields in the class.
 */
public class FieldList {

	/** The list. */
	private final ArrayList<Object[]> list = new ArrayList<Object[]>();

	/**
	 * Returns the size of the list.
	 * @return the size of the list.
	 */
	public final int size() {
		return list.size();
	}

	/**
	 * Returns the field name at the given index.
	 * @param index the index.
	 * @return the name.
	 */
	public final String getName(int index) {
		Object[] pair = list.get(index);
		return (String) pair[0];
	}

	/**
	 * Returns the field value at the given index.
	 * @param index the index.
	 * @return the name.
	 */
	public final Object getValue(int index) {
		Object[] pair = list.get(index);
		return pair[1];
	}

	/**
	 * Creates a new field list from the given interface.
	 * @param theClass the theInterface.
	 */
	public FieldList(Class theClass, Class theType) {
		if (theClass == null) {
			throw new NullPointerException();
		}
		Field[] array = theClass.getFields();
		try {
			for (int i = 0; i < array.length; i++) {
				Field field = array[i];
				if (theType == null || field.getType().equals(theType)) {
					String name = field.getName();
					Object value = field.get(null); // Null because its a static field
					check(name, value);
					Object[] pair = new Object[]{name, value};
					list.add(pair);
				}
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Unable to access all the fields from class: \"" + theClass + "\"");
		}
	}

	/**
	 * Creates a new field list from the given interface.
	 * @param theClass the theInterface.
	 */
	public FieldList(Class theClass) {
		this(theClass, null);
	}

	/**
	 * Returns the values as a list.
	 * @return the values as a list.
	 */
	public List toValueList() {
		List list = new UtilList();
		for (int i = 0; i < size(); i++) {
			list.add(getValue(i));
		}
		return list;
	}

	/**
	 * Returns the name of the given value.
	 * @param value
	 * @return
	 */
	public String getName(Object value) {
		for (int i = 0; i < size(); i++) {
			if (getValue(i).equals(value)) {
				return (String) getName(i);
			}
		}
		return null;
	}

	/**
	 * Check the given field name and value.
	 * @param name the name.
	 * @param value the value.
	 */
	protected void check(String name, Object value) {
	}

	/**
	 * Returns true if this contains the given name.
	 * @param name the name.
	 * @return true if this contains the given name.
	 */
	public boolean containsName(String name) {
		for (int i = 0; i < size(); i++) {
			if (getName(i).equals(name)) {
				return true;
			}
		}
		return false;
	}

	public final int indexOf(Object value) {
		for (int i = 0; i < size(); i++) {
			if (getValue(i).equals(value)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns true if this contains the given value.
	 * @param value the value.
	 * @return true if this contains the given value.
	 */
	public boolean containsValue(Object value) {
		return indexOf(value) != -1;
	}
}