package reactor.output;

import org.jetbrains.annotations.NotNull;
import reactor.connection.ConnectionVar;
import reactor.port.PortVar;

import java.util.Optional;

public class OutputVar<T> extends Output<T> implements PortVar<T> {
	private Optional<T> value = Optional.empty();
	private ConnectionVar<T> connection;

	/**
	 * @param name name
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public OutputVar(@NotNull String name) {
		super(name);
	}

	@Override
	public ConnectionVar<T> getConnection() {
		return connection;
	}

	@Override
	public void connect(ConnectionVar<T> connection) {
	this.connection = connection;
	}

	@Override
	public long timestamp() {
		return 0; // TODO
	}
}
