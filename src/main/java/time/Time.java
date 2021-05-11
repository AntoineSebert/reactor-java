package time;

import java.util.concurrent.TimeUnit;

public class Time {
	static long logical; // 0

	public static long physical() {
		return System.nanoTime();
	}

	public static long logical() {
		return logical;
	}

	public static long advance(int delay) {
		assert 0 <= delay;
		logical += delay;

		return logical;
	}

	public static long match() {
		assert logical <= physical();
		long diff = physical() - logical;
		logical = physical();

		return diff;
	}

	public static boolean has_passed(long old, Timestamp time) {
		if (time.unit().isEmpty())
			throw new RuntimeException("Cannot determine deadline state if provided timespan has no time unit.");

		return old + TimeUnit.NANOSECONDS.convert(time.time(), time.unit().get()) < physical();
	}
}
