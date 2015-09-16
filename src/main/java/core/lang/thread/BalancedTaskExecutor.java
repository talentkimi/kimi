package core.lang.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import core.lang.thread.ThreadPool.Poolable;
import core.lang.thread.balancer.BalancedCategory;
import core.lang.thread.balancer.Balancer;
import core.lang.thread.balancer.TaskRejectedException;
import core.lang.thread.balancer.BalancedCategory.TaskInfo;
import core.util.Task;
import core.util.WatchDog;
import engine.WebSpider;

/**
 * It is an executor implementation with task balancing capabilities. 
 * It uses {@link Balancer} to wrap tasks. 
 * @author Dimitrijs
 *
 */
public class BalancedTaskExecutor implements Executor {
	private final Executor executor;
	private final Balancer balancer;

	/**
	 * Constructs new instance.
	 * @param threadPool Executor to execute tasks.
	 * @param balancer Balancer to balance tasks.
	 */
	public BalancedTaskExecutor(Executor threadPool, Balancer balancer) {
		this.executor = threadPool;
		this.balancer = balancer;
	}
	
	private BalancedCategory getCategory(Runnable task) {
		String name = task.getClass().getSimpleName();
		return balancer.getCategory(name);
	}

	private BalancedWorker createWorker(TaskInfo info, Runnable task) {
		if (task instanceof Poolable) {
			return new BalancedWorkerPoolableWrapper(info, (Poolable) task);
		} else {
			return new BalancedWorker(info, task);
		}
	}
	
	/**
	 * Checks if specified task can be executed (see {@link Balancer}) and if so 
	 * executes it using external executor. 
	 * @param command task to execute
	 * @throws RejectedExecutionException if task can't be executed.
	 * @throws NullPointerException if command is null
	 */
	@Override
	public void execute(final Runnable command) {
		if (command == null) {
			throw new NullPointerException("Command is null!");
		}
		BalancedCategory category = getCategory(command);
		TaskInfo info = null;
		try {
			 info = category.startNewTask();
		} catch (TaskRejectedException ex) {
			if (command instanceof Task) {
				// Mark the task as unsuccessful.
				Task task = (Task) command;
				task.setThrowable(ex);
				task.complete();
			}
			// by contract of Executor.execute
			throw new RejectedExecutionException(ex); 
		}
		BalancedWorker worker = createWorker(info, command);
		executor.execute(worker);
	}
	
	public class BalancedWorker implements Runnable {

		private final TaskInfo info;
		private final Runnable task;

		public BalancedWorker(TaskInfo info, Runnable task) {
			this.info = info;
			this.task = task;
		}
		
		public void run() {
			WatchDog.Info wdInfo = null;
			// TODO: This stuff should be in ThreadPool only, 
			// but it is workaround just before Balancer goes live.
			if (task instanceof WebSpider) {
				WebSpider ws = (WebSpider) task;
				int timeout = ws.getStageRestTime() + 10000;
				wdInfo = WatchDog.monitor(timeout);
			}
			try {
				task.run();
			} finally {
				info.taskFinished();
				if (wdInfo != null) {
					wdInfo.cancel();
				}
			}
		}
	}
	
	public class BalancedWorkerPoolableWrapper extends BalancedWorker implements Poolable {

		private final Poolable task;
		
		public BalancedWorkerPoolableWrapper(TaskInfo info, Poolable task) {
			super(info, task);
			this.task = task;
		}

		@Override
		public String getPoolableName() {
			return task.getPoolableName();
		}

		@Override
		public long getPoolableStartTime() {
			return task.getPoolableStartTime();
		}
		
		public Poolable getWrapped() {
			return task;
		}
	}
 
}
