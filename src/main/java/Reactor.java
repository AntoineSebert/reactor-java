import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Reactor specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reactor-block
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#contained-reactors
 */
public class Reactor {
	private String name;
	private HashSet<Parameter<?>> params;
	private HashSet<State<?>> states;
	private HashSet<Input<?>> inputs;
	private HashSet<Output<?>> outputs;
	private HashSet<Timer> timers;
	private HashSet<Action<?>> actions;

	/**
	 * @param name name
	 * @param params parameters
	 * @param states states
	 * @param timers timers
	 * @param actions actions
	 */
	public Reactor(@NotNull String name,
	               @NotNull HashSet<Parameter<?>> params,
	               @NotNull HashSet<State<?>> states,
	               @NotNull HashSet<Input<?>> inputs,
	               @NotNull HashSet<Output<?>> outputs,
	               @NotNull HashSet<Timer> timers,
	               @NotNull HashSet<Action<?>> actions) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		for (Parameter<?> param : params)
			if (param.value() instanceof Time time)
				if (time.time() == 0 && time.unit().isEmpty())
					throw new ExceptionInInitializerError("Non-zero time parameter for reactor had no time unit");

		this.name = name;
		this.params = params;
		this.states = states;
		this.inputs = inputs;
		this.outputs = outputs;
		this.timers = timers;
		this.actions = actions;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * @return a string representation in Lingua Franca
	 */
	public String toLF() {
		StringBuilder builder = new StringBuilder();
		builder.append("reactor ").append(name);

		// TODO

		return builder.toString();
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
}
