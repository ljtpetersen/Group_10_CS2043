
package cs2043group10;

import cs2043group10.data.LoginClass;

public class DatabaseManager {
	private int loggedInId;
	private String loggedInName;
	private LoginClass loginClass;
	private IReversableManager manager;
	
	public DatabaseManager(IReversableManager manager) {
		this.manager = manager;
		loggedInId = -1;
		loginClass = LoginClass.NOT_LOGGED_IN;
		loggedInName = null;
	}
	
	public LoginClass tryLogin(int id, String password) throws DatabaseException {
		// Try to login using id and password.
		throw new RuntimeException("Not yet implemented.");
	}
	
	public LoginClass getLoginClass() {
		return loginClass;
	}
	
	public int getId() {
		return loggedInId;
	}
	
	public String getName() {
		return loggedInName;
	}
	
	public void logout() {
		loggedInId = -1;
		loggedInName = null;
		loginClass = LoginClass.NOT_LOGGED_IN;
	}
}