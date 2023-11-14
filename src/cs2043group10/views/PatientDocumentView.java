package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.PatientInformation;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class PatientDocumentView implements IReversable {
	private PatientInformation data;
	private final int patientId;
	private static GridPane view;
	private final IReversableManager manager;
	
	public PatientDocumentView(IReversableManager manager, int patientId) throws DatabaseException {
		data = manager.getDatabaseManager().queryPatientInformation(patientId);
		this.patientId = patientId;
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	public PatientDocumentView(IReversableManager manager, PatientInformation data) {
		this.data = data;
		this.manager = manager;
		patientId = data.patientId;
		if (view == null) {
			createView();
		}
	}
	
	private static void createView() {
		// TODO
	}

	@Override
	public void beforeShow() {
		// Set event handlers.
		// check loginclass and disable/enable buttons (eg edit button)
	}

	@Override
	public void afterHide() {}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "Patient Document";
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeHide() {
		// TODO Auto-generated method stub
		
	}
}
