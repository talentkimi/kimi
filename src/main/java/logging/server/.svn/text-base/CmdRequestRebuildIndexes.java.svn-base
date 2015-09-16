package logging.server;

import java.io.File;
import java.io.IOException;

import logging.filelog.LogDatabase;

public class CmdRequestRebuildIndexes extends CmdRequest {
	private static final long serialVersionUID = 1L;
	private File baseDir;
	
	public CmdRequestRebuildIndexes(String host, int port) {
		super(host, port);
	}

	@Override
	public CmdResponse process() {
		try {
			LogDatabase.rebuild(baseDir, null);
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
}
