package components;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.system.MemoryUtil.*;

import grayscale.Application;
import grayscale.Component;
import grayscale.FontStyle;
import grayscale.Graphics;
import grayscale.Image;
import ui.LabelUI;
import utils.Colors;

public class Label extends Component {
	private int textPlacement = TOP;
	private int textAlignment = Graphics.ALIGN_CENTER | Graphics.ALIGN_MIDDLE;
	private String text;
	private ByteBuffer textBBF;
	private Image image;
	private FontStyle font;
	private boolean respectProportions = true;

	public Label(String text, Image image, FontStyle font) {
		super(new LabelUI());
		setText(text);
		this.image = image;
		this.font = font != null ? font : Application.getDefaultFontStyle();
		Colors.transparent.set(background);
		Colors.black.set(foreground);
	}

	public Label(String text, Image image) {
		this(text, image, null);
	}

	public Label(String text) {
		this(text, null, null);
	}

	public Label(String text, FontStyle font) {
		this(text, null, font);
	}

	public Label(Image image) {
		this(null, image, null);
	}
	
	public Label() {
		this(null, null, null);
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

	public boolean respectProportions() {
		return respectProportions;
	}

	public void respectProportions(boolean respectProportions) {
		this.respectProportions = respectProportions;
	}
}
