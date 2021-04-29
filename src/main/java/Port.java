/**
 * Port specification class.
 */
public interface Port<T> extends Trigger, Effect {
	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the width
	 */
	int getWidth();

	/**
	 * @return true if the port is a multiple, i.e. its width is greater than 1
	 */
	boolean isMultiport();

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();
}
