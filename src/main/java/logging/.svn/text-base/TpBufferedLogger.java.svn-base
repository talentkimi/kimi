package logging;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * One of logger implementations. 
 * Now we have two logger implementations: {@link TpLoggerImpl} and {@TpBufferedLogger}.
 * The first one is used for normal logging work.
 * The second one just for buffering records while the system not started up.
 * After the system is started the buffered records goes to normal loggers. 
 * @author Dimitrijs
 */
public class TpBufferedLogger extends TpLogger {

	private final LinkedBlockingQueue<TpLogRecord> records = new LinkedBlockingQueue<TpLogRecord>(5000);
	
	@Override
	public String getId() {
		return null;
	}

	@Override
	public TpLogReader getReader() {
		return null;
	}

	@Override
	public void log(TpLogRecord record) {
		records.offer(record);
	}
	
	public void flush() {
		final Thread t = new Thread() {

			@Override
			public void run() {
				TpLogRecord record;
				while ((record = records.poll()) != null) {
					try {
						final String id = record.getLoggerId();
						TpLoggerManager.getLogger(id).log(record);
					} catch (Error er) {
						// exit from loop because Error signals about serious problems.
						records.clear();
						break;
					} catch (Throwable ex) {
						// Just ignore any other exceptions.
					}
				}
			}
		};
		t.start();
	}
}
