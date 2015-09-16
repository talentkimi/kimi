package core.os.unix;

import java.util.ArrayList;

import core.text.Text;

public class UnixFileList {

	private final ArrayList<UnixFile> list = new ArrayList<UnixFile>();

	public int size() {
		return list.size();
	}

	public UnixFile get(int index) {
		return list.get(index);
	}

	public String toString() {
		return list.toString();
	}

	public void add(UnixFile file) {
		list.add(file);
	}

	public static UnixFileList parseUnixFileList(String response) {
		if (response == null) {
			throw new NullPointerException();
		}
		UnixFileList list = new UnixFileList();
		int index = response.trim().startsWith("total") ? 1 : 0;
		String[] lines = Text.splitToLines(response);
		for (; index < lines.length; index++) {
			String line = lines[index].trim();
			if (line.length() > 0) {
				list.add(new UnixFile(line));
			}
		}
		return list;
	}

}
