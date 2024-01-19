package grayscale;

public class FontStyle {
	private final String name;
	private final float size;

	public FontStyle(String name, float size) {
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public float getSize() {
		return size;
	}
}
