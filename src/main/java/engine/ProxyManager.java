package engine;

import core.net.NetUrl;
import core.util.ConfigClass;
import core.util.Random;
import core.util.UtilList;
import core.xml.Xml;
import core.xml.XmlException;

/**
 * A Proxy Manager.
 */
public class ProxyManager implements ConfigClass {

	/** The proxy list. */
	private final UtilList proxyList = new UtilList();

	/**
	 * Returns the number of proxies.
	 * @return the number of proxies.
	 */
	public int proxies() {
		return proxyList.size();
	}

	/**
	 * Returns a random proxy.
	 * @return a random proxy.
	 */
	public NetUrl getRandomProxy() {
		return (NetUrl) Random.getRandom().nextElement(proxyList);
	}

	/**
	 * Returns the proxy at the given index.
	 * @param index the index.
	 * @return the proxy.
	 */
	public final NetUrl getProxy(int index) {
		return (NetUrl) proxyList.get(index);
	}

	/**
	 * Parse this manager from the given xml.
	 * @param xml the xml.
	 */
	public void parseFrom(Xml xml) throws XmlException {
		int proxies = xml.children("proxy");
		for (int i = 0; i < proxies; i++) {
			Xml child = xml.getChild("proxy", i);
			String host = child.getAttribute("host").getValue().toString();
			int port = Integer.parseInt(child.getAttribute("port").getValue().toString());
			NetUrl url = new NetUrl(host + ":" + port);
			proxyList.add(url);
		}
	}

}
