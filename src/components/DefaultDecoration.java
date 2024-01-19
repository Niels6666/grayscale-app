package components;

import grayscale.Application;
import grayscale.FontStyle;
import grayscale.Graphics;
import grayscale.Image;
import grayscale.Window;

public class DefaultDecoration extends Decoration {
	private Window owner;
	private Image icon;
	private String title;
	private FontStyle font;

	public DefaultDecoration(Image icon, String title, FontStyle font) {
		this.icon = icon;
		this.title = title;
		this.font = font;
	}

	@Override
	public void installDecoration(Window owner) {
		this.owner = owner;
	}
}
