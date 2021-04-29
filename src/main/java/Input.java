public class Input<T> implements IO<T> {
	String name;
	int width;
	boolean mutable;

	/**
	 * @param name name
	 */
	Input(String name) {
		this(name, 1, false);
	}

	/**
	 * @param name name
	 */
	Input(String name, boolean mutable) {
		this(name, 1, mutable);
	}

	/**
	 * @param name name
	 * @param width width
	 */
	Input(String name, int width) {
		this(name, width, false);
	}

	/**
	 * @param name name
	 * @param width width of the port
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	Input(String name, int width, boolean mutable) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Input name cannot be empty");

		if (width < 1)
			throw new ExceptionInInitializerError("Input width cannot be less than 1");

		this.name = name;
		this.mutable = mutable;
		this.width = width;
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
