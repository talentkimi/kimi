package logging.server;

import java.util.LinkedList;
import java.util.List;

import logging.TpLogRecord;

public class CmdResponseRecordList extends CmdResponseOk {
	private static final long serialVersionUID = 1L;
	
	private final List<TpLogRecord> result;

	public CmdResponseRecordList() {
		result = new LinkedList<TpLogRecord>();
	}
	
	public CmdResponseRecordList(List<TpLogRecord> result) {
		this.result = result;
	}
	
	public List<TpLogRecord> getResult() {
		return result;
	}

	public void add(TpLogRecord record) {
		result.add(record);
	}
	
} 
