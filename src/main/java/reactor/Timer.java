package reactor;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * reactor.Timer specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#timer-declaration
 */
public class Timer extends Declaration implements Trigger {
	private Duration period = Duration.ZERO;
	private Duration offset = Duration.ZERO;
	private final long time = Time.logical();

	public Timer(@NotNull String name) {
		super(name);
	}

	@Override
	public void ToLF(int lvl) {
	}

	public Timer(@NotNull String name, @NotNull Duration period) {
		super(name);

		this.period = period;
	}

	/**
	 * @param name   name
	 * @param period period
	 * @param offset offset
	 * @throws ExceptionInInitializerError if the name is empty
	 */
	public Timer(@NotNull String name, @NotNull Duration period, @NotNull Duration offset) {
		super(name);

		this.period = period;
		this.offset = offset;
	}

	public Duration period() {
		return period;
	}

	public Duration offset() {
		return offset;
	}

	@Override
	public long timestamp() {
		return time;
	}
}
