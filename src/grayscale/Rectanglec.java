package grayscale;

/**
 * Interface to a read-only view of a rectangle.
 * 
 * @author Niels
 */
public interface Rectanglec {
	public float x();

	public float y();

	public float width();

	public float height();

	public float cx();

	public float cy();

	public float minx();

	public float miny();

	public float maxx();

	public float maxy();

	// double-versions ----------------------------------

	public double getX();

	public double getY();

	public double getWidth();

	public double getHeight();

	public double getCenterX();

	public double getCenterY();

	public double getMinX();

	public double getMinY();

	public double getMaxX();

	public double getMaxY();

	public Vector getDimensions();

	/**
	 * A rectangle is considered empty if encloses no area, in other words, if its
	 * width <= 0 or if its height <= 0.
	 * 
	 * @return {@code true} if this rectangle is empty, {@code false} otherwise
	 */
	public boolean isEmpty();

	/**
	 * @param other
	 * @return {@code true} if other intersects with this rectangle, {@code false}
	 *         otherwise
	 */
	public boolean intersects(Rectanglec other);

	/**
	 * Tests wether or not this rectangle contains the specified point
	 * 
	 * @param vx the x coordinate of the point to test
	 * @param vy the y coordinate of the point to test
	 * @return {@code true} if this rectangle contains the point {@code (vx, vy)},
	 *         {@code false} otherwise
	 */
	public boolean contains(double vx, double vy);

	public boolean contains(Vectorc v);
}
