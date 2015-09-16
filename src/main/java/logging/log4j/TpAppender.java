package logging.log4j;

import logging.TpLogger;
import logging.TpLoggerManager;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import core.lang.thread.ThreadPool;
import core.lang.thread.ThreadPool.Worker;
import core.nonblocking.NonBlockingRequestThreadManagerFactory;

/**
 * Log4j appender. Just redirects events of log4j to c5 logging system.
 * @author Dimitrijs
 *
 */
public class TpAppender extends AppenderSkeleton {
	
	private final ThreadLocal<Level> inAppend = new ThreadLocal<Level>();
	
	@Override
	protected void append(LoggingEvent event) {
		// Check for recursion: if this method is already called in the same thread with equals or greater log level - just exit.
		// Without this check the infinity recursion can happens.
		if (inAppend.get() != null && inAppend.get().toInt() >= event.getLevel().toInt()) {
			return;
		}
		inAppend.set(event.getLevel());
		try {
			final String taskName = resolveTaskName();
			final Log4jLogRecord record = new Log4jLogRecord(getName(), event, taskName);
			final TpLogger logger = TpLoggerManager.getLogger(record.getLoggerId());
			logger.log(record);
		} finally {
			inAppend.remove();
		}
	}

	private String resolveTaskName() {
		final Thread thread = Thread.currentThread();
		String name = null;
		Worker worker = ThreadPool.forThread(thread);
		if (worker != null) {
			name = worker.getName();
		} else {
			try{
				if (NonBlockingRequestThreadManagerFactory.getNonBlockingRequestThreadManager().containsThread(thread))
				{
					name = "c5: NIO processing thread";
				}
			}catch (Exception l_e){
				// and throw it away.
			}
		}

		if (name == null) {
			return thread.getName();
		}
		
		return name;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
}
