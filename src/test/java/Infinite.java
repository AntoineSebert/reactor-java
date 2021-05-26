import org.junit.jupiter.api.Test;
import program.Program;
import reactor.Reaction;
import reactor.Reactor;
import target.Target;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Infinite {
    @Test
    public void testShutdownAndStartup() {
        assertDoesNotThrow(
                () -> (new Program.Builder())
                        .targets(Target.Java)
                        .mainReactor((new Reactor.Builder("Minimal"))
                                .reactions((new Reaction.Builder())
                                        .targetCode((self, reaction) -> {
                                            while(true)
                                                System.out.println("This should be fired every second");
                                        })
                                        .triggers("STARTUP")
                                        .build()
                                ).build())
                        .build()
                        .run()
        );
    }
}
