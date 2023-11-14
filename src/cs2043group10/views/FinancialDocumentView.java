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
		// TODO
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
