package core.lang.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import core.lang.thread.balancer.Balancer;
import core.lang.thread.balancer.BalancerVisualiserMain;

public class ThreadManager {

	private static ThreadPool createThreadPool(String name, int priority, boolean daemon, int corePoolSize,
			int maxPoolSize, int keepAliveTimeMillis, int queueSize, boolean enabled) {
		ThreadPool pool = new ThreadPool(name, priority, daemon, corePoolSize, maxPoolSize, keepAliveTimeMillis,
				TimeUnit.MILLISECONDS, queueSize);
		pool.setEnabled(enabled);
		ALL_POOLS.put(pool.getName(), pool);
		return pool;
	}

	private static ThreadPool createThreadPool(String name, int priority, boolean daemon, int poolSize, boolean enabled) {
		ThreadPool pool = new ThreadPool(name, priority, daemon, poolSize);
		pool.setEnabled(enabled);
		ALL_POOLS.put(pool.getName(), pool);
		return pool;
	}

	public static ThreadPool getNamedThreadPool(String name) {
		return ALL_POOLS.get(name);
	}

	public synchronized static void startup(int poolSize) {
		if (!running) {
			MAIN = createThreadPool("Main", Thread.NORM_PRIORITY, true, ThreadPoolsConfig.getMainCorePoolSize(),
					poolSize, ThreadPoolsConfig.getMainKeepAliveTimeMillis(),
					ThreadPoolsConfig.getMainQueueSize(), ThreadPoolsConfig.isMainEnabledOnStartup());

			SCHEDULED = createThreadPool("Scheduled", Thread.NORM_PRIORITY, false, ThreadPoolsConfig
					.getScheduledPoolSize(), ThreadPoolsConfig.isScheduledEnabledOnStartup());
			
//			QUEUED = createThreadPool("Queued", Thread.MIN_PRIORITY, true, ThreadPoolsConfig.getQueuedCorePoolSize(),
//			ThreadPoolsConfig.getQueuedMaxPoolSize(), ThreadPoolsConfig.getQueuedKeepAliveTimeMinutes(),
//			ThreadPoolsConfig.getQueuedQueueSize(), ThreadPoolsConfig.isQueuedEnabledOnStartup());
			
			SEARCH_BALANCER = new Balancer("search");
			
			SEARCH_EXECUTOR = new BalancedTaskExecutor(MAIN, SEARCH_BALANCER);
			
			running = true;
			
			if (Boolean.getBoolean("showBalancerVisualiser")) {
				BalancerVisualiserMain.start();
			}
		}
	}
	
	public synchronized static void startup() {
		startup(ThreadPoolsConfig.getMainMaxPoolSize());
	}

	public synchronized static void shutdown(boolean now) {
		if (running) {
			MAIN.shutdown(now);
			SCHEDULED.shutdown(now);
//			QUEUED.shutdown(now);

			running = false;
		}
	}

	private static final Map<String, ThreadPool> ALL_POOLS = new HashMap();

	private static boolean running = false;

	public static ThreadPool MAIN;
	public static ThreadPool SCHEDULED;
	public static Balancer SEARCH_BALANCER;
	public static BalancedTaskExecutor SEARCH_EXECUTOR;
//	public static ThreadPool QUEUED;
}
