package reactor.port;

import reactor.connection.ConnectionVar;

public interface PortVar<T> extends Port<T> {
	ConnectionVar<T> getConnection();
	void connect(ConnectionVar<T> connection);
}
