package logging;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import core.xml.Xml;

/**
 * General class for all log records. It implements Externalizable interface 
 * because it is faster and need less space in comparing with serialization.
 * @author Dimitrijs
 *
 */
public abstract class TpLogRecord implements Externalizable {
	private static final long serialVersionUID = 2L;
	
	private long time = System.currentTimeMillis();

	public TpLogRecord() {
	}
	
	public TpLogRecord(long time) {
		this.time = time;
	}
	
	/**
	 * Returns appropreated logger id. 
	 * @return
	 */
	public abstract String getLoggerId();
	
	/**
	 * This method called by some writers before storing record to file.
	 */
	public abstract void maskPrivate();

	/**
	 * Creates index entries for this record. 
	 * This method is called by writers while storing record.
	 * @return array of index entries to index this record.
	 */
	public abstract TpLogIndexEntry[] createIndexEntries();
	
	/**
	 * All log records in general must have time. 
	 * @return time in ms
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Creates a xml representation of this record.
	 * @return xml representation of this record.
	 */
	public abstract Xml toXml();
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int ver = in.readInt(); // version
		switch (ver) {
			case 2:
				time = in.readLong();
				break;
			default:
				throw new IllegalStateException("Unsupported version " + ver);
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(2); // version
		out.writeLong(time);
	}
}
