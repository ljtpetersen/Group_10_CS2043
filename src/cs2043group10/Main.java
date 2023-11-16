package cs2043group10;

import java.util.Vector;

import cs2043group10.data.LoginClass;
import cs2043group10.test.TestDatabaseManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application implements IReversableManager {
	private Vector<IReversable> nodeStack;
	private int currentNodeIndex;
	private GridPane primaryPane;
	private Button backwardsButton;
	private Button forwardsButton;
	private Button homeButton;
	private Button logoutButton;
	private Text currentNodeTitle;
	private Text auxiliaryText;
	private IDatabase databaseManager;
	private IReversable loginPrompt;
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Hospital Management Interface");
		
		nodeStack = new Vector<IReversable>();
		currentNodeIndex = 0;
		primaryPane = new GridPane();
		backwardsButton = new Button("\u2190");
		backwardsButton.setOnAction(this::historyEvent);
		backwardsButton.setDisable(true);
		forwardsButton = new Button("\u2192");
		forwardsButton.setOnAction(this::historyEvent);
		forwardsButton.setDisable(true);
		homeButton = new Button("\u2302");
		homeButton.setOnAction(this::homeEvent);
		homeButton.setDisable(true);
		logoutButton = new Button("Logout");
		logoutButton.setOnAction(this::logoutEvent);
		logoutButton.setDisable(true);
		Button refreshButton = new Button("\u21bb");
		refreshButton.setOnAction(this::refreshEvent);
		currentNodeTitle = new Text("");
		auxiliaryText = new Text("");
		
		databaseManager = new DatabaseManager(this);
		
		primaryPane.setHgap(4);
		primaryPane.setVgap(8);
		GridPane.setHalignment(currentNodeTitle, HPos.CENTER);
		GridPane.setHalignment(auxiliaryText, HPos.RIGHT);
		GridPane.setHalignment(logoutButton, HPos.RIGHT);
		GridPane.setHgrow(currentNodeTitle, Priority.ALWAYS);
		GridPane.setValignment(currentNodeTitle, VPos.CENTER);
		GridPane.setMargin(homeButton, new Insets(4, 0, 0, 4));
		GridPane.setMargin(logoutButton, new Insets(4, 4, 0, 0));
		Insets standardInset = new Insets(4, 0, 0, 0);
		GridPane.setMargin(backwardsButton, standardInset);
		GridPane.setMargin(forwardsButton, standardInset);
		GridPane.setMargin(refreshButton, standardInset);
		GridPane.setMargin(currentNodeTitle, standardInset);
		GridPane.setMargin(auxiliaryText, standardInset);
		primaryPane.addRow(0, homeButton, backwardsButton, forwardsButton, refreshButton, currentNodeTitle, auxiliaryText, logoutButton);
		
		currentNodeTitle.setText("Login");
		loginPrompt = new LoginPrompt(this, this::login);
		loginPrompt.beforeShow();
		primaryPane.add(loginPrompt.getNode(), 0, 1, 7, 1);
		
		Scene scene = new Scene(primaryPane, 800, 400);
		stage.setScene(scene);
		stage.show();
		loginPrompt.afterShow();
	}

	@Override
	public void pushNewNode(IReversable newNode) {
		for (int i = ++currentNodeIndex; i < nodeStack.size(); ++i) {
			nodeStack.get(i).destroy();
		}
		nodeStack.setSize(currentNodeIndex + 1);
		nodeStack.set(currentNodeIndex, newNode);
		backwardsButton.setDisable(false);
		forwardsButton.setDisable(true);
		currentNodeTitle.setText(newNode.getTitle());
		IReversable oldNode = nodeStack.get(currentNodeIndex - 1);
		oldNode.beforeHide();
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.afterHide();
		newNode.beforeShow();
		primaryPane.add(newNode.getNode(), 0, 1, 7, 1);
		newNode.afterShow();
	}

	@Override
	public IDatabase getDatabaseManager() {
		return databaseManager;
	}
	
	private void login() {
		IReversable homeView;
		try {
			homeView = databaseManager.instantiateHomeView();
		} catch (DatabaseException e) {
			e.display();
			return;
		}
		loginPrompt.beforeHide();
		primaryPane.getChildren().remove(loginPrompt.getNode());
		loginPrompt.afterHide();
		currentNodeIndex = 0;
		homeButton.setDisable(false);
		logoutButton.setDisable(false);
		currentNodeTitle.setText(homeView.getTitle());
		auxiliaryText.setText("Logged in as " + databaseManager.getName());
		nodeStack.add(homeView);
		homeView.beforeShow();
		primaryPane.add(homeView.getNode(), 0, 1, 7, 1);
		homeView.afterShow();
	}
	
	private void logoutEvent(ActionEvent event) {
		IReversable oldNode = nodeStack.get(currentNodeIndex);
		oldNode.beforeHide();
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.afterHide();
		for (int i = nodeStack.size() - 1; i >= 0; --i) {
			nodeStack.get(i).destroy();
		}
		nodeStack.clear();
		homeButton.setDisable(true);
		logoutButton.setDisable(true);
		backwardsButton.setDisable(true);
		forwardsButton.setDisable(true);
		auxiliaryText.setText("");
		currentNodeTitle.setText("Login");
		databaseManager.logout();
		loginPrompt.beforeShow();
		primaryPane.add(loginPrompt.getNode(), 0, 1, 7, 1);
		loginPrompt.afterShow();
	}
	
	private void homeEvent(ActionEvent event) {
		try {
			pushNewNode(databaseManager.instantiateHomeView());
		} catch (DatabaseException e) {
			e.display();
		}
	}
	
	private void historyEvent(ActionEvent event) {
		if (event.getSource() == backwardsButton) {
			IReversable oldNode = nodeStack.get(currentNodeIndex);
			oldNode.beforeHide();
			primaryPane.getChildren().remove(oldNode.getNode());
			oldNode.afterHide();
			IReversable newNode = nodeStack.get(--currentNodeIndex);
			newNode.beforeShow();
			primaryPane.add(newNode.getNode(), 0, 1, 7, 1);
			newNode.afterShow();
			if (currentNodeIndex == 0) {
				backwardsButton.setDisable(true);
			}
			forwardsButton.setDisable(false);
			currentNodeTitle.setText(newNode.getTitle());
		} else {
			IReversable oldNode = nodeStack.get(currentNodeIndex);
			oldNode.beforeHide();
			primaryPane.getChildren().remove(oldNode.getNode());
			oldNode.afterHide();
			IReversable newNode = nodeStack.get(++currentNodeIndex);
			newNode.beforeShow();
			primaryPane.add(newNode.getNode(), 0, 1, 7, 1);
			newNode.afterShow();
			if (currentNodeIndex == nodeStack.size() - 1) {
				forwardsButton.setDisable(true);
			}
			backwardsButton.setDisable(false);
			currentNodeTitle.setText(newNode.getTitle());
		}
	}
	
	@Override
	public void goBackwards() {
		if (currentNodeIndex > 0) {
			backwardsButton.fire();
		}
	}
	
	@Override
	public void goForwards() {
		if (currentNodeIndex < nodeStack.size() - 1) {
			forwardsButton.fire();
		}
	}
	
	@Override
	public void popNode() {
		if (currentNodeIndex == 0 || currentNodeIndex + 1 != nodeStack.size()) {
			throw new RuntimeException();
		}
		IReversable oldNode = nodeStack.get(currentNodeIndex);
		oldNode.beforeHide();
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.afterHide();
		oldNode.destroy();
		nodeStack.setSize(currentNodeIndex--);
		IReversable newNode = nodeStack.get(currentNodeIndex);
		newNode.beforeShow();
		primaryPane.add(newNode.getNode(), 0, 1, 7, 1);
		newNode.afterShow();
		currentNodeTitle.setText(newNode.getTitle());
		if (currentNodeIndex == 0) {
			backwardsButton.setDisable(true);
		}
	}
	
	@Override
	public void replaceTop(IReversable newTop) {
		if (currentNodeIndex == 0 || currentNodeIndex + 1 != nodeStack.size()) {
			throw new RuntimeException();
		}
		IReversable oldNode = nodeStack.get(currentNodeIndex);
		oldNode.beforeHide();
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.afterHide();
		oldNode.destroy();
		nodeStack.set(currentNodeIndex, newTop);
		newTop.beforeShow();
		primaryPane.add(newTop.getNode(), 0, 1, 7, 1);
		newTop.afterShow();
		currentNodeTitle.setText(newTop.getTitle());
	}
	
	@Override
	public boolean isAtTop(IReversable node) {
		return nodeStack.lastElement() == node;
	}
	
	private void refreshEvent(ActionEvent event) {
		if (databaseManager.getLoginClass() == LoginClass.NOT_LOGGED_IN) {
			loginPrompt.refresh();
		} else {
			nodeStack.get(currentNodeIndex).refresh();
		}
	}
}
