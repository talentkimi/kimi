package logging.filelog;

import java.io.File;

public class IndexedLogFileUtils {
	public static final String DATE_FORMAT = "%1$tY%1$tm%1$td";

	public static String convertIdToPath(String id) {
		return id.replace('.', File.separatorChar);	
	}

	public static String getDBNameForTime(long time) {
		return String.format(IndexedLogFileUtils.DATE_FORMAT, time);
	}

}
