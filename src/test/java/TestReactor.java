import org.junit.jupiter.api.Test;
import _import.Import;
import reactor.Reactor;
import reactor.Reaction;
import reactor.Trigger;
import target.Target;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

public class TestReactor {
	@Test
	public void testHelloWorld() {
		HashSet<Target> targets = new HashSet<>(1);
		targets.add(Target.Java);

		HashSet<Trigger> triggers = new HashSet<>(1);
		triggers.add(Trigger.STARTUP);

		HashSet<Reaction> reactions = new HashSet<>(1);
		reactions.add(new Reaction(triggers, new HashSet<>(0), new HashSet<>(0), (reaction) -> {
			System.out.println("Hello World.\n");
			return null;
		}));

		Reactor main = new Reactor(
				"Minimal",
				new HashSet<>(0),
				new HashSet<>(0),
				new HashSet<>(0),
				new HashSet<>(0),
				new HashSet<>(0),
				new HashSet<>(0),
				reactions,
				new HashSet<>(0),
				"");

		new Program(targets, new HashSet<>(0), new HashSet<>(0), Optional.of(main)).run();
	}
}
