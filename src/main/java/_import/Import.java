package _import;

import org.jetbrains.annotations.NotNull;
import program.Program;
import reactor.Reactor;

import java.util.HashSet;

public class Import {
	private HashSet<Reactor> reactors;

	public Import(@NotNull Program program) {
		reactors = program.getReactors();
	}

	public HashSet<Reactor> getReactors() {
		return reactors;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return reactors.equals(((Import) o).reactors);
	}

	@Override
	public int hashCode() {
		return reactors.hashCode();
	}
}