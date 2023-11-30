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

/**
 * This class is the reversable node which prompts the user for their credentials.
 * 
 * @author James Petersen
 */
public class LoginPrompt implements IReversable {
	/**
	 * This is the manager which created this prompt.
	 */
	private final IReversableManager manager;
	/**
	 * This is the method to be called if a user successfully logs in.
	 */
	private final Runnable loginMethod;
	/**
	 * This is the field containing the user's id.
	 */
	private static TextField idField;
	/**
	 * This is the field containing the user's password.
	 */
	private static PasswordField passwordField;
	/**
	 * This is the node containing the prompt.
	 */
	private static VBox loginNode;
	/**
	 * This is the status text which is used to convey status information to the user
	 * if an error occurs.
	 */
	private static Text message;
	/**
	 * This is the button the user presses when they wish to log in.
	 */
	private static Button loginButton;

	/**
	 * Construct a new login prompt from a manager and method.
	 * @param manager The manager which handles this prompt.
	 * @param loginMethod The method called upon a successful log in.
	 */
	public LoginPrompt(IReversableManager manager, Runnable loginMethod) {
		this.manager = manager;
		this.loginMethod = loginMethod;
		if (loginNode == null) {
			createNode();
		}
	}

	/**
	 * This method constructs the node view.
	 */
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
	public void beforeShow() {
		idField.setText("");
		passwordField.setText("");
		idField.setOnAction(this::loginEvent);
		passwordField.setOnAction(this::loginEvent);
		loginButton.setOnAction(this::loginEvent);
	}

	@Override
	public void afterHide() {
	}

	@Override
	public String getTitle() {
		return "Login";
	}

	@Override
	public void destroy() {}

	/**
	 * This is the event handler which handles the login button being pressed.
	 * @param event The event.
	 */
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
			loginClass = manager.getDatabaseManager().tryLogin(id, passwordField.getText());
		} catch (DatabaseException e) {
			e.display();
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
	
	@Override
	public void refresh() {
		idField.setText("");
		message.setText("");
		passwordField.setText("");
		idField.requestFocus();
	}

	@Override
	public void afterShow() {
		idField.requestFocus();
	}

	@Override
	public void beforeHide() {}
}
