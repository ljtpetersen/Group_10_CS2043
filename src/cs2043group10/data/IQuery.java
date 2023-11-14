package cs2043group10.data;

import java.util.Comparator;
import java.util.function.Predicate;

public interface IQuery<T> {
	public T get(int index);
	public int getEntryCount();
	public void sortBy(Comparator<T> comparator);
	public IQuery<T> filter(Predicate<T> predicate);
}