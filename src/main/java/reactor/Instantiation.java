package reactor;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Instantiation implements Statement {
	Optional<Reactor> reactor = Optional.empty();
	String name, reactor_name;

	public Instantiation(@NotNull String name, @NotNull String reactor_name) {
		this.name = name;
		this.reactor_name = reactor_name;
	}

	public String name() {
		return name;
	}

	public String reactor_name() {
		return reactor_name;
	}

	public Optional<Reactor> reactor() {
		return reactor;
	}

	public void setReactor(Reactor reactor) {
		this.reactor = Optional.of(reactor);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return name.equals(((Instantiation) o).name);
	}

	public void ToLF(int lvl){
		String instantiation = "\t".repeat(lvl) +
				name + " = new " + reactor_name + "();";

		System.out.println(instantiation);
	}
}
