package reactor.connection;

import org.jetbrains.annotations.NotNull;
import reactor.Statement;
import time.Timestamp;
import reactor.input.Input;
import reactor.output.Output;

import java.util.Optional;

record Connection<T>(@NotNull Input<T> input, @NotNull Output<T> output, @NotNull Optional<Timestamp> after,
                     boolean physical, boolean broadcast) implements Statement {
}
