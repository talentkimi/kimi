package logging;

import logging.config.ConfigException;
import logging.config.FilterConfig;
import logging.config.LogWriterConfig;
import logging.filter.TpLogFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.xml.Xml;

/**
 * All log-writers should be subclasses of this class.
 * @author Dimitrijs
 *
 */
public abstract class TpLogWriter {
	protected final Logger logger = LoggerFactory.getLogger(getClass()); 
	private TpLogFilter filter;
	private boolean enabled = true;
	private TpLogger parentLogger;
	
	/**
	 * This method must be implemented by subclass to actually append record to log.
	 * @param record record to append to log.
	 */
	protected abstract void doWrite(TpLogRecord record);

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
		final String name = xml.getName();
		if (name.equals("filter")) {
			filter = FilterConfig.createFilter(xml);
		} else if (name.equals("enabled")) {
			enabled = Boolean.parseBoolean(xml.getValue().toString());
		}
	}

	/**
	 * Getter for logger which own this writer.
	 * @return owner logger.
	 */
	public TpLogger getLogger() {
		return parentLogger;
	}

	/**
	 * Sets owner logger for this writer. 
	 * This method called by logger while reading configuration.
	 * @param logger parent logger.
	 */
	public void setLogger(TpLogger logger) {
		this.parentLogger = logger;
	}

	/**
	 * Reads configuration for this writer.
	 * This method usually no need to override. 
	 * It calls {@link #parseLeaf(Xml)} for each configuration element.
	 * @param xml
	 * @throws ConfigException
	 */
	public void parse(final Xml xml) throws ConfigException {
		if (xml == null) {
			return;
		}
		for (Xml child : xml.childList()) {
			parseLeaf(child);
		}
	}

	/**
	 * Checks if this writer is enabled and specified record is loggable 
	 * used configured filter. And if it is -  calls 
	 * {@ling #doWrite(TpLogRecord)} method to actually append log with specified record 
	 * @param record record to log
	 */
	public void write(final TpLogRecord record) {
		if (enabled && (filter == null || filter.accept(record))) {
			doWrite(record);
		}
	}

	/**
	 * Gets filter of this writer. 
	 * Filter used to check if records are loggable by this writer.
	 * @return instance of filter.
	 */
	public TpLogFilter getFilter() {
		return filter;
	}

	/**
	 * Sets filter for this writer. This method called by logger while initializing.
	 * Filter used to check if records are loggable by this writer.
	 * @param filter configured filter from configuration.
	 */
	public void setFilter(final TpLogFilter filter) {
		this.filter = filter;
	}

	/**
	 * Returns true if this writer is enabled by configuration.
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets whether writer is enabled by configuration.
	 * @param enabled
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
}
