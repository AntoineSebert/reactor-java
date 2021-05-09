import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Trigger;
import target.Target;

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
										.addTrigger(new Trigger.STARTUP())
										.build()
								).build()
						)
						.build()
						.run()
		);
	}
}
