package reactor.port;

import org.jetbrains.annotations.NotNull;
import reactor.Connection;
import reactor.Declaration;
import reactor.Time;

import java.util.Optional;

/**
 * Input specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#input-declaration
 */
public class Input<T> extends Declaration implements Port<T> {
	protected final boolean mutable;
	protected long time;
	private Optional<T> value = Optional.empty();
	private Connection<T> connection;

	/**
	 * @param name    name
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public Input(@NotNull String name, @NotNull Optional<Boolean> mutable) {
		super(name);

		this.mutable = mutable.orElse(Boolean.FALSE);
	}

	/**
	 * @return the mutability
	 */
	public boolean isMutable() {
		return mutable;
	}

	@Override
	public long timestamp() {
		return time;
	}

	@Override
	public boolean isPresent() {
		return value.isPresent();
	}

	@Override
	public void set(@NotNull T value) {
		if (mutable) {
			this.value = Optional.of(value);
			time = Time.logical(); // not sure if physical or logical
		} else
			throw new RuntimeException("Cannot modify an immutable type");
	}

	public void set(@NotNull T value, long msg_time) {
		this.value = Optional.of(value);
		time = msg_time;
	}

	@Override
	public void connect(@NotNull Connection<T> connection) {
		this.connection = connection;
	}

	@Override
	public Connection<T> getConnection() {
		return connection;
	}

	@Override
	public T value() {
		return value.orElse(null);
	}
}
