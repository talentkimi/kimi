package logging.filelog;

import java.io.IOException;

public interface TpLogRecordReader {
	/**
	 * Reads object from specified position. 
	 * This method is used by {@link IndexedLogFileIndexer} while searching by index.
	 * @param pos position of data stored in index-file.
	 * @return true if indexer should continue searching of next record. False - if it should stop.
	 * @throws IOException
	 */
	public boolean read(long pos) throws IOException;
}
