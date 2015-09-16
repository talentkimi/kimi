package core.text;

public class FormattedTable {

	private String newLine = "\r\n";
	private char junction = '+';
	private char vertical = '|';
	private char horizontal = '-';
	private int padding = 1;

	private final String[] columnNames;
	private final String[][] table;

	public int columns() {
		return columnNames.length;
	}

	public String getColumnName(int index) {
		return columnNames[index];
	}

	public int rows() {
		return table.length;
	}

	public String get(int row, int column) {
		return table[row][column];
	}

	public FormattedTable set(int row, Object... columns) {
		if (columns.length != columns()) {
			throw new IllegalArgumentException("columns not the correct length");
		}
		String[] array = new String[columns.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = format(columns[i]);
		}
		table[row] = array;
		return this;
	}

	public FormattedTable set(int row, int column, Object text) {
		table[row][column] = format(text);
		return this;
	}

	private final String format(Object text) {
		return text.toString().replace("\n", "\\n").replace("\r", "\\r");
	}

	private int[] getWidths() {
		int[] widths = new int[columns()];
		for (int col = 0; col < columns(); col++) {
			widths[col] = getColumnName(col).length();
			for (int row = 0; row < rows(); row++) {
				if (get(row, col) == null) {
					set(row, col, "");
				}
				if (widths[col] < get(row, col).length()) {
					widths[col] = get(row, col).length();
				}
			}
		}
		return widths;
	}

	private void append(StringBuilder builder, char c, int count) {
		for (int i = 0; i < count; i++) {
			builder.append(c);
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		int[] widths = getWidths();

		// Columns
		appendSeparator(builder, widths);
		for (int col = 0; col < columns(); col++) {
			builder.append(vertical);
			String name = getColumnName(col);
			append(builder, ' ', padding);
			builder.append(name);
			int extra = 1 + (widths[col] - name.length());
			append(builder, ' ', extra * padding);
		}
		builder.append(vertical).append(newLine);
		appendSeparator(builder, widths);

		// Rows
		for (int row = 0; row < rows(); row++) {
			for (int col = 0; col < columns(); col++) {
				builder.append(vertical);
				String cell = get(row, col);
				append(builder, ' ', padding);
				builder.append(cell);
				int extra = 1 + (widths[col] - cell.length());
				append(builder, ' ', extra * padding);
			}
			builder.append(vertical).append(newLine);
			// appendSeparator(builder, widths);
		}
		appendSeparator(builder, widths);

		return builder.toString();
	}

	private void appendSeparator(StringBuilder builder, int[] widths) {
		builder.append(junction);
		for (int i = 0; i < columns(); i++) {
			if (i > 0) {
				builder.append(junction);
			}
			append(builder, horizontal, widths[i] + 2);
		}
		builder.append(junction).append(newLine);
	}

	public FormattedTable(String[] columnNames, int rows) {
		if (columnNames.length < 1) {
			throw new IllegalArgumentException("column names empty");
		}
		if (rows < 0) {
			throw new IllegalArgumentException("rows=" + rows);
		}
		this.columnNames = columnNames;
		this.table = new String[rows][columnNames.length];
	}

	public static void main(String[] args) {
		String[] names = new String[]{"id", "name"};
		FormattedTable table = new FormattedTable(names, 3);
		table.set(0, new String[]{"", ""});
		table.set(1, new String[]{"33", "robin"});
		table.set(2, new String[]{"332354", "the biggy"});
		System.out.println(table);
	}

}
