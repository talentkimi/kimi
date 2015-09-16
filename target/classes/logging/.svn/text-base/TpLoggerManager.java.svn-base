package logging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import logging.config.ConfigException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Loggers' factory class. 
 * @author Dimitrijs
 *
 */
public abstract class TpLoggerManager {
	private static final Logger log = LoggerFactory.getLogger(TpLoggerManager.class);
	private static final Map<String, TpLoggerImpl> loggers = new HashMap<String, TpLoggerImpl>();
	private static final ReentrantReadWriteLock loggersLock = new ReentrantReadWriteLock();
	private static volatile TpBufferedLogger bufferedLogger = new TpBufferedLogger(); 
	
	/**
	 * Returns appropriated logger to specified id. If logger was not created yet - creates it.
	 * If call this method before system started up it returns BufferedLogger to collect all records 
	 * which will be stored by normal logger after system started up. 
	 * @param id id of the logger.
	 * @return instance of logger appropriated to specified id.
	 */
	public static TpLogger getLogger(String id) {
		if (bufferedLogger != null) {
			return bufferedLogger;
		}
		loggersLock.readLock().lock();
		TpLoggerImpl result = null;
		try {
			result = loggers.get(id);
			if (result == null) {
				loggersLock.readLock().unlock();
				loggersLock.writeLock().lock();
				try {
					result = loggers.get(id);
					if (result == null) {
						result = new TpLoggerImpl(id);
						loggers.put(id, result);
						result.reload();
					}
				} finally {
					loggersLock.readLock().lock();
					loggersLock.writeLock().unlock();
				}
			}
		} catch (ConfigException ex) {
			log.error("Logger " + id + " instantiation error. Logger disabled.", ex);
		} finally {
			loggersLock.readLock().unlock();
		}
		return result;
	}
	
	/**
	 * This method is called by TripPlannerMain or LoggingServer to startup the logging mechanism.
	 * Method flushes bufferedLogger to send all collected, during the system startup, log-records.
	 * Should be called <b>after</b> initialisation of SystemVariables.
	 */
	public static void startup() {
		TpBufferedLogger l = bufferedLogger;
		bufferedLogger = null;
		if (l != null) {
			l.flush();
		}
	}
	
	/**
	 * Reconfigures all logger.
	 */
	public static void reloadLoggers() {
		loggersLock.writeLock().lock();
		try {
			for (TpLoggerImpl l : loggers.values()) {
				try {
					l.reload();
				} catch (ConfigException ex) {
					log.error("Logger " + l.getId() + " reloading error. Logger disabled.", ex);
				}
			}
		} finally {
			loggersLock.writeLock().unlock();
		}
	}

	/**
	 * Prepares all loggers for shutdown.
	 * @throws InterruptedException 
	 */
	public static void shutdown() throws InterruptedException {
		loggersLock.writeLock().lock();
		try {
			TpLoggerImpl.shutdown();
			loggers.clear();
		} finally {
			loggersLock.writeLock().unlock();
		}
	}
	
}
