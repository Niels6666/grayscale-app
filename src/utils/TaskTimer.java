package utils;

/**
 * The task is immediately ran when calling the constructor. The time is
 * measured like this : <br>
 * 
 * <pre>
 * long start = System.nanoTime();
 * task.run();
 * long end = System.nanoTime();
 * elapsedTime = end - start;
 * </pre>
 * 
 * <p>
 * Default unit is {@link UNITS#milliseconds}
 * </p>
 */
public class TaskTimer {

	public static enum UNITS {
		seconds(1.0e+9), milliseconds(1.0e+6), nanoseconds(1.0);

		final double mult;

		UNITS(double mult) {
			this.mult = mult;
		}
	}

	private final long timeNanos;
	private final String name;
	private UNITS unit = UNITS.milliseconds;

	public TaskTimer(Runnable task, String name) {
		this.name = name;
		long start = System.nanoTime();
		task.run();
		long end = System.nanoTime();
		timeNanos = end - start;
	}

	public void setUnit(UNITS unit) {
		this.unit = unit;
	}

	public long getTimeNanos() {
		return timeNanos;
	}

	public double getTimeMillis() {
		return timeNanos / 1.0e+6;
	}

	public double getTimeSeconds() {
		return timeNanos / 1.0e+9;
	}

	@Override
	public String toString() {
		return "Task [" + name + "] took " + (getTimeNanos() / unit.mult) + " " + unit.toString();
	}

}
