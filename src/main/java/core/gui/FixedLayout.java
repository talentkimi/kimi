package core.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A Fixed Layout.
 */
public class FixedLayout {

	/** The layout panel. */
	private final JPanel layout = new JPanel();
	/** The current row panel. */
	private JPanel row = null;
	/** Indicates if layout has been set. */
	private boolean set = false;
	/** The background color. */
	private Color background = null;
	/** The foreground color. */
	private Color foreground = null;
	/** The height. */
	private int height = 0;
	/** The width. */
	private int width = 0;

	/**
	 * Returns true if set.
	 * @return true if set.
	 */
	public boolean isSet() {
		return set;
	}

	/**
	 * Set this is the given component.
	 * @param component the component
	 */
	public void set(GuiComponent component) {
		if (!set) {
			if (row == null) {
				throw new IllegalStateException("layout empty");
			}
			if (layout.getHeight() == 0) {
				throw new IllegalStateException("layout empty");
			}
			if (row.getWidth() < layout.getWidth()) {
				padding();
			}
			width = layout.getWidth();
			height = layout.getHeight();
			set = true;
		}
		component.setContentPane(layout);
		if (!component.isUndecorated()) {
			component.centre(width + 8, height + 27);
		}
		component.setResizable(false);
	}

	/**
	 * Creates a new fixed layout.
	 * @param width the width.
	 */
	public FixedLayout(int width) {
		layout.setSize(width, 0);
		layout.setLayout(new BoxLayout(layout, BoxLayout.Y_AXIS));
	}

	/**
	 * Set the background colour.
	 * @param c the colour.
	 */
	public void setBackground(Color c) {
		this.background = c;
	}

	/**
	 * Set the foreground colour.
	 * @param c the colour.
	 */
	public void setForeground(Color c) {
		this.foreground = c;
	}

	/**
	 * New row.
	 */
	public FixedLayout row(int height) {
		if (isSet()) {
			throw new IllegalStateException("layout is set");
		}
		if (height < 1) {
			throw new IllegalArgumentException("height=" + height);
		}
		if (row != null) {
			if (row.getWidth() != layout.getWidth()) {
				padding();
				// throw new IllegalStateException("previous row not filled");
			}
		}
		layout.setSize(layout.getWidth(), layout.getHeight() + height);
		row = new JPanel();
		row.setSize(0, height);
		row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		layout.add(row);
		return this;
	}

	/**
	 * Add padding.
	 */
	public FixedLayout padding() {
		if (isSet()) {
			throw new IllegalStateException("layout is set");
		}
		int width = layout.getWidth() - row.getWidth();
		if (width != 0) {
			padding(width);
		}
		return this;
	}

	/**
	 * Add padding.
	 * @param width the width.
	 */
	public FixedLayout padding(int width) {
		if (isSet()) {
			throw new IllegalStateException("layout is set");
		}
		JPanel panel = new JPanel();
		if (background != null) {
			panel.setBackground(background);
		}
		return add(panel, width);
	}

	/**
	 * Add the given component.
	 * @param c the component.
	 * @param width the width.
	 */
	public FixedLayout centre(Component c, int width) {
		if (isSet()) {
			throw new IllegalStateException("layout is set");
		}
		if (c == null) {
			throw new NullPointerException();
		}
		int paddingWidth = layout.getWidth();
		paddingWidth -= width;
		paddingWidth /= 2;
		padding(paddingWidth);
		add(c, width);
		padding(paddingWidth);
		return this;
	}

	/**
	 * Add the given component.
	 * @param c the component.
	 * @param width the width.
	 */
	public FixedLayout add(Component c, int width) {
		if (isSet()) {
			throw new IllegalStateException("layout is set");
		}
		if (c == null) {
			throw new NullPointerException();
		}
		if (width < 1) {
			throw new IllegalArgumentException("width=" + width);
		}
		if (row.getWidth() == layout.getWidth()) {
			throw new IllegalStateException("width already filled");
		}
		if (row.getWidth() + width > layout.getWidth()) {
			throw new IllegalStateException("component too wide " + (row.getWidth() + width) + ">" + layout.getWidth());
		}
		GuiToolkit.setSize(row, row.getWidth() + width, row.getHeight());
		GuiToolkit.setSize(c, width, row.getHeight());
		row.add(c);
		return this;
	}

	/**
	 * Add the given text.
	 * @param text the text.
	 * @param width the width.
	 */
	public FixedLayout add(String text, int width) {
		return add(newLabel(text), width);
	}

	/**
	 * Creates a new label.
	 * @param text the text.
	 * @return the label.
	 */
	private Component newLabel(String text) {
		JLabel label = new JLabel(text);
		if (foreground != null) {
			label.setForeground(foreground);
		}
		if (background != null) {
			label.setBackground(background);
		}
		return label;
	}

	/**
	 * Add the given component.
	 * @param c the component.
	 */
	public FixedLayout add(Component c) {
		int width = layout.getWidth() - row.getWidth();
		if (width == 0) {
			throw new IllegalStateException("row already filled");
		}
		return add(c, width);
	}

	/**
	 * Add the given text.
	 * @param text the text.
	 */
	public FixedLayout add(String text) {
		return add(newLabel(text));
	}

	/**
	 * Centre the given text.
	 * @param text the text.
	 * @param width the width.
	 */
	public FixedLayout centre(String text, int width) {
		if (isSet()) {
			throw new IllegalStateException("layout is set");
		}
		return centre(new JLabel(text), width);
	}

}