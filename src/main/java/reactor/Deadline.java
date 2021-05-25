package reactor;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.BiFunction;

public record Deadline(@NotNull Duration deadline,  BiFunction<Reactor, Reaction, Void> handler) {
}
