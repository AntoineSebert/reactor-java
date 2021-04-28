import org.jetbrains.annotations.NotNull;

/**
 * State specification class.
 */
public class State<T> {
	String name;
	T value;

	/**
	 * @param name name
	 * @param value value
	 * @throws ExceptionInInitializerError if the name is empty or value is of type Parameter
	 */
	State(@NotNull String name, @NotNull T value) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Target name cannot be empty");

		this.name = name;
		this.value = value;

		// disallow "state name(parameter);" in favor of "state<parameter.value().getClass()> name(parameter.value());
		if (value instanceof Parameter<?>)
			throw new ExceptionInInitializerError("State cannot receive Parameter as parameter");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		else
			return name.equals(((State<?>)obj).name);
	}
}
