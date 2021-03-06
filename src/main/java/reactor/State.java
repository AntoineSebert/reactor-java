package reactor;

import org.jetbrains.annotations.NotNull;

/**
 * reactor.State specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#state-declaration
 */
public class State<T> extends Declaration {
	private T value;

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

	@Override
	public void toLF(int lvl) {

		String state = "\t".repeat(lvl) + getClass().getSimpleName() + " " + name
				+ ": " + value.getClass().getSimpleName() + "(" + value + ");";

		System.out.println(state);
	}

	/**
	 * @return the value
	 */
	public T get() {
		return value;
	}

	public void set(T val) {
		value = val;
	}
}
