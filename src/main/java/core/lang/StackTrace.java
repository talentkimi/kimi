package core.lang;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.io.file.TextFile;

/**
 * A StackTrace Utility.
 */
public final class StackTrace {

	/** The logger. */
	private static StackTraceLogger stackTraceLogger = null;
	/** Regex pattern required by the toQSString metthod */
    private static final Pattern dotJavaPattern = Pattern.compile("^.*\\((.*).java*");

	/**
	 * Set the stack trace logger.
	 * @param logger the stack trace logger.
	 */
	public static final void setLogger(StackTraceLogger logger) {
		stackTraceLogger = logger;
	}

	/**
	 * Logs the given throwable.
	 * @param t the throwable.
	 */
	public static final void log(Throwable t) {
		if (stackTraceLogger != null && t != null) {
			long timestampInMillis = System.currentTimeMillis();
			String stackTrace = toString(t);
			stackTraceLogger.log(stackTrace, timestampInMillis);
		}
	}

	/**
	 * Returns the stack trace of the given throwable.
	 * @param t the throwable.
	 * @return the stack trace.
	 */
	public static final String toString(Throwable t) {
		StringBuilder builder = new StringBuilder();
		appendTo(t, builder);
		return builder.toString();
	}

	/**
	 * Returns a human readable string containing all non-duplicate
	 * class names, when reading stack trace from top to bottom.
	 * @param t
	 * @param maxNumClassNames
	 * @return
	 */
	public static final String toQSString(Throwable t, int maxNumClassNames) {
		if (maxNumClassNames < 1) maxNumClassNames = 1;
		ArrayList<String> tempArrayList = new ArrayList<String>(20);
		for (int i = 0; i < t.getStackTrace().length; i++) {
		    Matcher m = dotJavaPattern.matcher(t.getStackTrace()[i].toString());
		    if (m.find()) {
		        String javaClass = m.group(1);
		        if (!tempArrayList.contains(javaClass)) {
		        	tempArrayList.add(javaClass);
    		        if (tempArrayList.size() == maxNumClassNames) {
    		        	break;
    		        }
		        }
		    }
		}
		return tempArrayList.toString();
	}

	/**
	 * Returns the stack trace of the given throwable.
	 * @param t the throwable.
	 * @return the stack trace.
	 */
	public static final String toString(StackTraceElement[] trace) {
		StringBuilder builder = new StringBuilder();
		appendTo(trace, builder);
		return builder.toString();
	}

	/**
	 * Appends the stack trace of the given throwable to the builder.
	 * @param t the throwable.
	 * @param builder the builder.
	 */
	public static final void appendTo(Throwable t, StringBuilder builder) {
		builder.append(t.toString()).append(TextFile.newLine);
		appendTo(t.getStackTrace(), builder);
		t = t.getCause();
		if (t != null) {
			builder.append("Caused by: ");
			appendTo(t, builder);
		}
	}

	/**
	 * Appends the stack trace of the given throwable to the builder.
	 * @param t the throwable.
	 * @param builder the builder.
	 */
	public static final void appendTo(StackTraceElement[] trace, StringBuilder builder) {
		appendTo(trace, builder, -1);
	}
	
	/**
	 * Appends the stack trace of the given throwable to the builder.
	 * @param t the throwable.
	 * @param builder the builder.
	 * @param maxElements the max elements
	 */
	public static final void appendTo(StackTraceElement[] trace, StringBuilder builder, int maxElements) {
		for (int i = 0; (i < trace.length) && (maxElements < 0 || i < maxElements); i++) {
			builder.append("\tat ").append(trace[i]).append(TextFile.newLine);
		}
	}
}
