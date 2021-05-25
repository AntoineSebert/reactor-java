package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Output;
import scheduler.Scheduler;

import java.io.IOException;
import java.util.*;

public class Reactor extends Declaration implements Runnable {
	protected String preamble;
	protected HashMap<String, Parameter<?>> params = new HashMap<>();
	private final HashMap<String, Declaration> declarations = new HashMap<>();
	protected ArrayList<Reaction> reactions = new ArrayList<>();
	private final HashSet<Statement> statements = new HashSet<>();

	public Reactor(@NotNull String name, @NotNull String preamble, @NotNull ArrayList<? extends Reaction> reactions,
				   @NotNull Iterable<? extends Parameter<?>> params, @NotNull Iterable<? extends Declaration> declarations,
				   @NotNull Iterable<? extends Statement> statements) {
		super(name);

		for (Parameter<?> p : params)
			this.params.put(p.name(), p);

		this.preamble = preamble;

		for (Declaration decl : declarations)
			this.declarations.put(decl.name(), decl);

		// replace by better loop
		int limit = reactions.size();
		for (int i = 0; i < limit; i++) {
			reactions.get(i).self(this);

			this.reactions.add(reactions.get(i));
		}

		for (Statement statement : statements)
			this.statements.add(statement);
	}

	/**
	 * @return the preamble
	 */
	public String getPreamble() {
		return preamble;
	}

	/**
	 * @return the reactions
	 */
	public ArrayList<Reaction> getReactions() {
		return reactions;
	}

	/**
	 * @return the parameters
	 */
	public HashMap<String, Parameter<?>> getParams() {
		return params;
	}

	public Declaration lookup(@NotNull String name) {
		if (name.isEmpty())
			throw new RuntimeException("Cannot lookup empty name");

		var _name = name.split("\\.");

		if (_name.length == 1) {
			if (declarations.containsKey(name))
				return declarations.get(name);
			else
				return ((Instantiation) statements.stream()
						.filter(s -> s instanceof Instantiation i && i.name.equals(name))
						.findFirst()
						.get()
				).reactor().get();
		}
		else
			return ((Reactor) lookup(_name[0])).lookup(String.join("", Arrays.copyOfRange(_name, 1, _name.length)));
	}

	public Parameter<?> param(@NotNull String name) {
		return params.get(name);
	}

	/**
	 * @return the statements
	 */
	public HashSet<Statement> getStatements() {
		return statements;
	}

	public void setContextReactors(@NotNull Map<String, ? extends Reactor> contextReactors) {
		for (Map.Entry<String, ? extends Reactor> entry : contextReactors.entrySet())
			if (entry.getValue() != this && !declarations.containsKey(entry.getKey()))
				declarations.put(entry.getKey(), entry.getValue());
	}

	private void resolveStatements() {
		List<Instantiation> instantiations = statements.stream()
				.filter(s -> s instanceof Instantiation)
				.map(s -> (Instantiation) s)
				.filter(i -> i.reactor().isEmpty())
				.toList();

		List<? extends Connection<?>> connections = statements.stream()
				.filter(s -> s instanceof Connection<?>)
				.map(s -> (Connection<?>) s)
				.filter(c -> !c.is_init())
				.toList();

		for (Instantiation instance : instantiations)
			if (declarations.containsKey(instance.reactor_name()) && declarations.get(instance.reactor_name()) instanceof Reactor reactor)
				instance.setReactor(reactor);
			else
				throw new ExceptionInInitializerError(
						"Could not find reactor '" + instance.reactor_name() + "' for instantiation of '" + instance.name() + "'");

		for (Connection<?> connection : connections) {
			var input_result = lookup(connection.input_name);

			if (input_result instanceof Input<?> input)
				connection.input(input);
			else
				throw new ExceptionInInitializerError("Name '" + connection.input_name + "' does not identify and Input");

			var output_result = lookup(connection.output_name);

			if (output_result instanceof Output<?> output)
				connection.output(output);
			else
				throw new ExceptionInInitializerError("Name '" + connection.output_name + "' does not identify and Output");
		}

	}

	protected void init() {
		// lazy initialization
		resolveStatements();

		for(Reaction reaction : reactions)
			reaction.init();

		int limit = reactions.size();
		for (int i = 0; i < limit; i++) {
			for (Trigger t : reactions.get(i).getTriggers().values()) {
				TriggerObserver.addReactionMapEntry(t, reactions.get(i));
			}
		}

		try {
			if (!preamble.isEmpty())
				Runtime.getRuntime().exec(preamble);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		init();

		for (Statement statement : statements)
			if (statement instanceof Instantiation instance)
				instance.reactor().ifPresent(Reactor::run);

		for (Reaction reaction : reactions)
			for (Trigger trigger : reaction.getTriggers().values())
				if (trigger instanceof Trigger.STARTUP) {
					Scheduler.addReactionTask(reaction);
				} else if (trigger instanceof Timer timer) {
					if (timer.period().isZero()) {
						Scheduler.addScheduledReaction(reaction, timer.offset().toNanos());
					} else {
						Scheduler.addRepeatingReaction(reaction, timer.offset().toNanos(), timer.period().toNanos());
					}
				}
	}

	public void before_shutdown() {
		for (Reaction reaction : reactions) {
			for (Trigger trigger : reaction.getTriggers().values()) {
				if (trigger instanceof  Trigger.SHUTDOWN)
					Scheduler.addReactionTask(reaction);
			}
		}
	}

	public void toLF(int lvl) {
		System.out.println("reactor "+name + " {");

		for (Declaration declaration: declarations.values()) {
			declaration.ToLF(1);
		}

		for (Statement state: statements) {
			if(state instanceof  Instantiation instance) {
				instance.ToLF(1);
			}
			if(state instanceof Connection connection) {
				connection.ToLF(1);
			}
		}

		System.out.println("}\n");
	}

	@Override
	public void ToLF(int lvl) {

	}

	public static class Builder {
		protected final String name;
		protected final HashSet<Parameter<?>> params = new HashSet<>();
		protected String preamble = "";
		protected HashSet<Declaration> declarations = new HashSet<>();
		protected HashSet<Statement> statements = new HashSet<>();
		protected ArrayList<Reaction> reactions = new ArrayList<>();

		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Builder param(Parameter<?> param) {
			params.add(param);

			return this;
		}

		public <T> Builder param(String name, T value) {
			params.add(new Parameter<>(name, value));

			return this;
		}

		public Builder preamble(@NotNull String preamble) {
			this.preamble = preamble;

			return this;
		}

		public Builder declarations(Declaration... declarations) {
			this.declarations = new HashSet<>(Arrays.stream(declarations).toList());

			return this;
		}

		public Builder statements(Statement... statements) {
			this.statements = new HashSet<>(Arrays.stream(statements).toList());

			return this;
		}

		public Builder reactions(Reaction... reactions) {
			this.reactions = new ArrayList<>(Arrays.stream(reactions).toList());

			return this;
		}

		public Reactor build() {
			return new Reactor(name, preamble, reactions, params, declarations, statements);
		}
	}
}
