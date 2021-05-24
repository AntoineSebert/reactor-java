import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Timer;
import reactor.Trigger;
import target.Target;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ScheduledTaskTest {
    @Test
    public void testShutdownAndStartup() {
        Timer t = new Timer("t", Duration.ofSeconds(1),Duration.ofSeconds(1));
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
                                                    System.out.println("This should be fired every second");

                                                    return null;
                                                })
                                                .triggers(t)
                                                .build()
                                        ).build())
                        .build()
                        .run()
        );
    }

}
