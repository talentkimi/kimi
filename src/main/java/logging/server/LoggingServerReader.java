package logging.server;

import java.io.IOException;
import java.util.List;

import core.xml.Xml;

import logging.TpLogIndexEntry;
import logging.TpLogReader;
import logging.TpLogRecord;
import logging.config.ConfigException;
import logging.config.LoggingConfig;

public class LoggingServerReader extends TpLogReader {

	private String host;
	private int port;
	
	public LoggingServerReader() {
		host = LoggingConfig.getServerHost();
		port = LoggingConfig.getServerPort();
	}
	
	@Override
	protected void parseLeaf(Xml xml) throws ConfigException {
		final String name = xml.getName();
		if (name.equals("host")) {
			host = xml.getValue().toString();
		} else if (name.equals("port")) {
			port = Integer.parseInt(xml.getValue().toString());
		} else {
			super.parseLeaf(xml);
		}
	}

	@Override
	public List<TpLogRecord> getRecords(TpLogIndexEntry start, TpLogIndexEntry end, int offset, int limit) {
		try {
			final CmdRequestFindRecords cmd = new CmdRequestFindRecords(host, port);
			cmd.setLoggerId(getLogger().getId());
			cmd.setStart(start);
			cmd.setEnd(end);
			cmd.setOffset(offset);
			cmd.setLimit(limit);
			final CmdResponse resp = cmd.send();
			if (resp instanceof CmdResponseRecordList) {
				final CmdResponseRecordList list = (CmdResponseRecordList) resp;
				return list.getResult();
			} else if (resp instanceof CmdResponseFailure) {
				final CmdResponseFailure failure = (CmdResponseFailure) resp;
				throw new RuntimeException(failure.getMessage(), failure.getException());
			} else {
				throw new ClassCastException(resp.getClass().toString());
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public List<String> getExistsLoggers() {
		try {
			final CmdRequestGetExistsLoggers cmd = new CmdRequestGetExistsLoggers(host, port);
			cmd.setBaseLoggerId(getLogger().getId());
			final CmdResponse resp = cmd.send();
			if (resp instanceof CmdResponseLoggersList) {
				final CmdResponseLoggersList list = (CmdResponseLoggersList) resp;
				return list.getResult();
			} else if (resp instanceof CmdResponseFailure) {
				final CmdResponseFailure failure = (CmdResponseFailure) resp;
				throw new RuntimeException(failure.getMessage(), failure.getException());
			} else {
				throw new ClassCastException(resp.getClass().toString());
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
