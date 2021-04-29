public interface IO<T> {
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
}
