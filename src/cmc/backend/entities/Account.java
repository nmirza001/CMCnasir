
package cmc.backend.entities;

/**
 * Represents a Account entity
 * @author Alex
 * @version 3/25/2025
 */

public abstract class Account{
	protected String username;
	protected String password;
	protected String firstName;
	protected String lastName;
	protected boolean isActive;
	
	public Account (String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isActive = true;
	}
	/**
	 * @return the username for this account
	 */
	public String getUsername() {
		return this.username;
	}
	/**
	 * @param password is the password to check
	 * @return true if password is a match otherwise false
	 */
	public boolean authenticate (String password) {
		return this.password.equals(password);
		}
	/**
	 * @param newFirstName updated first name
	 * @param newLastName updates last name
	 */
	public void updateProfile (String newFirstName, String newLastName) {
		this.firstName = newFirstName;
		this.lastName = newLastName;
	}
	/**
	 * @return true if the account is active otherwise false
	 */
	public boolean isActive() {
		return this.isActive;
	}
	/**
	 * @param active is the new active status
	 */
	public void setActive (boolean active) {
		this.isActive = active;
	}
	/**
	 * @return true if user is admin, otherwise false
	 */
	public abstract boolean isAdmin();
	
}