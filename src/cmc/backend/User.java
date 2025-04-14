package cmc.backend;

public class User implements Cloneable {
	public final String username;
	public String password;
	private boolean isAdmin;
	public String firstName;
	public String lastName;
	private boolean activated;

	/**
	 * Create a new user.
	 * @param username Username
	 * @param password Password
	 * @param isAdmin if user is admin
	 * @param firstName first name
	 * @param lastName last name
	 */
	public User(String username, String password, boolean isAdmin, String firstName,
			String lastName) {
		this.username = username;
		this.password = password;
		this.isAdmin = isAdmin;
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
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin if the user should be an admin
	 */
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
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
	public boolean isActivated() {
		return activated;
	}

	/**
	 * @param if activated to set
	 */
	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}
	
	@Override
	public String toString(){
		return ("Username: " +  username + ", Name: " + firstName + " " + lastName + (isAdmin ? "(Admin)": ""));
	}
	
	public Object clone() {
		User u = new User(username, password, isAdmin, firstName, lastName);
		u.setActivated(activated);
		return u;
	}
	
	/**
	 * Same as {@link #clone()} but casted to a User.
	 * @return A clone of the user object.
	 */
	public User uClone() {
		return (User)clone();
	}
	
}
