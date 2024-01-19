package grayscale;

import java.util.Objects;

import org.lwjgl.nanovg.NVGColor;

import components.Decoration;
import components.Panel;
import events.Keyboard;
import events.Mouse;
import utils.Colors;
import utils.TaskTimer;

public class Window {
	private boolean shouldClose = false;
	private boolean visible = true;
	private boolean resizable = true;
	private Vector newSize;
	private Decoration decoration;
	private float decorationHeight = 24;

	private Rectangle bounds = new Rectangle();
	private boolean shouldUpdateBounds = true;
	private Vector minSize;

	private Component content = new Panel();
	private NVGColor background = NVGColor.create();

	public Window(Rectanglec bounds) {
		this(bounds, null);
	}

	public Window(Rectanglec bounds, Decoration decoration) {
		this.bounds.set(bounds);
		this.decoration = decoration;
		minSize = newSize = bounds.getDimensions();
	}

	public void close() {
		shouldClose = true;
	}

	public boolean shouldClose() {
		return shouldClose;
	}

	public void update(Mouse mouse, Keyboard keyboard) {
		if (!visible) {
			return;
		}

		if (decoration != null) {
			if (shouldUpdateBounds) {
				Rectangle deco = new Rectangle();
				deco.set(bounds.x(), bounds.y(), bounds.width(), decorationHeight);
				decoration.setBounds(deco);
			}
			decoration.update(mouse, keyboard);
			if (shouldUpdateBounds) {
				Rectangle rect = new Rectangle(bounds).addInsets(decorationHeight, 0, 0, 0);
				content.setBounds(rect);
			}
			content.update(mouse, keyboard);
		} else {
			if (shouldUpdateBounds) {
				content.setBounds(bounds);
			}
			content.update(mouse, keyboard);
		}

		shouldUpdateBounds = false;

//		if (resizable) {
//			testResizing();
//		}
	}

//	private void testResizing() {
//		Mouse mouse = app.getMouse();
//		Vector2fc mp = mouse.getPos();
//
//		float s = 20;
//		Rectangle r = new Rectangle(bounds.getMaxX(), bounds.getMaxY(), s, s);
//
//		if (mouse.draggedL() && r.contains(mouse.getLPressedStartPos())) {
//			System.out.println("plop");
////			newSize = new Vector2f(x - mp.x(), y - mp.y()).max(minSize);
////			app.setCursor(app.RESIZE_NWSE_CURSOR);
//		}
//		if (mouse.releasedL()) {
////			setBounds(new Rectangle(x, y, newSize.x, newSize.y));
//		}
//	}

	public void render(Graphics g) {
		if (!visible) {
			return;
		}

		g.beginPath();
		g.rect(bounds);
		g.setFillColor(background);
		g.fill();

		if (decoration != null) {
			decoration.render(g);
		}

		content.render(g);
	}

	public void setBounds(Rectanglec newBounds) {
		if (!bounds.equals(newBounds)) {
			bounds.set(newBounds);
			shouldUpdateBounds = true;
		}
	}

	public void setDecoration(Decoration newDeco) {
		decoration = newDeco;
	}

	/**
	 * it is a {@code Panel} by default
	 * 
	 * @param content the new content of this window
	 * @throws NullPointerException if content is null
	 */
	public void setContent(Component content) {
		this.content = Objects.requireNonNull(content);
	}

	public void setBackground(int r, int g, int b, int a) {
		Colors.rgba(r, g, b, a, background);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}
}
