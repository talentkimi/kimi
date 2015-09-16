package core.html;

/**
 * The HtmlPageWriter.
 */
public class HtmlFormWriter {

	/**
	 * Write the given form list to a string.
	 * @param list the list.
	 * @return the string.
	 */
	public String write(HtmlFormList list) {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			HtmlForm form = list.get(i);
			text.append("<form");
			appendAttribute(text, "name", form.getName());
			appendAttribute(text, "method", form.getMethod());
			appendAttribute(text, "action", form.getAction());
			boolean body = false;
			for (int k = 0; k < form.fields(); k++) {
				if (k == 0) {
					text.append(">\n");
					body = true;
				}
				HtmlField field = form.getField(k);
				text.append("<field");
				for (int l = 0; l < field.size(); l++) {
					String name = field.getName(l);
					String value = field.getValue(l);
					String displayValue = field.getDisplayValue(l);
					appendAttribute(text, name, value);
				}
				text.append(">\n");
			}
			if (body) {
				text.append("</form>\n");
			} else {
				text.append("/>\n");
			}
		}
		return text.toString();
	}

	private void appendAttribute(StringBuilder text, String name, String value) {
		if (name == null || value == null) {
			return;
		}
		text.append(' ');
		text.append(name);
		text.append('=');
		text.append('\"');
		text.append(value);
		text.append('\"');
	}

}