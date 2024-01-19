package layouts;

import java.util.List;

import grayscale.Component;
import grayscale.Layout;
import grayscale.Rectangle;
import grayscale.Rectanglec;

public class DynamicColumn extends Layout {
	private float min_height;

	public DynamicColumn(float min_height) {
		this.min_height = min_height;
	}

	@Override
	public void update(List<Component> comps, Rectanglec r) {
		double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r.getHeight();
		Rectangle bounds = new Rectangle();
		int num_columns = comps.size();
		for (int i = 0; i < num_columns; i++) {
			double compHeight = Math.max(h / num_columns, min_height);
			double currentX = x;
			double currentY = y + i * compHeight;
			bounds.set(currentX, currentY, w, compHeight);
			comps.get(i).setBounds(bounds);
		}
	}
}
