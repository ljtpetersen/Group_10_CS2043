package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.MedicalDocument;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MedicalDocumentView implements IReversable {
	private MedicalDocument data;
	private final int documentId;
	private final IReversableManager manager;
	private static GridPane view;
	private static Label titleLabel;
	private static Label docIdLabel;
	private static Label patientIdLabel;
	private static Label typeLabel;
	private static Label bodyLabel;
	private static Label auxiliaryLabel;
	private static Label createdAtLabel;
	private static Label modifiedAtLabel;
	private static Button editButton;
	
	public MedicalDocumentView(IReversableManager manager, int documentId) throws DatabaseException {
		data = manager.getDatabaseManager().queryMedicalDocument(documentId);
		this.documentId = documentId;
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	public MedicalDocumentView(IReversableManager manager, MedicalDocument data) {
		this.data = data;
		this.manager = manager;
		documentId = data.documentId;
		if (view == null) {
			createView();
		}
	}
	
	private static void createView() {
		view = new GridPane();
		view.setPadding(new Insets(10));
		view.setHgap(10);
		view.setVgap(10);

		titleLabel = new Label("");
		docIdLabel = new Label("");
		patientIdLabel = new Label("");
		typeLabel = new Label("");
		bodyLabel = new Label("");
		auxiliaryLabel = new Label("");
		createdAtLabel = new Label("");
		modifiedAtLabel = new Label("");

		view.addColumn(0, titleLabel, docIdLabel, patientIdLabel, typeLabel, bodyLabel, auxiliaryLabel, createdAtLabel, modifiedAtLabel);

		editButton = new Button("Edit");
		GridPane.setHalignment(editButton, HPos.RIGHT);
		view.add(editButton, 1, 7);
	}


	@Override
	public void beforeShow() {
		titleLabel.setText("Title: " + data.title);
		docIdLabel.setText("Document ID: " + data.documentId);
		patientIdLabel.setText("Patient ID: " + data.patientId);
		typeLabel.setText("Type: " + data.type);
		bodyLabel.setText("Body: " + data.body);
		auxiliaryLabel.setText("Auxiliary: " + data.auxiliary);
		createdAtLabel.setText("Created At: " + data.createTimestamp);
		modifiedAtLabel.setText("Modified At: " + data.modifyTimestamp);
		editButton.setOnAction(this::editEvent);
	}

	@Override
	public void afterHide() {}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "View Medical Document";
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		try {
			data = manager.getDatabaseManager().queryMedicalDocument(documentId);
			beforeShow();
		} catch (DatabaseException e) {
			e.display();
		}
	}

	@Override
	public void afterShow() {}

	@Override
	public void beforeHide() {}
	
	private void editEvent(ActionEvent event) {
		try {
			manager.pushNewNode(new MedicalCreateView(manager, manager.getDatabaseManager().queryMedicalDocument(documentId)));
		} catch (DatabaseException e) {
			e.display();
		}
	}
}
