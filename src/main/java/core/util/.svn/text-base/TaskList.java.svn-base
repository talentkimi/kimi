package core.util;


/**
 * A Task List.
 */
public final class TaskList {

	/** The list. */
	private final UtilList list = new UtilList();

	/**
	 * Returns the size.
	 * @return the size.
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Add the given task.
	 * @param task the task.
	 */
	public void add(Task task) {
		list.add(task);
	}

	/**
	 * Returns the task at the given index.
	 * @param index the index.
	 * @return the task.
	 */
	public Task get(int index) {
		return (Task) list.get(index);
	}

	/**
	 * Returns true if this list has finished.
	 * @return true if this list has finished.
	 */
	public boolean hasFinished() {
		for (int i = 0; i < size(); i++) {
			if (get(i).hasStarted() && !get(i).hasFinished()) {
				return false;
			}
		}
		return true;
	}
}
