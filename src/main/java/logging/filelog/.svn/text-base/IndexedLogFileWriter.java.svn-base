package logging.filelog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import logging.TpLogRecord;
import logging.TpLogWriter;
import logging.TpLogger;
import logging.config.ConfigException;
import logging.config.LoggingConfig;
import core.xml.Xml;


public class IndexedLogFileWriter extends TpLogWriter {
	private final Calendar cal = Calendar.getInstance();
	private LogDatabase curDb;
	private File baseDir;
	private String logPath;
	private long minFreeSpace;
	private long lastRotate = 0;
	private long nextRotate = 0;
	private long lastCheckedFreeSpace;
	private boolean hasEnoughFreeSpace = true;
	
	public IndexedLogFileWriter() {
		logPath = LoggingConfig.getLogPath();
		minFreeSpace = LoggingConfig.getMinFreeSpace() * 1024L * 1024L;
	}
	
	@Override
	public void setLogger(TpLogger logger) {
		super.setLogger(logger);
		final String path = IndexedLogFileUtils.convertIdToPath(logger.getId());
		baseDir = new File(logPath, path);
	}

	private void rotate(long time) throws IOException {
		String newDate = IndexedLogFileUtils.getDBNameForTime(time);
		curDb = LogDatabase.getDatabase(baseDir, newDate, true);
	}

	@Override
	protected void parseLeaf(Xml xml) throws ConfigException {
		final String name = xml.getName();
		if (name.equals("path")) {
			logPath = xml.getValue().toString();
		} else {
			super.parseLeaf(xml);
		}
	}

	private boolean hasEnoughFreeSpace() {
		if (baseDir.exists() && 
				System.currentTimeMillis() - lastCheckedFreeSpace > 30000) {
			long freeSpace = baseDir.getFreeSpace();
			hasEnoughFreeSpace = minFreeSpace < freeSpace;
			lastCheckedFreeSpace = System.currentTimeMillis();
		}
		return hasEnoughFreeSpace;
	}
	
	@Override
	protected void doWrite(TpLogRecord record) {
		if (!hasEnoughFreeSpace()) {
			return;
		}
		try {
			long recTime = record.getTime();
			if (recTime < lastRotate || recTime >= nextRotate) {
				rotate(recTime);
				cal.setTimeInMillis(recTime);
				cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				lastRotate = cal.getTimeInMillis();
				cal.add(Calendar.DATE, 1);
				nextRotate = cal.getTimeInMillis();
			}
			record.maskPrivate();
			curDb.write(record);
		} catch (IOException ex) {
			logger.warn("Writing log exception", ex);
		}
	}
}
