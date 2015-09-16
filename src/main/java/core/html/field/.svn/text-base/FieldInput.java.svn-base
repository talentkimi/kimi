package core.html.field;

import core.html.HtmlField;

/**
 * An Input Field.
 */
public abstract class FieldInput implements HtmlField {

	/** Initialised. * */
	private boolean initialised = false;
	/** The name. * */
	private String name = null;
	/** The value. * */
	private String value = null;

	public boolean areValuesEqual() {
		return true;
	}

	/**
	 * Returns a copy of the field.
	 * @return a copy of the field.
	 */
	public HtmlField copy() {
		FieldInput input = newInput();
		input.initialised = this.initialised;
		input.name = this.name;
		input.value = this.value;
		return input;
	}

	/**
	 * Creates a new input.
	 * @return a new input.
	 */
	protected abstract FieldInput newInput();

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

	public boolean isHidden() {
		return false;
	}
	
	/**
	 * Initialise this field.
	 */
	public void initialise() {
		initialised = true;
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
		return (name == null ? 0 : 1);
	}

	/**
	 * Returns the selected index.
	 * @return the selected index.
	 */
	public int getSelectedIndex() {
		return (name == null ? -1 : 0);
	}

	/**
	 * Returns true if this field is initialised.
	 * @return true if this field is initialised.
	 */
	public boolean isInitialised() {
		return initialised || name == null || value == null;
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
		return value;
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
		if (name != null && value == null) {
			value = "";
		}
		this.name = name;
		this.value = value;
	}

}
