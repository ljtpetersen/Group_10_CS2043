package cs2043group10.data;

import java.util.Collection;
import java.util.function.Predicate;

public interface IQuery<T> {
	public T get(int id);
	public int getEntryCount();
	public IQuery<T> filter(Predicate<T> predicate);
	public Collection<T> getEntries();
}