package logging.server;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;

import logging.HttpLogRecord;
import logging.HttpRoutingLogIndexEntry;
import logging.HttpTimeLogIndexEntry;
import logging.TpLogIndexEntry;
import logging.TpLogRecord;
import logging.config.LoggingConfig;
import core.text.DateUtil;
import core.util.ConfigClassFile;

public class LoggingClient {

	private static String configPath = "java/config/";
	private static boolean init;
	
	private static void init() throws Throwable {
		if (init) {
			return;
		}
		startupConfig(configPath);
		init = true;
	}
	
	public static void main(String[] args) {
		try {
			 
			final Iterator<String> iargs = Arrays.asList(args).iterator();
			
			while (iargs.hasNext()) {
				final String cmd = iargs.next();
				if (cmd.equals("-c")) {
					configPath = iargs.next();
					continue;
				} 
				init();
				if (cmd.equals("search")) {
					search(iargs);
				} else if (cmd.equals("shutdown")) {
					shutdown(iargs);
				} else if (cmd.equals("rebuild")) {
					rebuild(iargs);
				} else if (cmd.equals("delete")) {
					delete(iargs);
				}
				break;
			}
			
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	private static void rebuild(Iterator<String> iargs) throws IOException {
		final File baseDir = new File(iargs.next());
		final CmdRequestRebuildIndexes cmd = new CmdRequestRebuildIndexes(LoggingConfig.getServerHost(), LoggingConfig.getServerPort());
		cmd.setBaseDir(baseDir);
		final CmdResponse resp = cmd.send();
		System.out.println(resp.toString());
	}

	private static void delete(Iterator<String> iargs) throws Exception {
		final File baseDir = new File(iargs.next());
		final DateFormat fmt = DateUtil.getDateFormat("yyyyMMdd");
		final long threshold = fmt.parse(iargs.next()).getTime();
		final CmdRequestDeleteOld cmd = new CmdRequestDeleteOld(LoggingConfig.getServerHost(), LoggingConfig.getServerPort());
		cmd.setBaseDir(baseDir);
		cmd.setThreshold(threshold);
		final CmdResponse resp = cmd.send();
		System.out.println(resp.toString());
	}

	private static void shutdown(Iterator<String> iargs) throws IOException {
		final String password = LoggingConfig.getShutdownPassword();
		final CmdRequestShutdown cmd = new CmdRequestShutdown(LoggingConfig.getServerHost(), LoggingConfig.getServerPort());
		cmd.setShutdownPassword(password);
		final CmdResponse resp = cmd.send();
		System.out.println(resp.toString());
	}

	private static void search(Iterator<String> iargs) throws Exception {
		final String cmd = iargs.next();
		if (cmd.equals("time")) {
			searchByTime(iargs);
		} else if (cmd.equals("routing")) {
			searchByRouting(iargs);
		}
	}

	private static void searchByRouting(Iterator<String> iargs) throws Exception {
		final String loginId = iargs.next();
		final String routingId = iargs.next();
		final long t1 = parseDateTime(iargs.next());
		final long t2 = parseDateTime(iargs.next());
		final TpLogIndexEntry start = new HttpRoutingLogIndexEntry(routingId, t1);
		final TpLogIndexEntry end = new HttpRoutingLogIndexEntry(routingId, t2);
		list(start, end, loginId);
	}

	private static void searchByTime(Iterator<String> iargs) throws Exception {
		final String loginId = iargs.next();
		final long t1 = parseDateTime(iargs.next());
		final long t2 = parseDateTime(iargs.next());
		final TpLogIndexEntry start = new HttpTimeLogIndexEntry(t1);
		final TpLogIndexEntry end = new HttpTimeLogIndexEntry(t2);
		list(start, end, loginId);
	}
	
	private static void list(TpLogIndexEntry start, TpLogIndexEntry end, String loginId) throws IOException {
		CmdRequestFindRecords cmd = new CmdRequestFindRecords(LoggingConfig.getServerHost(), LoggingConfig.getServerPort());
		cmd.setLoggerId(HttpLogRecord.TYPE + "." + loginId);
		cmd.setStart(start);
		cmd.setEnd(end);
		CmdResponse resp = cmd.send();
		if (resp instanceof CmdResponseRecordList) {
			CmdResponseRecordList result = (CmdResponseRecordList) resp;
			for (TpLogRecord rec : result.getResult()) {
				System.out.println(rec.toString());
			}
			System.out.println("total " + result.getResult().size() + " records");
		}
		System.out.println(resp.toString());
	}
	
	public static void startupConfig(String configPath) throws Throwable {
		ConfigClassFile config = new ConfigClassFile(configPath, false);
		config.addConfig(LoggingConfig.class);
	}

	private static long parseDateTime(String str) throws ParseException {
		final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return fmt.parse(str).getTime();
	}
}
