package core.lang.thread;

import java.util.ArrayList;

/**
 * Thread management.
 */
public final class Threads {

	/**
	 * Returns the number of active threads.
	 * @return the number of active threads.
	 */
	public static final int active() {
		Thread[] threads = getAllThreads();
		int active = 0;
		for (int i = 0; i < threads.length; i++) {
			if (threads[i] != null) {
				active++;
			}
		}
		return active;
	}

	/**
	 * Fast way of returning all threads in the given group.
	 * @param group the group.
	 * @param recursive true to examine sub groups.
	 * @return
	 */
	public static final Thread[] getAllThreads(ThreadGroup group, boolean recursive) {
		int multiplier = 2;
		while (true) {
			int count = group.activeCount();
			Thread[] array = new Thread[count * multiplier];
			count = group.enumerate(array, recursive);
			ArrayList list = new ArrayList<Thread>();
			for (int i = 0; i < array.length; i++) {
				if (array[i] != null) {
					list.add(array[i]);
				}
			}
			if (list.size() >= count) {
				array = new Thread[list.size()];
				list.toArray(array);
				return array;
			}
			multiplier++;
		}
	}

	/**
	 * Fast way of returning all threads in the system.
	 * @return an array of threads.
	 */
	public static final Thread[] getAllThreads() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		while (group.getParent() != null) {
			group = group.getParent();
		}
		return getAllThreads(group, true);
	}

}
