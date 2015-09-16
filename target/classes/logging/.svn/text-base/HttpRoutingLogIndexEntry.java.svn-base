package logging;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class HttpRoutingLogIndexEntry implements TpLogIndexEntry {
	private static final long serialVersionUID = 2L;

	private String routingId;
	private long time;
	
	public HttpRoutingLogIndexEntry() {
	}

	public HttpRoutingLogIndexEntry(String routingId, long time) {
		this.time = time;
		this.routingId = routingId;
	}

	public HttpRoutingLogIndexEntry(HttpLogRecord record) {
		this(record.getRoutingId(), record.getTime());
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public String getIndexName() {
		return "routing";
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		in.readInt(); // version
		routingId = (String) in.readObject();
		time = in.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(2); // version
		out.writeObject(routingId);
		out.writeLong(time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final HttpRoutingLogIndexEntry other = (HttpRoutingLogIndexEntry) obj;
		if (routingId == null && other.routingId == null || (routingId != null && routingId.equals(other.routingId))) {
			return time == other.time;
		}
		return false;
	}

	private int signum(long val) {
		if (val < 0) {
			return -1;
		} else if (val > 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public int compareTo(TpLogIndexEntry obj) {
		if (getClass() != obj.getClass()) {
			throw new IllegalArgumentException();
		}

		final HttpRoutingLogIndexEntry other = (HttpRoutingLogIndexEntry) obj;
		
		if (routingId != null && other.routingId != null) {
			int result = routingId.compareTo(other.routingId);
			if (result == 0) {
				return signum(time - other.time);
			} else {
				return result;
			}
		} else if (!(routingId == null ^ other.routingId == null)) {
			return signum(time - other.time);
		} else if (routingId == null) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((routingId == null) ? 0 : routingId.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}
}
