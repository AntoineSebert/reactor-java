package reactor;

/**
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#parameter-declaration
 */
public record Parameter<T>(String name, T value) {
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
}
