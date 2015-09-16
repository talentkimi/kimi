package core.html;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import core.io.file.TextFile;
import core.net.NetUrl;

/**
 * An HTML URL Replacer.
 */
public class HtmlUrlReplacer {
	
	private static final Logger log = LoggerFactory.getLogger(HtmlUrlReplacer.class);
	
	/**
	 * Replace relative urls in the HTML with the given url.
	 * @param html the html.
	 * @param url the url
	 */
	public String replace(String html, String url) {
		NetUrl baseUrl = new NetUrl(url);
		StringBuilder buffer = new StringBuilder(html);

		// replace "../"
		int index = 0;
		while (true) {
			index = buffer.indexOf("../", index);
			if (index == -1) {
				break;
			}
			char c = buffer.charAt(index - 1);
			if (c == '\'') {
				index = replace(buffer, index, "\'", baseUrl);
			} else if (c == '\"') {
				index = replace(buffer, index, "\"", baseUrl);
			} else {
				index++;
			}
		}

		// replace "/"
		index = 0;
		while (true) {
			index = buffer.indexOf("\'/", index);
			if (index == -1) {
				break;
			}
			index = replace(buffer, index + 1, "\'", baseUrl);
		}
		index = 0;
		while (true) {
			index = buffer.indexOf("\"/", index);
			if (index == -1) {
				break;
			}
			index = replace(buffer, index + 1, "\"", baseUrl);
		}

		// window.open
		index = 0;
		while (true) {
			index = buffer.indexOf("window.open(\'", index);
			if (index == -1) {
				break;
			}
			index = replace(buffer, index + 13, "\'", baseUrl);
		}
		index = 0;
		while (true) {
			index = buffer.indexOf("window.open(\"", index);
			if (index == -1) {
				break;
			}
			index = replace(buffer, index + 13, "\"", baseUrl);
		}

		return buffer.toString();
	}

	/**
	 * @param buffer
	 * @param index
	 * @param endIndex
	 */
	private int replace(StringBuilder buffer, int index, String c, NetUrl baseUrl) {
		int endIndex = buffer.indexOf(c, index);
		if (endIndex == -1) {
			return index + 1;
		}
		try {
			String url = buffer.substring(index, endIndex);
			NetUrl netUrl = new NetUrl(url);
			netUrl.setFrom(baseUrl);
			//System.out.print(url+" -> ");
			url = netUrl.toString(true);
			//System.out.println(url);
			buffer.replace(index, endIndex, url);
		} catch (Exception e) {
//			QuickStatsEngine2.engine.MISC_ERRORS.logEvent("HtmlUrlReplacement failed"+e.getMessage());
		}
		return endIndex;
	}

	public static void main(String[] args) {
		try {
			String provider = "ryanair";
			String url = "https://www.bookryanair.com/skylights/cgi-bin/skylights.cgi";
			String html = new TextFile("../c4/data/webspider/fakebooking/" + provider + "/booking.html.http").readToString();
			html = new HtmlUrlReplacer().replace(html, url);

			new TextFile("../c4/data/webspider/fakebooking/" + provider + "/booking.html").write(html);
			new TextFile("../c4/data/webspider/fakebooking/" + provider + "/booking.html.http2").write(html);

		} catch (Exception e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
	}

}
