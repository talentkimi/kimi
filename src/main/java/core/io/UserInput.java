package core.io;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User Input.
 */
public final class UserInput {
	
	private static final Logger log = LoggerFactory.getLogger(UserInput.class);
	
	/** The input thread. */
	private static final UserInputThread INPUT_THREAD = new UserInputThread();

	public static final int available() {
		return INPUT_THREAD.available();
	}

	public static final String readLine() {
		return INPUT_THREAD.readLine();
	}

	/**
	 * Reads a line of user input.
	 * @param prompt the user prompt.
	 * @return the line.
	 */
	public static final String readLine(String prompt) {
		if (prompt == null) {
			throw new NullPointerException();
		}
		System.out.print(prompt);
		return INPUT_THREAD.readLine();
	}

	/**
	 * Reads a question of user input.
	 * @param question the question.
	 * @return the answer.
	 */
	public static final boolean readQuestion(String question) {
		if (question == null) {
			throw new NullPointerException();
		}
		if (!question.endsWith(" ")) {
			question = question + " ";
		}
		while (true) {
			String answer = readLine(question);
			if (answer != null) {
				answer = answer.toLowerCase().trim();

				// Yes
				if (answer.equals("y") || answer.equals("yes") || answer.equals("yep")) {
					return true;
				}
				if (answer.equals("ok")) {
					return true;
				}
				if (answer.equals("proceed")) {
					return true;
				}
				if (answer.equals("affirmative")) {
					return true;
				}
				if (answer.equals("positive")) {
					return true;
				}
				if (answer.equals("correct")) {
					return true;
				}
				if (answer.equals("aye")) {
					return true;
				}
				if (answer.equals("fine")) {
					return true;
				}
				if (answer.equals("yar")) {
					return true;
				}

				// No
				if (answer.equals("n") || answer.equals("no")) {
					return false;
				}
				if (answer.equals("cancel")) {
					return false;
				}
				if (answer.equals("exit")) {
					return false;
				}
				if (answer.equals("negative")) {
					return false;
				}
				if (answer.equals("incorrect")) {
					return false;
				}
			}
		}
	}

	/**
	 * Reads a question of user input.
	 * @param question the question.
	 * @return the answer.
	 */
	public static final String readState(String question, String trueString, String falseString) {
		return readState(question, new String[]{trueString, falseString});
	}

	/**
	 * Reads a question of user input.
	 * @param question the question.
	 * @return the answer.
	 */
	public static final boolean readQuestion(String question, String trueAnswer, String falseAnswer) {
		if (trueAnswer.equalsIgnoreCase(falseAnswer)) {
			throw new IllegalArgumentException(trueAnswer);
		}
		String answer = readState(question, new String[]{trueAnswer, falseAnswer});
		return answer.equalsIgnoreCase(trueAnswer);
	}

	/**
	 * Reads a question of user input.
	 * @param question the question.
	 * @return the answer.
	 */
	public static final String readState(String question, String[] states) {
		if (question == null) {
			throw new NullPointerException();
		}
		if (!question.endsWith(" ")) {
			question = question + " ";
		}
		while (true) {
			String answer = readLine(question);
			if (answer != null) {
				answer = answer.trim();
				for (int i = 0; i < states.length; i++) {
					if (answer.equalsIgnoreCase(states[i])) {
						return states[i];
					}
				}
			}
		}
	}

	private static final class UserInputThread extends Thread {

		private final StreamReader reader = new StreamReader(System.in);
		private final ArrayList<String> lines = new ArrayList<String>();

		public final int available() {
			return lines.size();
		}

		public synchronized String readLine() {
			while (true) {
				try {
					if (lines.size() > 0) {
						synchronized (lines) {
							return lines.remove(0);
						}
					}
					Thread.sleep(10);
				} catch (InterruptedException e) {
					if (log.isErrorEnabled()) log.error(e.getMessage(),e);
				}
			}
		}

		public void run() {
			while (true) {
				try {
					String line = reader.readLine();
					synchronized (lines) {
						lines.add(line);
					}
				} catch (Throwable t) {
					if (log.isErrorEnabled()) log.error(t.getMessage(),t);
				}
			}
		}

		public UserInputThread() {
			setDaemon(true);
			start();
		}
	}
}
