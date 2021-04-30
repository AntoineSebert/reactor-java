package reactor;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

/**
 * Reaction specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reaction-declaration
 */
public record Reaction(@NotNull HashSet<Trigger> triggers, @NotNull HashSet<Input<?>> uses,
                       @NotNull HashSet<Effect> effects, @NotNull Function<Reaction, Void> targetCode) {
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

	/**
	 * @return the target code
	 */
	public Function<Reaction, Void> getTargetCode() {
		return targetCode;
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
}
