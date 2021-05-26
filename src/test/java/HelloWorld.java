import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class HelloWorld {
	private Program program = (new Program.Builder())
			.targets(Target.Java)
				.mainReactor((new Reactor.Builder("Minimal"))
			.reactions((new Reaction.Builder())
			.targetCode((self, r) -> System.out.println("Hello World.\n"))
			.triggers("STARTUP")
								.build()
						).build())
			.build();

	@Test
	@DisplayName("HelloWorld run")
	public void testHelloWorldRun() {
		assertDoesNotThrow(() -> program.run());
	}

	@Test
	@DisplayName("HelloWorld toLF")
	public void testHelloWorldToLF() {
		assertDoesNotThrow(() -> program.toLF());
	}
}
