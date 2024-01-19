package grayscale;

import static org.lwjgl.opengl.GL46C.*;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.stb.STBImageWrite.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import utils.Texture;

public class Image {
	/**
	 * NanoVG Image flags.
	 * 
	 * <ul>
	 * <li>{@link #MIPMAPS IMAGE_GENERATE_MIPMAPS} - Generate mipmaps during
	 * creation of the image.</li>
	 * <li>{@link #IMAGE_REPEATX} - Repeat image in X direction.</li>
	 * <li>{@link #IMAGE_REPEATY} - Repeat image in Y direction.</li>
	 * <li>{@link #IMAGE_FLIPY} - Flips (inverses) image in Y direction when
	 * rendered.</li>
	 * <li>{@link #IMAGE_PREMULTIPLIED} - Image data has premultiplied alpha.</li>
	 * <li>{@link #IMAGE_NEAREST} - Image interpolation is Nearest instead
	 * Linear.</li>
	 * </ul>
	 */
	public static final int IMAGE_GENERATE_MIPMAPS = NVG_IMAGE_GENERATE_MIPMAPS, //
			IMAGE_REPEATX = NVG_IMAGE_REPEATX, //
			IMAGE_REPEATY = NVG_IMAGE_REPEATY, //
			IMAGE_FLIPY = NVG_IMAGE_FLIPY, //
			IMAGE_PREMULTIPLIED = NVG_IMAGE_PREMULTIPLIED, //
			IMAGE_NEAREST = NVG_IMAGE_NEAREST;

	private Texture texture;
	private int nanovgFlags;
	private boolean isAlive = true;

	/**
	 * @param path       the file path to this image
	 * @param imageFlags the NanoVG image flags. One of:<br>
	 *                   <table>
	 *                   <tr>
	 *                   <td>{@link #IMAGE_GENERATE_MIPMAPS}</td>
	 *                   <td>{@link #IMAGE_REPEATX}</td>
	 *                   <td>{@link #IMAGE_REPEATY}</td>
	 *                   <td>{@link #IMAGE_FLIPY}</td>
	 *                   <td>{@link #IMAGE_PREMULTIPLIED}</td>
	 *                   </tr>
	 *                   <tr>
	 *                   <td>{@link #IMAGE_NEAREST}</td>
	 *                   </tr>
	 *                   </table>
	 */
	public Image(String path, int imageFlags) {
		try {
			texture = new Texture(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not load image " + path, e);
		}

		this.nanovgFlags = imageFlags;
	}

	/**
	 * @param texture
	 * @param imageFlags the NanoVG image flags. One of:<br>
	 *                   <table>
	 *                   <tr>
	 *                   <td>{@link #IMAGE_GENERATE_MIPMAPS}</td>
	 *                   <td>{@link #IMAGE_REPEATX}</td>
	 *                   <td>{@link #IMAGE_REPEATY}</td>
	 *                   <td>{@link #IMAGE_FLIPY}</td>
	 *                   <td>{@link #IMAGE_PREMULTIPLIED}</td>
	 *                   </tr>
	 *                   <tr>
	 *                   <td>{@link #IMAGE_NEAREST}</td>
	 *                   </tr>
	 *                   </table>
	 */
	public Image(Texture texture, int imageFlags) {
		this.texture = texture;
		this.nanovgFlags = imageFlags;
	}

	public int getWidth() {
		return texture.getWidth();
	}

	public int getHeight() {
		return texture.getHeight();
	}

	public int getOpenglID() {
		return texture.getID();
	}

	public int getFlags() {
		return nanovgFlags;
	}

	/**
	 * Saves this image as a png file
	 * 
	 * @param path
	 */
	public void saveImage(String path) {
		int width = getWidth(), height = getHeight(), stride = getWidth() * 4;
		ByteBuffer image = memAlloc(getWidth() * getHeight() * 4);

		texture.bind();
		glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		texture.unbind();

		stbi_write_png(path, width, height, 4, image, stride);
		memFree(image);
	}

	public void delete() {
		texture.delete();
		isAlive = false;
	}

	public boolean isAlive() {
		return isAlive;
	}
}
