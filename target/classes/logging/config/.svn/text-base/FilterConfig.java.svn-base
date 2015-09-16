package logging.config;

import logging.filter.TpLogFilter;
import core.xml.Xml;

public class FilterConfig {
	private final Xml xml;
	
	public FilterConfig(final Xml xml) {
		this.xml = xml;
	}
	
	public static TpLogFilter createFilter(final Xml xml) throws ConfigException {
		final Object classValue = xml.getAttribute("class").getValue();
		final String filterClass = classValue == null ? null : classValue.toString().trim(); 
		if (filterClass == null || filterClass.isEmpty()) {
			return null;
		}
		try {
			final Class<? extends TpLogFilter> cls = Class.forName(filterClass).asSubclass(TpLogFilter.class);
			final TpLogFilter inst = cls.newInstance();
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
	
	public TpLogFilter createFilter() throws ConfigException {
		return createFilter(xml);
	}
}
