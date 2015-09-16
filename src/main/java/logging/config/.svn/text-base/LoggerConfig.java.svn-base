package logging.config;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import core.util.ConfigClass;
import core.xml.Xml;
import core.xml.XmlException;

public class LoggerConfig implements ConfigClass  {

	private final Map<String, LoggerConfig> children = new HashMap<String, LoggerConfig>();
	private final Map<String, LogWriterConfig> writersConfig = new HashMap<String, LogWriterConfig>();
	private boolean enabled = true;
	private int queueSize = 1000; 
	private FilterConfig filterConfig;
	private LogReaderConfig readerConfig;
	
	public LoggerConfig() {
		this(null);
	}
	
	public LoggerConfig(LoggerConfig parent) {
		if (parent != null) {
			this.enabled = parent.enabled;
			this.filterConfig = parent.filterConfig;
			this.readerConfig = parent.readerConfig;
			this.queueSize = parent.queueSize;
			// Copy all writersConfig from parent
			for (Map.Entry<String, LogWriterConfig> e : parent.writersConfig.entrySet()) {
				final LogWriterConfig pWriter = e.getValue();
				final String writerName = e.getKey();
				final LogWriterConfig writer = new LogWriterConfig(pWriter);
				writersConfig.put(writerName, writer);
			}
		}
	}
	
	public LoggerConfig findByPath(final String path) {
		final StringTokenizer tok = new StringTokenizer(path, ".");
		LoggerConfig result = this;
		while (tok.hasMoreTokens()) {
			final String name = tok.nextToken();
			final LoggerConfig cfg = result.children.get(name);
			if (cfg == null) {
				break;
			}
			result = cfg;
		}
		return result;
	}
	
	@Override
	public void parseFrom(Xml xml) throws XmlException {
		for (Xml x : xml.childList()) {
			final String nodeName = x.getName();
			if (nodeName.equals("filterConfig")) {
				filterConfig = new FilterConfig(x);
			} else if (nodeName.equals("logger")) {
				final String name = x.getAttribute("name").getValue().toString();
				final LoggerConfig childLogger = new LoggerConfig(this);
				children.put(name, childLogger);
				childLogger.parseFrom(x);
			} else if (nodeName.equals("enabled")) {
				enabled = Boolean.parseBoolean(x.getValue().toString());
			} else if (nodeName.equals("queueSize")) {
				queueSize = Integer.parseInt(x.getValue().toString());
			} else if (nodeName.equals("writer")) {
				final String name = x.getAttribute("name").getValue().toString();
				LogWriterConfig wcfg = writersConfig.get(name);
				if (wcfg == null) {
					wcfg = new LogWriterConfig(null);
					writersConfig.put(name, wcfg);
				}
				wcfg.parseFrom(x);
			} else if (nodeName.equals("reader")) {
				readerConfig = new LogReaderConfig(x);
			}
		}
	}

	public Map<String, LogWriterConfig> getWritersConfig() {
		return writersConfig;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public LogReaderConfig getReaderConfig() {
		return readerConfig;
	}
}
