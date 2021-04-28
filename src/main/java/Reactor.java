import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Reactor {
	private String name;
	private HashMap<String, Object> params;

	/**
	 * @param name name
	 * @param params parameters
	 */
	public Reactor(@NotNull String name, @NotNull HashMap<String, Object> params) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Reactor name cannot be empty");

		for (Object value : params.values())
			if (value instanceof Time time)
				if (time.time() == 0 && time.unit().isEmpty())
					throw new ExceptionInInitializerError("Non-zero time parameter for reactor had no time unit");

		this.name = name;
		this.params = params;
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
	public HashMap<String, Object> getParams() {
		return params;
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
