package logging.loadtest;

import java.util.concurrent.BlockingQueue;

import logging.HttpLogRecord;
import logging.HttpTimeLogIndexEntry;
import logging.TpLogRecord;
import logging.config.LoggingConfig;
import logging.server.CmdRequestFindRecords;
import logging.server.CmdResponse;
import logging.server.CmdResponseRecordList;

class FetchRecordsTask implements Runnable{

	private BlockingQueue<TpLogRecord> recordsToStore;
	private BlockingQueue<String> loginsToFetch;
	private long fromTimestamp;
	private long tillTimestamp;
	
	public FetchRecordsTask(long fromTimestamp, long tillTimestamp, 
			BlockingQueue<TpLogRecord> recordsToStore,
			BlockingQueue<String> loginsToFetch) {
		this.recordsToStore = recordsToStore;
		this.loginsToFetch = loginsToFetch;
		this.fromTimestamp = fromTimestamp;
		this.tillTimestamp = tillTimestamp;
	}
	
	@Override
	public void run() {
		try{
			String loginId = loginsToFetch.poll();
			while(loginId != null){
				CmdRequestFindRecords cmd = new CmdRequestFindRecords(LoggingConfig.getServerHost(), LoggingConfig.getServerPort());
				cmd.setStart(new HttpTimeLogIndexEntry(fromTimestamp));
				cmd.setEnd(new HttpTimeLogIndexEntry(tillTimestamp));
				
				cmd.setLoggerId(HttpLogRecord.TYPE + "." +loginId);
				cmd.setOffset(0);
				cmd.setLimit(100);
					
				int numberOfResultsFetched = 0;
				do{
					CmdResponse resp = cmd.send();
					CmdResponseRecordList result = (CmdResponseRecordList) resp;
					for(TpLogRecord record : result.getResult()){
						recordsToStore.put(record);
					}
					numberOfResultsFetched = result.getResult().size();
					System.out.println("Id:"+ cmd.getLoggerId() + " Resp:"+ resp + ", total " + numberOfResultsFetched + " records");
					cmd.setOffset(cmd.getOffset()+cmd.getLimit());
				} while (numberOfResultsFetched > 0);
				loginId = loginsToFetch.poll();
			}
		} catch(Exception e) {
			System.out.println("failed to complete requests fetching");
			e.printStackTrace();
		}
	}
}
