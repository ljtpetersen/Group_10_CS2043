package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class FinancialDocumentView implements IReversable {
	private FinancialDocument data;
	private final int documentId;
	private final IReversableManager manager;
	private static GridPane view;
	private static Label titleLabel;
	private static Label documentIdLabel;
	private static Label patientIdLabel;
	private static Label amountLabel;
	private static Label descriptionLabel;
	private static Label createdAtLabel;
	private static Label amountPaidLabel;

	public FinancialDocumentView(IReversableManager manager, int documentId) throws DatabaseException {
		data = manager.getDatabaseManager().queryFinancialDocument(documentId);
		this.documentId = documentId;
		this.manager = manager;
		if (view == null) {
			createView();
		}
	}
	
	public FinancialDocumentView(IReversableManager manager, FinancialDocument data) {
		this.data = data;
		this.manager = manager;
		documentId = data.documentId;
		if (view == null) {
			createView();
		}
	}
	
	private static void createView() {
        view = new GridPane();
        view.setVgap(10);
        view.setHgap(10);

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
		amountLabel.setText("Amount: $" + data.amount);
		descriptionLabel.setText("Description: " + data.description);
		createdAtLabel.setText("Created At: " + data.createTimestamp);
		if (data.amountPaid.isPresent()) {
			amountPaidLabel.setText("Amount Paid: $" + data.amountPaid.get());
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

