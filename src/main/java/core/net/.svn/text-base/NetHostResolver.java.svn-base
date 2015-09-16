package core.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.StreamOutput;
import core.util.UtilMap;

/**
 * The Network Resolver.
 */
public class NetHostResolver {
	
	private static final Logger log = LoggerFactory.getLogger(NetHostResolver.class);

	/** The default timeout. */
	private static final long MINIMUM_TIMEOUT = 300;

	/** The host to address map. */
	private final UtilMap hostToAddress = new UtilMap();
	/** The host to timeout map. */
	private final UtilMap hostToTimeout = new UtilMap();

	/**
	 * Get the timeout for the given host.
	 * @param host the host.
	 * @return the timeout.
	 */
	public long getTimeout(String host) {
		Double timeout = (Double) hostToTimeout.get(host);
		if (timeout == null) {
			return MINIMUM_TIMEOUT;
		}
		return timeout.longValue();
	}

	/**
	 * Set the timeout.
	 * @param host the host.
	 * @param timeout the timeout.
	 */
	public void setTimeout(String host, long timeout) {
		if (timeout > MINIMUM_TIMEOUT) {
			timeout += MINIMUM_TIMEOUT;
			Double previousTimeout = (Double) hostToTimeout.get(host);
			if (previousTimeout == null) {
				hostToTimeout.put(host, new Double(timeout));
			} else {
				if (previousTimeout.longValue() > timeout) {
					hostToTimeout.put(host, new Double(timeout));
				}
			}
		}
	}

	/**
	 * Resolve the given address.
	 * @param host the host.
	 * @return the address.
	 */
	public InetAddress resolve(String host) throws UnknownHostException {
		byte[] address = (byte[]) hostToAddress.get(host);
		if (address == null) {
			long start = System.currentTimeMillis();
			address = InetAddress.getByName(host).getAddress();
			long finish = System.currentTimeMillis();
			if (log.isDebugEnabled()) log.debug ("[Resolved] " + host + " (" + (finish - start) + " millis)");
			hostToAddress.put(host, address);
		}
		return InetAddress.getByAddress(address);
	}

	/**
	 * Resolve the given address.
	 * @param host the host.
	 * @return the address.
	 */
	public InetSocketAddress resolve(String host, int port) throws UnknownHostException {
		InetAddress address = resolve(host);
		return new InetSocketAddress(address, port);
	}
}
