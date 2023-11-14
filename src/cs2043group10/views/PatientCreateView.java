package cs2043group10.views;

import java.time.LocalDate;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.InsurancePlan;
import cs2043group10.data.PatientInformation;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class PatientCreateView implements IReversable {
	private String fullName;
	private String address;
	private InsurancePlan insurance;
	private int patientId;
	private LocalDate dateOfBirth;
	private final IReversableManager manager;
	private static GridPane view;
	
	public PatientCreateView(IReversableManager manager) {
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	public PatientCreateView(IReversableManager manager, PatientInformation data) {
		fullName = data.fullName;
		address = data.address;
		insurance = data.insurance;
		dateOfBirth = data.dateOfBirth;
		patientId = data.patientId;
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
