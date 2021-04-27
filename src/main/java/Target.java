import java.util.HashMap;
import java.util.HashSet;

/**
 * Target specification class.
 */
public class Target {
	private String name;
	private HashMap<String, String> params;
	public static HashMap<String, HashSet<Class<?>>> paramSpecification;

	static {
		paramSpecification = new HashMap<>() {{
			put("compiler", new HashSet<>() {{
				add(String.class);
			}});
			put("fast", new HashSet<>() {{
				add(boolean.class);
			}});
			put("flags", new HashSet<>() {{
				add(String.class);
				add(String[].class);
			}});
			put("keepalive", new HashSet<>() {{
				add(boolean.class);
			}});
			put("logging", new HashSet<>() {{
				add(loggingParameterValue.class);
			}});
			put("no-compile", new HashSet<>() {{
				add(boolean.class);
			}});
			put("timeout", new HashSet<>() {{
				add(timeoutParameterValue.class);
			}});
		}};
	}

	public enum loggingParameterValue {
		log,
		debug
	}

	public record timeoutParameterValue(int time, String unit) {}

	/**
	 * Constructs a new Target instance.
	 *
	 * @param name a target name
	 * @param params target parameters
	 */
	public Target(String name, HashMap<String, String> params) {
		checkParameters(name, params);

		this.name = name;
		this.params = params;
	}

	/**
	 * Check whether or not the constructor parameters are valid.
	 *
	 * @param name a target name
	 * @param params target parameters
	 */
	private static void checkParameters(String name, HashMap<String, String> params) {
		if (null == name)
			throw new ExceptionInInitializerError("Target name cannot be null");

		if (null == params)
			throw new ExceptionInInitializerError("Target parameters cannot be null");
	}

	/**
	 * @return the name of the target
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the parameters of the target
	 */
	public HashMap<String, String> getParams() {
		return params;
	}

	/**
	 * @return a string representation of the target specification
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
	 * @return a string representation of the target specification
	 */
	public String toJava() {
		return "";
	}
}
