package logging.server;

import java.io.IOException;

import logging.TpLogRecord;
import logging.TpLogger;
import logging.TpLoggerManager;


/**
 * Command to send log record to remote logging server. 
 * Instances of this class is not thread-safe.
 * @author Dimitrijs
 */
public class CmdRequestLogMessage extends CmdRequest {
	private static final long serialVersionUID = 1L;

	private TpLogRecord record;

	public CmdRequestLogMessage(String host, int port) {
		super(host, port);
	}

	public void log(TpLogRecord record) throws IOException {
		this.record = record;
		final CmdResponse result = send();
		if (result instanceof CmdResponseFailure) {
			final CmdResponseFailure failure = (CmdResponseFailure) result;
			throw new IOException(failure.getMessage(), failure.getException());
		}
	}
	
	@Override
	public CmdResponse process() {
		final String id = record.getLoggerId();
		final TpLogger lgr = TpLoggerManager.getLogger(id);
		lgr.log(record);
		return null;
	}

}
