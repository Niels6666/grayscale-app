package components;

import static org.lwjgl.system.MemoryUtil.memLengthUTF8;
import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import grayscale.Application;
import grayscale.Component;
import grayscale.FontStyle;
import grayscale.Graphics;
import grayscale.Image;
import ui.ButtonUI;
import utils.Colors;

public class Button extends Component {
	private Runnable action;

	private int textPlacement = TOP;
	private int textAlignment = Graphics.ALIGN_CENTER | Graphics.ALIGN_MIDDLE;
	private String text;
	private ByteBuffer textBBF;
	private Image image;
	private FontStyle font;

	public Button(String text, Image image, FontStyle font) {
		super(new ButtonUI());
		setText(text);
		this.image = image;
		this.font = font != null ? font : Application.getDefaultFontStyle();
		Colors.gray.set(background);
		Colors.black.set(foreground);
	}

	public Button(String text, Image image) {
		this(text, image, null);
	}

	public Button(String text) {
		this(text, null, null);
	}

	public Button(String text, FontStyle font) {
		this(text, null, font);
	}

	public Button(Image image) {
		this(null, image, null);
	}

	public int getTextPlacement() {
		return textPlacement;
	}

	public void setTextPlacement(int textPlacement) {
		this.textPlacement = textPlacement;
	}

	public int getTextAlignment() {
		return textAlignment;
	}

	public void setTextAlignment(int textAlignment) {
		this.textAlignment = textAlignment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		if (text != null) {
			int l = memLengthUTF8(text, false);
			textBBF = BufferUtils.createByteBuffer(l);
			memUTF8(text, false, textBBF);
		}
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public FontStyle getFont() {
		return font;
	}

	public void setFont(FontStyle font) {
		this.font = font;
	}

	public ByteBuffer getTextBBF() {
		return textBBF;
	}

	public Runnable getAction() {
		return action;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}
}
