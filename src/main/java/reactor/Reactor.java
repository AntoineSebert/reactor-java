package reactor;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Reactor specification class.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reactor-block
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#contained-reactors
 */
public abstract class Reactor extends Declaration {

	protected Map<Trigger, Reaction> pool = new HashMap<Trigger, Reaction>();

	protected Queue<Reaction> exec_q = new LinkedList<>();

	AtomicBoolean in_exec_q = new AtomicBoolean(false);

	protected Reactor(String name) {
		super(name);
	}

	public abstract void run() throws IOException;
}
