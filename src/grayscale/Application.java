package grayscale;

import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import events.Keyboard;
import events.Mouse;
import utils.Colors;
import utils.FrameBufferObject;
import utils.FrameBufferObject.AttachmentFormat;

public abstract class Application {
	protected final long window;
	protected Vector windowSize = new Vector();
	protected Vector contentSize = new Vector();
	protected Vector contentScale = new Vector();
	protected Mouse mouse;
	protected Keyboard keyboard;

	private double frameTime = 0;
	private NVGColor clearColor = NVGColor.create();
	private List<Font> fonts = new ArrayList<>();
	private List<Image> images = new ArrayList<>();
	protected Graphics graphics;

	private static FontStyle defaultFontStyle = new FontStyle("", 10f);

	private List<Window> windows = new ArrayList<>();
	private List<Window> windowsToAdd = new ArrayList<>();

	/**
	 * <ul>
	 * <li>{@link #ARROW_CURSOR } The regular arrow cursor shape.</li>
	 * <li>{@link #IBEAM_CURSOR } The text input I-beam cursor shape.</li>
	 * <li>{@link #CROSSHAIR_CURSOR } The crosshair cursor shape.</li>
	 * <li>{@link #POINTING_HAND_CURSOR} The pointing hand cursor shape.</li>
	 * <li>{@link #RESIZE_EW_CURSOR } The horizontal resize/move arrow shape.</li>
	 * <li>{@link #RESIZE_NS_CURSOR } The vertical resize/move shape.</li>
	 * <li>{@link #RESIZE_NWSE_CURSOR } The top-left to bottom-right diagonal
	 * resize/move shape.</li>
	 * <li>{@link #RESIZE_NESW_CURSOR } The top-right to bottom-left diagonal
	 * resize/move shape.</li>
	 * <li>{@link #RESIZE_ALL_CURSOR } The omni-directional resize cursor/move
	 * shape.</li>
	 * <li>{@link #NOT_ALLOWED_CURSOR } The operation-not-allowed shape.</li>
	 * </ul>
	 * 
	 * @see #setCursor(long)
	 */
	public final long ARROW_CURSOR, //
			IBEAM_CURSOR, //
			CROSSHAIR_CURSOR, //
			POINTING_HAND_CURSOR, //
			RESIZE_EW_CURSOR, //
			RESIZE_NS_CURSOR, //
			RESIZE_NWSE_CURSOR, //
			RESIZE_NESW_CURSOR, //
			RESIZE_ALL_CURSOR, //
			NOT_ALLOWED_CURSOR;

	public Application(String title, int width, int height) {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW");

		window = createWindow(title, width, height);

		if (window == NULL) {
			glfwTerminate();
			throw new NullPointerException("Window pointer is NULL");
		}
		initCallBacks();

		ARROW_CURSOR = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		IBEAM_CURSOR = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
		CROSSHAIR_CURSOR = glfwCreateStandardCursor(GLFW_CROSSHAIR_CURSOR);
		POINTING_HAND_CURSOR = glfwCreateStandardCursor(GLFW_POINTING_HAND_CURSOR);
		RESIZE_EW_CURSOR = glfwCreateStandardCursor(GLFW_RESIZE_EW_CURSOR);
		RESIZE_NS_CURSOR = glfwCreateStandardCursor(GLFW_RESIZE_NS_CURSOR);
		RESIZE_NWSE_CURSOR = glfwCreateStandardCursor(GLFW_RESIZE_NWSE_CURSOR);
		RESIZE_NESW_CURSOR = glfwCreateStandardCursor(GLFW_RESIZE_NESW_CURSOR);
		RESIZE_ALL_CURSOR = glfwCreateStandardCursor(GLFW_RESIZE_ALL_CURSOR);
		NOT_ALLOWED_CURSOR = glfwCreateStandardCursor(GLFW_NOT_ALLOWED_CURSOR);

		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwShowWindow(window);
		glfwSwapInterval(1);

		windowSize.set(width, height);

		int[] fw = new int[1], fh = new int[1];
		glfwGetFramebufferSize(window, fw, fh);
		contentSize.set(fw[0], fh[0]);

		float[] sx = new float[1], sy = new float[1];
		glfwGetWindowContentScale(window, sx, sy);
		contentScale.set(sx[0], sy[0]);
	}

	/**
	 * may be overriden
	 * 
	 * @param title
	 * @param width
	 * @param height
	 * @return the handle of the created window
	 */
	protected long createWindow(String title, int width, int height) {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
		glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
		glfwWindowHint(GLFW_SAMPLES, 8);

		long handle = glfwCreateWindow(width, height, title, NULL, NULL);

		if (handle == NULL) {
			glfwTerminate();
			throw new RuntimeException("could not create window");
		}
		return handle;
	}

	private void initCallBacks() {
		mouse = new Mouse(this);
		keyboard = new Keyboard(this);

		glfwSetWindowSizeCallback(window, (handle, w, h) -> {
			windowSize.set(w, h);
		});

		glfwSetFramebufferSizeCallback(window, (handle, w, h) -> {
			contentSize.set(w, h);
		});

		glfwSetWindowContentScaleCallback(window, (handle, xscale, yscale) -> {
			contentScale.set(xscale, yscale);
		});
	}

	/**
	 * @param cursor one of :
	 *               <p>
	 *               <ul>
	 *               <li>{@link #ARROW_CURSOR } The regular arrow cursor shape.</li>
	 *               <li>{@link #IBEAM_CURSOR } The text input I-beam cursor
	 *               shape.</li>
	 *               <li>{@link #CROSSHAIR_CURSOR } The crosshair cursor shape.</li>
	 *               <li>{@link #POINTING_HAND_CURSOR} The pointing hand cursor
	 *               shape.</li>
	 *               <li>{@link #RESIZE_EW_CURSOR } The horizontal resize/move arrow
	 *               shape.</li>
	 *               <li>{@link #RESIZE_NS_CURSOR } The vertical resize/move
	 *               shape.</li>
	 *               <li>{@link #RESIZE_NWSE_CURSOR } The top-left to bottom-right
	 *               diagonal resize/move shape.</li>
	 *               <li>{@link #RESIZE_NESW_CURSOR } The top-right to bottom-left
	 *               diagonal resize/move shape.</li>
	 *               <li>{@link #RESIZE_ALL_CURSOR } The omni-directional resize
	 *               cursor/move shape.</li>
	 *               <li>{@link #NOT_ALLOWED_CURSOR } The operation-not-allowed
	 *               shape.</li>
	 *               </ul>
	 *               </p>
	 */
	public void setCursor(long cursor) {
		glfwSetCursor(window, cursor);
	}

	public void loadFont(String name, String filePath) {
		fonts.add(new Font(name, filePath));
	}

	public Image loadImage(String path, int imageFlags) {
		Image image = new Image(path, imageFlags);
		images.add(image);
		return image;
	}

	public void addWindow(Window window) {
		windowsToAdd.add(window);
	}

	/**
	 * Runs this application.<br>
	 * Returns once the app is closed or an exception has occured.
	 */
	public void run() {
		init();
		graphics = new Graphics(Graphics.ANTIALIAS, fonts);
		while (!shouldClose()) {
			privateUpdate();
			privateRender();
		}
		privateDestroy();
		destroy();
	}

	protected abstract boolean shouldClose();

	protected abstract void init();

	protected void privateUpdate() {
		frameTime = glfwGetTime();
		glfwSetTime(0);
		setCursor(ARROW_CURSOR);

		mouse.prepare();
		keyboard.prepare();

		glfwPollEvents();
		update();

		windows.addAll(windowsToAdd);
		windowsToAdd.clear();

		List<Window> windowsToRemove = new ArrayList<>();

		for (Window w : windows) {
			w.update(mouse, keyboard);
			if (w.shouldClose()) {
				windowsToRemove.add(w);
			}
		}

		windows.removeAll(windowsToRemove);
		windowsToRemove.clear();
	}
	
	protected abstract void update();

	protected void privateRender() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glViewport(0, 0, Math.round(contentSize.x()), Math.round(contentSize.y()));
		GL11.glClearColor(clearColor.r(), clearColor.g(), clearColor.b(), 1f);
		render();

		graphics.beginFrame(contentSize, contentScale);
		for (Window w : windows) {
			w.render(graphics);
		}

		graphics.endFrame();

		glfwSwapBuffers(window);
	}
	
	protected abstract void render();

	private final void privateDestroy() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		graphics.destroy();

		for (Image i : images) {
			i.delete();
		}

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	protected abstract void destroy();

	/**
	 * @return the glfw window
	 */
	public long getHandle() {
		return window;
	}

	public Vectorc getContentSize() {
		return contentSize;
	}

	public Vectorc getContentScale() {
		return contentScale;
	}

	public void setBackground(float r, float g, float b) {
		Colors.rgb(r, g, b, clearColor);
	}

	/**
	 * @return The time the last frame took to render, in seconds.
	 *         <p>
	 *         <strong>Note</strong> : <br>
	 *         The frame time is measured with {@link GLFW#glfwGetTime()
	 *         glfwGetTime()}
	 *         </p>
	 */
	public double getFrameTime() {
		return frameTime;
	}

	// Easier to set it to static
	public static FontStyle getDefaultFontStyle() {
		return defaultFontStyle;
	}

	public static void setDefaultFontStyle(FontStyle fontStyle) {
		defaultFontStyle = fontStyle;
	}
}
