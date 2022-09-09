package nz.ac.auckland.se206.profile;

public class User {
	private String username;
	private String password;

	User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
}
