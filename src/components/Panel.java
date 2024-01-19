package components;

import grayscale.Component;
import ui.PanelUI;
import utils.Colors;

/**
 * A pannel usually draws nothing, but can hold other components.
 * @author Niels
 */
public class Panel extends Component {
	public Panel() {
		super(new PanelUI());
		setBackground(Colors.lightGray);
	}
}
