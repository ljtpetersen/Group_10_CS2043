package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.PatientQuery;
import cs2043group10.data.IQuery;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class PatientQueryView implements IReversable {
	private final int doctorId;
	private IQuery<PatientQuery.PatientEntry> data;
	private IQuery<PatientQuery.PatientEntry> displayedData;
	private final IReversableManager manager;
	private static GridPane view;
	
	public PatientQueryView(IReversableManager manager, int doctorId) throws DatabaseException {
		data = manager.getDatabaseManager().queryPatientsUnderDoctor(doctorId);
		displayedData = data;
		this.doctorId = doctorId;
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	private static void createView() {
		// TODO
	}

	@Override
	public void beforeShow() {
		
	}

	@Override
	public void afterHide() {}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "Query Patients";
	}

	@Override
	public void destroy() {}

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
