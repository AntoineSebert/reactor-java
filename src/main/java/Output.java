import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Output specification class.
 */
public class Output<T> implements Port<T> {
	String name;
	int width;

	/**
	 * @param name name
	 * @param width width of the port
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	Output(@NotNull String name, @NotNull Optional<Integer> width) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		if (width.isPresent() && width.get() < 1)
			throw new ExceptionInInitializerError("Output width cannot be less than 1");

		this.name = name;
		this.width = width.orElse(1);
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
		return name.equals(((Output<?>) o).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
