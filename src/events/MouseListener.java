package events;

import grayscale.Component;
import grayscale.Vector;
import grayscale.Vectorc;

public abstract class MouseListener {
	protected MouseListener() {
	}

	public void mouseClicked(Component comp, int button, int mods, Vectorc pos, boolean strict_clicked) {
	}

	public void mousePressed(Component comp, int button, int mods, Vectorc pos) {
	}

	/**
	 * @param drop if the mouse was released on comp
	 */
	public void mouseReleased(Component comp, int button, int mods, Vectorc pos, boolean drop) {
	}

	public void mouseEntered(Component comp) {
	}

	public void mouseExited(Component comp) {
	}

	public void mouseMoved(Component comp, Vectorc prev_pos, Vectorc pos) {
	}

	public void mouseDragged(Component comp, int buttons, int mods, Vectorc press_start_pos, Vectorc prev_pos, Vectorc pos) {
	}
}
