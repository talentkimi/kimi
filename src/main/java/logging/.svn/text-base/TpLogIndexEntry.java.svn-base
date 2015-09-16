package logging;

import java.io.Externalizable;

/**
 * This interface specified general behaviour of index entries for log records.
 * @author Dimitrijs
 *
 */
public interface TpLogIndexEntry extends Comparable<TpLogIndexEntry>, Externalizable {

	/**
	 * Returns name of index. Each subclass should return constant value.
	 * @return name of this index.
	 */
	public String getIndexName();
	
	/**
	 * Each index should be based on time.
	 * @return time of record.
	 */
	public long getTime();
}
