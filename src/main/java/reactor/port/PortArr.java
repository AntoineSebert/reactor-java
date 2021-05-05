package reactor.port;

import reactor.connection.ConnectionArr;

public interface PortArr<T> extends Port<T> {
	int size();
	ConnectionArr<T> getConnection();
	void connect(ConnectionArr<T> connection);
}
