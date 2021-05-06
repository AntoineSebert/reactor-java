package reactor;

import org.jetbrains.annotations.NotNull;

/**
 * reactor.State specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#state-declaration
 */
public class State<T> extends Declaration {
	private final T value;

	/**
	 * @param name  name
	 * @param value value
	 * @throws ExceptionInInitializerError if the name is empty or value is of type reactor.Parameter
	 */
	public State(@NotNull String name, @NotNull T value) {
		super(name);

		this.value = value;
	}
	public State(@NotNull String name, @NotNull Parameter<? extends T> param) {
		this(name, param.value());
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
}
