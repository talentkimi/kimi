package core.http;

import core.net.NetUrl;

/**
 * An HTTP Session.
 */
public final class HttpSession {

	/** Indicates if redirects should be followed. */
	private boolean followRedirects = true;
	/** The read timeout. */
	private int readTimeout = 0;
	/** The referer url. */
	private NetUrl referer = null;
	/** The cookie list. */
	private final HttpCookieList cookieList = new HttpCookieList();

	/**
	 * Sets the referer url.
	 * @param referer the url.
	 */
	public void setReferer(NetUrl referer) {
		this.referer = referer;
	}

	/**
	 * Returns the referer.
	 * @return the referer.
	 */
	public NetUrl getReferer() {
		return referer;
	}

	/**
	 * Returns true if redirects must be followed.
	 * @return true if redirects must be followed.
	 */
	public boolean mustFollowRedirects() {
		return followRedirects;
	}

	/**
	 * Returns the read timeout.
	 * @return the read timeout.
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Set the read timeout.
	 * @param seconds the seconds.
	 */
	public void setReadTimeout(int seconds) {
		if (seconds < 0) {
			throw new IllegalArgumentException("seconds=" + seconds);
		}
		this.readTimeout = 1000 * seconds;
	}

}