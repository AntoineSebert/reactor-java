package reactor.input;

import org.jetbrains.annotations.NotNull;
import reactor.Declaration;
import reactor.port.Port;

import java.util.Optional;

/**
 * Input specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#input-declaration
 */
public abstract class Input<T> extends Declaration implements Port<T> {
	protected final boolean mutable;

	/**
	 * @param name    name
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	Input(@NotNull String name, @NotNull Optional<Boolean> mutable) {
		super(name);

		this.mutable = mutable.orElse(Boolean.FALSE);
	}

	/**
	 * @return the mutability
	 */
	public boolean isMutable() {
		return mutable;
	}
}
