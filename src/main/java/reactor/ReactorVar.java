package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.input.Input;
import reactor.output.Output;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class ReactorVar extends Reactor {
	protected String preamble;
	protected HashSet<Parameter<?>> params;
	protected HashSet<State<?>> states = new HashSet<>();
	protected HashSet<Input<?>> inputs = new HashSet<>();
	protected HashSet<Output<?>> outputs = new HashSet<>();
	protected HashSet<Timer> timers = new HashSet<>();
	protected HashSet<Action<?>> actions = new HashSet<>();
	protected HashSet<Reaction> reactions;
	protected HashSet<Reactor> containedReactors = new HashSet<>();

	public ReactorVar(@NotNull String name, @NotNull String preamble, @NotNull HashSet<Reaction> reactions,
	                  @NotNull HashSet<Parameter<?>> params, @NotNull Iterable<? extends Declaration> declarations) {
		super(name);

		for (Parameter<?> param : params)
			if (param.value() instanceof Time time)
				if (time.time() == 0 && time.unit().isEmpty())
					throw new ExceptionInInitializerError("Non-zero time parameter for reactor had no time unit");

		this.params = params;

		this.preamble = preamble;
		this.reactions = reactions;

		for (Declaration decl : declarations)
			if(decl instanceof State)
				states.add((State<?>) decl);
			else if(decl instanceof Input)
				inputs.add((Input<?>) decl);
			else if(decl instanceof Output)
				outputs.add((Output<?>) decl);
			else if(decl instanceof Timer)
				timers.add((Timer) decl);
			else if(decl instanceof Action)
				actions.add((Action<?>) decl);
			else if(decl instanceof Reactor)
				containedReactors.add((Reactor) decl);

		for (Reaction reaction : reactions) {
			if (!inputs.containsAll(reaction.getUses()))
				throw new ExceptionInInitializerError(
						"At least one unknown Input parameter(s) in Reaction Use list " + reaction.getUses());

			reaction.self(this);

			this.reactions.add(reaction);
		}
	}

	/**
	 * @return the preamble
	 */
	public String getPreamble() {
		return preamble;
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
	public HashSet<Input<?>> getInputs() {
		return inputs;
	}

	/**
	 * @return the outputs
	 */
	public HashSet<Output<?>> getOutputs() {
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
	public HashSet<Reactor> getContainedReactors() {
		return containedReactors;
	}

	/**
	 * @return the parameters
	 */
	public HashSet<Parameter<?>> getParams() {
		return params;
	}

	protected void init() throws IOException {
		if (!preamble.isEmpty())
			Runtime.getRuntime().exec(preamble);
	}

	public void run() throws IOException {
		init();

		for (Reaction reaction : reactions)
			reaction.run();
	}

	public static class Builder {
		protected final String name;
		protected HashSet<Parameter<?>> params = new HashSet<>();
		protected String preamble;
		protected HashSet<Declaration> declarations = new HashSet<>();
		protected HashSet<Reaction> reactions;

		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Builder param(Parameter<?> param) {
			params.add(param);

			return this;
		}

		public <T> Builder param(String name, T value) {
			params.add(new Parameter<>(name, value));

			return this;
		}

		public Builder preamble(@NotNull String preamble) {
			this.preamble = preamble;

			return this;
		}

		public Builder declarations(Declaration... declarations) {
			this.declarations = new HashSet<>(Arrays.stream(declarations).toList());

			return this;
		}

		public Builder reactions(@NotNull HashSet<Reaction> reactions) {
			this.reactions = reactions;

			return this;
		}

		public Builder addReaction(@NotNull Reaction reaction) {
			reactions.add(reaction);

			return this;
		}

		public ReactorVar build() {
			return new ReactorVar(name, preamble, reactions, params, declarations);
		}
	}
}
