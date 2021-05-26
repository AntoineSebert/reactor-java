package reactor;

import org.jetbrains.annotations.NotNull;
import reactor.port.Input;
import reactor.port.Output;
import scheduler.Scheduler;

import java.util.*;

public class Reactor extends Declaration implements Runnable {
	private final HashMap<String, Declaration> declarations = new HashMap<>();
	protected ArrayList<Reaction> reactions = new ArrayList<>();
	private final HashSet<Statement> statements = new HashSet<>();
	private boolean is_init;

	public Reactor(@NotNull String name, @NotNull Iterable<? extends Reaction> reactions,
				   @NotNull Iterable<? extends Declaration> declarations, @NotNull Iterable<? extends Statement> statements) {
		super(name);

		for (Declaration decl : declarations)
			this.declarations.put(decl.name(), decl);

		for (Reaction reaction : reactions) {
			reaction.self(this);
			this.reactions.add(reaction);
		}

		for (Statement statement : statements)
			this.statements.add(statement);
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

	/**
	 * @return the statements
	 */
	public HashSet<Statement> getStatements() {
		return statements;
	}

	public ArrayList<Reaction> getReactions() {
		return reactions;
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

	public void init() {
		if(!is_init) {
			resolveStatements();

			for(Reaction reaction : reactions) {
				reaction.init();

				for (Trigger t : reaction.getTriggers().values())
					TriggerObserver.addReactionMapEntry(t, reaction);
			}

			is_init = true;
		}
	}

	public void run() {
		if(!is_init)
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

	@Override
	public void toLF(int lvl) {
		init();
		System.out.print("reactor "+name+" ");
		/*
		if(!params.isEmpty()) {

			for (Parameter param: params.values()) {
				param.ToLF(0);
			}
		}
		 */

		System.out.println("{");

		for (Declaration declaration: declarations.values()) {
			declaration.toLF(1);
		}

		System.out.println();

		for (Statement state: statements) {
			if(state instanceof  Instantiation instance) {
				instance.toLF(1);
			}
			if(state instanceof Connection<?> connection) {
				connection.toLF(1);
			}
		}

		System.out.println("}\n");
	}

	public static class Builder {
		protected final String name;
		protected HashSet<Declaration> declarations = new HashSet<>();
		protected HashSet<Statement> statements = new HashSet<>();
		protected ArrayList<Reaction> reactions = new ArrayList<>();

		public Builder(@NotNull String name) {
			this.name = name;
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
			return new Reactor(name, reactions, declarations, statements);
		}
	}
}
