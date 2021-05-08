public class Time {
	static long logical; // 0

	public static long physical() {
		return System.currentTimeMillis();
	}

	public static long delay(int span) {
		assert 0 <= span;
		logical += span;

		return logical;
	}

	public static long match() {
		assert logical <= physical();
		long diff = physical() - logical;
		logical = physical();

		return diff;
	}
}
