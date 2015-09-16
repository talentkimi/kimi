package logging.config;

import logging.TpLogReader;
import logging.TpLogger;
import core.xml.Xml;

public class LogReaderConfig {
	private final Xml xml;
	
	public LogReaderConfig(final Xml xml) {
		this.xml = xml;
	}
	
	public TpLogReader createReader(TpLogger logger) throws ConfigException {
		final Object classValue = xml.getAttribute("class").getValue();
		final String readerClass = classValue == null ? null : classValue.toString().trim(); 
		if (readerClass == null || readerClass.isEmpty()) {
			return null;
		}
		try {
			final Class<? extends TpLogReader> cls = Class.forName(readerClass).asSubclass(TpLogReader.class);
			final TpLogReader inst = cls.newInstance();
			inst.setLogger(logger);
			inst.parse(xml);
			return inst;
		} catch (IllegalAccessException ex) {
			throw new ConfigException(ex);
		} catch (InstantiationException ex) {
			throw new ConfigException(ex);
		} catch (ClassNotFoundException ex) {
			throw new ConfigException(ex);
		}
	}

}
