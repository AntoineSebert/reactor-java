import _import.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.*;
import reactor.port.Input;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestReactor {
	@Test
	@DisplayName("Gain")
	public void testGain() {
		System.out.println("[testGain]");

		assertDoesNotThrow(() -> (new Program.Builder())
				.targets(Target.Java)
				.reactors(
						(new Reactor.Builder("Scale"))
								.declarations(
										new Parameter<>("scale", 2),
										new Input<Integer>("x"),
										new Input<Integer>("y")
								)
								.reactions(
										(new Reaction.Builder())
												.triggers("x")
												.effects("y")
												.targetCode((self, r) -> (r.e("y")).set(
														((Input<Integer>) r.t("x")).value()
																* ((Parameter<Integer>) self.lookup("scale")).value()
												))
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
												.triggers("y")
												.targetCode((self, r) -> {
													Input<Integer> x = ((Input<Integer>) r.t("x"));

													System.out.println("Received " + x.value() +".\n");
													((State<Boolean>) self.lookup("received_value")).set(true);

													if (x.value() != 2) {
														System.out.println("ERROR: Expected 2!\n");
														System.exit(1);
													}
												})
												.build(),
										(new Reaction.Builder())
												.triggers("STARTUP")
												.targetCode((self, reaction) -> {
													if (((State<Boolean>) self.lookup("received_value")).get())
														System.out.println("Test passes.\n");
													else
														System.out.println("ERROR: No value received by Test reactor!\n");
												})
												.build()
								)
								.build()
				)
				.mainReactor((new Reactor.Builder("Gain"))
						.statements(
								new Instantiation("g", "Scale"),
								new Instantiation("d", "Test"),
								new Connection<Integer>("g.y", "d.x")
						)
						.reactions(
								(new Reaction.Builder())
										.triggers("STARTUP")
										.effects("g.x")
										.targetCode((self, r) -> r.e("g.x").set(1))
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
								.triggers("STARTUP")
								.targetCode((reaction, self) -> System.out.println("Hello World.\n"))
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
