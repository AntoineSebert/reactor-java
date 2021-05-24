package program;

import _import.Import;
import org.jetbrains.annotations.NotNull;
import reactor.Action;
import reactor.Reactor;
import scheduler.Scheduler;
import target.Target;

import java.time.Duration;
import java.util.*;

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

		this.targets = targets;
		this.imports = imports;
		this.reactors = reactors;
		this.mainReactor = mainReactor;

		HashMap<String, Reactor> importedReactors = new HashMap<>();
		for (Import _import : imports)
			importedReactors.putAll(_import.getReactors());

		Map<String, Reactor> globalReactors = new HashMap<>(importedReactors);
		for(Reactor r : reactors)
			globalReactors.put(r.name(), r);

		mainReactor.ifPresent(reactor -> reactor.setContextReactors(globalReactors));

		Iterator<Target> targetIt = targets.iterator();
		Action.TIME_PRECISION = targetIt.next().getPrecision();

		while (targetIt.hasNext()) {
			Duration newPrecision = targetIt.next().getPrecision();

			if (newPrecision.compareTo(Action.TIME_PRECISION) < 0)
				Action.TIME_PRECISION = newPrecision;
		}
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
			int number_of_threads = 10;
			Scheduler.createExecutorService(number_of_threads);
			mainReactor.ifPresent(Reactor::run);

			for (Reactor reactor : reactors)
				reactor.run();

			while (!Scheduler.isEmpty());

			for (Reactor reactor : reactors)
				reactor.before_shutdown();

			while (!Scheduler.isEmpty());

			Scheduler.shutDown();

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
