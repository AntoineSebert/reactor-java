import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Timer specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#timer-declaration
 */
public class Timer implements Trigger {
	private String name;
	private Time period;
	private Time offset;

	/**
	 * @param name name
	 * @param period period
	 * @param offset offset
	 * @throws ExceptionInInitializerError if the name is empty
	 */
	public Timer(@NotNull String name, @NotNull Optional<Time> period, @NotNull Optional<Time> offset) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		this.name = name;
		this.period = period.orElse(new Time(0, Optional.empty()));
		this.offset = offset.orElse(new Time(0, Optional.empty()));
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the period
	 */
	public Time getPeriod() {
		return period;
	}

	/**
	 * @return the offset
	 */
	public Time getOffset() {
		return offset;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Timer) o).name);
	}
}
