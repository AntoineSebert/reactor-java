import reactor.Action;
import reactor.Reactor;
import target.Target;
import reactor.Time;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

/**
 * program := target+, import*, reactor-block+
 * https://github.com/icyphy/lingua-franca/wiki/Language-Specification
 */
public class Program {
	private HashSet<Target> targets;
	private HashSet<Object/*Import*/> imports;
	private HashSet<Reactor> reactors;
	private Optional<Reactor> mainReactor;

	/**
	 * @param targets targets
	 * @param imports imports
	 * @param reactors reactors
	 * @param mainReactor main reactor
	 */
	public Program(@NotNull HashSet<Target> targets, @NotNull HashSet<Object/*Import*/> imports,
	               @NotNull HashSet<Reactor> reactors, @NotNull Optional<Reactor> mainReactor) {
		if (targets.isEmpty())
			throw new ExceptionInInitializerError("Program targets cannot be empty");

		if (reactors.isEmpty() && mainReactor.isEmpty())
			throw new ExceptionInInitializerError("Program must contain at least one reactor");

		Iterator<Target> targetIt = targets.iterator();
		Action.TIME_PRECISION = targetIt.next().getPrecision();

		while(targetIt.hasNext()) {
			Time newPrecision = targetIt.next().getPrecision();

			if(newPrecision.compareTo(Action.TIME_PRECISION) < 0 )
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
	public HashSet<Object/*Import*/> getImports() {
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
	public void run() {
		try {
			if (mainReactor.isPresent())
				mainReactor.get().run();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Reactor reactor : reactors)
			try {
				reactor.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
