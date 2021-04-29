import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Reactor specification class.
 */
public class Reactor {
	private String name;
	private HashSet<Parameter<?>> params;
	private HashSet<State<?>> states;

	/**
	 * @param name name
	 * @param params parameters
	 * @param states states
	 */
	public Reactor(@NotNull String name, @NotNull HashSet<Parameter<?>> params, @NotNull HashSet<State<?>> states) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Reactor name cannot be empty");

		for (Parameter<?> param : params)
			if (param.value() instanceof Time time)
				if (time.time() == 0 && time.unit().isEmpty())
					throw new ExceptionInInitializerError("Non-zero time parameter for reactor had no time unit");

		this.name = name;
		this.params = params;
		this.states = states;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		else
			return name.equals(((Reactor)obj).name);
	}
}
