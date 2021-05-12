package reactor;


/**
 * reactor.Trigger specification class, either Timer, Action, Output or Input.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reaction-declaration
 */
public interface Trigger {
	long timestamp();

	class STARTUP implements Trigger {
		public long timestamp() {
			return 0;
		}
	}
	class SHUTDOWN implements Trigger {
		public long timestamp() {
			return Long.MAX_VALUE;
		}
	}
}
