import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import reactor.Reaction;
import reactor.ReactorVar;
import reactor.Trigger;
import target.Target;
import reactor.input.InputVar;
import reactor.output.OutputVar;

import java.util.Optional;

public class outputTest {
	InputVar<Integer> x = new InputVar<Integer>("x", Optional.of(true));
	InputVar<Integer> y = new InputVar<Integer>("y", Optional.of(true));
	OutputVar<Integer> out = new OutputVar<Integer>("o");
	@Test
	public void testHelloWorld() {

		assertDoesNotThrow(
				() -> (new Program.Builder())
						.targets(Target.Java)
						.mainReactor((new ReactorVar.Builder("Minimal"))
								.addReaction((new Reaction.Builder())
										.targetCode((reaction) -> {
											x.set(1);
											y.set(2);
											System.out.println(out.value());

											return null;
										})
										.addTrigger(new Trigger.STARTUP())
										.build()
								).build())
						.reactors((new ReactorVar.Builder("outputTest"))
								.declarations(x, y, out)
								.addReaction((new Reaction.Builder())
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
