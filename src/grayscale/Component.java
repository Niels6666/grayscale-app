package grayscale;

import java.util.ArrayList;

import java.util.List;

import org.lwjgl.nanovg.NVGColor;

import events.DropTargetListener;
import events.KeyBinding;
import events.KeyEvent;
import events.Keyboard;
import events.KeyboardListener;
import events.Mouse;
import events.MouseListener;
import layouts.DynamicRow;
import utils.ColorSetter;

public abstract class Component {
	/**
	 * Placements
	 */
	public static final int TOP = 0, LEFT = 1, BOTTOM = 2, RIGHT = 3, CENTER = 4;

	/**
	 * The component that has the focus --> only one component can be focused
	 */
	private static Component FOCUSED = null;

	private Component parent;

	// not updated
	private boolean visible = true;
	private boolean disabled = false;

	/**
	 * if true this component will listen to all events if its parent is focused
	 */
	private boolean isGlass = false;

	private DropTargetListener dropListener;
	private MouseListener mouseListener;
	private KeyboardListener keyboardListener;
	private List<KeyBinding> bindings = new ArrayList<>();

	private Layout layout = new DynamicRow(0, LEFT, CENTER);
	private ComponentUI componentUI;
	private List<Component> children = new ArrayList<>();

	protected NVGColor background = NVGColor.create();
	protected NVGColor foreground = NVGColor.create();

	// updated every frame

	/**
	 * absolute position and dimensions on the screen
	 */
	protected Rectangle bounds = new Rectangle();

	protected Rectangle preferedSize = new Rectangle();

	protected Insets insets = new Insets();

	private boolean hovered = false;

	private boolean pressLstartedOnComp = false;
	private boolean pressWstartedOnComp = false;
	private boolean pressRstartedOnComp = false;

	protected Component(ComponentUI componentUI) {
		setUI(componentUI);
	}

	/**
	 * This method will update and recalculate all of the child's bounds as well
	 * 
	 * @param b the new bounds for this component
	 */
	public void setBounds(Rectanglec b) {
		bounds.set(b).addInsets(insets);

		if (!children.isEmpty()) {
			layout.update(children, bounds);
		}
	}

	/**
	 * if disabled, the child class won't be updated
	 */
	public final void update(Mouse mouse, Keyboard keyboard) {
		testMouse(mouse);
		testKeyboard(keyboard);

		if (dropListener != null && hovered && !isDisabled()) {
			List<String> drop = mouse.consumeDrop();
			if (!drop.isEmpty()) {
				dropListener.acceptDrop(this, drop);
			}
		}

		componentUI.update(this);

		children.forEach((c) -> c.update(mouse, keyboard));
	}

	private void testMouse(Mouse mouse) {
		Vectorc pos = mouse.getPos(), prevPos = mouse.getPrevPos();
		boolean prevHovered = hovered;

		boolean anyChildHovered = children.stream().allMatch((c) -> !c.bounds.contains(pos));
		hovered = bounds.contains(pos) && (isGlass || anyChildHovered);

		if (isDisabled() || mouseListener == null) {
			return;
		}

		if (!prevHovered && hovered) {
			mouseListener.mouseEntered(this);
		} else if (prevHovered && !hovered) {
			mouseListener.mouseExited(this);
		}

		int mods = mouse.getModifiers();

		if (mouse.isMoved() && hovered) {
			if (mouse.draggedL()) {
				mouseListener.mouseDragged(this, Mouse.BTN_LEFT, mods, mouse.getLPressedStartPos(), prevPos, pos);
			} else if (mouse.draggedW()) {
				mouseListener.mouseDragged(this, Mouse.BTN_MIDDLE, mods, mouse.getWPressedStartPos(), prevPos, pos);
			} else if (mouse.draggedR()) {
				mouseListener.mouseDragged(this, Mouse.BTN_RIGHT, mods, mouse.getRPressedStartPos(), prevPos, pos);
			} else {
				mouseListener.mouseMoved(this, prevPos, pos);
			}
		}

		if (mouse.pressedL()) {
			pressLstartedOnComp = hovered;
			mouseListener.mousePressed(this, Mouse.BTN_LEFT, mods, pos);
		} else if (mouse.releasedL()) {
			if (pressLstartedOnComp) {
				mouseListener.mouseClicked(this, Mouse.BTN_LEFT, mods, pos, mouse.clickedL());
			} else {
				mouseListener.mouseReleased(this, Mouse.BTN_LEFT, mods, pos, hovered);
			}
			pressLstartedOnComp = false;
		}

		if (mouse.pressedW()) {
			pressWstartedOnComp = hovered;
			mouseListener.mousePressed(this, Mouse.BTN_MIDDLE, mods, pos);
		} else if (mouse.releasedW()) {
			if (pressWstartedOnComp) {
				mouseListener.mouseClicked(this, Mouse.BTN_MIDDLE, mods, pos, mouse.clickedW());
			} else {
				mouseListener.mouseReleased(this, Mouse.BTN_MIDDLE, mods, pos, hovered);
			}
			pressWstartedOnComp = false;
		}

		if (mouse.pressedR()) {
			pressRstartedOnComp = hovered;
			mouseListener.mousePressed(this, Mouse.BTN_RIGHT, mods, pos);
		} else if (mouse.releasedR()) {
			if (pressRstartedOnComp) {
				mouseListener.mouseClicked(this, Mouse.BTN_RIGHT, mods, pos, mouse.clickedR());
			} else {
				mouseListener.mouseReleased(this, Mouse.BTN_RIGHT, mods, pos, hovered);
			}
			pressRstartedOnComp = false;
		}
	}

	private void testKeyboard(Keyboard keyboard) {
		boolean focused = isFocused();
		boolean descendantFocused = descendantFocused();

		for (KeyEvent e : keyboard.getEvents()) {
			for (KeyBinding kb : bindings) {
				if (kb.test(e.getKey(), e.getAction(), e.getMods(), focused, descendantFocused)) {
					kb.performAction();
				}
			}
		}

		if (focused && keyboardListener != null) {
			for (KeyEvent e : keyboard.getEvents()) {
				keyboardListener.processKeyEvent(this, e);
			}
		}
	}

	public void render(Graphics g) {
		if (!visible || bounds.isEmpty()) {
			return;
		}

		componentUI.render(this, g);
		for (Component c : children) {
			c.render(g);
		}
	}

	public void grabFocus() {
		FOCUSED = this;
	}

	public boolean isFocused() {
		return FOCUSED == this;
	}

	/**
	 * @return true if one of the descendant is focused
	 */
	public boolean descendantFocused() {
		if (!children.isEmpty()) {
			return children.stream().anyMatch((c) -> c.isFocused() || c.descendantFocused());
		}
		return false;
	}

	public float getX() {
		return bounds.x();
	}

	public float getY() {
		return bounds.y();
	}

	public float getWidth() {
		return bounds.width();
	}

	public float getHeight() {
		return bounds.height();
	}

	public Rectanglec getBounds() {
		return bounds;
	}

	public Insets getInsets() {
		return insets;
	}

	public void setInsets(Insets insets) {
		this.insets = insets;
	}

	public boolean hasPreferedSize() {
		return !preferedSize.isEmpty();
	}

	public Rectanglec getPreferedSize() {
		return preferedSize;
	}

	public void setPreferedSize(Rectanglec preferedSize) {
		this.preferedSize.set(preferedSize);
	}

	public NVGColor getBackground() {
		return background;
	}

	public void setBackground(NVGColor color) {
		background.set(color);
	}

	public void setBackground(ColorSetter color) {
		color.set(background);
	}

	public NVGColor getForeground() {
		return foreground;
	}

	public void setForeground(NVGColor color) {
		foreground.set(color);
	}

	public void setForeground(ColorSetter color) {
		color.set(foreground);
	}

	public boolean isDisabled() {
		return disabled || (parent != null ? parent.isDisabled() : false);
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isHovered() {
		return hovered;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isGlass() {
		return isGlass;
	}

	public void setGlass(boolean isGlass) {
		this.isGlass = isGlass;
	}

	public void addKeyBinding(KeyBinding kb) {
		bindings.add(kb);
	}

	public void removeKeyBinding(KeyBinding kb) {
		bindings.remove(kb);
	}

	public DropTargetListener setDropTargetListener(DropTargetListener dropListener) {
		DropTargetListener old = this.dropListener;
		this.dropListener = dropListener;
		return old;
	}

	public MouseListener setMouseListener(MouseListener mouseListener) {
		MouseListener old = this.mouseListener;
		this.mouseListener = mouseListener;
		return old;
	}

	public KeyboardListener setKeyboardListener(KeyboardListener keyboardListener) {
		KeyboardListener old = this.keyboardListener;
		this.keyboardListener = keyboardListener;
		return old;
	}

	public void add(Component child) {
		if (child.parent != null || child == this) {
			throw new IllegalStateException("Cannot add this component.");
		}
		children.add(child);
		child.parent = this;
	}

	public void remove(Component child) {
		if (children.remove(child)) {
			// The child component was owned by this component
			child.parent = null;
		}
	}

	/**
	 * Default layout is {@code DynamicRow} with a minimal component width of 0
	 * 
	 * @param layout
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public void setUI(ComponentUI componentUI) {
		this.componentUI = componentUI;
		componentUI.installUI(this);
	}

}
