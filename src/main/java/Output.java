/**
 * Output specification class.
 */
public class Output<T> implements Port<T> {
	String name;
	int width;

	/**
	 * @param name name
	 */
	Output(String name) {
		this(name, 1);
	}

	/**
	 * @param name name
	 * @param width width of the port
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	Output(String name, int width) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Input name cannot be empty");

		if (width < 1)
			throw new ExceptionInInitializerError("Input width cannot be less than 1");

		this.name = name;
		this.width = width;
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
