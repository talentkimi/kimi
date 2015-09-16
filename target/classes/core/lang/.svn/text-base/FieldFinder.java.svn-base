package core.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * A Field Finder.
 */
public final class FieldFinder {

	/** The singleton. */
	private static final FieldFinder SINGLETON = new FieldFinder();
	/** No fields. */
	private static final Field[] NO_FIELDS = new Field[0];

	/**
	 * Returns the singleton.
	 * @return the singleton.
	 */
	public static final FieldFinder getFieldFinder() {
		return SINGLETON;
	}

	/** The field map. */
	private final Map<Class, Field[]> fieldMap = new IdentityHashMap<Class, Field[]>();

	/**
	 * Inaccessiable Constructor.
	 */
	private FieldFinder() {
	}

	/**
	 * Returns the fields for the given class.
	 * @param c the class.
	 * @return the fields.
	 */
	public final Field[] findFields(Object object) {
		if (object == null) {
			return NO_FIELDS;
		}
		return findFields(object.getClass());
	}

	/**
	 * Returns the fields for the given class.
	 * @param clazz the class.
	 * @return the fields.
	 */
	public final Field[] findFields(Class clazz) {
		if (clazz == null) {
			return NO_FIELDS;
		}
		synchronized (fieldMap) {
			Field[] fields = fieldMap.get(clazz);
			if (fields == null) {
				ArrayList list = new ArrayList();
				addFields(clazz, list);
				fields = new Field[list.size()];
				list.toArray(fields);
				fieldMap.put(clazz, fields);
			}
			return fields;
		}
	}

	/**
	 * Add all the fields in the given class to the list.
	 * @param clazz the class.
	 * @param list the list.
	 */
	private final void addFields(Class clazz, ArrayList list) {
		if (clazz != null) {
			if (clazz.equals(Object.class)) {
				return;
			}
			if (clazz.equals(Class.class)) {
				return;
			}
			if (clazz.equals(Thread.class)) {
				return;
			}
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// Ignore Static Fields
				if (!Modifier.isStatic(fields[i].getModifiers())) {
					list.add(fields[i]);
				}
			}
			addFields(clazz.getSuperclass(), list);
		}
	}

}
