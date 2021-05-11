import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Trigger;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class InputTest {
	@Test
	public void InputTests(){
		assertDoesNotThrow(
				() -> (new Program.Builder())
						.targets(Target.Java)
						.mainReactor((new Reactor.Builder("Minimal"))
								.reactions((new Reaction.Builder())
										.targetCode(reaction -> {
											System.out.println("Hello world.");

											return null;
										})
										.triggers(new Trigger.STARTUP())
										.build()
								).build()
						)
						.build()
						.run()
		);
	}
}
