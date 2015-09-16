package core.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JComponent;

/**
 * The GUI Toolkit.
 */
public class GuiToolkit {

	/** The toolkit. */
	private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

	/**
	 * Centre this window.
	 */
	public static void centreInScreen(Component c) {
		if (c == null) {
			throw new NullPointerException();
		}
		double screenWidth = TOOLKIT.getScreenSize().getWidth();
		double screenHeight = TOOLKIT.getScreenSize().getHeight();
		double x = (screenWidth - c.getWidth()) / 2.0;
		double y = (screenHeight - c.getHeight()) / 2.0;
		c.setLocation((int) x, (int) y);
	}

	/**
	 * Set the size of the given component.
	 * @param c the component.
	 * @param percent the percent.
	 */
	public static void setSize(Component c, int percent) {
		int width = getScreenWidth(percent);
		int height = getScreenHeight(percent);
		c.setSize(width, height);
	}

	/**
	 * Returns the width of a percentage of the screen resolution.
	 * @param percent the percentage.
	 * @return the width.
	 */
	public static int getScreenWidth(int percent) {
		if (percent < 10 || percent > 100) {
			throw new IllegalArgumentException("percent=" + percent);
		}
		double factor = 100.0 / percent;
		double width = TOOLKIT.getScreenSize().getWidth();
		return (int) (width / factor);
	}

	/**
	 * Returns the width of a percentage of the screen resolution.
	 * @param percent the percentage.
	 * @return the width.
	 */
	public static int getScreenHeight(int percent) {
		if (percent < 10 || percent > 100) {
			throw new IllegalArgumentException("percent=" + percent);
		}
		double factor = 100.0 / percent;
		double height = TOOLKIT.getScreenSize().getHeight();
		return (int) (height / factor);
	}

	/**
	 * Set the size of the given component.
	 * @param c the component.
	 * @param width the width.
	 * @param height the height.
	 */
	public static final void setSize(Component c, int width, int height) {
		Dimension d = new Dimension(width, height);
		c.setSize(d);
		if (c instanceof JComponent) {
			JComponent jc = (JComponent) c;
			jc.setPreferredSize(d);
			jc.setMaximumSize(d);
			jc.setMinimumSize(d);
		}
	}

}
