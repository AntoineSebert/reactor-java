package reactor;

/**
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#parameter-declaration
 */
public class Parameter<T> extends Declaration {
	private final T value;

	public Parameter(String name, T value) {
		super(name);

		this.value = value;
	}

	public T value() {
		return value;
	}

	public void toLF(int lvl) {
		String param = "\t".repeat(lvl) +"(" + name + ": " + value.getClass().getSimpleName() +"(" + value + "))";
		System.out.print(param);
	}
}
