package cs2043group10;

import cs2043group10.data.LoginClass;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginPrompt implements IReversable {
	private DatabaseManager databaseManager;
	private Runnable loginMethod;
	private static TextField idField;
	private static PasswordField passwordField;
	private static VBox loginNode;
	private static Text message;
	private static Button loginButton;

	public LoginPrompt(DatabaseManager databaseManager, Runnable loginMethod) {
		this.databaseManager = databaseManager;
		this.loginMethod = loginMethod;
		if (loginNode == null) {
			createNode();
		}
	}

	private static void createNode() {
		idField = new TextField();
		idField.setPromptText("Account ID");
		idField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					idField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		idField.setMaxWidth(300);
		passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setMaxWidth(300);
		loginButton = new Button("Login");
		message = new Text("");
		message.setFill(Color.RED);
		loginNode = new VBox(idField, passwordField, loginButton, message);
		loginNode.setAlignment(Pos.BASELINE_CENTER);
		loginNode.setSpacing(8);
		GridPane.setHalignment(loginNode, HPos.CENTER);
	}

	@Override
	public Node getNode() {
		return loginNode;
	}

	@Override
	public void onShow() {
		idField.setText("");
		passwordField.setText("");
		idField.setOnAction(this::loginEvent);
		passwordField.setOnAction(this::loginEvent);
		loginButton.setOnAction(this::loginEvent);
	}

	@Override
	public void onHide() {
	}

	@Override
	public String getTitle() {
		return "Login";
	}

	@Override
	public void destroy() {}

	private void loginEvent(ActionEvent event) {
		int id;
		try {
			id = Integer.parseInt(idField.getText());
		} catch (NumberFormatException e) {
			message.setText("ID must be a valid integer.");
			return;
		}

		LoginClass loginClass;
		try {
			loginClass = databaseManager.tryLogin(id, passwordField.getText());
		} catch (DatabaseException e) {
			message.setText("Database Exception: " + e.getMessage());
			return;
		}

		switch (loginClass) {
		case NOT_LOGGED_IN:
			message.setText("Incorrect username or password.");
			break;
		case PATIENT:
		case DOCTOR:
			loginMethod.run();
			break;
		}
	}
}
