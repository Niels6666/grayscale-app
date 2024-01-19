package layouts;

import java.util.List;
import grayscale.Component;
import grayscale.Layout;
import grayscale.Rectangle;
import grayscale.Rectanglec;

public class DynamicRow extends Layout {
	private float min_width;
	private int vAlign;
	private int hAlign;

	public DynamicRow(float min_width, int vAlign, int hAlign) {
		this.min_width = min_width;
		this.vAlign = vAlign;
		this.hAlign = hAlign;
	}

	@Override
	public void update(List<Component> comps, Rectanglec r) {
		double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r.getHeight();
		
		Rectangle bounds = new Rectangle();
		int num_columns = comps.size();
		for (int i = 0; i < num_columns; i++) {
			double compWidth = Math.max(w / num_columns, min_width);
			double currentX = x + i * compWidth;
			double currentY = y;
			bounds.set(currentX, currentY, compWidth, h);
			comps.get(i).setBounds(bounds);
		}
	}

}
