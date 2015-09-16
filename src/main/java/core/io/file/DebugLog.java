package core.io.file;

import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.UtilDate;
import core.util.bytes.Bytes;
import core.util.bytes.Megabytes;

public final class DebugLog {

	private static final Logger log = LoggerFactory.getLogger(DebugLog.class);


	/** The maximum log file size. */
	private static final Bytes MAX_FILE_LENGTH = new Megabytes(500);

	/** The log file map. */
	private static final Map<String, TextFile> logFileMap = new HashMap<String, TextFile>();

	/**
	 * Log the given line of text to the named log file.
	 * @param filenamePrefix the filename prefix (a timestamp and .log is added to it)
	 * @param textLine the line of text to append to the log file.
	 */
	public static final void log(String filenamePrefix, String textLine) {
		if (filenamePrefix == null) {
			throw new NullPointerException();
		}
		if (filenamePrefix.isEmpty()) {
			throw new IllegalArgumentException("filename prefix is empty");
		}
		synchronized (filenamePrefix) {
			try {
				TextFile file = logFileMap.get(filenamePrefix);
				String timestamp = new UtilDate().toString("yyMMdd-HHmmss");
				if (file == null || file.length() > MAX_FILE_LENGTH.getBytes()) {
					file = new TextFile(filenamePrefix + "-" + timestamp + ".log");
					logFileMap.put(filenamePrefix, file);
				}
				file.write(timestamp + '\t' + textLine + '\n', true);
			} catch (Throwable t) {
				if (log.isDebugEnabled()) log.debug ("exception", t);
			}
		}
	}

	/**
	 * Log the given line of text to the named log file.
	 * @param filenamePrefix the filename prefix (a timestamp and .log is added to it)
	 * @param title the title of the line of text (keep it short for preference).
	 * @param textLine the line of text to append to the log file.
	 */
	public static final void log(String filenamePrefix, String title, String textLine) {
		if (title == null) {
			throw new NullPointerException();
		}
		log(filenamePrefix, title + '\t' + textLine);
	}

	public static void main(String[] args) {
		log("fred", "BOB", "my name is JIM DAMMIT!!");
		log("fred", "BOB", "my name is FRED DAMMIT!!");
	}

}
