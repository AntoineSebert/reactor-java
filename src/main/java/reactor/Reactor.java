package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.input.Input;
import reactor.output.Output;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

/**
 * reactor.Reactor specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reactor-block
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#contained-reactors
 */
public class Reactor extends Declaration {
	private String preamble;
	private HashSet<Parameter<?>> params;
	private HashSet<State<?>> states;
	private HashSet<Input<?>> inputs;
	private HashSet<Output<?>> outputs;
	private HashSet<Timer> timers;
	private HashSet<Action<?>> actions;
	private HashSet<Reaction> reactions;
	private HashSet<Reactor> containedReactors;

	public Reactor(@NotNull String name, @NotNull String preamble, @NotNull HashSet<Reaction> reactions,
	               @NotNull Iterable<? extends Declaration> declarations) {
		super(name);

		for (Parameter<?> param : params)
			if (param.value() instanceof Time time)
				if (time.time() == 0 && time.unit().isEmpty())
					throw new ExceptionInInitializerError("Non-zero time parameter for reactor had no time unit");

		for (Reaction reaction : reactions) {
			reaction.self(this);

			if (!inputs.containsAll(reaction.getUses()))
				throw new ExceptionInInitializerError(
						"At least one unknown Input parameter(s) in Reaction Use list " + reaction.getUses());
		}

		this.preamble = preamble;
		this.reactions = reactions;

		for (Declaration decl : declarations)
			if (decl instanceof Parameter)
				params.add((Parameter<?>) decl);
			else if(decl instanceof State)
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
	 * @return a string representation in Lingua Franca
	 */
	public String toLF() {
		StringBuilder builder = new StringBuilder();
		builder.append("reactor ").append("");

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

	public static class Builder {
		private String name;
		private String preamble = "";
		private HashSet<Reaction> reactions = new HashSet<>();
		private HashSet<Declaration> declarations = new HashSet<>();

		public Builder(@NotNull String name) {
			this.name = name;
		}


		public Reactor build() {
			return new Reactor(name, preamble, reactions, declarations);
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
	}
}
