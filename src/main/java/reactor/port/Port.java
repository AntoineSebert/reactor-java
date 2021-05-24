package reactor.port;

import org.jetbrains.annotations.NotNull;
import reactor.Connection;
import reactor.Trigger;

/**
 * Port specification class.
 */
public interface Port<T> extends Trigger {
	Connection<T> getConnection();
	void connect(@NotNull Connection<T> connection);
	T value();
	long timestamp();
	boolean isPresent();
	void set(@NotNull T value);
}
