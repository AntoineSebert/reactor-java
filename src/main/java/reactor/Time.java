package reactor;

import java.time.Duration;

public class Time {
	private static long logical;

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

	public static boolean has_passed(long old, Duration time) {
		return time.plus(Duration.ofNanos(old)).toNanos() < physical();
	}

	public static void next() {
		logical++;
	}
}
