package core.net.ftp;

import java.util.ArrayList;
import java.util.List;

public class FtpResponse {

	private List<String> lines = new ArrayList<String>();
	private FtpCode code = null;

	public FtpCode getCode() {
		return code;
	}

	public int lines() {
		return lines.size();
	}

	public String getLine(int index) {
		return lines.get(index);
	}

	public String getLastLine() {
		return getLine(lines() - 1);
	}

	/**
	 * Adds a new line to this response.
	 * @param line the line to add.
	 * @return true if there are more lines in the response.
	 */
	public boolean addLine(String line) {
		String lineCodeString = line.substring(0, 3);
		try {
			int lineCode = Integer.parseInt(lineCodeString);
			if (code == null) {
				code = new FtpCode(lineCode);
			} else {
				if (!code.equals(lineCode)) {
					throw new IllegalArgumentException("Expected code " + code + " in line '" + line + "'");
				}
			}
			lines.add(line);
			return line.charAt(3) != ' ';
		} catch (NumberFormatException nfe) {
			System.out.println("[Skipping]");
			return true;
		}
	}

}
