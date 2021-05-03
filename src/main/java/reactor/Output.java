package reactor;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * reactor.Output specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#output-declaration
 */
public record Output<T>(String name) implements Port<T> {
	/**
	 * @param name name
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public Output(@NotNull String name) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Output<?>) o).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	enum Var {
		Output(Output.class),
		OutputArr(Output[].class);

		private Type type;

		Var(Type type) {
			this.type = type;
		}

		public Type getType() {
			return type;
		}
	}
}
