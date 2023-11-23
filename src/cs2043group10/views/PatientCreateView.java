package cs2043group10.views;

import java.time.LocalDate;
import java.util.Optional;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.InsurancePlan;
import cs2043group10.data.PatientInformation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PatientCreateView implements IReversable {
	private String fullName;
	private String address;
	private InsurancePlan insurance;
	private int patientId;
	private LocalDate dateOfBirth;
	private final IReversableManager manager;
	private static VBox view;
	private static TextField nameField;
	private static TextField addressField;
	private static Text id;
	private static DatePicker birthField;
	private static TextField insuranceDeductibleField;
	private static TextField insuranceOutOfPocketMaximum;
	private static TextField insuranceCostSharePercentage;
	private static Button saveButton;
	private static Text statusText;
	
	public PatientCreateView(IReversableManager manager) {
		this.manager = manager;
		this.patientId = -1;
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
		view = new VBox(5);
		
		Label idLabel = new Label("Patient Id: ");
		id = new Text("");
		idLabel.setLabelFor(id);
		view.getChildren().add(new HBox(idLabel, id));
		
		Label nameLabel = new Label("Full Name: ");
		nameField = new TextField();
		nameField.setPromptText("Last Name, First Name Middle Names...");
		nameField.setMinWidth(300);
		nameField.setMaxWidth(300);
		nameLabel.setLabelFor(nameField);
		view.getChildren().add(new HBox(nameLabel, nameField));
		
		Label addressLabel = new Label("Address: ");
		addressField = new TextField();
		addressField.setPromptText("# Street, City, Province/State, Country");
		addressField.setMinWidth(500);
		addressField.setMaxWidth(500);
		addressLabel.setLabelFor(addressField);
		view.getChildren().add(new HBox(addressLabel, addressField));
		
		Label birthLabel = new Label("Date of Birth: ");
		birthField = new DatePicker();
		birthLabel.setLabelFor(birthField);
		view.getChildren().add(new HBox(birthLabel, birthField));
		
		insuranceDeductibleField = new TextField();
		insuranceDeductibleField.setPromptText("Deductible");
		insuranceOutOfPocketMaximum = new TextField();
		insuranceOutOfPocketMaximum.setPromptText("Out of Pocket Maximum");
		insuranceCostSharePercentage = new TextField();
		insuranceCostSharePercentage.setPromptText("Cost Share Percentage");
		insuranceDeductibleField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[0-9.]*")) {
					insuranceDeductibleField.setText(newValue.replaceAll("[^0-9.]", ""));
				}
			}
		});
		insuranceCostSharePercentage.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[0-9.]*")) {
					insuranceCostSharePercentage.setText(newValue.replaceAll("[^0-9.]", ""));
				}
			}
		});
		insuranceOutOfPocketMaximum.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[0-9.]*")) {
					insuranceOutOfPocketMaximum.setText(newValue.replaceAll("[^0-9.]", ""));
				}
			}
		});
		
		insuranceDeductibleField.setMinWidth(300);
		insuranceOutOfPocketMaximum.setMinWidth(300);
		insuranceCostSharePercentage.setMinWidth(300);
		
		Label deductibleLabel = new Label("Insurance Deductible: ");
		deductibleLabel.setLabelFor(insuranceDeductibleField);
		view.getChildren().add(new HBox(deductibleLabel, insuranceDeductibleField));
		
		Label oopMaxLabel = new Label("Insurance Out of Pocket Maximum: ");
		oopMaxLabel.setLabelFor(insuranceOutOfPocketMaximum);
		view.getChildren().add(new HBox(oopMaxLabel, insuranceOutOfPocketMaximum));
		
		Label cspLabel = new Label("Cost Share Percentage: ");
		cspLabel.setLabelFor(insuranceCostSharePercentage);
		view.getChildren().add(new HBox(cspLabel, insuranceCostSharePercentage));
		
		for (Node n : view.getChildren()) {
			if (n instanceof HBox) {
				((HBox)n).setAlignment(Pos.CENTER_LEFT);
			}
		}
		
		saveButton = new Button("Save");
		HBox sbBox = new HBox(saveButton);
		sbBox.setAlignment(Pos.CENTER_RIGHT);
		view.getChildren().add(sbBox);
		
		statusText = new Text("");
		statusText.setFill(Color.RED);
		view.getChildren().add(statusText);
		view.setAlignment(Pos.CENTER_LEFT);
		
		GridPane.setMargin(view, new Insets(4, 100, 4, 100));
		GridPane.setHalignment(view, HPos.CENTER);
	}

	@Override
	public void beforeShow() {
		if (fullName == null) {
			nameField.setText("");
		} else {
			nameField.setText(fullName);
		}
		if (address == null) {
			addressField.setText("");
		} else {
			addressField.setText(address);
		}
		if (patientId == -1) {
			id.setText("None");
		} else {
			id.setText(Integer.toString(patientId));
		}
		
		birthField.setValue(dateOfBirth);
		
		if (insurance == null) {
			insuranceDeductibleField.setText("");
			insuranceOutOfPocketMaximum.setText("");
			insuranceCostSharePercentage.setText("");
		} else {
			if (insurance.costSharePercentage < 0) {
				insuranceCostSharePercentage.setText("");
			} else {
				insuranceCostSharePercentage.setText(Double.toString(insurance.costSharePercentage / 100.0));
			}
			if (insurance.deductible < 0) {
				insuranceDeductibleField.setText("");
			} else {
				insuranceDeductibleField.setText(Double.toString(insurance.deductible / 100.0));
			}
			if (insurance.outOfPocketMaximum < 0) {
				insuranceOutOfPocketMaximum.setText("");
			} else {
				insuranceOutOfPocketMaximum.setText(Double.toString(insurance.outOfPocketMaximum / 100.0));
			}
		}
		
		saveButton.setOnAction(this::saveEvent);
		statusText.setText("");
	}

	@Override
	public void afterHide() {
		fullName = nameField.getText();
		address = addressField.getText();
		int deductible;
		int oopMax;
		int csp;
		try {
			deductible = (int)(Double.parseDouble(insuranceDeductibleField.getText()) * 100.0);
			if (deductible < 0) {
				deductible = -1;
			}
		} catch (NumberFormatException e) {
			deductible = -1;
		}
		try {
			csp = (int)(Double.parseDouble(insuranceCostSharePercentage.getText()) * 100.0);
			if (csp > 100 || csp < 0) {
				csp = -1;
			}
		} catch (NumberFormatException e) {
			csp = -1;
		}
		try {
			oopMax = (int)(Double.parseDouble(insuranceOutOfPocketMaximum.getText()) * 100.0);
			if (oopMax < 0) {
				oopMax = -1;
			}
		} catch (NumberFormatException e) {
			oopMax = -1;
		}
		insurance = new InsurancePlan(deductible, oopMax, csp);
		dateOfBirth = birthField.getValue();
	}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		if (patientId == -1) {
			return "Create Patient Document";
		} else {
			return "Edit Patient Document";
		}
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		if (patientId == -1) {
			return;
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you want to refresh?");
		Optional<ButtonType> bt = alert.showAndWait();
		if (bt.isPresent() && bt.get() == ButtonType.OK) {
			try {
				PatientInformation data = manager.getDatabaseManager().queryPatientInformation(patientId);
				this.address = data.address;
				this.fullName = data.fullName;
				this.dateOfBirth = data.dateOfBirth;
				this.insurance = data.insurance;
				beforeShow();
			} catch (DatabaseException e) {
				e.display();
			}
		}
	}

	@Override
	public void afterShow() {}

	@Override
	public void beforeHide() {}
	
	private void saveEvent(ActionEvent event) {
		String invalidAccum = "";
		afterHide();
		if (fullName == null || fullName.isBlank()) {
			invalidAccum += "Full Name, ";
		}
		
		if (address == null || address.isBlank()) {
			invalidAccum += "Address, ";
		}
		
		if (insurance == null) {
			invalidAccum += "All Insurance Fields, ";
		} else {
			if (insurance.deductible < 0) {
				invalidAccum += "Deductible, ";
			}
			if (insurance.costSharePercentage < 0) {
				invalidAccum += "Cost Share Percentage, ";
			}
			if (insurance.outOfPocketMaximum < 0) {
				invalidAccum += "Out of Pocket Maximum, ";
			}
		}
		
		if (dateOfBirth == null) {
			invalidAccum += "Date Of Birth, ";
		}
		
		if (invalidAccum.isEmpty()) {
			statusText.setText("");
			PatientInformation data = new PatientInformation(patientId, fullName, address, insurance, -1, dateOfBirth, -1, -1, manager.getDatabaseManager().getId());
			try {
				if (patientId == -1) {					
					int id = manager.getDatabaseManager().createPatient(data);
					patientId = id;
					PatientCreateView.id.setText(Integer.toString(patientId));
				} else {
					manager.getDatabaseManager().updatePatient(data);
				}
			} catch (DatabaseException e) {
				e.display();
			}
		} else {
			statusText.setText("Invalid Fields: " + invalidAccum.substring(0, invalidAccum.length() - 2));
		}
	}
}
