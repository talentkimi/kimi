package logging.server;

import java.util.List;

import logging.TpLogReader;
import logging.TpLogger;
import logging.TpLoggerManager;

/**
 * Command is used for requesting server to retrieve ids of existed loggers. 
 * @author Dimitrijs
 */
public class CmdRequestGetExistsLoggers extends CmdRequest {
	private static final long serialVersionUID = 1L;
	private String baseLoggerId;
	
	public CmdRequestGetExistsLoggers(String host, int port) {
		super(host, port);
	}

	public String getBaseLoggerId() {
		return baseLoggerId;
	}

	public void setBaseLoggerId(String baseLoggerId) {
		this.baseLoggerId = baseLoggerId;
	}

	@Override
	public CmdResponse process() {
		final TpLogger logger = TpLoggerManager.getLogger(baseLoggerId);
		final TpLogReader reader = logger.getReader();
		final List<String> list = reader.getExistsLoggers();
		return new CmdResponseLoggersList(list);
	}

}
