package reactor;

import org.jetbrains.annotations.NotNull;

/**
 * reactor.Timer specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#timer-declaration
 */
public class Timer extends Declaration implements Trigger {
	private Time period = Time.ZERO;
	private Time offset = Time.ZERO;

	public Timer(@NotNull String name) {
		super(name);
	}

	public Timer(@NotNull String name, @NotNull Time period) {
		super(name);

		this.period = period;
	}

	/**
	 * @param name   name
	 * @param period period
	 * @param offset offset
	 * @throws ExceptionInInitializerError if the name is empty
	 */
	public Timer(@NotNull String name, @NotNull Time period, @NotNull Time offset) {
		super(name);

		this.period = period;
		this.offset = offset;
	}
}
