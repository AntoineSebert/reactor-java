package reactor;

import org.jetbrains.annotations.NotNull;

/**
 * reactor.State specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#state-declaration
 */
public class State<T> {
	String name;
	T value;

	/**
	 * @param name name
	 * @param value value
	 * @throws ExceptionInInitializerError if the name is empty or value is of type reactor.Parameter
	 */
	State(@NotNull String name, @NotNull T value) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		// disallow "state name(parameter);" in favor of "state<parameter.value().getClass()> name(parameter.value());
		if (value instanceof Parameter<?>)
			throw new ExceptionInInitializerError("reactor.State cannot receive reactor.Parameter as parameter");

		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((State<?>) o).name);
	}
}
