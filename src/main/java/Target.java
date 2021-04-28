import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

/**
 * Target specification class.
 */
public class Target {
	private String name;
	private HashMap<String, Object> params;
	public enum loggingParameter {
		log,
		debug
	}

	/**
	 * @param name name
	 * @param params parameters
	 */
	public Target(@NotNull String name, @NotNull HashMap<String, Object> params) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Target name cannot be empty");

		if (params.containsKey("timeout")) {
			Time timeout = (Time)params.get("timeout");

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
	public HashMap<String, Object> getParams() {
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

			for (HashMap.Entry<String, Object> param : params.entrySet())
				builder.append("\t").append(param.getKey()).append(": ").append(param.getValue()).append(",\n");

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
