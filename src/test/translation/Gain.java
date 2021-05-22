import reactor.Trigger;

import java.util.Optional;

class Input<T> {
	T value;

	public void set(T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}
}

class Output<T> {
	T value;
	Optional<Input<T>> input;

	public void connect(Input<T> input) {
		this.input = Optional.of(input);
	}

	public void set(T value) {
		this.value = value;
	}
}

//class Reactor {}

public class Gain extends Object/*Reactor*/ {
	Scale g = new Scale();
	Test d = new Test();

	public Gain() {
		g.y.connect(d.x);
	}

	public Input<Integer> reaction(Trigger.STARTUP trigger) {
		g.x.set(1);

		return g.x;
	}

	private static class Scale extends Object/*Reactor*/ {
		int scale = 2;
		public Input<Integer> x = new Input<>();
		Output<Integer> y = new Output<>();

		public Scale(int scale) {
			this.scale = scale;
		}

		public Scale() {}

		public Output<Integer> reaction() {
			y.set(x.get() * scale);

			return y;
		}
	}

	private static class Test extends Object/*Reactor*/ {
		public Input<Integer> x = new Input<>();
		private boolean received_value;

		public void reaction() {
			System.out.println("Received " + x.get() + ".\n");
			received_value = true;

			if (x.get() != 2) {
				System.out.println("ERROR: Expected 2!\n");
				System.exit(1);
			}
		}

		public void reaction(Trigger.SHUTDOWN trigger) {
			System.out.println(received_value ? "Test passes.\n" : "ERROR: No value received by Test reactor!\n");
		}
	}
}