package core.html.field;

/**
 * A Checkbox Field.
 */
public final class FieldCheckbox extends FieldInput {

	/**
	 * Creates a new input.
	 * @return a new input.
	 */
	protected final FieldInput newInput() {
		return new FieldCheckbox();
	}

	/**
	 * Returns true if this field is optional.
	 * @return true if this field is optional.
	 */
	public boolean isOptional() {
		return true;
	}

	/**
	 * Adds the given value.
	 * @param value the value.
	 */
	public void add(String name, String value, String displayValue, boolean selected) {
		if (name != null && value == null) {
			value = "on";
		}
		super.add(name, value, displayValue, selected);
	}

	public String getType() {
		return "CheckBox";
	}

}
