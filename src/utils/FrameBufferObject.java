package utils;

import java.nio.ByteBuffer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL46C;
import org.lwjgl.system.MemoryUtil;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL46C.*;

public class FrameBufferObject {
	public enum AttachmentFormat {

		RGB(GL_RGB, GL_RGB, GL_COLOR_ATTACHMENT0, true, GL_FLOAT),
		RGB16F(GL_RGB16F, GL_RGB, GL_COLOR_ATTACHMENT0, true, GL_FLOAT),
		RGB32F(GL_RGB32F, GL_RGB, GL_COLOR_ATTACHMENT0, true, GL_FLOAT),

		R11F_G11F_B10F(GL_R11F_G11F_B10F, GL_RGB, GL_COLOR_ATTACHMENT0, true, GL_FLOAT),

		RGBA(GL_RGBA8, GL_RGBA, GL_COLOR_ATTACHMENT0, true, GL_FLOAT),
		RGBA16F(GL_RGBA16F, GL_RGBA, GL_COLOR_ATTACHMENT0, true, GL_FLOAT),
		RGBA32F(GL_RGBA32F, GL_RGBA, GL_COLOR_ATTACHMENT0, true, GL_FLOAT),

		// warning doesn't work
		DEPTH_COMPONENT(GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT, GL_DEPTH_ATTACHMENT, false, GL_FLOAT),
		DEPTH24_STENCIL8(GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL, GL_DEPTH_STENCIL_ATTACHMENT, false,
				GL_UNSIGNED_INT_24_8),
		DEPTH32_STENCIL8(GL_DEPTH32F_STENCIL8, GL_DEPTH_STENCIL, GL_DEPTH_STENCIL_ATTACHMENT, false,
				GL_FLOAT_32_UNSIGNED_INT_24_8_REV);

		public final int internalFormat;
		public final int format;
		public final int attachment;
		public final boolean isColorAttachment;
		public final int dataType;

		private AttachmentFormat(int internalFormat, int format, int attachment, boolean isColorAttachment,
				int dataType) {
			this.internalFormat = internalFormat;
			this.format = format;
			this.attachment = attachment;
			this.isColorAttachment = isColorAttachment;
			this.dataType = dataType;
		}
	}

	public static class FBOAttachment {
		int ID;
		boolean is_texture;// it is a renderbuffer otherwise
		boolean is_multisampled;
		AttachmentFormat format;
		String name;
		int colorAttachmentNumber;// enum
		int target;
		int width;
		int height;

		FBOAttachment(int ID, boolean isTexture, boolean is_multisampled, AttachmentFormat format, String name,
				int colorAttachmentNumber, int target, int width, int height) {
			this.ID = ID;
			this.is_texture = isTexture;
			this.is_multisampled = is_multisampled;
			this.format = format;
			this.name = name;
			this.colorAttachmentNumber = colorAttachmentNumber;
			this.target = target;
			this.width = width;
			this.height = height;
		}

		public void bind() {
			glBindTexture(target, ID);
		}

		public void unbind() {
			glBindTexture(target, 0);
		}

		public void bindAsTexture(int textureUnit) {
			if (!is_texture) {
				throw new IllegalArgumentException("Error, the FBOAttachment " + name + " isn't a texture");
			}
			glActiveTexture(GL_TEXTURE0 + textureUnit);
			glBindTexture(target, ID);
		}

		public void bindAsTexture(int textureUnit, int filter, int border) {
			bindAsTexture(textureUnit);

			if (is_multisampled) {
				throw new IllegalArgumentException("Filtering is not available for multisampled textures");
			}

			glTexParameteri(target, GL_TEXTURE_MAG_FILTER, filter);
			glTexParameteri(target, GL_TEXTURE_MIN_FILTER, filter);
			glTexParameteri(target, GL_TEXTURE_WRAP_S, border);
			glTexParameteri(target, GL_TEXTURE_WRAP_T, border);
		}

		public void unbindAsTexture(int textureUnit) {
			if (!is_texture) {
				throw new IllegalArgumentException("Error, the FBOAttachment " + name + " isn't a texture");
			}
			glActiveTexture(GL_TEXTURE0 + textureUnit);
			glBindTexture(target, 0);
		}

		public int getID() {
			return ID;
		}

		public boolean isTexture() {
			return is_texture;
		}

		public boolean isMultisampled() {
			return is_multisampled;
		}

		public AttachmentFormat getFormat() {
			return format;
		}

		public String getName() {
			return name;
		}

		public int getColorAttachmentNumber() {// enum
			return colorAttachmentNumber;
		}

		public int getTarget() {
			return target;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}

	int ID;
	public int width;
	public int height;
	int MULTISAMPLE_COUNT;
	String name;

	int colorAttachments = 0;
	Map<String, FBOAttachment> attachments;
	List<Integer> drawBuffers; // the color attachments which are currently bound

	/*
	 * Creates a new fbo with a size of 1x1.
	 *
	 * @return a new fbo.
	 */
	public FrameBufferObject(String name, int MULTISAMPLE_COUNT) {
		this(name, 1, 1, MULTISAMPLE_COUNT);
	}

	/*
	 * Creates a new fbo with the specified dimensions.
	 *
	 * @param width
	 * 
	 * @param height
	 * 
	 * @return a new FBO.
	 */
	public FrameBufferObject(String name, int width, int height, int MULTISAMPLE_COUNT) {
		this.ID = 0;
		this.width = width;
		this.height = height;
		this.MULTISAMPLE_COUNT = MULTISAMPLE_COUNT;
		this.name = name;
		this.attachments = new HashMap<>();
		this.drawBuffers = new ArrayList<>();

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pID = stack.mallocInt(1); // int*
			glGenFramebuffers(pID);
			ID = pID.get(0);
		}

	}

	public void delete() {
		for (FBOAttachment attachment : attachments.values()) {
			int id = attachment.getID();
			if (attachment.isTexture()) {
				glDeleteTextures(id);
			} else {
				glDeleteRenderbuffers(id);
			}
		}

		glDeleteFramebuffers(ID);
	}

	/**
	 * Make sure to call this method after binding the FBO, even with no parameter.
	 * Binds an array of color attachments. An attachment that doesn't exists is
	 * ignored.
	 *
	 * @param colorAttachments The color attachments that have to be bound.
	 *
	 */
	public void bindColorAttachments(List<String> colorAttachments) {
		drawBuffers.clear();

		for (var attachmentName : colorAttachments) {
			FBOAttachment attachment = getAttachment(attachmentName);
			if (attachment.getFormat().isColorAttachment) {
				drawBuffers.add(attachment.getColorAttachmentNumber());
			} else {
				throw new IllegalArgumentException("Error, " + attachmentName + " isn't a color attachment");
			}
		}

		if (drawBuffers.isEmpty()) {
			drawBuffers.add(GL_NONE);
		}

		glDrawBuffers(drawBuffers.stream().mapToInt(i -> i).toArray());

	}

	/**
	 * Sets the read and draw buffers to GL_NONE, ie: there is no color data. Useful
	 * for shadow mapping for eg.
	 */
	public void bindNoColorBuffers() {
		glReadBuffer(GL_NONE);
		glDrawBuffer(GL_NONE);
	}

	/**
	 * Sets the viewport to the current size of that FBO.
	 */
	public void setViewport() {
		glViewport(0, 0, width, height);
	}

	/**
	 * Resize all the FBOAttachments of this FBO. Does nothing if the size is the
	 * same.
	 *
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		if (this.width == width && this.height == height) {
			return;
		}

		this.width = width;
		this.height = height;

		bind();

		for (FBOAttachment attachment : attachments.values()) {
			attachment.width = width;
			attachment.height = height;

			if (attachment.isTexture()) {

				glBindTexture(attachment.getTarget(), attachment.getID());

				if (MULTISAMPLE_COUNT == 0) {
					glTexImage2D(attachment.getTarget(), 0, attachment.getFormat().internalFormat, width, height, 0,
							attachment.getFormat().format, GL_FLOAT, MemoryUtil.NULL);
				} else {
					glTexImage2DMultisample(attachment.getTarget(), MULTISAMPLE_COUNT,
							attachment.getFormat().internalFormat, width, height, true);
				}

				glBindTexture(attachment.getTarget(), 0);
			} else {
				glBindRenderbuffer(GL_RENDERBUFFER, attachment.getID());

				glRenderbufferStorage(GL_RENDERBUFFER, attachment.getFormat().format, width, height);

				glBindRenderbuffer(GL_RENDERBUFFER, 0);
			}
		}

		unbind();
	}

	/**
	 * Creates a new texture attachment for this fbo.
	 *
	 * @param name
	 * @param format
	 * 
	 * @return may return null
	 */
	public Texture addTextureAttachment(String name, AttachmentFormat format) {
		Texture res = null;
		bind();

		int target = MULTISAMPLE_COUNT == 0 ? GL_TEXTURE_2D : GL_TEXTURE_2D_MULTISAMPLE;

		int textureID = glCreateTextures(GL_TEXTURE_2D);

		glBindTexture(target, textureID);

		if (MULTISAMPLE_COUNT == 0) {
			glTexImage2D(target, 0, format.internalFormat, width, height, 0, format.format, format.dataType,
					(ByteBuffer) null);

			glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			res = new Texture(width, height, textureID);
		} else {
			glTexImage2DMultisample(target, MULTISAMPLE_COUNT, format.internalFormat, width, height, true);
		}

		int colorAttachmentNumber = -1;
		if (format.isColorAttachment) {
			colorAttachmentNumber = this.colorAttachments + format.attachment;

			glFramebufferTexture2D(GL_FRAMEBUFFER, colorAttachmentNumber, target, textureID, 0);

			this.colorAttachments++;
		} else {
			glFramebufferTexture2D(GL_FRAMEBUFFER, format.attachment, target, textureID, 0);
		}

		FBOAttachment attachment = new FBOAttachment(textureID, true, MULTISAMPLE_COUNT != 0, format, name,
				colorAttachmentNumber, target, width, height);

		this.attachments.put(name, attachment);

		glBindTexture(target, 0);
		unbind();
		return res;
	}

	public FBOAttachment getAttachment(String attachmentName) {
		FBOAttachment it = attachments.get(attachmentName);
		if (it == null) {
			String allAttachmentNames = "";
			for (String name : attachments.keySet()) {
				allAttachmentNames += name + " ";
			}
			throw new IllegalArgumentException("Error, unknown FBO attachment name: " + attachmentName + " for fbo "
					+ name + ", all attachments: " + allAttachmentNames);
		} else {
			return it;
		}
	}

	/**
	 * Binds this framebuffer. Make sure to unbind the fbo when you are finished
	 * using it ! This method is called automatically when attachments are created.
	 */
	public void bind() {
		// Bind both to read and draw framebuffers
		glBindFramebuffer(GL_FRAMEBUFFER, ID);
	}

	/**
	 * Unbinds the current FBO.
	 */
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	/**
	 * @return true if the FBO is complete.
	 */
	public boolean finish() {
		bind();
		drawBuffers.clear();
		drawBuffers.add(GL_NONE);
		glDrawBuffers(drawBuffers.stream().mapToInt(i -> i).toArray());

		boolean success = glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE;

		unbind();
		return success;
	}

	/**
	 * Clears a specific color attachment. The attachment must have been bound with
	 * <code>bindColorAttachments(String... colorAttachments)</code> otherwise an
	 * exception is thrown
	 *
	 * @param attachmentName The name of the color attachment.
	 * @param color          The color to clear the attachment with.
	 */
	public void clearColorAttachment(String attachmentName, float r, float g, float b, float a) {

		FBOAttachment attachment = getAttachment(attachmentName);
		if (attachment.getFormat().isColorAttachment) {

			for (int i = 0; i < drawBuffers.size(); i++) {
				if (attachment.getColorAttachmentNumber() == drawBuffers.get(i)) {
					float[] buffer = new float[] { r, g, b, a, };
					glClearBufferfv(GL_COLOR, i, buffer);
					return;
				}
			}

			throw new IllegalArgumentException(
					"FBO color attachment " + attachmentName + " isn't currently bound, cannot clear");
		}
		throw new IllegalArgumentException(attachmentName + " isn't a color buffer");
	}

	/**
	 * Clears the depth buffer attachment of that FBO. Make sure depth writing is
	 * enabled, otherwise nothing will happen.
	 *
	 * @param value The float value to clear the buffer with. Should be in the range
	 *              [0.0f, 1.0f]
	 */
	public void clearDepthAttachment(float value) {
		float v[] = new float[] { value };
		glClearBufferfv(GL_DEPTH, 0, v);
	}

	/**
	 * Clears the stencil buffer attachment of that FBO.
	 *
	 * @param value The int value to clear the buffer with. Must be in the range [0,
	 *              2^m-1] where m is the number of bits in the stencil buffer.
	 */
	public void clearStencilAttachment(int value) {
		int v[] = new int[] { value };
		glClearBufferiv(GL_STENCIL, 0, v);
	}
}
