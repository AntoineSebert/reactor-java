import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Trigger;
import reactor.port.Input;
import reactor.port.Output;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StartupShutDownTest {
    @Test
    public void testOutputTest() {

        assertDoesNotThrow(
                () -> (new Program.Builder())
                        .targets(Target.Java)
                        .mainReactor((new Reactor.Builder("Minimal"))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> {

                                            return null;
                                        })
                                        .triggers(new Trigger.STARTUP())
                                        .build()
                                ).build())
                        .reactors((new Reactor.Builder("startupTest"))
                                .declarations()
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> {
                                            System.out.println("This should be printed second");

                                            return null;
                                        })
                                        .triggers(new Trigger.SHUTDOWN())
                                        .build()
                                ).build()
        ,(new Reactor.Builder("shutdowntest"))
                        .declarations()
                        .reactions((new Reaction.Builder())
                                .targetCode((self, reaction) -> {
                                    System.out.println("This should be printed first");
                                    return null;
                                })
                                .triggers(new Trigger.STARTUP())
                                .build()
                        ).build())
                        .build()
                        .run()
        );
    }
}
