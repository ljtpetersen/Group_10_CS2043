package cs2043group10;

import javafx.scene.control.Alert;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = 6426419060331664662L;

	public void display() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(this.getMessage());
		alert.show();
	}
}
