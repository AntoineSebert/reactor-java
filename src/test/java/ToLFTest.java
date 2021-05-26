import _import.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.*;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ToLFTest {
    Duration duration = Duration.ofNanos(100);

    Program hello_world = (new Program.Builder())
            .targets(Target.Java)
            .reactors((new Reactor.Builder("HelloWorld"))
                    .reactions((new Reaction.Builder())
                            .targetCode((self, reaction) -> {
                                System.out.println("Hello world");
                            })
                            .triggers("STARTUP")
                            .build())
                    .build())
            .build(),
            LF = new Program.Builder()
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
                                            new State<>("i", 2),
                                            new Timer("j", duration, duration),
                                            new Action<>("action", Action.Type.logical, Action.Policy.replace,Duration.ofSeconds(1),Duration.ofSeconds(1))
                                    )
                                    .statements(
                                            new Instantiation("a", "HelloWorld"),
                                            new Instantiation("b", "HelloWorld")
                                    )
                                    .reactions((new Reaction.Builder())
                                            .triggers("SHUTDOWN").build()
                                    ).build())
                            .build();


    @Test
    @DisplayName("Test ToLF")
    public void TestToLF() {
        assertDoesNotThrow(() -> LF.toLF());
    }

    @Test
    @DisplayName("Test ToRun")
    public void TestToRun() {
        assertDoesNotThrow(() -> LF.run());
    }
}