import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Target specification class.
 */
public class Target {
	private String name;
	private HashSet<Parameter<?>> params;

	/**
	 * @param name name
	 * @param params parameters
	 * @throws ExceptionInInitializerError if the name is empty or if the "timeout" parameter is present and invalid
	 */
	public Target(@NotNull String name, @NotNull HashSet<Parameter<?>> params) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Target name cannot be empty");

		for (Parameter<?> param : params)
			if ("timeout".equals(param.name())) {
				Time timeout = (Time)param.value();

				if (timeout.time() == 0 || timeout.unit().isEmpty())
					throw new ExceptionInInitializerError("Timeout parameter must be a non-zero time with unit");
			}

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
	public HashSet<Parameter<?>> getParams() {
		return params;
	}

	/**
	 * @return a string representation in Lingua Franca
	 */
	public String toLF() {
		StringBuilder builder = new StringBuilder();
		builder.append("target ").append(name);

		if (!params.isEmpty()) {
			builder.append(" {\n");

			for (Parameter<?> param : params)
				builder.append("\t").append(param.name()).append(": ").append(param.value()).append(",\n");

			builder.append("}");
		}

		builder.append(";");

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
			return name.equals(((Target)obj).name);
	}
}
