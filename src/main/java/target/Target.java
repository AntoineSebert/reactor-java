package target;

import org.jetbrains.annotations.NotNull;
import reactor.Parameter;
import reactor.Time;
import reactor.Unit;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;

/**
 * Target specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#target-specification
 */
public class Target {
	private final String name;
	private HashSet<Parameter<?>> params;
	private Time precision;

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
		timeout(Time.class);

		private final Type type;

		Parameters(Type type) {
			this.type = type;
		}

		public Type getType() {
			return type;
		}
	}

	public static final Target Java = new Target("Java", new Time(1, Optional.of(Unit.nsec)), new HashSet<>(0));

	/**
	 * @param name      name
	 * @param params    parameters
	 * @param precision time precision, used in target.Target
	 * @throws ExceptionInInitializerError if the name is empty or if the "timeout" parameter is present and invalid
	 */
	public Target(@NotNull String name, @NotNull Time precision, @NotNull HashSet<Parameter<?>> params) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		if (precision.unit().isEmpty())
			throw new ExceptionInInitializerError("Time precision must have a unit");

		for (Parameter<?> param : params)
			if ("timeout".equals(param.name())) {
				Time timeout = (Time) param.value();

				if (timeout.time() == 0 || timeout.unit().isEmpty())
					throw new ExceptionInInitializerError("Target parameter 'timeout' must be a non-zero time with unit");
			}

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
	public HashSet<Parameter<?>> getParams() {
		return params;
	}

	/**
	 * @return the time precision
	 */
	public Time getPrecision() {
		return precision;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Target) o).name);
	}

	public static class Builder {
		private final String name;
		private final HashSet<Parameter<?>> params = new HashSet<>(); // map<field, val>
		private Time precision = Time.ZERO;

		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Target build() {
			return new Target(name, precision, params);
		}

		public <T> Builder param(@NotNull String param, @NotNull T value) {
			params.add(new Parameter<>(param, value));

			return this;
		}

		public Builder precision(@NotNull Time precision) {
			this.precision = precision;

			return this;
		}
	}
}
