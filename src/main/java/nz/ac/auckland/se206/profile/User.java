package nz.ac.auckland.se206.profile;

import java.util.List;

public class User {
	private String username;
	private String password;
	private List<String> usedWords;

	User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
}
