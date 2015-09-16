package logging.filelog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import logging.TpLogIndexEntry;
import logging.TpLogReader;
import logging.TpLogRecord;
import logging.TpLogger;
import logging.config.ConfigException;
import logging.config.LoggingConfig;
import core.xml.Xml;

public class IndexedLogFileReader extends TpLogReader {
	
	private File baseDir;
	private String logPath;

	public IndexedLogFileReader() {
		logPath = LoggingConfig.getLogPath();
	}

	@Override
	public void setLogger(TpLogger logger) {
		super.setLogger(logger);
		final String path = IndexedLogFileUtils.convertIdToPath(logger.getId());
		baseDir = new File(logPath, path);
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

	@Override
	public List<TpLogRecord> getRecords(TpLogIndexEntry start, TpLogIndexEntry end, int offset, int limit) {
		final List<TpLogRecord> result = new LinkedList<TpLogRecord>();
		if (limit != 0) {
			try {
				final Calendar startCal = Calendar.getInstance();
				startCal.setTimeInMillis(start.getTime());
				final Calendar endCal = Calendar.getInstance();
				endCal.setTimeInMillis(end.getTime());
				while (startCal.before(endCal)) {
					final long time = startCal.getTimeInMillis();
					final String name = IndexedLogFileUtils.getDBNameForTime(time);
					try {
						final LogDatabase db = LogDatabase.getDatabase(baseDir, name, false);
						final int processed = db.read(result, start, end, offset, limit);
						if ((offset -= processed) < 0) {
							if (limit != -1 && (limit += offset) <= 0) {
								break;
							}
							offset = 0;
						}
					} catch (FileNotFoundException ex) {
						// Ignore. This exception is thrown when database not exists.
					}
					startCal.add(Calendar.DATE, 1);
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		return result;
	}

	@Override
	public List<String> getExistsLoggers() {
		final List<String> result = new LinkedList<String>();
		for (File dir: baseDir.listFiles()) {
			if (!dir.isDirectory()) {
				continue;
			}
			for (File db: dir.listFiles()) {
				if (db.isFile() && LogDatabase.DbName.isDatabase(db)) {
					result.add(dir.getName());
					break;
				}
			}
		}
		return result;
	}

}
