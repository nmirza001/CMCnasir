package cmc.backend.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Admin entity
 * @author Nasir Mirza
 * @version 4/15/2025
 */
public class Admin extends Account {
    
    /**
     * Creates a new Admin account
     * @param username Username for the admin
     * @param password Password for the admin
     * @param firstName Admin's first name
     * @param lastName Admin's last name
     */
    public Admin(String username, String password, String firstName, String lastName) {
        super(username, password, firstName, lastName);
    }
    
    /**
     * Indicates that this account has admin privileges
     * @return always returns true for Admin
     */
    @Override
    public boolean isAdmin() {
        return true;
    }
    
    /**
     * Creates a new user in the system
     * @param user the user to create
     * @return true if user is created otherwise false
     */
    public boolean createUser(User user) {
        // Implementation would connect to database controller
        // For now returning false as placeholder
        return false;
    }
    
    /**
     * Deletes a user from the system
     * @param username the username of account to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(String username) {
        // Implementation would connect to database controller
        // For now returning false as placeholder
        return false;
    }
    
    /**
     * Retrieves a list of all users in the system
     * @return a list of User objects
     */
    public List<User> listAllUsers() {
        // Implementation would connect to database controller
        // For now returning empty list as placeholder
        return new ArrayList<User>();
    }
    
    /**
     * Manages a university's information
     * @param universityName name of university to manage
     * @return true if successful, false otherwise
     */
    public boolean manageUniversity(String universityName) {
        // Implementation would connect to database controller
        // For now returning false as placeholder
        return false;
    }
    
    /**
     * Deactivates a user account
     * @param username the user to deactivate
     * @return true if successful, false otherwise
     */
    public boolean deactivateUser(String username) {
        // Implementation would connect to database controller
        // For now returning false as placeholder
        return false;
    }
}