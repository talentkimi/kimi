package core.util;

import core.lang.basic.array.IntegerArray;

public class RangeSelector {

	private String original;
	private IntegerArray result;

	public String getOriginal() {
		return original;
	}

	public int[] getResult() {
		return result.toArray();
	}

	public int[] parse(String rangeSelect) {
		return parse(rangeSelect, false, false);
	}

	public int[] parse(String rangeSelect, boolean sort) {
		return parse(rangeSelect, sort, false);
	}

	public int[] parse(String rangeSelect, boolean sort, boolean allowDuplicates) {
		this.original = rangeSelect;
		result = new IntegerArray();
		
		String[] options = rangeSelect.split(",");
		for (int i = 0; i < options.length; i++) {
			if (options[i].indexOf("-") != -1) {
				// Range here..
				String[] range = options[i].split("-");
				try {
					int selectStart = (Integer.parseInt(range[0]));
					int selectEnd = (Integer.parseInt(range[1]));

					if (selectStart > selectEnd) {
						// Asked to End before we've started..
						throw new IllegalArgumentException();
					}

					for (int j = selectStart; j <= selectEnd; j++) {
						result.add(j);
					}
				} catch (NumberFormatException e) {
					// Something broke..
					throw new IllegalArgumentException(e);
				}
			} else {
				try {
					int selected = (Integer.parseInt(options[i]));
					result.add(selected);
				} catch (Exception e) {
					throw new IllegalArgumentException();
				}
			}
		}

		if (!allowDuplicates) {
			result.removeDuplicates();
		}
		
		if (sort) {
			result.sort(true);
		}
		return result.toArray();
	}
}
