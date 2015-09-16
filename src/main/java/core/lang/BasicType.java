package core.lang;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Basic Type Operations.
 */
public final class BasicType {

	private static final Logger log = LoggerFactory.getLogger(BasicType.class);


	/**
	 * Returns true if the given object is a basic type.
	 * @param object the object.
	 * @param includeObject true to include the object.
	 * @return true if the given object is a basic type.
	 */
	public static boolean isBasicType(Object object, boolean includeObject) {
		return isBasicType(object.getClass(), includeObject);
	}

	/**
	 * Returns true if the given class is a basic type.
	 * @param c the class.
	 * @param includeObject true to include the object.
	 * @return true if the given class is a basic type.
	 */
	public static boolean isBasicType(Class c, boolean includeObject) {
		if (c.equals(boolean.class)) {
			return true;
		}
		if (c.equals(byte.class)) {
			return true;
		}
		if (c.equals(short.class)) {
			return true;
		}
		if (c.equals(int.class)) {
			return true;
		}
		if (c.equals(long.class)) {
			return true;
		}
		if (c.equals(float.class)) {
			return true;
		}
		if (c.equals(double.class)) {
			return true;
		}
		if (c.equals(char.class)) {
			return true;
		}

		if (includeObject) {
			if (c.equals(Boolean.class)) {
				return true;
			}
			if (c.equals(Byte.class)) {
				return true;
			}
			if (c.equals(Short.class)) {
				return true;
			}
			if (c.equals(Integer.class)) {
				return true;
			}
			if (c.equals(Long.class)) {
				return true;
			}
			if (c.equals(Float.class)) {
				return true;
			}
			if (c.equals(Double.class)) {
				return true;
			}
			if (c.equals(Character.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the given object is a basic type. WARNING: Does not deal with 3+ dimensional arrays.
	 * @param object the object.
	 * @return true if the given object is a basic type.
	 */
	public static boolean isBasicTypeArray(Object object) {
		return isBasicTypeArray(object.getClass());
	}

	/**
	 * Returns true if the given class is a basic type. WARNING: Does not deal with 3+ dimensional arrays.
	 * @param object the object.
	 * @return true if the given class is a basic type.
	 */
	public static boolean isBasicTypeArray(Class c) {
		if (c.equals(boolean[].class) || c.equals(boolean[][].class)) {
			return true;
		}
		if (c.equals(byte[].class) || c.equals(byte[][].class)) {
			return true;
		}
		if (c.equals(short[].class) || c.equals(short[][].class)) {
			return true;
		}
		if (c.equals(int[].class) || c.equals(int[][].class)) {
			return true;
		}
		if (c.equals(long[].class) || c.equals(long[][].class)) {
			return true;
		}
		if (c.equals(float[].class) || c.equals(float[][].class)) {
			return true;
		}
		if (c.equals(double[].class) || c.equals(double[][].class)) {
			return true;
		}
		if (c.equals(char[].class) || c.equals(char[][].class)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		Object object = new int[2];
		if (log.isDebugEnabled()) log.debug (String.valueOf(isBasicTypeArray(object)));
	}

}
