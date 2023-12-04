package cs2043group10.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.PatientInformation;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PatientDocumentView implements IReversable {
	private PatientInformation data;
	private final int patientId;
	private static VBox view;
	private final IReversableManager manager;
	private static Label patientNameLabel;
	private static Label dateOfBirthLabel;
	private static Label addressLabel;
	private static Label insuranceStatusLabel;
	private static Label totalMoneyOwedLabel;
	private static Label idLabel;
	private static Label createLabel;
	private static Label modifyLabel;
	private static Label doctorIdLabel;
	private static Button edit;
	private static Button viewMedicalDocuments;
	private static Button viewFinancialDocuments;

	
	public PatientDocumentView(IReversableManager manager, int patientId) throws DatabaseException {
		data = manager.getDatabaseManager().queryPatientInformation(patientId);
		System.out.println(data.patientId);
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
		view = new VBox(5);
		view.setPadding(new Insets(10));

		patientNameLabel = new Label();
		dateOfBirthLabel = new Label();
		addressLabel = new Label();
		insuranceStatusLabel = new Label();
		totalMoneyOwedLabel = new Label();
		idLabel = new Label();
		createLabel = new Label();
		modifyLabel = new Label();
		doctorIdLabel = new Label();

		edit = new Button("Edit");
		viewMedicalDocuments = new Button("View Medical History");
		viewFinancialDocuments = new Button("View Financial History");
		HBox box = new HBox(5);
		Region r = new Region();
		box.getChildren().addAll(r, edit, viewMedicalDocuments, viewFinancialDocuments);
		HBox.setHgrow(r, Priority.ALWAYS);

		view.getChildren().addAll(patientNameLabel, dateOfBirthLabel, addressLabel, insuranceStatusLabel, totalMoneyOwedLabel, idLabel, createLabel, modifyLabel, doctorIdLabel, box);
	}

	@Override
	public void beforeShow() {
		patientNameLabel.setText("Full name: " + data.fullName);
		dateOfBirthLabel.setText("Date of birth: " + data.dateOfBirth.toString());
		addressLabel.setText("Address: " + data.address);
		insuranceStatusLabel.setText("Insurance Information: " + data.insurance.toString());
		totalMoneyOwedLabel.setText("Total Money Owed: $" + data.totalMoneyOwed);
		idLabel.setText("Id: " + data.patientId);
		createLabel.setText("Created: " + LocalDateTime.ofInstant(Instant.ofEpochSecond(data.createTimestamp), TimeZone.getDefault().toZoneId()));
		modifyLabel.setText("Last Modified: " + LocalDateTime.ofInstant(Instant.ofEpochSecond(data.modifyTimestamp), TimeZone.getDefault().toZoneId()));
		doctorIdLabel.setText("Doctor Id: " + data.doctorId);
		edit.setOnAction(this::onEdit);
		viewMedicalDocuments.setOnAction(this::onMedicalHistory);
		viewFinancialDocuments.setOnAction(this::onFinancialHistory);
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
	public void destroy() {}

	@Override
	public void refresh() {
		try {
			data = manager.getDatabaseManager().queryPatientInformation(patientId);
			beforeShow();
		} catch (DatabaseException e) {
			e.display();
		}
	}

	@Override
	public void afterShow() {}

	@Override
	public void beforeHide() {}

	private void onEdit(ActionEvent event) {
		refresh();
		manager.pushNewNode(new PatientCreateView(manager, data));
	}

	private void onMedicalHistory(ActionEvent event) {
		try {
			manager.pushNewNode(new MedicalQueryView(manager, patientId));
		} catch (DatabaseException e) {
			e.display();
		}
	}

	private void onFinancialHistory(ActionEvent event) {
		try {
			manager.pushNewNode(new FinancialQueryView(manager, patientId));
		} catch (DatabaseException e) {
			e.display();
		}
	}
}
