package ui;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGPaint;

import components.Label;
import grayscale.Component;
import grayscale.ComponentUI;
import grayscale.FontStyle;
import grayscale.Graphics;
import grayscale.Image;
import grayscale.Rectangle;
import grayscale.Rectanglec;
import grayscale.Vector;
import grayscale.Vectorc;

public class LabelUI extends ComponentUI {

	@Override
	public void installUI(Component comp) {
	}

	@Override
	public void update(Component comp) {
	}

	@Override
	public void render(Component comp, Graphics g) {
		Label label = (Label) comp;
		Image image = label.getImage();
		String text = label.getText();

		if (image == null && text == null) {
			return;
		}

		g.beginPath();
		g.rect(label.getBounds());
		g.setFillColor(label.getBackground());
		g.fill();

		ByteBuffer textBBF = label.getTextBBF();
		if (image != null) {
			if (text != null) {
				drawImageAndText(label, image, textBBF, g);
			} else {
				drawImage(label, image, g);
			}
		} else {
			drawText(label, textBBF, g);
		}
	}

	private void drawImageAndText(Label label, Image image, ByteBuffer text, Graphics g) {
		Rectangle strB = new Rectangle();
		g.setFont(label.getFont());
		g.getTextBounds(0, 0, text, strB);
		int textPlacement = label.getTextPlacement();
		int textAlignement = label.getTextAlignment();
		float textW = strB.width(), textH = strB.height();
		Rectanglec lb = label.getBounds();

		Rectangle temp = new Rectangle(lb);
		Vector imgD = getImageDimensions((switch (textPlacement) {
		case Label.TOP -> temp.addInsets(textH, 0, 0, 0);
		case Label.LEFT -> temp.addInsets(0, textW, 0, 0);
		case Label.BOTTOM -> temp.addInsets(0, 0, textH, 0);
		case Label.RIGHT -> temp.addInsets(0, 0, 0, textW);
		default -> throw new IllegalArgumentException("Unexpected value: " + textPlacement);
		}).getDimensions(), image, g);
		Rectangle imgB = Rectangle.from_center(temp.cx(), temp.cy(), imgD.x(), imgD.y());

		Vector strPos = getStrCoords((switch (textPlacement) {
		case Label.TOP -> strB.set(lb.x(), lb.y(), lb.width(), imgB.y() - lb.y());
		case Label.LEFT -> strB.set(lb.x(), lb.y(), imgB.x() - lb.x(), lb.height());
		case Label.BOTTOM -> strB.set(lb.x(), imgB.maxy(), lb.width(), lb.maxy() - imgB.maxy());
		case Label.RIGHT -> strB.set(imgB.maxx(), lb.y(), lb.maxx() - imgB.maxx(), lb.height());
		default -> throw new IllegalArgumentException("Unexpected value: " + textPlacement);
		}), textAlignement);

		// draw the image and the text
		NVGPaint p = g.getImagePattern(imgB.x(), imgB.y(), imgB.width(), imgB.height(), 0, 1f, image);
		g.beginPath();
		g.rect(imgB);
		g.setFillPaint(p);
		g.fill();

		g.setTextAlign(textAlignement);
		g.setFillColor(label.getForeground());
		g.text(strPos.x(), strPos.y(), text);
	}

	private void drawImage(Label label, Image image, Graphics g) {
		Rectanglec bounds = label.getBounds();
		Vectorc labelD = bounds.getDimensions();
		Vector imgD = new Vector(labelD);
		if (label.respectProportions()) {
			imgD = getImageDimensions(labelD, image, g);
		}
		float imgw = imgD.x(), imgh = imgD.y();
		float imgx = bounds.cx() - imgw / 2f, imgy = bounds.cy() - imgh / 2f;
		NVGPaint p = g.getImagePattern(imgx, imgy, imgw, imgh, 0, 1f, image);
		g.beginPath();
		g.rect(imgx, imgy, imgw, imgh);
		g.setFillPaint(p);
		g.fill();
	}

	private void drawText(Label label, ByteBuffer text, Graphics g) {
		FontStyle font = label.getFont();
		g.setFont(font);
		g.setTextAlign(label.getTextAlignment());
		g.setFillColor(label.getForeground());
		Vector str = getStrCoords(label.getBounds(), label.getTextAlignment());
		g.text(str.x(), str.y(), text);
	}

	private Vector getStrCoords(Rectanglec bounds, int alignement) {
		int halign = alignement & 0b111;
		int valign = alignement & 0b1111000;

		float strx = switch (halign) {
		case Graphics.ALIGN_CENTER -> bounds.cx();
		case Graphics.ALIGN_LEFT -> bounds.x();
		case Graphics.ALIGN_RIGHT -> bounds.maxx();
		default -> throw new IllegalArgumentException("Unexpected value: " + halign);
		};

		float stry = switch (valign) {
		case Graphics.ALIGN_MIDDLE -> bounds.cy();
		case Graphics.ALIGN_BOTTOM -> bounds.y();
		case Graphics.ALIGN_TOP -> bounds.maxy();
		case Graphics.ALIGN_BASELINE -> bounds.maxy();
		default -> throw new IllegalArgumentException("Unexpected value: " + valign);
		};

		return new Vector(strx, stry);
	}

	private Vector getImageDimensions(Vectorc availableSpace, Image image, Graphics g) {
		Vector imgD = new Vector(image.getWidth(), image.getHeight());
		imgD.div(g.getContentScale());// adapt to application scale
		Vector ratios = new Vector(imgD).div(availableSpace);
		float maxRatio = Math.max(ratios.x(), ratios.y());
		return imgD.div(maxRatio);// respect proportions
	}

}
