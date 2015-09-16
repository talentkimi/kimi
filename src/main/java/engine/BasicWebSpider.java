package engine;

import java.io.IOException;

import core.html.HtmlException;
import core.http.exception.HttpException;

/**
 * A Basic Web Spider.
 */
public class BasicWebSpider extends WebSpider {

	public String getSpiderName() {
		return "basic";
	}

	public void runTask() throws Throwable {
	}

	/**
	 * Downloads the page, parses the HTML forms found and updates the selection buffer
	 * @param filename
	 * @throws IOException
	 * @throws HttpException
	 * @throws InterruptedException
	 * @throws HtmlException
	 */
	public void downloadHtmlPage(String filename) throws IOException, HttpException, InterruptedException, HtmlException {
		downloadHtmlFormList(filename);
	}

	/**
	 * Downloads the page, parses the HTML forms found and updates the selection buffer
	 * @param readTimeout
	 * @param filename
	 * @throws IOException
	 * @throws HttpException
	 * @throws InterruptedException
	 * @throws HtmlException
	 */
	public void downloadHtmlPage(int readTimeout, String filename) throws IOException, HttpException, InterruptedException, HtmlException {
		downloadHtmlFormList(readTimeout, filename, true);
	}
}