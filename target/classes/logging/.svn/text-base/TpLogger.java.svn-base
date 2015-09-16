package logging;

/**
 * Abstract class to be implemented by loggers. 
 * Now we have two logger implementations: {@link TpLoggerImpl} and {@TpBufferedLogger}.
 * The first one is used for normal logging work.
 * The second one just for buffering records while the system not started up.
 * After the system is started the buffered records goes to normal loggers. 
 * @author Dimitrijs
 */
public abstract class TpLogger {
	
	/**
	 * Adds record to queue to process later by another thread. 
	 * Record added only if this logger is enabled and record passed by filter
	 * of this logger. 
	 * @param record
	 */
	public abstract void log(TpLogRecord record);

	/**
	 * Returns reader for this logger. Reader is defined in config-file.
	 * @return
	 */
	public abstract TpLogReader getReader();

	/**
	 * Returns Id of this logger.
	 * @return
	 */
	public abstract String getId();

}
