package core.util;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.file.TextFile;
import core.xml.Xml;
import core.xml.XmlException;
import core.xml.XmlNode;

/**
 * A Config Directory.
 */
public class ConfigClassDirectory {

	private static final Logger log = LoggerFactory.getLogger(ConfigClassDirectory.class);


	/** The debug. */
	private final boolean debug;

	/** Config host name address map. */
	public static final UtilMap nameMap = new UtilMap();
	public static final UtilMap hostMap = new UtilMap();
	public static final UtilMap addressMap = new UtilMap();

	public static final String getAddressByHost(String host) {
		ConfigEntry entry = (ConfigEntry) hostMap.get(host);
		return entry.getAddress();
	}

	/**
	 * Creates a new Config Directory.
	 * @param debug true to enable debug.
	 */
	public ConfigClassDirectory(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Merge the given xml.
	 * @param mergeWith the xml to merge with.
	 * @param toMerge the xml to merge.
	 */
	private void mergeXml(Xml mergeWith, Xml toMerge, UtilMap nameMap) {
		for (int i = 0; i < toMerge.children(); i++) {
			Xml child = toMerge.getChild(i);
			if (child.getName().equals("import")) {
				importXml(mergeWith, child.getValue().toString(), nameMap);
			} else {
				addXml(child, mergeWith);
			}
		}
	}

	/**
	 * Import the named xml file to the xml.
	 * @param xml the xml to import to.
	 * @param name the name of the xml file to import.
	 */
	private void importXml(Xml importTo, String name, UtilMap nameMap) {
		ConfigEntry entry = (ConfigEntry) nameMap.get(name);
		if (debug) {
			if (log.isDebugEnabled()) log.debug ("[imported] '" + entry + "'");
		}
		if (entry == null) {
			throw new XmlException("Import file not found: " + name);
		}
		mergeXml(importTo, entry.getXml(), nameMap);
	}

	/**
	 * Add the given xml.
	 * @param toAdd the xml to add.
	 * @param addTo the xml to add to.
	 */
	private void addXml(Xml toAdd, Xml addTo) {
		String name = toAdd.getName();
		for (int i = 0; i < addTo.children(); i++) {
			Xml child = addTo.getChild(i);
			if (child.getName().equals(name)) {
				mergeChildren(child, toAdd);
				return;
			}
		}
		addTo.addChild(toAdd);
	}

	/**
	 * Merge the children of the given xml.
	 * @param mergeWith the xml to merge with
	 * @param toMerge the xml to merge.
	 */
	private void mergeChildren(Xml mergeWith, Xml toMerge) {
		for (int i = 0; i < toMerge.children(); i++) {
			Xml child = toMerge.getChild(i);
			String name = child.getName();
			if (mergeWith.containsChild(name)) {
				mergeWith.removeChild(name);
			}
			mergeWith.addChild(child);
		}
	}

	/**
	 * Load from the given directory.
	 * @param directory the directory.
	 * @return the xml.
	 */
	public Xml loadFromDirectory(TextFile directory) throws IOException, XmlException {
		// Check directory
		if (!directory.exists()) {
			throw new IOException("Directory does not exist: '" + directory + "'");
		}
		if (!directory.isDirectory()) {
			throw new IOException("Not a directory: '" + directory + "'");
		}

		// Check files
		TextFile[] files = (TextFile[]) directory.listFiles(true, ".*\\.xml");
		if (files.length == 0) {
			throw new IOException("Directory empty: '" + this + "'");
		}
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			String contents = files[i].readToString();
			try {
				Xml xml = Xml.READER.read(contents);
				if (!xml.getName().equals("Config")) {
					throw new XmlException("Expected root to be called 'Config' in file '" + name + "'");
				}
				checkForDuplicates(xml);
				String host = xml.getAttribute("host").getValue().toString().trim().toLowerCase();
				String address = xml.getAttribute("address").getValue().toString().trim().toLowerCase();
				ConfigEntry entry = new ConfigEntry(name, host, address, xml);
				if (entry.hasAHost()) {
					ConfigEntry mapEntry = (ConfigEntry) hostMap.get(host);
					if (mapEntry != null) {
						throw new XmlException("Duplicate host '" + host + "' in " + entry + " and " + mapEntry);
					}
					hostMap.put(host, entry);
				}
				if (entry.hasAnAddress()) {
					ConfigEntry mapEntry = (ConfigEntry) addressMap.get(address);
					if (mapEntry != null) {
						throw new XmlException("Duplicate address '" + address + "' in " + entry + " and " + mapEntry);
					}
					addressMap.put(address, entry);
				}
				nameMap.put(name, entry);
			} catch (XmlException xe) {
				throw new XmlException("Unable to load XML from file: '" + name + "'", xe);
			}
		}
		if (hostMap.size() == 0 && addressMap.size() == 0) {
			throw new IOException("No config file contains a host or address");
		}

		// Local address
		String host;
		String address = null;
		InetAddress netAddress = null;
		try {
			// Establish the hosts name
			netAddress = InetAddress.getLocalHost();
			host = netAddress.getHostName();

			// Fetch IP addresses for the host..
			InetAddress[] ipAddresses = InetAddress.getAllByName(host);
			if (log.isDebugEnabled()) log.debug (ipAddresses.length + " IPs found for host: " + host);
			for (int i = 0; i < ipAddresses.length; i++) {
				if (log.isDebugEnabled()) log.debug ("Checking local IP: " + ipAddresses[i]);
				if (addressMap.get(ipAddresses[i].getHostAddress()) != null) {
					address = ipAddresses[i].getHostAddress();
					if (log.isDebugEnabled()) log.debug ("IP address: " + address + " found!");
					break;
				}
			}
		} catch (Exception e) {
			host = "jellyfish";
			address = "10.0.0.171";
		}

		// Find host/address
		ConfigEntry entry = (ConfigEntry) hostMap.get(host);
		if (entry == null) {
			if (address == null) {
				throw new XmlException("No configuration found for " + host);
			}
			entry = (ConfigEntry) addressMap.get(address);
			if (entry == null) {
				throw new XmlException("No configuration found for " + host + " (" + address + ")");
			}
		} else {
			if (address == null) {
				address = netAddress.getHostAddress();
			}
		}

		if (debug) {
			if (log.isDebugEnabled()) log.debug ("[Localhost] " + host + " (" + address + ")");
		}
		
		// This part is checking if the config IP address loaded much any of the machines config.
		InetAddress[] ipAddresses = InetAddress.getAllByName(host);
		if (log.isDebugEnabled()) log.debug(ipAddresses.length + " IPs found for host: " + host);
		boolean matchingAddressFound = false;
		for (int i = 0; i < ipAddresses.length; i++) {
			if (entry.getAddress().equals(ipAddresses[i].getHostAddress())) {
				matchingAddressFound = true;
			}
		}
		if (!matchingAddressFound){
			if (log.isErrorEnabled()) log.error("Config Address [" + entry.getAddress() + "] is not the same as any of the server addresses.");
			throw new XmlException("Config IP address not included from server IP.");
		}

		// Merge xml
		Xml xml = new XmlNode("Config");
		if (debug) {
			if (log.isDebugEnabled()) log.debug ("[imported] '" + entry + "'");
		}
		mergeXml(xml, entry.getXml(), nameMap);
		return xml;
	}

	/**
	 * Check for duplicates.
	 * @param xml the xml.
	 */
	private void checkForDuplicates(Xml xml) {
		for (int i = 0; i < xml.children(); i++) {
			Xml child = xml.getChild(i);
			String name = child.getName();
			if (!name.equals("import") && xml.children(name) > 1) {
				throw new XmlException("Duplicate declaration of xml: '" + name + "'");
			}
		}
	}
}

class ConfigEntry {

	private static final Logger log = LoggerFactory.getLogger(ConfigEntry.class);


	/** The name. */
	private final String name;
	/** The host. */
	private final String host;
	/** The address. */
	private final String address;
	/** The xml. */
	private final Xml xml;

	/**
	 * Returns true if this has a host.
	 * @return true if this has a host.
	 */
	public boolean hasAHost() {
		return host.length() != 0;
	}

	/**
	 * Returns true if this has an address.
	 * @return true if this has an address.
	 */
	public boolean hasAnAddress() {
		return address.length() != 0;
	}

	/**
	 * Returns the name.
	 * @return the name.
	 */
	public String toString() {
		return name;
	}

	/**
	 * Returns the host.
	 * @return the host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns the address.
	 * @return the address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Returns the xml.
	 * @return the xml.
	 */
	public Xml getXml() {
		return xml;
	}

	/**
	 * Creates a new Config Entry.
	 * @param host the host.
	 * @param address the address.
	 */
	public ConfigEntry(String name, String host, String address, Xml xml) {
		if (name == null || host == null || address == null || xml == null) {
			throw new NullPointerException();
		}
		if (host.equals("localhost")) {
			throw new IllegalArgumentException("host cannot be localhost");
		}
		if (address.equals("127.0.0.1")) {
			throw new IllegalArgumentException("address cannot be 127.0.0.1");
		}
		this.name = name;
		this.host = host;
		this.address = address;
		this.xml = xml;
	}
}
