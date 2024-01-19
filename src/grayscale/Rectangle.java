package grayscale;

/**
 * Represents a rectangle with a top-left corner (x, y) and bottom-right corner
 * (x+width, y+height)
 * 
 * @author Niels
 */
public class Rectangle implements Rectanglec {
	private double x;
	private double y;
	private double width;
	private double height;

	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle(Rectanglec other) {
		this(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}

	/**
	 * Initializes an empty rectangle with x=0, y=0, width=0, height=0
	 */
	public Rectangle() {
		this(0, 0, 0, 0);
	}

	public float x() {
		return (float) x;
	}

	public float y() {
		return (float) y;
	}

	public float width() {
		return (float) width;
	}

	public float height() {
		return (float) height;
	}

	public float cx() {
		return (float) (x + width / 2f);
	}

	public float cy() {
		return (float) (y + height / 2f);
	}

	@Override
	public float minx() {
		return (float) x;
	}

	@Override
	public float miny() {
		return (float) y;
	}

	@Override
	public float maxx() {
		return (float) (x + width);
	}

	@Override
	public float maxy() {
		return (float) (y + height);
	}

	// double-versions ----------------------------------

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getCenterX() {
		return x + width / 2f;
	}

	public double getCenterY() {
		return y + height / 2f;
	}

	public double getMinX() {
		return x;
	}

	public double getMinY() {
		return y;
	}

	public double getMaxX() {
		return x + width;
	}

	public double getMaxY() {
		return y + height;
	}

	public Vector getDimensions() {
		return new Vector(width, height);
	}

	/**
	 * Sets this rectangle's coordinates and dimensions.
	 * 
	 * @param x      the x coordinate of this rectangle upper-left corner
	 * @param y      the y coordinate of this rectangle upper-left corner
	 * @param width  the new width of this rectangle
	 * @param height the new height of this rectangle
	 */
	public Rectangle set(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		return this;
	}

	public Rectangle set(Rectanglec other) {
		this.x = other.getX();
		this.y = other.getY();
		this.width = other.getWidth();
		this.height = other.getHeight();
		return this;
	}

	public Rectangle setRectFromCenter(double cx, double cy, double w, double h) {
		double halfW = w * 0.5, halfH = h * 0.5;
		return this.set(cx - halfW, cy - halfH, cx + halfW, cy + halfH);
	}

	public Rectangle setRectFromDiagonal(double x1, double y1, double x2, double y2) {
		if (x2 < x1) {
			double t = x1;
			x1 = x2;
			x2 = t;
		}
		if (y2 < y1) {
			double t = y1;
			y1 = y2;
			y2 = t;
		}
		return set(x1, y1, x2 - x1, y2 - y1);
	}

	/**
	 * Creates and return a new {@code Rectangle} whose center is {@code (cx, cy)}
	 * 
	 * @param cx center point x coordinate
	 * @param cy center point y coordinate
	 * @param w  the width of the rectangle
	 * @param h  the height of the rectangle
	 * @return the created rectangle
	 */
	public static Rectangle from_center(double cx, double cy, double w, double h) {
		return new Rectangle(cx - w * 0.5, cy - h * 0.5, w, h);
	}

	public static Rectangle from_diagonal(double x1, double y1, double x2, double y2) {
		if (x2 < x1) {
			double t = x1;
			x1 = x2;
			x2 = t;
		}
		if (y2 < y1) {
			double t = y1;
			y1 = y2;
			y2 = t;
		}
		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	public boolean isEmpty() {
		return (width <= 0 || height <= 0);
	}

	/**
	 * Adds insets to this rectangle.
	 * 
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 * @return {@code this}
	 */
	public Rectangle addInsets(double top, double left, double bottom, double right) {
		x += left;
		y += top;
		width -= right + left;
		height -= bottom + top;
		return this;
	}

	/**
	 * Adds insets to this rectangle.
	 * 
	 * @return {@code this}
	 */
	public Rectangle addInsets(Insets insets) {
		x += insets.left;
		y += insets.top;
		width -= insets.right + insets.left;
		height -= insets.bottom + insets.top;
		return this;
	}

	public boolean intersects(Rectanglec other) {
		if (isEmpty() || other.isEmpty()) {
			return false;
		}
		return (x + width > other.getX() && //
				y + height > other.getY() && //
				x < other.getX() + other.getWidth() && //
				y < other.getY() + other.getHeight());
	}

	public boolean contains(double vx, double vy) {
		return (getMinX() < vx && vx < getMaxX()) && (getMinY() < vy && vy < getMaxY());
	}

	public boolean contains(Vectorc v) {
		return contains(v.getX(), v.getY());
	}

	/**
	 * Compute the intersection of r1 and r2 and put the result into {@code dest}
	 * 
	 * @param r1
	 * @param r2
	 * @param dest
	 */
	public static void intersection(Rectangle r1, Rectangle r2, Rectangle dest) {
		double x1 = Math.max(r1.getMinX(), r1.getMinX());
		double y1 = Math.max(r1.getMinY(), r1.getMinY());
		double x2 = Math.min(r1.getMaxX(), r1.getMaxX());
		double y2 = Math.min(r1.getMaxY(), r1.getMaxY());
		dest.set(x1, y1, x2 - x1, y2 - y1);
	}

	/**
	 * Compute the union of r1 and r2 and put the result into {@code dest}
	 * 
	 * @param r1
	 * @param r2
	 * @param dest
	 */
	public static void union(Rectangle r1, Rectangle r2, Rectangle dest) {
		double x1 = Math.min(r1.getMinX(), r2.getMinX());
		double y1 = Math.min(r1.getMinY(), r2.getMinY());
		double x2 = Math.max(r1.getMaxX(), r2.getMaxX());
		double y2 = Math.max(r1.getMaxY(), r2.getMaxY());
		dest.setRectFromDiagonal(x1, y1, x2, y2);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Rectangle) {
			Rectangle b = (Rectangle) obj;
			return b.x == x && b.y == y && b.width == width && b.height == height;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
	}

}
