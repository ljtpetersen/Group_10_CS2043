package cs2043group10.views;

import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class FinancialCreateView implements IReversable {
	private int documentId;
	private final int patientId;
	private long amount;
	private String description;
	private final IReversableManager manager;
	private static GridPane view;
	private String title;

	public FinancialCreateView(IReversableManager manager, int patientId) {
		this.manager = manager;
		this.patientId = patientId;
		if (view == null) {
			createView();
		}
	}
	
	public FinancialCreateView(IReversableManager manager, FinancialDocument document) {
		documentId = document.documentId;
		patientId = document.patientId;
		amount = document.amount;
		description = document.description;
		title = document.title;
		this.manager = manager;
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
