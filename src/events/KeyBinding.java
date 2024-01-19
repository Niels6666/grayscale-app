package events;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFW;

import grayscale.Component;

public class KeyBinding {
	public static final int CONDITION_FOCUSED = 1, CONDITION_FOCUSED_OR_DESCENDANT_FOCUSED = 2;

	public final int key;
	public final int keyAction;
	public final int mods;

	public final int condition;

	private final Runnable action;

	/**
	 * @param key       a glfw key
	 * @param keyAction can be {@link GLFW#GLFW_DONT_CARE}
	 * @param mods      can be {@link GLFW#GLFW_DONT_CARE}
	 */
	public KeyBinding(int key, int keyAction, int mods, int condition, Runnable action) {
		this.key = key;
		this.keyAction = keyAction;
		this.mods = mods;
		this.condition = condition;
		this.action = action;
	}

	/**
	 * @param key
	 * @param action
	 * @param mods
	 * @return true if the action mapped to this {@code KeyBinding} must be
	 *         performed with {@link KeyBinding#performAction(int, int, int, int)}
	 */
	public final boolean test(int key, int action, int mods, boolean focused, boolean descendant_focused) {
		boolean cond1 = this.key == key;
		boolean cond2 = (this.keyAction == GLFW_DONT_CARE) || (this.keyAction == action);
		boolean cond3 = (this.mods == GLFW_DONT_CARE) || (this.mods == mods);
		boolean cond4 = (condition == CONDITION_FOCUSED) ? focused : (focused || descendant_focused);
		return cond1 && cond2 && cond3 && cond4;
	}

	public void performAction() {
		action.run();
	}
}
