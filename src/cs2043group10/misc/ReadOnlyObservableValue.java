package cs2043group10.misc;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * This class represents a JavaFX observable value that cannot be modified.
 * @param <T> The type this observable value contains.
 * 
 * @author James Petersen
 */
public class ReadOnlyObservableValue<T> implements ObservableValue<T> {
	/**
	 * The value in the observable value.
	 */
	private final T value;
	
	/**
	 * Construct the observable value.
	 * @param value The value it should hold.
	 */
	public ReadOnlyObservableValue(T value) {
		this.value = value;
	}

	@Override
	public void addListener(InvalidationListener arg0) {}

	@Override
	public void removeListener(InvalidationListener arg0) {}

	@Override
	public void addListener(ChangeListener<? super T> arg0) {}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void removeListener(ChangeListener<? super T> arg0) {}
}
