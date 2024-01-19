package ui;

import grayscale.Component;
import grayscale.ComponentUI;
import grayscale.Graphics;

public class PanelUI extends ComponentUI {

	@Override
	public void installUI(Component comp) {
	}

	@Override
	public void update(Component comp) {
	}

	@Override
	public void render(Component comp, Graphics g) {
		g.beginPath();
		g.rect(comp.getBounds());
		g.setFillColor(comp.getBackground());
		g.fill();
	}

}
