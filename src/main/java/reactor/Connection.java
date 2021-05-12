package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Output;
import time.Time;
import time.Timestamp;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public record Connection<T>(@NotNull Input<T> input, @NotNull Output<T> output, @NotNull Optional<Timestamp> after,
                           boolean physical) implements Statement {
	public void set(T value) {
		long msg_time = physical ? Time.physical() : output.timestamp();

		if (after.isPresent()) {
			Timestamp delay = after.get();
			msg_time += TimeUnit.NANOSECONDS.convert(delay.time(), delay.unit().orElse(TimeUnit.NANOSECONDS));
		}

		input.set(value, msg_time);
	}
}
