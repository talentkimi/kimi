package core.util;

public final class Assert {

	public static <T> T notNull(T o) {
		if (o == null)
			throw new NullPointerException();
		return o;
	}
}
