package cs2043group10.views;

import java.util.Optional;
import java.util.function.Consumer;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import cs2043group10.data.InsurancePlan;
import cs2043group10.test.TestDatabaseManager;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FinancialCreateView implements IReversable {
	private int documentId;
	private final int patientId;
	private int amount;
	private int amountPaid;
	private InsurancePlan insurance;
	private String description;
	private final IReversableManager manager;
	private static VBox view;
	private String title;
	private static TextField titleField;
	private static TextField amountField;
	private static TextArea descriptionField;
	private static Text id;
	private static Text patientIdText;
	private static Text amountToPay;
	private static RadioButton billOption;
	private static RadioButton paymentOption;
	private static Button saveButton;
	private static Consumer<Type> changeType;
	private static Text statusText;
	private Type billType;

	public FinancialCreateView(IReversableManager manager, int patientId) {
		this.manager = manager;
		this.patientId = patientId;
		billType = Type.BILL;
		documentId = -1;
		amount = -1;
		if (view == null) {
			createView();
		}
	}
	
	public FinancialCreateView(IReversableManager manager, FinancialDocument document) {
		documentId = document.documentId;
		patientId = document.patientId;
		amount = document.amount >= 0 ? document.amount : -document.amount;
		description = document.description;
		title = document.title;
		billType = document.amount >= 0 ? Type.BILL : Type.PAYMENT; 
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	private static void createView() {
		view = new VBox(5);
		
		Label idLabel = new Label("Document Id: ");
		id = new Text("");
		idLabel.setLabelFor(id);
		view.getChildren().add(new HBox(idLabel, id));
		
		Label patientIdLabel = new Label("Patient Id: ");
		patientIdText = new Text("");
		patientIdLabel.setLabelFor(patientIdText);
		view.getChildren().add(new HBox(patientIdLabel, patientIdText));
		
		Label titleLabel = new Label("Title: ");
		titleField = new TextField("");
		titleField.setPromptText("Title");
		titleField.setMinWidth(300);
		titleLabel.setLabelFor(titleField);
		view.getChildren().add(new HBox(titleLabel, titleField));
		
		Label amountLabel = new Label("Amount: ");
		amountField = new TextField("");
		amountField.setPromptText("Amount");
		amountField.setMinWidth(300);
		amountLabel.setLabelFor(amountField);
		view.getChildren().add(new HBox(amountLabel, amountField));
		amountField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[0-9.]*")) {
					amountField.setText(newValue.replaceAll("[^0-9.]", ""));
				}
			}
		});
		
		
		descriptionField = new TextArea();
		descriptionField.setPromptText("Description");
		HBox dfBox = new HBox(descriptionField);
		HBox.setHgrow(descriptionField, Priority.ALWAYS);
		VBox.setVgrow(dfBox, Priority.ALWAYS);
		view.getChildren().add(dfBox);
		
		billOption = new RadioButton("Bill");
		paymentOption = new RadioButton("Payment");
		ToggleGroup group = new ToggleGroup();
		billOption.setToggleGroup(group);
		paymentOption.setToggleGroup(group);
		billOption.setUserData(Type.BILL);
		paymentOption.setUserData(Type.PAYMENT);
		GridPane pane = new GridPane();
		pane.addColumn(0, billOption, paymentOption);
		amountToPay = new Text("");
		Label atpLabel = new Label("Amount to Pay: ");
		pane.add(amountToPay, 2, 1);
		pane.add(atpLabel, 1, 1);
		view.getChildren().add(pane);
		pane.setHgap(8);
		pane.setVgap(4);
		group.selectedToggleProperty().addListener((ov, oldVal, newVal) -> {
			if (group.getSelectedToggle() != null) {
				changeType.accept((Type)group.getSelectedToggle().getUserData());
			}
		});
		
		for (Node node : view.getChildren()) {
			if (node instanceof HBox) {
				((HBox)node).setAlignment(Pos.CENTER_LEFT);
			}
		}
		
		saveButton = new Button("Save");
		HBox sbBox = new HBox(saveButton);
		sbBox.setAlignment(Pos.CENTER_RIGHT);
		view.getChildren().add(sbBox);
		
		statusText = new Text("");
		statusText.setFill(Color.RED);
		view.getChildren().add(statusText);
		
		GridPane.setHalignment(view, HPos.CENTER);
		GridPane.setMargin(view, new Insets(4, 100, 4, 100));
	}

	@Override
	public void beforeShow() {
		if (documentId == -1) {
			id.setText("None");
		} else {
			id.setText(Integer.toString(documentId));
		}
		patientIdText.setText(Integer.toString(patientId));
		if (amount == -1) {
			amountField.setText("");
		} else {
			amountField.setText(Double.toString(amount / 100.0));
		}
		if (description == null) {
			descriptionField.setText("");
		} else {
			descriptionField.setText(description);
		}
		if (title == null) {
			titleField.setText("");
		} else {
			titleField.setText(title);
		}
		statusText.setText("");
		changeType = this::typeChange;
		saveButton.setOnAction(this::saveEvent);
		switch (billType) {
		case BILL:
			billOption.setSelected(true);
			break;
		case PAYMENT:
			paymentOption.setSelected(true);
			break;
		}
		amountField.setOnAction(this::updateAmountEvent);
		typeChange(billType);
	}
	
	private void getInsurancePlan() throws DatabaseException {
		if (insurance != null) {
			return;
		}
		if (manager.getDatabaseManager() instanceof TestDatabaseManager) {
			insurance = new InsurancePlan(50000, 1000000, 20);
			return;
		}
		insurance = manager.getDatabaseManager().queryPatientInformation(patientId).insurance;
	}

	@Override
	public void afterHide() {
		try {
			amount = (int)(Double.parseDouble(amountField.getText())*100.0);
			if (amount < 0) {
				amount = -1;
			}
		} catch (NumberFormatException e) {
			amount = -1;
		}
		description = descriptionField.getText();
		title = titleField.getText();
	}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "Create Financial Document";
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		if (documentId == -1) {
			return;
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you want to refresh?");
		Optional<ButtonType> bt = alert.showAndWait();
		if (bt.isPresent() && bt.get() == ButtonType.OK) {
			try {
				FinancialDocument data = manager.getDatabaseManager().queryFinancialDocument(documentId);
				title = data.title;
				description = data.description;
				amount = data.amount >= 0 ? data.amount : -data.amount;
				billType = data.amount >= 0 ? Type.BILL : Type.PAYMENT;
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
	
	private static enum Type {
		PAYMENT,
		BILL;
	}
	
	private void typeChange(Type newType) {
		billType = newType;
		switch (billType) {
		case BILL:
			amountToPay.setText("N/A");
			break;
		case PAYMENT:
			updateAmountToPay();
			break;
		}
	}
	
	private void saveEvent(ActionEvent event) {
		String invalidAccum = "";
		afterHide();
		if (title == null || title.isBlank()) {
			invalidAccum += "Title, ";
		}
		if (description == null || description.isBlank()) {
			invalidAccum += "Description, ";
		}
		if (amount == -1) {
			invalidAccum += "Amount, ";
		}
		if (invalidAccum.isEmpty()) {
			statusText.setText("");
			if (billType == Type.PAYMENT) {
				updateAmountToPay();
			}
			FinancialDocument data = new FinancialDocument(documentId, patientId, amount * (billType == Type.BILL ? 1 : -1), description, -1, title, billType == Type.PAYMENT ? Optional.of(amountPaid) : Optional.empty());
			try {
				if (documentId == -1) {
					documentId = manager.getDatabaseManager().createFinancialDocument(data);
					id.setText(Integer.toString(documentId));
				} else {
					statusText.setText("Cannot modify financial document.");
				}
			} catch (DatabaseException e) {
				e.display();
			}
		} else {
			statusText.setText("Invalid Fields: " + invalidAccum.substring(0, invalidAccum.length() - 2));
		}
	}
	
	private void updateAmountEvent(ActionEvent event) {
		if (billType == Type.PAYMENT) {
			updateAmountToPay();
		}
	}
	
	private void updateAmountToPay() {
		try {
			amount = (int)(Double.parseDouble(amountField.getText()) * 100.0);
		} catch (NumberFormatException e) {
			amount = -1;
			amountToPay.setText("");
			return;
		}
		try {
			getInsurancePlan();
			amountPaid = insurance.amountPaidForPayment(amount);
			amountToPay.setText(Double.toString(amountPaid / 100.0));
		} catch (DatabaseException e) {
			e.display();
		}
	}
}
