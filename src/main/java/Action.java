import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Action specification class.
 */
public class Action<T> implements Trigger, Effect {
	private String name;
	private Type type;
	private Policy policy;
	private Time minDelay;
	private Optional<Time> minSpacing;
	private Optional<T> value;

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
	 * @param policy policy
	 * @param minDelay minimal delay
	 * @param minSpacing minimal spacing
	 * @param value value
	 */
	public Action(@NotNull String name, @NotNull Type type, @NotNull Optional<Policy> policy,
	              @NotNull Optional<Time> minDelay, @NotNull Optional<Time> minSpacing, @NotNull Optional<T> value) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		if (minSpacing.isPresent() && minSpacing.get().time() < 0/*Program.getMinTimePrecision() or Target.getMinTimePrecision()*/)
				throw new ExceptionInInitializerError(
						"Action minimum time spacing must be greater than or equal to the time precision of the target");

		this.name = name;
		this.type = type;
		this.policy = policy.orElse(Policy.defer);
		this.minDelay = minDelay.orElse(new Time(0, Optional.empty()));
		this.minSpacing = minSpacing;
		this.value = value;
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
	public Optional<Time> getMinSpacing() {
		return minSpacing;
	}

	/**
	 * @return the value
	 */
	public Optional<T> getValue() {
		return value;
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
}
