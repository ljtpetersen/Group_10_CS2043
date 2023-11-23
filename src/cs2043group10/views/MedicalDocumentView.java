package cs2043group10.views;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.MedicalDocument;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class MedicalDocumentView implements IReversable {
	private MedicalDocument data;
	private final int documentId;
	private final IReversableManager manager;
	private static GridPane view;
	
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

        
        Label titleLabel = new Label("Title: ");
        Label docIdLabel = new Label("Document ID: " );
        Label patientIdLabel = new Label("Patient ID: " );
        Label typeLabel = new Label("Type: " );
        Label bodyLabel = new Label("Body: " );
        Label auxiliaryLabel = new Label("Auxiliary: " );
        Label createdAtLabel = new Label("Created At: " );
        Label modifiedAtLabel = new Label("Modified At: " );
       
	view.addColumn(0,titleLabel,docIdLabel,patientIdLabel,typeLabel,bodyLabel,auxiliaryLabel,createdAtLabel,modifiedAtLabel );
        

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            
        });
        view.add(editButton, 1, 7);

        // Adjust column constraints as needed

        Stage primaryStage = new Stage();
        Scene scene = new Scene(view, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Medical Document Details");
        primaryStage.show();
    }

		
		
	}

	@Override
	public void beforeShow() {
	Label titleLabel = new Label("Title: " + data.title);
        Label docIdLabel = new Label("Document ID: " + data.documentId);
        Label patientIdLabel = new Label("Patient ID: " + data.patientId);
        Label typeLabel = new Label("Type: " + data.type);
        Label bodyLabel = new Label("Body: " + data.body);
        Label auxiliaryLabel = new Label("Auxiliary: " + data.auxiliary);
        Label createdAtLabel = new Label("Created At: " + data.createTimestamp);
        Label modifiedAtLabel = new Label("Modified At: " + data.modifyTimestamp);
		
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
