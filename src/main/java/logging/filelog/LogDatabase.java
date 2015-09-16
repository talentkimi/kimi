package logging.filelog;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import logging.StreamUtils;
import logging.TpLogIndexEntry;
import logging.TpLogRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogDatabase {
	private static final Logger logger = LoggerFactory.getLogger(LogDatabase.class); 
	private static final HashMap<File, LogDatabaseRef> registry = new HashMap<File, LogDatabaseRef>();
	private static ReferenceQueue<LogDatabase> refQueue = new ReferenceQueue<LogDatabase>();
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final Object sync = new Object();
	private final File dataFile;
	private final File baseDir;
	private final String name;
	private final IndexedLogFileIndexer indexer;
	private final RandomAccessFile data;
	private boolean closed = false;
	
	public static LogDatabase getDatabase(File baseDir, String name, boolean create) throws IOException {
		final File dataFile = new File(baseDir, name + ".log");
		lock.readLock().lock();
		try {
			LogDatabase result = get(dataFile);
			if (result == null) {
				lock.readLock().unlock();
				lock.writeLock().lock();
				try {
					result = get(dataFile);
					if (result == null) {
						cleanup();
						result = new LogDatabase(dataFile, baseDir, name, create);
						put(result);
					}
				} finally {
					lock.readLock().lock();
					lock.writeLock().unlock();
				}
			}
			return result;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	private static Iterable<DbName> collectDatabases(File baseDir, DatabaseFilter filter) {
		final List<DbName> result = new LinkedList<DbName>();
		final LinkedList<File> toProcess = new LinkedList<File>();
		toProcess.add(baseDir);
		File next;
		while ((next = toProcess.poll()) != null) {
			for (File file : next.listFiles()) {
				if (file.isDirectory()) {
					toProcess.add(file);
				} else if (DbName.isDatabase(file)) {
					final DbName n = new DbName(file);
					if (filter == null || filter.accept(n.getBaseDir(), n.getName())) {
						result.add(n);
					}
				}
			}
		}
		return result;
	}
	
	public static void rebuild(File baseDir, DatabaseFilter filter) throws IOException {
		lock.writeLock().lock();
		try {
			for (DbName n : collectDatabases(baseDir, filter)) {
				try {
					final LogDatabase db = getDatabase(n.getBaseDir(), n.getName(), false);
					db.rebuild();
				} catch (FileNotFoundException ex) {
					logger.warn("Database " + n + "not found");
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public static void delete(File baseDir, DatabaseFilter filter) throws IOException {
		lock.writeLock().lock();
		try {
			for (DbName n : collectDatabases(baseDir, filter)) {
				try {
					final LogDatabase db = getDatabase(n.getBaseDir(), n.getName(), false);
					db.delete();
					registry.remove(db.dataFile);
				} catch (FileNotFoundException ex) {
					logger.warn("Database " + n + "not found");
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static void shutdown() {
		lock.writeLock().lock();
		try {
			for (LogDatabaseRef ref : registry.values()) {
				final LogDatabase db = ref.get();
				if (db != null) {
					db.close();
				}
			}
			registry.clear();
			refQueue = new ReferenceQueue<LogDatabase>();
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public File getBaseDir() {
		return baseDir;
	}

	public String getName() {
		return name;
	}

	public File getDataFile() {
		return dataFile;
	}

	private LogDatabase(File file, File baseDir, String name, boolean create) throws IOException {
		this.dataFile = file;
		this.baseDir = baseDir;
		this.name = name;
		if (create) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} else if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		this.data = new RandomAccessFile(file, "rw");
		this.indexer = new IndexedLogFileIndexer(baseDir, name);
	}
	
	public void write(TpLogRecord record) throws IOException {
		synchronized (sync) {
			final long pos = data.length();
			data.seek(pos);
			StreamUtils.writeObject(data, record, true);
			indexer.indexRecord(record, pos);
		}
	}
	
	private TpLogRecord readRecord() throws IOException, ClassNotFoundException {
		return (TpLogRecord) StreamUtils.readObject(data);
	}
	
	/**
	 * Populates the specified list for records by range of index entries.
	 * @param result List to populate.
	 * @param start Index entry for first record in the range.
	 * @param end Index entre for last record in the range.
	 * @param offset the number of rows to skip before starting to return records.
	 * @param limit the maximum number of records to return.
	 * @return count of processed records (skipped + readed). If limit == 0 then returns {@code offset} to indicate that all records are processed.
	 * @throws IOException
	 */
	public int read(final List<TpLogRecord> result, final TpLogIndexEntry start, final TpLogIndexEntry end, final int offset, final int limit) throws IOException {
		
		if (offset < 0) {
			throw new IllegalArgumentException("offset < 0");
		}
		
		if (limit < -1) {
			throw new IllegalArgumentException("offset < -1");
		}
		
		if (limit == 0) {
			return offset;
		}
		
		synchronized (sync) {

			class Counter {
				int count = 0;
				void increase() { count++; }
			}
			
			final Counter skipped = new Counter();
			final Counter readed = new Counter();
			indexer.find(start, end, new TpLogRecordReader() {

				@Override
				public boolean read(long pos) throws IOException {
					if (skipped.count < offset) {
						skipped.increase();
						return true;
					}
					data.seek(pos);
					try {
						final TpLogRecord rec = readRecord();
						result.add(rec);
						readed.increase();
						return limit == -1 || readed.count < limit;
					} catch (ClassNotFoundException ex) {
						throw new IOException(ex);
					}
				}
			});
			return readed.count + skipped.count;
		}
	}
	
	public void rebuild() throws IOException {
		synchronized (sync) {
			indexer.clear();
			data.seek(0);
			try {
				// Loop will be finished by EOFException from readRecord()
				while (true) {
					final long pos = data.getFilePointer();
					final TpLogRecord rec = readRecord();
					indexer.indexRecord(rec, pos);
				}
			} catch (EOFException ex) {
				// ignore. Just finish the loop
			} catch (ClassNotFoundException ex) {
				throw new IOException(ex);
			}
		}
	}

	private void delete() throws IOException {
		synchronized (sync) {
			indexer.clear();
			close();
			dataFile.delete();
		}
	}
	
	private void close() {
		synchronized (sync) {
			if (closed) {
				return;
			}
			closed = true;
			try {
				indexer.close();
			} catch (Throwable t) {
				// Ignore
			}
			try {
				data.close();
			} catch (Throwable t) {
				// Ignore
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	
	private static LogDatabase get(File file) {
		final LogDatabaseRef ref = registry.get(file);
		return ref == null ? null : ref.get();
	}
	
	private static void put(LogDatabase db) {
		final LogDatabaseRef ref = new LogDatabaseRef(db);
		registry.put(db.dataFile, ref);
	}
	
	private static void cleanup() {
		LogDatabaseRef ref;
		while ((ref = (LogDatabaseRef) refQueue.poll()) != null) {
			registry.remove(ref.file);
		}
	}

	private static class LogDatabaseRef extends WeakReference<LogDatabase> {
		private File file;
		
		public LogDatabaseRef(LogDatabase referent) {
			super(referent, refQueue);
			this.file = referent.dataFile;
		}
	}
	
	public static class DbName {
		private final File baseDir;
		private final String name;
		
		public DbName(File dbFile) {
			if (!isDatabase(dbFile)) {
				throw new IllegalArgumentException(dbFile.getAbsolutePath() + " is not a database");
			}
			baseDir = extractBaseDir(dbFile);
			name = extractName(dbFile);
		}
		
		private static void checkIsDatabase(File file) {
			if (!isDatabase(file)) {
				throw new IllegalArgumentException(file + " is not a database");
			}
		}
		
		public static boolean isDatabase(File file) {
			return file.getName().endsWith(".log");
		}
		
		public static String extractName(File file) {
			checkIsDatabase(file);
			final String fileName = file.getName();
			return fileName.substring(0, fileName.length()-4);
		}
		
		public static File extractBaseDir(File file) {
			checkIsDatabase(file);
			return file.getParentFile();
		}

		public File getBaseDir() {
			return baseDir;
		}

		public String getName() {
			return name;
		}
	}
}
