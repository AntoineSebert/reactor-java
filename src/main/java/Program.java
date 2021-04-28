import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;

/*
program := target+, import*, reactor-block+
*/
public class Program {
	private HashSet<Target> targets;
	private HashSet<Object/*Import*/> imports;
	private HashSet<Reactor> reactors;
	private Optional<Reactor> mainReactor;

	public Program(@NotNull HashSet<Target> targets, @NotNull HashSet<Object/*Import*/> imports,
	               @NotNull HashSet<Reactor> reactors, @NotNull Optional<Reactor> mainReactor) {
		if (targets.isEmpty())
			throw new ExceptionInInitializerError("Program targets cannot be empty");

		if (reactors.isEmpty() && mainReactor.isEmpty())
			throw new ExceptionInInitializerError("Program must contain at least one reactor");

		this.targets = targets;
		this.imports = imports;
		this.reactors = reactors;
		this.mainReactor = mainReactor;
	}

	/**
	 * @return
	 */
	HashSet<Target> getTargets() {
		return targets;
	}

	/**
	 * @return
	 */
	public HashSet<Object/*Import*/> getImports() {
		return imports;
	}

	/**
	 * @return
	 */
	public HashSet<Reactor> getReactors() {
		return reactors;
	}

	/**
	 * @return
	 */
	public Optional<Reactor> getMainReactor() {
		return mainReactor;
	}

	/**
	 *
	 */
	public void run() {}
}
