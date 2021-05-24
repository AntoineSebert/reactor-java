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
	 */
	public Output(@NotNull String name) {
		super(name);
	}

	/*
	TODO :
	Reactions in a reactor may set an output value more than once at any instant of logical time, but only the last of
	the values set will be sent on the output port.
	 */
	@Override
	public void set(@NotNull T value) {
		time = Time.logical();
		this.value = Optional.of(value);

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
