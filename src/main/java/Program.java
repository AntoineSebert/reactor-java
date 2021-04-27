import java.util.ArrayList;

/*
A Lingua Franca file, which has a .lf extension, contains the following:
    One or more target specifications.
    Zero or more import statements.
    One or more reactor blocks, which contain reaction declarations.
*/
public class Program {
	private ArrayList<Target> targets;
	private ArrayList<Object/*Import*/> imports;
	private ArrayList<Object/*reactor*/> reactors;

	public Program(ArrayList<Target> targets, ArrayList<Object/*Import*/> imports, ArrayList<Object/*reactor*/> reactors) {
		checkParameters(targets, imports, reactors);

		this.targets = targets;
		this.imports = imports;
		this.reactors = reactors;
	}

	private static void checkParameters(ArrayList<Target> targets, ArrayList<Object/*Import*/> imports,
	                                    ArrayList<Object/*reactor*/> reactors) {
		if (null == targets)
			throw new ExceptionInInitializerError("Program targets cannot be null");

		if (null == imports)
			throw new ExceptionInInitializerError("Program imports cannot be null");

		if (null == reactors)
			throw new ExceptionInInitializerError("Program reactors cannot be null");

		if (targets.size() < 1)
			throw new ExceptionInInitializerError("Program must have at least one target");

		if (reactors.size() < 1)
			throw new ExceptionInInitializerError("Program must have at least one reactor block");
	}

	ArrayList<Target> getTargets() {
		return targets;
	}

	public ArrayList<Object> getImports() {
		return imports;
	}

	public ArrayList<Object> getReactors() {
		return reactors;
	}
}
