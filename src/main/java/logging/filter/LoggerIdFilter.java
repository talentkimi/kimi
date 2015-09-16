package logging.filter;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import core.xml.Xml;

import logging.TpLogRecord;
import logging.config.ConfigException;

public class LoggerIdFilter extends TpLogFilter {

	private final Set<String> enabledIds = new HashSet<String>();
	private long lastUpdated;
	private String systemVariable = "logging.filter.loggerIdFilter";
	
	@Override
	public boolean accept(TpLogRecord record) {
		if (System.currentTimeMillis() - lastUpdated > 90000) {
			update();
		}
		if (enabledIds.isEmpty()) {
			return true;
		} else {
			return enabledIds.contains(record.getLoggerId());
		}
	}
	
	private void update() {
		enabledIds.clear();
		String str = null;
//		String str = TripPlanner.getTripPlanner().getSystemVariableManager().getVariableAsString(systemVariable);
		StringTokenizer t = new StringTokenizer(str, ", \r\n\t");
		while (t.hasMoreTokens()) {
			String id = t.nextToken();
			if (id.equals("*")) {
				enabledIds.clear();
				break;
			}
			enabledIds.add(id);
		}
		lastUpdated = System.currentTimeMillis();
	}

	@Override
	protected void parseLeaf(Xml xml) throws ConfigException {
		if (xml.getName().equals("systemVariable")) {
			systemVariable = xml.getValue().toString();
		}
	}

	
}
