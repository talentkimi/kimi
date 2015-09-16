package engine;

import java.io.IOException;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.html.HtmlDecoder;
import core.html.HtmlException;
import core.html.HtmlField;
import core.html.HtmlForm;
import core.html.HtmlFormList;
import core.html.HtmlFormReader;
import core.html.HtmlFormWriter;
import core.http.HttpContent;
import core.http.HttpCookie;
import core.http.HttpCookieList;
import core.http.HttpDownloader;
import core.http.HttpHeader;
import core.http.HttpHeaderList;
import core.http.HttpMessageHeaderList;
import core.http.HttpQuery;
import core.http.HttpQueryContainer;
import core.http.HttpVersionList;
import core.http.browser.HttpBrowserFactory;
import core.http.exception.HttpBadRequestException;
import core.http.exception.HttpException;
import core.http.exception.HttpMaxRedirectsException;
import core.http.request.HttpRequest;
import core.http.request.HttpRequestHeaderList;
import core.http.request.HttpRequestMethodList;
import core.http.response.HttpResponse;
import core.http.response.HttpResponseHeaderList;
import core.io.CompressedObject;
import core.io.ConnectTimeoutException;
import core.io.DownloadTimeoutException;
import core.io.Gzip;
import core.io.StageTimeoutException;
import core.io.StreamReader;
import core.io.TimeoutException;
import core.io.file.TextFile;
import core.mime.MimeTypes;
import core.net.HttpsProtocol;
import core.net.NetUrl;
import core.nonblocking.NonBlockingRequestHandler;
import core.text.Charsets;
import core.text.Selection;
import core.text.SelectionException;
import core.text.SelectionOptions;
import core.text.Text;
import core.util.FieldList;
import core.util.ParamMap;
import core.util.Random;
import core.util.Task;
import core.util.UtilDate;
import core.xml.Xml;
import core.xml.XmlException;
import core.xml.XmlReader;
import core.xml.XmlWriter;

/**
 * An HTTP Spider.
 */
public abstract class WebSpider extends Task implements Runnable, SelectionOptions, HttpRequestMethodList, Charsets {

	private static final Logger log = LoggerFactory.getLogger(WebSpider.class);

	/** The request writer. */
	private static final HttpRequestWriter REQUEST_WRITER = new HttpRequestWriter();

	/** Indicates if this spider is closed. */
	private boolean closed = false;
	/** The HTTP cookie list. */
	private final CompressedObject<HttpCookieList> cookieList = new CompressedObject<HttpCookieList>(false, new HttpCookieList());
	/** The cookies are enabled by default. */
	private boolean cookiesEnabled = true;
	/** The debug status. */
	private boolean debugEnabled = WebSpiderConfig.isDebugEnabled();
	/** The HTTP downloader. */
	private HttpDownloader downloader = null;
	protected long totalDurationOfDownloads = 0;
	protected long totalNumberOfDownloads = 0;
	protected long totalBytesSent = 0;
	protected long totalBytesReceived = 0;
	protected long totalGZipTime = 0;

	/** The dowloads from file. */
	private boolean fileDownloads = false;
	/** File writing disabled? */
	private boolean fileWritingDisabled = WebSpiderConfig.isFileWritingDisabled();
	/** Forcing A special proxy */
//	private boolean forceProxy = WebSpiderStageConfig.isProxyForced();
	private boolean forceProxy = true;
	/** The HTML form. */
	private HtmlForm htmlForm = null;
	/** The HTML page. */
	protected final CompressedObject<HtmlFormList> htmlFormList = new CompressedObject<HtmlFormList>(false, new HtmlFormList());
	/** The http charset. */
	private String httpCharset = null;
	/** The http query. */
	protected String httpQueryCharset = Charsets.ISO_8859_1;
	/** The location redirect header. */
	protected String locationHeader = null;
	/** The multipart boundary. */
	private String multipartBoundary = null;
	/** The mutex. */
	private Object mutex = null;
	/** The params. */
	private final ParamMap params = new ParamMap();
	/** Proxy disabled. */
	protected boolean proxyDisabled = false;
	/** The proxy URL. */
	protected NetUrl proxyUrl = null;
	/** The php proxy URL. */
	protected NetUrl phpProxyUrl = null;
	/** Redircts enabled? */
	protected boolean redirectsEnabled = true;
	/** The referer URL. */
	protected NetUrl refererUrl = null;
	/** Indicates if disable refresh redirction only */
	protected boolean refreshRedirectionDisabled = false;
	/** The selecton. */
	private final Selection selection = new Selection();
	/** Enable Gzip Compression */
	protected boolean enableCompression = WebSpiderConfig.isGzipEnabled();
	/** Indicates whether the request goes through proxy cloud solution */
	private boolean proxyCloudRequest = false;
	/** the supplier proxy instance usage pool, if such available */
	/** The default HTTP protocol version used for each HTTP request */
	private String httpVersion = HttpVersionList.VERSION_10;
	/** The time when we first check for stage timeout. Solving problem with NIO StageTimeout (MISCCORE-4) */
	private volatile long firstDownloadStarted;
	/** The stage timeout value. Can be initialised by setDeligatedStageTimeout-method or retrieved from the system variables */
	private volatile int stageTimeout = -1;

	/**
	 * Gets the list of custom defined HTTP headers applied to all HTTP requests this Webspider instance do
	 * @return the list of custom defined HTTP headers applied to all HTTP requests
	 */
	protected HttpHeaderList getCustomHttpHeadersList() {
		return null;
	}

	/**
	 * is download in progress
	 * @return true if download is in progress
	 */
	public boolean isDownloadInProgress() {
		return downloader != null && downloader.isDownloadInProgress();
	}

	/**
	 * Gets the HTTP protocol version used for each HTTP request
	 * @return a string representation of the HTTP protocol version
	 */
	protected String getHttpVersion() {
		return httpVersion;
	}
	
	/**
	 * Sets the proxy cloud request state
	 * @param pool
	 * @param state
	 */
//	public void setProxyCloudRequest(UsagePool pool, boolean state) {
//		proxyCloudRequest = state;
//	}

	/**
	 * Sets the proxy cloud request state from previous stage
	 * @param spider
	 */
	public void setProxyCloudRequest(WebSpider spider) {
		proxyCloudRequest = spider.proxyCloudRequest;
	}

	/**
	 * Checks whether this {@link WebSpider} is going through the proxy cloud solution
	 * @return {@code true} if this {@link WebSpider} is configured through the proxy cloud solution 
	 */
	public boolean isProxyCloudRequest() {
		return proxyCloudRequest;
	}

	/**
	 * set compression
	 */
	public void setCompression() {
		setCompression(true);
	}

	/**
	 * set compression
	 * @param state
	 */
	public void setCompression(boolean state) {
		this.enableCompression = state;
		if (state) {
			if (log.isDebugEnabled()) log.debug("[Gzip Enabled]");
		} else {
			if (log.isDebugEnabled()) log.debug("[Gzip Disabled]");
		}
	}

	/**
	 * set compression
	 * @param spider
	 */
	public void setCompression(WebSpider spider) {
		this.enableCompression = spider.enableCompression;
	}

	/**
	 * Get default stage timeout in second. 
	 * This method is called from getDownloadTimeoutInSeconds to determine 
	 * default download timeout for this stage. 
	 * @return default timeout (in seconds).
	 */
	protected int getDefaultStageTimeoutInSeconds() {
		return 1200; // Default - 20 minutes.
	}
	
	/**
	 * Gets timeout of this stage. 
	 * This timeout can be initialised by setDeligatedStageTimeout(int) method 
	 * or it gets its value from the system variable.
	 * @return timeout (in millis) for this stage.
	 */
	public final int getStageTimeout() {
		if (stageTimeout < 0) {
//			stageTimeout = getStageTimeoutInSeconds() * 1000;
			stageTimeout=600*1000;
		}
		return stageTimeout;
	}
	
	/**
	 * Sets timeout for this stage. Please do not use this method in the suppliers' code!
	 * This method is used only when we are deligating work from one stage to another, 
	 * like in the TaxMethod calculation.
	 * @param t timeout in millis to set.
	 */
	public final void setDeligatedStageTimeout(int t) {
		if (t < 0) {
			t = 0;
		}
		this.stageTimeout = t;
	}
	
	/**
	 * Gets stage timeout in seconds from the system variables.
	 * See Jira OPSIMP-56 for details.
	 * @return the stage timeout in seconds from the system variables.
	 */
	private int getStageTimeoutInSeconds() {
//		SystemVariableManager svm = SystemVariableManager.getInstance(); 
		Class<?> clazz = getClass();
		do {
			final String name = "timeout." + clazz.getSimpleName();
//			final Integer result = svm.getVariableAsInteger(name, false);
			final Integer result = 0;
			if (result != null && result > 0) {
				return result;
			}
			clazz = clazz.getSuperclass();
	 	} while (WebSpider.class.isAssignableFrom(clazz));
		return getDefaultStageTimeoutInSeconds();
	}

	/**
	 * Checks if stage time is out. If so - logs quickstat and throws an DownloadTimeoutException, or returns the rest time otherwise.
	 * See MISCCORE-4 for details.
	 * @param extra an extra time is planned to be taken by further process.
	 * @return the rest of the stage timeout (millis).
	 * @throws StageTimeoutException if timeout occurred.
	 */
	protected final int checkStageTimeout(int extra) throws StageTimeoutException {
		// Decreasing download timeout (MISCCORE-4)
		int st = getStageTimeout();
		if (firstDownloadStarted == 0) {
			firstDownloadStarted = System.currentTimeMillis();
		}
		long timeSpent = System.currentTimeMillis() - firstDownloadStarted;
		int result = (int) ((st - timeSpent) - extra);
		if (result <= 0) {
			logStageTimeoutPerSupplier(st);
			throw new StageTimeoutException("Stage took too long [" + timeSpent  + "ms], but expected [" + st + "ms]");
		}
		return result;
	}

	/**
	 * Checks if stage time is out. If so - logs quickstat and throws an DownloadTimeoutException, or returns the rest time otherwise.
	 * See MISCCORE-4 for details.
	 * @return the rest of the stage timeout (millis).
	 * @throws StageTimeoutException if timeout occurred.
	 */
	protected final int checkStageTimeout() throws StageTimeoutException {
		return checkStageTimeout(0);
	}

	/**
	 * Gets the amount of time is left for this stage before timeout will occur. 
	 * @return rest time in ms.
	 */
	public final int getStageRestTime() {
		int st = getStageTimeout();
		if (firstDownloadStarted == 0) {
			firstDownloadStarted = System.currentTimeMillis();
		}
		long timeSpent = System.currentTimeMillis() - firstDownloadStarted;
		return (int) (st - timeSpent);
	}
	
	/**
	 * The max number of redirect. -1 means always redirect, 0 means not redirect, a positive number means limited redirect. *
	 */
	protected int maxRedirect = WebSpiderConfig.getMaxRedirectsAllowed();

	/**
	 * Sets the max HTTP redirects webspider is allowed to follow for any subsequent downloads 
	 * @param max
	 */
	protected void setMaxRedirect(int max) {
		this.maxRedirect = max;
	}

	@Override
	public String getTaskName() {
		return "WebSpider:" + super.getTaskName();
	}

	/**
	 * Add the given objects.
	 * @param toAdd the number to add.
	 * @param addTo what to add to.
	 * @return the result of the addition.
	 */
	public String add(Object toAdd, Object addTo) {
//		Amount amount1 = new Amount(toAdd.toString().trim());
//		Amount amount2 = new Amount(addTo.toString().trim());
//		return amount1.add(amount2).toString();
		return null;
	}

	/**
	 * Adds an HTTP form field.
	 * @param name the name.
	 */
	public final void addHttpFormField(String name) throws HtmlException, UnsupportedEncodingException {
		if (name == null) throw new NullPointerException();
		HtmlField field = getHtmlForm().getField(name);
		int index = field.getSelectedIndex();
		if (index == -1) throw new HtmlException("HTML field has no default value: \"" + name + "\"");
		String fieldName = field.getName(index);
		String fieldValue = field.getValue(index);
		field.initialise();
		if (getHtmlForm().getMethod().equals(METHOD_POST))
			addHttpPostField(fieldName, fieldValue);
		else
			addHttpGetField(fieldName, fieldValue);
	}

	/**
	 * Adds an HTTP form field.
	 * @param name the name.
	 * @param value the value.
	 */
	public final void addHttpFormField(String name, long value) throws UnsupportedEncodingException, HtmlException {
		addHttpFormField(name, String.valueOf(value), false);
	}

	/**
	 * Adds an HTTP form field.
	 * @param name the name.
	 * @param value the value.
	 */
	public final void addHttpFormField(String name, long value, boolean ignoreValue) throws UnsupportedEncodingException, HtmlException {
		addHttpFormField(name, String.valueOf(value), ignoreValue);
	}

	/**
	 * Adds an HTTP form field.
	 * @param name the name.
	 * @param value the value.
	 */
	public final void addHttpFormField(String name, String value) throws HtmlException, UnsupportedEncodingException {
		addHttpFormField(name, value, false);
	}

	/**
	 * Adds an HTTP form field.
	 * @param name the name.
	 * @param value the value.
	 */
	public final void addHttpFormField(String name, String value, boolean ignoreValue) throws HtmlException, UnsupportedEncodingException {
		if (name == null || value == null) throw new NullPointerException();
		int fields = getHtmlForm().fields(name);
		if (fields == 0) throw new HtmlException("HTML form field not found: \"" + name + "\"");
		HtmlField field = null;
		for (int i = 0; i < fields; i++) {
			field = getHtmlForm().getField(name, i);
			if (field.canContainAnyValue()) break;
			boolean found = false;
			for (int k = 0; k < field.size(); k++) {
				String fieldValue = field.getValue(k);
				if (value.equals(fieldValue)) {
					name = field.getName(k);
					found = true;
					break;
				}
				fieldValue = field.getDisplayValue(k);
				if (value.equals(fieldValue)) {
					value = field.getValue(k);
					name = field.getName(k);
					found = true;
					break;
				}
			}
			if (found || ignoreValue) break;
			field = null;
		}
		if (field == null) throw new HtmlException("HTML form field value not found: \"" + name + "\"!=\"" + value + "\"");
		field.initialise();
		if (getHtmlForm().getMethod().equals(METHOD_POST))
			addHttpPostField(name, value);
		else
			addHttpGetField(name, value);
	}

	/**
	 * Adds the given GET field to the request.
	 * @param field the field.
	 * @param value the value.
	 */
	public final void addHttpGetField(String field, long value) {
		addHttpGetField(field, String.valueOf(value));
	}

	/**
	 * Adds the given GET field to the request.
	 * @param field the field.
	 * @param value the value.
	 */
	public final void addHttpGetField(String field, Object value) {
		field = htmlDecode(field);
		value = htmlDecode(value);
		HttpRequest request = getDownloader().getRequest();
		HttpQueryContainer container = request.getUrl();
		HttpQuery query = new HttpQuery(container, httpQueryCharset, true);
		query.add(field, value.toString());
		container.setQuery(query.toString());
	}

	/**
	 * Adds the given header to the request.
	 * @param header the header.
	 * @param value the value.
	 */
	public final void addHttpHeader(String header, String value) {
		getDownloader().getRequest().getHeaderList().add(new HttpHeader(header, value));
	}

	/**
	 * Adds the given POST field to the request.
	 * @param field the field.
	 * @param value the value.
	 */
	public final void addHttpPostField(String field, long value) throws UnsupportedEncodingException {
		addHttpPostField(field, String.valueOf(value));
	}

	/**
	 * Adds the given POST field to the request.
	 * @param field the field.
	 * @param value the value.
	 */
	public final void addHttpPostField(String field, String value) throws UnsupportedEncodingException {
		field = htmlDecode(field);
		value = htmlDecode(value);
		HttpRequest request = getDownloader().getRequest();
		if (multipartBoundary != null) {
			String content = request.getContent().toString();
			StringBuilder part = new StringBuilder(content);
			part.append("--").append(multipartBoundary).append("\r\n");
			part.append("Content-Disposition: form-data; name=\"").append(field).append("\"\r\n");
			part.append("\r\n");
			part.append(value).append("\r\n");
			request.getContent().set(part.toString());
		} else {
			HttpQueryContainer container = request.getContent();
			HttpQuery query = new HttpQuery(container, httpQueryCharset);
			query.add(field, value.toString());
			container.setQuery(query.toString());
			request.setContent(MimeTypes.APPLICATION_X_WWW_FORM_URLENCODED);
		}
	}

	/**
	 * Add an HTTP form field if it exists.
	 * @param field the field name.
	 */
	public final boolean addOptionalHttpFormField(String field) throws UnsupportedEncodingException, HtmlException {
		if (existsHttpFormField(field)) {
			addHttpFormField(field);
			return true;
		}
		return false;
	}

	/**
	 * Add an HTTP form field if it exists.
	 * @param field the field name.
	 */
	public final boolean addOptionalHttpFormField(String field, String value) throws UnsupportedEncodingException, HtmlException {
		if (existsHttpFormField(field)) {
			addHttpFormField(field, value);
			return true;
		}
		return false;
	}

	/**
	 * Add all remaining HTTP form fields.
	 */
	public final void addRemainingHttpFormFields() throws UnsupportedEncodingException, HtmlException {
		for (int i = 0; i < getHtmlForm().fields(); i++) {
			HtmlField field = getHtmlForm().getField(i);
			if (!field.isInitialised()) {
				int selectedIndex = field.getSelectedIndex();
				if (selectedIndex != -1) addHttpFormField(field.getName(0));
			}
		}
	}

	/**
	 * Clear the map.
	 */
	public final void clearMap() {
		params.clear();
	}

	/**
	 * Close this spider.
	 */
	public void close() {
		closed = true;
		closeDownloader();
		closeHtmlForm();
		closeHtmlFormList();
		closeMap();
		closeSelection();
	}

	public void closeDownloader() {
		// Removed stats update - these are now set up during the actual download.
		downloader = null;
	}

	public final void closeHtmlForm() {
		htmlForm = null;
	}

	public final void closeHtmlFormList() {
		// htmlFormList.getObject().close();
	}

	public final void closeMap() {
		params.clear();
	}

	public final void closeSelection() {
		selection.clear();
	}

	protected final void compressHtmlFormList() {
		htmlFormList.compress();
	}

	protected final void compressHttpCookieList() {
		cookieList.compress();
	}

	/**
	 * Checks if the currently selected text contains a given string. Returns true and moves the cursor if the selected text does contain the string otherwise has no effect and returns false.
	 * @param end The string to match in the select buffer
	 * @return true if the select buffer contains the text
	 * @throws SelectionException if the extraction failed and was not optional.
	 */
	public final boolean containsText(String end) throws SelectionException {
		return containsText(null, end, 0);
	}

	/**
	 * Checks if the currently selected text contains a given string. Returns true and moves the cursor if the selected text does contain the string otherwise has no effect and returns false.
	 * @param end The string to match in the select buffer
	 * @return true if the select buffer contains the text
	 * @throws SelectionException if the extraction failed and was not optional.
	 */
	public final boolean containsText(String end, int options) throws SelectionException {
		return containsText(null, end, options);
	}

	/**
	 * Checks if the currently selected text contains a given string. Returns true and moves the cursor if the selected text does contain the string otherwise has no effect and returns false.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @return true if the select buffer contains the text
	 * @throws SelectionException if the extraction failed and was not optional.
	 */
	public final boolean containsText(String begin, String end) throws SelectionException {
		return containsText(begin, end, 0);
	}

	/**
	 * Extracts and returns the first string beginning and ending with the given delimiters.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @param options the options (OPTIONAL | MOVE | INCLUDE_DELIMITERS).
	 * @return the extracted text.
	 * @throws SelectionException if the extraction failed and was not optional.
	 */
	public final boolean containsText(String begin, String end, int options) throws SelectionException {
		return extractText(begin, end, OPTIONAL | options) != null;
	}

	/**
	 * Returns true if cookies are enabled.
	 * @return true if cookies are enabled.
	 */
	public boolean cookiesAreEnabled() {
		return cookiesEnabled;
	}

	/**
	 * Debug the given text.
	 * @param text the text.
	 */
	public final void debug(Object text) {
		if (debugEnabled) if (log.isDebugEnabled()) log.debug(String.valueOf(text));
	}

	/**
	 * Debug the given text.
	 * @param text the text.
	 * @param detail the detail.
	 */
	public final void debug(Object text, Object detail) {
		if (debugEnabled) if (log.isDebugEnabled()) {
			if (log.isDebugEnabled()) log.debug(String.valueOf(text));
			if (log.isDebugEnabled()) log.debug(String.valueOf(detail));
		}
	}

	/**
	 * Returns true if debugging is enabled.
	 * @return true if debugging is enabled.
	 */
	public boolean debugIsEnabled() {
		return debugEnabled;
	}

	/**
	 * Deselects all selected text.
	 */
	public void deselectAllText() {
		selection.clear();
	}

	/**
	 * Deselects the currently selected text.
	 * @return the deselected text.
	 */
	public String deselectText() {
		return selection.deselectText();
	}

	/**
	 * Disable cookies.
	 */
	public void disableCookies() {
		cookiesEnabled = false;
	}

	/**
	 * Disable file writing.
	 */
	public void disableFileWriting() {
		fileWritingDisabled = true;
	}

	/**
	 * Disable the proxy.
	 */
	public void disableProxy() {
		proxyDisabled = true;
	}

	/**
	 * Disable redirects.
	 */
	public void disableRedirects() {
		redirectsEnabled = false;
	}

	/**
	 * Returns true if refresh redirection disabled
	 * @return the refreshRedirectionDisable
	 */
	protected void disableRefreshRedirection() {
		refreshRedirectionDisabled = true;
	}

	/**
	 * Divide the given object by the number.
	 * @param toDivide the number to divide.
	 * @param divideBy what to divide by.
	 * @return the result of the division.
	 */
	public String divide(Object toDivide, long divideBy) {
		return divide(toDivide, String.valueOf(divideBy));
	}

	/**
	 * Divide the given objects.
	 * @param toDivide the number to divide.
	 * @param divideBy what to divide by.
	 * @return the result of the division.
	 */
	public String divide(Object toDivide, Object divideBy) {
//		Amount amount1 = new Amount(toDivide.toString().trim());
//		Amount amount2 = new Amount(divideBy.toString().trim());
//		return amount1.divide(amount2).toString();
		return null;
	}

	/**
	 * Download HTTP!
	 * @param readTimeout timeout (seconds). If it is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used. 
	 */
	protected void download(int readTimeout) throws HttpBadRequestException, IOException {
		
		int timeout = checkStageTimeout();
		
		// As by MISCCORE-2 we should use the lowest of these two timeouts.
		if (readTimeout > 0) {
			timeout = Math.min(readTimeout * 1000, timeout);
		}
		
		// TODO log quick stats and investigate to set more accurate time out.

		// Set the timeout for use in Non-Blocking IO.
		if (this instanceof NonBlockingRequestHandler) {
			((NonBlockingRequestHandler) this).setSocketTimeout(timeout);
		}

		if (timeout > 0) {
			if (log.isDebugEnabled()) {
				log.debug("[Download timeout] " + timeout + "ms");
			}
		}
		if (debugEnabled && log.isDebugEnabled()) {
			if (!proxyDisabled && proxyUrl != null) {
				log.debug("[Download " + timeout + "ms] " + proxyUrl + " [Proxy] " + getDownloader().getRequest().getUrl().getNetUrl().toString(true));
			} else if (!proxyDisabled && phpProxyUrl != null) {
				log.debug("[Download " + timeout + "ms] " + phpProxyUrl + " [PHPProxy] " + getDownloader().getRequest().getUrl().getNetUrl().toString(true));
			} else {
				log.debug("[Download " + timeout + "ms] " + getDownloader().getRequest().getUrl().getNetUrl().toString(true));
			}
		}
//		try {
			try {
				getDownloader().download(timeout, getHttpsProtocols());
			} catch (SocketTimeoutException e) {
				// WARN: this method used to log time in seconds. But now it is more useful to do so in milliseconds.
				logReadTimeoutPerSupplier(timeout);
				throw e;
			} catch (ConnectTimeoutException e) {
				// WARN: this method used to log time in seconds. But now it is more useful to do so in milliseconds.
				logConnectTimeoutPerSupplier(Math.min(timeout, HttpDownloader.CONNECTION_TIMEOUT));
				throw e;
			} catch (DownloadTimeoutException s) {
				// WARN: this method used to log time in seconds. But now it is more useful to do so in milliseconds.
				logReadTimeoutPerSupplier(timeout);
				throw s;
			}
			HttpResponse response = getDownloader().getResponse();

			if (response.getHeaderList().contains("Content-Encoding") && response.getHeaderList().getHeader("Content-Encoding").getValue().contains("gzip")) {
				try {
//					if (log.isDebugEnabled()) log.debug("[GZIP] Started Decompression");
					byte[] responseContentPreGunzip = response.getContent().toByteArray();
					long gzipStartTime = System.currentTimeMillis();
					// We need to pass the current stage timeout to the decompression; gunzip might take long time to finish
					timeout = checkStageTimeout();
					if (readTimeout > 0) {
						timeout = Math.min(readTimeout * 1000, timeout);
					}
					byte[] responseContentPostGunzip = new Gzip().decompress(timeout, responseContentPreGunzip);
					long gzipEndTime = System.currentTimeMillis();

//					if (ScheduledQuickStatsLogger.quickStatsLoggingIsEnabled) {
//						QuickStatsEngine2.engine.SUPPLIER_GUNZIP_TIME.logEvent(gzipEndTime - gzipStartTime);
//						QuickStatsEngine2.engine.SUPPLIER_GUNZIP_DATA_SIZE_PRE_GUNZIP.logEvent(responseContentPreGunzip.length);
//						QuickStatsEngine2.engine.SUPPLIER_GUNZIP_DATA_SIZE_POST_GUNZIP.logEvent(responseContentPostGunzip.length);
//					}

					response.setContent(responseContentPostGunzip, response.getHeaderList().getHeader("Content-Type").getValue());
					
//					totalGZipTime += (System.currentTimeMillis() - gzipStartTime);
//					int sizeAfterDecompress = response.getContent().length();
//					int sizeSaving = sizeAfterDecompress - sizeBeforeDecompress;
//					if (log.isDebugEnabled()) log.debug("[GZIP] Finished Decompression");
//					if (log.isDebugEnabled()) log.debug("[GZIP] Compressed Size: " + responseContentPreGunzip.length);
//					if (log.isDebugEnabled()) log.debug("[GZIP] Decompressed Size: " + responseContentPostGunzip.length);
//					if (log.isDebugEnabled()) log.debug("[GZIP] Saving: " + sizeSaving);
					getDownloader().getResponse().getHeaderList().removeHeader("Content-Encoding");
				} catch (TimeoutException te) {
					// This exception may be thrown by the decompression process
					int st = getStageTimeout();
					logStageTimeoutPerSupplier(st);
					throw new StageTimeoutException("Stage timed out (Stage timeout: " + st + " ms). Error: " + te.getMessage());
				} catch (Throwable t) {
					if (log.isDebugEnabled()) log.debug("exception", t);
					
//					StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//					int max=40, i;
//			        StringBuilder sb = new StringBuilder("GZIP error 3 - ");
//			        for (i=1 ; i<Math.min(stackTraceElements.length, 15); i++)
//				    {
//			        	StackTraceElement ste = stackTraceElements[i];
//			            String classname = ste.getClassName();
//			            sb.append(classname.length()<max ? classname : classname.substring(classname.length()-max));
//			            sb.append(':').append(ste.getLineNumber()).append('<');
//			        }
//			        if(i<stackTraceElements.length-1){
//			        	sb.append("...");
//			        }else{
//			        	sb.setLength(sb.length()-1);
//			        }
//			        QuickStatsEngine2.engine.MISC_ERRORS.logEvent(sb.toString());
					
					String message = t.getMessage();
					if (message == null) message = "";
					if (message.length() > 60) message = message.substring(0, 60);
//					QuickStatsEngine2.engine.MISC_ERRORS.logEvent("GZIP error 2 - " + getSpiderName() + message);
					try {
						message += ":"+getDownloader().getRequest().getUrl().getNetUrl().toString(true);
					} catch (Throwable tt2) {message += "Error Getting URL.....";}
					if (message.length() > 90) message = message.substring(0, 90);
//					QuickStatsEngine2.engine.MISC_ERRORS.logEvent("GZIP error 2 - " + getSpiderName() + message);
				}
			} else {
				if (log.isDebugEnabled()) {
					if (enableCompression) {
						if (log.isDebugEnabled()) log.debug("[GZIP] Not Supported");
					} else {
						if (log.isDebugEnabled()) log.debug("[GZIP] Is Disabled");
					}
				}
			}
//		} 
/*		finally {
			try {
				logSupplierBandwidth();
			} catch (Throwable t) {
				if (log.isDebugEnabled()) log.debug("exception", t);
			}
		}
		*/
		multipartBoundary = null;
	}

	/**
	 * Downloads the HTTP response. 
	 * If readTimeout is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used.
	 * @param readTimeout the read timeout in seconds. BEWARE: Zulf overrides in planestagesearch and details
	 * @return the downloaded html.
	 */
	private final HttpContent download(int readTimeout, String filename) throws IOException, HttpException {
		if (!proxyDisabled && proxyUrl != null) getDownloader().setProxyUrl(proxyUrl);
		if (!proxyDisabled && phpProxyUrl != null) getDownloader().setPHPProxyUrl(phpProxyUrl);
		if (getCookieList().size() > 0) getCookieList().setCookiesIn(getDownloader().getRequest());
		// sleep( getRandomNumber( 1, 2 ) );
		if (refererUrl != null) setHttpHeader(HttpRequestHeaderList.HEADER_REFERER, refererUrl.toString(true));
		if (multipartBoundary != null) {
			HttpRequest request = getDownloader().getRequest();
			String content = request.getContent().toString();
			StringBuilder part = new StringBuilder(content);
			part.append("--").append(multipartBoundary).append("--\r\n");
			request.getContent().set(part.toString());
			request.setHeader("Content-Length", String.valueOf(request.getContent().length()));
		}
		if (!proxyDisabled && proxyUrl != null) {
			getDownloader().setProxyUrl(proxyUrl);
			getDownloader().getRequest().getUrl().getNetUrl().setAbsolute(true);
		} else if (!proxyDisabled && phpProxyUrl != null) {
			getDownloader().setPHPProxyUrl(phpProxyUrl);
			getDownloader().getRequest().getUrl().getNetUrl().setAbsolute(true);
		} else {
			getDownloader().getRequest().getUrl().getNetUrl().setAbsolute(false);
		}
		if (debugIsEnabled()) {
			if (log.isDebugEnabled()) log.debug("[Http Request]\n" + REQUEST_WRITER.write(getDownloader().getRequest()));
			if (log.isDebugEnabled()) log.debug("[Http Request]\n" + getDownloader().getRequest());
		}
		if (getHttpRequest().getMethod().equals(METHOD_POST))
			if (getHttpRequest().getContent().length() == 0) getHttpRequest().getHeaderList().set(new HttpHeader(HttpMessageHeaderList.HEADER_CONTENT_LENGTH, "0"));
		if (fileDownloads) {
			if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Warn]  loading from " + filename + ".http");
			byte[] bytes = readFileToByteArray(filename + ".http");
			HttpResponse response = new HttpResponse();
			response.readFrom(new StreamReader(bytes), "File download: " + this.getSpiderName());
			getDownloader().setResponse(response);
		} else
			download(readTimeout);
		refererUrl = getDownloader().getRequest().getUrl().getNetUrl();
		if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Referer] " + refererUrl.toString(true));
		// Redirects
		NetUrl newRefererUrl = refererUrl;
		int i = 0;
		final boolean limitRedirects = maxRedirect != -1;
		while (getDownloader().isRedirectResponse() && redirectsEnabled) {
			if (limitRedirects && (i >= maxRedirect)) {
				if (log.isDebugEnabled()) {
					log.debug("Maximum redirects (" + maxRedirect + ") reached, stopping.");
				}
				throw new HttpMaxRedirectsException("Maximum redirects(" + maxRedirect + ") reached");
			}

			HttpHeader header = getDownloader().getResponse().getHeaderList().getHeader(HttpResponseHeaderList.HEADER_LOCATION);
			HttpHeader headerRefresh = getDownloader().getResponse().getHeaderList().getHeader(HttpResponseHeaderList.HEADER_REFRESH);
			if (header != null) locationHeader = header.getValue();
			if (headerRefresh != null && refreshRedirectionDisabled) break;

			if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Http Response Redirect]\n" + getDownloader().getResponse().toString(false));
			if (cookiesAreEnabled()) getCookieList().getCookiesFrom(getDownloader().getResponse());
			HttpRequest redirect = getDownloader().getRedirectRequest(getCookieList());
			redirect.getVersion().set(httpVersion);
			// Authorization
			HttpHeader authorizationHeader = getDownloader().getRequest().getHeaderList().getHeader(HttpRequestHeaderList.HEADER_AUTHORIZATION);
			if (authorizationHeader != null) redirect.getHeaderList().add(authorizationHeader);

			// url contains a space! encode it!
			if (redirect.getUrl().getNetUrl().getQuery().indexOf(' ') != -1) {
				HttpQuery query = new HttpQuery(redirect.getUrl(), httpQueryCharset, false);
				redirect.getUrl().getNetUrl().setQuery(query.toString());
			}
			if (enableCompression) {
				redirect.setHeader(HttpRequestHeaderList.HEADER_ACCEPT_ENCODING, "gzip");
			}
			getDownloader().setRequest(redirect);
			if (refererUrl != null) setHttpHeader(HttpRequestHeaderList.HEADER_REFERER, refererUrl.toString(true));
			if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Http Request Redirect]\n" + redirect);
			if (!proxyDisabled && proxyUrl != null) getDownloader().setProxyUrl(proxyUrl);
			if (!proxyDisabled && phpProxyUrl != null) getDownloader().setPHPProxyUrl(phpProxyUrl);
			download(readTimeout);
			newRefererUrl = getDownloader().getRequest().getUrl().getNetUrl();
			i++;

		}
		refererUrl = newRefererUrl;
		// Cookies
		if (cookiesAreEnabled()) getCookieList().getCookiesFrom(getDownloader().getResponse());
		// Response
		if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Http Response]\n" + getDownloader().getResponse().toString(false));
		byte[] http = getDownloader().getResponse().writeToByteArray();
		writeFile(filename + ".http", http);
		byte[] content = getDownloader().getResponse().getContent().toByteArray();
		writeFile(filename, content);
		return getDownloader().getResponse().getContent();
	}

	public InputStream download(String url) throws IOException {
		return new URL(url).openStream();
	}

	/**
	 * Download from file instead of online.
	 */
	public void downloadFromFile() {
		downloadFromFile(false);
	}

	/**
	 * Download from file instead of online.
	 * @param forced
	 */
	public void downloadFromFile(boolean forced) {
		if (WebSpiderConfig.isDownloadFromFileEnabled() || forced) fileDownloads = true;
	}

	/**
	 * Downloads the HTTP response and parses the HTML forms found in it<br />
	 * <b>Uses system variable to estimate the read timeout that will be used for this download</b> 
	 * @param filename
	 * @throws IOException
	 * @throws HttpException
	 * @throws HtmlException
	 * @throws InterruptedException
	 */
	public final void downloadHtmlFormList(String filename) throws IOException, HttpException, HtmlException, InterruptedException {
		downloadHtmlFormList(0, filename);
	}
	
	/**
	 * Downloads the HTTP response, selecting it as text.
	 * If readTimeout is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used.
	 * @param readTimeout the read timeout in seconds.
	 * @return the downloaded text.
	 * @throws InterruptedException
	 */
	public final void downloadHtmlFormList(int readTimeout, String filename) throws IOException, HttpException, HtmlException, InterruptedException {
		downloadHtmlFormList(readTimeout, filename, true);
	}

	/**
	 * Downloads the HTTP response, selecting it as text.
	 * If readTimeout is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used.
	 * @param readTimeout the read timeout in seconds.
	 * @return the downloaded text.
	 * @throws InterruptedException
	 */
	public final void downloadHtmlFormList(int readTimeout, String filename, boolean newPage) throws IOException, HttpException, HtmlException, InterruptedException {
		if (htmlForm != null) for (int i = 0; i < getHtmlForm().fields(); i++) {
			HtmlField field = getHtmlForm().getField(i);
			if (!field.isInitialised() && !field.isOptional()) {
				if (field.isHidden()) throw new HtmlException("Existing HTML form field (hidden field) not initialised: \"" + field.getName(0) + "\"" + " Field Type: " + field.getType());
				throw new HtmlException("Existing HTML form field (non hidden field) not initialised: \"" + field.getName(0) + "\"" + " Field Type: " + field.getType());
			}
		}
		if (!filename.endsWith(".html")) filename = filename + ".html";
		String html = downloadHttpToString(readTimeout, filename);
		selectText(html);
		if (newPage) {
			NetUrl previousUrl = getDownloader().getRequest().getUrl().getNetUrl();
			htmlFormList.setObject(new HtmlFormReader().read(html, previousUrl));
			if (debugIsEnabled()) if (getHtmlFormList().isEmpty()) {
				if (log.isDebugEnabled()) log.debug("[No HTML Forms Found]");
			} else {
				if (log.isDebugEnabled()) log.debug("[HTML Forms]\n" + new HtmlFormWriter().write(getHtmlFormList()));
			}
		}
	}

	/**
	 * Downloads the HTTP response and updates the selection buffer
	 * <b>Uses system variable to estimate the read timeout that will be used for this download</b>
	 * @param filename
	 * @throws IOException
	 * @throws HttpException
	 * @throws InterruptedException
	 */
	public void downloadHttp(String filename) throws IOException, HttpException, InterruptedException {
		downloadHttp(0, filename);
	}

	/**
	 * Downloads the HTTP response and updates the selection buffer
	 * If readTimeout is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used.
	 * @param readTimeout timeout in seconds.
	 * @param filename
	 * @throws IOException
	 * @throws HttpException
	 * @throws InterruptedException
	 */
	public void downloadHttp(int readTimeout, String filename) throws IOException, HttpException, InterruptedException {
		String text = downloadHttpToString(readTimeout, filename);
		selectText(text);
	}

	/**
	 * Downloads the HTTP response.
	 * If readTimeout is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used.
	 * @param readTimeout the read timeout in seconds. BEWARE: Zulf overrides in planestagesearch and details
	 * @return the downloaded html.
	 */
	public HttpContent downloadHttpToByteArray(int readTimeout, String filename) throws IOException, HttpException, InterruptedException {
		if (isSynchronized()) synchronized (getMutex()) {
			try {
				// if (debugIsEnabled()) {
			// if (log.isDebugEnabled()) log.debug ("[Synchronize] " + getClass().getName() + "/" +
			// hashCode());
			// }
			return download(readTimeout, filename);
		} finally {
			// if (debugIsEnabled()) {
			// if (log.isDebugEnabled()) log.debug ("[Synchronized] " + getClass().getName() + "/"
			// + hashCode());
			// }
		}
	}
		return download(readTimeout, filename);
	}

	/**
	 * Downloads the HTTP response.
	 * <b>Uses system variable to estimate the read timeout that will be used for this download</b>
	 * @return the downloaded html.
	 */
	public HttpContent downloadHttpToByteArray(String filename) throws IOException, HttpException, InterruptedException {
		return downloadHttpToByteArray(0, filename);
	}
	
	/**
	 * Downloads HTTP to string.
	 * If readTimeout is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used.
	 * @param readTimeout(seconds) the read timeout.
	 * @param filename the filename.
	 * @return the string. ZULF overrides in planestagebooking
	 */
	protected String downloadHttpToString(int readTimeout, String filename) throws IOException, HttpException, InterruptedException {
		HttpContent content = downloadHttpToByteArray(readTimeout, filename);
		if (httpCharset != null) content.setCharset(httpCharset);
		return content.toString();
	}

	/**
	 * Downloads HTTP to string.
	 * <b>Uses system variable to estimate the read timeout that will be used for this download</b>
	 * @param filename the filename.
	 * @return the string. ZULF overrides in planestagebooking
	 */
	protected String downloadHttpToString(String filename) throws IOException, HttpException, InterruptedException {
		return downloadHttpToString(0, filename);
	}

	/**
	 * Download xml.
	 * If readTimeout is less then or equals to 0 then timeout from system variable will be used. If greater then 0 - the lowest of both will be used.
	 * @param readTimeout the read timeout (seconds).
	 * @param filename the filename.
	 */
	public Xml downloadXml(int readTimeout, String filename) throws IOException, HttpException, InterruptedException {
		if (!filename.endsWith(".xml")) filename = filename + ".xml";
		String selection = downloadHttpToString(readTimeout, filename);
		selectText(selection);
		try {
			return new XmlReader().read(selection);
		} catch (XmlException xe) {
			if (log.isDebugEnabled()) log.debug("[Invalid XML] " + xe.getMessage() + "\n{}", selection);
			throw xe;
		}
	}

	/**
	 * Download xml.
	 * <b>Uses system variable to estimate the read timeout that will be used for this download</b>
	 * @param filename the filename.
	 */
	public Xml downloadXml(String filename) throws IOException, HttpException, InterruptedException {
		return downloadXml(0, filename);
	}

	/**
	 * Debug the given text.
	 * @param text the text.
	 */
	public final void echo(Object text) {
		debug(text);
	}

	/**
	 * Enable redirects.
	 */
	public void enableRedirects() {
		redirectsEnabled = true;
	}

	/**
	 * Returns true if the given HTTP form field exists.
	 * @param field the field.
	 * @return true if the given HTTP form field exists.
	 */
	public final boolean existsHttpFormField(String field) throws HtmlException {
		if (field == null) throw new NullPointerException();
		HtmlField htmlField = getHtmlForm().getField(field, false);
		return htmlField != null;
	}

	/**
	 * Extract an html page from the given html.
	 * @param html the html.
	 */
	public final void extractHtmlFormList(String html) {
		NetUrl previousUrl = getDownloader().getRequest().getUrl().getNetUrl();
		htmlFormList.setObject(new HtmlFormReader().read(html, previousUrl));
		if (debugIsEnabled() && log.isDebugEnabled()) {
			if (getHtmlFormList().isEmpty()) {
				log.debug("[Html Page Empty]");
			} else {
				log.debug("[Html Page]\n" + new HtmlFormWriter().write(getHtmlFormList()));
			}
		}
	}

	/**
	 * Extracts and returns the entire buffer (does not move).
	 * @throws SelectionException if the extraction failed.
	 */
	public String extractText() throws SelectionException {
		return selection.extractText(null, null, NO_MOVE);
	}

	/**
	 * Extracts and returns the first string beginning and ending with the given delimiters.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @return the extracted text.
	 * @throws SelectionException if the extraction failed and was not optional.
	 */
	public final String extractText(String begin, String end) throws SelectionException {
		return extractText(begin, end, 0);
	}

	/**
	 * Extracts and returns the first string beginning and ending with the given delimiters.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @param options the options (OPTIONAL | MOVE | INCLUDE_DELIMITERS).
	 * @return the extracted text.
	 * @throws SelectionException if the extraction failed and was not optional.
	 */
	public String extractText(String begin, String end, int options) throws SelectionException {
		return selection.extractText(begin, end, options);
	}

	/**
	 * Writes a file from the given text.
	 * @param filename the filename.
	 */
	public final void extractToFile(String filename) throws IOException, SelectionException {
		writeFile(filename, extractText());
	}

	/**
	 * Select an XML tag.
	 * @param tag the tag.
	 * @return true if the selection succeeded.
	 * @throws SelectionException if the selection failed.
	 */
	public final String extractXml(String tag) throws SelectionException {
		return extractXml(tag, 0);
	}

	/**
	 * Select an XML tag.
	 * @param tag the tag.
	 * @param options the selection options.
	 * @return true if the selection succeeded.
	 * @throws SelectionException if the selection failed.
	 */
	public final String extractXml(String tag, int options) throws SelectionException {
		if (tag == null) throw new NullPointerException();
		return extractText('<' + tag + '>', "</" + tag + '>', options);
	}

	/**
	 * Returns true if the given field contains the value.
	 * @param name the field name.
	 * @param value the value.
	 * @return true if the given field contains the value.
	 */
	public final boolean fieldContainsValue(String name, String value) throws HtmlException {
		return indexOfHttpFormField1(name, value) != -1;
	}

	/**
	 * Returns true if file writing is disabled.
	 * @return true if file writing is disabled.
	 */
	public final boolean fileWritingIsDisabled() {
		return fileWritingDisabled;
	}

	/**
	 * Returns the string stored on the given key.
	 * @param key the key.
	 * @return the string stored on the given key.
	 */
	public final String get(int index, Object key) {
		return get(String.valueOf(index), key);
	}

	/**
	 * Returns the string stored on the given key.
	 * @param key the key.
	 * @return the string stored on the given key.
	 */
	public final String get(Object key) {
		return get(0, key);
	}

	/**
	 * Returns the string stored on the given key.
	 * @param key the key.
	 * @return the string stored on the given key.
	 */
	public String get(String index, Object key) {
		if (key == null) throw new NullPointerException();
		String keyString;
		if (index == null)
			keyString = key.toString();
		else
			keyString = key.toString() + "[" + index + "]";
		return params.get(keyString);
	}

	/**
	 * Returns the amount from the given amount and currency.
	 * @return the amount.
	 */
	public final String getAmount(String text) {
//		Amount amount = new Amount(text);
//		return amount.toString();
		return null;
	}

	/**
	 * Returns the amount from the given amount and currency.
	 * @return the amount.
	 */
	public final String getAmountFromPrice(String amountAndCurrency) throws Exception {
//		Price price = new Price(amountAndCurrency);
//		return price.getAmount().toString();
		return null;
	}

	/**
	 * Gets and creates the directory.
	 * @return the directory.
	 */
	private final String getAndCreateDirectory() {
		String directory = getDirectory();
		new TextFile(directory).mkdirs();
		return directory;
	}

	/**
	 * Returns the string stored on the given key.
	 * @param key the key.
	 * @return the string stored on the given key.
	 */
	public final boolean getBoolean(int index, Object key) {
		return "true".equalsIgnoreCase(get(index, key));
	}

	/**
	 * Returns the string stored on the given key.
	 * @param key the key.
	 * @return the string stored on the given key.
	 */
	public final boolean getBoolean(Object key) {
		return "true".equalsIgnoreCase(get(0, key));
	}

	/**
	 * Returns the cookie list.
	 * @return the cookie list.
	 */
	public HttpCookieList getCookieList() {
		return cookieList.getObject();
	}

	/**
	 * Returns the currency from the given amount and currency.
	 * @return the currency.
	 */
	public final String getCurrencyFromPrice(String amountAndCurrency) throws Exception {
//		Price price = new Price(amountAndCurrency);
//		return price.getCurrency().toString();
		return null;
	}

	/**
	 * Returns the date format pattern.
	 * @return the date format pattern.
	 */
	public String getDateFormatPattern() {
		throw new IllegalStateException();
	}

	/**
	 * Returns the directory for file I/O.
	 * @return the directory for file I/O.
	 */
	protected String getDirectory() {
		String className = getClass().toString().toLowerCase();
		int indexDot2 = className.lastIndexOf('.');
		if (indexDot2 == -1) indexDot2 = className.length();
		int indexDot1 = className.lastIndexOf('.', indexDot2 - 1);
		if (indexDot1 == -1) indexDot1 = 0;
		className = className.substring(indexDot1 + 1, indexDot2);
		return WebSpiderConfig.getLogsDirectory() + className;
	}

	/**
	 * Returns the downloader.
	 * @return the downloader.
	 */
	public HttpDownloader getDownloader() {
		return downloader;
	}

	/**
	 * Returns the HTML form.
	 * @return the HTML form.
	 */
	protected HtmlForm getHtmlForm() throws HtmlException {
		if (htmlForm == null) throw new HtmlException("HTML form not initialised");
		return htmlForm;
	}

	protected final HtmlForm getHtmlForm(String name) {
		return getHtmlFormList().get(name);
	}

	/**
	 * Returns the HTML page.
	 * @return the HTML page.
	 */
	public HtmlFormList getHtmlFormList() {
		HtmlFormList formList = htmlFormList.getObject();
		// Theo: Fix compression-decompression side-effect - before compression,
		// FieldButton references within a FieldInage used to point to a
		// FieldButton
		// in the HtmlForm; after decompression, those references point to an
		// equal
		// but different object. We have to update those references, so that
		// they
		// point to the same object as before compression. (Job #15587)
		if (formList != null) formList.updateFieldButtonReferencesWithinFieldImages();
		return formList;
	}

	public String getHttpCharset() {
		return httpCharset;
	}

	/**
	 * Returns the value of an HTTP form field.
	 * @param form
	 * @param field
	 * @return
	 * @throws HtmlException
	 */
	public final String getHttpFormField(int form, String field) throws HtmlException {
		if (field == null) throw new NullPointerException();
		if (!hasFormList()) throw new HtmlException("HTML page not initialised");
		HtmlForm htmlForm = getHtmlFormList().get(form);
		HtmlField htmlField = htmlForm.getField(field);
		int index = htmlField.getSelectedIndex();
		if (index == -1) throw new HtmlException("HTML form field has no selected value: \"" + field + "\"");
		return htmlField.getValue(index);
	}

	/**
	 * Returns the value of an HTTP form field.
	 * @param form
	 * @param field
	 * @return
	 * @throws HtmlException
	 */
	public final String getHttpFormField(String form, String field) throws HtmlException {
		if (form == null || field == null) throw new NullPointerException();
		HtmlForm htmlForm = getHtmlFormList().get(form);
		HtmlField htmlField = htmlForm.getField(field);
		int index = htmlField.getSelectedIndex();
		if (index == -1) throw new HtmlException("HTML form field has no selected value: \"" + field + "\"");
		return htmlField.getValue(index);
	}

	/**
	 * Returns the request.
	 * @return the request.
	 */
	protected HttpRequest getHttpRequest() {
		return downloader.getRequest();
	}

	protected HttpsProtocol[] getHttpsProtocols() {
		return null;
	}

	/**
	 * @return whether file downloads is TRUE
	 */
	public boolean getIsdownloadFromFileTrue() {
		return fileDownloads;
	}

	/**
	 * Returns the location header.
	 * @return the location header.
	 */
	public String getLocationHeader() {
		return locationHeader;
	}

	/**
	 * Returns the param map.
	 * @return the param map.
	 */
	public ParamMap getMap() {
		return params;
	}

	/**
	 * Returns the number for the given month.
	 * @param month the month.
	 * @return the number.
	 */
	public final String getMonthName(int month) {
		return UtilDate.getMonthName(month, 0);
	}

	/**
	 * Returns the number for the given month.
	 * @param month the month.
	 * @return the number.
	 */
	public final String getMonthNumber(String month) {
		return UtilDate.getMonthNumber(month.trim());
	}

	/**
	 * Returns the synchronization mutex.
	 * @return the synchronization mutex.
	 */
	public final Object getMutex() {
		return mutex;
	}

	/**
	 * Returns a random element of the given array.
	 * @param array the array.
	 * @return a random element of the given array.
	 */
	public Object getRandomElement(Object[] array) {
		return array[getRandomNumber(0, array.length - 1)];
	}

	/**
	 * Returns a random number between the given min and max.
	 * @param min the minimum.
	 * @param max the maximum.
	 * @return a random number between the given min and max.
	 */
	public int getRandomNumber(int min, int max) {
		if (max <= min) throw new IllegalArgumentException("min=" + min + ", max=" + max);
		return Random.getRandom().nextInt(max - min) + min;
	}

	/**
	 * Returns the referer url.
	 * @return the referer url.
	 */
	public NetUrl getRefererUrl() {
		return refererUrl;
	}

	/**
	 * Returns the selection.
	 * @return the selection.
	 */
	public Selection getSelection() {
		return selection;
	}

	/**
	 * Returns the spider name.
	 * @return the spider name.
	 */
	public abstract String getSpiderName();

	/**
	 * Gets the time in the twenty four hour clock.
	 * @param time the time in am/pm format.
	 * @return the time in the twenty four hour clock.
	 */
	public final String getTwentyFourHourTime(String time) throws ParseException {
		time = Text.removeWhitespace(time);
		return new UtilDate("hh:mma", time).toString("HH:mm");
	}

	/**
	 * Returns the given number as a two digit string. If the number is greater than two digits, it is trimmed.
	 * @param number the number.
	 */
	public final String getTwoDigitString(int number) {
		return Text.getTwoDigitString(number);
	}

	/**
	 * Returns the given number as a two digit string.
	 * @param number the number.
	 */
	public final String getTwoDigitString(String number) {
		try {
			return getTwoDigitString(Integer.parseInt(number));
		} catch (Exception e) {
			throw new NumberFormatException("Not an integer: \"" + number + "\"");
		}
	}

	/**
	 * Returns the full url for the given relative url.
	 * @param relativeUrl the relative url.
	 * @return the full url.
	 */
	public final String getUrl(String relativeUrl) {
		return getUrl(null, relativeUrl);
	}

	/**
	 * Returns the url combined from the given base and relative url.
	 * @param baseUrl the base url.
	 * @param relativeUrl the relative url.
	 * @return the combined url.
	 */
	public final String getUrl(String baseUrl, String relativeUrl) {
		NetUrl netUrl1 = refererUrl;
		if (baseUrl != null) netUrl1 = new NetUrl(baseUrl);
		NetUrl netUrl2 = new NetUrl(relativeUrl);
		netUrl2.setFrom(netUrl1);
		return netUrl2.toString(true);
	}

	/**
	 * Returns true if this has a form list.
	 * @return true if this has a form list.
	 */
	public boolean hasFormList() {
		return !htmlFormList.isEmpty();
	}

	/**
	 * HTML decode the given object.
	 * @param toDecode the object to decode.
	 */
	public final String htmlDecode(Object toDecode) {
		if (toDecode == null) throw new NullPointerException();
		return HtmlDecoder.getHtmlDecoder().decode(toDecode);
	}

	/**
	 * Removes all HTTP form fields starting with the given name.
	 * @param name the name.
	 */
	public final int indexOfHttpFormField1(String name, String value) throws HtmlException {
		if (name == null || value == null) throw new NullPointerException();
		HtmlField field = getHtmlForm().getField(name);
		for (int i = 0; i < field.size(); i++)
			if (field.getValue(i).equals(value)) return i;
		return -1;
		// throw new HtmlException("HTML form field: \"" + name + "\" does not
		// contain value: \"" + value + "\"");
	}

	/**
	 * Returns true if this spider is closed.
	 * @return true if this spider is closed.
	 */
	public final boolean isClosed() {
		return closed;
	}

	/**
	 * Returns true if synchronized.
	 * @return true if synchronized.
	 */
	public final boolean isSynchronized() {
		return mutex != null;
	}

//	private void logTimeoutPerSupplier(QuickStat stat, int timeout) {
//		try {
//			String stage = "NOT A STAGE";
//			if (this instanceof WebSpiderStage) stage = ((WebSpiderStage) this).getStageName().get();
//			String supplier = getSpiderName();
//			stat.logEvent(new String[]{stage==null?"NULL":stage, supplier==null?"NULL":supplier}, "" + timeout);
//		} catch (Throwable t) {
//			QuickStatsEngine2.engine.MISC_ERRORS.logEvent("Error logging supplier timeout");
//			if (log.isErrorEnabled()) log.error("Error logging quickstats for webspider timeout", t);
//		}
//	}
	
	private void logStageTimeoutPerSupplier(int stageTimeout) {
//		logTimeoutPerSupplier(QuickStatsEngine2.engine.SUPPLIER_STAGE_TIMEOUTS, stageTimeout);
	}
	
	private void logReadTimeoutPerSupplier(int readTimeout) {
//		logTimeoutPerSupplier(QuickStatsEngine2.engine.SUPPLIER_READ_TIMEOUTS, readTimeout);
	}

	private void logConnectTimeoutPerSupplier(int connectTimeout) {
//		logTimeoutPerSupplier(QuickStatsEngine2.engine.SUPPLIER_CONNECT_TIMEOUTS, connectTimeout);
	}

/*	private void logSupplierBandwidth() {
		if (TripPlanner.getTripPlanner().isInitialised()) {
			try {
				long bytesSent = downloader.getBytesSent();
				long bytesReceived = downloader.getBytesReceivied();
				GeneralisedStatsEngine statsEngine = TripPlanner.getTripPlanner().getGeneralisedStatsEngine();
				UserId id = statsEngine.getTFUserId();
				statsEngine.logEventAmount(GeneralisedStatsEngine.SUPPLIERBANDWIDTHSTATISTIC, getSpiderName(), bytesSent + bytesReceived, id, id);
			} catch (Throwable t) {
				if (log.isDebugEnabled()) log.debug("Stats engine failed logging supplier bandwidth: " + t);
				if (log.isErrorEnabled()) log.error("Error logging quickstats for supplier bandwidth", t);
			}
		}
	}
*/
	/**
	 * Multiply the given object by the number.
	 * @param toMultiply the number to multiply.
	 * @param multiplyBy what to multiply by.
	 * @return the result of the multiplication.
	 */
	public String multiply(Object toMultiply, long multiplyBy) {
		return multiply(toMultiply, String.valueOf(multiplyBy));
	}

	/**
	 * Multiply the given objects.
	 * @param toMultiply the number to multiply.
	 * @param multiplyBy what to multiply by.
	 * @return the result of the multiplication.
	 */
	public String multiply(Object toMultiply, Object multiplyBy) {
//		Amount amount1 = new Amount(toMultiply.toString().trim());
//		Amount amount2 = new Amount(multiplyBy.toString().trim());
//		return amount1.multiply(amount2).toString();
		return null;
	}

	/**
	 * Creates a new HTTP form request.
	 */
	public final void newHttpFormRequest() throws HtmlException {
		newHttpFormRequest(0);
	}

	/**
	 * Creates a new HTTP form request.
	 * @param name the form name.
	 */
	private final void newHttpFormRequest(HtmlForm form, String url) throws HtmlException {
		getHtmlForm().reset();
		if (url == null) url = getHtmlForm().getAction();
		newHttpRequest(url, getHtmlForm().getMethod());
	}

	/**
	 * Creates a new HTTP form request.
	 * @param index the index of the form.
	 */
	public final void newHttpFormRequest(int index) throws HtmlException {
		newHttpFormRequest(index, null);
	}

	/**
	 * Creates a new HTTP form request.
	 * @param index the index of the form.
	 */
	public final void newHttpFormRequest(int index, String url) throws HtmlException {
		if (!hasFormList()) throw new HtmlException("HTML page not initialised");
		htmlForm = getHtmlFormList().get(index);
		if (htmlForm == null) throw new HtmlException("HTML form not found, index=" + index);
		newHttpFormRequest(htmlForm, url);
	}

	/**
	 * Creates a new HTTP form request.
	 * @param name the form name.
	 */
	public final void newHttpFormRequest(String name) throws HtmlException {
		newHttpFormRequest(name, null);
	}

	/**
	 * Creates a new HTTP form request.
	 * @param name the form name.
	 */
	public final void newHttpFormRequest(String name, String url) throws HtmlException {
		if (name == null) throw new NullPointerException();
		if (!hasFormList()) throw new HtmlException("HTML page not initialised");
		htmlForm = getHtmlFormList().get(name);
		if (htmlForm == null) throw new HtmlException("HTML form not found: \"" + name + "\"");
		newHttpFormRequest(htmlForm, url);
	}

	/**
	 * Creates a new HTTP GET request.
	 * @param url the request URL.
	 */
	public final void newHttpGetRequest(String url) {
		htmlForm = null;
		newHttpRequest(url, METHOD_GET);
	}

	/**
	 * Creates a new HTTP POST request.
	 * @param url the request URL.
	 */
	public final void newHttpPostRequest(String url) {
		htmlForm = null;
		newHttpRequest(url, METHOD_POST);
	}

	/**
	 * Creates a new HTTP request.
	 * @param url the request URL.
	 * @param method the request method.
	 */
	private final void newHttpRequest(String url, String method) {
		url = HtmlDecoder.getHtmlDecoder().decode(url);
		HttpRequest request = new HttpRequest(method, url);

		// Hmmmm
		if (request.getUrl().getNetUrl().getQuery().indexOf(' ') != -1) {
			HttpQuery query = new HttpQuery(request.getUrl(), httpQueryCharset);
			request.getUrl().getNetUrl().setQuery(query.toString());
		}
		// Hmmmm

		if (cookiesAreEnabled()) {
			getCookieList().setCookiesIn(request);
		}
		if (method.equals(METHOD_POST)) {
			request.setHeader(HttpMessageHeaderList.HEADER_CONTENT_LENGTH, Integer.valueOf(0));
		}
		if (enableCompression) {
			request.setHeader(HttpRequestHeaderList.HEADER_ACCEPT_ENCODING, "gzip");
		}
		request.getVersion().set(httpVersion);
		downloader = new HttpDownloader(4, this);
		downloader.setHttpHeaderList(getCustomHttpHeadersList());
		downloader.getBrowser().setHeaders(request);
		downloader.setHttpHeaders(request);
		downloader.setRequest(request);
//		if (proxyCloudRequest) {
//			downloader.setProxyCloudRequest(proxyCloudUsagePool, getSpiderName());
//		}
		// Tell the downloader not to carry out the download if we are using the
		// non-blocking functionality.
		if (this instanceof NonBlockingRequestHandler) {
			downloader.setNonBlocking();
		}
	}

	/** Call this to override use of NIO from extractAirportMap methods.*/
	protected final void setBlocking()
	{
		downloader.setBlocking();
	}
	
	/**
	 * Debug the given text.
	 * @param text the text.
	 */
	public final void out(Object text) {
		debug(text);
	}

	/**
	 * Parse a date using the given pattern.
	 * @param pattern the pattern.
	 * @param date the date.
	 * @return the parsed date.
	 */
	protected final String parseDate(String pattern, String date) throws ParseException {
		return new UtilDate(pattern, date).toString(UtilDate.FORMAT_PATTERN);
	}

	/**
	 * Debug the given text.
	 * @param text the text.
	 */
	public final void print(Object text) {
		debug(text);
	}

	/**
	 * Reads and returns the text from the given file.
	 * @param filename the filename.
	 */
	public final String readFile(String filename) throws IOException {
		return new String(readFileToByteArray(filename));
	}

	/**
	 * Reads and returns the text from the given file.
	 * @param filename the filename.
	 */
	public final byte[] readFileToByteArray(String filename) throws IOException {
		String path = getDirectory() + "/" + filename;
		// byte[] stored = (byte[])storedPages.get(path);
		byte[] stored = null;
		// if (stored != null) return stored;
		if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Read File] \"" + path + "\"");
		stored = new TextFile(path).readToByteArray();
		// storedPages.put(path, stored);
		return stored;
	}

	/**
	 * Remove all occurances of the given string.
	 * @param removeIn the string to replace in.
	 * @param toRemove the string to replace.
	 * @return the result.
	 */
	public final String remove(String removeIn, String toRemove) {
		return replace(removeIn, toRemove, "");
	}

	/**
	 * Removes the first HTTP form field with the given name.
	 * @param name the name.
	 */
	public final void removeHttpFormField(String name) throws HtmlException {
		if (name == null) throw new NullPointerException();
		removeHttpFormField(name, 0);
	}

	/**
	 * Adds an HTTP form field.
	 * @param name the name.
	 * @param index the index.
	 */
	public final void removeHttpFormField(String name, int index) throws HtmlException {
		if (name == null) throw new NullPointerException();
		getHtmlForm().removeField(name, index);
	}

	/**
	 * Removes all HTTP form fields starting with the given name.
	 * @param name the name.
	 */
	public final void removeHttpFormFieldsStartingWith(String name) throws HtmlException {
		if (name == null) throw new NullPointerException();
		for (int i = 0; i < getHtmlForm().fields(); i++) {
			HtmlField field = getHtmlForm().getField(i);
			if (field.size() > 0) if (field.getName(0).startsWith(name)) {
				getHtmlForm().removeField(i);
				i--;
			}
		}
	}

	/**
	 * Remove all remaining HTTP form fields.
	 */
	public final void removeRemainingHttpFormFields() throws HtmlException {
		getHtmlForm().removeUninitialisedFields();
	}

	/**
	 * Replaces all occurances of the given string with the replacement.
	 * @param replaceIn the string to replace in.
	 * @param toReplace the string to replace.
	 * @param replaceWith the string to replace with.
	 * @return the result.
	 */
	public final String replace(String replaceIn, String toReplace, String replaceWith) {
		return Text.replace(replaceIn, toReplace, replaceWith);
	}

	protected void selectForm(int formIndex) {
		if (formIndex < 0) throw new IllegalArgumentException("formIndex=" + formIndex);
		HtmlFormList list = getHtmlFormList();
		if (list.size() == 0) throw new IllegalArgumentException("cannot select a form, form list is empty");
		if (list.size() > 1) {
			HtmlForm form = list.get(formIndex);
			list = new HtmlFormList();
			list.add(form);
		}
	}

	protected void selectForm(String formName) {
		if (formName == null) throw new NullPointerException();
		HtmlFormList list = getHtmlFormList();
		if (list.size() == 0) throw new IllegalArgumentException("cannot select a form, form list is empty");
		int formIndex = list.indexOf(formName);
		if (formIndex == -1) throw new IllegalArgumentException("form not found: '" + formName + "'");
		selectForm(formIndex);
	}

	/**
	 * Read and select the named file.
	 * @param filename the filename.
	 */
	public final void selectFromFile(String filename) throws IOException {
		String text = readFile(filename);
		selectText(text);
	}

	/**
	 * Selects the all text.
	 * @return true if the selection succeeded.
	 * @throws SelectionException if the selection failed.
	 */
	public final boolean selectText() throws SelectionException {
		return selectText(null, null, NO_MOVE);
	}

	/**
	 * Selects the given text.
	 * @param text the text to select.
	 */
	public void selectText(String text) {
		selection.selectText(text);
	}

	/**
	 * Selects the given text beginning and ending with the given delimiters.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @return true if the selection succeeded.
	 * @throws SelectionException if the selection failed.
	 */
	public final boolean selectText(String begin, String end) throws SelectionException {
		return selectText(begin, end, 0);
	}

	/**
	 * Selects the given text beginning and ending with the given delimiters.
	 * @param begin the beginning text.
	 * @param end the end text.
	 * @param options the options (OPTIONAL | MOVE | INCLUDE_DELIMITERS).
	 * @return true if the selection succeeded.
	 * @throws SelectionException if the selection failed and was not optional.
	 */
	public boolean selectText(String begin, String end, int options) throws SelectionException {
		return selection.selectText(begin, end, options);
	}

	/**
	 * Select an XML tag.
	 * @param tag the tag.
	 * @return true if the selection succeeded.
	 * @throws SelectionException if the selection failed.
	 */
	public final boolean selectXml(String tag) throws SelectionException {
		return selectXml(tag, 0);
	}

	/**
	 * Select an XML tag.
	 * @param tag the tag.
	 * @param options the selection options.
	 * @return true if the selection succeeded.
	 * @throws SelectionException if the selection failed.
	 */
	public final boolean selectXml(String tag, int options) throws SelectionException {
		if (tag == null) throw new NullPointerException();
		return selectText('<' + tag + '>', "</" + tag + '>', options);
	}

	/**
	 * Sets the value of the given key.
	 * @param key the key.
	 * @param val the value.
	 */
	public final void set(int index, Object key, Object val) {
		set(String.valueOf(index), key, val);
	}

	/**
	 * Sets the value of the given key.
	 * @param key the key.
	 * @param val the value.
	 */
	public final void set(Object key, boolean val) {
		set(key, String.valueOf(val));
	}

	/**
	 * Sets the value of the given key.
	 * @param key the key.
	 * @param val the value.
	 */
	public final void set(Object key, long val) {
		set(key, String.valueOf(val));
	}

	/**
	 * Sets the value of the given key.
	 * @param key the key.
	 * @param val the value.
	 */
	public final void set(Object key, Object val) {
		set(0, key, val);
	}

	/**
	 * Sets the value of the given key.
	 * @param key the key.
	 * @param val the value.
	 */
	public void set(String index, Object key, Object val) {
		if (key == null || val == null) throw new NullPointerException();
		String keyString;
		if (index == null)
			keyString = key.toString();
		else
			keyString = key.toString() + "[" + index + "]";
		String valString = val.toString();
		params.put(keyString, valString);
	}

	/**
	 * Set the cookies from the given spider.
	 * @param spider the spider.
	 */
	protected void setCookies(WebSpider spider) {
		if (spider.cookiesAreEnabled())
			getCookieList().getCookiesFrom(spider.getCookieList());
		else
			disableCookies();
	}

	/**
	 * Set the debug.
	 * @param enabled true to enable debug.
	 */
	public void setDebug(boolean enabled) {
		debugEnabled = enabled;
	}

	/**
	 * Set the decimal places for the given number.
	 * @param decimal the decimal number.
	 * @param places the places.
	 * @return the number with the decimal places set.
	 */
	public String setDecimalPlaces(Object decimal, int places) {
		BigDecimal bd = new BigDecimal(decimal.toString().trim());
		return bd.setScale(places).toString();
	}

	/**
	 * Sets the HTTP settings from the given stage.
	 * @param previous the stage.
	 */
	public final void setFromPrevious(WebSpider previous) throws XmlException {
		if (previous != null) {
			setCookies(previous);
			setHtmlFormList(previous);
			setRefererUrl(previous);
			setProxyUrl(previous);
			setPHPProxyUrl(previous);
			setProxyDisabled(previous);
			setCompression(previous);
			setProxyCloudRequest(previous);
			setHttpVersion(previous);
		}
	}

	protected void setHtmlForm(HtmlForm form) {
		htmlForm = form;
	}

	/**
	 * Sets the html page from the given spider.
	 * @param spider the spider.
	 */
	public void setHtmlFormList(HtmlFormList list) {
		htmlFormList.setObject(list);
	}

	/**
	 * Sets the html page from the given spider.
	 * @param spider the spider.
	 */
	protected void setHtmlFormList(WebSpider spider) {
		htmlFormList.setObject(spider.getHtmlFormList());
	}

	/**
	 * Set the character set.
	 * @param charset the character set.
	 */
	public void setHttpCharset(String charset) {
		if (charset == null) throw new NullPointerException();
		httpCharset = charset;
	}

	/**
	 * Sets an HTTP cookie.
	 * @param key the key.
	 * @param value the value.
	 */
	public final void setHttpCookie(String key, String value) {
		if (key == null) throw new NullPointerException();
		if (value == null)
			getCookieList().remove(key);
		else {
			HttpCookie cookie = new HttpCookie(key, value);
			getCookieList().set(cookie);
		}
	}

	/**
	 * Sets the given header to the request (overwriting any existing header).
	 * @param header the header.
	 * @param value the value.
	 */
	public final void setHttpHeader(String header, String value) {
		getDownloader().getRequest().getHeaderList().set(new HttpHeader(header, value));
	}

	/**
	 * Set the HTTP method as GET.
	 */
	public final void setHttpMethodGet() {
		downloader.getRequest().getMethod().set(METHOD_GET);
		if (htmlForm != null) htmlForm.setMethod(METHOD_GET);
	}

	/**
	 * Set the HTTP method as POST.
	 */
	public final void setHttpMethodPost() {
		downloader.getRequest().getMethod().set(METHOD_POST);
		if (htmlForm != null) htmlForm.setMethod(METHOD_POST);
	}

	public void setHttpQueryCharset(String charset) {
		if (charset == null) throw new NullPointerException();
		httpQueryCharset = charset;
	}

	/**
	 * Sets the HTTP version.
	 */
	public void setHttpVersion() {
		setHttpVersion(HttpVersionList.VERSION_11);
	}

	/**
	 * Sets the HTTP protocol version from already initialised {@link WebSpider} wrapper
	 * @param spider
	 * @throws NullPointerException
	 */
	protected boolean setHttpVersion(WebSpider spider) throws NullPointerException {
		if (spider == null) {
			throw new NullPointerException();
		}
		return setHttpVersion(spider.getHttpVersion());
	}

	/**
	 * Sets the HTTP protocol version
	 * @param version
	 * @return true if the supplied version is supported, otherwise false
	 * @throws NullPointerException
	 */
	public boolean setHttpVersion(final String version) throws NullPointerException {
		if (version == null) {
			throw new NullPointerException();
		}
		boolean found = false;
		FieldList fieldList = new FieldList(HttpVersionList.class);
		for (int x = 0; x < fieldList.size(); x++) {
			Object c = fieldList.getValue(x);
			if ((c instanceof String) && version.equals((String) c)) {
				found = true;
				httpVersion = (String) c;
				if (downloader != null && downloader.hasARequest()) {
					downloader.getRequest().getVersion().set(httpVersion);
				}
				break;
			}
		}
		return found;
	}

	/**
	 * Set this as multipart.
	 */
	public final void setMultiPart() {
		multipartBoundary = "---------------------------" + Random.getRandom().nextUnique(11, 10);
		getDownloader().getRequest().setHeader("Content-Type", "multipart/form-data; boundary=" + multipartBoundary);
		if (debugEnabled) if (log.isDebugEnabled()) log.debug("[Multi-Part Boundary] '" + multipartBoundary + "'");
	}

	/**
	 * Set whether the proxy is disabled from another spider.
	 * @param spider the spider.
	 */
	protected void setProxyDisabled(WebSpider spider) {
		proxyDisabled = spider.proxyDisabled;
	}

	/**
	 * Set the proxy url.
	 * @param url the url.
	 */
	public void setProxyUrl(NetUrl url) {
		setProxyUrl(url, true);
	}

	/**
	 * clear proxy url
	 */
	public void clearProxyUrl() {
		clearProxyUrl(true);
	}

	/**
	 * clear proxy url
	 * @param checkForce
	 */
	public void clearProxyUrl(boolean checkForce) {
		if (!checkForce || !forceProxy) {
			proxyUrl = null;
			if (getDownloader() != null) getDownloader().clearProxyUrl();
		}
	}

	/**
	 * clear php proxy url
	 */
	public void clearPHPProxyUrl() {
		clearPHPProxyUrl(true);
	}

	/**
	 * clear php proxy url
	 * @param checkForce
	 */
	public void clearPHPProxyUrl(boolean checkForce) {
		if (!checkForce || !forceProxy) {
			phpProxyUrl = null;
			if (getDownloader() != null) getDownloader().clearPHPProxyUrl();
		}
	}

	/**
	 * Set the php proxy url.
	 * @param secure use encrypted connection when communicating with the php proxy
	 * @param url the url.
	 */
	public void setPHPProxyUrl(NetUrl url) {
		if (url == null) {
			throw new NullPointerException();
		}
		setPHPProxyUrl(url, true);
	}

	/**
	 * Set the proxy url.
	 * @param url the url.
	 */
	public void setProxyUrl(NetUrl url, boolean checkForce) {
		// The aim of this check is to ensure that sepecial servers such as
		// (squid/LFT) ignore
		// the proxies set up in the spider
		if (!checkForce || !forceProxy) proxyUrl = url;
	}

	/**
	 * Set the php proxy url.
	 * @param url the url.
	 * @param checkForce
	 */
	public void setPHPProxyUrl(NetUrl url, boolean checkForce) {
		// The aim of this check is to ensure that sepecial servers such as
		// (squid/LFT) ignore
		// the proxies set up in the spider
		if (!checkForce || !forceProxy) phpProxyUrl = url;
	}

	/**
	 * Set the proxy url.
	 * @param url the url.
	 */
	public void setProxyUrl(String url) {
		if (url == null) throw new NullPointerException();
		setProxyUrl(new NetUrl(url));
	}

	/**
	 * Set the php proxy url.
	 * @param url the url.
	 */
	public void setPHPProxyUrl(String url) {
		if (url == null) throw new NullPointerException();
		setPHPProxyUrl(new NetUrl(url));
	}

	/**
	 * Set the proxy url.
	 * @param url the url.
	 */
	public void setProxyUrl(WebSpider spider) {
		proxyUrl = spider.proxyUrl;
	}

	/**
	 * Set the php proxy url.
	 * @param url the url.
	 */
	public void setPHPProxyUrl(WebSpider spider) {
		phpProxyUrl = spider.phpProxyUrl;
	}

	/**
	 * Returns the referer url.
	 * @return the referer url.
	 */
	public void setRefererUrl(NetUrl url) {
		refererUrl = url;
	}

	/**
	 * Returns the referer url.
	 * @return the referer url.
	 */
	public void setRefererUrl(String url) {
		setRefererUrl(new NetUrl(url));
	}

	/**
	 * Set the referer url.
	 * @param url the url.
	 */
	public void setRefererUrl(WebSpider spider) {
		setRefererUrl(spider.refererUrl);
	}

	/**
	 * Set the http content.
	 * @param content the content.
	 */
	public final void setXmlContent(String content) {
		HttpRequest request = getDownloader().getRequest();
		request.setContent(content, MimeTypes.TEXT_XML);
	}

	/**
	 * Set the http content.
	 * @param content the content.
	 */
	public final void setXmlContent(Xml content) {
		setXmlContent(content, false);
	}

	/**
	 * Set the http content.
	 * @param content the content.
	 */
	public final void setXmlContent(Xml content, boolean header) {
		HttpRequest request = getDownloader().getRequest();
		String text = "";
		if (header) text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
		request.setContent(text + new XmlWriter().write(content), MimeTypes.TEXT_XML);
	}

	/**
	 * Sleep for the given number of seconds.
	 * @param seconds the number of seconds.
	 */
	public final void sleep(int seconds) throws InterruptedException, StageTimeoutException {
		if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Sleep] " + seconds + " seconds");
		int millis = seconds * 1000;
		checkStageTimeout(millis);
		Thread.sleep(millis);
	}

	/**
	 * Subtract the given objects.
	 * @param subtractFrom the number to subtract from.
	 * @param toSubtract what to subtract.
	 * @return the result of the subtraction.
	 */
	public String subtract(Object subtractFrom, Object toSubtract) {
//		Amount amount1 = new Amount(subtractFrom.toString().trim());
//		Amount amount2 = new Amount(toSubtract.toString().trim());
//		return amount1.subtract(amount2).toString();
		return null;
	}

	/**
	 * Synchronize this spider.
	 */
	public void synchronize() {
		synchronize(getClass());
	}

	/**
	 * Synchronize this spider.
	 * @param mutex the mutex.
	 */
	public void synchronize(Object mutex) {
		if (mutex == null) throw new NullPointerException();
		this.mutex = mutex;
	}

	/**
	 * Returns the given xml as string.
	 * @param xml the xml.
	 * @return the string
	 */
	public String toString(Xml xml) {
		return Xml.WRITER.write(xml);
	}

	/**
	 * Returns the given string as xml.
	 * @param xml the xml string.
	 * @return the xml
	 */
	public Xml toXml(String xml) {
		return Xml.READER.read(xml);
	}

	/**
	 * Trim the given amount.
	 * @param amount the amount.
	 * @return the amount.
	 */
	public final String trimDecimals(String amount) {
		int dotIndex = amount.indexOf('.');
		if (dotIndex == -1) return amount;
		int trimTo = amount.length() - 1;
		for (int i = trimTo; i >= 0; i--) {
			char c = amount.charAt(i);
			if (c != '0') {
				if (trimTo == i) return amount;
				trimTo = i + 1;
				break;
			}
		}
		if (trimTo < dotIndex + 2) trimTo++;
		return amount.substring(0, trimTo);
	}

	/**
	 * Decode and return the given string.
	 * @param text the text to decode.
	 * @return the decoded text.
	 */
	public final String urlDecode(String text) {
		return urlDecode(text, Charsets.UTF_8);
	}

	/**
	 * Decode and return the given string.
	 * @param text the text to decode.
	 * @return the decoded text.
	 */
	public final String urlDecode(String text, String charset) {
		return HttpBrowserFactory.getDefault().getBrowser().urlDecode(text, charset);
	}

	/**
	 * Encode and return the given string.
	 * @param text the text to encode.
	 * @return the encoded text.
	 */
	public final String urlEncode(String text) {
		return urlEncode(text, Charsets.UTF_8);
	}

	/**
	 * Encode and return the given string.
	 * @param text the text to encode.
	 * @return the encoded text.
	 */
	public final String urlEncode(String text, String charset) {
		return HttpBrowserFactory.getDefault().getBrowser().urlEncode(text, charset);
	}

	/**
	 * Writes a file from the given text.
	 * @param filename the filename.
	 * @param data the byte data.
	 */
	public final void writeFile(String filename, byte[] data) throws IOException {
		if (fileWritingIsDisabled()) {
			if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[File Writing Disabled]");
		} else {
			String path = getAndCreateDirectory() + "/" + filename;
			if (debugIsEnabled()) if (log.isDebugEnabled()) log.debug("[Write File] \"" + path + "\"");
			new TextFile(path).write(data);
		}
	}

	/**
	 * Writes a file from the given text.
	 * @param filename the filename.
	 * @param text the text.
	 */
	public final void writeFile(String filename, String text) throws IOException {
		writeFile(filename, text.getBytes());
	}

	/** Adds to the total of bytes sent as part of this stage. */
	public void addBytesSent(long p_bytes) {
		totalBytesSent += p_bytes;
	}

	/** Adds to the total of bytes received as part of this stage. */
	public void addBytesReceived(long p_bytes) {
		totalBytesReceived += p_bytes;
	}

	/** Adds to the total duration of downloads for this stage. */
	public void addDownloadTime(long p_milliseconds) {
		totalDurationOfDownloads += p_milliseconds;
	}

	/** Increments the number of downloads. */
	public void addDownload() {
		totalNumberOfDownloads++;
	}
	
}