package reactor.connection;

import org.jetbrains.annotations.NotNull;
import reactor.Time;
import reactor.input.InputVar;
import reactor.output.OutputVar;

import java.util.Optional;

public record ConnectionVar<T>(@NotNull InputVar<T> input, @NotNull OutputVar<T> output, @NotNull Optional<Time> after,
                               boolean physical, boolean broadcast) {
}
