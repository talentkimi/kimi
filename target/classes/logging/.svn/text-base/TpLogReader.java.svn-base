package logging;

import java.util.List;

import logging.config.ConfigException;
import logging.config.LogWriterConfig;
import core.xml.Xml;

/**
 * General class for all log-readers wich provides functionality to search 
 * log records by range of index entries.
 * @author Dimitrijs
 *
 */
public abstract class TpLogReader {
	private TpLogger parentLogger;
	
	/**
	 * Finds and returns log records specified by range of index entries.
	 * @param start Index entry for first record in the range.
	 * @param end Index entre for last record in the range.
	 * @param offset the number of rows to skip before starting to return records.
	 * @param limit the maximum number of records to return.
	 * @return list of log records
	 */
	public abstract List<TpLogRecord> getRecords(TpLogIndexEntry start, TpLogIndexEntry end, int offset, int limit);

	/**
	 * Returns list of exists loggers' ids.
	 * @return 
	 */
	public abstract List<String> getExistsLoggers();
	
	/**
	 * Called by the {@link LogWriterConfig} to parse and set 
	 * configuration value from xml.
	 * Subclasses may overwrite this method and 
	 * call {@code super.parseLeaf(xml)} if xml-value not supported
	 * by this subclass.
	 * @param xml configuration to parse
	 * @throws ConfigException
	 */
	protected void parseLeaf(Xml xml) throws ConfigException {
	}

	/**
	 * Getter for logger which own this reader.
	 * @return owner logger.
	 */
	public TpLogger getLogger() {
		return parentLogger;
	}

	/**
	 * Sets owner logger for this reader. 
	 * This method called by logger while reading configuration.
	 * @param logger parent logger.
	 */
	public void setLogger(TpLogger logger) {
		this.parentLogger = logger;
	}
	
	/**
	 * Reads configuration for this reader.
	 * This method usually no need to override. 
	 * It calls {@link #parseLeaf(Xml)} for each configuration element.
	 * @param xml
	 * @throws ConfigException
	 */
	public void parse(final Xml xml) throws ConfigException {
		for (Xml child : xml.childList()) {
			parseLeaf(child);
		}
	}
}
