package logging.filter;

import java.text.ParseException;

import logging.TpLogRecord;
import logging.config.ConfigException;

import core.util.UtilDate;
import core.xml.Xml;

public class TpIntervalLogFilter extends TpLogFilter {
	private long from;
	private long to;

	public TpIntervalLogFilter() {
	}
	
	public TpIntervalLogFilter(long from, long to) {
		this.from = from;
		this.to = to;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}

	@Override
	public boolean accept(TpLogRecord record) {
		final long time = record.getTime();
		return from <= time && time <= to;
	}

	@Override
	protected void parseLeaf(final Xml xml) throws ConfigException {
		final String name = xml.getName();
		try {
			if (name.equals("from")) {
				final UtilDate d = new UtilDate(xml.getValue().toString());
				from = d.getTimeInMillis();
			} else if (name.equals("to")) {
				final UtilDate d = new UtilDate(xml.getValue().toString());
				to = d.getTimeInMillis();
			} else {
				super.parseLeaf(xml);
			}
		} catch (ParseException ex) {
			throw new ConfigException(ex);
		}
	}
	
	

}
