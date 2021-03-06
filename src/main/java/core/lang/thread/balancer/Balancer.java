package core.lang.thread.balancer;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import core.lang.thread.balancer.BalancedCategory.TaskInfo;
import core.lang.thread.balancer.BalancedCategory.TaskInfoRef;

/**
 * Balancer holds a number of {@link BalancedCategory}-instances 
 * and balancing tasks of each category.  
 * <pre>
 * Balancer balancer = new Balancer("someName");
 * ...
 * ...
 * BalancedCategory category = balancer.getCategory("CategoryName");
 * BalancedCategory.TaskInfo taskInfo;
 * try {
 *     taskInfo = category.startNewTask();
 *     // do task's job
 * } catch (RejectedExecutionException ex) {
 *     // we can't execute this task due to no free slots in the balancer for this category
 * } finally {
 *     if (taskInfo != null) {
 *         taskInfo.taskFinished();
 *     }
 * }
 * </pre>
 * @author Dimitrijs
 *
 */
public class Balancer {
	private static final List<Reference<Balancer>> allBalancers = Collections.synchronizedList(new ArrayList<Reference<Balancer>>());
	final ReferenceQueue<TaskInfo> refQueue = new ReferenceQueue<TaskInfo>();
	private final Map<String, BalancedCategory> categories = new HashMap<String, BalancedCategory>();
	private final ReentrantReadWriteLock catLock = new ReentrantReadWriteLock();
	private final String limitSysVar;
	private final String enabledSysVar;
	private final String warningThresholdSysVar;
	private final String releaseThresholdSysVar;
	
	private volatile int threadsThreshold;
	private volatile long lastSysVarUpdated;
	private volatile boolean enabled = true;
	private volatile int warningThreshold;
	private volatile int releaseThreshold;

	final String name;
	final boolean ignorePopularity;
	final boolean ignoreSpeed;
	final boolean shareUnused;
	
	float activeCategoriesWeight;
	int activeCategoriesCount;
	float popularitySum;
	int threadCount;
	long lastCleanedUp;
	
	public static List<Balancer> getAllBalancers() {
		synchronized (allBalancers) {
			List<Balancer> result = new LinkedList<Balancer>();
			Iterator<Reference<Balancer>> i = allBalancers.iterator();
			while (i.hasNext()) {
				Reference<Balancer> r = i.next();
				Balancer b = r.get();
				if (b == null) {
					i.remove();
				} else {
					result.add(b);
				}
			}
			return result;
		}
	}

	/**
	 * Creates new balancer with specified name. 
	 * Same as {@link #Balancer(String, boolean, boolean) Balancer(name, false, false, true)}
	 * @param name name of new balancer
	 */
	public Balancer(String name) {
		this(name, false, false, true);
	}
	
	/**
	 * Creates new balancer with specified name.
	 * @param name name of new balancer
	 * @param ignorePopularity if true popularity factor should be ignored calculating weight.
	 * @param ignoreSpeed if true speed factor should be ignored calculating weight.
	 * @param shareUnused unused slots can be or not shared between active tasks. 
	 */
	public Balancer(String name, boolean ignorePopularity, boolean ignoreSpeed, boolean shareUnused) {
		this.name = name;
		String sysVarPrefix = "balancer." + name;
		limitSysVar = sysVarPrefix + ".threshold";
		enabledSysVar = sysVarPrefix + ".enabled";
		warningThresholdSysVar = sysVarPrefix + ".warningThreshold";
		releaseThresholdSysVar = sysVarPrefix + ".releaseThreshold";
		this.ignorePopularity = ignorePopularity;
		this.ignoreSpeed = ignoreSpeed;
		this.shareUnused = shareUnused;
		allBalancers.add(new WeakReference<Balancer>(this));
	}

	private void updateSysVar() {
		long cur = System.currentTimeMillis();
		if (cur - lastSysVarUpdated < 5000) {
			return;
		}
		lastSysVarUpdated = cur;
//		SystemVariableManager mgr = SystemVariableManager.getInstance(); 
//		threadsThreshold = mgr.getVariableAsInteger(limitSysVar);
//		enabled = mgr.getVariableAsBoolean(enabledSysVar);
//		releaseThreshold = mgr.getVariableAsInteger(releaseThresholdSysVar);
//		warningThreshold = mgr.getVariableAsInteger(warningThresholdSysVar);
	}
	
	/**
	 * Gets category from this balancer. 
	 * Each balancer can manage as many categories as needed.
	 * This method searches for existed category and if not found - creates new one.
	 * All categories created from this balancer belongs to this balancer only.
	 * @param name name of required category.
	 * @return existed or newly created BalancerCategory.
	 */
	public BalancedCategory getCategory(String name) {
		catLock.readLock().lock();
		try {
			BalancedCategory result = categories.get(name);
			if (result == null) {
				catLock.readLock().unlock();
				catLock.writeLock().lock();
				try {
					result = categories.get(name);
					if (result == null) {
						result = new BalancedCategory(name, this);
						categories.put(name, result);
					}
				} finally {
					catLock.readLock().lock();
					catLock.writeLock().unlock();
				}
			}
			return result;
		} finally {
			catLock.readLock().unlock();
		}
	}
	
	void cleanUp() {
		long cur = System.currentTimeMillis(); 
		if (cur - lastCleanedUp < 300) {
			return;
		}
		lastCleanedUp = cur;
		
		TaskInfoRef r;
		while ((r = (TaskInfoRef) refQueue.poll()) != null) {
			r.cleanup();
		}
	}

	/**
	 * Gets threshold of this balancer from system variables. 
	 * If number of tasks running under this balancer exceeds this value, new tasks should be rejected.
	 * Name of system variable is <code>balancer.<i>balancer&nbsp;name</i>.threshold</code> 
	 * @return threshold of this balancer from system variables.
	 */
	public int getThreshold() {
		updateSysVar();
		return threadsThreshold;
	}
	
	/**
	 * Gets warning threshold of this balancer from system variables. 
	 * If number of tasks running under this balancer exceeds this value, we set warning status, as this category is close to overuse.
	 * Name of system variable is <code>balancer.<i>balancer&nbsp;name</i>.releaseThreshold</code> 
	 * @return threshold of this balancer from system variables.
	 * @see #getReleaseThreshold()
	 */
	public int getWarningThreshold() {
		updateSysVar();
		return warningThreshold;
	}

	/**
	 * Gets release threshold of this balancer from system variables.
	 * If number of threads reaches warning threshold we wait for this threshold to reset warning status.  
	 * Name of system variable is <code>balancer.<i>balancer&nbsp;name</i>.releaseThreshold</code> 
	 * @return threshold of this balancer from system variables.
	 * @see #getWarningThreshold()
	 */
	public int getReleaseThreshold() {
		updateSysVar();
		return releaseThreshold;
	}
	
	/**
	 * Returns true if this balancer is enabled via system variables.
	 * Name of system variable is <code>balancer.<i>balancer&nbsp;name</i>.enabled</code> 
	 * @return true if this balancer is enabled, false otherwise.
	 */
	public boolean isEnabled() {
		updateSysVar();
		return enabled;
	}
	
	/**
	 * Creates and return momentary state of this balancer.
	 * @return Momentary state.
	 */
	public synchronized Info getSnapshot() {
		return new Info();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Specified momentary state of this balancer.
	 * This information used for debugging only.
	 * @author Dimitrijs
	 *
	 */
	public class Info {
		public final String name = Balancer.this.name;
		public final int limit = getThreshold();
		public final int size = threadCount;
		public final int activeCategories = activeCategoriesCount;
		public final List<BalancedCategory.Info> categories;
		public final boolean enabled = isEnabled();
		private Info() {
			categories = new LinkedList<BalancedCategory.Info>();
			catLock.readLock().lock();
			try {
				for (BalancedCategory c : Balancer.this.categories.values()) {
					categories.add(c.getSnapshot());
				}
			} finally {
				catLock.readLock().unlock();
			}
		}
	}
}
