package cs2043group10.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * This is the view which allows one to observe a financial document's contents.
 * 
 * @author James Petersen
 * @author Boluwatife Oduntan
 */
public class FinancialDocumentView implements IReversable {
	/**
	 * The data to be displayed.
	 */
	private FinancialDocument data;
	/**
	 * The id of the document whose data is to be displayed.
	 */
	private final int documentId;
	/**
	 * The manager within which this view resides.
	 */
	private final IReversableManager manager;
	/**
	 * The node which contains the data.
	 */
	private static GridPane view;
	/**
	 * The label which contains the title.
	 */
	private static Label titleLabel;
	/**
	 * The label which contains the document's id.
	 */
	private static Label documentIdLabel;
	/**
	 * The label which contains the patient's id.
	 */
	private static Label patientIdLabel;
	/**
	 * The label which contains the amount that will be added.
	 */
	private static Label amountLabel;
	/**
	 * The label which contains the description of the document.
	 */
	private static Label descriptionLabel;
	/**
	 * The label which contains the time and date the document was created.
	 */
	private static Label createdAtLabel;
	/**
	 * The label which contains the amount the patient will have to pay, if applicable.
	 */
	private static Label amountPaidLabel;

	/**
	 * Create a new financial document view from a corrsponding id.
	 * @param manager The manager within which the view resides.
	 * @param documentId The id of the document to be displayed.
	 * @throws DatabaseException
	 */
	public FinancialDocumentView(IReversableManager manager, int documentId) throws DatabaseException {
		data = manager.getDatabaseManager().queryFinancialDocument(documentId);
		this.documentId = documentId;
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	/**
	 * Create a new financial document view using some data.
	 * @param manager The manager within which the view resides.
	 * @param data The data to be displayed.
	 */
	public FinancialDocumentView(IReversableManager manager, FinancialDocument data) {
		this.data = data;
		this.manager = manager;
		documentId = data.documentId;
		if (view == null) {
			createView();
		}
	}
	
	/**
	 * Create the view associated with this class.
	 */
	private static void createView() {
        view = new GridPane();
        view.setVgap(10);
        view.setHgap(10);
        GridPane.setMargin(view, new Insets(4, 4, 4, 4));

        titleLabel = new Label("");
        documentIdLabel = new Label("");
        patientIdLabel = new Label("");
        amountLabel = new Label("");
        descriptionLabel = new Label("");
        createdAtLabel = new Label("");
        amountPaidLabel = new Label("");
        
        view.addColumn(0, titleLabel, documentIdLabel, patientIdLabel,
            amountLabel, descriptionLabel, createdAtLabel, amountPaidLabel);
    }

	@Override
	public void beforeShow() {
		titleLabel.setText("Title: " + data.title);
		documentIdLabel.setText("Document ID: " + data.documentId);
		patientIdLabel.setText("Patient ID: " + data.patientId);
		amountLabel.setText("Amount: " + (data.amount < 0 ? "-$" : "$") + (data.amount < 0 ? -data.amount / 100.0 : data.amount / 100.0));
		descriptionLabel.setText("Description: " + data.description);
		createdAtLabel.setText("Created At: " + LocalDateTime.ofInstant(Instant.ofEpochSecond(data.createTimestamp), TimeZone.getDefault().toZoneId()));
		if (data.amountPaid.isPresent()) {
			amountPaidLabel.setText("Amount Paid: $" + data.amountPaid.get() / 100.0);
		} else {
			amountPaidLabel.setText("");
		}
	}
		
	@Override
	public void afterHide() {}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "View Financial Document";
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		try {
			data = manager.getDatabaseManager().queryFinancialDocument(documentId);
			beforeShow();
		} catch (DatabaseException e) {
			e.display();
		}
	}

	@Override
	public void afterShow() {}

	@Override
	public void beforeHide() {}
}

