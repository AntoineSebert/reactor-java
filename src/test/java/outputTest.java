import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Trigger;
import target.Target;
import reactor.port.Input;
import reactor.port.Output;

import java.util.Optional;

public class outputTest {
	Input<Integer> x = new Input<>("x", Optional.of(true));
	Input<Integer> y = new Input<>("y", Optional.of(true));
	Output<Integer> out = new Output<>("o");
	@Test
	public void testHelloWorld() {

		assertDoesNotThrow(
				() -> (new Program.Builder())
						.targets(Target.Java)
						.mainReactor((new Reactor.Builder("Minimal"))
								.reactions((new Reaction.Builder())
										.targetCode((reaction) -> {
											x.set(1);
											y.set(2);
											System.out.println(out.value());

											return null;
										})
										.addTrigger(new Trigger.STARTUP())
										.build()
								).build())
						.reactors((new Reactor.Builder("outputTest"))
								.declarations(x, y, out)
								.reactions((new Reaction.Builder())
										.targetCode((reaction) -> {
											int result = 0;
											if (x.isPresent()) result += x.value();
											if (y.isPresent()) result += y.value();
											out.set(result);

											return null;
										})
										.addTrigger(x)
										.addTrigger(y)
										.build()
								).build()
						).build()
						.run()
		);
	}
}