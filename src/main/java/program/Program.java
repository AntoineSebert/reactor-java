package program;

import _import.Import;
import org.jetbrains.annotations.NotNull;
import reactor.Action;
import reactor.Reactor;
import time.Timestamp;
import target.Target;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import scheduler.Scheduler;

/**
 * program := target+, import*, reactor-block+
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification
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
			throw new ExceptionInInitializerError("program.Program targets cannot be empty");

		if (reactors.isEmpty() && mainReactor.isEmpty())
			throw new ExceptionInInitializerError("program.Program must contain at least one reactor");

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


	public void run() {

		for (Target target : targets) {
			Object o = target.get("threads").isPresent() ? target.get("threads").get() : 10;
			int number_of_threads = ((int) o);
			number_of_threads = 10;
			Scheduler.createExecutorService(number_of_threads);
			mainReactor.ifPresent(Reactor::run);

			for (Reactor reactor : reactors)
				reactor.run();

			while (!Scheduler.isEmpty());

		}


	}

	public static class Builder {
		private final HashSet<Target> targets = new HashSet<>();
		private final HashSet<Import> imports = new HashSet<>();
		private final HashSet<Reactor> reactors = new HashSet<>();
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
