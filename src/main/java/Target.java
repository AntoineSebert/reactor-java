import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Target specification class.
 */
public class Target {
	private String name;
	private HashMap<String, String> params;
	public static final HashMap<String, HashSet<Class<?>>> paramSpecification;
	public enum loggingParameter {
		log,
		debug
	}

	static {
		paramSpecification = new HashMap<>(7) {{
			put("compiler", new HashSet<>(1) {{
				add(String.class);
			}});
			put("fast", new HashSet<>(1) {{
				add(boolean.class);
			}});
			put("flags", new HashSet<>(2) {{
				add(String.class);
				add(String[].class);
			}});
			put("keepalive", new HashSet<>(1) {{
				add(boolean.class);
			}});
			put("logging", new HashSet<>(1) {{
				add(loggingParameter.class);
			}});
			put("no-compile", new HashSet<>(1) {{
				add(boolean.class);
			}});
			put("timeout", new HashSet<>(1) {{
				add(Time.class);
			}});
		}};
	}

	/**
	 * @param name name
	 * @param params parameters
	 */
	public Target(@NotNull String name, @NotNull HashMap<String, String> params) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Target name cannot be empty");

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
	public HashMap<String, String> getParams() {
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

			for (HashMap.Entry<String, String> param : params.entrySet())
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
