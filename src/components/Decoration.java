package components;

import grayscale.Component;
import grayscale.Window;
import ui.DecorationUI;

/**
 * Window decoration
 * 
 * @author Niels
 */
public abstract class Decoration extends Component {

	public Decoration() {
		super(new DecorationUI());
	}

	public abstract void installDecoration(Window owner);
}
