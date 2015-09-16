package core.lang.thread.balancer;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class represents a balanced category inside a {@link Balancer balancer}.
 * Instances of this class should be used to determinate is it possible or not 
 * to start new task of this category. See {@link Balancer balancer} for further info.
 * @author Dimitrijs
 *
 */
public class BalancedCategory {
	
	private final static float MAX_POPULARITY_FACTOR = 2.5f;
	private final static float MIN_SPEED_FACTOR = 0.4f;
	
	private final Logger log; 
	private final String name;
	private final String fullName;
	private final String weightSysVar;
	private final Balancer parent;
	private int weight = 1000;
	private long weightUpdated;
	private float speedFactor = 1.0f;
	private float actualWeight = weight;
	private float popularity = 0.0f;
	private float popularityFactor = 1.0f;
	
	private long activeOffset;
	private int activeCount;
	private int limit;
	private int activePeak;
	
	private static final int SHORT_HISTORY_MAX_SIZE = 50;
	private final LinkedList<Long> shortHistory = new LinkedList<Long>();
	private long shortHistorySum;
	
	private static final int ACTIVITY_HISTORY_MAX_SIZE = 50;
	private final LinkedList<Integer> activityHistory = new LinkedList<Integer>();
	private long activityHistorySum;
	private long lastActivity;
	private int activityCountPerSec;
	
	private long finishedDurationSum;
	private int finishedCount;
	
	private int rejectedCount;
	
	private boolean inHighUsage = false;
	private long lastTriggered = System.currentTimeMillis();
	
	BalancedCategory(String name, Balancer parent) {
		this.name = name;
		this.weightSysVar = "balancer." + parent.name + ".weight." + name;
		this.parent = parent;
		this.fullName = parent.name + "." + name;
		this.log = LoggerFactory.getLogger(getClass().getName() + "." + fullName);
	}

	public String getName() {
		return name;
	}

	/**
	 * Checks if there is free slots in the {@link Balancer}. 
	 * And if so returns {@link TaskInfo} - descriptor of running task. 
	 * Otherwise throws RejectedExecutionException.
	 * @return {@link TaskInfo} - descriptor of running task. 
	 * @throws TaskRejectedException if there isn't free slots in the {@link Balancer}.
	 */
	public TaskInfo startNewTask() {
		synchronized (parent) {
			addActivity();
			parent.cleanUp();
			if (!acceptNew()) {
//				QuickStatsEngine2.engine.TASK_BALANCER_REJECTED_TASK.logEvent(new String[] {parent.name}, name);
				rejectedCount++;
				if (log.isWarnEnabled()) {
					log.warn("Task [" + name + "] rejected by Balancer [" + parent.name + "]");
				}
				throw new TaskRejectedException("Task Balancer rejected thread");
			}
			TaskInfo result = new TaskInfo();
			return result;
		}
	}
	
	private boolean acceptNew() {
		if (!parent.isEnabled()) {
			return true;
		}
		updateActualWeight();
		
		// Calculate limit for this category
		float allActWght = parent.activeCategoriesWeight;
		if (activeCount == 0) {
			allActWght += actualWeight;
		}
		
		limit = (int) Math.round(parent.getThreshold() * actualWeight / allActWght);
		
		if (limit < 1) {
			limit = 1;
		}
		
		if (inHighUsage) {
			int releaseLimit = limit * parent.getReleaseThreshold() / 100;
			if (activeCount < releaseLimit) {
				long cur = System.currentTimeMillis();
				if (log.isInfoEnabled()) {
					log.info("High usage warning reset [{}%]! Active count: {}; Was in high usage for {}ms", 
							new Object[] {parent.getReleaseThreshold(), activeCount, cur-lastTriggered});
				}
				sendWarningResetEmail(cur, lastTriggered);
				inHighUsage = false;
				lastTriggered = cur;
			}
		} else {
			int warningLimit = limit * parent.getWarningThreshold() / 100;
			if (activeCount > warningLimit) {
				long cur = System.currentTimeMillis();
				if (log.isWarnEnabled()) {
					log.warn("High usage [{}%] detected! Active count: {}; Was normal for {}ms", 
							new Object[] {parent.getWarningThreshold(), activeCount, cur-lastTriggered});
				}
				sendWarningEmail(cur, lastTriggered);
				inHighUsage = true;
				lastTriggered = cur;
			}
		}
		
		return limit > activeCount || (parent.shareUnused && parent.threadCount < parent.getThreshold());
	}

//	private void sendEmail(EmailSubCategory cat, String subj, String message) {
//		try {
//			C5EmailInternal email = new C5EmailInternal(subj, message, false, cat);
//			email.send();
//		} catch (Throwable ex) {
//			if (log.isErrorEnabled()) {
//				log.error("Error sending balancer message", ex);
//			}
//		}
//	}
	
	private void sendWarningEmail(long cur, long prev) {
		String msg = String.format("Category: %s\n" +
				"Registered at: %tc\n" +
				"Was normal since: %tc\n" +
				"Was normal for %ts seconds\n" + 
				"Active count: %d\n" +
				"Limit: %d",
				fullName, cur, prev, cur-prev, activeCount, limit);
//		sendEmail(C5EmailCategories.BALANCER_WARNING, "Overusage warning", msg);
	}
	
	private void sendWarningResetEmail(long cur, long prev) {
		String msg = String.format("Category: %s\n" +
				"Registered at: %tc\n" +
				"Was in overuse since: %tc\n" +
				"Was in overuse for %ts seconds\n" +
				"Active count: %d\n" +
				"Limit: %d",
				fullName, cur, prev, cur-prev, activeCount, limit);
//		sendEmail(C5EmailCategories.BALANCER_WARNING_RESET, "Overusage warning resets", msg);
	}
	
	private void updateWeight() {
		activePeak = Math.max(activePeak, activeCount);
		long cur = System.currentTimeMillis();
		if (cur - weightUpdated < 1000) {
			return;
		}
		weightUpdated = cur;
//		Integer w = SystemVariableManager.getInstance().getVariableAsInteger(weightSysVar, false);
//		if (w != null) {
//			weight = w;
//		}
		if (activePeak > 0) {
//			QuickStatsEngine2.engine.TASK_BALANCER_TASKS_RUNNING_PER_TASK.logEvent(new String[] {parent.name, name}, activePeak);
			activePeak = 0;
		}
	}
	
	private void updateActualWeight() {
		int shortHistoryCount = activeCount + shortHistory.size();
		long momentAverageDuration = 0;
		if (shortHistoryCount > 0) {
			long actSum = System.currentTimeMillis() * activeCount - activeOffset;
			momentAverageDuration = (actSum + shortHistorySum) / shortHistoryCount;
		}
		
		long totalAverageDuration = momentAverageDuration;
		if (finishedCount > 0) {
			totalAverageDuration = finishedDurationSum / finishedCount;
		}
		
		if (activeCount > 0) { 
			if (totalAverageDuration != 0 && momentAverageDuration != 0) { 
				speedFactor = Math.min(1.0f, totalAverageDuration / (float)momentAverageDuration);
				speedFactor = Math.max(MIN_SPEED_FACTOR, speedFactor);
			}
			if (parent.popularitySum > 0) {
				popularityFactor = Math.max(1.0f, popularity / (parent.popularitySum / parent.activeCategoriesCount));
				popularityFactor = Math.min(MAX_POPULARITY_FACTOR, popularityFactor);
			}
		}

		float oldActualWeight = actualWeight;
		updateWeight();
		float sf = parent.ignoreSpeed ? 1 : speedFactor;
		float pf = parent.ignorePopularity ? 1 : popularityFactor;
		actualWeight = weight * sf * pf;
		if (activeCount > 0) {
			parent.activeCategoriesWeight += actualWeight - oldActualWeight; 
		}
	}
	
	private void addActiveThread(long startTime) {
		if (activeCount == 0) {
			parent.activeCategoriesWeight += actualWeight;
			parent.activeCategoriesCount++;
			parent.popularitySum += popularity;
		}
		activeOffset += startTime;
		activeCount++;
		parent.threadCount++;
	}
	
	private void removeActiveThread(long startTime) {
		activeOffset -= startTime;
		activeCount--;
		parent.threadCount--;
		if (activeCount == 0) {
			parent.activeCategoriesWeight -= actualWeight;
			parent.activeCategoriesCount--;
			parent.popularitySum -= popularity;
		}
	}
	
	private void addActivity() {
		long cur = System.currentTimeMillis();
		if (cur - lastActivity > 1000 && lastActivity != 0) {
			activityHistory.add(activityCountPerSec);
			activityHistorySum += activityCountPerSec;
			if (activityHistory.size() > ACTIVITY_HISTORY_MAX_SIZE) {
				activityHistorySum -= activityHistory.poll();
			}
			float oldPopularity = popularity;
			popularity = (float)activityHistorySum / activityHistory.size();
			if (activeCount > 0) {
				parent.popularitySum += popularity - oldPopularity;
			}
			activityCountPerSec = 0;
			lastActivity = cur;
		}
		activityCountPerSec++;
		if (lastActivity == 0) {
			lastActivity = cur;
		}
	}
	
	private void putToHistory(long time) {
		shortHistorySum += time;
		shortHistory.add(time);
		if (shortHistory.size() > SHORT_HISTORY_MAX_SIZE) {
			shortHistorySum -= shortHistory.poll();
		}
		finishedDurationSum += time;
		finishedCount++;
	}
	
	/**
	 * Descriptor of executed task.
	 * @author Dimitrijs
	 *
	 */
	public class TaskInfo {
		private long timeStarted;
		private long timeFinished;
		private TaskInfoRef ref;
		
		public long getTimeStarted() {
			return timeStarted;
		}
		
		private TaskInfo() {
			this.timeStarted = System.currentTimeMillis();
			ref = new TaskInfoRef(this);
			addActiveThread(timeStarted);
			if (log.isDebugEnabled()) {
				log.debug("Task started");
			}
		}
		
		public long getTimeFinished() {
			return timeFinished;
		}
		
		/**
		 * Finishes the task described by this TaskInfo. 
		 * Fries a slot occupied by the task.
		 */
		public void taskFinished() {
			synchronized (parent) {
				if (timeFinished != 0) {
					throw new IllegalStateException("Task already finished");
				}
				ref.setFinished();
				this.timeFinished = System.currentTimeMillis();
				putToHistory(timeFinished - timeStarted);
				if (log.isDebugEnabled()) {
					log.debug("Task finished successfully");
				}
				removeActiveThread(timeStarted);
				updateActualWeight();
			}
		}
	}
	
	class TaskInfoRef extends WeakReference<TaskInfo> {

		private long timeStarted;
		private boolean isFinished;
		
		public TaskInfoRef(TaskInfo referent) {
			super(referent, parent.refQueue);
			timeStarted = referent.timeStarted;
		}
		
		void cleanup() {
			if (get() == null && !isFinished) {
				removeActiveThread(timeStarted);
				updateWeight();
			}
		}
		
		private void setFinished() {
			isFinished = true;
		}
	}
	
	/**
	 * Creates and return momentary state of this BalancedCategory.
	 * @return Momentary state.
	 */
	Info getSnapshot() {
		return new Info();
	}
	
	/**
	 * Specified momentary state of this {@link BalancedCategory}.
	 * This information used for debugging only.
	 * @author Dimitrijs
	 *
	 */
	public class Info {
		String name = BalancedCategory.this.name;
		int activeCount = BalancedCategory.this.activeCount;
		int weight = BalancedCategory.this.weight;
		float speedFactor = BalancedCategory.this.speedFactor;
		int rejectedCount = BalancedCategory.this.rejectedCount;
		int finishedCount = BalancedCategory.this.finishedCount;
		float popularityFactor = BalancedCategory.this.popularityFactor;
		int limit = BalancedCategory.this.limit;
		
		private Info() {
			
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			Info other = (Info) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private BalancedCategory getOuterType() {
			return BalancedCategory.this;
		}
	}
}
