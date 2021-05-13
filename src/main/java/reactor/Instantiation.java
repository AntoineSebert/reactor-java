package reactor;

import org.jetbrains.annotations.NotNull;

public record Instantiation(@NotNull String var_name, @NotNull String reactor_name) implements Statement {
	@Override
	public int hashCode() {
		return var_name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return var_name.equals(((Instantiation) o).var_name);
	}
}
