package logging.filter;

import logging.TpLogRecord;
import logging.config.ConfigException;
import logging.config.FilterConfig;
import core.xml.Xml;

public abstract class TpLogFilter {
	public abstract boolean accept(TpLogRecord record);

	public void parse(final Xml xml) throws ConfigException {
		for (Xml child : xml.childList()) {
			parseLeaf(child);
		}
	}

	/**
	 * Called by the {@link FilterConfig} to parse and set 
	 * configuration value from xml.
	 * Subclasses may overwrite this method and 
	 * call {@code super.parseLeaf(xml)} if xml-value not supported
	 * by this subclass.
	 * @param xml configuration to parse
	 * @throws ConfigException
	 */
	protected void parseLeaf(Xml xml) throws ConfigException {
	}

}
