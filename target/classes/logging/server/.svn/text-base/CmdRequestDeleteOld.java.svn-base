package logging.server;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;

import logging.filelog.DatabaseFilter;
import logging.filelog.LogDatabase;
import core.text.DateUtil;

public class CmdRequestDeleteOld extends CmdRequest {
	private static final long serialVersionUID = 1L;
	private File baseDir;
	private long threshold;
	
	public CmdRequestDeleteOld(String host, int port) {
		super(host, port);
	}

	@Override
	public CmdResponse process() {
		try {
			final DateFormat fmt = DateUtil.getDateFormat("yyyyMMdd");
			LogDatabase.delete(baseDir, new DatabaseFilter() {
				
				@Override
				public boolean accept(File baseDir, String name) {
					try {
						final long date = fmt.parse(name).getTime();
						return threshold >= date;
					} catch (ParseException ex) {
						throw new IllegalArgumentException(ex);
					}
				}
			});
			return null;
		} catch (IOException e) {
			return new CmdResponseFailure(e);
		}
	}

	public File getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}

	public long getThreshold() {
		return threshold;
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}
}
