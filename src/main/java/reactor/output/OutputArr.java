package reactor.output;

import org.jetbrains.annotations.NotNull;
import reactor.connection.ConnectionArr;
import reactor.port.PortArr;

public class OutputArr<T> extends Output<T> implements PortArr<T> {
	private ConnectionArr<T> connection;

	/**
	 * @param name name
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public OutputArr(@NotNull String name) {
		super(name);
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public ConnectionArr<T> getConnection() {
		return connection;
	}

	@Override
	public void connect(ConnectionArr<T> connection) {
		this.connection = connection;
	}

	@Override
	public long timestamp() {
		return 0; // TODO
	}
}
