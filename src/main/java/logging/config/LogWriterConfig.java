package logging.config;

import java.util.Iterator;
import java.util.LinkedList;

import logging.TpLogWriter;
import logging.TpLogger;

import core.xml.Xml;
import core.xml.XmlException;

public class LogWriterConfig {
	private String writerClass;
	private LogWriterConfig parent;
	private Xml xml;
	
	public LogWriterConfig(LogWriterConfig parentWriter) {
		this.parent = parentWriter;
		if (parentWriter != null) {
			this.writerClass = parentWriter.writerClass;
		}
	}
	
	public void parseFrom(Xml xml) throws XmlException {
		this.xml = xml;
		final Object classValue = xml.getAttribute("class").getValue();
		if (classValue != null) { 
			writerClass =  classValue.toString().trim();
		}
	}
	
	public TpLogWriter createWriter(TpLogger logger) throws ConfigException  {
		if (writerClass == null || writerClass.isEmpty()) {
			return null;
		}
		try {
			final Class<? extends TpLogWriter> cls = Class.forName(writerClass).asSubclass(TpLogWriter.class);
			final TpLogWriter inst = cls.newInstance();
			final LinkedList<LogWriterConfig> inheritance = new LinkedList<LogWriterConfig>();
			inst.setLogger(logger);
			LogWriterConfig cur = this;
			do {
				inheritance.add(cur);
				cur = cur.parent;
			} while (cur != null);
			
			final Iterator<LogWriterConfig> inhIter = inheritance.descendingIterator();
			while (inhIter.hasNext()) {
				final LogWriterConfig cfg = inhIter.next();
				inst.parse(cfg.xml);
			}
			return inst;
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(ex);
		} catch (InstantiationException ex) {
			throw new IllegalStateException(ex);
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException(ex);
		} 
	}

	public Xml getXml() {
		return xml;
	}

}
