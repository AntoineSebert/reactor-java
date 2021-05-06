package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.input.Input;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

/**
 * Reaction specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reaction-declaration
 */
public class Reaction {
	private Reactor self;
	private HashSet<Trigger> triggers;
	private HashSet<Input<?>> uses;
	private HashSet<Effect> effects;
	private Function<Reaction, Void> targetCode;

	public Reaction(@NotNull HashSet<Trigger> triggers, @NotNull HashSet<Input<?>> uses,
	                @NotNull HashSet<Effect> effects, @NotNull Function<Reaction, Void> targetCode) {}

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

		public Reaction build() {
			return new Reaction(triggers, uses, effects, targetCode);
		}

		public Builder triggers(@NotNull HashSet<Trigger> triggers) {
			this.triggers = triggers;

			return this;
		}

		public Builder addTrigger(@NotNull Trigger trigger) {
			triggers.add(trigger);

			return this;
		}

		public Builder uses(@NotNull HashSet<Input<?>> uses) {
			this.uses = uses;

			return this;
		}

		public Builder effects(@NotNull HashSet<Effect> effects) {
			this.effects = effects;

			return this;
		}

		public Builder targetCode(@NotNull Function<Reaction, Void> targetCode) {
			this.targetCode = targetCode;

			return this;
		}
	}
}
