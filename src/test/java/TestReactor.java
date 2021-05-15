import _import.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Instantiation;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Trigger;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestReactor {
	@Test
	@DisplayName("HelloWorld")
	public void testHelloWorld() {
		System.out.println("testHelloWorld");
		assertDoesNotThrow(() -> (new Program.Builder())
				.targets(Target.Java)
				.mainReactor((new Reactor.Builder("Minimal"))
						.reactions((new Reaction.Builder())
								.targetCode((reaction) -> {
									System.out.println("Hello World.\n");

									return null;
								})
								.triggers(new Trigger.STARTUP())
								.build()
						).build())
				.build()
				.run());
	}

	@Test
	@DisplayName("testGain")
	public void testGain() {
		System.out.println("testHelloWorld");
		assertDoesNotThrow(() -> (new Program.Builder())
				.targets(Target.Java)
				.mainReactor((new Reactor.Builder("TwoHelloWorlds"))
					.build())
				.build());
	}

	@Test
	@DisplayName("Import")
	public void testImport() {
		System.out.println("testImport");
		Program hello_world = (new Program.Builder())
				.targets(Target.Java)
				.reactors((new Reactor.Builder("HelloWorld"))
						.reactions((new Reaction.Builder())
								.targetCode((reaction) -> {
									System.out.println("Hello World.\n");

									return null;
								})
								.triggers(new Trigger.STARTUP())
								.build()
						).build())
				.build();

		assertDoesNotThrow(() -> (new Program.Builder())
				.targets(Target.Java)
				.imports(new Import(hello_world))
				.mainReactor((new Reactor.Builder("TwoHelloWorlds"))
						.statements(
								new Instantiation("a", "HelloWorld"),
								new Instantiation("b", "HelloWorld")
						)
						.build())
				.build()
				.run());
	}
}
