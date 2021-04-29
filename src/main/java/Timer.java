import java.util.Optional;

/**
 * Timer specification class.
 */
public class Timer {
	private String name;
	private Time period;
	private Time offset;

	/**
	 * @param name name
	 */
	public Timer(String name) {
		this(name, new Time(0, Optional.empty()), new Time(0, Optional.empty()));
	}

	/**
	 * @param name name
	 * @param period period
	 */
	public Timer(String name, Time period) {
		this(name, period, new Time(0, Optional.empty()));
	}

	/**
	 * @param name name
	 * @param period period
	 * @param offset offset
	 */
	public Timer(String name, Time period, Time offset) {
		if (name.isEmpty())
			throw new ExceptionInInitializerError("Input name cannot be empty");

		this.name = name;
		this.period = period;
		this.offset = offset;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the period
	 */
	public Time getPeriod() {
		return period;
	}

	/**
	 * @return the offset
	 */
	public Time getOffset() {
		return offset;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Timer) o).name);
	}
}
