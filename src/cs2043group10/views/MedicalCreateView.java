package cs2043group10.views;

import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.MedicalDocument;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class MedicalCreateView implements IReversable {
	private String title;
	private String type;
	private String body;
	private String auxiliary;
	private final int patientId;
	private int documentId;
	private final IReversableManager manager;
	private static GridPane view;
	
	public MedicalCreateView(IReversableManager manager, int patientId) {
		this.patientId = patientId;
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	public MedicalCreateView(IReversableManager manager, MedicalDocument data) {
		this.manager = manager;
		patientId = data.patientId;
		title = data.title;
		documentId = data.documentId;
		auxiliary = data.auxiliary;
		body = data.body;
		type = data.type;
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
