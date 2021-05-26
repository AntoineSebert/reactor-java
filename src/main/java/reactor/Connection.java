package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Output;

import java.time.Duration;

public class Connection<T> implements Statement {
	private Input<T> input;
	private Output<T> output;
	String input_name, output_name;
	private Duration after = Duration.ZERO;
	private boolean physical;

	public Connection(@NotNull String output_name, @NotNull String input_name) {
		if(input_name.isEmpty() || output_name.isEmpty())
			throw new ExceptionInInitializerError("Connection must specify input and output");

		this.output_name = output_name;
		this.input_name = input_name;
	}

	public boolean is_init() {
		return input != null && output != null;
	}

	public void output(Output<?> output) {
		this.output = (Output<T>) output;
	}

	public void input(Input<?> input) {
		this.input = (Input<T>) input;
	}

	public Output<T> output() {
		return output;
	}

	public Input<T> input() {
		return input;
	}

	public boolean physical() {
		return physical;
	}

	public Connection<T> physical(boolean physical) {
		this.physical = physical;

		return this;
	}

	public Duration after() {
		return after;
	}

	public Connection<T> after(Duration after) {
		this.after = after;

		return this;
	}

	public void set(T value) {
		long msg_time = physical ? Time.physical() : output.timestamp();

		if (after != Duration.ZERO)
			msg_time += after.toNanos();

		input.set(value, msg_time);
	}

	public void toLF(int lvl) {
		StringBuilder connection = new StringBuilder();

		for (int i = 0; i < lvl; i++) {
			connection.append("\t");
		}

		connection.append(output_name).append(" -> ").append(input_name);

		System.out.println(connection);
	}
}
