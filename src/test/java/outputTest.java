import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class outputTest {
	@Test
	public void testOutputTest() {

		assertDoesNotThrow(
				() -> (new Program.Builder())
						.targets(Target.Java)
						.mainReactor((new Reactor.Builder("Minimal"))
								.reactions((new Reaction.Builder())
										.targetCode((self, reaction) -> {
											reaction.e("outputTest.y").set(1);
											reaction.e("outputTest.x").set(1);
											return null;
										})
										.triggers("STARTUP")
										.effects("outputTest.x", "outputTest.y")
										.build()
								).build())
						.reactors((new Reactor.Builder("outputTest"))
								.declarations(new Input<>("x", true), new Input<>("y", true), new Output<>("o"))
								.statements()
								.reactions((new Reaction.Builder())
										.targetCode((self, reaction) -> {
											int result = 0;
											if (((Input<?>) self.lookup("x")).isPresent()) result += ((Input<Integer>) self.lookup("x")).value();
											if (((Input<?>) self.lookup("y")).isPresent()) result += ((Input<Integer>) self.lookup("y")).value();
											((Input<Integer>) self.lookup("o")).set(result);
											System.out.println(result);
											System.out.println(((Input<Integer>) self.lookup("o")).value());

											return null;
										})
										.triggers("x", "y")
										.build()
								).build()
						).build()
						.run()
		);
	}
}
