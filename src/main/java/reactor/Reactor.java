package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Output;
import scheduler.Scheduler;
import time.Timestamp;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Reactor extends Declaration implements Runnable {
	protected String preamble;
	protected HashSet<Parameter<?>> params;
	protected final HashSet<State<?>> states = new HashSet<>();
	protected final HashSet<Input<?>> inputs = new HashSet<>();
	protected final HashSet<Output<?>> outputs = new HashSet<>();
	protected final HashSet<Timer> timers = new HashSet<>();
	protected final HashSet<Action<?>> actions = new HashSet<>();
	protected ArrayList<Reaction> reactions = new ArrayList<>();
	protected final HashSet<Reactor> containedReactors = new HashSet<>();

	public Reactor(@NotNull String name, @NotNull String preamble, @NotNull ArrayList<? extends Reaction> reactions,
	               @NotNull HashSet<Parameter<?>> params, @NotNull Iterable<? extends Declaration> declarations) {
		super(name);

		for (Parameter<?> param : params)
			if (param.value() instanceof Timestamp timestamp)
				if (timestamp.time() == 0 && timestamp.unit().isEmpty())
					throw new ExceptionInInitializerError("Non-zero timestamp parameter for reactor had no timestamp unit");

		this.params = params;

		this.preamble = preamble;

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

		int limit = reactions.size();
		for (int i = 0; i < limit; i++) {
			if (!inputs.containsAll(reactions.get(i).getUses()))
				throw new ExceptionInInitializerError(
						"At least one unknown Input parameter(s) in Reaction Use list " + reactions.get(i).getUses());

			reactions.get(i).self(this);

			for (Trigger t : reactions.get(i).getTriggers()) {
				TriggerObserver.addReactionMapEntry(t, reactions.get(i));
			}

			this.reactions.add(reactions.get(i));
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
	public ArrayList<Reaction> getReactions() {
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

	protected void init() {
		try {
			if (!preamble.isEmpty())
				Runtime.getRuntime().exec(preamble);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		init();

		for (Reaction reaction : reactions)
			for (Trigger trigger : reaction.getTriggers())
				if (trigger instanceof Trigger.STARTUP)
					Scheduler.addReactionTask(reaction);
	}

	public static class Builder {
		protected final String name;
		protected final HashSet<Parameter<?>> params = new HashSet<>();
		protected String preamble = "";
		protected HashSet<Declaration> declarations = new HashSet<>();
		protected ArrayList<Reaction> reactions = new ArrayList<>();

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

		public Builder reactions(Reaction... reactions) {
			this.reactions = new ArrayList<>(Arrays.stream(reactions).toList());

			return this;
		}

		public Reactor build() {
			return new Reactor(name, preamble, reactions, params, declarations);
		}
	}
}
