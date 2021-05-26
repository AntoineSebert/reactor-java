import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Instantiation;
import reactor.Reaction;
import reactor.Reactor;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Import {
	private Program hello_world = (new Program.Builder())
			.targets(Target.Java)
			.reactors((new Reactor.Builder("HelloWorld"))
					.reactions((new Reaction.Builder())
							.triggers("STARTUP")
							.targetCode((reaction, self) -> System.out.println("Hello World.\n"))
							.build()
					).build())
			.build(),
		_import = (new Program.Builder())
				.targets(Target.Java)
				.imports(new _import.Import(hello_world))
				.mainReactor((new Reactor.Builder("TwoHelloWorlds"))
						.statements(
								new Instantiation("a", "HelloWorld"),
								new Instantiation("b", "HelloWorld")
						)
						.build())
				.build();

	@Test
	@DisplayName("Import run")
	public void testImportRun() {
		assertDoesNotThrow(() -> _import.run());
	}

	@Test
	@DisplayName("Import toLF")
	public void testImportToLF() {
		assertDoesNotThrow(() -> _import.toLF());
	}
}
