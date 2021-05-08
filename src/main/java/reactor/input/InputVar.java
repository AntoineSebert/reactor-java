package reactor.input;

import org.jetbrains.annotations.NotNull;
import reactor.connection.ConnectionVar;
import reactor.port.PortVar;

import java.util.Optional;

public class InputVar<T> extends Input<T> implements PortVar<T> {
	private Optional<T> value = Optional.empty();
	private ConnectionVar<T> connection;

	/**
	 * @param name    name
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public InputVar(@NotNull String name, @NotNull Optional<Boolean> mutable) {
		super(name, mutable);
	}

	public boolean isPresent() {
		return value.isPresent();
	}

	public void set(T value) {
		if (mutable)
			this.value = Optional.of(value);
	}

	@Override
	public void connect(ConnectionVar<T> connection) {
		this.connection = connection;
	}

	@Override
	public ConnectionVar<T> getConnection() {
		return connection;
	}

	public T value() {
		return value.orElse(null);
	}

	@Override
	public long timestamp() {
		return 0; // TODO
	}
}
