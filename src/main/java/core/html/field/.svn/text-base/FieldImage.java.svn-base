package core.html.field;

import core.html.HtmlField;

/**
 * An Image Field.
 */
public class FieldImage implements HtmlField {

	/** The button field. * */
	private FieldButton button;
	/** The name. * */
	private String name = null;
	/** The value. * */
	private String value = null;
	/** Initialised flag. * */
	private boolean initialised = false;

	public boolean areValuesEqual() {
		return true;
	}

	/**
	 * Returns a copy of the field.
	 * @return a copy of the field.
	 */
	public HtmlField copy() {
		FieldImage image = new FieldImage((FieldButton) this.button.copy());
		image.initialised = this.initialised;
		image.name = this.name;
		image.value = this.value;
		return image;
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
		return button.isInitialised() == initialised;
	}

	/**
	 * Returns the button.
	 * @return the button.
	 */
	public FieldButton getButton() {
		return button;
	}

	/**
	 * Sets the button.
	 * @param button
	 */
	public void setButton(FieldButton button) {
		this.button = button;
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
			value = "1";
		}
		this.name = name;
		this.value = value;
	}

	public FieldImage() {
		this.button = null;
	}

	/**
	 * Create a new image field.
	 * @param button the button.
	 */
	public FieldImage(FieldButton button) {
		if (button == null) {
			throw new NullPointerException();
		}
		this.button = button;
	}

	public boolean isHidden() {
		return false;
	}

	public String getType() {
		return "Image";
	}

}
