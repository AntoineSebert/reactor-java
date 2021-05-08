import _import.Import;
import org.jetbrains.annotations.NotNull;
import reactor.Action;
import reactor.Reactor;
import reactor.Timestamp;
import target.Target;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

/**
 * program := target+, import*, reactor-block+
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification
 * TODO : add toLF() to everything, to check if source is preserved
 */
public record Program(HashSet<Target> targets, HashSet<Import> imports,
                      HashSet<Reactor> reactors, Optional<Reactor> mainReactor) {
	/**
	 * @param targets     targets
	 * @param imports     imports
	 * @param reactors    reactors
	 * @param mainReactor main reactor
	 */
	public Program(@NotNull HashSet<Target> targets, @NotNull HashSet<Import> imports,
	               @NotNull HashSet<Reactor> reactors, @NotNull Optional<Reactor> mainReactor) {
		if (targets.isEmpty())
			throw new ExceptionInInitializerError("Program targets cannot be empty");

		if (reactors.isEmpty() && mainReactor.isEmpty())
			throw new ExceptionInInitializerError("Program must contain at least one reactor");

		Iterator<Target> targetIt = targets.iterator();
		Action.TIME_PRECISION = targetIt.next().getPrecision();

		while (targetIt.hasNext()) {
			Timestamp newPrecision = targetIt.next().getPrecision();

			if (newPrecision.compareTo(Action.TIME_PRECISION) < 0)
				Action.TIME_PRECISION = newPrecision;
		}

		this.targets = targets;
		this.imports = imports;
		this.reactors = reactors;
		this.mainReactor = mainReactor;
	}

	/**
	 * @return the targets
	 */
	HashSet<Target> getTargets() {
		return targets;
	}

	/**
	 * @return the imports
	 */
	public HashSet<Import> getImports() {
		return imports;
	}

	/**
	 * @return the reactors
	 */
	public HashSet<Reactor> getReactors() {
		return reactors;
	}

	/**
	 * @return the eventual main reactor
	 */
	public Optional<Reactor> getMainReactor() {
		return mainReactor;
	}

	/**
	 * Runs the program.
	 */
	public void run() throws IOException {
		if (mainReactor.isPresent())
			mainReactor.get().run();

		for (Reactor reactor : reactors)
			reactor.run();
	}

	public static class Builder {
		private HashSet<Target> targets = new HashSet<>();
		private HashSet<Import> imports = new HashSet<>();
		private HashSet<Reactor> reactors = new HashSet<>();
		private Optional<Reactor> mainReactor = Optional.empty();

		public Program build() {
			return new Program(targets, imports, reactors, mainReactor);
		}

		public Builder targets(Target... targets) {
			this.targets.addAll(Arrays.stream(targets).toList());

			return this;
		}

		public Builder imports(Import... imports) {
			this.imports.addAll(Arrays.stream(imports).toList());

			return this;
		}

		public Builder reactors(Reactor... reactors) {
			this.reactors.addAll(Arrays.stream(reactors).toList());

			return this;
		}

		public Builder mainReactor(@NotNull Reactor mainReactor) {
			this.mainReactor = Optional.of(mainReactor);

			return this;
		}
	}
}
