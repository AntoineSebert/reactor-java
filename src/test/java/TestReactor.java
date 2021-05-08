import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import reactor.Reaction;
import reactor.ReactorVar;
import reactor.Trigger;
import target.Target;

public class TestReactor {
	@Test
	public void testHelloWorld() {
		assertDoesNotThrow(
				() -> (new Program.Builder())
					.targets(Target.Java)
					.mainReactor((new ReactorVar.Builder("Minimal"))
							.addReaction((new Reaction.Builder())
									.targetCode((reaction) -> {
										System.out.println("Hello World.\n");

										return null;
									})
									.addTrigger(new Trigger.STARTUP())
									.build()
							).build())
					.build()
					.run()
		);
	}
}
