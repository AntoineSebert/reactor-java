import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import reactor.Reaction;
import reactor.ReactorVar;
import reactor.Trigger;
import reactor.input.InputVar;
import target.Target;

public class InputTest {
	@Test
	public void InputTests(){
		assertDoesNotThrow(
				() -> (new Program.Builder())
						.targets(Target.Java)
						.mainReactor((new ReactorVar.Builder("Minimal"))
								.addReaction((new Reaction.Builder())
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
