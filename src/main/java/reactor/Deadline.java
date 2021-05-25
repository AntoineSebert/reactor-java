package reactor;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.BiConsumer;

public record Deadline(@NotNull Duration deadline,  BiConsumer<Reactor, Reaction> handler) {
}
