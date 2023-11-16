package cs2043group10.misc;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ReadOnlyObservableValue<T> implements ObservableValue<T> {
	private final T value;
	
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
