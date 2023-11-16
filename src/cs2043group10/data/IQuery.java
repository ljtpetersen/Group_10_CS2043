package cs2043group10.data;

import java.util.Collection;

public interface IQuery<T> {
	public T get(int id);
	public int getEntryCount();
	public Collection<T> getEntries();
}