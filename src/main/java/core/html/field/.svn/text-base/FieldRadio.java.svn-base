package core.html.field;

import java.util.ArrayList;

import core.html.HtmlField;

/**
 * A Radio Field.
 */
public final class FieldRadio implements HtmlField {

	/** Initialised. * */
	private boolean initialised = false;
	/** The name list. * */
	private ArrayList<String> nameList = new ArrayList<String>();
	/** The value list. * */
	private ArrayList<String> valueList = new ArrayList<String>();
	/** The selected index. * */
	private int selectedIndex = -1;
	/** Indicates if locked. */
	private boolean locked = false;

	public boolean areValuesEqual() {
		return true;
	}

	/**
	 * Returns a copy of the field.
	 * @return a copy of the field.
	 */
	public HtmlField copy() {
		FieldRadio radio = new FieldRadio();
		radio.initialised = this.initialised;
		radio.nameList = this.nameList;
		radio.valueList = this.valueList;
		radio.selectedIndex = this.selectedIndex;
		radio.locked = true;
		this.locked = true;
		return radio;
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
		return true;
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
		return initialised;
	}

	/**
	 * Returns true if the case of the name should be ignored.
	 * @return true if the case of the name should be ignored.
	 */
	public boolean ignoreNameCase() {
		return true;
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
		return (String) nameList.get(index);
	}

	/**
	 * Returns the value at the given index.
	 * @param index the index.
	 * @return the value.
	 */
	public String getValue(int index) {
		return (String) valueList.get(index);
	}

	/**
	 * Returns the display value at the given index.
	 * @param index the index.
	 * @return the display value.
	 */
	public String getDisplayValue(int index) {
		return null;
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
			if (value == null) {
				value = "on";
			}
			if (selectedIndex == -1) {
				selectedIndex = 0;
			}
			if (selected) {
				selectedIndex = this.valueList.size();
			}
			nameList.add(name);
			valueList.add(value);
		}
	}

	public boolean isHidden() {
		return false;
	}

	public String getType() {
		return "Radio";
	}

}