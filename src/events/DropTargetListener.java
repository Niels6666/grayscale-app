package events;

import java.util.List;

import grayscale.Component;

public abstract class DropTargetListener {

	protected DropTargetListener() {}

	public void acceptDrop(Component comp, List<String> droppedFiles) {
	}
}
