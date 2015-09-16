package core.util;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;


/**
 * Sync universal timeout cache that allows limitation by access timeout, insertion timeout and size.
 * Backed by LinkedHashMap
 * @author dmytro
 */
public class UniversalTimeoutCache<K, V> implements Cache<K, V>{

	private static final long serialVersionUID = -4001956959383778962L;

	private CacheImpl<K, V> cache;
	
	/**
	 * @param totalTimeout milliseconds, if greater than zero, element gets discarded after 
	 * <code>totalTimeout</code> seconds since last insertion into the cash
	 *  
	 * @throws IllegalArgumentException in case no valid limit provided(greater than 0)
	 */	
	public UniversalTimeoutCache(long totalTimeout) {
		validate(0, totalTimeout, 0);	
		cache = new CacheImpl<K,V>(0, totalTimeout, 0, false);
	}

	/**
	 * @param accessTimeout milliseconds, if greater than zero, element gets discarded after 
	 * <code>accessTimeout</code> seconds since last access
	 *  
	 * @param totalTimeout milliseconds, if greater than zero, element gets discarded after 
	 * <code>totalTimeout</code> seconds since last insertion into the cash
	 *  
	 * @param maximumSize if greater than zero puts the limit on maximum number of entities 
	 * held by the cache. If maximum size was reached new insertion removes the oldest entry  
	 * If used in conjunction with timeouts, oldest entry gets discarded even if it's not timed out yet
	 *  
	 * @throws IllegalArgumentException in case no valid limit provided(greater than 0)
	 */
	public UniversalTimeoutCache(long accessTimeout, long totalTimeout, int maximumSize) {
		validate(accessTimeout, totalTimeout, maximumSize);		
		cache = new CacheImpl<K,V>(accessTimeout, totalTimeout, maximumSize, accessTimeout > 0);
	}
	
	/**
	 * 
	 * @param accessTimeout milliseconds, if greater than zero, element gets discarded after 
	 * <code>accessTimeout</code> seconds since last access
	 *  
	 * @param totalTimeout milliseconds, if greater than zero, element gets discarded after 
	 * <code>totalTimeout</code> seconds since last insertion into the cash
	 *  
	 * @param maximumSize if greater than zero puts the limit on maximum number of entities 
	 * held by the cache. If maximum size was reached new insertion removes the oldest entry  
	 * If used in conjunction with timeouts, oldest entry gets discarded even if it's not timed out yet
	 * 
	 * @param initialCapacity @see HashMap(int, float, boolean)
	 * @param loadFactor @see HashMap(int, float, boolean)
	 * 
	 * @throws IllegalArgumentException in case no valid limit provided(greater than 0)
	 */
	public UniversalTimeoutCache(long accessTimeout, long totalTimeout, int maximumSize, int initialCapacity, int loadFactor) {
		validate(accessTimeout, totalTimeout, maximumSize);
		cache = new CacheImpl<K,V>(accessTimeout, totalTimeout, maximumSize, 
				initialCapacity, loadFactor, accessTimeout > 0);
	}
	
	private void validate(long accessTimeout, long totalTimeout, int maximumSize){
		if((totalTimeout <= 0) && (accessTimeout <= 0) && (maximumSize <= 0))
			throw new IllegalArgumentException("no limiting boundary");
	}

	@Override
	public synchronized V get(K key) {
		TimestampedWrapper<V> tw = cache.get(key);
		return (tw != null)? tw.getValue() : null;
	}
	
	@Override
	public synchronized V put(K key, V value){
		TimestampedWrapper<V> tw = cache.put(key, new TimestampedWrapper<V>(value));
		return (tw != null)? tw.getValue() : null;
	}
	
	@Override
	public synchronized void clear(){
		cache.clear();
	}
	
	@Override
	public synchronized int size(){
		return cache.size();
	}
	
	private static final class TimestampedWrapper<V>{
		private final V v;
		private final long creationTimestamp;
		private long lastAccessTimestamp;
		
		TimestampedWrapper(V v){
			creationTimestamp = System.currentTimeMillis();
			lastAccessTimestamp = creationTimestamp;
			this.v = v;
		}
		
		public V getValue(){
			return v;
		}
		
		public long getCreationTimestamp(){
			return creationTimestamp;
		}
		
		public long getLastAccessTimestamp(){
			return lastAccessTimestamp;
		}

		public void recordAccess(long now){
			lastAccessTimestamp = System.currentTimeMillis();
		}
		
		@Override
		public int hashCode() {
			return (v == null)?0:v.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;

			TimestampedWrapper<V> other = (TimestampedWrapper<V>) obj;
			if (v == null) {
				if (other.v != null)
					return false;
			} else if (!v.equals(other.v))
				return false;
			return true;
		}
	}
	
	
	private static final class CacheImpl<K, V> extends LinkedHashMap<K, TimestampedWrapper<V> >{
		private static final long serialVersionUID = 882090826834292205L;

		static final int DEFAULT_INITIAL_CAPACITY = 16;
	    static final float DEFAULT_LOAD_FACTOR = 0.75f;
	    
	    private long accessTimeout = 0; 
		private long totalTimeout = 0; 
		private int  maximumSize = 0;
		
		public CacheImpl(long accessTimeout, long totalTimeout, int maximumSize, boolean promoteOnAccess) {
			this(accessTimeout, totalTimeout, maximumSize, DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, promoteOnAccess);
		}
		
		public CacheImpl(long accessTimeout, long totalTimeout, int maximumSize, int initialCapacity, float loadFactor, boolean promoteOnAccess) {
			super(initialCapacity, initialCapacity, promoteOnAccess);
			this.accessTimeout = accessTimeout;
			this.totalTimeout = totalTimeout;
			this.maximumSize = maximumSize;
		}
		
		private boolean isExpired(TimestampedWrapper<V> tw, long time){
			return ((accessTimeout > 0) && ((time - tw.getLastAccessTimestamp()) > accessTimeout)) ||
				   ((totalTimeout > 0) && ((time - tw.getCreationTimestamp()) > totalTimeout));
		}

		@Override
		public TimestampedWrapper<V> get(Object key) {
			TimestampedWrapper<V> tw = super.get(key);

			if (tw != null) {
				long now = System.currentTimeMillis();
				if (!isExpired(tw, now)) {
					tw.recordAccess(now);
					return tw;
				} else {
					super.remove(key);
				}
			}
			return null;
		}
		
		@Override
		public TimestampedWrapper<V> put(K key, TimestampedWrapper<V> value){
			TimestampedWrapper<V> v = super.remove(key); //force entry position refresh
			super.put(key, value);
			return v;
		}
		
		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<K, TimestampedWrapper<V> > eldest) {
			if((size() > 0) && ((accessTimeout > 0)||(totalTimeout > 0))){
				//some timeout was specified
				//removing all timed out values starting from the oldest one 
				long now = System.currentTimeMillis();
				int size = size() - 1;
				int num = 0;
				
				HashSet<K> keysToRemove = new HashSet<K>();  
				for(Entry<K, TimestampedWrapper<V>> entry:entrySet()){
					//just inserted element is already in map(first one), should not iterate over it
					if(++num == size) break; 
					
					if(isExpired(entry.getValue(), now)){
						keysToRemove.add(entry.getKey());
					} else {
						break;
					}
					
				}
				keySet().removeAll(keysToRemove);
			}
			
			if((maximumSize > 0) && (size() > maximumSize)){
				remove(keySet().iterator().next());
			}
			
			return false;
		}
	}
}
