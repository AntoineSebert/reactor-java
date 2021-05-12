package reactor.port;

import org.jetbrains.annotations.NotNull;
import reactor.Connection;
import reactor.Declaration;
import reactor.Time;

import java.util.Optional;

/**
 * reactor.port.Output specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#output-declaration
 */
public class Output<T> extends Declaration implements Port<T> {
	protected long time;
	private Connection<T> connection;
	private Optional<T> value = Optional.empty();

	/**
	 * @param name name
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public Output(@NotNull String name) {
		super(name);
	}

	@Override
	public void set(@NotNull T value) {
		time = Time.logical();
		//
		if (connection != null)
			connection.set(value);
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
		return time;
	}

	@Override
	public boolean isPresent() {
		return value.isPresent();
	}
}
