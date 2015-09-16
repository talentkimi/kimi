package core.os.unix;

import core.text.StringDigestor;
import core.text.Text;

/**
 * A Unix File.
 */
public class UnixFile {

	/**
	 * Escape the given UNIX filename.
	 * @param filename the filename.
	 * @return the escaped filename.
	 */
	public static final String escapeFilename(String filename) {
		return Text.insertEscapedChars(filename, '\\', ' ', '$', '*', '&', '%', '|', '^');
	}

	private final String permissions;
	private final long links;
	private final String user;
	private final String group;
	private final long bytes;
	private final String month;
	private final long day;
	private final String date;
	private final String filename;

	public boolean isDirectory() {
		return permissions.charAt(0) == 'd';
	}

	public boolean isSocket() {
		return permissions.charAt(0) == 's';
	}

	public boolean isSymbolicLink() {
		return permissions.charAt(0) == 'l';
	}

	public boolean isFile() {
		return permissions.charAt(0) == '-';
	}

	public long getBytes() {
		return bytes;
	}

	public String getDate() {
		return date;
	}

	public long getDay() {
		return day;
	}

	public String getFilename() {
		return filename;
	}

	public String getGroup() {
		return group;
	}

	public String getMonth() {
		return month;
	}

	public long getLinks() {
		return links;
	}

	public String getPermissions() {
		return permissions;
	}

	public String getUser() {
		return user;
	}

	public String toString() {
		return permissions + " " + links + " " + user + " " + group + " " + bytes + " " + month + " " + day + " " + date + " " + filename;
	}

	public UnixFile(String fileDefinition) {
		StringDigestor digestor = new StringDigestor(fileDefinition);
		this.permissions = digestor.text();
		digestor.whitespace();
		this.links = digestor.parseLong();
		digestor.whitespace();
		this.user = digestor.text();
		digestor.whitespace();
		this.group = digestor.text();
		digestor.whitespace();
		this.bytes = digestor.parseLong();
		digestor.whitespace();
		this.month = digestor.text();
		digestor.whitespace();
		this.day = digestor.parseLong();
		digestor.whitespace();
		this.date = digestor.text();
		digestor.whitespace();
		this.filename = digestor.text();
	}

}
