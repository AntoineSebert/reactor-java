import _import.Import;
import org.junit.jupiter.api.Test;
import program.Program;
import reactor.*;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ToLFTest {
    Input<Integer> x = new Input<>("x", true);
    Input<Integer> y = new Input<>("y", true);
    Output<Integer> out = new Output<>("o");
    @Test
    public void TestToLF(){
        Program hello_world = (new Program.Builder())
                .targets(Target.Java)
                .reactors((new Reactor.Builder("HelloWorld"))
                        .reactions((new Reaction.Builder())
                                        .targetCode((reaction, self) -> {
                                            return null;
                                            })
                                            .triggers("STARTUP")
                                            .build()
                        ).build())
                .build();

        assertDoesNotThrow(
                () -> (new Program.Builder())
                        .targets(Target.Java)
                        .imports(new Import(hello_world))
                        .mainReactor((new Reactor.Builder("Minimal"))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> {
                                            return null;
                                        })
                                        .triggers("STARTUP")
                                        .build()
                                ).build()
                        )
                        .reactors((new Reactor.Builder("ToLFTest"))
                                .declarations(x,y,out)
                                .statements(
                                        new Connection<Integer>(new String[]{"g.y"}, new String[]{"d.x"}),
                                        new Instantiation("a", "HelloWorld"),
                                        new Instantiation("b", "HelloWorld")
                                )
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> {
                                            return null;
                                        })
                                        .triggers("SHUTDOWN")
                                        .build()
                                ).build())
                        .build()
                        .ToLF()
        );
    }
}
