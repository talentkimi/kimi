package core.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import core.util.UtilDate;

/**
 * An HTTP Cookie.
 */
public class HttpCookie implements Cloneable {
	
	private static final Logger log = LoggerFactory.getLogger(HttpCookie.class);
	
	private final String name;
	private final String value;
	private String domain = null;
	private String path = "/";
	private String expires = null;
	private boolean secure = false;
	private int maxAge = -1;

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		try {
			this.maxAge = Integer.parseInt(maxAge);
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid integer format for max-age: '" + maxAge + "'");
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if (path == null) {
			throw new NullPointerException();
		}
		this.path = path;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isValidDomain(String domainName) {
		if (domain == null) {
			return true;
		}
		domainName = domainName.toLowerCase();
		if (domain.startsWith(".")) {
			if (domainName.endsWith(domain)) {
				return true;
			}
			return domainName.equals(domain.substring(1));
		}
		return domainName.equals(domain);
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		if (expires != null) {
			try {
				new UtilDate("EEE, dd-MMM-yy HH:mm:ss z", expires);
			} catch (Exception e) {
				if (log.isErrorEnabled()) log.error(e.getMessage(),e);
				throw new IllegalArgumentException("invalid date format for expires: '" + expires + "'");
			}
		}
		this.expires = expires;
	}

	public HttpCookie() {
		this.name = null;
		this.value = null;
	}
	
	/**
	 * Creates a new HTTP cookie.
	 * @param name the name.
	 * @param value the value.
	 */
	public HttpCookie(String name, String value) {
		if (name == null || value == null) {
			throw new NullPointerException();
		}
		this.name = name;
		this.value = value;
		this.domain = null;
	}

	/**
	 * Returns a clone of this cookie.
	 * @return a clone of this cookie.
	 */
	public Object clone() {
		HttpCookie cookie = null;
		try {
			cookie = (HttpCookie) super.clone();
			cookie.expires = null;
			cookie.secure = false;
			cookie.maxAge = -1;
		} catch (CloneNotSupportedException e) {
		}
		return cookie;
	}

	public String toString() {
		return toString(false);
	}

	public String toString(boolean includeData) {
		if (includeData) {
			StringBuilder text = new StringBuilder();
			text.append(name).append('=').append(value);
			text.append("; path=").append(path);
			if (domain != null) {
				text.append("; domain=").append(domain);
			}
			if (maxAge >= 0) {
				text.append("; max-age=").append(maxAge);
			}
			if (expires != null) {
				text.append("; expires=").append(expires);
			}
			if (secure) {
				text.append("; secure");
			}
			return text.toString();
		} else {
			return name + '=' + value;
		}
	}

	public static void main(String[] args) {
		// System.out.println(parseHttpCookie("fred=john; path=/fred/;max-age=2;secure;expires=Fri, 31-Dec-2010 23:59:59 GMT", true).toString(true));
		// System.out.println(parseHttpCookie("fred=john;", true).toString(true));
		HttpCookie cookie = parseHttpCookie("a=b; domain=.travelfusion.com", true);
		System.out.println(cookie.isValidDomain("admin.travelfusion.com"));
		System.out.println(cookie.isValidDomain("travelfusion.com"));
		System.out.println(cookie.isValidDomain("www.travelfission.com"));
		System.out.println(cookie.isValidDomain("travelfission.com"));
	}

	public static final HttpCookie parseHttpCookie(String text, boolean findData) {

		// Cookie Parsing
		int equalsIndex = text.indexOf('=');
		if (equalsIndex == -1) {
			throw new IllegalArgumentException("malformed HTTP cookie: '" + text + "'");
		}
		int endIndex = text.indexOf(';', equalsIndex + 1);
		if (endIndex == -1) {
			endIndex = text.length();
		}
		String name = text.substring(0, equalsIndex);
		String value = text.substring(equalsIndex + 1, endIndex);
		HttpCookie cookie = new HttpCookie(name, value);

		// Data Parsing
		if (findData) {
			while (endIndex < text.length()) {
				endIndex++;
				int colonIndex = text.indexOf(';', endIndex);
				if (colonIndex == -1) {
					colonIndex = text.length();
				}
				String info = text.substring(endIndex, colonIndex).trim();
				if (info.length() == 0) {
					continue;
				}
				equalsIndex = info.indexOf('=');
				if (equalsIndex == -1) {
					name = info;
					value = info;
				} else {
					name = info.substring(0, equalsIndex).trim().toLowerCase();
					value = info.substring(equalsIndex + 1);
				}

				if (name.equals("domain")) {
					cookie.setDomain(value.trim());
				} else {
					if (name.equals("path")) {
						cookie.setPath(value.trim());
					} else {
						if (name.equals("expires")) {
							cookie.setExpires(value.trim());
						} else {
							if (name.equals("max-age")) {
								cookie.setMaxAge(value.trim());
							} else {
								if (name.equals("secure")) {
									cookie.setSecure(true);
								}
							}
						}
					}
				}

				if (colonIndex == text.length()) {
					break;
				}
				endIndex = colonIndex;
			}
		}

		// Done
		return cookie;
	}
}
