package reactor.connection;

import org.jetbrains.annotations.NotNull;
import reactor.Timestamp;
import reactor.input.InputArr;
import reactor.output.OutputArr;

import java.util.Optional;

public record ConnectionArr<T>(@NotNull InputArr<?> input, @NotNull OutputArr<?> output,
                               @NotNull Optional<Timestamp> after, boolean physical, boolean broadcast) {
}
