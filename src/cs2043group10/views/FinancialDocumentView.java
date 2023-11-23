package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class FinancialDocumentView implements IReversable {
	private FinancialDocument data;
	private final int documentId;
	private final IReversableManager manager;
	private static GridPane view;
	
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
	private static VBox createView(FinancialDocument financialDocument) {
    VBox view = new VBox(10);
    view.setPadding(new Insets(10));

    Label titleLabel = new Label("Title: " + financialDocument.title);
    Label idLabel = new Label("Document ID: " + financialDocument.documentId);
    Label patientIdLabel = new Label("Patient ID: " + financialDocument.patientId);
    Label amountLabel = new Label("Amount: $" + financialDocument.amount);
    Label descriptionLabel = new Label("Description: " + financialDocument.description);
    Label timestampLabel = new Label("Created At: " + new Date(financialDocument.createTimestamp));
    Label amountPaidLabel = new Label("Amount Paid: " + (financialDocument.amountPaid.isPresent() ? "$" + financialDocument.amountPaid.get() : "Not paid"));

    // Style labels
    Stream.of(titleLabel, idLabel, patientIdLabel, amountLabel, descriptionLabel, timestampLabel, amountPaidLabel)
            .forEach(label -> label.setStyle("-fx-font-weight: bold"));

    view.getChildren().addAll(titleLabel, idLabel, patientIdLabel, amountLabel, descriptionLabel, timestampLabel, amountPaidLabel);

    return view;
}

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
