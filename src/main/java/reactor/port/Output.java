package reactor.port;

import org.jetbrains.annotations.NotNull;
import reactor.Connection;
import reactor.Declaration;
import reactor.port.Port;
import time.Time;

import java.util.Optional;

/**
 * reactor.port.Output specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#output-declaration
 */
public class Output<T> extends Declaration implements Port<T> {
	protected long time;
	private Optional<T> value = Optional.empty();
	private Connection<T> connection;

	/**
	 * @param name name
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public Output(@NotNull String name) {
		super(name);
	}

	@Override
	public void set(@NotNull T value) {
		this.value = Optional.of(value);
		time = Time.physical();
	}

	@Override
	public T value() {
		return value.orElse(null);
	}

	@Override
	public Connection<T> getConnection() {
		return connection;
	}

	@Override
	public void connect(@NotNull Connection<T> connection) {
		this.connection = connection;
	}

	@Override
	public long timestamp() {
		return 0;
	}

	@Override
	public boolean isPresent() {
		return value.isPresent();
	}
}
