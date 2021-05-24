package target;

import org.jetbrains.annotations.NotNull;
import reactor.Reactor;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

		public Type getType() {
			return type;
		}
	}

	public static final Target Java = new Target(
			"Java",
			Duration.ofNanos(1),
			new HashMap<>(1) {{
				put("default", TimeUnit.NANOSECONDS);
				put("threads", 1);
				put("timeout", Duration.parse("PT10S"));
				put("keepalive", false);
			}}
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
			throw new ExceptionInInitializerError("Timestamp precision cannot be zero");

		for (Map.Entry<String, Object> param : params.entrySet())
			if ("timeout".equals(param.getKey()) && param.getValue() == Duration.ZERO)
				throw new ExceptionInInitializerError("Target parameter 'timeout' must be a non-zero timestamp with unit");

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

	public Optional<Object> get(@NotNull String name) {
		return Optional.of(params.getOrDefault(name, Optional.empty()));
	}

	public void setParams(@NotNull Map<String, Object> params) {
		this.params.putAll(params);
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
		String target = "\t".repeat(lvl) +"Target " + name +";\n";
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
