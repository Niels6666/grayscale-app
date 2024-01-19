package events;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.system.MemoryStack.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import grayscale.Application;
import grayscale.Vector;
import grayscale.Vectorc;

public class Mouse {
	public static final int BTN_RIGHT = GLFW_MOUSE_BUTTON_RIGHT;
	public static final int BTN_MIDDLE = GLFW_MOUSE_BUTTON_MIDDLE;
	public static final int BTN_LEFT = GLFW_MOUSE_BUTTON_LEFT;

	private Vector pos = new Vector();
	private Vector prevPos = new Vector();
	private Vector scroll = new Vector();

	private Vector LPressedStartPos = new Vector();
	private Vector WPressedStartPos = new Vector();
	private Vector RPressedStartPos = new Vector();

	// Left button
	private boolean pressedL = false;
	private boolean clickedL = false;
	private boolean draggedL = false;
	private boolean releasedL = false;

	// Mouse wheel
	private boolean pressedW = false;
	private boolean clickedW = false;
	private boolean draggedW = false;
	private boolean releasedW = false;

	// Rigth button
	private boolean pressedR = false;
	private boolean clickedR = false;
	private boolean draggedR = false;
	private boolean releasedR = false;

	private boolean moved = false;

	private int modifiers = 0;

	private List<String> droppedFiles = new ArrayList<>();

	private final Application app;

	public Mouse(Application application) {
		this.app = application;
		long w = application.getHandle();
		glfwSetCursorPosCallback(w, this::GLFWCursorPosCallback);
		glfwSetScrollCallback(w, this::GLFWScrollCallback);
		glfwSetMouseButtonCallback(w, this::GLFWMouseButtonCallback);
		glfwSetDropCallback(w, this::GLFWDropCallback);
	}

	private void GLFWCursorPosCallback(long window, double xpos, double ypos) {
		pos.set(xpos, ypos).div(app.getContentScale());

		moved = true;

		if (glfwGetMouseButton(window, BTN_LEFT) == GLFW_PRESS) {
			draggedL = true;
		}

		if (glfwGetMouseButton(window, BTN_MIDDLE) == GLFW_PRESS) {
			draggedW = true;
		}

		if (glfwGetMouseButton(window, BTN_RIGHT) == GLFW_PRESS) {
			draggedR = true;
		}
	}

	private void GLFWScrollCallback(long window, double xoffset, double yoffset) {
		scroll.set(xoffset, yoffset);
	}

	private void GLFWMouseButtonCallback(long window, int button, int action, int mods) {
		Vector currentPos = new Vector();
		try (MemoryStack stack = stackPush()) {
			DoubleBuffer x = stack.mallocDouble(1);
			DoubleBuffer y = stack.mallocDouble(1);
			glfwGetCursorPos(window, x, y);
			currentPos.set(x.get(0), y.get(0)).div(app.getContentScale());
		}

		modifiers = mods;

		if (button == GLFW_MOUSE_BUTTON_RIGHT) {
			if (action == GLFW_RELEASE) {
				pressedR = false;
				clickedR = !draggedR;
				draggedR = false;
				releasedR = true;
			} else {
				pressedR = true;
				RPressedStartPos.set(currentPos);
			}
		}

		if (button == GLFW_MOUSE_BUTTON_MIDDLE) {
			if (action == GLFW_RELEASE) {
				pressedW = false;
				clickedW = !draggedW;
				draggedW = false;
				releasedW = true;
			} else {
				pressedW = true;
				WPressedStartPos.set(currentPos);
			}
		}

		if (button == GLFW_MOUSE_BUTTON_LEFT) {
			if (action == GLFW_RELEASE) {
				pressedL = false;
				clickedL = !draggedL;
				draggedL = false;
				releasedL = true;
			} else {
				pressedL = true;
				LPressedStartPos.set(currentPos);
			}
		}
	}

	private void GLFWDropCallback(long window, int count, long names) {
		PointerBuffer namesBuff = PointerBuffer.create(names, count);
		for (int index = 0; index < count; index++) {
			droppedFiles.add(namesBuff.getStringUTF8(index));
		}
	}

	/**
	 * Prepares this mouse for the next callback.<br>
	 * Must be called before glfwPollEvents
	 */
	public void prepare() {
		pressedL = false;
		pressedW = false;
		pressedR = false;
		releasedL = false;
		releasedW = false;
		releasedR = false;
		clickedL = false;
		clickedW = false;
		clickedR = false;
		moved = false;
		modifiers = 0;
		scroll.set(0);
		prevPos.set(pos);

		droppedFiles.clear();
	}

	public Vectorc getPos() {
		return pos;
	}

	public Vectorc getPrevPos() {
		return prevPos;
	}

	public Vectorc getRPressedStartPos() {
		return RPressedStartPos;
	}

	public Vectorc getWPressedStartPos() {
		return WPressedStartPos;
	}

	public Vectorc getLPressedStartPos() {
		return LPressedStartPos;
	}

	public Vectorc getScroll() {
		return scroll;
	}

	public boolean pressedL() {
		return pressedL;
	}

	public boolean pressedR() {
		return pressedR;
	}

	public boolean pressedW() {
		return pressedW;
	}

	public boolean clickedL() {
		return clickedL;
	}

	public boolean clickedR() {
		return clickedR;
	}

	public boolean clickedW() {
		return clickedW;
	}

	public boolean draggedL() {
		return draggedL && moved;
	}

	public boolean draggedR() {
		return draggedR && moved;
	}

	public boolean draggedW() {
		return draggedW && moved;
	}

	public boolean releasedL() {
		return releasedL;
	}

	public boolean releasedR() {
		return releasedR;
	}

	public boolean releasedW() {
		return releasedW;
	}

	public boolean isMoved() {
		return moved;
	}

	public int getModifiers() {
		return modifiers;
	}

	public List<String> consumeDrop() {
		return droppedFiles;
	}

}
