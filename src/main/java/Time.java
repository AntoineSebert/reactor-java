import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record Time(int time, @NotNull Optional<Unit> unit) {}
