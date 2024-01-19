package grayscale;

/**
 * Controls the behavior of a component, and how it shoud be drawn.
 * 
 * @author Niels
 */
public abstract class ComponentUI {

	public abstract void installUI(Component comp);

	public abstract void update(Component comp);

	public abstract void render(Component comp, Graphics g);
}
