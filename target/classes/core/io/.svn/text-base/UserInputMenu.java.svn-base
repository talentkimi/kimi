package core.io;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UserInputMenu {

	private static final Logger log = LoggerFactory.getLogger(UserInputMenu.class);

	
	private final ArrayList<Object> selectionList = new ArrayList<Object>();
	private final ArrayList<String> descriptionList = new ArrayList<String>();
	private final String title;
	private final String prompt;

	public UserInputMenu(String title, String prompt) {
		if (title == null || prompt == null) {
			throw new NullPointerException();
		}
		this.title = title;
		this.prompt = prompt;
	}

	public void add(Object selection, String description) {
		if (selection.toString().length() == 0) {
			throw new IllegalArgumentException("selection is empty");
		}
		if (description.length() == 0) {
			throw new IllegalArgumentException("description is empty");
		}
		for (int i = 0; i < selectionList.size(); i++) {
			if (selectionList.get(i).toString().equals(selection.toString())) {
				throw new IllegalArgumentException("duplicate selection '" + selection + "'");
			}
		}
		selectionList.add(selection);
		descriptionList.add(description);
	}

	public void execute() {
		while (true) {
			try {
				System.out.println();
				System.out.println();
				System.out.println(title);
				System.out.println();
				for (int i = 0; i < selectionList.size(); i++) {
					System.out.println(selectionList.get(i) + ". " + descriptionList.get(i));
				}
				System.out.println();

				String selection = UserInput.readLine(prompt).trim();
				if (selection.length() == 0) {
					break;
				}
				int index = Integer.parseInt(selection);
				System.out.println();
				execute(index);
			} catch (NumberFormatException nfe) {
			} catch (Throwable t) {
				if (log.isDebugEnabled()) log.debug ("exception", t);
			}
		}
	}

	protected abstract boolean execute(int selection);
}
