package core.lang;

import java.io.Serializable;

/**
 * Latitiude & Longitude Coordinates.
 * <p>
 * Latitude is the vertical measurment from -90 to +90.
 * <p>
 * Longitude is the horizontal measurement from -180 to +180.
 */
public final class LatLon implements Serializable {

	/** Latitude constant. */
	public static final boolean LATITUDE = true;
	/** Longitude constant. */
	public static final boolean LONGITUDE = false;

	/** Mean radius of earth. * */
	public static final double RADIUS_EARTH = 6371000.0;

	/** The latitude of the position. */
	private double lat;
	/** The longitude of the position. */
	private double lon;

	/**
	 * Returns the hash code.
	 * @return the hash code.
	 */
	public final int hashCode() {
		return (int) (lat * 1024 + lon * 512);
	}

	/**
	 * Return the latitude.
	 */
	public final double getLat() {
		return lat;
	}

	/**
	 * Return the longitude.
	 */
	public final double getLon() {
		return lon;
	}

	/**
	 * Return the latitude in decimal/minutes/seconds format.
	 */
	public final String getLatDecimalMinutesSeconds() {
		return convert(getLat(), LATITUDE);
	}

	/**
	 * Return the longitude in decimal/minutes/seconds format.
	 */
	public final String getLonDecimalMinutesSeconds() {
		return convert(getLon(), LONGITUDE);
	}

	/**
	 * Set the location of these coordinates.
	 * @param lat the latitude.
	 * @param lon the longitude.
	 */
	public final void set(double lat, double lon) {
		if (lat > 90.0 || lat < -90.0) {
			throw new IllegalArgumentException("latitude=" + lat);
		}
		if (lon > 180.0 || lon < -180.0) {
			throw new IllegalArgumentException("longitude=" + lon);
		}
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Set the location of these coordinates.
	 * @param lat the latitude.
	 * @param lon the longitude.
	 */
	public final void set(String lat, String lon) {
		double latitude = Double.parseDouble(lat);
		double longitude = Double.parseDouble(lon);
		set(latitude, longitude);
	}

	/**
	 * Set the location of these coordinates.
	 * @param lat the latitude.
	 * @param lon the longitude.
	 */
	public final void setDecimalMinutesSeconds(String lat, String lon) {
		set(convert(lat, LATITUDE), convert(lon, LONGITUDE));
	}

	/**
	 * Returns a string representation of these coordinates.
	 */
	public String toString() {
		return getLat() + "," + getLon();
	}

	/**
	 * Returns true if the given object is equal to this.
	 * @param obj the object.
	 */
	public final boolean equals(Object obj) {
		if (obj instanceof LatLon) {
			LatLon c = (LatLon) obj;
			return getLat() == c.getLat() && getLon() == c.getLon();
		}
		return false;
	}

	/**
	 * Creates a new pair of coordinates.
	 * @param lon the longitude.
	 * @param lat the latitude.
	 */
	public LatLon() {
	}

	/**
	 * Creates a new pair of coordinates.
	 * @param lon the longitude.
	 * @param lat the latitude.
	 */
	public LatLon(double lat, double lon) {
		set(lat, lon);
	}

	/**
	 * Creates a new pair of coordinates.
	 * @param lon the longitude in decimal/minutes/seconds format.
	 * @param lat the latitude in decimal/minutes/seconds format.
	 */
	public LatLon(String lat, String lon) {
		this(lat, lon, false);
	}

	/**
	 * Creates a new pair of coordinates.
	 * @param lon the longitude in decimal/minutes/seconds format.
	 * @param lat the latitude in decimal/minutes/seconds format.
	 */
	public LatLon(String lat, String lon, boolean decimal) {
		if (decimal) {
			setDecimalMinutesSeconds(lat, lon);
		} else {
			set(lat, lon);
		}
	}

	/**
	 * Returns a vector representation of these coordinates.
	 */
	private final Vector3D toVector3D(double radius) {
		double rLon = Math.toRadians(getLon());
		double rLat = Math.toRadians(getLat());
		double r = Math.cos(rLat) * radius;
		double x = Math.sin(rLon) * r;
		double y = Math.sin(rLat) * radius;
		double z = Math.cos(rLon) * r;
		return new Vector3D(x, y, z);
	}

	/**
	 * Returns the straight line distance to the given coordinates. This method is 33 times faster than the world distance method.
	 * @param coords the coordinates.
	 */
	public final double getStraightLineDistanceInDegreesTo(LatLon coords) {
		if (equals(coords)) {
			return 0.0;
		}
		double latSquared = getLat() - coords.getLat();
		double lonSquared = getLon() - coords.getLon();
		latSquared *= latSquared;
		lonSquared *= lonSquared;
		return Math.sqrt(latSquared + lonSquared);
	}

	/**
	 * Returns the distance to the given coordinates across the surface of a sphere. This method is 33 times slower than the surface distance method.
	 * @param coords the coordinates.
	 */
	public final double getSurfaceDistanceInMetresTo(LatLon coords) {
		if (equals(coords)) {
			return 0.0;
		}
		Vector3D p1 = toVector3D(RADIUS_EARTH);
		Vector3D p2 = coords.toVector3D(RADIUS_EARTH);
		return p1.getAngleTo(RADIUS_EARTH, p2) * RADIUS_EARTH;
	}

	/**
	 * Converts the coordinates in the given decimal/minute/second fomat to plain decimal.
	 * @param ddmmss the decimal/minute/second format.
	 * @param lat true if the result is expected to be latitude.
	 */
	static final double convert(String ddmmss, boolean lat) {
		if (ddmmss.length() < 7) {
			throw new IllegalArgumentException("Invalid ddmmss coordinate: \"" + ddmmss + "\"");
		}

		// Direction
		char direction = ddmmss.charAt(0);
		if (lat) {
			if (direction != 'N' && direction != 'S') {
				throw new IllegalArgumentException("Invalid ddmmss direction: \"" + ddmmss + "\"");
			}
		} else {
			if (direction != 'E' && direction != 'W') {
				throw new IllegalArgumentException("Invalid ddmmss direction: \"" + ddmmss + "\"");
			}
		}

		int offset = 0;
		if (!lat && ddmmss.length() > 7) {
			offset = 1;
		}
		// Degrees
		double degrees = Double.parseDouble(ddmmss.substring(1, 3 + offset));
		// Minutes
		double minutes = Double.parseDouble(ddmmss.substring(3 + offset, 5 + offset));
		// Seconds
		double seconds = Double.parseDouble(ddmmss.substring(5 + offset, ddmmss.length()));

		// Calculate
		seconds = seconds / 3600.0;
		minutes = minutes / 60.0;
		double coord = (degrees + minutes + seconds);
		if (direction == 'S' || direction == 'W') {
			coord = -coord;
		}
		return coord;
	}

	/**
	 * Converts the coordinates in the given plain decimal fomat to decimal/minute/second decimal.
	 * @param d the decimal format.
	 * @param lat true if the result is expected to be latitude.
	 */
	static final String convert(double d, boolean lat) {

		// Direction
		char direction;
		if (lat) {
			if (d < 0.0) {
				direction = 'S';
			} else {
				direction = 'N';
			}
		} else {
			if (d < 0.0) {
				direction = 'W';
			} else {
				direction = 'E';
			}
		}
		d = Math.abs(d);

		// Degrees
		int degrees = (int) d;

		// Minutes
		d = d - degrees;
		d = d * 60;
		int minutes = (int) d;

		// Seconds
		d = d - minutes;
		d = d * 60;
		d = Math.round(d * 1000.0) / 1000.0;
		double seconds = d;

		// Offset
		int off = (lat ? 0 : 1);

		// Success
		StringBuilder sb = new StringBuilder();
		sb.append(direction);
		append(sb, degrees, 2 + off);
		append(sb, minutes, 2);
		append(sb, seconds, 2, 11 + off);
		return sb.toString();
	}

	private static final void append(StringBuilder sb, int i, int length) {
		if (length > 2 && i < 100) {
			sb.append('0');
		}
		if (length > 1 && i < 10) {
			sb.append('0');
		}
		sb.append(i);
	}

	private static final void append(StringBuilder sb, double d, int length, int total) {
		if (length > 2 && d < 100.0) {
			sb.append('0');
		}
		if (length > 1 && d < 10.0) {
			sb.append('0');
		}
		sb.append(d);
		while (sb.length() < total) {
			sb.append('0');
		}
	}

}