package reactor;

/**
 * reactor.Trigger specification class, either Output, Timer, Action or Input.
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification#reaction-declaration
 */
public interface Trigger {
	Trigger STARTUP = null;
	Trigger SHUTDOWN = null;
}
