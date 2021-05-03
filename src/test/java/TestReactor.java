import org.junit.jupiter.api.Test;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Trigger;
import target.Target;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestReactor {
	@Test
	public void testHelloWorld() {
		(new Program.Builder())
				.targets(Stream.of(Target.Java).collect(Collectors.toCollection(HashSet::new)))
				.mainReactor((new Reactor.Builder("Minimal"))
						.reactions(Stream.of(
								(new Reaction.Builder())
										.targetCode((reaction) -> {
											System.out.println("Hello World.\n");
											return null;
										})
										.triggers(Stream.of(Trigger.STARTUP).collect(
												Collectors.toCollection(HashSet::new)
										))
										.build()
						).collect(Collectors.toCollection(HashSet::new)))
						.build())
				.build()
				.run();
	}
}
