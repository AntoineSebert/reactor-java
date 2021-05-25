import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import reactor.Trigger;
import scheduler.Scheduler;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AbnormalExecution {
    @Test
    public void AbnormalExecution(){
        assertDoesNotThrow(
                () -> (new Program.Builder())
                        .targets(Target.Java)
                        .mainReactor((new Reactor.Builder("Minimal"))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> {
                                            Scheduler.abort();
                                            System.out.println("This should not be printed");

                                            return null;
                                        })
                                        .triggers("STARTUP")
                                        .build()
                                ).build()
                        )
                        .build()
                        .run()
        );
    }
}
