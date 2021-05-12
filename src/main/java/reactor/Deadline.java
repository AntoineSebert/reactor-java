package reactor;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.Function;

public record Deadline(@NotNull Duration deadline, Function<Reaction, Void> handler) {
}
