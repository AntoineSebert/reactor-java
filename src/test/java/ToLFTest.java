import _import.Import;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.*;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ToLFTest {
    @Test
    public void TestToLF(){

        Duration duration = Duration.ofNanos(100);

        Program hello_world = (new Program.Builder())
                .targets(Target.Java)
                .reactors((new Reactor.Builder("HelloWorld"))
                        .reactions((new Reaction.Builder()).triggers("STARTUP").build())
                        .build())
                .build();

        assertDoesNotThrow(
                () -> (new Program.Builder())
                        .targets(Target.Java)
                        .imports(new Import(hello_world))
                        .mainReactor((new Reactor.Builder("Minimal"))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> {})
                                        .triggers("STARTUP")
                                        .build()
                                ).build()
                        )
                        .reactors((new Reactor.Builder("ToLFTest"))
                                .declarations(
                                        new Parameter<>("ParamTest", 2),
                                        new Input<>("x", true),
                                        new Input<>("y", true),
                                        new Output<Integer>("o"),
                                        new State<>("a", 2),
                                        new Timer("b", duration, duration)
                                )
                                .statements(
                                        new Connection<Integer>("g.y", "d.x"),
                                        new Instantiation("a", "HelloWorld"),
                                        new Instantiation("b", "HelloWorld")
                                )
                                .reactions((new Reaction.Builder()).triggers("SHUTDOWN").build()
                                ).build())
                        .build()
                        .ToLF()
        );
    }
}
