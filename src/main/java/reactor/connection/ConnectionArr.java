package reactor.connection;

import org.jetbrains.annotations.NotNull;
import reactor.Time;
import reactor.input.InputArr;
import reactor.output.Output;

import java.util.Optional;

public record ConnectionArr<T>(@NotNull InputArr<?> input, @NotNull OutputArr<?> output, @NotNull Optional<Time> after,
                               boolean physical, boolean broadcast) {
}
