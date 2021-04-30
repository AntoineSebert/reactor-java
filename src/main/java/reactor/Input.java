package reactor;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * reactor.Input specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#input-declaration
 * TODO : make interface, then implements in InputVar & InputArr
 */
public class Input<T> implements Port<T> {
	String name;
	boolean mutable;
	enum Var {
		Input(Input.class),
		InputArr(Input[].class);

		private Type type;

		Var(Type type) {
			this.type = type;
		}

		public Type getType() {
			return type;
		}
	}

	/**
	 * @param name name
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	Input(@NotNull String name, @NotNull Optional<Boolean> mutable) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError(getClass().getTypeName() + " name cannot be empty");

		this.name = name;
		this.mutable = mutable.orElse(Boolean.FALSE);
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
