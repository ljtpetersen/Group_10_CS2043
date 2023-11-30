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

/**
 * This is the view which allows users to create financial documents.
 * 
 * @author James Petersen
 */
public class FinancialCreateView implements IReversable {
	/**
	 * The id of the document.
	 */
	private int documentId;
	/**
	 * The id of the patient associated with the document.
	 */
	private final int patientId;
	/**
	 * The amount of the financial document.
	 */
	private int amount;
	/**
	 * The amount the patient will pay, if applicable.
	 */
	private int amountPaid;
	/**
	 * The insurance plan of the patient.
	 */
	private InsurancePlan insurance;
	/**
	 * The description of the document.
	 */
	private String description;
	/**
	 * The manager within which the view resides.
	 */
	private final IReversableManager manager;
	/**
	 * The node within which the form resides.
	 */
	private static VBox view;
	/**
	 * The title of the document.
	 */
	private String title;
	/**
	 * The field within which the user enters the document's title.
	 */
	private static TextField titleField;
	/**
	 * The field within which the user enters the document's amount.
	 */
	private static TextField amountField;
	/**
	 * The field within which the user enters the document's description.
	 */
	private static TextArea descriptionField;
	/**
	 * The text which will display the document's id.
	 */
	private static Text id;
	/**
	 * The text which will display the patient's id.
	 */
	private static Text patientIdText;
	/**
	 * The text which will display the amount the patient will
	 * have to pay, if applicable.
	 */
	private static Text amountToPay;
	/**
	 * The option that this document is a bill.
	 */
	private static RadioButton billOption;
	/**
	 * The option that this document is a payment. 
	 */
	private static RadioButton paymentOption;
	/**
	 * The button the user will press to save the document.
	 */
	private static Button saveButton;
	/**
	 * The method to be called when the document's type is switched.
	 */
	private static Consumer<Type> changeType;
	/**
	 * The text within which error messages will be displayed.
	 */
	private static Text statusText;
	/**
	 * The type of the document.
	 */
	private Type billType;

	/**
	 * Create a new financial create view with a given patient id.
	 * @param manager The manager within which the view resides.
	 * @param patientId The id of the patient.
	 */
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
	
	/**
	 * Create the view associated with this class.
	 */
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
	
	/**
	 * Get the insurance plan of the patient.
	 * @throws DatabaseException
	 */
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
	
	/**
	 * The enumeration of the types of financial documents.
	 */
	private static enum Type {
		PAYMENT,
		BILL;
	}
	
	/**
	 * The method which is called when they type is changed.
	 * @param newType The new type.
	 */
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
	
	/**
	 * The event handler which is to be called when the user tries to save their changes. 
	 * @param event The event.
	 */
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
	
	/**
	 * The event handler which is to be called when the user changes the amount of the document.
	 * @param event The event.
	 */
	private void updateAmountEvent(ActionEvent event) {
		if (billType == Type.PAYMENT) {
			updateAmountToPay();
		}
	}
	
	/**
	 * The method which will update the amount to be payed based on the insurance plan.
	 */
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
