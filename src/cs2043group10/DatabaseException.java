package cs2043group10;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = 6426419060331664662L;
	
	public DatabaseException(SQLException e) {
		super(e);
	}
	
	public DatabaseException(NoSuchAlgorithmException e) {
		super(e);
	}
	
	public DatabaseException(String s) {
		super(s);
	}
	
	public void display() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(this.getMessage());
		alert.show();
	}
}
