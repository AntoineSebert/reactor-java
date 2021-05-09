package reactor.output;

import org.jetbrains.annotations.NotNull;
import reactor.Declaration;
import reactor.port.Port;

/**
 * reactor.output.Output specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#output-declaration
 */
public abstract class Output<T> extends Declaration implements Port<T> {
	protected long time;

	/**
	 * @param name name
	 * @throws ExceptionInInitializerError if the name is empty or the width is less than 1
	 */
	public Output(@NotNull String name) {
		super(name);
	}

	@Override
	public long timestamp() {
		return time;
	}
}
