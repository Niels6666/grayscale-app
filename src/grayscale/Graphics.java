package grayscale;

import static org.lwjgl.nanovg.NanoVG.*;

import static org.lwjgl.nanovg.NanoVGGL3.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGGlyphPosition;
import org.lwjgl.nanovg.NVGLUFramebuffer;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NVGTextRow;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;

import utils.ColorSetter;

/**
 * <p>
 * This {@code Graphics} class uses NanoVG for the rendering.
 * </p>
 * 
 * @author Niels
 *
 */
public class Graphics {
	/**
	 * NanoVG Create flags.
	 * 
	 * <h5>Enum values:</h5>
	 * 
	 * <ul>
	 * <li>{@link #ANTIALIAS} - Flag indicating if geometry based anti-aliasing is
	 * used (may not be needed when using MSAA).</li>
	 * <li>{@link #STENCIL_STROKES} - Flag indicating if strokes should be drawn
	 * using stencil buffer. The rendering will be a little slower, but path
	 * overlaps (i.e. self-intersecting or sharp turns) will be drawn just
	 * once.</li>
	 * <li>{@link #DEBUG} - Flag indicating that additional debug checks are
	 * done.</li>
	 * </ul>
	 * 
	 * @see {@link NanoVGGL3#nvgCreate(int)}
	 */
	public static final int ANTIALIAS = NVG_ANTIALIAS, //
			STENCIL_STROKES = NVG_STENCIL_STROKES, //
			DEBUG = NVG_DEBUG;

	/**
	 * NanoVG Composite operations.
	 * 
	 * @see {@link NanoVG#NVG_SOURCE_OVER operations}
	 */
	public static final int SOURCE_OVER = NVG_SOURCE_OVER, //
			SOURCE_IN = NVG_SOURCE_IN, //
			SOURCE_OUT = NVG_SOURCE_OUT, //
			ATOP = NVG_ATOP, //
			DESTINATION_OVER = NVG_DESTINATION_OVER, //
			DESTINATION_IN = NVG_DESTINATION_IN, //
			DESTINATION_OUT = NVG_DESTINATION_OUT, //
			DESTINATION_ATOP = NVG_DESTINATION_ATOP, //
			LIGHTER = NVG_LIGHTER, //
			COPY = NVG_COPY, //
			XOR = NVG_XOR;

	/**
	 * NanoVG Blend factors.
	 * 
	 * @see {@link NanoVG#NVG_ZERO factors}
	 */
	public static final int ZERO = NVG_ZERO, //
			ONE = NVG_ONE, //
			SRC_COLOR = NVG_SRC_COLOR, //
			ONE_MINUS_SRC_COLOR = NVG_ONE_MINUS_SRC_COLOR, //
			DST_COLOR = NVG_DST_COLOR, //
			ONE_MINUS_DST_COLOR = NVG_ONE_MINUS_DST_COLOR, //
			SRC_ALPHA = NVG_SRC_ALPHA, //
			ONE_MINUS_SRC_ALPHA = NVG_ONE_MINUS_SRC_ALPHA, //
			DST_ALPHA = NVG_DST_ALPHA, //
			ONE_MINUS_DST_ALPHA = NVG_ONE_MINUS_DST_ALPHA, //
			SRC_ALPHA_SATURATE = NVG_SRC_ALPHA_SATURATE;

	/**
	 * NanoVG Winding order.
	 * 
	 * <ul>
	 * <li>{@link #CCW} - counter-clockwise</li>
	 * <li>{@link #CW} - clockwise</li>
	 * </ul>
	 */
	public static final int CCW = NVG_CCW, CW = NVG_CW;

	/**
	 * NanoVG Solidity.
	 * 
	 * <ul>
	 * <li>{@link #SOLID}</li>
	 * <li>{@link #HOLE}</li>
	 * </ul>
	 */
	public static final int SOLID = NVG_SOLID, HOLE = NVG_HOLE;

	/**
	 * NanoVG Line caps and joins.
	 */
	public static final int BUTT = NVG_BUTT, //
			ROUND = NVG_ROUND, //
			SQUARE = NVG_SQUARE, //
			BEVEL = NVG_BEVEL, //
			MITER = NVG_MITER;

	/**
	 * NanoVG Alignments.
	 * <ul>
	 * <li>{@link #ALIGN_LEFT} - Default, align text horizontally to left.</li>
	 * <li>{@link #ALIGN_CENTER} - Align text horizontally to center.</li>
	 * <li>{@link #ALIGN_RIGHT} - Align text horizontally to right.</li>
	 * <li>{@link #ALIGN_TOP} - Align text vertically to top.</li>
	 * <li>{@link #ALIGN_MIDDLE} - Align text vertically to middle.</li>
	 * <li>{@link #ALIGN_BOTTOM} - Align text vertically to bottom.</li>
	 * <li>{@link #ALIGN_BASELINE} - Default, align text vertically to
	 * baseline.</li>
	 * </ul>
	 */
	public static final int ALIGN_LEFT = NVG_ALIGN_LEFT, //
			ALIGN_CENTER = NVG_ALIGN_CENTER, //
			ALIGN_RIGHT = NVG_ALIGN_RIGHT, //
			ALIGN_TOP = NVG_ALIGN_TOP, //
			ALIGN_MIDDLE = NVG_ALIGN_MIDDLE, //
			ALIGN_BOTTOM = NVG_ALIGN_BOTTOM, //
			ALIGN_BASELINE = NVG_ALIGN_BASELINE;

	private final long ctx;

	private HashMap<Image, Integer> images = new HashMap<>();

	private NVGColor colorA = NVGColor.create();
	private NVGColor colorB = NVGColor.create();

	private NVGPaint paint = NVGPaint.create();

	private Vector contentScale = new Vector();

	/**
	 * @param flags the context flags. One of:<br>
	 *              <ul>
	 *              <li>{@link #ANTIALIAS}</li>
	 *              <li>{@link #STENCIL_STROKES}</li>
	 *              <li>{@link #DEBUG}</li>
	 *              </ul>
	 */
	public Graphics(int nvgFlags, List<Font> fonts) {
		ctx = nvgCreate(nvgFlags);

		for (Font f : fonts) {
			addFont(f);
		}
	}

	/**
	 * @return a handle to the image, you must delete it manually
	 */
	private int createNVGImage(int textureID, int width, int height, int flags) {
		return nvglCreateImageFromHandle(ctx, textureID, width, height, flags);
	}
//	
//	public void deleteNVGImage(int image) {
//		nvgDeleteImage(ctx, image);
//	}

	public int addImage(Image img) {
		check(img);
		int id = createNVGImage(img.getOpenglID(), img.getWidth(), img.getHeight(), img.getFlags());
		images.put(img, id);
		return id;
	}

	public void removeImage(Image img) {
		nvgDeleteImage(ctx, images.remove(img));
	}

	/**
	 * check to see if there are any dead images that should be removed
	 */
	private void checkImages() {
		List<Image> deadImages = new ArrayList<>();
		images.forEach((im, id) -> {
			if (!im.isAlive()) {
				deadImages.add(im);
			}
		});
		deadImages.forEach(this::removeImage);
	}

	private static void check(Image img) {
		if (!img.isAlive()) {
			throw new IllegalStateException("Trying to use deleted texture !");
		}
	}

	/**
	 * If img was not added, it will create an ID for img and save it for the next
	 * call to this function.<br>
	 * This function also checks for dead images and delete the nanoVG id associated
	 * with them.
	 * 
	 * @param img
	 * @return the nanoVG id associated with img.
	 * @throws IllegalStateException if img is not alive
	 */
	private int getID(Image img) {
		check(img);
		checkImages();
		Integer id = images.get(img);
		return id == null ? addImage(img) : id.intValue();
	}

	public void addFont(Font font) {
		nvgCreateFontMem(ctx, font.getName(), font.getData(), false);
	}

	/**
	 * Creates a framebuffer object to render to.
	 */
	public NVGLUFramebuffer createFBO(int width, int height, int flags) {
		return nvgluCreateFramebuffer(ctx, width, height, flags);
	}

	/**
	 * Binds the framebuffer object associated with the specified NVGLUFramebuffer.
	 * 
	 * @param fbo the framebuffer to bind
	 */
	public void bindFBO(NVGLUFramebuffer fbo) {
		nvgluBindFramebuffer(ctx, fbo);
	}

	/**
	 * Deletes an NVGLUFramebuffer.
	 * 
	 * @param fbo the framebuffer to delete
	 */
	public void deleteFBO(NVGLUFramebuffer fbo) {
		nvgluDeleteFramebuffer(ctx, fbo);
	}

	/**
	 * Begins drawing a new frame.
	 * <p>
	 * Calls to the drawing functions of this class must be wrapped in
	 * {@link #beginFrame(float, float, float)} and {@link #endFrame()} as it
	 * delegates everything to nanovg drawing API.
	 * </p>
	 * 
	 * @param contentSize  the dimensions of the canvas
	 * @param contentScale the content scale
	 * @see {@link GLFW#glfwGetWindowContentScale(long, FloatBuffer, FloatBuffer)}
	 */
	public void beginFrame(Vectorc contentSize, Vectorc contentScale) {
		Vector dim = new Vector(contentSize).div(contentScale);
		float dpr = Math.max(contentScale.x(), contentScale.y());
		nvgBeginFrame(ctx, dim.x(), dim.y(), dpr);
		this.contentScale.set(contentScale);
	}

	/**
	 * Ends drawing flushing remaining render state.
	 */
	public void endFrame() {
		nvgEndFrame(ctx);
	}

	/**
	 * Sets the composite operation.
	 *
	 * @param op the composite operation. One of:<br>
	 *           <table>
	 *           <tr>
	 *           <td>{@link #SOURCE_OVER }</td>
	 *           <td>{@link #SOURCE_IN }</td>
	 *           <td>{@link #SOURCE_OUT }</td>
	 *           <td>{@link #ATOP }</td>
	 *           <td>{@link #DESTINATION_OVER }</td>
	 *           <td>{@link #DESTINATION_IN }</td>
	 *           <td>{@link #ESTINATION_OUT }</td>
	 *           <td>{@link #DESTINATION_ATOP }</td>
	 *           </tr>
	 *           <tr>
	 *           <td>{@link #LIGHTER}</td>
	 *           <td>{@link #COPY}</td>
	 *           <td>{@link #XOR}</td>
	 *           </tr>
	 *           </table>
	 */
	public void setGlobalCompositeOperation(int op) {
		nvgGlobalCompositeOperation(ctx, op);
	}

	/**
	 * Sets the composite operation with custom pixel arithmetic.
	 *
	 * @param sfactor the source blend factor.
	 * @param dfactor the destination blend factor.
	 * 
	 *                <p>
	 *                {@code sfactor} and {@code dfactor} must be one of:
	 *                </p>
	 *                <table>
	 *                <tr>
	 *                <td>{@link #ZERO}</td>
	 *                <td>{@link #ONE}</td>
	 *                <td>{@link #SRC_COLOR}</td>
	 *                <td>{@link #ONE_MINUS_SRC_COLOR}</td>
	 *                <td>{@link #DST_COLOR}</td>
	 *                <td>{@link #ONE_MINUS_DST_COLOR}</td>
	 *                </tr>
	 *                <tr>
	 *                <td>{@link #SRC_ALPHA}</td>
	 *                <td>{@link #ONE_MINUS_SRC_ALPHA}</td>
	 *                <td>{@link #DST_ALPHA}</td>
	 *                <td>{@link #ONE_MINUS_DST_ALPHA}</td>
	 *                <td>{@link #SRC_ALPHA_SATURATE}</td>
	 *                </tr>
	 *                </table>
	 */
	public void setGlobalCompositeBlendFunc(int sfactor, int dfactor) {
		nvgGlobalCompositeBlendFunc(ctx, sfactor, dfactor);
	}

	/**
	 * Sets the composite operation with custom pixel arithmetic for RGB and alpha
	 * components separately.
	 *
	 * @param srcRGB   the source RGB blend factor.
	 * @param dstRGB   the destination RGB blend factor.
	 * @param srcAlpha the source alpha blend factor.
	 * @param dstAlpha the destination alpha blend factor.
	 * 
	 *                 <p>
	 *                 {@code srcRGB}, {@code dstRGB}, {@code srcAlpha} and
	 *                 {@code dstAlpha} must be one of:
	 *                 </p>
	 *                 <table>
	 *                 <tr>
	 *                 <td>{@link #ZERO}</td>
	 *                 <td>{@link #ONE}</td>
	 *                 <td>{@link #SRC_COLOR}</td>
	 *                 <td>{@link #ONE_MINUS_SRC_COLOR}</td>
	 *                 <td>{@link #DST_COLOR}</td>
	 *                 <td>{@link #ONE_MINUS_DST_COLOR}</td>
	 *                 </tr>
	 *                 <tr>
	 *                 <td>{@link #SRC_ALPHA}</td>
	 *                 <td>{@link #ONE_MINUS_SRC_ALPHA}</td>
	 *                 <td>{@link #DST_ALPHA}</td>
	 *                 <td>{@link #ONE_MINUS_DST_ALPHA}</td>
	 *                 <td>{@link #SRC_ALPHA_SATURATE}</td>
	 *                 </tr>
	 *                 </table>
	 */
	public void setGlobalCompositeBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		nvgGlobalCompositeBlendFuncSeparate(ctx, srcRGB, dstRGB, srcAlpha, dstAlpha);
	}

	/**
	 * Pushes and saves the current render state into a state stack. A matching
	 * {@link #nvgRestore Restore} must be used to restore the state.
	 */
	public void save() {
		nvgSave(ctx);
	}

	/**
	 * Pops and restores current render state.
	 */
	public void restore() {
		nvgRestore(ctx);
	}

	/**
	 * Resets current render state to default values. Does not affect the render
	 * state stack.
	 */
	public void reset() {
		nvgReset(ctx);
	}

	/**
	 * Sets current stroke style to a solid color.
	 *
	 * @param color the color to set
	 */
	public void setStrokeColor(NVGColor color) {
		nvgStrokeColor(ctx, color);
	}

	/**
	 * @param color typically one of the {@code Colors} class color setters
	 */
	public void setStrokeColor(ColorSetter color) {
		nvgStrokeColor(ctx, color.set(colorA));
	}

	/**
	 * Sets current stroke style to a paint, which can be a one of the gradients or
	 * a pattern.
	 * 
	 * @param paint the paint to set
	 */
	public void setStrokePaint(NVGPaint paint) {
		nvgStrokePaint(ctx, paint);
	}

	/**
	 * Sets current fill style to a color.
	 *
	 * @param color the color to set
	 */
	public void setFillColor(NVGColor color) {
		nvgFillColor(ctx, color);
	}

	/**
	 * @param color typically one of the {@code Colors} class color setters
	 */
	public void setFillColor(ColorSetter color) {
		nvgFillColor(ctx, color.set(colorA));
	}

	/**
	 * Sets current fill style to a paint, which can be a one of the gradients or a
	 * pattern.
	 * 
	 * @param paint the paint to set
	 */
	public void setFillPaint(NVGPaint paint) {
		nvgFillPaint(ctx, paint);
	}

	/**
	 * Returns an image pattern. The {@code NVGPaint} object returned may change
	 * afterwards. If you want to store this image pattern, consider creating a copy
	 * of the returned paint.<br>
	 * The pattern is transformed by the current transform when it is passed to
	 * {@link #setStrokePaint(NVGPaint)} or {@link #setFillPaint(NVGPaint)}.
	 * 
	 * @param x     the image pattern left coordinate
	 * @param y     the image pattern top coordinate
	 * @param w     the image width
	 * @param h     the image height
	 * @param angle the rotation angle around the top-left corner
	 * @param image the image to render
	 * @param alpha the alpha value
	 * @return {@code dest}
	 */
	public NVGPaint getImagePattern(float x, float y, float w, float h, //
			float angle, float alpha, Image image) {
		return nvgImagePattern(ctx, x, y, w, h, angle, getID(image), alpha, paint);
	}

	/**
	 * Creates and returns a box gradient. Box gradient is a feathered rounded
	 * rectangle, it is useful for rendering drop shadows or highlights for boxes.
	 * <br>
	 * The {@code NVGPaint} object returned may change afterwards. If you want to
	 * store this gradient, consider creating a copy of the returned paint.
	 * <p>
	 * The gradient is transformed by the current transform when it is passed to
	 * {@link #setStrokePaint(NVGPaint)} or {@link #setFillPaint(NVGPaint)}.
	 * </p>
	 *
	 * @param x    the rectangle left coordinate
	 * @param y    the rectangle top coordinate
	 * @param w    the rectangle width
	 * @param h    the rectangle height
	 * @param r    the corner radius
	 * @param f    the feather value. Feather defines how blurry the border of the
	 *             rectangle is.
	 * @param icol the inner color
	 * @param ocol the outer color
	 * @return {@code dest}
	 */
	public NVGPaint getBoxGradient(float x, float y, float w, float h, //
			float r, float f, NVGColor icol, NVGColor ocol) {
		return nvgBoxGradient(ctx, x, y, w, h, r, f, icol, ocol, paint);
	}

	public NVGPaint getBoxGradient(float x, float y, float w, float h, //
			float r, float f, ColorSetter icol, ColorSetter ocol) {
		return nvgBoxGradient(ctx, x, y, w, h, r, f, icol.set(colorA), ocol.set(colorB), paint);
	}

	/**
	 * Creates and returns a linear gradient. <br>
	 * The {@code NVGPaint} object returned may change afterwards. If you want to
	 * store this gradient, consider creating a copy of the returned paint.
	 * <p>
	 * The gradient is transformed by the current transform when it is passed to
	 * {@link #setStrokePaint(NVGPaint)} or {@link #setFillPaint(NVGPaint)}.
	 * </p>
	 *
	 * @param sx   the X axis start coordinate
	 * @param sy   the Y axis start coordinate
	 * @param ex   the X axis end coordinate
	 * @param ey   the Y axis end coordinate
	 * @param icol the start color
	 * @param ocol the end color
	 * @return {@code dest}
	 */
	public NVGPaint getLinearGradient(float sx, float sy, float ex, float ey, //
			NVGColor icol, NVGColor ocol) {
		return nvgLinearGradient(ctx, sx, sy, ex, ey, icol, ocol, paint);
	}

	public NVGPaint getLinearGradient(float sx, float sy, float ex, float ey, //
			ColorSetter icol, ColorSetter ocol) {
		return nvgLinearGradient(ctx, sx, sy, ex, ey, icol.set(colorA), ocol.set(colorB), paint);
	}

	/**
	 * Creates and returns a radial gradient. <br>
	 * The {@code NVGPaint} object returned may change afterwards. If you want to
	 * store this gradient, consider creating a copy of the returned paint.
	 * <p>
	 * The gradient is transformed by the current transform when it is passed to
	 * {@link #setStrokePaint(NVGPaint)} or {@link #setFillPaint(NVGPaint)}.
	 * </p>
	 *
	 * @param cx   the X axis center coordinate
	 * @param cy   the Y axis center coordinate
	 * @param inr  the inner radius
	 * @param outr the outer radius
	 * @param icol the start color
	 * @param ocol the end color
	 * @return {@code dest}
	 */
	public NVGPaint getRadialGradient(float cx, float cy, float inr, float outr, //
			NVGColor icol, NVGColor ocol) {
		return nvgRadialGradient(ctx, cx, cy, inr, outr, icol, ocol, paint);
	}

	public NVGPaint getRadialGradient(float cx, float cy, float inr, float outr, //
			ColorSetter icol, ColorSetter ocol) {
		return nvgRadialGradient(ctx, cx, cy, inr, outr, icol.set(colorA), ocol.set(colorB), paint);
	}

	/**
	 * Sets the miter limit of the stroke style. Miter limit controls when a sharp
	 * corner is beveled.
	 *
	 * @param limit the miter limit to set
	 */
	public void setMiterLimit(float limit) {
		nvgMiterLimit(ctx, limit);
	}

	/**
	 * Sets the stroke width of the stroke style.
	 *
	 * @param size the stroke width to set
	 */
	public void setStrokeWidth(float size) {
		nvgStrokeWidth(ctx, size);
	}

	/**
	 * Sets how the end of the line (cap) is drawn.
	 * 
	 * <p>
	 * The default line cap is {@link #BUTT}.
	 * </p>
	 *
	 * @param cap the line cap to set. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #BUTT}</td>
	 *            <td>{@link #ROUND}</td>
	 *            <td>{@link #SQUARE}</td>
	 *            </tr>
	 *            </table>
	 */
	public void setLineCap(int cap) {
		nvgLineCap(ctx, cap);
	}

	/**
	 * Sets how sharp path corners are drawn.
	 * 
	 * <p>
	 * The default line join is {@link #MITER}.
	 * </p>
	 *
	 * @param join the line join to set. One of:<br>
	 *             <table>
	 *             <tr>
	 *             <td>{@link #MITER}</td>
	 *             <td>{@link #ROUND}</td>
	 *             <td>{@link #BEVEL}</td>
	 *             </tr>
	 *             </table>
	 */
	public void setLineJoin(int join) {
		nvgLineJoin(ctx, join);
	}

	/**
	 * Sets the transparency applied to all rendered shapes.
	 * 
	 * <p>
	 * Already transparent paths will get proportionally more transparent as well.
	 * </p>
	 *
	 * @param alpha the alpha value to set
	 */
	public void setGlobalAlpha(float alpha) {
		nvgGlobalAlpha(ctx, alpha);
	}

	/**
	 * Resets the current transform to an identity matrix.
	 */
	public void resetTransform() {
		nvgResetTransform(ctx);
	}

	/**
	 * Premultiplies the current coordinate system by specified matrix. The
	 * parameters are interpreted as matrix as follows:
	 * 
	 * <pre>
	 * <code>
	 * [a c e]
	 * [b d f]
	 * [0 0 1]</code>
	 * </pre>
	 *
	 * @param a the a value (or the {@code m00} element of an
	 *          {@link AffineTransform}
	 * @param b the b value (or the {@code m10} element of an
	 *          {@link AffineTransform}
	 * @param c the c value (or the {@code m01} element of an
	 *          {@link AffineTransform}
	 * @param d the d value (or the {@code m11} element of an
	 *          {@link AffineTransform}
	 * @param e the e value (or the {@code m02} element of an
	 *          {@link AffineTransform}
	 * @param f the f value (or the {@code m20} element of an
	 *          {@link AffineTransform}
	 */
	public void premultiply(float a, float b, float c, float d, float e, float f) {
		nvgTransform(ctx, a, b, c, d, e, f);
	}

	/**
	 * Stores the top part (a-f) of the current transformation matrix in to the
	 * specified buffer.
	 * 
	 * <pre>
	 * <code>
	 * [a c e]
	 * [b d f]
	 * [0 0 1]</code>
	 * </pre>
	 * 
	 * <p>
	 * There should be space for 6 floats in the return buffer for the values
	 * {@code a-f}.
	 * </p>
	 *
	 * @param xform the destination buffer
	 */
	public void getCurrentTransform(FloatBuffer dest) {
		nvgCurrentTransform(ctx, dest);
	}

	/**
	 * Stores the top part (a-f) of the current transformation matrix in to the
	 * specified array.
	 * 
	 * <pre>
	 * <code>
	 * [a c e]
	 * [b d f]
	 * [0 0 1]</code>
	 * </pre>
	 * 
	 * <p>
	 * There should be space for 6 floats in the return array for the values
	 * {@code a-f}.
	 * </p>
	 *
	 * @param xform the destination buffer
	 */
	public void getCurrentTransform(float[] dest) {
		nvgCurrentTransform(ctx, dest);
	}

	/**
	 * Translates current coordinate system.
	 * 
	 * @param tx the X axis translation amount
	 * @param ty the Y axis translation amount
	 */
	public void translate(float tx, float ty) {
		nvgTranslate(ctx, tx, ty);
	}

	/**
	 * Scales the current coordinate system.
	 *
	 * @param sx the X axis scale factor
	 * @param sy the Y axis scale factor
	 */
	public void scale(float sx, float sy) {
		nvgScale(ctx, sx, sy);
	}

	/**
	 * Skews the current coordinate system along X axis.<br>
	 * <p>
	 * <strong>Note :</strong><br>
	 * Sometimes, the skew is referred as 'shear', that is because it is not
	 * expressed as a rotation but as a shift.<br>
	 * For example the {@code java.awt.AffineTransform} class says :
	 * <p style="margin:5px; padding:5px; border-left: 1px white solid;">
	 * "shearx is the multiplier by which coordinates are shifted in the direction
	 * of the positive X axis as a factor of their Y coordinate."
	 * </p>
	 * It can be calculated like so :
	 * 
	 * <pre>
	 * double shearX = Math.tan(skewX);
	 * </pre>
	 * </p>
	 *
	 * @param angle the skew angle, in radians
	 */
	public void skewX(float angle) {
		nvgSkewX(ctx, angle);
	}

	/**
	 * Skews the current coordinate system along Y axis.<br>
	 * <p>
	 * <strong>Note :</strong><br>
	 * Sometimes, the skew is referred as 'shear', that is because it is not
	 * expressed as a rotation but as a shift.<br>
	 * For example the {@code java.awt.AffineTransform} class says :
	 * <p style="margin:5px; padding:3px; border-left: 2px gray solid;">
	 * "shearx is the multiplier by which coordinates are shifted in the direction
	 * of the positive Y axis as a factor of their X coordinate."
	 * </p>
	 * It can be calculated like so :
	 * 
	 * <pre>
	 * double shearY = Math.tan(skewY);
	 * </pre>
	 * </p>
	 *
	 * @param angle the skew angle, in radians
	 */
	public void skewY(float angle) {
		nvgSkewY(ctx, angle);
	}

	/**
	 * Rotates current coordinate system.
	 *
	 * @param angle the rotation angle, in radians
	 */
	public void rotate(float angle) {
		nvgRotate(ctx, angle);
	}

	/**
	 * Sets current scissor rectangle.
	 * 
	 * <p>
	 * The scissor rectangle is transformed by the current transform.
	 * </p>
	 *
	 * @param x the rectangle X axis coordinate
	 * @param y the rectangle Y axis coordinate
	 * @param w the rectangle width
	 * @param h the rectangle height
	 */
	public void setScissor(float x, float y, float w, float h) {
		nvgScissor(ctx, x, y, w, h);
	}

	/**
	 * Intersects current scissor rectangle with the specified rectangle.
	 * 
	 * <p>
	 * The scissor rectangle is transformed by the current transform.
	 * </p>
	 * 
	 * <p>
	 * Note: in case the rotation of previous scissor rect differs from the current
	 * one, the intersection will be done between the specified rectangle and the
	 * previous scissor rectangle transformed in the NanoVG's transform space. The
	 * resulting shape is always rectangle.
	 * </p>
	 *
	 * @param x the rectangle X axis coordinate
	 * @param y the rectangle Y axis coordinate
	 * @param w the rectangle width
	 * @param h the rectangle height
	 */
	public void intersectScissor(float x, float y, float w, float h) {
		nvgIntersectScissor(ctx, x, y, w, h);
	}

	/**
	 * Resets and disables scissoring.
	 */
	public void resetScissor() {
		nvgResetScissor(ctx);
	}

	/**
	 * Clears the current path and sub-paths.
	 */
	public void beginPath() {
		nvgBeginPath(ctx);
	}

	/**
	 * Starts new sub-path with specified point as first point.
	 *
	 * @param x the point X axis coordinate
	 * @param y the point Y axis coordinate
	 */
	public void moveTo(float x, float y) {
		nvgMoveTo(ctx, x, y);
	}

	/**
	 * Adds line segment from the last point in the path to the specified point.
	 *
	 * @param x the point X axis coordinate
	 * @param y the point Y axis coordinate
	 */
	public void lineTo(float x, float y) {
		nvgLineTo(ctx, x, y);
	}

	/**
	 * Adds cubic bezier segment from last point in the path via two control points
	 * to the specified point.
	 *
	 * @param c1x the first control point X axis coordinate
	 * @param c1y the first control point Y axis coordinate
	 * @param c2x the second control point X axis coordinate
	 * @param c2y the second control point Y axis coordinate
	 * @param x   the point X axis coordinate
	 * @param y   the point Y axis coordinate
	 */
	public void bezierTo(float c1x, float c1y, float c2x, float c2y, float x, float y) {
		nvgBezierTo(ctx, c1x, c1y, c2x, c2y, x, y);
	}

	/**
	 * Adds quadratic bezier segment from last point in the path via a control point
	 * to the specified point.
	 *
	 * @param cx the control point X axis coordinate
	 * @param cy the control point Y axis coordinate
	 * @param x  the point X axis coordinate
	 * @param y  the point Y axis coordinate
	 */
	public void quadTo(float cx, float cy, float x, float y) {
		nvgQuadTo(ctx, cx, cy, x, y);
	}

	/**
	 * Adds an arc segment at the corner defined by the last path point, and two
	 * specified points.
	 *
	 * @param x1     the first point X axis coordinate
	 * @param y1     the first point Y axis coordinate
	 * @param x2     the second point X axis coordinate
	 * @param y2     the second point Y axis coordinate
	 * @param radius the arc radius, in radians
	 */
	public void arcTo(float x1, float y1, float x2, float y2, float radius) {
		nvgArcTo(ctx, x1, y1, x2, y2, radius);
	}

	/**
	 * Closes current sub-path with a line segment.
	 */
	public void closePath() {
		nvgClosePath(ctx);
	}

	/**
	 * Sets the current sub-path winding.
	 *
	 * @param dir the sub-path winding. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #SOLID}</td>
	 *            <td>{@link #HOLE}</td>
	 *            </tr>
	 *            </table>
	 */
	public void setPathWinding(int dir) {
		nvgPathWinding(ctx, dir);
	}

	/**
	 * Creates new circle arc shaped sub-path.
	 *
	 * @param cx  the arc center X axis coordinate
	 * @param cy  the arc center Y axis coordinate
	 * @param r   the arc radius
	 * @param a0  the arc starting angle, in radians
	 * @param a1  the arc ending angle, in radians
	 * @param dir the arc direction. One of:<br>
	 *            <table>
	 *            <tr>
	 *            <td>{@link #CCW}</td>
	 *            <td>{@link #CW}</td>
	 *            </tr>
	 *            </table>
	 */
	public void arc(float cx, float cy, float r, float a0, float a1, int dir) {
		nvgArc(ctx, cx, cy, r, a0, a1, dir);
	}

	/**
	 * Creates new rectangle shaped sub-path.
	 *
	 * @param x the rectangle X axis coordinate
	 * @param y the rectangle Y axis coordinate
	 * @param w the rectangle width
	 * @param h the rectangle height
	 */
	public void rect(float x, float y, float w, float h) {
		nvgRect(ctx, x, y, w, h);
	}

	/**
	 * Creates new rectangle shaped sub-path.
	 * 
	 * @param rect
	 */
	public void rect(Rectanglec rect) {
		nvgRect(ctx, rect.x(), rect.y(), rect.width(), rect.height());
	}

	/**
	 * Creates new rounded rectangle shaped sub-path.
	 *
	 * @param x the rectangle X axis coordinate
	 * @param y the rectangle Y axis coordinate
	 * @param w the rectangle width
	 * @param h the rectangle height
	 * @param r the corner radius
	 */
	public void roundedRect(float x, float y, float w, float h, float r) {
		nvgRoundedRect(ctx, x, y, w, h, r);
	}

	/**
	 * Creates new rounded rectangle shaped sub-path.
	 * 
	 * @param rect
	 * @param r    the corner radius
	 */
	public void roundedRect(Rectanglec rect, float r) {
		nvgRoundedRect(ctx, rect.x(), rect.y(), rect.width(), rect.height(), r);
	}

	/**
	 * Creates new rounded rectangle shaped sub-path with varying radii for each
	 * corner.
	 *
	 * @param x              the rectangle X axis coordinate
	 * @param y              the rectangle Y axis coordinate
	 * @param w              the rectangle width
	 * @param h              the rectangle height
	 * @param radTopLeft     the top-left corner radius
	 * @param radTopRight    the top-right corner radius
	 * @param radBottomRight the bottom-right corner radius
	 * @param radBottomLeft  the bottom-left corner radius
	 */
	public void roundedRectVarying(float x, float y, float w, float h, float radTopLeft, float radTopRight,
			float radBottomRight, float radBottomLeft) {
		nvgRoundedRectVarying(ctx, x, y, w, h, radTopLeft, radTopRight, radBottomRight, radBottomLeft);
	}

	/**
	 * Creates new rounded rectangle shaped sub-path with varying radii for each
	 * corner.
	 * 
	 * @param rect
	 * @param radTopLeft     the top-left corner radius
	 * @param radTopRight    the top-right corner radius
	 * @param radBottomRight the bottom-right corner radius
	 * @param radBottomLeft  the bottom-left corner radius
	 */
	public void roundedRectVarying(Rectanglec rect, float radTopLeft, float radTopRight, float radBottomRight,
			float radBottomLeft) {
		nvgRoundedRectVarying(ctx, rect.x(), rect.y(), rect.width(), rect.height(), radTopLeft, radTopRight,
				radBottomRight, radBottomLeft);
	}

	/**
	 * Creates new ellipse shaped sub-path.
	 *
	 * @param cx the ellipse center X axis coordinate
	 * @param cy the ellipse center Y axis coordinate
	 * @param rx the ellipse X axis radius
	 * @param ry the ellipse Y axis radius
	 */
	public void ellipse(float cx, float cy, float rx, float ry) {
		nvgEllipse(ctx, cx, cy, rx, ry);
	}

	/**
	 * Creates new ellipse shaped sub-path.
	 * 
	 * @param rect the bounding box of the ellipse to draw
	 */
	public void ellipse(Rectanglec rect) {
		nvgEllipse(ctx, rect.cx(), rect.cy(), rect.width() / 2f, rect.height() / 2f);
	}

	/**
	 * Creates new circle shaped sub-path.
	 *
	 * @param cx the circle center X axis coordinate
	 * @param cy the circle center Y axis coordinate
	 * @param r  the circle radius
	 */
	public void circle(float cx, float cy, float r) {
		nvgCircle(ctx, cx, cy, r);
	}

	/**
	 * Fills the current path with current fill style.
	 */
	public void fill() {
		nvgFill(ctx);
	}

	/**
	 * Fills the current path with current stroke style.
	 */
	public void stroke() {
		nvgStroke(ctx);
	}

	/**
	 * Sets the font size of current text style.
	 *
	 * @param size the font size to set
	 */
	public void setFontSize(float size) {
		nvgFontSize(ctx, size);
	}

	/**
	 * Sets the blur of current text style.
	 *
	 * @param blur the blur amount to set
	 */
	public void setFontBlur(float blur) {
		nvgFontBlur(ctx, blur);
	}

	/**
	 * Sets the letter spacing of current text style.
	 *
	 * @param spacing the letter spacing amount to set
	 */
	public void setTextLetterSpacing(float spacing) {
		nvgTextLetterSpacing(ctx, spacing);
	}

	/**
	 * Sets the proportional line height of current text style. The line height is
	 * specified as multiple of font size.
	 *
	 * @param lineHeight the line height to set
	 */
	public void setTextLineHeight(float lineHeight) {
		nvgTextLineHeight(ctx, lineHeight);
	}

	/**
	 * Sets the text align of current text style.
	 *
	 * @param align the text align to set. One of:<br>
	 *              <table>
	 *              <tr>
	 *              <td>{@link #ALIGN_LEFT}</td>
	 *              <td>{@link #ALIGN_CENTER}</td>
	 *              <td>{@link #ALIGN_RIGHT}</td>
	 *              <td>{@link #ALIGN_TOP}</td>
	 *              <td>{@link #ALIGN_MIDDLE}</td>
	 *              <td>{@link #ALIGN_BOTTOM}</td>
	 *              <td>{@link #ALIGN_BASELINE}</td>
	 *              </tr>
	 *              </table>
	 */
	public void setTextAlign(int align) {
		nvgTextAlign(ctx, align);
	}

	/**
	 * Sets the font face based on specified name of current text style.
	 *
	 * @param font the font name
	 */
	public void setFontFace(String font) {
		nvgFontFace(ctx, font);
	}

	/**
	 * Sets the font face and size.
	 * 
	 * @param font a font style
	 */
	public void setFont(FontStyle font) {
		nvgFontFace(ctx, font.getName());
		nvgFontSize(ctx, font.getSize());
	}

	/**
	 * Draws text string at specified location.
	 * 
	 * @param x      the text X axis coordinate
	 * @param y      the text Y axis coordinate
	 * @param string the text string to draw
	 */
	public void text(float x, float y, CharSequence string) {
		nvgText(ctx, x, y, string);
	}

	/**
	 * Draws text string at specified location.
	 * 
	 * @param x      the text X axis coordinate
	 * @param y      the text Y axis coordinate
	 * @param string the text string to draw
	 */
	public void text(float x, float y, ByteBuffer string) {
		nvgText(ctx, x, y, string);
	}

	/**
	 * Draws multi-line text string at specified location wrapped at the specified
	 * width.
	 * 
	 * <p>
	 * White space is stripped at the beginning of the rows, the text is split at
	 * word boundaries or when new-line characters are encountered. Words longer
	 * than the max width are slit at nearest character (i.e. no hyphenation).
	 * </p>
	 *
	 * @param x             the text box X axis coordinate
	 * @param y             the text box Y axis coordinate
	 * @param breakRowWidth the maximum row width
	 * @param string        the text string to draw
	 */
	public void textBox(float x, float y, float breakRowWidth, ByteBuffer string) {
		nvgTextBox(ctx, x, y, breakRowWidth, string);
	}

	/**
	 * Draws multi-line text string at specified location wrapped at the specified
	 * width.
	 * 
	 * <p>
	 * White space is stripped at the beginning of the rows, the text is split at
	 * word boundaries or when new-line characters are encountered. Words longer
	 * than the max width are slit at nearest character (i.e. no hyphenation).
	 * </p>
	 *
	 * @param x             the text box X axis coordinate
	 * @param y             the text box Y axis coordinate
	 * @param breakRowWidth the maximum row width
	 * @param string        the text string to draw
	 */
	public void textBox(float x, float y, float breakRowWidth, CharSequence string) {
		nvgTextBox(ctx, x, y, breakRowWidth, string);
	}

	/**
	 * Measures the specified text string.
	 * 
	 * <p>
	 * Parameter {@code bounds} should be a pointer to {@code float[4]}, if the
	 * bounding box of the text should be returned. The bounds value are
	 * {@code [xmin,ymin, xmax,ymax]}.
	 * </p>
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param x      the text X axis coordinate
	 * @param y      the text Y axis coordinate
	 * @param string the text string to measure
	 * @param bounds returns the bounding box of the text
	 *
	 * @return the horizontal advance of the measured text (i.e. where the next
	 *         character should drawn)
	 */
	public float getTextBounds(float x, float y, ByteBuffer string, FloatBuffer bounds) {
		return nvgTextBounds(ctx, x, y, string, bounds);
	}

	public float getTextBounds(float x, float y, ByteBuffer string, float[] bounds) {
		return nvgTextBounds(ctx, x, y, string, bounds);
	}

	public float getTextBounds(float x, float y, ByteBuffer string, Rectangle bounds) {
		float[] dest = new float[4];
		float res = nvgTextBounds(ctx, x, y, string, dest);
		bounds.setRectFromDiagonal(dest[0], dest[0], dest[0], dest[0]);
		return res;
	}

	public float getTextBounds(float x, float y, CharSequence string, Rectangle bounds) {
		float[] dest = new float[4];
		float res = nvgTextBounds(ctx, x, y, string, dest);
		bounds.setRectFromDiagonal(dest[0], dest[1], dest[2], dest[3]);
		return res;
	}

	/**
	 * CharSequence version of
	 * {@link #getTextBounds(float, float, ByteBuffer, FloatBuffer)}
	 */
	public float getTextBounds(float x, float y, CharSequence string, FloatBuffer bounds) {
		return nvgTextBounds(ctx, x, y, string, bounds);
	}

	/**
	 * Array version of
	 * {@link #getTextBounds(float, float, CharSequence, FloatBuffer)}
	 */
	public float getTextBounds(float x, float y, CharSequence string, float[] bounds) {
		return nvgTextBounds(ctx, x, y, string, bounds);
	}

	/**
	 * Measures the specified multi-text string.
	 * 
	 * <p>
	 * Parameter {@code bounds} should be a pointer to {@code float[4]}, if the
	 * bounding box of the text should be returned. The bounds value are
	 * {@code [xmin,ymin, xmax,ymax]}.
	 * </p>
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param x             the text box X axis coordinate
	 * @param y             the text box Y axis coordinate
	 * @param breakRowWidth the maximum row width
	 * @param string        the text string to measure
	 * @param bounds        returns the bounding box of the text box
	 */
	public void getTextBoxBounds(float x, float y, float breakRowWidth, ByteBuffer string, FloatBuffer bounds) {
		nvgTextBoxBounds(ctx, x, y, breakRowWidth, string, bounds);
	}

	/**
	 * Array version of
	 * {@link #getTextBoxBounds(float, float, float, ByteBuffer, FloatBuffer)}
	 */
	public void getTextBoxBounds(float x, float y, float breakRowWidth, ByteBuffer string, float[] bounds) {
		nvgTextBoxBounds(ctx, x, y, breakRowWidth, string, bounds);
	}

	/**
	 * CharSequence version of
	 * {@link #getTextBoxBounds(float, float, float, ByteBuffer, FloatBuffer)}
	 */
	public void getTextBoxBounds(float x, float y, float breakRowWidth, CharSequence string, FloatBuffer bounds) {
		nvgTextBoxBounds(ctx, x, y, breakRowWidth, string, bounds);
	}

	/**
	 * Array version of
	 * {@link #getTextBoxBounds(float, float, float, CharSequence, FloatBuffer)}
	 */
	public void getTextBoxBounds(float x, float y, float breakRowWidth, CharSequence string, float[] bounds) {
		nvgTextBoxBounds(ctx, x, y, breakRowWidth, string, bounds);
	}

	/**
	 * Calculates the glyph x positions of the specified text. If {@code end} is
	 * specified only the sub-string will be used.
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param x         the text X axis coordinate
	 * @param y         the text Y axis coordinate
	 * @param string    the text string to measure
	 * @param positions returns the glyph x positions
	 */
	public int getTextGlyphPositions(float x, float y, ByteBuffer string, NVGGlyphPosition.Buffer positions) {
		return nvgTextGlyphPositions(ctx, x, y, string, positions);
	}

	/**
	 * CharSequence verson of
	 * {@link #getTextGlyphPositions(float, float, ByteBuffer, org.lwjgl.nanovg.NVGGlyphPosition.Buffer)}
	 */
	public int getTextGlyphPositions(float x, float y, CharSequence string, NVGGlyphPosition.Buffer positions) {
		return nvgTextGlyphPositions(ctx, x, y, string, positions);
	}

	/**
	 * Returns the vertical metrics based on the current text style.
	 * 
	 * <p>
	 * Measured values are returned in local coordinate space.
	 * </p>
	 *
	 * @param ascender  the line ascend
	 * @param descender the line descend
	 * @param lineh     the line height
	 */
	public void getTextMetrics(FloatBuffer ascender, FloatBuffer descender, FloatBuffer lineh) {
		nvgTextMetrics(ctx, ascender, descender, lineh);
	}

	/**
	 * Array version of
	 * {@link #getTextMetrics(FloatBuffer, FloatBuffer, FloatBuffer)}
	 */
	public void getTextMetrics(float[] ascender, float[] descender, float[] lineh) {
		nvgTextMetrics(ctx, ascender, descender, lineh);
	}

	/**
	 * Breaks the specified text into lines.
	 * 
	 * <p>
	 * White space is stripped at the beginning of the rows, the text is split at
	 * word boundaries or when new-line characters are encountered. Words longer
	 * than the max width are slit at nearest character (i.e. no hyphenation).
	 * </p>
	 *
	 * @param string        the text string to measure
	 * @param breakRowWidth the maximum row width
	 * @param rows          returns the text rows
	 * 
	 * @return the number of lines/rows measured
	 */
	public int textBreakLines(ByteBuffer string, float breakRowWidth, NVGTextRow.Buffer rows) {
		return nvgTextBreakLines(ctx, string, breakRowWidth, rows);
	}

	/**
	 * CharSequence version of
	 * {@link #textBreakLines(ByteBuffer, float, org.lwjgl.nanovg.NVGTextRow.Buffer)}
	 */
	public int textBreakLines(CharSequence string, float breakRowWidth, NVGTextRow.Buffer rows) {
		return nvgTextBreakLines(ctx, string, breakRowWidth, rows);
	}

	public Vectorc getContentScale() {
		return contentScale;
	}

	public void destroy() {
		images.forEach((im, id) -> {
			nvgDeleteImage(ctx, id);
		});
		images.clear();

		nvgDelete(ctx);
	}
}
