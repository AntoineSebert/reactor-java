package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Output;

import java.time.Duration;
import java.util.Optional;

public record Connection<T>(@NotNull Input<T> input, @NotNull Output<T> output, @NotNull Optional<Duration> after,
                           boolean physical) implements Statement {
	public void set(T value) {
		long msg_time = physical ? Time.physical() : output.timestamp();

		if (after.isPresent())
			msg_time += after.get().toNanos();

		input.set(value, msg_time);
	}
}
