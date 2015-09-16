package core.html;

/**
 * An HTML Form Field.
 */
public interface HtmlField {

	/**
	 * Returns true if the value and display value are the same.
	 * @return true if the value and display value are the same.
	 */
	boolean areValuesEqual();

	/**
	 * Returns true if this field is optional.
	 * @return true if this field is optional.
	 */
	boolean isOptional();

	/**
	 * Returns true if this field is hidden;
	 * @return
	 */
	boolean isHidden();
	
	/**
	 * Returns the types of field.
	 * @return
	 */
	String getType();

	/**
	 * Initialise this field.
	 */
	void initialise();

	/**
	 * Returns true if this field is initialised.
	 * @return true if this field is initialised.
	 */
	boolean isInitialised();

	/**
	 * Returns the size.
	 * @return the size.
	 */
	int size();

	/**
	 * Returns true if the case of the name should be ignored.
	 * @return true if the case of the name should be ignored.
	 */
	boolean ignoreNameCase();

	/**
	 * Returns the name at the given index.
	 * @param index the index.
	 * @return the name.
	 */
	String getName(int index);

	/**
	 * Returns the value at the given index.
	 * @param index the index.
	 * @return the value.
	 */
	String getValue(int index);

	/**
	 * Returns the display value at the given index.
	 * @param index the index.
	 * @return the display value.
	 */
	String getDisplayValue(int index);

	/**
	 * Adds the given value.
	 * @param value the value.
	 */
	void add(String name, String value, String displayValue, boolean selected);

	/**
	 * Returns true if this field can hold any value.
	 * @return true if this field can hold any value.
	 */
	boolean canContainAnyValue();

	/**
	 * Returns the selected index.
	 * @return the selected index.
	 */
	int getSelectedIndex();

	/**
	 * Reset the field.
	 */
	void reset();

	/**
	 * Returns a copy of the field.
	 * @return a copy of the field.
	 */
	HtmlField copy();

}
