package reactor;

import org.jetbrains.annotations.NotNull;
import scheduler.Scheduler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Reactor extends Declaration implements Runnable {
	protected String preamble;
	protected HashMap<String, Parameter<?>> params = new HashMap<String, Parameter<?>>();
	private final HashMap<String, Declaration> declarations = new HashMap<>();
	protected ArrayList<Reaction> reactions = new ArrayList<>();
	protected HashMap<String, Reactor> contextReactors = new HashMap<>();
	private final HashSet<Statement> statements = new HashSet<>();

	protected Map<Trigger, Reaction> pool = new HashMap<>();
	protected Queue<Reaction> exec_q = new LinkedList<>();
	AtomicBoolean in_exec_q = new AtomicBoolean(false);

	public Reactor(@NotNull String name, @NotNull String preamble, @NotNull ArrayList<? extends Reaction> reactions,
	               @NotNull Iterable<? extends Parameter<?>> params, @NotNull Iterable<? extends Declaration> declarations,
	               @NotNull Iterable<? extends Statement> statements) {
		super(name);

		for(Parameter<?> p : params)
			this.params.put(p.name(), p);

		this.preamble = preamble;

		for (Declaration decl : declarations)
			this.declarations.put(decl.name(), decl);

		// replace by better loop
		int limit = reactions.size();
		for (int i = 0; i < limit; i++) {
			reactions.get(i).self(this);

			for (Trigger t : reactions.get(i).getTriggers()) {
				TriggerObserver.addReactionMapEntry(t, reactions.get(i));
			}

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

	public Optional<Declaration> get(@NotNull String name) {
		return Optional.ofNullable(declarations.getOrDefault(name, null));
	}

	public Optional<Parameter<?>> param(@NotNull String name) {
		return Optional.ofNullable(params.getOrDefault(name, null));
	}

	/**
	 * @return the statements
	 */
	public HashSet<Statement> getStatements() {
		return statements;
	}

	public void setContextReactors(@NotNull Map<String, ? extends Reactor> contextReactors) {
		for (Map.Entry<String, ? extends Reactor> entry : contextReactors.entrySet())
			if (entry.getValue() != this)
				this.contextReactors.put(entry.getKey(), entry.getValue());
	}

	protected void init() {
		List<Instantiation> toResolve = statements.stream()
				.filter(s -> s instanceof Instantiation)
				.map(s -> (Instantiation) s)
				.filter(i -> i.reactor().isEmpty())
				.toList();

		for (Instantiation instance : toResolve) {
			if (declarations.containsKey(instance.name()) && declarations.get(instance.name()) instanceof Reactor reactor) {
				instance.setReactor(reactor);
				break;
			}

			if(instance.reactor().isEmpty())
				if(contextReactors.containsKey(instance.reactor_name()))
					instance.setReactor(contextReactors.get(instance.reactor_name()));
				else
					throw new ExceptionInInitializerError(
							"Could not find reactor '" + instance.reactor_name() + "' for instantiation of '" + instance.name() + "'");
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

		for (Declaration decl : declarations.values())
			if (decl instanceof Reactor reactor)
				reactor.run();

		for (Statement statement : statements)
			if(statement instanceof Instantiation instance)
				instance.reactor().ifPresent(Reactor::run);

		for (Reaction reaction : reactions)
			for (Trigger trigger : reaction.getTriggers())
				if (trigger instanceof Trigger.STARTUP)
					Scheduler.addReactionTask(reaction);
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
