package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import time.Timestamp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

/**
 * Reaction specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reaction-declaration
 */
public class Reaction {
	private Reactor self;
	private final HashSet<Trigger> triggers ;
	private final HashSet<Input<?>> uses;
	private final HashSet<Effect> effects;
	private final Function<Reaction, Void> targetCode;
	private final Deadline deadline;
	
	public Reaction(@NotNull HashSet<Trigger> triggers, @NotNull HashSet<Input<?>> uses,
	                @NotNull HashSet<Effect> effects, @NotNull Function<Reaction, Void> targetCode,
	                @NotNull Deadline deadline) {
		this.triggers = triggers;
		this.uses = uses;
		this.effects = effects;
		this.targetCode = targetCode;
		this.deadline = deadline;
	}

	/**
	 * @return the triggers
	 */
	public HashSet<Trigger> getTriggers() {
		return triggers;
	}

	/**
	 * @return the uses
	 */
	public HashSet<Input<?>> getUses() {
		return uses;
	}

	/**
	 * @return the effects
	 */
	public HashSet<Effect> getEffects() {
		return effects;
	}

	public Reactor self() {
		return self;
	}

	/**
	 * @return the target code
	 */
	public Function<Reaction, Void> getTargetCode() {
		return targetCode;
	}

	public void self(@NotNull Reactor self) {
		this.self = self;
	}

	/**
	 * Runs the target code.
	 */
	public void run() {
		targetCode.apply(this);

		// TODO : check
			// call deadline handler
	}

	public Deadline deadline() {
		return deadline;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reaction reaction = (Reaction) o;
		return triggers.equals(reaction.triggers) && uses.equals(reaction.uses) && effects.equals(reaction.effects);
	}

	@Override
	public int hashCode() {
		return Objects.hash(triggers, uses, effects);
	}

	public static class Builder {
		private HashSet<Trigger> triggers = new HashSet<>();
		private HashSet<Input<?>> uses = new HashSet<>();
		private HashSet<Effect> effects = new HashSet<>();
		private Function<Reaction, Void> targetCode = (reaction) -> null;
		private Deadline deadline = new Deadline(Timestamp.ZERO, (reaction) -> null);

		public Reaction build() {
			return new Reaction(triggers, uses, effects, targetCode, deadline);
		}

		public Builder deadline(@NotNull Deadline deadline) {
			this.deadline = deadline;

			return this;
		}

		public Builder triggers(Trigger... triggers) {
			this.triggers.addAll(Arrays.asList(triggers));

			return this;
		}

		public Builder uses(Input<?>... uses) {
			this.uses.addAll(Arrays.asList(uses));

			return this;
		}

		public Builder effects(Effect... effects) {
			this.effects.addAll(Arrays.asList(effects));

			return this;
		}

		public Builder targetCode(@NotNull Function<Reaction, Void> targetCode) {
			this.targetCode = targetCode;

			return this;
		}
	}
}
