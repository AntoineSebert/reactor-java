package time;

import org.jetbrains.annotations.NotNull;
import reactor.Declaration;
import reactor.Trigger;

/**
 * time.Timer specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#timer-declaration
 */
public class Timer extends Declaration implements Trigger {
	private Timestamp period = Timestamp.ZERO;
	private Timestamp offset = Timestamp.ZERO;
	private long time = Time.logical();

	public Timer(@NotNull String name) {
		super(name);
	}

	public Timer(@NotNull String name, @NotNull Timestamp period) {
		super(name);

		this.period = period;
	}

	/**
	 * @param name   name
	 * @param period period
	 * @param offset offset
	 * @throws ExceptionInInitializerError if the name is empty
	 */
	public Timer(@NotNull String name, @NotNull Timestamp period, @NotNull Timestamp offset) {
		super(name);

		this.period = period;
		this.offset = offset;
	}

	public Timestamp period() {
		return period;
	}

	public Timestamp offset() {
		return offset;
	}

	@Override
	public long timestamp() {
		return time;
	}
}
