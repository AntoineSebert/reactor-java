package reactor;

public abstract class Declaration {
	protected String name;

	protected Declaration(String name) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		this.name = name;
	}

	/**
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
		return name.equals(((Declaration) o).name);
	}

	public abstract void ToLF(int lvl);
}
