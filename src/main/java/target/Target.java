package target;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Target specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#target-specification
 */
public class Target {
	private final String name;
	private HashMap<String, Object> params;
	private Duration precision;

	enum Logging {
		debug,
		log,
	}

	enum CodeKeyword {
		schedule,
		request_stop,
	}

	enum Parameters {
		build(String.class),
		files(String[].class),
		compiler(String.class),
		fast(Boolean.class),
		flags(String[].class),
		keepalive(Boolean.class),
		logging(Logging.class),
		no_compile(Boolean.class),
		threads(Integer.class),
		timeout(Duration.class);

		private final Type type;

		Parameters(Type type) {
			this.type = type;
		}

		public Type type() {
			return type;
		}
	}

	static EnumMap<Parameters, Object> _default = new EnumMap<>(Parameters.class) {{
		put(Parameters.build, "");
		put(Parameters.files, new String[]{});
		put(Parameters.compiler, "javac");
		put(Parameters.fast, false);
		put(Parameters.flags, new String[]{});
		put(Parameters.keepalive, false);
		put(Parameters.no_compile, false);
		put(Parameters.threads, 1);
		put(Parameters.logging, Logging.log);
		put(Parameters.timeout, Duration.parse("PT10S"));
	}};

	public static final Target Java = new Target(
			"Java",
			Duration.ofNanos(1),
			new HashMap<>()
	);

	/**
	 * @param name      name
	 * @param precision timestamp precision, used in target.Target
	 * @param params    parameters
	 * @throws ExceptionInInitializerError if the name is empty or if the "timeout" parameter is present and invalid
	 */
	public Target(@NotNull String name, Duration precision, @NotNull HashMap<String, Object> params) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		if (precision == Duration.ZERO)
			throw new ExceptionInInitializerError("Target time precision cannot be zero");

		for (Map.Entry<String, Object> param : params.entrySet())
			if ("timeout".equals(param.getKey()) && param.getValue() == Duration.ZERO)
				throw new ExceptionInInitializerError("Target parameter 'timeout' must be a non-zero timestamp with unit");
			else
				try {
					var expected = Parameters.valueOf(param.getKey()).type();

					if(param.getValue().getClass() != expected)
						throw new ExceptionInInitializerError(
								"Type of parameter '" + param.getKey() + "' does not match '" + expected + "'");
				} catch(IllegalArgumentException ignored) {}

		this.name = name;
		this.params = params;
		this.precision = precision;
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
	 * @return the timestamp precision
	 */
	public Duration getPrecision() {
		return precision;
	}

	public <T> T get(@NotNull String name) {
		if(params.containsKey(name))
			return (T) params.get(name);
		else {
			for (Parameters p : Parameters.values())
				if (p.name().equals(name))
					return (T) _default.get(Parameters.valueOf(name));

			return null;
		}
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Target) o).name);
	}

	public void toLF(int lvl) {

		StringBuilder target = new StringBuilder("\t".repeat(lvl) +"Target " + name);

		if(params.isEmpty()) {
			target.append(";\n");
		} else {
			target.append(" { \n");
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				target.append("\t").append(entry.getKey()).append(" : ").append(entry.getValue()).append(",\n");
			}
			target.append("};\n");
		}

		System.out.println(target);
	}

	public static class Builder {
		private final String name;
		private final HashMap<String, Object> params = new HashMap<>(); // map<field, val>
		private Duration precision = Duration.ZERO;

		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Target build() {
			return new Target(name, precision, params);
		}

		public <T> Builder param(@NotNull String param, @NotNull T value) {
			params.put(param, value);

			return this;
		}

		public Builder precision(@NotNull Duration precision) {
			this.precision = precision;

			return this;
		}
	}
}
