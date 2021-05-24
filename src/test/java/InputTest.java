import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
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
										.targetCode((self, reaction) -> {
											System.out.println("Hello world.");

											return null;
										})
										.triggers("STARTUP")
										.build()
								).build()
						)
						.build()
						.run()
		);
	}
}
