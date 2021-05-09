package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Output;
import time.Timestamp;

import java.util.Optional;

public record Connection<T>(@NotNull Input<T> input, @NotNull Output<T> output, @NotNull Optional<Timestamp> after,
                            boolean physical, boolean broadcast) implements Statement {
}
