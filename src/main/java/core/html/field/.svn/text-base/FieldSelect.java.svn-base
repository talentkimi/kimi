package core.html.field;

import java.util.ArrayList;

import core.html.HtmlField;

/**
 * A Select Field.
 */
public final class FieldSelect implements HtmlField {

	/** Initialised. */
	private boolean initialised = false;
	/** The name. */
	private String name = null;
	/** The value list. */
	private ArrayList<String> valueList = new ArrayList<String>();
	/** The display value list. */
	private ArrayList<String> displayValueList = new ArrayList<String>();
	/** The selected index. */
	private int selectedIndex = -1;
	/** Indicates if locked. */
	private boolean locked = false;

	private boolean valuesEqual = true;

	public boolean areValuesEqual() {
		return valuesEqual;
	}

	/**
	 * Returns a copy of the field.
	 * @return a copy of the field.
	 */
	public HtmlField copy() {
		FieldSelect select = new FieldSelect();
		select.initialised = this.initialised;
		select.name = this.name;
		select.displayValueList = this.displayValueList;
		select.valueList = this.valueList;
		select.selectedIndex = this.selectedIndex;
		select.locked = true;
		this.locked = true;
		return select;
	}

	/**
	 * Reset the field.
	 */
	public void reset() {
		initialised = false;
	}

	/**
	 * Returns true if this field is optional.
	 * @return true if this field is optional.
	 */
	public boolean isOptional() {
		return false;
	}

	/**
	 * Initialise this field.
	 */
	public void initialise() {
		this.initialised = true;
	}

	/**
	 * Returns true if this field is initialised.
	 * @return true if this field is initialised.
	 */
	public boolean isInitialised() {
		return initialised || name == null || valueList.size() == 0;
	}

	/**
	 * Returns true if the case of the name should be ignored.
	 * @return true if the case of the name should be ignored.
	 */
	public boolean ignoreNameCase() {
		return false;
	}

	/**
	 * Returns true if this field can hold any value.
	 * @return true if this field can hold any value.
	 */
	public boolean canContainAnyValue() {
		return false;
	}

	/**
	 * Returns the size.
	 * @return the size.
	 */
	public int size() {
		return valueList.size();
	}

	/**
	 * Returns the selected index.
	 * @return the selected index.
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * Returns the name at the given index.
	 * @param index the index.
	 * @return the name.
	 */
	public String getName(int index) {
		return name;
	}

	/**
	 * Returns the value at the given index.
	 * @param index the index.
	 * @return the value.
	 */
	public String getValue(int index) {
		return valueList.get(index);
	}

	/**
	 * Returns the display value at the given index.
	 * @param index the index.
	 * @return the display value.
	 */
	public String getDisplayValue(int index) {
		return displayValueList.get(index);
	}

	/**
	 * Adds the given value.
	 * @param value the value.
	 */
	public void add(String name, String value, String displayValue, boolean selected) {
		if (locked) {
			throw new IllegalStateException("select field locked");
		}
		if (name != null) {
			this.name = name;
		}
		if (displayValue != null) {
			if (selectedIndex == -1) {
				selectedIndex = 0;
			}
			if (selected) {
				selectedIndex = this.valueList.size();
			}
			if (value == null) {
				value = displayValue;
			} else {
				if (!value.equals(displayValue)) {
					valuesEqual = false;
				}
			}
			this.valueList.add(value);
			this.displayValueList.add(displayValue);
		}
	}

	public boolean isHidden() {
		return false;
	}

	public String getType() {
		return "Select";
	}

}