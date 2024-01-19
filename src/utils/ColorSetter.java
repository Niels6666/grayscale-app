package utils;

import org.lwjgl.nanovg.NVGColor;

/**
 * This interface is usefull to pass {@link Colors} class methods as lambdas. For example :
 * <pre>
 * Graphics g = ...
 * g.beginPath();
 * ......
 * g.setStrokeColor(Colors::orange);
 * g.stroke();
 * </pre>
 * This is a functional interface.
 * @author Niels
 */
@FunctionalInterface
public interface ColorSetter {
	/**
	 * Sets each of the given {@code NVGColor}'s component to a certain value.
	 * @param color the color to set
	 * @return {@code color}
	 */
	public NVGColor set(NVGColor color);
}
