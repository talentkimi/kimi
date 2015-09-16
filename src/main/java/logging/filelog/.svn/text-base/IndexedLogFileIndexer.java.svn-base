package logging.filelog;

import java.io.EOFException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;

import logging.StreamUtils;
import logging.TpLogIndexEntry;
import logging.TpLogRecord;

public class IndexedLogFileIndexer {
	private static final int CHAIN_SIZE = 16;
	private final HashMap<String, IndexFile> indexes = new HashMap<String, IndexFile>();
	private final File baseDir;
	private final String dbName;
	
	public IndexedLogFileIndexer(File baseDir, String dbName) throws IOException {
		this.baseDir = baseDir;
		this.dbName = dbName;
	}
	
	public void indexRecord(TpLogRecord record, long pos) throws IOException {
		for (TpLogIndexEntry e : record.createIndexEntries()) {
			final IndexFile idxFile = getIndexFile(e.getIndexName());
			idxFile.index(e, pos);
		}
	}
	
	/**
	 * Finds apropriate index and traverses through all index entries in specified range.
	 * @param start
	 * @param end
	 * @param reader
	 * @return false if traversing is stopped by reader.
	 * @throws IOException
	 */
	public boolean find(TpLogIndexEntry start, TpLogIndexEntry end, TpLogRecordReader reader) throws IOException {
		final IndexFile idxFile = getIndexFile(start.getIndexName());
		return idxFile.find(start, end, reader);
	}

	public void clear() throws IOException {
		final String prefix = dbName + ".";
		final File[] idxFiles = baseDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(prefix) && (name.endsWith(".idx") || name.endsWith(".rat"));
			}
		});
		close();
		for (File f : idxFiles) {
			f.delete();
		}
	}

	public void close() throws IOException {
		for (IndexFile idx : indexes.values()) {
			idx.close();
		}
		indexes.clear();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		try {
			close();
		} catch (Throwable ex) {
			// ignore
		}
	}

	private IndexFile getIndexFile(String name) throws IOException {
		IndexFile result = indexes.get(name);
		if (result == null) {
			result = new IndexFile(name);
			indexes.put(name, result);
		}
		return result;
	}

	private class IndexFile {
		private final File idxFile;
		private final File ratFile;
		private RandomAccessFile idxData;
		private RandomAccessFile ratData;
		
		public IndexFile(String name) throws IOException {
			idxFile = new File(baseDir, dbName + "." + name + ".idx");
			ratFile = new File(baseDir, dbName + "." + name + ".rat");
		}
		
		private void checkFiles(boolean create) throws IOException {
			if (create) {
				idxFile.createNewFile();
				ratFile.createNewFile();
			}
			if (idxData == null) {
				idxData = new RandomAccessFile(idxFile, "rw");
				ratData = new RandomAccessFile(ratFile, "rw");
			}
		}
		
		public void index(TpLogIndexEntry entry, long dataPos) throws IOException {
			checkFiles(true);
			final long idxPos = idxData.length();
			idxData.seek(idxPos);
			StreamUtils.writeObject(idxData, entry, false);
			final Chain chain = find(entry);
			chain.insert(idxPos, dataPos);
		}
		
		public boolean find(TpLogIndexEntry start, TpLogIndexEntry end, TpLogRecordReader reader) throws IOException {
			checkFiles(false);
			final Chain chain = find(start);
			try {
				while (chain.read() && chain.getIndexEntry().compareTo(end) <= 0) {
					if (!reader.read(chain.datPos)) {
						return false;
					}
				}
				return true;
			} catch (ClassNotFoundException ex) {
				throw new IOException(ex);
			}
		}
		
		public Chain find(TpLogIndexEntry entry) throws IOException {
			try {
				final Chain result = new Chain();
				if (ratData.length() == 0) {
					return result;
				}
				result.readLast();
				int first = 0;
				int last = result.position-1; 
				TpLogIndexEntry check = result.getIndexEntry();
				if (check.compareTo(entry) < 0) {
					return result;
				}
				result.readFirst();
				check = result.getIndexEntry();
				if (check.compareTo(entry) >= 0) {
					result.seek(0);
					return result;
				}
				
				while (true) {
					final int len = last - first;
					final int mid = first + len / 2 + len % 2;
					result.read(mid);
					check = result.getIndexEntry();
					
					if (check.compareTo(entry) >= 0) {
						result.read(mid-1);
						check = result.getIndexEntry();
						if (check.compareTo(entry) < 0) {
							return result;
						}
						last = mid;
					} else {
						first = mid;
					}
				}
			} catch (ClassNotFoundException ex) {
				throw new IOException(ex); 
			}
		}
		
		public void close() throws IOException {
			if (idxData != null) {
				idxData.close();
			}
			if (ratData != null) {
				ratData.close();
			}
		}
		
		private class Chain {
			private final byte[] chainArray = new byte[CHAIN_SIZE];
			private final ByteBuffer chainBuffer = ByteBuffer.wrap(chainArray);
			private long idxPos;
			private long datPos;
			private int position;
			
			public void seek(int position) throws IOException {
				this.position = position;
			}
			
			public void readLast() throws IOException {
				final int size = (int)(ratData.length() / CHAIN_SIZE);
				seek(size - 1);
				read();
			}
			
			public void readFirst() throws IOException {
				seek(0);
				read();
			}
			
			public void read(int pos) throws IOException {
				seek(pos);
				read();
			}

			public void write(long idxPos, long datPos) throws IOException {
				chainBuffer.clear();
				chainBuffer.putLong(idxPos);
				chainBuffer.putLong(datPos);
				ratData.seek(position * CHAIN_SIZE);
				ratData.write(chainArray);
				this.idxPos = idxPos;
				this.datPos = datPos;
				position++;
			}

			public void insert(long idxPos, long datPos) throws IOException {
				final long oldPos = position * CHAIN_SIZE;
				final long oldLen = ratData.length();
				if (oldPos < oldLen) {
					final byte buf[] = new byte[20480];
					final long amount = oldLen - oldPos;
					long moved = 0;
					while (moved < amount) {
						final int toMove = Math.min((int)(amount - moved), buf.length);
						final long movePos = oldLen - (long)toMove - moved;
						ratData.seek(movePos);
						ratData.readFully(buf, 0, toMove);
						ratData.seek(movePos + CHAIN_SIZE);
						ratData.write(buf, 0, toMove);
						moved += toMove;
					}
				}
				write(idxPos, datPos);
			}
			
			public TpLogIndexEntry getIndexEntry() throws IOException, ClassNotFoundException {
				idxData.seek(idxPos);
				return (TpLogIndexEntry) StreamUtils.readObject(idxData);
			}
			
			public boolean read() throws IOException {
				try {
					ratData.seek(position * CHAIN_SIZE);
					ratData.readFully(chainArray);
					chainBuffer.clear();
					this.idxPos = chainBuffer.getLong();
					this.datPos = chainBuffer.getLong();
					position++;
					return true;
				} catch (EOFException ex) {
					return false;
				}
			}
		}
	}

}
