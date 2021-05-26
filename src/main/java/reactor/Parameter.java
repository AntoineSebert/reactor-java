package reactor;

/**
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#parameter-declaration
 */
public class Parameter<T> {
	private String name;
	private final T value;

	public Parameter(String name, T value) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		this.name = name;
		this.value = value;
	}

	public T value() {
		return value;
	}

	public void toLF(int lvl) {
	 * @return the name
	 */
	public String name() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Parameter<?>) o).name);
	}

	public void ToLF(int lvl) {
		String param = "\t".repeat(lvl) +"(" + name + ": " + value.getClass().getSimpleName() +"(" + value + "))";
		System.out.print(param);
	}
}
