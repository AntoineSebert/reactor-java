import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Timer;
import target.Target;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ScheduledTaskTest {
    @Test
    public void testShutdownAndStartup() {
        assertDoesNotThrow(
                () -> (new Program.Builder())
                        .targets(Target.Java)
                        .mainReactor((new Reactor.Builder("Minimal"))
                                .reactions((new Reaction.Builder()).triggers("STARTUP").build())
                                .build())
                        .reactors((new Reactor.Builder("startupTest"))
                                .declarations(new Timer("t", Duration.ofSeconds(1),Duration.ofSeconds(1)))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> System.out.println("This should be fired every second"))
                                        .triggers("t")
                                        .build()
                                ).build())
                        .build()
                        .run()
        );
    }
}
