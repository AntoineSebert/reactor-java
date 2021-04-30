package reactor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * reactor.Port specification class.
 */
public interface Port<T> extends Trigger, Effect {
	enum Var {
		Port(Port.class),
		PortArr(Port[].class);

		private Type type;

		Var(Type type) {
			this.type = type;
		}

		public Type getType() {
			return type;
		}
	}

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the value type
	 */
	default Type getType() {
		return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	boolean equals(Object o);

	int hashCode();
}
