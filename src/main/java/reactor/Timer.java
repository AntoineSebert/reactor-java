package reactor;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.UUID;

/**
 * reactor.Timer specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#timer-declaration
 */
public class Timer extends Declaration implements Trigger {
	private Duration period = Duration.ZERO;
	private Duration offset = Duration.ZERO;
	private final long time = Time.logical();
	private UUID uuid;

	public Timer(@NotNull String name) {
		super(name);
		this.uuid = UUID.randomUUID();
	}

	@Override
	public void toLF(int lvl) {

		StringBuilder state = new StringBuilder("\t".repeat(lvl) + getClass().getSimpleName() + " " + name
				+ ": (");

		if(offset != Duration.ZERO && period != Duration.ZERO) {
			state.append(offset.toNanos()).append(" nsec,");
		} else if (offset != Duration.ZERO) {
			state.append(offset.toNanos()).append(" nsec");
		}

		if(period != Duration.ZERO) {
			state.append(period.toNanos()).append(" nsec");
		}

		state.append(");");

		System.out.println(state);
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

	public String toString() {
		return String.valueOf(uuid);
	}
}
