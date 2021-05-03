package reactor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;

/**
 * reactor.Reactor specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reactor-block
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#contained-reactors
 */
public class Reactor {
	private final String name;
	private String preamble;
	private HashSet<Parameter<?>> params;
	private HashSet<State<?>> states;
	private HashSet<Input.Var> inputs;
	private HashSet<Output.Var> outputs;
	private HashSet<Timer> timers;
	private HashSet<Action<?>> actions;
	private HashSet<Reaction> reactions;
	private HashSet<Statement> containedReactors;

	/**
	 * @param name              name
	 * @param params            parameters
	 * @param states            states
	 * @param timers            timers
	 * @param actions           actions
	 * @param reactions         reactions
	 * @param containedReactors contained reactors
	 */
	public Reactor(@NotNull String name,
	               @NotNull HashSet<Parameter<?>> params,
	               @NotNull HashSet<State<?>> states,
	               @NotNull HashSet<Input.Var> inputs,
	               @NotNull HashSet<Output.Var> outputs,
	               @NotNull HashSet<Timer> timers,
	               @NotNull HashSet<Action<?>> actions,
	               @NotNull HashSet<Reaction> reactions,
	               @NotNull HashSet<Statement> containedReactors,
	               @NotNull String preamble) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		for (Parameter<?> param : params)
			if (param.value() instanceof Time time)
				if (time.time() == 0 && time.unit().isEmpty())
					throw new ExceptionInInitializerError("Non-zero time parameter for reactor had no time unit");

		for (Reaction reaction : reactions)
			if (!inputs.containsAll(reaction.getUses()))
				throw new ExceptionInInitializerError(
						"At least one unknown Input parameter(s) in Reaction Use list " + reaction.getUses());

		this.name = name;
		this.preamble = preamble;
		this.params = params;
		this.states = states;
		this.inputs = inputs;
		this.outputs = outputs;
		this.timers = timers;
		this.actions = actions;
		this.reactions = reactions;
		this.containedReactors = containedReactors;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the preamble
	 */
	public String getPreamble() {
		return preamble;
	}

	/**
	 * @return the parameters
	 */
	public HashSet<Parameter<?>> getParams() {
		return params;
	}

	/**
	 * @return the states
	 */
	public HashSet<State<?>> getStates() {
		return states;
	}

	/**
	 * @return the inputs
	 */
	public HashSet<Input.Var> getInputs() {
		return inputs;
	}

	/**
	 * @return the outputs
	 */
	public HashSet<Output.Var> getOutputs() {
		return outputs;
	}

	/**
	 * @return the timers
	 */
	public HashSet<Timer> getTimers() {
		return timers;
	}

	/**
	 * @return the actions
	 */
	public HashSet<Action<?>> getActions() {
		return actions;
	}

	/**
	 * @return the reactions
	 */
	public HashSet<Reaction> getReactions() {
		return reactions;
	}

	/**
	 * @return the contained reactors
	 */
	public HashSet<Statement> getContainedReactors() {
		return containedReactors;
	}

	/**
	 * @return a string representation in Lingua Franca
	 */
	public String toLF() {
		StringBuilder builder = new StringBuilder();
		builder.append("reactor ").append(name);

		// TODO

		return builder.toString();
	}

	private void init() throws IOException {
		if (!preamble.isEmpty())
			Runtime.getRuntime().exec(preamble);
	}

	public void run() throws IOException {
		init();

		for (Reaction reaction : reactions)
			reaction.run();
	}

	/**
	 * @return a string representation in Java
	 */
	public String toJava() {
		return "";
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Reactor) o).name);
	}

	enum Var implements Statement {
		Reactor(Reactor.class),
		Bank(Reactor[].class);

		private Type type;

		//Constructor to initialize the instance variable
		Var(Type type) {
			this.type = type;
		}

		public Type getType() {
			return type;
		}
	}

	public static class Builder {
		private String name;
		private HashSet<Parameter<?>> params = new HashSet<>();
		private HashSet<State<?>> states = new HashSet<>();
		private HashSet<Input.Var> inputs = new HashSet<>();
		private HashSet<Output.Var> outputs = new HashSet<>();
		private HashSet<Timer> timers = new HashSet<>();
		private HashSet<Action<?>> actions = new HashSet<>();
		private HashSet<Reaction> reactions = new HashSet<>();
		private HashSet<Statement> containedReactors = new HashSet<>();
		private String preamble = "";

		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Reactor build() {
			return new Reactor(name, params, states, inputs, outputs, timers, actions, reactions, containedReactors, preamble);
		}

		public Builder params(@NotNull HashSet<Parameter<?>> params) {
			this.params = params;

			return this;
		}

		public Builder states(@NotNull HashSet<State<?>> states) {
			this.states = states;

			return this;
		}

		public Builder inputs(@NotNull HashSet<Input.Var> inputs) {
			this.inputs = inputs;

			return this;
		}

		public Builder outputs(@NotNull HashSet<Output.Var> outputs) {
			this.outputs = outputs;

			return this;
		}

		public Builder timers(@NotNull HashSet<Timer> timers) {
			this.timers = timers;

			return this;
		}

		public Builder actions(@NotNull HashSet<Action<?>> actions) {
			this.actions = actions;

			return this;
		}

		public Builder reactions(@NotNull HashSet<Reaction> reactions) {
			this.reactions = reactions;

			return this;
		}

		public Builder containedReactors(@NotNull HashSet<Statement> containedReactors) {
			this.containedReactors = containedReactors;

			return this;
		}

		public Builder preamble(@NotNull String preamble) {
			this.preamble = preamble;

			return this;
		}
	}
}
