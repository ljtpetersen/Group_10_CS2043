package cs2043group10;

import java.sql.SQLException;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = 6426419060331664662L;
	
	public DatabaseException(SQLException e) {
		super(e);
	}
	
	public DatabaseException(String s) {
		super(s);
	}
	
	public void display() {
		
	}
}
