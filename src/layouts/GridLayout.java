package layouts;

import java.util.List;
import grayscale.Component;
import grayscale.Layout;
import grayscale.Rectangle;
import grayscale.Rectanglec;

public class GridLayout extends Layout {
	private int per_row;
	private float min_width;
	private float min_height;

	public GridLayout(int per_row, float min_width, float min_height) {
		this.per_row = per_row;
		this.min_width = min_width;
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
