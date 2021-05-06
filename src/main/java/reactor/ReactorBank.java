package reactor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class ReactorBank extends Reactor {
	private final ArrayList<Reactor> reactors = new ArrayList<>();

	public ReactorBank(@NotNull String name, @NotNull Reactor reactor, int size) {
		super(name);

		for(int i = 0; i < size; i++)
			reactors.add(reactor);
	}

	public Reactor get(int i) {
		return reactors.get(i);
	}

	public void run() throws IOException {
		for (Reactor reactor : reactors)
			reactor.run();
	}
}
