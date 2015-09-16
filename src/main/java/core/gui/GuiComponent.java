package core.gui;

import javax.swing.RootPaneContainer;

/**
 * A GUI Component.
 */
public interface GuiComponent extends RootPaneContainer {

	/**
	 * Centre this component.
	 */
	void centre();

	/**
	 * Centre this component.
	 * @param width the width.
	 * @param height the height.
	 */
	void centre(int width, int height);

	/**
	 * Set the size of this component.
	 * @param width the width.
	 * @param height the height.
	 */
	void setSize(int width, int height);

	/**
	 * Returns true if this is undecorated.
	 * @return true if this is undecorated.
	 */
	boolean isUndecorated();

	/**
	 * Set whether this component is resizable.
	 * @param enable true to make component resizable.
	 */
	void setResizable(boolean enable);

}