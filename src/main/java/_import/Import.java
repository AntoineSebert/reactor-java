package _import;

import org.jetbrains.annotations.NotNull;
import program.Program;
import reactor.Reactor;

import java.util.*;

public class Import {
	private HashMap<String/*name / alias*/, Reactor> reactors = new HashMap<>();

	private static <T> Set<T> findDuplicates(Iterable<? extends T> collection) {
		Set<T> duplicates = new LinkedHashSet<>();
		Collection<T> uniques = new HashSet<>();

		for(T t : collection)
			if(!uniques.add(t))
				duplicates.add(t);

		return duplicates;
	}

	public Import(@NotNull Program program) {
		if (program.getReactors().isEmpty())
			throw new ExceptionInInitializerError("No reactors to import");
	}

	public Import(@NotNull Program program, @NotNull Map<String/*name*/, String/*alias*/> aliasing) {
		this(program);

		if (aliasing.isEmpty())
			for (Reactor r : program.getReactors())
				reactors.put(r.name(), r);
		else {
			Set<String> dups = findDuplicates(aliasing.values());

			if(dups.isEmpty()) {
				for(String key : aliasing.keySet()) {
					boolean found = false;

					for (Reactor r : program.getReactors())
						if(r.name().equals(key)) {
							found = true;
							break;
						}

					if(!found)
						throw new ExceptionInInitializerError("Reactor " + key + " could not be found");
				}

				for (Reactor r : program.getReactors())
					reactors.put(aliasing.getOrDefault(r.name(), r.name()), r);
			}
			else
				throw new ExceptionInInitializerError("Duplicated aliases " + Arrays.toString(dups.toArray()));
		}
	}

	public HashMap<String, Reactor> getReactors() {
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