package _import;

import reactor.Reactor;

import java.io.File;
import java.util.HashMap;

public class Import {
    HashMap<String, Reactor> reactors;

    File file;

    File getFile(){
        return this.file;
    }

    HashMap<String, Reactor> getReactor(){
        return reactors;
    }

    public Import(File file) {
     if (!file.exists())
	     throw new ExceptionInInitializerError("The field to import does not exists");
         
         this.file = file;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return file.equals(((Import) o).file);
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}
}