package grayscale;

public class Insets {
	public float top;
	public float left;
	public float bottom;
	public float right;

	/**
	 * All insets initialized to 0f
	 */
	public Insets() {
	}
	
	public Insets(float top, float left, float bottom, float right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public void set(float top, float left, float bottom, float right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}
}
