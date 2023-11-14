package cs2043group10.data;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.views.PatientDocumentView;
import cs2043group10.views.PatientQueryView;

public enum LoginClass {
	NOT_LOGGED_IN,
	DOCTOR,
	PATIENT;
	
	public IReversable instantiateHomeView(IReversableManager manager, int id) throws DatabaseException {
		switch (this) {
		case NOT_LOGGED_IN:
			throw new RuntimeException("Cannot instantiate home view for not logged in.");
		case DOCTOR:
			return new PatientQueryView(manager, id);
		case PATIENT:
			return new PatientDocumentView(manager, id);
		default:
			throw new RuntimeException("Invalid LoginClass value.");
		}
	}
}