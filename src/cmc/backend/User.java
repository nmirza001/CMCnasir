package cmc.backend;

public class User {
	public final String username;
	public String password;
	public char type; // u or a
	public String firstName;
	public String lastName;
	private boolean activated;

	/**
	 * Create a new user.
	 * @param username Username
	 * @param password Password
	 * @param type 'u' for user or 'a' for admin
	 * @param firstName first name
	 * @param lastName last name
	 */
	public User(String username, String password, char type, String firstName,
			String lastName) {
		this.username = username;
		this.password = password;
		this.type = type;
		this.firstName = firstName;
		this.lastName = lastName;
		this.activated = true; // users always start activated
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		// NOTE: we cannot modify the username, since this is the core
		// "id" for users in the database (and must be unique)
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(char type) {
		this.type = type;
	}
	
	// return true if the user is an admin ('a') type,
	// or false otherwise
	public boolean isAdmin() {
		return (type == 'a');
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return if activated
	 */
	public boolean getActivated() {
		return activated;
	}

	/**
	 * @param if activated to set
	 */
	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}

}
