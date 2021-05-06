package reactor;

import java.io.IOException;

/**
 * Reactor specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reactor-block
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#contained-reactors
 */
public abstract class Reactor extends Declaration {
	protected Reactor(String name) {
		super(name);
	}

	public abstract void run() throws IOException;
}
