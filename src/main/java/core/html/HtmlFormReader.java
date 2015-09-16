package core.html;

import core.html.field.FieldButton;
import core.html.field.FieldCheckbox;
import core.html.field.FieldHidden;
import core.html.field.FieldImage;
import core.html.field.FieldRadio;
import core.html.field.FieldSelect;
import core.html.field.FieldText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.file.TextFile;
import core.net.NetUrl;
import core.text.Text;

/**
 * An HTML Page Reader.
 */
public final class HtmlFormReader {

	private static final Logger log = LoggerFactory.getLogger(HtmlFormReader.class);


	/**
	 * Creates a new html page using the given html.
	 * @param html the html.
	 */
	public HtmlFormList read(String html, NetUrl url) {
		if (html == null) {
			throw new NullPointerException();
		}
		html = Text.remove(html, "<!--", "-->");
		HtmlFormList formList = new HtmlFormList();
		HtmlForm form = null;
		FieldSelect select = null;
		int index = 0;
		while (true) {

			// Start of tag
			index = html.indexOf('<', index);
			if (index == -1) {
				break;
			}
			boolean tagEnd = false;
			if (html.charAt(index + 1) == '/') {
				tagEnd = true;
				index++;
			}

			// Ignore whitespace
			index = skipWhitespace(html, index + 1);

			// Odd characters
			if (!Character.isLetter(html.charAt(index))) {
				continue;
			}

			// Tag name
			StringBuilder nameBuffer = new StringBuilder();
			for (; index < html.length(); index++) {
				char c = html.charAt(index);
				if (!Character.isLetter(c)) {
					break;
				}
				nameBuffer.append(c);
			}
			String tagName = nameBuffer.toString().toLowerCase();

			// Tag body
			int indexEnd = html.indexOf('>', index);
			if (indexEnd == -1) {
				break;
			}
			String tagBody = html.substring(index, indexEnd);
			index = indexEnd + 1;

			// Anchor
			if (tagName.equals("a")) {
				if (!tagEnd) {
					String name = parseAttribute("name", tagBody);
					String href = parseAttribute("href", tagBody);
				}
				continue;
			}

			// Form
			if (tagName.equals("form")) {
				if (!tagEnd) {
					String name = parseAttribute("name", tagBody);
					String action = parseAttribute("action", tagBody);
					String method = parseAttribute("method", tagBody);
					form = new HtmlForm(name, action, method, url);
					formList.add(form);
				}
				continue;
			}
			if (form != null) {

				// Select
				if (tagName.equals("select")) {
					if (!tagEnd) {
						String name = parseAttribute("name", tagBody);
						select = new FieldSelect();
						select.add(name, null, null, false);
						form.addField(select);
					} else {
						select = null;
					}
					continue;
				}

				// Option
				if (tagName.equals("option")) {
					if (!tagEnd && select != null) {
						boolean selected = containsAttribute("selected", tagBody);
						String value = parseAttribute("value", tagBody, null);
						indexEnd = html.indexOf('<', index);
						if (indexEnd == -1) {
							break;
						}
						String valueToDisplay = new String(html.substring(index, indexEnd).trim());
						index = indexEnd;
						if (value == null) {
							value = valueToDisplay;
						}
						select.add(null, value, valueToDisplay, selected);
					}
					continue;
				}

				// Input
				if (tagName.equals("input")) {
					String type = parseAttribute("type", tagBody, "text").toLowerCase();
					// Not sure if these are safe... what about javascript?
					// type = Text.remove(type, "\\");
					// type = Text.remove(type, "\"");
					String name = parseAttribute("name", tagBody);
					String value = parseAttribute("value", tagBody);
					if (type.equals("text") || type.equals("password") || type.equals("edit") || type.equals("input")) {
						FieldText text = new FieldText();
						text.add(name, value, null, false);
						form.addField(text);
						continue;
					}
					if (type.equals("checkbox")) {
						boolean checked = containsAttribute("checked", tagBody);
						FieldCheckbox checkbox = new FieldCheckbox();
						checkbox.add(name, value, null, checked);
						form.addField(checkbox);
						continue;
					}
					if (type.equals("radio")) {
						boolean checked = containsAttribute("checked", tagBody);
						HtmlField radio = form.getField(name, false);
						if (radio != null && radio instanceof FieldRadio) {
							radio.add(name, value, null, checked);
						} else {
							radio = new FieldRadio();
							radio.add(name, value, null, checked);
							form.addField(radio);
						}
						continue;
					}
					if (type.equals("submit")) {
						HtmlField button = form.getField(name, false);
						if (button != null && button instanceof FieldButton) {
							button.add(name, value, null, false);
						} else {
							button = new FieldButton();
							button.add(name, value, null, false);
							form.addField(button);
						}
						continue;
					}
					if (type.equals("hidden")) {
						FieldHidden hidden = new FieldHidden();
						hidden.add(name, value, null, false);
						form.addField(hidden);
						continue;
					}
					if (type.equals("button") || type.equals("image")) {
						HtmlField button = form.getField(name, false);
						if (button != null && button instanceof FieldButton) {
							button.add(name, value, null, false);
						} else {
							button = new FieldButton();
							button.add(name, value, null, false);
							form.addField(button);
						}
						if (type.equals("image")) {
							if (name != null && button != null) {
								FieldImage imageX = new FieldImage((FieldButton) button);
								FieldImage imageY = new FieldImage((FieldButton) button);
								imageX.add(name + ".x", "1", null, false);
								imageY.add(name + ".y", "1", null, false);
								form.addField(imageX);
								form.addField(imageY);
							}
						}
						continue;
					}
					if (type.equals("test")) {
						continue;
					}
					if (!type.equals("\\\"checkbox\\\"")) {
						if (log.isDebugEnabled()) log.debug ("<input type=" + type + "> ??");
					}
					continue;
				}
			}
		}
		return formList;
	}

	/**
	 * Parses an attribute from the given tag body.
	 * @param name the attribute name.
	 * @param tagBody the tag body.
	 * @param defaultValue the default value
	 * @return the attribute value.
	 */
	private String parseAttribute(String name, String tagBody, String defaultValue) {
		int index = 0;
		while (true) {
			index = indexOf(tagBody, name, index);
			if (index == -1) {
				return defaultValue;
			}
			if (index > 0) {
				if (!Character.isWhitespace(tagBody.charAt(index - 1))) {
					index++;
					continue;
				}
			}
			break;
		}
		index += name.length();
		index = skipWhitespace(tagBody, index);
		if (index == tagBody.length()) {
			return "";
		}
		char c = tagBody.charAt(index);
		if (c != '=') {
			return defaultValue;
		}
		index = skipWhitespace(tagBody, index + 1);
		if (index == tagBody.length()) {
			return "";
		}
		c = tagBody.charAt(index);
		int endIndex = index;
		if (c == '\'' || c == '\"') {
			index++;
			endIndex = skipUntil(tagBody, index, c);
		} else {
			for (; endIndex < tagBody.length(); endIndex++) {
				c = tagBody.charAt(endIndex);
				if (c == '>' || Character.isWhitespace(c)) {
					break;
				}
				if (c == '/') {
					if (endIndex + 1 < tagBody.length()) {
						char c2 = tagBody.charAt(endIndex + 1);
						if (c2 == '>') {
							break;
						}
					}
				}
			}
		}
		// IMPORTANT, this substring causes a huge memory leak if we do not do a new String() on it later. Ask James about substrings. 15/9/10 
		String attribute = tagBody.substring(index, endIndex);		
		// IMPORTANT NOTE, james 15/9/10, we are explicitly requesting a new string here to ensure that any unnecessary memory usage due to substring is cleared. This is crucial.
		return Text.intern(attribute, 4, true);
	}

	/**
	 * Parses an attribute from the given tag body.
	 * @param name the attribute name.
	 * @param tagBody the tag body.
	 * @param defaultValue the default value
	 * @return the attribute value.
	 */
	private String parseAttribute(String name, String tagBody) {
		return parseAttribute(name, tagBody, null);
	}

	/**
	 * Returns true if an attribute is in the given tag body.
	 * @param name the name.
	 * @param tagBody the tag body.
	 * @return true if the attribute exists.
	 */
	private boolean containsAttribute(String name, String tagBody) {
		return Text.indexOfIgnoreCase(tagBody, name, 0) != -1;
	}

	/**
	 * Returns the index of the given subtext.
	 * @param text the text.
	 * @param subtext the subtext.
	 * @return the index.
	 */
	private int indexOf(String text, String subtext, int fromIndex) {
		int index = 0;
		for (int i = fromIndex; i < text.length(); i++) {
			char c1 = text.charAt(i);
			char c2 = subtext.charAt(index);
			if (Character.toUpperCase(c1) == Character.toUpperCase(c2)) {
				index++;
				if (index == subtext.length()) {
					return i - subtext.length() + 1;
				}
			} else {
				index = 0;
			}
		}
		return -1;
	}

	/**
	 * Skips whitespace.
	 * @param index the index.
	 * @return the index.
	 */
	private int skipWhitespace(String text, int index) {
		for (; index < text.length(); index++) {
			if (!Character.isWhitespace(text.charAt(index))) {
				break;
			}
		}
		return index;
	}

	private int skipUntil(String text, int index, char until) {
		for (; index < text.length(); index++) {
			char c = text.charAt(index);
			if (c == '\\') {
				index++;
				continue;
			}
			if (c == until) {
				return index;
			}
		}
		return index;
	}

	public static void main(String[] args) {
		try {
			String filename = "C:\\Documents and Settings\\robin\\Desktop\\html reader fix.html";
			String html = new TextFile(filename).readToString();
			NetUrl url = new NetUrl("http://robin.test.com/");
			HtmlFormList page = new HtmlFormReader().read(html, url);
			System.out.println("[HTML PAGE]\n" + new HtmlFormWriter().write(page));
		} catch (Exception e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
	}

}
