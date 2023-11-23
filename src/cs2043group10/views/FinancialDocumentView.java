package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Optional;


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
        view = new GridPane();
        view.setVgap(10);
        view.setHgap(10);

        Label titleLabel = new Label("Title: ");
        

        Label documentIdLabel = new Label("Document ID: ");
        

        Label patientIdLabel = new Label("Patient ID: ");
        

        Label amountLabel = new Label("Amount: ");
        

        Label descriptionLabel = new Label("Description: ");
       

        Label createdAtLabel = new Label("Created At: ");
        

        Label amountPaidLabel = new Label("Amount Paid: ");
        
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
		
	
 Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            
        });

        GridPane.setConstraints(editButton, 1, 7);
        GridPane.setMargin(editButton, new Insets(10, 0, 0, 0)); // Adjust margins as needed

        view.getChildren().add(editButton);
	@Override
	public void afterHide() {}

	@Override
	public Node getNode() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "View Financial Document";
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

