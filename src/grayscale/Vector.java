package grayscale;

import org.joml.Vector2d;

public class Vector implements Vectorc {
	private double x;
	private double y;

	public Vector() {
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector(Vectorc other) {
		x = other.getX();
		y = other.getY();
	}

	public float x() {
		return (float) x;
	}

	public float y() {
		return (float) y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vector set(double v) {
		x = v;
		y = v;
		return this;
	}

	public Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector set(Vectorc other) {
		x = other.getX();
		y = other.getY();
		return this;
	}

	public Vector mul(float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	public Vector div(float scalar) {
		x /= scalar;
		y /= scalar;
		return this;
	}

	public Vector mul(Vectorc other) {
		x *= other.x();
		y *= other.y();
		return this;
	}

	public Vector div(Vectorc other) {
		x /= other.x();
		y /= other.y();
		return this;
	}

	public Vector add(Vectorc other) {
		x += other.x();
		y += other.y();
		return this;
	}

	public Vector add(double xadd, double yadd) {
		x += xadd;
		y += yadd;
		return this;
	}

	public Vector sub(Vectorc other) {
		x -= other.x();
		y -= other.y();
		return this;
	}

	public Vector sub(double xsub, double ysub) {
		x -= xsub;
		y -= ysub;
		return this;
	}

	public Vector lerp(Vectorc v, double u) {
		x = x + u * (v.x() - x);
		y = y + u * (v.y() - y);
		return this;
	}

	public static void lerp(Vectorc v, Vectorc w, double u, Vector dest) {
		double xres = v.x() + u * (w.x() - v.x());
		double yres = v.y() + u * (w.y() - v.y());
		dest.set(xres, yres);
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector normalize() {
		double invLength = 1.0 / length();
		x *= invLength;
		y *= invLength;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Vectorc) {
			return x == ((Vectorc) obj).getX() && y == ((Vectorc) obj).getY();
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + x + " ; " + y + ")";
	}
}
