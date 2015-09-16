package logging.server;

import java.util.List;

import logging.TpLogIndexEntry;
import logging.TpLogReader;
import logging.TpLogRecord;
import logging.TpLogger;
import logging.TpLoggerManager;

public class CmdRequestFindRecords extends CmdRequest {
	private static final long serialVersionUID = 1L;

	private TpLogIndexEntry start;
	private TpLogIndexEntry end;
	private String loggerId;
	private int offset = 0;
	private int limit = -1;
	
	public CmdRequestFindRecords(String host, int port) {
		super(host, port);
	}

	public TpLogIndexEntry getStart() {
		return start;
	}

	public void setStart(TpLogIndexEntry start) {
		this.start = start;
	}

	public TpLogIndexEntry getEnd() {
		return end;
	}

	public void setEnd(TpLogIndexEntry end) {
		this.end = end;
	}

	public String getLoggerId() {
		return loggerId;
	}

	public void setLoggerId(String loggerId) {
		this.loggerId = loggerId;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public CmdResponse process() {
		final TpLogger logger = TpLoggerManager.getLogger(loggerId);
		final TpLogReader reader = logger.getReader();
		final List<TpLogRecord> list = reader.getRecords(start, end, offset, limit);
		return new CmdResponseRecordList(list);
	}

}
