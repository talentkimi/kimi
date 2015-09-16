package core.html;

import java.util.ArrayList;
import java.util.List;

/**
 * An HTML Page.
 */
public final class HtmlFormList {

	/** The form list. */
	private final List<HtmlForm> list = new ArrayList<HtmlForm>();

	/**
	 * Returns the number of forms.
	 * @return the number of forms.
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Returns the form at the given index.
	 * @param index the index.
	 * @return the form at the given index.
	 */
	public HtmlForm get(int index) {
		return list.get(index);
	}

	public int indexOf(String name) {
		if (name != null) {
			for (int i = 0; i < size(); i++) {
				HtmlForm form = get(i);
				if (name.equals(form.getName())) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns the form with the given name.
	 * @param name the name.
	 * @return the form.
	 */
	public HtmlForm get(String name) {
		int index = indexOf(name);
		if (index != -1) {
			return get(index);
		}
		return null;
	}

	/**
	 * Add the given form.
	 * @param form the form.
	 */
	public void add(HtmlForm form) {
		if (form == null) {
			throw new NullPointerException();
		}
		list.add(form);
	}

	/**
	 * Returns true if this page is empty.
	 * @return true if this page is empty.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Reset the list.
	 */
	public void reset() {
		for (int i = 0; i < size(); i++) {
			get(i).reset();
		}
	}

	/**
	 * Creates a new form list.
	 */
	public HtmlFormList() {
	}

	/**
	 * Creates a new form list.
	 * @param formList list the form list to copy.
	 */
	public HtmlFormList(HtmlFormList formList) {
		if (formList == null) {
			throw new NullPointerException();
		}
		for (int i = 0; i < formList.size(); i++) {
			HtmlForm form = new HtmlForm(formList.get(i));
			add(form);
		}
	}

	/**
	 * Clear this list.
	 */
	public void clear() {
		list.clear();
	}

	/**
	 * Copies the html form list.
	 * @return the copy.
	 */
	public HtmlFormList copy() {
		HtmlFormList formList = new HtmlFormList();
		for (int i = 0; i < size(); i++) {
			HtmlForm form = new HtmlForm(get(i));
			formList.add(form);
		}
		return formList;
	}

	/**
	 * Fix compression-decompression side-effect - before compression, FieldButton references within a FieldInage used to point to a FieldButton in the HtmlForm; after decompression, those references point to an equal but different object. This method updates those references, so that they point to the same object as before compression. (Job #15587)
	 */
	public void updateFieldButtonReferencesWithinFieldImages() {
		for (int i = 0; i < size(); i++) {
			HtmlForm form = get(i);
			if (form != null) {
				get(i).updateFieldButtonReferencesWithinFieldImages();
			}
		}
	}

	public void close() {
		this.list.clear();
	}

}