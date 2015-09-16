package core.lang.thread;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.Task;
import core.util.WatchDog;
import engine.WebSpider;

/**
 * Don't create instances of this class yourself unless you know what you are
 * doing. It will not be garbage collected unless it is explicitly shutdown.
 * 
 * Most code should use ThreadManager to access a pre-defined pool.
 */
public class ThreadPool implements Executor {

	private static final Logger log = LoggerFactory.getLogger(ThreadPool.class);
	private volatile boolean dumpOnMassiveRejectionEnabled = false;
	private final AtomicInteger rejectedCount = new AtomicInteger();
	
	public static interface Poolable extends Runnable {

		String getPoolableName();

		long getPoolableStartTime();
	}

	private final String name;

	private final ThreadGroup threadGroup;
	private final int priority;
	private final boolean daemon;

	private final ThreadPoolExecutor executor;
	private final LinkedBlockingQueue<Runnable> queue;

	private final AtomicInteger workerCount = new AtomicInteger();

	private volatile boolean enabled = true;

	public ThreadPool(String name, int poolSize) {
		this(name, Thread.NORM_PRIORITY, true, poolSize);
	}

	public ThreadPool(String name, int priority, boolean daemon, int poolSize) {
		this(name, priority, daemon, poolSize, poolSize, Integer.MAX_VALUE, TimeUnit.NANOSECONDS, Integer.MAX_VALUE);
	}

	public ThreadPool(String name, int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit, int queueSize) {
		this(name, Thread.NORM_PRIORITY, true, corePoolSize, maxPoolSize, keepAliveTime, unit, queueSize);
	}

	public ThreadPool(String name, int priority, boolean daemon, int corePoolSize, int maxPoolSize, long keepAliveTime,
			TimeUnit unit, int queueSize) {

		this.name = name;

		this.threadGroup = new ThreadGroup(name);
		this.priority = priority;
		this.daemon = daemon;

		this.queue = new LinkedBlockingQueue<Runnable>(queueSize);
		this.executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, queue);

		this.executor.setThreadFactory(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				return createThread(runnable);
			}
		});

		this.executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
				if (!executor.isShutdown()) {
					reject(runnable);
				}
			}
		});

		if (log.isDebugEnabled())
			if (log.isDebugEnabled()) log.debug (this.toString());
	}

	private void reject(Runnable runnable) {
		if (runnable instanceof Worker) {
			runnable = ((Worker) runnable).getRunnable();
		}

		String threadName = null;
		try {
			if (runnable instanceof Poolable) {
				threadName = ((Poolable) runnable).getPoolableName();
			}
		} catch (Exception ignored) {
		}

		if (threadName == null) {
			threadName = runnable.getClass().getSimpleName();
		}

//		QuickStatsEngine.logQuickStatEventString("Thread Pool Rejected Execution", getName(), 100);
//		QuickStatsEngine2.engine.REJ_THREAD_POOL_EX.logEvent(getName());
//		QuickStatsEngine2.logEvent(new String[] {"Errors", "Rejected Thread Pool Execution"}, getName());

		final RejectedExecutionException rejectedException = new RejectedExecutionException(
				"Task Rejected by Thread Pool " + getName() + " (Pool Saturated): " + threadName);

		if (runnable instanceof Task) {
			final Task task = (Task) runnable;
			task.setThrowable(rejectedException);
			task.complete();
		}

		if (log.isErrorEnabled()) {
			logRejection();
		}
		
		throw rejectedException;
	}
	
	/**
	 * Logs all threads' stacktraces if it is enabled by system variable.
	 * Logs only once per 200 rejections. 
	 */
	private void logRejection() {
		boolean oldDumpEnabled = dumpOnMassiveRejectionEnabled;
//		dumpOnMassiveRejectionEnabled = TripPlanner.getTripPlanner().getSystemVariableManager().getVariableAsBoolean("threadPool.dumpOnMassiveRejectionEnabled");
		if (!dumpOnMassiveRejectionEnabled ) {
			if (oldDumpEnabled) {
				rejectedCount.set(0);
			}
			return;
		}
		if (rejectedCount.incrementAndGet() != 200) {
			return;
		}
		rejectedCount.set(0);
		Map<Thread, StackTraceElement[]> trace = Thread.getAllStackTraces();
		StringBuilder sb = new StringBuilder();
		sb.append("Massive rejection. ")
		  .append(toString())
		  .append("All stacks traces:\n");
		for (Map.Entry<Thread, StackTraceElement[]> thread : trace.entrySet()) {
			sb.append('\t')
			  .append(thread.getKey().getName())
			  .append('\n');
			for (StackTraceElement e: thread.getValue()) {
				sb.append("\t\t")
				  .append(e.toString())
				  .append('\n');
			}
			sb.append('\n');
		}
		log.error(sb.toString());
	}

	@Override
	public String toString() {
		return "ThreadPool[name=" + name + ", core=" + getCorePoolSize() + ", max=" + getMaxPoolSize() + ", queue="
				+ getQueueSize() + ", current=" + getPoolSize() + ", workers=" + getWorkerCount() + ", priority="
				+ priority + ", daemon=" + daemon + ", enabled=" + isEnabled() + "]";
	}

	private Thread createThread(Runnable runnable) {
		Thread thread = new Thread(threadGroup, runnable);
		thread.setPriority(priority);
		thread.setDaemon(daemon);
		return thread;
	}

	public void execute(Runnable runnable) {
//		if (ScheduledQuickStatsLogger.quickStatsLoggingIsEnabled) {
//			QuickStatsEngine2.engine.THREAD_POOL_PROC_THREADS.logEvent(new String[] {getName()}, getWorkerCount());
//			QuickStatsEngine2.engine.THREAD_POOL_SIZE.logEvent(new String[] {getName()}, getPoolSize());
//			QuickStatsEngine2.engine.THREAD_POOL_ACTIVE_COUNT.logEvent(new String[] {getName()}, executor.getActiveCount());
//			QuickStatsEngine2.engine.THREAD_POOL_QUEUE_SIZE.logEvent(new String[] {getName()}, getQueueSize());
//		}

		if (enabled) {
			executor.execute(new Worker(runnable));
		} else {
			createThread(new Worker(runnable)).start();
		}
	}

	public String getName() {
		return name;
	}

	public ThreadGroup getThreadGroup() {
		return threadGroup;
	}

	public int getWorkerCount() {
		return workerCount.get();
	}

	public int getPoolSize() {
		return executor.getPoolSize();
	}

	public int getQueueSize() {
		return queue.size();
	}

	public int getCorePoolSize() {
		return executor.getCorePoolSize();
	}

	public void setCorePoolSize(int corePoolSize) {
		executor.setCorePoolSize(corePoolSize);
	}

	public int getMaxPoolSize() {
		return executor.getMaximumPoolSize();
	}

	public void setMaxPoolSize(int maxPoolSize) {
		executor.setMaximumPoolSize(maxPoolSize);
	}

	public long getKeepAliveTime(TimeUnit unit) {
		return executor.getKeepAliveTime(unit);
	}

	public void setKeepAliveTime(long time, TimeUnit unit) {
		executor.setKeepAliveTime(time, unit);
	}

	public void shutdown(boolean now) {
		if (log.isDebugEnabled()) {
			if (log.isDebugEnabled()) log.debug ("Shutting down thread pool" + (now ? " now" : ""));
			if (log.isDebugEnabled()) log.debug (this.toString());
		}

		if (now) {
			executor.shutdownNow();
		} else {
			executor.shutdown();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Do not set this to false!!! If you do, the system will melt under load.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (!this.enabled) {
			if (log.isWarnEnabled()) log.warn("WARNING!!! Thread Pool " + getName() + " has been disabled.");
		}
	}

	private static final Map<Thread, Worker> workers = Collections.synchronizedMap(new WeakHashMap());

	public static Worker forThread(Thread thread) {
		return workers.get(thread);
	}

	public final class Worker implements Runnable {

		private final Runnable runnable;

		private final long submittedTime;
		private long submitDelay = -1;

		private Worker(Runnable runnable) {
			this.runnable = runnable;
			this.submittedTime = System.currentTimeMillis();
		}

		public String getPoolName() {
			return threadGroup.getName();
		}

		public Runnable getRunnable() {
			return runnable;
		}

		public long getSubmittedTime() {
			return submittedTime;
		}

		/**
		 * This returns the time taken (in millis) for this job to be allocated
		 * a thread from the pool (starting from the time it was submitted to
		 * the pool). If the time is too long, then jobs may be queueing for too
		 * long. A return value of -1 means this job has not been allocated a
		 * thread yet.
		 */
		public long getSubmitDelay() {
			return submitDelay;
		}

		public String getName() {
			String name = runnable.getClass().getSimpleName();
			if (runnable instanceof Poolable) {
				name = ((Poolable) runnable).getPoolableName();
			}
			return "c5:" + name;
		}

		public long getStartTime() {
			long startTime = -1;
			if (runnable instanceof Poolable) {
				startTime = ((Poolable) runnable).getPoolableStartTime();
			}
			return startTime;
		}

		public void run() {
			WatchDog.Info wdInfo = null;
//			ScheduledQuickStatsLoggerThreadRecord threadRecord = null;
//			if (ScheduledQuickStatsLogger.quickStatsLoggingIsEnabled) threadRecord = logThreadStartStats();

			workerCount.incrementAndGet();
			workers.put(Thread.currentThread(), this);
			if (runnable instanceof WebSpider) {
				WebSpider ws = (WebSpider) runnable;
				int timeout = ws.getStageRestTime() + 10000;
				wdInfo = WatchDog.monitor(timeout);
			}
			try {
				try {
					runnable.run();
				} finally {
					workers.remove(Thread.currentThread());
					workerCount.decrementAndGet();
				
				}
			} finally {
				if (wdInfo != null) {
					wdInfo.cancel();
				}
			}
		}
//		private ScheduledQuickStatsLoggerThreadRecord logThreadStartStats() {
//			ScheduledQuickStatsLoggerThreadRecord threadRecord = null;
//			if (!ThreadPool.this.getName().equals("Scheduled"))	{
//				threadRecord = ScheduledQuickStatsLogger.logThreadOperationStarted(runnable);
//			}
//			this.submitDelay = System.currentTimeMillis() - this.submittedTime;
//			// this weird ThreadPool.this. distinguishes inner from outer class
//			QuickStatsEngine2.engine.THREAD_POOL_QUEUE_TIME.logEvent(new String[] {ThreadPool.this.getName()}, submitDelay);
//			return threadRecord;
//		}

//		private void logStats(ScheduledQuickStatsLoggerThreadRecord threadRecord) {
//			try {
				// log thread name (maybe hierarchically), and time spent. Perhaps log different hierarchy levels in separate stats? No, 
				// .... I think just log the lowest level but once live, identify special cases and hack a separate stats for them
				
//				System.err.println("THREAD CLASS NAME: "+runnableName);
//				long timeTaken = System.currentTimeMillis() - threadRecord.timeStarted;
//				ScheduledQuickStatsLogger.logThreadOperationTime(threadRecord.getTaskName(), timeTaken);
//				QuickStatsEngine2.engine.THREAD_OPERATION_PROCESSING_TIMES.logEvent(new String[] {threadRecord.getTaskName()}, timeTaken);
//				QuickStatsEngine2.logEvent(new String[] {"Thread", "Operation Processing Times", threadRecord.getTaskName()}, timeTaken);		
//				QuickStatsEngine2.engine.THREAD_OPERATIONS_EXECUTED.logEvent(threadRecord.getTaskName());
//				QuickStatsEngine2.logEvent(new String[] {"Thread", "Operations Executed"}, threadRecord.getTaskName());			
//			} catch (Throwable t) {
//				QuickStatsEngine2.engine.MISC_ERRORS.logEvent("Error logging thread times");
//			}
//		}
	}
}
