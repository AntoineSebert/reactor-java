import reactor.Reactor;
import reactor.Trigger;
import reactor.port.Input;
import reactor.port.Output;

public class Gain extends Reactor {
	Scale g = new Scale();
	Test d = new Test();

	public Gain() {
		g.y.connect(d.x);
	}

	public Output reaction(Trigger Trigger.startup) {
		g.x.set(1);

		return g.x;
	}

	private static class Scale extends Object/*Reactor*/ {
		int scale = 2;
		Input<Integer> x = new Input<>();
		Output<Integer> y = new Output<>();

		public Scale(int scale) {
			this.scale = scale;
		}

		public Scale() {}

		public Output reaction() {
			y.set(x.value() * scale);

			return y;
		}
	}

	private static class Test extends Object/*Reactor*/ {
		Input<Integer> x = new Input<>();
		private boolean received_value;

		public void reaction() {
			System.out.println("Received " + x.value() + ".\n");
			received_value = true;

			if (x.value() != 2) {
				System.out.println("ERROR: Expected 2!\n");
				System.exit(1);
			}
		}

		public reaction(Trigger Trigger.shutdown) {
			System.out.println(received_value ? "Test passes.\n" : "ERROR: No value received by Test reactor!\n");
		}
	}
}