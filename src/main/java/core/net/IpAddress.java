package core.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.Binary;

/**
 * An IP Address.
 */
public class IpAddress {

	private static final Logger log = LoggerFactory.getLogger(IpAddress.class);
	
	/** The local address. */
	private static volatile IpAddress localAddress = null;

	/**
	 * Returns the local host address.
	 * @return the local host address.
	 */
	public static IpAddress getLocalAddress() {
		if (localAddress == null) {
			try {
				localAddress = new IpAddress(InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException uhe) {
				if (log.isErrorEnabled()) log.error(uhe.getMessage(),uhe);
				localAddress = new IpAddress("127.0.0.1");
			}
		}
		return localAddress;
	}

	/** The address. */
	private transient String address = null;
	/** The code. */
	private final long code;
	/** The first part. */
	private final int part1;
	/** The second part. */
	private final int part2;
	/** The third part. */
	private final int part3;
	/** The fourth part. */
	private final int part4;

	/**
	 * Returns the first part.
	 * @return the first part.
	 */
	public int getPart1() {
		return part1;
	}

	/**
	 * Returns the second part.
	 * @return the second part.
	 */
	public int getPart2() {
		return part2;
	}

	/**
	 * Returns the third part.
	 * @return the third part.
	 */
	public int getPart3() {
		return part3;
	}

	/**
	 * Returns the fourth part.
	 * @return the fourth part.
	 */
	public int getPart4() {
		return part4;
	}

	/**
	 * Returns the hashcode.
	 * @return the hashcode.
	 */
	public int hashCode() {
		return (int) getCode();
	}

	/**
	 * Returns the hashcode.
	 * @return the hashcode.
	 */
	public long getCode() {
		return this.code;
	}

	/**
	 * Returns true if this equals the given object.
	 * @param object the object.
	 * @return true if this equals the given object.
	 */
	public boolean equals(Object object) {
		if (object instanceof IpAddress) {
			IpAddress address = (IpAddress) object;
			return this.code == address.code;
		}
		return false;
	}

	/**
	 * Creates a new IP Address.
	 * @param address the address.
	 */
	public IpAddress(String address) {
		int index1 = address.indexOf('.');
		if (index1 == -1) {
			throw new IllegalArgumentException(address);
		}
		int index2 = address.indexOf('.', index1 + 1);
		if (index2 == -1) {
			throw new IllegalArgumentException(address);
		}
		int index3 = address.indexOf('.', index2 + 1);
		if (index3 == -1) {
			throw new IllegalArgumentException(address);
		}
		try {
			this.part1 = Integer.parseInt(address.substring(0, index1));
			this.part2 = Integer.parseInt(address.substring(index1 + 1, index2));
			this.part3 = Integer.parseInt(address.substring(index2 + 1, index3));
			this.part4 = Integer.parseInt(address.substring(index3 + 1, address.length()));
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(address);
		}
		if (part1 < 0 || part1 > 255) {
			throw new IllegalArgumentException(address);
		}
		if (part2 < 0 || part2 > 255) {
			throw new IllegalArgumentException(address);
		}
		if (part3 < 0 || part3 > 255) {
			throw new IllegalArgumentException(address);
		}
		if (part4 < 0 || part4 > 255) {
			throw new IllegalArgumentException(address);
		}

		// Code
		byte[] bytes = new byte[8];
		bytes[4] = (byte) part1;
		bytes[5] = (byte) part2;
		bytes[6] = (byte) part3;
		bytes[7] = (byte) part4;
		this.code = Binary.getLong(bytes);
	}

	/**
	 * Returns this as a string.
	 * @return this as a string.
	 */
	public String toString() {
		if (this.address == null) {
			StringBuilder address = new StringBuilder();
			address.append(part1);
			address.append('.');
			address.append(part2);
			address.append('.');
			address.append(part3);
			address.append('.');
			address.append(part4);
			this.address = address.toString();
		}
		return this.address;
	}

}
