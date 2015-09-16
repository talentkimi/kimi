package core.xml;

/**
 * The XML Filter.
 */
public final class XmlFilter {

	/**
	 * Filter the given xml.
	 * @param xml the xml.
	 */
	public final Xml filter(Xml xml) {
		for (int i = 0; i < xml.children(); i++) {
			Xml child = xml.getChild(i);
			filter(child);
			if (child.children() == 0 && child.attributes() == 0 && !child.hasAValue()) {
				xml.removeChild(i--);
			}
		}
		return xml;
	}

}
