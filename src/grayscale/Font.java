package grayscale;

import java.io.IOException;
import java.nio.ByteBuffer;

import utils.IOUtil;

public class Font {
	private final String name;
	private final ByteBuffer data;

	public Font(String name, String filepath) {
		this.name = name;
		try {
			data = IOUtil.ioResourceToByteBuffer(filepath, 1024 * 1024);
		} catch (IOException e) {
			throw new RuntimeException("Could not load font " + name + " from disk.", e);
		}
	}

	public String getName() {
		return name;
	}

	public ByteBuffer getData() {
		return data;
	}
}
