package logging;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class HttpTimeLogIndexEntry implements TpLogIndexEntry {
	private static final long serialVersionUID = 2L;

	private long time;
	
	public HttpTimeLogIndexEntry() {
		
	}
	
	public HttpTimeLogIndexEntry(HttpLogRecord record) {
		this(record.getTime());
	}
	
	public HttpTimeLogIndexEntry(long time) {
		this.time = time;
	}

	@Override
	public long getTime() {
		return time;
	}
	
	@Override
	public String getIndexName() {
		return "time";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpTimeLogIndexEntry other = (HttpTimeLogIndexEntry) obj;
		if (time != other.time)
			return false;
		return true;
	}

	@Override
	public int compareTo(TpLogIndexEntry o) {
		if (o.getClass() != this.getClass()) {
			throw new IllegalArgumentException();
		}
		final HttpTimeLogIndexEntry other = (HttpTimeLogIndexEntry) o;
		if (this.time > other.time) {
			return 1;
		} else if (this.time < other.time) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		in.readInt(); // version
		time = in.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(2); // version
		out.writeLong(time);
	}

}
