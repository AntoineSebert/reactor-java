package reactor;

import org.jetbrains.annotations.NotNull;
import time.Timestamp;

import java.util.function.Function;

public record Deadline(@NotNull Timestamp deadline, Function<Reaction, Void> handler) {
}
