package reactor;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Action specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#action-declaration
 */
public class Action<T> extends Declaration implements Trigger {
	public static Duration TIME_PRECISION;
	private final Type type;
	private Policy policy;
	private Duration minDelay;
	private Duration minSpacing;
	private long time, last;

	/**
	 * @param name name
	 * @param type type
	 */
	public Action(@NotNull String name, @NotNull Type type, @NotNull Policy policy, @NotNull Duration minDelay,
	              @NotNull Duration minSpacing) {
		super(name);

		if (minSpacing.compareTo(TIME_PRECISION) < 0)
			throw new ExceptionInInitializerError(
					"Action minimum time spacing must be greater than or equal to the time precision of the target");

		this.type = type;
		this.policy = policy;
		this.minDelay = minDelay;
		this.minSpacing = minSpacing;
		time = type == Type.logical ? Time.logical() + minDelay.toNanos() : Time.physical();
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
	public Duration getMinDelay() {
		return minDelay;
	}

	/**
	 * @return the minimal spacing
	 */
	public Duration getMinSpacing() {
		return minSpacing;
	}

	@Override
	public long timestamp() {
		return time;
	}

	public long getLast() {
		return last;
	}

	public void setLast(long timestamp) {
		last = timestamp;
	}

	@Override
	public void ToLF(int lvl) {
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
		private final String name;
		private final Type type;
		private Policy policy = Policy.defer;
		private Duration minDelay = Duration.ZERO;
		private Duration minSpacing = Duration.ZERO;

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

		public Builder minDelay(@NotNull Duration minDelay) {
			this.minDelay = minDelay;

			return this;
		}

		public Builder minSpacing(@NotNull Duration minSpacing) {
			this.minSpacing = minSpacing;

			return this;
		}
	}
}
