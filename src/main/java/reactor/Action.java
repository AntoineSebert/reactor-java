package reactor;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Optional;
import java.util.TreeMap;

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
	private long timestamp/*future?*/, last/*past*/;
	private TreeMap<Long, T> payload = new TreeMap<>();

	public enum Type {
		logical,
		physical
	}

	public enum Policy {
		defer,
		drop,
		replace
	}

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
	}

	/**
	 * @return the type
	 */
	public Type type() {
		return type;
	}

	/**
	 * @return the policy
	 */
	public Policy policy() {
		return policy;
	}

	/**
	 * @return the minimal delay
	 */
	public Duration minDelay() {
		return minDelay;
	}

	/**
	 * @return the minimal spacing
	 */
	public Duration minSpacing() {
		return minSpacing;
	}

	public T get(long timestamp) {
		return payload.floorEntry(timestamp).getValue();
	}

	public void add(Object payload) {
		this.payload.put(timestamp, (T) payload);
	}

	public void replace(Object payload) {
		this.payload.replace(this.payload.lastKey(), (T) payload);
	}

	@Override
	public long timestamp() {
		return timestamp;
	}

	public long last() {
		return last;
	}

	public long setLast() { return last = timestamp; }

	@Override
	public void toLF(int lvl) {
		StringBuilder builder = new StringBuilder();
		builder.append("\t".repeat(lvl))
			.append(type == Type.logical ? "logical " : "physical ")
			.append(name)
			.append(minDelay.isZero() ? "" : " (" + minDelay.toNanos())
			.append(minSpacing.isZero() ? "" : ", " + minSpacing.toNanos())
			.append(policy != null ? ", " + policy.name() : "")
			.append(minDelay.isZero() ? "" : ")")
		    .append(";");


		System.out.println(builder.toString());
	}

	public int start(Reaction r, long offset) {
		assert type == Type.logical;

		long tag = r.timestamp() + minDelay.toNanos() + offset;

		if(minSpacing != Duration.ZERO && last + minSpacing.toNanos() > tag)
			switch(policy) {
				case drop:
					return -1;
				case replace:
					timestamp = tag;
					return 0;
				case defer:
					timestamp = last + minSpacing.toNanos();

					return 1;
			}

		timestamp = tag;
		return 1;
	}

	public int start(long offset) {
		assert type == Type.physical;
		long tag = Time.physical();

		if(minSpacing != Duration.ZERO && last + minSpacing.toNanos() > tag)
			switch(policy) {
				case drop:
					return -1;
				case replace:
					timestamp = tag;
					return 0;
				case defer:
					timestamp = last + minSpacing.toNanos();

					return 1;
			}

		timestamp = tag;
		return 1;
	}

	public static class Builder {
		private final String name;
		private final Type type;
		private Policy policy = Policy.defer;
		private Duration minDelay = Duration.ZERO;
		private Duration minSpacing = Duration.ZERO;
		private Optional<?> payload = Optional.empty();

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

		public Builder payload(@NotNull Object payload) {
			this.payload = Optional.of(payload);

			return this;
		}
	}
}
