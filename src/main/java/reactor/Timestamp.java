package reactor;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public record Timestamp(int time, @NotNull Optional<Unit> unit) implements Comparable<Timestamp> {
	public static final Timestamp ZERO = new Timestamp(0, Optional.empty());

	/**
	 * @return -1 if this is less than other,  1 if this is greater than other, and zero if the two instances are
	 * incomparable, i.e. if one and only one of them has no unit. Because of that, this function cannot be used to test
	 * equality; equals() should me used instead.
	 */
	@Override
	public int compareTo(@NotNull Timestamp o) {
		if (unit.isPresent() && o.unit.isPresent())
			return unit.get() == o.unit.get()
					? Integer.compare(time, o.time)
					: Integer.compare(unit.get().ordinal(), o.unit.get().ordinal());
		else
			return unit.isEmpty() && o.unit.isEmpty() ? Integer.compare(time, o.time) : 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		return time == ((Timestamp) o).time && unit.equals(((Timestamp) o).unit);
	}

	@Override
	public int hashCode() {
		return Objects.hash(time, unit);
	}
}
