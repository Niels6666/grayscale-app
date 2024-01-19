package utils;

import static org.lwjgl.opengl.GL11C.*;

import static org.lwjgl.opengl.GL12C.*;
import static org.lwjgl.opengl.GL13C.*;
import static org.lwjgl.opengl.GL42C.*;
import static org.lwjgl.opengl.GL45C.*;

import java.io.IOException;
import java.nio.*;

import org.lwjgl.stb.STBImage;

public class Texture {
	private final int id;
	private final int width;
	private final int height;

	public Texture(int width, int height, int internalFormat, int filter, int wrap) {
		this.width = width;
		this.height = height;
		id = glCreateTextures(GL_TEXTURE_2D);
		bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, internalFormat, width, height);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		unbind();
	}

	public Texture(int width, int height, int internalFormat, int filter, int wrap, int pixelFormat, int dataType,
			ByteBuffer data) {
		this.width = width;
		this.height = height;
		id = glCreateTextures(GL_TEXTURE_2D);
		bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, internalFormat, width, height);
		glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, pixelFormat, dataType, data);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		unbind();
	}

	// copied data;
	public Texture(int width, int height, int id) {
		this.width = width;
		this.height = height;
		this.id = id;
	}

	public Texture(String path) throws IOException {

		int width[] = new int[1];
		int height[] = new int[1];
		int channels[] = new int[1];
		ByteBuffer buff = STBImage.stbi_load(path, width, height, channels, 4);
		if (buff == null) {
			throw new IOException("could not load image at : " + path);
		}

		this.width = width[0];
		this.height = height[0];

		id = glCreateTextures(GL_TEXTURE_2D);
		bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, this.width, this.height);
		glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, this.width, this.height, GL_RGBA, GL_UNSIGNED_BYTE, buff);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		unbind();

		STBImage.stbi_image_free(buff);
	}

	public void setTexture(ByteBuffer data) {
		bind();
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, this.width, this.height);
		glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, this.width, this.height, GL_RGBA, GL_UNSIGNED_BYTE, data);
		unbind();
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void bindAsTexture(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbindAsTexture(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void delete() {
		glDeleteTextures(id);
	}

	public int getID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
