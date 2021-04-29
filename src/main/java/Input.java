import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Input specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#input-declaration
 */
public class Input<T> implements Port<T> {
	String name;
	int width;
	boolean mutable;

	/**
	 * @param name name
	 * @param width width of the port
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	Input(@NotNull String name, @NotNull Optional<Integer> width, @NotNull Optional<Boolean> mutable) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		if (width.isPresent() && width.get() < 1)
			throw new ExceptionInInitializerError("Input width cannot be less than 1");

		this.name = name;
		this.mutable = mutable.orElse(Boolean.FALSE);
		this.width = width.orElse(1);
	}

	/**
	 * @return the mutability
	 */
	public boolean isMutable() {
		return mutable;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public boolean isMultiport() {
		return 1 < width;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Input<?>) o).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
