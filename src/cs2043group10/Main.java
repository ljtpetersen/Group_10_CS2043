package cs2043group10;

import java.util.Vector;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
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
	private DatabaseManager databaseManager;
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
		primaryPane.addRow(0, homeButton, backwardsButton, forwardsButton, currentNodeTitle, auxiliaryText, logoutButton);
		
		currentNodeTitle.setText("Login");
		loginPrompt = new LoginPrompt(databaseManager, this::login);
		primaryPane.add(loginPrompt.getNode(), 0, 1, 6, 1);
		
		Scene scene = new Scene(primaryPane, 500, 400);
		stage.setScene(scene);
		stage.show();
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
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.onHide();
		newNode.onShow();
		primaryPane.add(newNode.getNode(), 0, 1, 6, 1);
	}

	@Override
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
	
	private void login() {
		primaryPane.getChildren().remove(loginPrompt.getNode());
		loginPrompt.onHide();
		currentNodeIndex = 0;
		//IReversable homeView = new HomeView(this);
		homeButton.setDisable(false);
		logoutButton.setDisable(false);
		//currentNodeTitle.setText(homeView.getTitle());
		auxiliaryText.setText("Logged in as " + databaseManager.getName());
		//nodeStack.add(homeView);
		//homeView.onShow();
		//primaryPane.add(homeView.getNode(), 0, 6, 1, 1);
	}
	
	private void logoutEvent(ActionEvent event) {
		IReversable oldNode = nodeStack.get(currentNodeIndex);
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.onHide();
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
		loginPrompt.onShow();
		primaryPane.add(loginPrompt.getNode(), 0, 1, 6, 1);
	}
	
	private void homeEvent(ActionEvent event) {
		//pushNewNode(new HomeView(this));
	}
	
	private void historyEvent(ActionEvent event) {
		if (event.getSource() == backwardsButton) {
			IReversable oldNode = nodeStack.get(currentNodeIndex);
			primaryPane.getChildren().remove(oldNode.getNode());
			oldNode.onHide();
			IReversable newNode = nodeStack.get(--currentNodeIndex);
			newNode.onShow();
			primaryPane.add(newNode.getNode(), 0, 1, 6, 1);
			if (currentNodeIndex == 0) {
				backwardsButton.setDisable(true);
			}
			forwardsButton.setDisable(false);
			currentNodeTitle.setText(newNode.getTitle());
		} else {
			IReversable oldNode = nodeStack.get(currentNodeIndex);
			primaryPane.getChildren().remove(oldNode.getNode());
			oldNode.onHide();
			IReversable newNode = nodeStack.get(++currentNodeIndex);
			newNode.onShow();
			primaryPane.add(newNode.getNode(), 0, 1, 6, 1);
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
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.onHide();
		oldNode.destroy();
		nodeStack.setSize(currentNodeIndex--);
		IReversable newNode = nodeStack.get(currentNodeIndex);
		newNode.onShow();
		primaryPane.add(newNode.getNode(), 0, 1, 6, 1);
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
		primaryPane.getChildren().remove(oldNode.getNode());
		oldNode.onHide();
		oldNode.destroy();
		nodeStack.set(currentNodeIndex, newTop);
		newTop.onShow();
		primaryPane.add(newTop.getNode(), 0, 1, 6, 1);
		currentNodeTitle.setText(newTop.getTitle());
	}
	
	@Override
	public boolean isAtTop(IReversable node) {
		return nodeStack.lastElement() == node;
	}
}
