package core.lang;

/**
 * A Three Dimensional Vector.
 */
final class Vector3D {

	/** X component. */
	private double x = 0.0;
	/** Y component. */
	private double y = 0.0;
	/** Z component. */
	private double z = 0.0;

	/**
	 * Returns the X component.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the Y component.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the Z component.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Set the location of this vector.
	 */
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the dot product.
	 */
	public double getDotProduct(Vector3D v) {
		return getX() * v.getX() + getY() * v.getY() + getZ() * v.getZ();
	}

	/**
	 * Get angle to another vector.
	 */
	public double getAngleTo(double radius, Vector3D v) {
		double dotProduct = getDotProduct(v);
		double radiusSquared = radius * radius;
		double cosAngle = dotProduct / radiusSquared;
		if (cosAngle > 1.0) {
			cosAngle = 1.0;
		} else if (cosAngle < -1.0) {
			cosAngle = -1.0;
		}
		return Math.acos(cosAngle);
	}

	/**
	 * Returns a string representation of this vector.
	 */
	public String toString() {
		return getX() + "," + getY() + "," + getZ();
	}

	/**
	 * Creates a new vector.
	 */
	public Vector3D() {
	}

	/**
	 * Creates a new vector.
	 * @param x
	 */
	public Vector3D(double x, double y, double z) {
		set(x, y, z);
	}

}