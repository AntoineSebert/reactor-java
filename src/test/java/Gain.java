import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.*;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Gain {
	private Program program = (new Program.Builder())
			.targets(Target.Java)
			.reactors(
					(new Reactor.Builder("Scale"))
							.declarations(
									new Parameter<>("scale", 2),
									new Input<Integer>("x", true),
									new Output<Integer>("y")
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
											.triggers("x")
											.targetCode((self, r) -> {
												Input<Integer> x = ((Input<Integer>) r.t("x"));

												System.out.println("Received " + x.value() + ".\n");
												((State<Boolean>) self.lookup("received_value")).set(true);

												if (x.value() != 2) {
													System.out.println("ERROR: Expected 2!\n");
													System.exit(1);
												}
											})
											.build(),
									(new Reaction.Builder())
											.triggers("SHUTDOWN")
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
			.build();

	@Test
	@DisplayName("Gain run")
	public void testGainRun() {
		assertDoesNotThrow(() -> program.run());
	}

	@Test
	@DisplayName("Gain toLF")
	public void testGainToLF() {
		assertDoesNotThrow(() -> program.toLF());
	}
}
