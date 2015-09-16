package core.http;

import java.util.ArrayList;

import core.http.request.HttpRequest;
import core.http.response.HttpResponse;
import core.text.Text;
import core.util.PairList;

/**
 * An HTTP Cookie List.
 */
public class HttpCookieList {

	/** The cookie list. * */
	private final ArrayList<HttpCookie> cookieList = new ArrayList<HttpCookie>();

	/**
	 * Returns the size of the list.
	 * @return the size of the list.
	 */
	public int size() {
		return cookieList.size();
	}

	/**
	 * Adds the given cookie.
	 * @param cookie the cookie.
	 */
	public void set(HttpCookie cookie) {
		for (int i = 0; i < cookieList.size(); i++) {
			if (cookieList.get(i).getName().equals(cookie.getName())) {
				cookieList.set(i, cookie);
				return;
			}
		}
		add(cookie);
	}

	/**
	 * Adds the given cookie.
	 * @param cookie the cookie.
	 */
	public void add(HttpCookie cookie) {
		cookieList.add(cookie);
	}

	/**
	 * Remove the cookie with the given key.
	 * @param key the cookie key.
	 */
	public void remove(Object key) {
		if (key == null)
			throw new NullPointerException();
		for (int i = 0; i < size(); i++) {
			HttpCookie cookie = getCookie(i);
			if (cookie.getName().equals(key)) {
				cookieList.remove(i--);
			}
		}
	}

	/**
	 * Returns the cookie at the given index.
	 * @param index the index.
	 * @return the cookie.
	 */
	public HttpCookie getCookie(int index) {
		return (HttpCookie) cookieList.get(index);
	}

	/**
	 * Gets cookies from the given list.
	 * @param list the list.
	 */
	public int getCookiesFrom(HttpCookieList list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				set((HttpCookie) list.getCookie(i).clone());
			}
			return list.size();
		}
		return 0;
	}

	/**
	 * Gets cookies from the given request.
	 * @param request the HTTP request.
	 * @return the number of cookies found.
	 */
	public int getCookiesFrom(HttpRequest request) {
		int number = 0;
		for (int i = 0; i < request.getHeaderList().size(); i++) {
			HttpHeader header = request.getHeaderList().getHeader(i);
			if (header.hasName(HttpRequest.HEADER_COOKIE)) {
				String headerString = header.getValue();
				String[] getCookies = Text.split(headerString, ";");
				for (int k = 0; k < getCookies.length; k++) {
					String getCookie = getCookies[k];
					HttpCookie cookie = HttpCookie.parseHttpCookie(getCookie, false);
					set(cookie);
					number++;
				}
			}
		}
		return number;
	}

	/**
	 * Gets cookies from the given response.
	 * @param response the HTTP response.
	 * @return the number of cookies found.
	 */
	public int getCookiesFrom(HttpResponse response) {
		int number = 0;
		for (int i = 0; i < response.getHeaderList().size(); i++) {
			HttpHeader header = response.getHeaderList().getHeader(i);
			if (header.hasName(HttpResponse.HEADER_SET_COOKIE)) {
				String setCookie = header.getValue();
				HttpCookie cookie = HttpCookie.parseHttpCookie(setCookie, false);
				set(cookie);
				number++;
			}
		}
		return number;
	}

	/**
	 * Set cookies in the given request.
	 * @param request the request.
	 */
	public void setCookiesIn(HttpRequest request) {
		if (size() == 0)
			return;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			if (i > 0)
				buffer.append("; ");
			HttpCookie cookie = getCookie(i);
			buffer.append(cookie.getName());
			buffer.append('=');
			buffer.append(cookie.getValue());
		}
		HttpHeader header = new HttpHeader(HttpRequest.HEADER_COOKIE, buffer);
		request.getHeaderList().set(header);
	}

	public String toString() {
		return cookieList.toString();
	}

	public void clear() {
		cookieList.clear();
	}

}
