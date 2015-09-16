package core.gui;

import javax.swing.JDialog;

/**
 * A GUI Dialog.
 */
public class GuiDialog extends JDialog implements GuiComponent {

	/**
	 * Creates a new dialog.
	 * @param frame the frame.
	 * @param title the title.
	 */
	public GuiDialog(GuiFrame frame, String title) {
		super(frame, title);
		if (frame == null || title == null) {
			throw new NullPointerException();
		}
		setModal(true);
	}

	/**
	 * Centre this frame.
	 */
	public void centre() {
		GuiToolkit.centreInScreen(this);
	}

	/**
	 * Centre this frame.
	 */
	public void centre(int width, int height) {
		setResizable(false);
		GuiToolkit.setSize(this, width, height);
		GuiToolkit.centreInScreen(this);
	}

}