package core.html;

import java.util.ArrayList;

import core.html.field.FieldButton;
import core.html.field.FieldImage;
import core.http.request.HttpRequestMethodList;
import core.net.NetUrl;
import core.util.UtilMap;

/**
 * An HTML Form.
 */
public final class HtmlForm implements HttpRequestMethodList {

	/** The name. */
	private final String name;
	/** The action. */
	private final String action;
	/** The method. */
	private String method;
	/** The field list. */
	private final ArrayList<HtmlField> fieldList = new ArrayList<HtmlField>();

	/**
	 * Returns the action.
	 * @return the action.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns the action.
	 * @return the action.
	 */
	public final String getAction() {
		return action;
	}

	/**
	 * Set the method.
	 * @param method the method.
	 */
	public final void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Returns the method.
	 * @return the method.
	 */
	public final String getMethod() {
		return method;
	}

	/**
	 * Returns the number of fields.
	 * @return the number of fields.
	 */
	public int fields() {
		return fieldList.size();
	}

	/**
	 * Returns the field at the given index.
	 * @param index the index.
	 * @return the field at the given index.
	 */
	public HtmlField getField(int index) {
		return fieldList.get(index);
	}

	/**
	 * Removes the field at the given index.
	 * @param index the index.
	 * @return the field at the given index.
	 */
	public HtmlField removeField(int index) {
		return fieldList.remove(index);
	}

	/**
	 * Returns the number of fields with the given name.
	 * @param name the name.
	 * @return the number of fields.
	 */
	public int fields(String name) {
		int number = 0;
		if (name == null) {
			throw new NullPointerException();
		}
		for (int i = 0; i < fields(); i++) {
			HtmlField field = getField(i);
			if (!field.isInitialised() && field.size() > 0) {
				String fieldName = field.getName(0);
				if (field.ignoreNameCase()) {
					if (fieldName.equalsIgnoreCase(name)) {
						number++;
					}
				} else {
					if (fieldName.equals(name)) {
						number++;
					}
				}
			}
		}
		return number;
	}

	/**
	 * Returns the named field.
	 * @param name the name.
	 * @return the field.
	 */
	public HtmlField getField(String name, int index, boolean notNull) {
		if (name == null) {
			return null;
		}
		for (int i = 0; i < fields(); i++) {
			HtmlField field = getField(i);
			if (!field.isInitialised() && field.size() > 0) {
				String fieldName = field.getName(0);
				if (field.ignoreNameCase()) {
					if (fieldName.equalsIgnoreCase(name)) {
						if (index == 0) {
							return field;
						}
						index--;
					}
				} else {
					if (fieldName.equals(name)) {
						if (index == 0) {
							return field;
						}
						index--;
					}
				}
			}
		}
		if (notNull) {
			throw new IllegalArgumentException("HTML field not found: \"" + name + "\"");
		}
		return null;
	}

	/**
	 * Returns the named field.
	 * @param name the name.
	 * @return the field.
	 */
	public HtmlField getField(String name, int index) {
		return getField(name, index, true);
	}

	/**
	 * Returns the named field.
	 * @param name the name.
	 * @return the field.
	 */
	public HtmlField getField(String name, boolean notNull) {
		return getField(name, 0, notNull);
	}

	/**
	 * Returns the named field.
	 * @param name the name.
	 * @return the field.
	 */
	public HtmlField getField(String name) {
		return getField(name, 0, true);
	}

	/**
	 * Returns the named field.
	 * @param name the name.
	 * @param offset the offset.
	 * @return the field.
	 */
	public void removeField(String name, int offset) {
		if (name == null) {
			throw new NullPointerException();
		}
		for (int i = 0; i < fields(); i++) {
			HtmlField field = getField(i);
			if (!field.isInitialised() && field.size() > 0) {
				String fieldName = field.getName(0);
				if (field.ignoreNameCase()) {
					if (fieldName.equalsIgnoreCase(name)) {
						if (offset == 0) {
							removeField(i);
							return;
						}
						offset--;
					}
				} else {
					if (fieldName.equals(name)) {
						if (offset == 0) {
							removeField(i);
							return;
						}
						offset--;
					}
				}
			}
		}
		throw new IllegalArgumentException("HTML field not found: \"" + name + "\" (" + offset + ")");
	}

	/**
	 * Returns true if the named field exists.
	 * @param name the name of the field.
	 * @return true if the named field exists.
	 */
	public boolean containsField(String name) {
		return fields(name) > 0;
	}

	/**
	 * Add the given field.
	 * @param field the field.
	 */
	public void addField(HtmlField field) {
		if (field == null) {
			throw new NullPointerException();
		}
		fieldList.add(field);
	}

	/**
	 * Reset the fields.
	 */
	public void reset() {
		for (int i = 0; i < fields(); i++) {
			getField(i).reset();
		}
	}

	/**
	 * Remove uninitialised fields from the form.
	 */
	public void removeUninitialisedFields() {
		for (int i = 0; i < fields(); i++) {
			HtmlField field = getField(i);
			if (!field.isInitialised()) {
				int selectedIndex = field.getSelectedIndex();
				if (selectedIndex != -1) {
					removeField(i);
					i--;
				}
			}
		}
	}

	/**
	 * Fix compression-decompression side-effect - before compression, FieldButton references within a FieldInage used to point to a FieldButton in the HtmlForm; after decompression, those references point to an equal but different object. This method updates those references, so that they point to the same object as before compression. (Job #15587)
	 */
	public void updateFieldButtonReferencesWithinFieldImages() {
		UtilMap buttons = new UtilMap();
		for (int i = 0; i < fieldList.size(); i++) {
			HtmlField field = (HtmlField) fieldList.get(i);
			if (field != null && field instanceof FieldButton) {
				FieldButton button = (FieldButton) field;
				buttons.put(button.getButtonId(), button);
			}
		}
		for (int i = 0; i < fieldList.size(); i++) {
			HtmlField field = (HtmlField) fieldList.get(i);
			if (field != null && field instanceof FieldImage) {
				FieldImage image = (FieldImage) field;
				FieldButton button = image.getButton();
				if (button != null) {
					String buttonId = button.getButtonId();
					if (buttons.containsKey(buttonId)) {
						image.setButton((FieldButton) buttons.get(buttonId));
					}
				}
			}
		}
		buttons.clear();
		buttons = null;
	}

	/**
	 * Creates an empty html form.
	 */
	public HtmlForm() {
		this.name = null;
		this.action = null;
		this.method = null;
	}

	/**
	 * Creates a new HTML form.
	 * @param form the form to copy.
	 */
	public HtmlForm(HtmlForm form) {
		if (form == null) {
			throw new NullPointerException();
		}
		this.name = form.name;
		this.action = form.action;
		this.method = form.method;
		for (int i = 0; i < form.fields(); i++) {
			HtmlField field = form.getField(i).copy();
			addField(field);
		}
	}

	/**
	 * Creates a new HTML form.
	 * @param name the form name.
	 * @param action the action.
	 * @param method the method.
	 */
	public HtmlForm(String name, String action, String method) {
		this.name = name;
		this.action = action;
		this.method = getMethod(method);
	}

	/**
	 * Creates a new HTML form.
	 * @param url the url.
	 */
	public HtmlForm(NetUrl url) {
		this(null, url.toString(true), METHOD_GET);
	}

	/**
	 * Creates a new HTML form.
	 * @param name the form name.
	 * @param action the action.
	 * @param method the method.
	 * @param url the url.
	 */
	public HtmlForm(String name, String action, String method, NetUrl url) {
		this(name, getAction(action, url), method);
	}

	/**
	 * Returns the action.
	 * @param action the action.
	 * @param baseUrl the base url
	 * @return the action.
	 */
	private static final String getAction(String action, NetUrl baseUrl) {
		if (action == null && baseUrl == null) {
			throw new IllegalArgumentException("action or base url must be set");
		}
		boolean javascript = false;
		if (action != null) {
			javascript = action.toLowerCase().indexOf("javascript:") != -1;
		}
		if (baseUrl != null) {
			NetUrl actionUrl;
			if (action == null || javascript) {
				actionUrl = baseUrl.copy();
				actionUrl.setQuery("");
				actionUrl.setFragment("");
			} else {
				actionUrl = new NetUrl(action);
				if (actionUrl.getProtocol() == null) {
					actionUrl.setFrom(baseUrl);
				}
			}
			action = actionUrl.toString(true);
		}
		return action;
	}

	/**
	 * Returns the method, confirmed from the given method.
	 * @param method the method.
	 * @return the method.
	 */
	private static final String getMethod(String method) {
		if (method == null) {
			method = METHOD_GET;
		} else {
			if (method.trim().toUpperCase().equals(METHOD_POST)) {
				method = METHOD_POST;
			} else {
				method = METHOD_GET;
			}
		}
		return method;
	}

	/**
	 * Clear this form.
	 */
	public void clear() {
		fieldList.clear();
	}

	public void set(int index, HtmlField field) {
		fieldList.set(index, field);
	}

}
