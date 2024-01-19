package events;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import grayscale.Application;

public class Keyboard {
	public static final int KEY_NONE = -1, //
			KEY_DEL = 0, //
			KEY_ENTER = 1, //
			KEY_BACKSPACE = 3, //
			KEY_COPY = 4, //
			KEY_CUT = 5, //
			KEY_PASTE = 6, //
			KEY_LEFT = 7, //
			KEY_RIGHT = 8, //
			KEY_TEXT_SELECT_ALL = 11, //
			KEY_TEXT_WORD_LEFT = 12, //
			KEY_TEXT_WORD_RIGHT = 13, //
			KEY_START = 14, //
			KEY_END = 15; //

	private int editStringKey = KEY_NONE;
	private boolean replaceMode = false;
	private String typedChars = "";

	private List<KeyEvent> events = new ArrayList<>();

	private final Application app;

	public Keyboard(Application application) {
		this.app = application;
		glfwSetCharCallback(app.getHandle(), this::GLFWCharCallback);
		glfwSetKeyCallback(app.getHandle(), this::GLFWKeyCallback);
	}

	private void GLFWCharCallback(long window, int codepoint) {
		typedChars += Character.toString(codepoint);
	}

	/**
	 * Will be called when a key is pressed, repeated or released.
	 *
	 * @param window   the window that received the event
	 * @param key      the keyboard key that was pressed or released
	 * @param scancode the platform-specific scancode of the key
	 * @param action   the key action. One of:<br>
	 *                 <table>
	 *                 <tr>
	 *                 <td>{@link GLFW#GLFW_PRESS PRESS}</td>
	 *                 <td>{@link GLFW#GLFW_RELEASE RELEASE}</td>
	 *                 <td>{@link GLFW#GLFW_REPEAT REPEAT}</td>
	 *                 </tr>
	 *                 </table>
	 * @param mods     bitfield describing which modifiers keys were held down
	 */
	private void GLFWKeyCallback(long window, int key, int scancode, int action, int mods) {
		events.add(new KeyEvent(key, scancode, action, mods));
		
		boolean press = action == GLFW_PRESS;
		boolean repeated = action == GLFW_REPEAT;
		boolean released = action == GLFW_RELEASE;

		switch (key) {
		case GLFW_KEY_ESCAPE:
			if (released) {
				glfwSetWindowShouldClose(window, true);
			}
			break;
		case GLFW_KEY_INSERT:
			if (press || repeated) {
				replaceMode = !replaceMode;
			}
			break;
		case GLFW_KEY_DELETE:
			if (press || repeated) {
				editStringKey = KEY_DEL;
			}
			break;
		case GLFW_KEY_KP_ENTER:
		case GLFW_KEY_ENTER:
			if (released) {
				editStringKey = KEY_ENTER;
			}
			break;
		case GLFW_KEY_TAB:
			break;
		case GLFW_KEY_BACKSPACE:
			if (press || repeated) {
				editStringKey = KEY_BACKSPACE;
			}
			break;
		case GLFW_KEY_LEFT:
			if (press || repeated) {
				if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
					editStringKey = KEY_TEXT_WORD_LEFT;
				} else {
					editStringKey = KEY_LEFT;
				}
			}
			break;
		case GLFW_KEY_RIGHT:
			if (press || repeated) {
				if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
					editStringKey = KEY_TEXT_WORD_RIGHT;
				} else {
					editStringKey = KEY_RIGHT;
				}
			}
			break;
		case GLFW_KEY_HOME:
			if (released) {
				editStringKey = KEY_START;
			}
			break;
		case GLFW_KEY_END:
			if (released) {
				editStringKey = KEY_END;
			}
			break;
		case GLFW_KEY_A:
			if (press && glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
				editStringKey = KEY_TEXT_SELECT_ALL;
			}
			break;
		case GLFW_KEY_C:
			if (press && glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
				editStringKey = KEY_COPY;
			}
			break;
		case GLFW_KEY_V:
			if (press && glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
				editStringKey = KEY_PASTE;
			}
			break;
		case GLFW_KEY_X:
			if (press && glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
				editStringKey = KEY_CUT;
			}
			break;
		}
	}

	/**
	 * Prepares this keyboard for the next callback.<br>
	 * Must be called before glfwPollEvents
	 */
	public void prepare() {
		editStringKey = KEY_NONE;
		typedChars = "";
		events.clear();
	}

	public int getEditStringKey() {
		return editStringKey;
	}

	public boolean getReplaceMode() {
		return replaceMode;
	}

	public String getTypedChars() {
		return typedChars;
	}

	public boolean keyPressed(int keyCode) {
		return glfwGetKey(app.getHandle(), keyCode) == GLFW_PRESS;
	}

	public List<KeyEvent> getEvents() {
		return events;
	}
}
