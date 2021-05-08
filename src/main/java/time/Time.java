package time;

import java.lang.Math;

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

	public static long convert(Timestamp time) {
		long factor = 1;

		switch (time.unit().orElse(Unit.nsec)) {
			case nsec:
				// nothing to do
				break;
			case usec:
				factor = (int) Math.pow(10, 3);
				break;
			case msec:
				factor = (int) Math.pow(10, 6);
				break;
			case sec:
			case secs:
				factor = (int) Math.pow(10, 9);
				break;
			case minute:
			case minutes:
				factor = (int) Math.pow(10, 9) * 60;
				break;
			case hour:
			case hours:
				factor = (int) Math.pow(10, 9) * 3_600;
				break;
			case day:
			case days:
				factor = (int) Math.pow(10, 9) * 86_400;
				break;
			case week:
			case weeks:
				factor = (int) Math.pow(10, 9) * 604_800;
				break;
			default:
				factor = 1;
		}

		return time.time() * factor;
	}

	public static boolean has_passed(long old, Timestamp time) {
		if (time.unit().isEmpty())
			throw new RuntimeException("Cannot determine deadline state if provided time has no time unit.");

		return old + convert(time) < physical();
	}
}
