package reactor;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

// might require input & output contain values of the same type
public record Connection(@NotNull Input<?> input, @NotNull Output<?> output, @NotNull Optional<Time> after,
                         boolean physical, boolean broadcast) implements Statement {
}
