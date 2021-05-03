package reactor;

import org.jetbrains.annotations.NotNull;

/**
 * Action specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#action-declaration
 */
public class Action<T> implements Trigger, Effect {
	public static Time TIME_PRECISION;
	private final String name;
	private final Type type;
	private Policy policy;
	private Time minDelay;
	private Comparable<Time> minSpacing;

	/**
	 * @param name name
	 * @param type type
	 */
	public Action(@NotNull String name, @NotNull Type type, @NotNull Policy policy, @NotNull Time minDelay,
	              @NotNull Comparable<Time> minSpacing) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		if (minSpacing.compareTo(TIME_PRECISION) < 0)
			throw new ExceptionInInitializerError(
					"Action minimum time spacing must be greater than or equal to the time precision of the target");

		this.name = name;
		this.type = type;
		this.policy = policy;
		this.minDelay = minDelay;
		this.minSpacing = minSpacing;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the policy
	 */
	public Policy getPolicy() {
		return policy;
	}

	/**
	 * @return the minimal delay
	 */
	public Time getMinDelay() {
		return minDelay;
	}

	/**
	 * @return the minimal spacing
	 */
	public Comparable<Time> getMinSpacing() {
		return minSpacing;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Action<?>) o).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public enum Type {
		logical,
		physical
	}

	public enum Policy {
		defer,
		drop,
		replace
	}

	public static class Builder {
		private String name;
		private Type type;
		private Policy policy = Policy.defer;
		private Time minDelay = Time.ZERO;
		private Time minSpacing = Time.ZERO;

		public Builder(@NotNull String name, @NotNull Type type) {
			this.name = name;
			this.type = type;
		}

		public Action<?> build() {
			return new Action<>(name, type, policy, minDelay, minSpacing);
		}

		public Builder policy(@NotNull Policy policy) {
			this.policy = policy;

			return this;
		}

		public Builder minDelay(@NotNull Time minDelay) {
			this.minDelay = minDelay;

			return this;
		}

		public Builder minSpacing(@NotNull Time minSpacing) {
			this.minSpacing = minSpacing;

			return this;
		}
	}
}
