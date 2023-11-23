package cs2043group10.views;

import java.util.Optional;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.MedicalDocument;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MedicalCreateView implements IReversable {
	private String title;
	private String type;
	private String body;
	private String auxiliary;
	private final int patientId;
	private int documentId;
	private final IReversableManager manager;
	private static VBox view;
	private static Text id;
	private static Text patientIdText;
	private static TextField titleField;
	private static TextField typeField;
	private static TextArea descriptionField;
	private static TextField auxiliaryField;
	private static Button saveButton;
	private static Text statusText;
	
	public MedicalCreateView(IReversableManager manager, int patientId) {
		this.patientId = patientId;
		this.manager = manager;
		this.documentId = -1;
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
		
		Label typeLabel = new Label("Type: ");
		typeField = new TextField("");
		typeField.setPromptText("Type");
		typeField.setMinWidth(300);
		typeLabel.setLabelFor(typeField);
		view.getChildren().add(new HBox(typeLabel, typeField));
		
		descriptionField = new TextArea();
		descriptionField.setPromptText("Body");
		HBox.setHgrow(descriptionField, Priority.ALWAYS);
		HBox dfBox = new HBox(descriptionField);
		view.getChildren().add(dfBox);
		VBox.setVgrow(dfBox, Priority.ALWAYS);
		
		Label auxiliaryLabel = new Label("Auxiliary: ");
		auxiliaryField = new TextField("");
		auxiliaryField.setPromptText("Auxiliary");
		auxiliaryField.setMinWidth(300);
		auxiliaryLabel.setLabelFor(auxiliaryField);
		view.getChildren().add(new HBox(auxiliaryLabel, auxiliaryField));
		
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
		patientIdText.setText(Integer.toString(patientId));
		if (documentId == -1) {
			id.setText("None");
		} else {
			id.setText(Integer.toString(documentId));
		}
		if (title == null) {
			titleField.setText("");
		} else {
			titleField.setText(title);
		}
		if (type == null) {
			typeField.setText("");
		} else {
			typeField.setText(type);
		}
		if (body == null) {
			descriptionField.setText("");
		} else {
			descriptionField.setText(body);
		}
		if (auxiliary == null) {
			auxiliaryField.setText("");
		} else {
			auxiliaryField.setText(auxiliary);
		}
		statusText.setText("");
		saveButton.setOnAction(this::saveEvent);
	}

	@Override
	public void afterHide() {
		title = titleField.getText();
		type = typeField.getText();
		body = descriptionField.getText();
		auxiliary = auxiliaryField.getText();
	}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "Create Medical Document";
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		if (documentId == -1) {
			return;
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you to refresh?");
		Optional<ButtonType> bt = alert.showAndWait();
		if (bt.isPresent() && bt.get() == ButtonType.OK) {
			try {
				MedicalDocument data = manager.getDatabaseManager().queryMedicalDocument(documentId);
				title = data.title;
				type = data.type;
				body = data.body;
				auxiliary = data.auxiliary;
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
		if (title == null || title.isBlank()) {
			invalidAccum += "Title, ";
		}
		if (type == null || type.isBlank()) {
			invalidAccum += "Type, ";
		}
		if (body == null || body.isBlank()) {
			invalidAccum += "Body, ";
		}
		if (auxiliary == null || auxiliary.isBlank()) {
			invalidAccum += "Auxiliary, ";
		}
		if (invalidAccum.isEmpty()) {
			statusText.setText("");
			MedicalDocument data = new MedicalDocument(documentId, title, type, body, auxiliary, patientId, -1, -1);
			try {
				if (documentId == -1) {
					documentId = manager.getDatabaseManager().createMedicalDocument(data);
					id.setText(Integer.toString(documentId));
				} else {
					manager.getDatabaseManager().updateMedicalDocument(data);
				}
			} catch (DatabaseException e) {
				e.display();
			}
		} else {
			statusText.setText("Invalid Fields: " + invalidAccum.substring(0, invalidAccum.length() - 2));
		}
	}
}
