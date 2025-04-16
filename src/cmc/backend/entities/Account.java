package cmc.backend.entities;

/**
 * Represents a Account entity
 * @author Nasir Mirza
 * @version 4/15/2025
 */
public abstract class Account {
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected boolean isActive;
    
    /**
     * Creates a new Account with the specified properties
     * @param username Username for the account
     * @param password Password for the account
     * @param firstName User's first name
     * @param lastName User's last name
     */
    public Account(String username, String password, String firstName, String lastName) {
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
     * @return the first name of the account holder
     */
    public String getFirstName() {
        return this.firstName;
    }
    
    /**
     * @return the last name of the account holder
     */
    public String getLastName() {
        return this.lastName;
    }
    
    /**
     * @return the password for this account
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Sets a new password
     * @param newPassword the new password to set
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    
    /**
     * Verifies if the provided password matches the account password
     * @param password is the password to check
     * @return true if password is a match otherwise false
     */
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    
    /**
     * Updates the account profile information
     * @param newFirstName updated first name
     * @param newLastName updates last name
     */
    public void updateProfile(String newFirstName, String newLastName) {
        this.firstName = newFirstName;
        this.lastName = newLastName;
    }
    
    /**
     * Checks if the account is active
     * @return true if the account is active otherwise false
     */
    public boolean isActive() {
        return this.isActive;
    }
    
    /**
     * Sets the active status of the account
     * @param active is the new active status
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Determines if the account has admin privileges
     * @return true if user is admin, otherwise false
     */
    public abstract boolean isAdmin();
    
    @Override
    public String toString() {
        return "Username: " + username + ", Name: " + firstName + " " + lastName + 
               (isAdmin() ? " (Admin)" : "");
    }
}