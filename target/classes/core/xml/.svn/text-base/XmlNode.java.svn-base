package core.xml;

import java.util.Comparator;
import java.util.List;

/**
 * An XML Node (has attributes and children or a value).
 */
public class XmlNode implements Xml {

	public static Comparator<XmlNode> VALUE_ORDER_COMPARATOR = new Comparator<XmlNode>() {
		@Override
		public int compare(XmlNode x1, XmlNode x2) {
			Comparable v1 = (Comparable) x1.getValue();
			Comparable v2 = (Comparable) x2.getValue();
			if (v1 == v2)
				return 0;
			if (v1 == null)
				return -1;
			if (v2 == null)
				return 1;
			return v1.compareTo(v2);
		}
	};

	/** The name. */
	private String name;
	/** The value. */
	private Object value = null;
	/** Optional flag. */
	private boolean optional = true;
	/** The child list. */
	private final XmlList childList = new XmlList();
	/** The attribute list. */
	private final XmlList attributeList = new XmlList();
	private boolean closed = false;

	public void close() {
		name = null;
		value = null;
		childList.close();
		attributeList.close();
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name.intern();
	}

	/**
	 * Returns the child iterator for the named node.
	 * 
	 * @param name
	 *            the node name.
	 * @return the iterator.
	 */
	public final Iterable<Xml> childIterator(String name) {
		return childList.iterator(name);
	}

	/**
	 * Returns the child list (unmodifiable).
	 * 
	 * @return the child list.
	 */
	public List<Xml> childList() {
		return childList.unmodifiableList();
	}

	/**
	 * Returns the attribute list (unmodifiable).
	 * 
	 * @return the attribute list.
	 */
	public List<Xml> attributeList() {
		return attributeList.unmodifiableList();
	}

	/**
	 * Clear this xml.
	 */
	public void clear() {
		value = null;
		childList.clear();
		attributeList.clear();
	}
	
	/**
	 * Clear the value of this xml.
	 */
	public void clearValue() {
		value = null;
	}

	/**
	 * Creates a new XML node.
	 * 
	 * @param name
	 *            the name.
	 */
	public XmlNode(String name) {
		setName(name);
	}

	/**
	 * Creates a new XML node.
	 * 
	 * @param name
	 *            the name.
	 * @param value
	 *            the value.
	 */
	public XmlNode(String name, Object value) {
		this(name);
		setValue(value);
	}

	/**
	 * Returns true if this xml can have children.
	 * 
	 * @return true if this xml can have children.
	 */
	public boolean canHaveChildren() {
		return true;
	}

	/**
	 * Returns true if this xml can have attributes.
	 * 
	 * @return true if this xml can have attributes.
	 */
	public boolean canHaveAttributes() {
		return true;
	}

	/**
	 * Returns the type.
	 * 
	 * @return the type.
	 */
	public final byte getType() {
		return TYPE_XML;
	}

	/**
	 * Returns true if this is optional.
	 * 
	 * @return true if this is optional.
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Sets whether this is optional.
	 * 
	 * @param optional
	 *            true to set this optional.
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value.
	 * 
	 * @return the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(Object value) {
		if (children() > 0) {
			throw new XmlException("Xml cannot hold a value when it has children");
		}
		this.value = value;
	}

	/**
	 * Parses the value from the given string.
	 * 
	 * @param value
	 *            the value.
	 * @return the parsed value.
	 */
	public Object parseValue(String value) {
		return value;
	}

	/**
	 * Returns true if this has a value.
	 * 
	 * @return true if this has a value.
	 */
	public boolean hasAValue() {
		return value != null;
	}

	/**
	 * Returns the number of children.
	 * 
	 * @return the number of children.
	 */
	public int children() {
		return childList.size();
	}

	/**
	 * Returns the number of children with the given name.
	 * 
	 * @return the number of children with the given name.
	 */
	public int children(String name) {
		int children = 0;
		for (int i = 0; i < children(); i++) {
			Xml xml = getChild(i);
			if (xml.getName().equals(name)) {
				children++;
			}
		}
		return children;
	}

	/**
	 * Remove all children.
	 */
	public void removeChildren() {
		childList.clear();
	}

	/**
	 * Remove all attributes.
	 */
	public void removeAttributes() {
		attributeList.clear();
	}

	/**
	 * Returns true if this xml contains a child with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return true if this xml contains a child with the given name.
	 */
	public boolean containsChild(String name) {
		for (int i = 0; i < children(); i++) {
			Xml xml = getChild(i);
			if (xml.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the child at the given index.
	 * 
	 * @param index
	 *            the index.
	 * @return the child.
	 */
	public Xml getChild(int index) {
		return childList.get(index);
	}

	/**
	 * Returns the child with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return the child.
	 */
	public Xml getChild(String name) {
		return getChild(name, false);
	}

	/**
	 * Returns the child with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return the child.
	 */
	public Xml getChild(String name, boolean optional) {
		for (int i = 0; i < children(); i++) {
			Xml xml = getChild(i);
			if (xml.getName().equals(name)) {
				return xml;
			}
		}
		if (optional) {
			return null;
		}
		throw new XmlException("Child not found: \"" + name + "\"");
	}

	/**
	 * Returns the child with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @param index
	 *            the index.
	 * @return the child.
	 */
	public Xml getChild(String name, int index) {
		int count = index;
		for (int i = 0; i < children(); i++) {
			Xml xml = getChild(i);
			if (xml.getName().equals(name)) {
				if (count == 0) {
					return xml;
				}
				count--;
			}
		}
		throw new XmlException("Child not found: \"" + name + "\", index=" + index);
	}

	/**
	 * Removes the child at the given index.
	 * 
	 * @param index
	 * @return the child at the given index.
	 */
	public Xml removeChild(int index) {
		return childList.remove(index);
	}

	/**
	 * Removes the child with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return the child removed.
	 */
	public Xml removeChild(String name) {
		for (int i = 0; i < children(); i++) {
			Xml xml = getChild(i);
			if (xml.getName().equals(name)) {
				return removeChild(i);
			}
		}
		throw new XmlException("Child not found: \"" + name + "\"");
	}

	/**
	 * Removes the child with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @param index
	 *            the index.
	 * @return the child removed.
	 */
	public Xml removeChild(String name, int index) {
		int count = index;
		for (int i = 0; i < children(); i++) {
			Xml xml = getChild(i);
			if (xml.getName().equals(name)) {
				if (count == 0) {
					return removeChild(i);
				}
				count--;
			}
		}
		throw new XmlException("Child not found: \"" + name + "\", index=" + index);
	}

	/**
	 * Sets the child at the given index.
	 * 
	 * @param index
	 *            the index.
	 * @param child
	 *            the child.
	 * @return the child replaced.
	 */
	public Xml setChild(int index, Xml child) {
		if (hasAValue()) {
			throw new XmlException("Xml cannot hold children when it has a value");
		}
		return childList.set(index, child);
	}

	/**
	 * Adds the given child.
	 * 
	 * @param child
	 *            the child to add.
	 */
	public void addChild(Xml child) {
		if (hasAValue()) {
			throw new XmlException("Xml cannot hold children when it has a value");
		}
		childList.add(child);
	}

	/**
	 * Adds the given child at the given index.
	 * 
	 * @param index
	 *            the index.
	 * @param child
	 *            the child to add.
	 */
	public void addChild(int index, Xml child) {
		if (hasAValue()) {
			throw new XmlException("Xml cannot hold children when it has a value");
		}
		childList.add(index, child);
	}

	/**
	 * Returns a new child added to this xml.
	 * 
	 * @param name
	 *            the child name.
	 * @return the child.
	 */
	public Xml newChild(String name) {
		if (hasAValue()) {
			throw new XmlException("Xml cannot hold children when it has a value");
		}
		Xml child = new XmlNode(name);
		childList.add(child);
		return child;
	}

	/**
	 * Adds a new child at the given index.
	 * 
	 * @param name
	 *            the name.
	 * @param index
	 *            the index.
	 * @return the child.
	 */
	public Xml newChildAt(String name, int index) {
		if (hasAValue()) {
			throw new XmlException("Xml cannot hold children when it has a value");
		}
		Xml child = new XmlNode(name);
		childList.add(index, child);
		return child;
	}

	/**
	 * Returns a new child added to this xml.
	 * 
	 * @param name
	 *            the child name.
	 * @param value
	 *            the value.
	 * @return the child.
	 */
	public Xml newChild(String name, Object value) {
		Xml child = newChild(name);
		child.setValue(value);
		return child;
	}

	/**
	 * Adds a new child at the given index.
	 * 
	 * @param name
	 *            the name.
	 * @param value
	 *            the value.
	 * @param index
	 *            the index.
	 * @return the child.
	 */
	public Xml newChildAt(String name, Object value, int index) {
		Xml child = newChildAt(name, index);
		child.setValue(value);
		return child;
	}

	/**
	 * Returns the number of attributes.
	 * 
	 * @return the number of attributes.
	 */
	public int attributes() {
		return attributeList.size();
	}

	/**
	 * Returns the number of attributes with the given name.
	 * 
	 * @return the number of attributes with the given name.
	 */
	public int attributes(String name) {
		int attributes = 0;
		for (int i = 0; i < attributes(); i++) {
			Xml xml = getAttribute(i);
			if (xml.getName().equals(name)) {
				attributes++;
			}
		}
		return attributes;
	}

	/**
	 * Returns true if this xml contains a attribute with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return true if this xml contains a attribute with the given name.
	 */
	public boolean containsAttribute(String name) {
		for (int i = 0; i < attributes(); i++) {
			Xml xml = getAttribute(i);
			if (xml.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the attribute at the given index.
	 * 
	 * @param index
	 *            the index.
	 * @return the attribute.
	 */
	public Xml getAttribute(int index) {
		return attributeList.get(index);
	}

	/**
	 * Returns the attribute with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return the attribute.
	 */
	public Xml getAttribute(String name) {
		for (int i = 0; i < attributes(); i++) {
			Xml xml = getAttribute(i);
			if (xml.getName().equals(name)) {
				return xml;
			}
		}
		throw new XmlException("Attribute not found: \"" + name + "\"");
	}

	/**
	 * Returns the attribute with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @param index
	 *            the index.
	 * @return the attribute.
	 */
	public Xml getAttribute(String name, int index) {
		int count = index;
		for (int i = 0; i < attributes(); i++) {
			Xml xml = getAttribute(i);
			if (xml.getName().equals(name)) {
				if (count == 0) {
					return xml;
				}
				count--;
			}
		}
		throw new XmlException("Attribute not found: \"" + name + "\", index=" + index);
	}

	/**
	 * Removes the attribute at the given index.
	 * 
	 * @param index
	 * @return the attribute at the given index.
	 */
	public Xml removeAttribute(int index) {
		return attributeList.remove(index);
	}

	/**
	 * Removes the attribute with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @return the attribute removed.
	 */
	public Xml removeAttribute(String name) {
		for (int i = 0; i < attributes(); i++) {
			Xml xml = getAttribute(i);
			if (xml.getName().equals(name)) {
				return removeAttribute(i);
			}
		}
		throw new XmlException("Attribute not found: \"" + name + "\"");
	}

	/**
	 * Removes the attribute with the given name.
	 * 
	 * @param name
	 *            the name.
	 * @param index
	 *            the index.
	 * @return the attribute removed.
	 */
	public Xml removeAttribute(String name, int index) {
		int count = index;
		for (int i = 0; i < attributes(); i++) {
			Xml xml = getAttribute(i);
			if (xml.getName().equals(name)) {
				if (count == 0) {
					return removeAttribute(i);
				}
				count--;
			}
		}
		throw new XmlException("Attribute not found: \"" + name + "\", index=" + index);
	}

	/**
	 * Sets the attribute at the given index.
	 * 
	 * @param index
	 *            the index.
	 * @param attribute
	 *            the attribute.
	 * @return the attribute replaced.
	 */
	public Xml setAttribute(int index, Xml attribute) {
		return attributeList.set(index, attribute);
	}

	/**
	 * Adds the given attribute.
	 * 
	 * @param attribute
	 *            the attribute to add.
	 */
	public void addAttribute(Xml attribute) {
		attributeList.add(attribute);
	}

	/**
	 * Returns a new attribute added to this xml.
	 * 
	 * @param name
	 *            the attribute name.
	 * @return the attribute.
	 */
	public Xml newAttribute(String name) {
		XmlAttribute attribute = new XmlAttribute(name);
		attributeList.add(attribute);
		return attribute;
	}

	/**
	 * Returns a new attribute added to this xml.
	 * 
	 * @param name
	 *            the attribute name.
	 * @param value
	 *            the value.
	 * @return the attribute.
	 */
	public Xml newAttribute(String name, Object value) {
		XmlAttribute attribute = new XmlAttribute(name, value);
		attributeList.add(attribute);
		return attribute;
	}

	/**
	 * Returns a string representation of this xml.
	 * 
	 * @return a string representation of this xml.
	 */
	public String toString() {
		return WRITER.write(this);
	}

}