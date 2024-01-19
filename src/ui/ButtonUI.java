package ui;

import components.Button;
import events.Mouse;
import events.MouseListener;
import grayscale.Component;
import grayscale.ComponentUI;
import grayscale.Graphics;
import grayscale.Vectorc;

public class ButtonUI extends ComponentUI {
	
	private boolean pressed = false;
	private boolean hovered = false;

	private class ButtonMS extends MouseListener{
		@Override
		public void mouseClicked(Component comp, int button, int mods, Vectorc pos, boolean strict_clicked) {
			Button btn = (Button) comp;
			if (button == Mouse.BTN_LEFT) {
				Runnable action = btn.getAction();
				if (action != null) {
					action.run();
				}
			}
		}
		
		@Override
		public void mousePressed(Component comp, int button, int mods, Vectorc pos) {
			if (button == Mouse.BTN_LEFT) {
				pressed = true;
			}
		}
		
		@Override
		public void mouseReleased(Component comp, int button, int mods, Vectorc pos, boolean drop) {
			pressed = false;
		}
	}
	
	
	@Override
	public void installUI(Component comp) {
		comp.setMouseListener(new ButtonMS());
	}

	@Override
	public void update(Component comp) {
	}

	@Override
	public void render(Component comp, Graphics g) {
		g.setFillColor(comp.getBackground());
		g.beginPath();
		g.rect(comp.getBounds());
		g.fill();
	}

}
