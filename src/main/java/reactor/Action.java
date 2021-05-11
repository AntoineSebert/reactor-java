package reactor;

import org.jetbrains.annotations.NotNull;
import time.Timestamp;
import time.Time;

/**
 * Action specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#action-declaration
 */
public class Action<T> extends Declaration implements Trigger, Effect {
	public static Timestamp TIME_PRECISION;
	private final Type type;
	private Policy policy;
	private Timestamp minDelay;
	private Comparable<Timestamp> minSpacing;
	private long time, last;

	/**
	 * @param name name
	 * @param type type
	 */
	public Action(@NotNull String name, @NotNull Type type, @NotNull Policy policy, @NotNull Timestamp minDelay,
	              @NotNull Comparable<Timestamp> minSpacing) {
		super(name);

		if (minSpacing.compareTo(TIME_PRECISION) < 0)
			throw new ExceptionInInitializerError(
					"Action minimum time spacing must be greater than or equal to the time precision of the target");

		this.type = type;
		this.policy = policy;
		this.minDelay = minDelay;
		this.minSpacing = minSpacing;
		time = type == Type.logical ? Time.logical() + minDelay.time() : Time.physical();
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
	public Timestamp getMinDelay() {
		return minDelay;
	}

	/**
	 * @return the minimal spacing
	 */
	public Comparable<Timestamp> getMinSpacing() {
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
		private Timestamp minDelay = Timestamp.ZERO;
		private Timestamp minSpacing = Timestamp.ZERO;

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

		public Builder minDelay(@NotNull Timestamp minDelay) {
			this.minDelay = minDelay;

			return this;
		}

		public Builder minSpacing(@NotNull Timestamp minSpacing) {
			this.minSpacing = minSpacing;

			return this;
		}
	}
}
