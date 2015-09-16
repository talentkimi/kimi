package core.xml;

import java.util.List;

/**
 * The XML CDATA.
 */
public class XmlCdata implements Xml {

	private boolean closed = false;
	/** The value. */
	private String value = null;
	/** Optional flag. */
	private boolean optional = false;

	public void close() {
		value = null;
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}

	/**
	 * Returns the child iterator for the named node.
	 * @param name the node name.
	 * @return the iterator.
	 */
	public final Iterable<Xml> childIterator(String name) {
		return Xml.EMPTY_ITERABLE;
	}

	/**
	 * Returns the child list (unmodifiable).
	 * @return the child list.
	 */
	public final List<Xml> childList() {
		return Xml.EMPTY_LIST;
	}

	/**
	 * Returns the attribute list (unmodifiable).
	 * @return the attribute list.
	 */
	public final List<Xml> attributeList() {
		return Xml.EMPTY_LIST;
	}

	/**
	 * Clear this xml.
	 */
	public void clear() {
		value = null;
	}

	/**
	 * Clear the value of this xml.
	 */
	public void clearValue() {
		value = null;
	}

	/**
	 * Creates a new XML CDATA.
	 */
	public XmlCdata() {
	}

	/**
	 * Creates a new XML attribute.
	 * @param name the name.
	 * @param value the value.
	 */
	public XmlCdata(String value) {
		setValue(value);
	}

	/**
	 * Returns the type.
	 * @return the type.
	 */
	public final byte getType() {
		return TYPE_ATTRIBUTE;
	}

	/**
	 * Returns true if this is optional.
	 * @return true if this is optional.
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Sets whether this is optional.
	 * @param optional true to set this optional.
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * Returns true if this xml can have children.
	 * @return true if this xml can have children.
	 */
	public boolean canHaveChildren() {
		return false;
	}

	/**
	 * Returns true if this xml can have attributes.
	 * @return true if this xml can have attributes.
	 */
	public boolean canHaveAttributes() {
		return false;
	}

	/**
	 * Returns the name.
	 * @return the name.
	 */
	public String getName() {
		return "CDATA";
	}

	/**
	 * Returns the value.
	 * @return the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * @param value the value.
	 */
	public void setValue(Object value) {
		this.value = (String) value;
	}

	/**
	 * Parses the value from the given string.
	 * @param value the value.
	 * @return the parsed value.
	 */
	public Object parseValue(String value) {
		return value;
	}

	/**
	 * Returns true if this has a value.
	 * @return true if this has a value.
	 */
	public boolean hasAValue() {
		return value != null;
	}

	/**
	 * Returns the number of children.
	 * @return the number of children.
	 */
	public int children() {
		return 0;
	}

	/**
	 * Returns the number of children with the given name.
	 * @return the number of children with the given name.
	 */
	public int children(String name) {
		return 0;
	}

	/**
	 * Returns true if this xml contains a child with the given name.
	 * @param name the name.
	 * @return true if this xml contains a child with the given name.
	 */
	public boolean containsChild(String name) {
		return false;
	}

	/**
	 * Returns the child at the given index.
	 * @param index the index.
	 * @return the child.
	 */
	public Xml getChild(int index) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Returns the child with the given name.
	 * @param name the name.
	 * @return the child.
	 */
	public Xml getChild(String name) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Returns the child with the given name.
	 * @param name the name.
	 * @param optional true if this is optional.
	 * @return the child.
	 */
	public Xml getChild(String name, boolean optional) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Returns the child with the given name.
	 * @param name the name.
	 * @param index the index.
	 * @return the child.
	 */
	public Xml getChild(String name, int index) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Removes the child at the given index.
	 * @param index
	 * @return the child at the given index.
	 */
	public Xml removeChild(int index) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Removes the child with the given name.
	 * @param name the name.
	 * @return the child removed.
	 */
	public Xml removeChild(String name) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Removes the child with the given name.
	 * @param name the name.
	 * @param index the index.
	 * @return the child removed.
	 */
	public Xml removeChild(String name, int index) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Sets the child at the given index.
	 * @param index the index.
	 * @param child the child.
	 * @return the child replaced.
	 */
	public Xml setChild(int index, Xml child) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Adds the given child.
	 * @param child the child to add.
	 */
	public void addChild(Xml child) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Adds the given child at the given index.
	 * @param index the index.
	 * @param child the child to add.
	 */
	public void addChild(int index, Xml child) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Returns a new child added to this xml.
	 * @param name the child name.
	 */
	public Xml newChild(String name) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Returns a new child added to this xml at the given index.
	 * @param name the child name.
	 * @param index the index.
	 * @return the new child.
	 */
	public Xml newChildAt(String name, int index) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Returns a new child added to this xml.
	 * @param name the child name.
	 * @param value the value.
	 */
	public Xml newChild(String name, Object value) {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Remove all children.
	 */
	public void removeChildren() {
		throw new XmlException("A CDATA section has no children");
	}

	/**
	 * Remove all attributes.
	 */
	public void removeAttributes() {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Returns the number of attributes.
	 * @return the number of attributes.
	 */
	public int attributes() {
		return 0;
	}

	/**
	 * Returns the number of attributes with the given name.
	 * @return the number of attributes with the given name.
	 */
	public int attributes(String name) {
		return 0;
	}

	/**
	 * Returns true if this xml contains a attribute with the given name.
	 * @param name the name.
	 * @return true if this xml contains a attribute with the given name.
	 */
	public boolean containsAttribute(String name) {
		return false;
	}

	/**
	 * Returns the attribute at the given index.
	 * @param index the index.
	 * @return the attribute.
	 */
	public Xml getAttribute(int index) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Returns the attribute with the given name.
	 * @param name the name.
	 * @return the attribute.
	 */
	public Xml getAttribute(String name) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Returns the attribute with the given name.
	 * @param name the name.
	 * @param index the index.
	 * @return the attribute.
	 */
	public Xml getAttribute(String name, int index) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Removes the attribute at the given index.
	 * @param index
	 * @return the attribute at the given index.
	 */
	public Xml removeAttribute(int index) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Removes the attribute with the given name.
	 * @param name the name.
	 * @return the attribute removed.
	 */
	public Xml removeAttribute(String name) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Removes the attribute with the given name.
	 * @param name the name.
	 * @param index the index.
	 * @return the attribute removed.
	 */
	public Xml removeAttribute(String name, int index) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Sets the attribute at the given index.
	 * @param index the index.
	 * @param attribute the attribute.
	 * @return the attribute replaced.
	 */
	public Xml setAttribute(int index, Xml attribute) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Adds the given attribute.
	 * @param attribute the attribute to add.
	 */
	public void addAttribute(Xml attribute) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Returns a new attribute added to this xml.
	 * @param name the attribute name.
	 * @return the attribute.
	 */
	public Xml newAttribute(String name) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Returns a new attribute added to this xml.
	 * @param name the attribute name.
	 * @param value the value.
	 * @return the attribute.
	 */
	public Xml newAttribute(String name, Object value) {
		throw new XmlException("A CDATA section has no attributes");
	}

	/**
	 * Returns a string representation of this xml.
	 * @return a string representation of this xml.
	 */
	public String toString() {
		return WRITER.write(this);
	}

	/**
	 * Set the name.
	 * @param name the name.
	 */
	public void setName(String name) {
		throw new XmlException("A CDATA section has no name");
	}

}
