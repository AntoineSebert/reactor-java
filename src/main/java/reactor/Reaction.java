package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Port;
import scheduler.Scheduler;

import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;

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
	private final BiConsumer<Reactor, Reaction> targetCode;
	private final Optional<Deadline> deadline;
	private long timestamp;
	
	public Reaction(@NotNull HashSet<String> trigger_names, @NotNull HashSet<String> use_names,
	                @NotNull HashSet<String> effect_names, @NotNull BiConsumer<Reactor, Reaction> targetCode,
	                @NotNull Optional<Deadline> deadline) {
		this.trigger_names = trigger_names;
		this.use_names = use_names;
		this.effect_names = effect_names;
		this.targetCode = targetCode;
		this.deadline = deadline;
		timestamp = -1;
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

	public boolean can_trigger() {
		return timestamp < Time.logical();
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

			if (self.lookup(name) instanceof Trigger t)
				triggers.put(name, t);
			else
				throw new ExceptionInInitializerError("Name '" + name + "' does not identify a Trigger");
		}

		for(String name : use_names)
			if (self.lookup(name) instanceof Input<?> i)
				uses.put(name, i);
			else
				throw new ExceptionInInitializerError("Name '" + name + "' does not identify an Use");

		for (String name : effect_names)
			if (self.lookup(name) instanceof Port<?> port)
				effects.put(name, port);
			else
				throw new ExceptionInInitializerError("Name '" + name + "' does not identify an Effect");
	}

	public Reactor self() {
		return self;
	}

	/**
	 * Runs the target code.
	 */
	@Override
	public void run() {
		if (has_passed())
			deadline.get().handler().accept(self, this);
		else
			targetCode.accept(self, this);

		Time.next();
	}

	public Optional<Deadline> deadline() {
		return deadline;
	}

	public boolean has_passed() {
		if(deadline.isPresent() && deadline.get().deadline() != Duration.ZERO)
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

	public void toLF(int level) {
		String reaction_string = "reaction (";

		for (Trigger trigger : getTriggers().values()) {
			if (trigger.isStartup()) {
				reaction_string += "startup, ";
			} else if (trigger.isShutdown()) {
				reaction_string += "shutdown, ";
			} else if ( trigger instanceof  Declaration declaration) {
				reaction_string += declaration.name() + ", ";

			}
		}
		reaction_string = reaction_string.substring(0, reaction_string.length()-2) + ")";

		String uses_string = "";
		for (Input<?> use : uses.values()) {
			uses_string +=  " " + use.name();
		}
		if (!uses.isEmpty()) uses_string = " ";

		String effects_string = "";
		if (!effects.isEmpty()) effects_string = "-> ";
		for (Port<?> effect : effects.values()) {
			effects_string += effect.name() + " ";
		}

		String param = "\t".repeat(level) + reaction_string + uses_string + effects_string + " {";
		System.out.println(param);
		System.out.println("\t".repeat(level+1) + targetCode.toString());
		System.out.println("\t".repeat(level) + "}");


	}


	public static class Builder {
		private HashSet<String> trigger_names = new HashSet<>();
		private HashSet<String> use_names = new HashSet<>();
		private HashSet<String> effect_names = new HashSet<>();
		private BiConsumer<Reactor, Reaction> targetCode = (reactor, reaction) -> {};
		private Optional<Deadline> deadline = Optional.empty();

		public Reaction build() {
			return new Reaction(trigger_names, use_names, effect_names, targetCode, deadline);
		}

		public Builder deadline(@NotNull Deadline deadline) {
			this.deadline = Optional.of(deadline);

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

		public Builder targetCode(@NotNull BiConsumer<Reactor, Reaction> targetCode) {
			this.targetCode = targetCode;

			return this;
		}
	}
}
