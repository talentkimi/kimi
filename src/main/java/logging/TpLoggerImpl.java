package logging;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import logging.config.ConfigException;
import logging.config.FilterConfig;
import logging.config.LogReaderConfig;
import logging.config.LogWriterConfig;
import logging.config.LoggerConfig;
import logging.config.LoggingConfig;
import logging.filter.TpLogFilter;

/**
 * One of logger implementations. 
 * Now we have two logger implementations: {@link TpLoggerImpl} and {@TpBufferedLogger}.
 * The first one is used for normal logging work.
 * The second one just for buffering records while the system not started up.
 * After the system is started the buffered records goes to normal loggers. 
 * @author Dimitrijs
 */
public class TpLoggerImpl extends TpLogger {
	/** 
	 * How often "global enabled" should be refreshed from system variables.
	 * @see TpLogger#isGlobalEnabled()
	 */
	public static final long GLOBAL_ENABLED_TIMOUT = 1000;
	/** Name of the system variable to be used for the "global enabled" flag. */
	public static final String VAR_LOGGING_ENABLED = "logging.enabled";
	private static final ExecutorService executor = Executors.newCachedThreadPool();
	private static volatile long globalEnabledLastChecked;
	private static volatile boolean globalEnabled = true;
	private final LogQueue logQueue = new LogQueue();
	private final List<TpLogWriter> writers = new LinkedList<TpLogWriter>();
	private final Object writersLock = new Object();
	private final String id;
	private final ReentrantReadWriteLock logLock = new ReentrantReadWriteLock();
	private volatile boolean enabled = false;
	private volatile TpLogFilter filter;
	private volatile TpLogReader reader;
	
	/**
	 * Returns true if logging is enabled globally.
	 * This method uses {@link SystemVariable}, 
	 * but for better performance caches it for {@link #GLOBAL_ENABLED_TIMEOUT} milliseconds.
	 * So database is not asked for variables too often.
	 * If the system variable is not set then we assume "enabled" by default.  
	 * @return true if logging enabled globally, false otherwise.
	 * @see SystemVariable
	 * @see SystemVariableManager
	 * @see TpLogger#VAR_LOGGING_ENABLED
	 * @see TpLogger#GLOBAL_ENABLED_TIMOUT
	 */
	public static boolean isGlobalEnabled() {
		if (LoggingConfig.isIgnoreSystemVariables()) {
			return true;
		}
		if (System.currentTimeMillis() - globalEnabledLastChecked > GLOBAL_ENABLED_TIMOUT) {
			globalEnabledLastChecked = System.currentTimeMillis();
			// I use separate thread to update system var "global enabled" 
			// due to getSystemVariables uses our logging mechanism implicitly
			// and if call system variables in the same thread it can cause
			// deadlock.
			executor.execute(new Runnable() {
				public void run() {
//					Boolean var = SystemVariableManager.getInstance().getVariableAsBoolean(VAR_LOGGING_ENABLED);
//					globalEnabled = var == null || var;
				}
			});
		}
		return globalEnabled;
	}
	
	/**
	 * Creates new instance of TpLogger with specified id;
	 * @param id this logger's id.
	 */
	public TpLoggerImpl(String id) {
		this.id = id;
	}
	
	/**
	 * Loads configuration.
	 * @throws ConfigException
	 */
	public void reload() throws ConfigException {
		enabled = false;
		logLock.writeLock().lock();
		try {
			synchronized (writersLock) {
				writers.clear();
				final LoggerConfig cfg = LoggingConfig.getRoot().findByPath(id);
				final FilterConfig fcfg = cfg.getFilterConfig(); 
				filter = fcfg == null ? null : fcfg.createFilter();
				final LogReaderConfig rcfg = cfg.getReaderConfig(); 
				reader = rcfg == null ? null : rcfg.createReader(this);
				for (LogWriterConfig w : cfg.getWritersConfig().values()) {
					final TpLogWriter writer = w.createWriter(this);
					writers.add(writer);
				}
				enabled = cfg.isEnabled();
			}
		} finally {
			logLock.writeLock().unlock();
		}
	}
	
	/**
	 * Adds record to queue to process later by another thread. 
	 * Record added only if this logger is enabled and record passed by filter
	 * of this logger. 
	 * @param record
	 */
	@Override
	public void log(TpLogRecord record) {
		if (enabled && isLoggable(record)) {
			logQueue.put(record);
		}
	}

	/**
	 * Returns reader for this logger. Reader is defined in config-file.
	 * @return
	 */
	@Override
	public TpLogReader getReader() {
		return reader;
	}
	
	@Override
	public String getId() {
		return id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public TpLogFilter getFilter() {
		return filter;
	}

	public void setFilter(TpLogFilter filter) {
		this.filter = filter;
	}

	/**
	 * Checks if record is loggable.
	 * @param record
	 * @return
	 */
	public boolean isLoggable(TpLogRecord record) {
		if (enabled && isGlobalEnabled()) {
			return filter == null || filter.accept(record);
		}
		return false;
	}

	/**
	 * Shuts all loggers down. 
	 * @throws InterruptedException
	 */
	static void shutdown() throws InterruptedException {
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);
	}
	
	private class LogQueue implements Runnable {
		private LinkedBlockingQueue<TpLogRecord> queue = new LinkedBlockingQueue<TpLogRecord>(1000);
		private volatile boolean isRunning;
		
		public void put(TpLogRecord record) {
			logLock.readLock().lock();
			try {
				if (queue.offer(record)) {
					if (!isRunning) {
						logLock.readLock().unlock();
						logLock.writeLock().lock();
						try {
							if (!isRunning) {
								isRunning = true;
								executor.execute(this);
							}
						} finally {
							logLock.readLock().lock();
							logLock.writeLock().unlock();
						}
					}
				}
			} finally {
				logLock.readLock().unlock();
			}
		}
		
		@Override
		public void run() {
			TpLogRecord record;
			while ((record = poll()) != null) {
				synchronized (writersLock) {
					for (TpLogWriter w : writers) {
						w.write(record);
					}
				}
			}
		}
		
		private TpLogRecord poll() {
			logLock.readLock().lock();
			try {
				TpLogRecord result = queue.poll(500, TimeUnit.MILLISECONDS);
				if (result == null) {
					logLock.readLock().unlock();
					logLock.writeLock().lock();
					try {
						result = queue.poll();
						if (result == null) {
							isRunning = false;
						}
					} finally {
						logLock.readLock().lock();
						logLock.writeLock().unlock();
					}
				}
				return result;
			} catch (InterruptedException ex) {
				isRunning = false;
				return null;
			} finally {
				logLock.readLock().unlock();
			}
		}
	}
}
