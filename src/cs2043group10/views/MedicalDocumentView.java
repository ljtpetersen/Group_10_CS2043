package cs2043group10.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

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

/**
 * This is the view which allows one to observe the contents of a medical history entry.
 * 
 * @author James Petersen
 * @author Boluwatife Oduntan
 */
public class MedicalDocumentView implements IReversable {
	/**
	 * The data which should be displayed.
	 */
	private MedicalDocument data;
	/**
	 * The id of the document to be displayed
	 */
	private final int documentId;
	/**
	 * The manager within which this view resides.
	 */
	private final IReversableManager manager;
	/**
	 * The node which contains the contents of this view.
	 */
	private static GridPane view;
	/**
	 * The label which contains the title of the document.
	 */
	private static Label titleLabel;
	/**
	 * The label which contains the document id of the document.
	 */
	private static Label docIdLabel;
	/**
	 * The label which contains the patent id of the document.
	 */
	private static Label patientIdLabel;
	/**
	 * The label which contains the document's type.
	 */
	private static Label typeLabel;
	/**
	 * The label which contains the document's body.
	 */
	private static Label bodyLabel;
	/**
	 * The label which contains the document's auxiliary text.
	 */
	private static Label auxiliaryLabel;
	/**
	 * The label which contains the date and time the document was created.
	 */
	private static Label createdAtLabel;
	/**
	 * The label which contains the date and time the document was last modified.
	 */
	private static Label modifiedAtLabel;
	/**
	 * The button the user will press if they wish to edit the document.
	 */
	private static Button editButton;
	
	/**
	 * Create a new medical document view using an id.
	 * @param manager The manager within which the view shall reside.
	 * @param documentId The id of the document to be displayed.
	 * @throws DatabaseException
	 */
	public MedicalDocumentView(IReversableManager manager, int documentId) throws DatabaseException {
		data = manager.getDatabaseManager().queryMedicalDocument(documentId);
		this.documentId = documentId;
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	/**
	 * Create a new medical document view using some data.
	 * @param manager The manager within which the view shall reside.
	 * @param data The data to be displayed.
	 */
	public MedicalDocumentView(IReversableManager manager, MedicalDocument data) {
		this.data = data;
		this.manager = manager;
		documentId = data.documentId;
		if (view == null) {
			createView();
		}
	}
	
	/**
	 * Create the view for this class.
	 */
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
		view.add(editButton, 1, 8);
	}


	@Override
	public void beforeShow() {
		titleLabel.setText("Title: " + data.title);
		docIdLabel.setText("Document ID: " + data.documentId);
		patientIdLabel.setText("Patient ID: " + data.patientId);
		typeLabel.setText("Type: " + data.type);
		bodyLabel.setText("Body: " + data.body);
		auxiliaryLabel.setText("Auxiliary: " + data.auxiliary);
		createdAtLabel.setText("Created At: " + LocalDateTime.ofInstant(Instant.ofEpochSecond(data.createTimestamp), TimeZone.getDefault().toZoneId()));
		modifiedAtLabel.setText("Modified At: " + LocalDateTime.ofInstant(Instant.ofEpochSecond(data.modifyTimestamp), TimeZone.getDefault().toZoneId()));
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
	
	/**
	 * The event handler to be called when the user presses the edit button.
	 * @param event The event.
	 */
	private void editEvent(ActionEvent event) {
		try {
			manager.pushNewNode(new MedicalCreateView(manager, manager.getDatabaseManager().queryMedicalDocument(documentId)));
		} catch (DatabaseException e) {
			e.display();
		}
	}
}
