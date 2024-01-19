package demo;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL46C;
import org.scilab.forge.jlatexmath.Atom;
import org.scilab.forge.jlatexmath.Box;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXEnvironment;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import components.Button;
import components.Label;
import components.Panel;
import events.Keyboard;
import events.Mouse;
import grayscale.Application;
import grayscale.FontStyle;
import grayscale.Graphics;
import grayscale.Image;
import grayscale.Insets;
import grayscale.Rectangle;
import grayscale.Vector;
import grayscale.Vectorc;
import grayscale.Window;
import utils.Colors;
import utils.FrameBufferObject;
import utils.FrameBufferObject.AttachmentFormat;
import utils.FrameBufferObject.FBOAttachment;
import utils.Texture;

public class Example extends Application {

	private Rectangle windowB = new Rectangle();
	private Window window = new Window(windowB);
	private Graphics g2d = new Graphics(Graphics.ANTIALIAS, List.of());

	public Example() {
		super("Demo", 800, 600);
	}

	public static void main(String[] args) {
		Example demo = new Example();
		demo.run();
	}

	@Override
	protected boolean shouldClose() {
		return glfwWindowShouldClose(this.getHandle());
	}

	@Override
	protected void init() {
		setBackground(1f, 1f, 1f);
		loadFont("segoeui", "demo/segoeui.ttf");
		setDefaultFontStyle(new FontStyle("segoeui", 500f));
		
		Label content = new Label("plop !");
		window.setContent(content);
		addWindow(window);
	}

	@Override
	protected void update() {
		Vector dim = new Vector(contentSize).div(contentScale);
		window.setBounds(windowB.set(0, 0, dim.x(), dim.y()));
	}

	@Override
	protected void render() {
	}

	@Override
	protected void destroy() {
		System.out.println("destroying app...");
	}

}
