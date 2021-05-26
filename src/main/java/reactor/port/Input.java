package reactor.port;

import org.jetbrains.annotations.NotNull;
import reactor.Connection;
import reactor.Declaration;
import reactor.Time;
import reactor.TriggerObserver;

import java.util.Optional;
import java.util.UUID;

/**
 * Input specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#input-declaration
 */
public class Input<T> extends Declaration implements Port<T> {
	protected boolean mutable = true;
	protected long time;
	private Optional<T> value = Optional.empty();
	private Connection<T> connection;
	private UUID uuid;


	public Input(@NotNull String name) {
		super(name);
		this.uuid = UUID.randomUUID();
	}

	@Override
	public void toLF(int lvl) {

		String input = "\t".repeat(lvl) + "input " + name + ":" +";";
		System.out.println(input);
	}

	/**
	 * @param name    name
	 * @param mutable mutability
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public Input(@NotNull String name, boolean mutable) {
		super(name);
		this.mutable = mutable;
		this.uuid = UUID.randomUUID();
	}

	/**
	 * @return the mutability
	 */
	public boolean isMutable() {
		return mutable;
	}

	@Override
	public long timestamp() {
		return time;
	}

	@Override
	public boolean isPresent() {
		return value.isPresent();
	}

	private void _set(@NotNull T value) {
		assert mutable;
		this.value = Optional.of(value);
		TriggerObserver.update(this);
	}

	@Override
	public void set(@NotNull T value) {
		_set(value);
		time = Time.logical(); // not sure if physical or logical
	}

	public void set(@NotNull T value, long msg_time) {
		_set(value);
		time = msg_time;
	}

	@Override
	public void connect(@NotNull Connection<T> connection) {
		this.connection = connection;
	}

	@Override
	public Connection<T> getConnection() {
		return connection;
	}

	@Override
	public T value() {
		return value.orElse(null);
	}

	public String toString() {
		return String.valueOf(this.uuid);
	}
}
