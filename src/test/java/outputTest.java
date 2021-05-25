import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class outputTest {
	Input<Integer> x = new Input<>("x", true);
	Input<Integer> y = new Input<>("y", true);
	Output<Integer> out = new Output<>("o");
	@Test
	public void testOutputTest() {

		assertDoesNotThrow(
				() -> (new Program.Builder())
						.targets(Target.Java)
						.mainReactor((new Reactor.Builder("Minimal"))
								.reactions((new Reaction.Builder())
										.targetCode((self, reaction) -> {
											x.set(1);
											y.set(2);
											return null;
										})
										.triggers("STARTUP")
										.build()
								).build())
						.reactors((new Reactor.Builder("outputTest"))
								.declarations(x, y, out)
								.statements()
								.reactions((new Reaction.Builder())
										.targetCode((self, reaction) -> {
											int result = 0;
											if (x.isPresent()) result += x.value();
											if (y.isPresent()) result += y.value();
											out.set(result);
											System.out.println(result);
											System.out.println(out.value());

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
