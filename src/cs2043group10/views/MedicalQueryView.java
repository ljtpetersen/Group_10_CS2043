package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.IQuery;
import cs2043group10.data.MedicalQuery;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class MedicalQueryView implements IReversable {
	private final int patientId;
	private IQuery<MedicalQuery.MedicalEntry> data;
	private IQuery<MedicalQuery.MedicalEntry> displayedData;
	private final IReversableManager manager;
	private static GridPane view;
	
	public MedicalQueryView(IReversableManager manager, int patientId) throws DatabaseException {
		this.data = manager.getDatabaseManager().queryMedicalDocumentsUnderPatient(patientId);
		this.displayedData = data;
		this.manager = manager;
		this.patientId = patientId;
		if (view == null) {
			createView();
		}
	}
	
	private static void createView() {
		// TODO
	}

	@Override
	public void beforeShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterHide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
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
