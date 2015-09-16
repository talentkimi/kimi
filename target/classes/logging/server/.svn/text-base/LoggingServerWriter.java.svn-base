package logging.server;

import logging.TpLogRecord;
import logging.TpLogWriter;
import logging.config.ConfigException;
import logging.config.LoggingConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.xml.Xml;


public class LoggingServerWriter extends TpLogWriter {

	private static final Logger logger = LoggerFactory.getLogger(LoggingServer.class); 
	private String host;
	private int port;
	
	public LoggingServerWriter() {
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
	protected void doWrite(TpLogRecord record) {
		try {
			CmdRequestLogMessage cmd = new CmdRequestLogMessage(host, port);
			cmd.log(record);
		} catch (Throwable ex) {
			logger.warn("log record sending exception", ex);
		}
	}

}
