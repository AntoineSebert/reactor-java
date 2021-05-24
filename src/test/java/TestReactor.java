import _import.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.*;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestReactor {
	@Test
	@DisplayName("HelloWorld")
	public void testHelloWorld() {
		System.out.println("[testHelloWorld]");
		assertDoesNotThrow(() -> (new Program.Builder())
				.targets(Target.Java)
				.mainReactor((new Reactor.Builder("Minimal"))
						.reactions((new Reaction.Builder())
								.targetCode((reaction, self) -> {
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
	@DisplayName("Gain")
	public void testGain() {
		System.out.println("[testGain]");

		assertDoesNotThrow(() -> (new Program.Builder())
				.targets(Target.Java)
				.reactors(
						(new Reactor.Builder("Scale"))
								.param("scale", 2)
								.declarations(new Input<Integer>("x"), new Input<Integer>("y"))
								.reactions(
										(new Reaction.Builder())
												.triggers(new Output<>("y"))
												.effects(new Output<>("y"))
												.targetCode((self, reaction) -> {
													((Output<Integer>) reaction.get("y").get()).set(
															((Input<Integer>) reaction.get("x").get()).value()
																	* ((Parameter<Integer>) self.param("scale").get()).value()
													);

													return null;
												})
												.build()
								)
								.build(),
						(new Reactor.Builder("Test"))
								.declarations(
										new Input<Integer>("x"),
										new State<>("received_value", false)
								)
								.reactions(
										(new Reaction.Builder())
												.triggers(new Output<>("y"))
												.targetCode((self, reaction) -> {
													Input<Integer> x = ((Input<Integer>) reaction.get("x").get());

													System.out.println("Received " + x.value() +".\n");
													((State<Boolean>) self.get("received_value").get()).set(true);

													if (x.value() != 2) {
														System.out.println("ERROR: Expected 2!\n");
														System.exit(1);
													}

													return null;
												})
												.build(),
										(new Reaction.Builder())
												.triggers(new Trigger.STARTUP())
												.targetCode((self, reaction) -> {
													if (((State<Boolean>) self.get("received_value").get()).get())
														System.out.println("Test passes.\n");
													else
														System.out.println("ERROR: No value received by Test reactor!\n");

													return null;
												})
												.build()
								)
								.build()
				)
				.mainReactor((new Reactor.Builder("Gain"))
						.statements(
								new Instantiation("g", "Scale"),
								new Instantiation("d", "Test"),
								new Connection<Integer>(new String[]{"g", "y"}, new String[]{"d", "x"})
						)
						.reactions(
								(new Reaction.Builder())
										.triggers(new Trigger.STARTUP())
										.effects()
										.targetCode((reactor, reaction) -> {
											((Input<Integer>) reactor.get(new String[]{"g", "x"}).get()).set(1);

											return null;
										})
										.build()
						)
						.build())
				.build());
	}

	@Test
	@DisplayName("Import")
	public void testImport() {
		System.out.println("[testImport]");
		Program hello_world = (new Program.Builder())
				.targets(Target.Java)
				.reactors((new Reactor.Builder("HelloWorld"))
						.reactions((new Reaction.Builder())
								.triggers(new Trigger.STARTUP())
								.targetCode((reaction, self) -> {
									System.out.println("Hello World.\n");

									return null;
								})
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
