package reactor;

import org.jetbrains.annotations.NotNull;

/**
 * reactor.Timer specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#timer-declaration
 */
public record Timer(String name, Time period, Time offset) implements Trigger {
	/**
	 * @param name   name
	 * @param period period
	 * @param offset offset
	 * @throws ExceptionInInitializerError if the name is empty
	 */
	public Timer(@NotNull String name, @NotNull Time period, @NotNull Time offset) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		this.name = name;
		this.period = period;
		this.offset = offset;
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

	public static class Builder {
		private String name;
		private Time period = Time.ZERO;
		private Time offset = Time.ZERO;

		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Timer build() {
			return new Timer(name, period, offset);
		}

		public Builder period(@NotNull Time period) {
			this.period = period;

			return this;
		}

		public Builder offset(@NotNull Time offset) {
			this.offset = offset;

			return this;
		}
	}
}
