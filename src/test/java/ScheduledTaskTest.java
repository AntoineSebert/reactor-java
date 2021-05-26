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
        long current_time = System.nanoTime() / 100000;
        assertDoesNotThrow(

                () -> (new Program.Builder())
                        .targets(Target.Java)
                        .mainReactor((new Reactor.Builder("Minimal"))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self,reaction) -> {
                                            System.out.println("Main reactor executed after " + (System.nanoTime() / 100000 - current_time) );
                                        })
                                        .triggers("STARTUP").build())
                                .build())
                        .reactors((new Reactor.Builder("startupTest"))
                                .declarations(new Timer("t", Duration.ofMillis(1000),Duration.ofMillis(1000)))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> System.out.println("reactor executed after " + (System.nanoTime() / 100000 - current_time) ))
                                        .triggers("t")
                                        .build()
                                ).build())
                        .build()
                        .run()
        );
    }
}
