package utils;

import static org.lwjgl.nanovg.NanoVG.*;

import org.lwjgl.nanovg.NVGColor;

/**
 * Color utilities.
 * <p>
 * This class use NanoVG's functions therefore doing :
 * 
 * <pre>
 * NVGColor color = NVGColor.create();
 * color.r(0.45f).g(0.23f).b(0.24f).a(1f);
 * ...
 * </pre>
 * 
 * is slower than doing :
 * 
 * <pre>
 * NVGColor color = NVGColor.create();
 * Colors.rgba(0.45f, 0.23f, 0.24f, 1f, color);
 * </pre>
 * 
 * which is equivalent to :
 * 
 * <pre>
 * NVGColor color = NVGColor.create();
 * NanoVG.nvgRGBAf(0.45f, 0.23f, 0.24f, 1f, color);
 * </pre>
 * </p>
 * 
 * @author Niels
 *
 */
public final class Colors {

	/**
	 * Sets color to <b style="color: white;">white</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(1f, 1f, 1f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter white = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(1f, 1f, 1f, 1f, color);
		}
	};

	/**
	 * Sets color to <b style="color: lightGray;">lightGray</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0.753f, 0.753f, 0.753f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter lightGray = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.753f, 0.753f, 0.753f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: gray;">gray</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0.502f, 0.502f, 0.502f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter gray = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.502f, 0.502f, 0.502f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: darkGray;">darkGray</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0.251f, 0.251f, 0.251f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter darkGray = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.251f, 0.251f, 0.251f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: black;">black</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0f, 0f, 0f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter black = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.000f, 0.000f, 0.000f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: red;">red</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(1f, 0f, 0f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter red = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(1.000f, 0.000f, 0.000f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: pink;">pink</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(1f, 0.686f, 0.686f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter pink = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(1.000f, 0.686f, 0.686f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: orange;">orange</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(1f, 0.784f, 0f, 1f, color);
	 * </pre>
	 */
	public static final ColorSetter orange = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(1.000f, 0.784f, 0.000f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: yellow;">yellow</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(1f, 1f, 0f, 1f, color);
	 * </pre>
	 * 
	 */
	public static final ColorSetter yellow = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(1.000f, 1.000f, 0.000f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: green;">green</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0f, 1f, 0f, 1f, color);
	 * </pre>
	 * 
	 */
	public static final ColorSetter green = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.000f, 1.000f, 0.000f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: magenta;">magenta</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(1f, 0f, 1f, 1f, color);
	 * </pre>
	 * 
	 */
	public static final ColorSetter magenta = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(1.000f, 0.000f, 1.000f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: cyan;">cyan</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0f, 1f, 1f, 1f, color);
	 * </pre>
	 * 
	 */
	public static final ColorSetter cyan = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.000f, 1.000f, 1.000f, 1.000f, color);
		}
	};

	/**
	 * Sets color to <b style="color: blue;">blue</b>.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0f, 0f, 1f, 1f, color);
	 * </pre>
	 * 
	 */
	public static final ColorSetter blue = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.000f, 0.000f, 1.000f, 1.000f, color);
		}
	};

	/**
	 * Sets all of color's components to 0f.<br>
	 * equivalent to :
	 * 
	 * <pre>
	 * rgba(0f, 0f, 0f, 0f, color);
	 * </pre>
	 * 
	 */
	public static final ColorSetter transparent = new ColorSetter() {
		public NVGColor set(NVGColor color) {
			return nvgRGBAf(0.000f, 0.000f, 0.000f, 0.000f, color);
		}
	};

	/**
	 * Returns a color value from red, green, blue and alpha values.
	 *
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @param a the alpha value
	 * @return {@code color}
	 */
	public static NVGColor rgba(byte r, byte g, byte b, byte a, NVGColor color) {
		return nvgRGBA(r, g, b, a, color);
	}

	/**
	 * Returns a color value from red, green, blue and alpha values.
	 *
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @param a the alpha value
	 * @return {@code color}
	 */
	public static NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
		return rgba(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f, color);
	}

	/**
	 * Returns a color value from red, green, blue and alpha values.
	 *
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @param a the alpha value
	 * @return {@code color}
	 */
	public static NVGColor rgba(float r, float g, float b, float a, NVGColor color) {
		return nvgRGBAf(r, g, b, a, color);
	}

	/**
	 * Returns a color value from red, green, blue values. Alpha will be set to
	 * {@code 255}.
	 *
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @return {@code color}
	 */
	public static NVGColor rgb(byte r, byte g, byte b, NVGColor color) {
		return nvgRGB(r, g, b, color);
	}

	/**
	 * Returns a color value from red, green, blue values. Alpha will be set to
	 * {@code 255}.
	 *
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @return {@code color}
	 */
	public static NVGColor rgb(int r, int g, int b, NVGColor color) {
		return rgb(r / 255.0f, g / 255.0f, b / 255.0f, color);
	}

	/**
	 * Returns a color value from red, green, blue values. Alpha will be set to
	 * {@code 1f}.
	 *
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @return {@code color}
	 */
	public static NVGColor rgb(float r, float g, float b, NVGColor color) {
		return nvgRGBf(r, g, b, color);
	}

	/**
	 * Returns color value specified by hue, saturation and lightness.
	 * 
	 * <p>
	 * HSL values are all in range {@code [0..1]}, alpha will be set to {@code 1f}.
	 * </p>
	 *
	 * @param h the hue value
	 * @param s the saturation value
	 * @param l the lightness value
	 * @return {@code color}
	 */
	public static NVGColor hsl(float h, float s, float l, NVGColor color) {
		return nvgHSL(h, s, l, color);
	}

	/**
	 * Returns color value specified by hue, saturation and lightness and alpha.
	 * 
	 * <p>
	 * hsla values are all in range {@code [0..1]}
	 * </p>
	 *
	 * @param h     the hue value
	 * @param s     the saturation value
	 * @param l     the lightness value
	 * @param a     the alpha value
	 * @param color
	 * @return {@code color}
	 */
	public static NVGColor hsla(float h, float s, float l, float a, NVGColor color) {
		return nvgHSLA(h, s, l, (byte) (a * 255f + 0.5f), color);
	}

	/**
	 * Converts a {@code String} to an integer and returns the specified opaque
	 * {@code Color}. This method handles string formats that are used to represent
	 * octal and hexadecimal numbers.
	 * 
	 * @param nm    a {@code String} that represents an opaque color as a 24-bit
	 *              integer
	 * @param color
	 * @return {@code color}
	 */
	public static NVGColor decode(String nm, NVGColor color) {
		Integer intval = Integer.decode(nm);
		int i = intval.intValue();
		return rgb((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF, color);
	}

	/**
	 * Converts a {@code String} to an integer and returns the specified opaque
	 * {@code Color}. This method handles string formats that are used to represent
	 * octal and hexadecimal numbers.
	 * 
	 * @param nm    a {@code String} that represents an opaque color as a 24-bit
	 *              integer
	 * @param color
	 * @param alpha the alpha value
	 * @return {@code color}
	 */
	public static NVGColor decode(String nm, int alpha, NVGColor color) {
		Integer intval = Integer.decode(nm);
		int i = intval.intValue();
		return rgba((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF, alpha, color);
	}

	/**
	 * Lerps from {@code a} to {@code b} by an interpolation factor {@code u} and
	 * stores the result into dest.
	 * 
	 * @param a
	 * @param b
	 * @param dest
	 * @param u
	 * @return {@code dest}
	 */
	public static NVGColor lerp(NVGColor a, NVGColor b, NVGColor dest, float u) {
		return nvgLerpRGBA(a, b, u, dest);
	}

	/**
	 * @param color
	 * @return true if {@code color} is completely transparent(i.e.
	 *         {@code color.a() == 0}), false otherwise;
	 */
	public static boolean isTransparent(NVGColor color) {
		return color.a() == 0;
	}
}
