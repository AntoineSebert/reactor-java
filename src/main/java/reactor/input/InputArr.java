package reactor.input;

import org.jetbrains.annotations.NotNull;
import reactor.connection.ConnectionArr;
import reactor.port.PortArr;

import java.util.ArrayList;
import java.util.Optional;

public class InputArr<T> extends Input<T> implements PortArr<T> {
	private ArrayList<Optional<T>> value = new ArrayList<>();
	private ConnectionArr<T> connection;

	/**
	 * @param name    name
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	InputArr(@NotNull String name, @NotNull Optional<Boolean> mutable, int size) {
		super(name, mutable);
		value.ensureCapacity(size);
	}

	public void set(int i, T value) {
		if (mutable)
			this.value.set(i, Optional.of(value));
	}

	public boolean isPresent(int i) {
		return value.get(i).isPresent();
	}

	public T value(int i) {
		return value.get(i).orElse(null);
	}

	@Override
	public void connect(ConnectionArr<T> connection) {
		this.connection = connection;
	}

	@Override
	public ConnectionArr<T> getConnection() {
		return connection;
	}

	@Override
	public int size() {
		return value.size();
	}
}
