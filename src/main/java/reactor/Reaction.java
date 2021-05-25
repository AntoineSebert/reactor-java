package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Port;

import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Reaction specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reaction-declaration
 */
public class Reaction implements Runnable {
	private Reactor self;
	private HashMap<String, Trigger> triggers = new HashMap<>();
	private HashSet<String> trigger_names;
	private HashMap<String, Input<?>> uses = new HashMap<>();
	private HashSet<String> use_names;
	private HashMap<String, Port<?>> effects = new HashMap<>();
	private HashSet<String> effect_names;
	private final BiFunction<Reactor, Reaction, Void> targetCode;
	private final Optional<Deadline> deadline;
	private long timestamp;
	
	public Reaction(@NotNull HashSet<String> trigger_names, @NotNull HashSet<String> use_names,
	                @NotNull HashSet<String> effect_names, @NotNull BiFunction<Reactor, Reaction, Void> targetCode,
	                @NotNull Optional<Deadline> deadline) {
		this.trigger_names = trigger_names;
		this.use_names = use_names;
		this.effect_names = effect_names;
		this.targetCode = targetCode;
		this.deadline = deadline;
	}

	/**
	 * @return the triggers
	 */
	public HashMap<String, Trigger> getTriggers() {
		return triggers;
	}

	public Trigger t(@NotNull String name) {
		return triggers.get(name);
	}

	/**
	 * @return the uses
	 */
	public HashMap<String, Input<?>> getUses() {
		return uses;
	}

	public <T> Port<T> e(@NotNull String name) {
		return (Port<T>) effects.get(name);
	}

	public long timestamp() {
		return timestamp;
	}

	public void timestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void self(@NotNull Reactor self) {
		this.self = self;
	}

	public void init() {
		for(String name : trigger_names) {
			if("STARTUP".equals(name)) {
				triggers.put(name, new Trigger.STARTUP());
				continue;
			} else if ("SHUTDOWN".equals(name)) {
				triggers.put(name, new Trigger.SHUTDOWN());
				continue;
			}

			var result = self.get(name);

			if (result.isPresent()) {
				if (result.get() instanceof Trigger t)
					triggers.put(name, t);
				else
					throw new ExceptionInInitializerError("Name '" + name + "' does not identify a Trigger");
			}
			else
				throw new ExceptionInInitializerError("Cannot find name '" + name + "' for Trigger");
		}

		for(String name : use_names) {
				var result = self.get(name);

				if (result.isPresent()) {
					if (result.get() instanceof Input<?> i)
						uses.put(name, i);
					else
						throw new ExceptionInInitializerError("Name '" + name + "' does not identify an Use");
				}
				else
					throw new ExceptionInInitializerError("Cannot find name '" + name + "' for Use");
			}

		for (String name : effect_names) {
				var result = self.get(name);

				if (result.isPresent()) {
					if (result.get() instanceof Port<?> port)
						effects.put(name, port);
					else
						throw new ExceptionInInitializerError("Name '" + name + "' does not identify an Effect");
				}
				else
					throw new ExceptionInInitializerError("Cannot find name '" + name + "' for Effect");
			}
	}

	public Reactor self() {
		return self;
	}

	/**
	 * Runs the target code.
	 */
	@Override
	public void run() {
		if (!has_passed())
			targetCode.apply(self, this);
		else
			deadline.get().handler().apply(self, this);
	}

	public Optional<Deadline> deadline() {
		return deadline;
	}

	public boolean has_passed() {
		if(deadline.isPresent())
			if(deadline.get().deadline() != Duration.ZERO)
				return timestamp + deadline.get().deadline().toNanos() < Time.physical();

		return false;
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
		private HashSet<String> trigger_names = new HashSet<>();
		private HashSet<String> use_names = new HashSet<>();
		private HashSet<String> effect_names = new HashSet<>();
		private BiFunction<Reactor, Reaction, Void> targetCode = (reactor, reaction) -> null;
		private Deadline deadline = new Deadline(Duration.ZERO, (self, r) -> null);

		public Reaction build() {
			return new Reaction(trigger_names, use_names, effect_names, targetCode, Optional.of(deadline));
		}

		public Builder deadline(@NotNull Deadline deadline) {
			this.deadline = deadline;

			return this;
		}

		public Builder triggers(String... trigger_names) {
			this.trigger_names.addAll(Arrays.asList(trigger_names));

			return this;
		}

		public Builder uses(String... use_names) {
			this.use_names.addAll(Arrays.asList(use_names));

			return this;
		}

		public Builder effects(String... effect_names) {
			this.effect_names.addAll(Arrays.asList(effect_names));

			return this;
		}

		public Builder targetCode(@NotNull BiFunction<Reactor, Reaction, Void> targetCode) {
			this.targetCode = targetCode;

			return this;
		}
	}
}
