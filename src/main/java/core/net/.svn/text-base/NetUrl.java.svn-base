package core.net;

/**
 * A Network URL.
 */
public class NetUrl {

	/** Indicates if this url is absolute. * */
	private boolean absolute = false;
	/** The protocol (e.g. http). * */
	private String protocol = null;
	/** The host. * */
	private String host = null;
	/** The port. * */
	private int port = -1;
	/** The path. * */
	private String path = null;
	/** The query. * */
	private String query = "";
	/** The fragment. * */
	private String fragment = "";

	/**
	 * Returns the protocol.
	 * @return the protocol.
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Returns the host.
	 * @return the host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns the port.
	 * @return the port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns the path.
	 * @return the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns the directory (of the path).
	 * @return the directory (of the path).
	 */
	public String getDirectory() {
		int indexSlash = path.lastIndexOf('/');
		if (indexSlash == -1)
			return null;
		return path.substring(0, indexSlash + 1);
	}

	/**
	 * Returns the query.
	 * @return the query.
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Returns the fragment.
	 * @return the fragment.
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * Sets the protocol.
	 * @param protocol the protocol.
	 */
	public void setProtocol(String protocol) {
		if (protocol == null)
			throw new NullPointerException();
		this.protocol = protocol;
	}

	/**
	 * Sets the host.
	 * @param host the host.
	 */
	public void setHost(String host) {
		if (host == null)
			throw new NullPointerException();
		this.host = host;
	}

	/**
	 * Sets the port.
	 * @param port the port.
	 */
	public void setPort(int port) {
		if (port < 1)
			throw new IllegalArgumentException("port=" + port);
		this.port = port;
	}

	/**
	 * Sets the path.
	 * @param path the path.
	 */
	public void setPath(String path) {
		if (path == null)
			throw new NullPointerException();
		this.path = path;
	}

	/**
	 * Sets the query.
	 * @param query the query.
	 */
	public void setQuery(String query) {
		if (query == null)
			throw new NullPointerException();
		this.query = query;
	}

	/**
	 * Sets the fragment.
	 * @param fragment the fragment.
	 */
	public void setFragment(String fragment) {
		if (fragment == null)
			throw new NullPointerException();
		this.fragment = fragment;
	}

	/**
	 * Returns true if this URL is absolute.
	 * @return true if this URL is absolute.
	 */
	public boolean isAbsolute() {
		return absolute;
	}

	/**
	 * Sets whether this URL is absolute.
	 * @param absolute true if this url is absolute.
	 */
	public void setAbsolute(boolean absolute) {
		if (protocol == null)
			throw new IllegalArgumentException("URL cannot be absolute without a protocol: " + toString());
		this.absolute = absolute;
	}

	/**
	 * Returns true if this URL is relative.
	 * @return true if this URL is relative.
	 */
	public boolean isRelative() {
		return !absolute;
	}

	/**
	 * Returns a string representation of this URL.
	 * @param absolute true to return the absolute URL (if available).
	 */
	public String toString(boolean absolute) {
		return toString(absolute, true);
	}

	/**
	 * Returns a string representation of this URL.
	 * @param absolute true to return the absolute URL (if available).
	 */
	public String toString(boolean absolute, boolean includeQuery) {
		StringBuilder url = new StringBuilder();

		// No protocol? (host:port only)
		if (getProtocol() == null && getHost() != null) {
			url.append(getHost());
			url.append(':');
			url.append(getPort());
			return url.toString();
		}

		// Absolute? (protocol/host/port)
		if (absolute && getProtocol() != null) {
			url.append(getProtocol());
			url.append("://");
			url.append(getHost());
			if (getPort() != 80) {
				url.append(':');
				url.append(getPort());
			}
		}

		// Path
		if (path == null) {
			url.append('/');
		} else {
			url.append(getPath());
		}

		// Query
		if (includeQuery) {
			if (query.length() > 0) {
				url.append('?');
				url.append(query);
			}

			// Fragment
			if (fragment.length() > 0) {
				url.append('#');
				url.append(fragment);
			}
		}
		return url.toString();
	}

	/**
	 * Returns a string representation of this URL.
	 */
	public String toString() {
		return toString(isAbsolute());
	}

	/**
	 * Creates a new URL.
	 */
	private NetUrl() {

	}

	/**
	 * Creates a new URL.
	 * @param url the url.
	 */
	public NetUrl(String url) {
		int indexBegin = 0;
		int indexEnd = url.length();

		// Fragment.
		int indexFragment = url.lastIndexOf('#');
		if (indexFragment != -1) {
			this.fragment = url.substring(indexFragment + 1, indexEnd);
			indexEnd = indexFragment;
		}

		// Query
		int indexQuery = url.indexOf('?');
		if (indexQuery != -1 && indexQuery < indexEnd) {
			this.query = url.substring(indexQuery + 1, indexEnd);
			indexEnd = indexQuery;
		}

		// Protocol
		int indexProtocol = url.indexOf("://");
		if (indexProtocol != -1 && indexProtocol < indexEnd) {
			absolute = true;
			this.protocol = url.substring(indexBegin, indexProtocol);
			indexBegin = indexProtocol + 3;

			// Host
			int indexPath = url.indexOf('/', indexBegin);
			if (indexPath == -1)
				indexPath = indexEnd;

			// Port
			this.port = 80;
			int indexPort = url.indexOf(':', indexBegin);
			if (indexPort != -1 && indexPort < indexPath) {
				if (indexPort + 1 < indexPath) {
					this.port = Integer.parseInt(url.substring(indexPort + 1, indexPath));
				}
			} else {
				indexPort = indexPath;
			}

			// Host
			this.host = url.substring(indexBegin, indexPort);
			indexBegin = indexPath;
		}

		// Host & Port
		if (indexFragment == -1 && indexQuery == -1 && indexProtocol == -1) {
			int indexSlash = url.indexOf('/');
			if (indexSlash == -1) {
				int indexPort = url.indexOf(':', indexBegin);
				if (indexPort != -1) {
					this.host = url.substring(indexBegin, indexPort);
					this.port = Integer.parseInt(url.substring(indexPort + 1, indexEnd));
					indexEnd = indexBegin;
				}
			}
		}

		// Path
		if (indexBegin < indexEnd) {
			this.path = new String(url.substring(indexBegin, indexEnd));
		} else {
			this.path = null;
		}
	}

	/**
	 * Set this url from the given one.
	 * @param url the url to set from.
	 */
	public NetUrl setFrom(NetUrl url) {
		if (url.getProtocol() != null) {
			setProtocol(url.getProtocol());
		}
		if (url.getHost() != null) {
			setHost(url.getHost());
		}
		if (url.getPort() != -1) {
			setPort(url.getPort());
		}
		if (url.getPath() != null) {
			setDirectory(url.getDirectory());
		}
		return this;
	}

	/**
	 * Adds the given directory to this path.
	 * @param directory the directory.
	 */
	private final void setDirectory(String directory) {
		if (getPath() == null) {
			return;
		}
		if (directory == null) {
			return;
		}
		if (getPath().startsWith("/")) {
			return;
		}
		String oldPath = getPath();
		String newPath = directory;
		while (oldPath.startsWith("../")) {
			oldPath = oldPath.substring(3, oldPath.length());
			int indexSlash = newPath.lastIndexOf('/', newPath.length() - 2);
			newPath = newPath.substring(0, indexSlash + 1);
		}
		if (newPath.endsWith("/")) {
			setPath(newPath + oldPath);
		}
	}

	/**
	 * Returns a copy of this URL.
	 * @return a copy of this URL.
	 */
	public NetUrl copy() {
		NetUrl url = new NetUrl();
		url.absolute = this.absolute;
		url.protocol = this.protocol;
		url.host = this.host;
		url.port = this.port;
		url.path = this.path;
		url.query = this.query;
		url.fragment = this.fragment;
		return url;
	}

	public static void main(String[] args) {
		System.out.println(new NetUrl("https://www.google.com:/en/pl"));
		System.out.println(new NetUrl("https://www.google.com/en/pl"));
		System.out.println(new NetUrl("https://www.google.com:80/en/pl"));
	}
}