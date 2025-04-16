package cmc.frontend;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import cmc.backend.AccountController;
import cmc.backend.UniversityController;
import cmc.backend.entities.University;
import cmc.backend.SystemController;
import cmc.CMCException;
import cmc.backend.User;

/**
 * AdminInteraction class extends UserInteraction to provide admin-specific
 * functionality for managing users and universities in the CMC system.
 * 
 * @author Nasir Mirza
 * @version April 15, 2025
 */
public class AdminInteraction extends UserInteraction {
    private final UniversityController theUniversityController;
    
    /**
     * Constructs an AdminInteraction with the specified controllers.
     * 
     * @param theSystemController The system controller for handling operations
     * @param acct The account controller for managing user accounts
     * @param theUniversityController The university controller for managing universities
     */
    public AdminInteraction(SystemController theSystemController, AccountController acct, UniversityController theUniversityController) {
        super(); // calls the super class which is UserInteraction / ui
        this.theUniversityController = theUniversityController;
        this.theSystemController = theSystemController;
        this.acct = acct;
    }
    
    //(0) - display methods
    
    /**
     * Displays a list of users in a tabular format with detailed information.
     * 
     * @param users The list of users to display
     */
    public void displayUserTable(List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No users to display.");
            return;
        }
        
        // Find maximum lengths for formatting
        int maxUsernameLength = "Username".length();
        int maxFirstNameLength = "First Name".length();
        int maxLastNameLength = "Last Name".length();
        int maxTypeLength = "Type".length();
        int maxStatusLength = "Status".length();
        
        for (User user : users) {
            maxUsernameLength = Math.max(maxUsernameLength, user.getUsername().length());
            maxFirstNameLength = Math.max(maxFirstNameLength, user.getFirstName().length());
            maxLastNameLength = Math.max(maxLastNameLength, user.getLastName().length());
            maxTypeLength = Math.max(maxTypeLength, user.isAdmin() ? "Admin".length() : "User".length());
            maxStatusLength = Math.max(maxStatusLength, user.isActivated() ? "Active".length() : "Inactive".length());
        }
        
        // Add padding
        maxUsernameLength += 2;
        maxFirstNameLength += 2;
        maxLastNameLength += 2;
        maxTypeLength += 2;
        maxStatusLength += 2;
        
        // Format string
        String format = "%-3s | %-" + maxUsernameLength + "s | %-" + maxFirstNameLength + 
                       "s | %-" + maxLastNameLength + "s | %-" + maxTypeLength + 
                       "s | %-" + maxStatusLength + "s\n";
        
        // Print header
        System.out.printf(format, "#", "Username", "First Name", "Last Name", "Type", "Status");
        
        // Print separator
        int totalLength = 4 + maxUsernameLength + 3 + maxFirstNameLength + 3 + 
                         maxLastNameLength + 3 + maxTypeLength + 3 + maxStatusLength;
        for (int i = 0; i < totalLength; i++) {
            System.out.print("-");
        }
        System.out.println();
        
        // Print data
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.printf(format, (i + 1), user.getUsername(), user.getFirstName(), 
                             user.getLastName(), (user.isAdmin() ? "Admin" : "User"), 
                             (user.isActivated() ? "Active" : "Inactive"));
        }
        System.out.println();
    }
    
    /**
     * Displays a simplified list of users with just their numbers and usernames.
     * 
     * @param users The list of users to display
     */
    public void displaySimpleUserList(List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No users to display.");
            return;
        }
        
        // Find maximum username length
        int maxUsernameLength = "Username".length();
        for (User user : users) {
            maxUsernameLength = Math.max(maxUsernameLength, user.getUsername().length());
        }
        
        // Add padding
        maxUsernameLength += 2;
        
        // Format string
        String format = "%-3s | %-" + maxUsernameLength + "s\n";
        
        // Print header
        System.out.printf(format, "#", "Username");
        
        // Print separator
        int totalLength = 4 + maxUsernameLength;
        for (int i = 0; i < totalLength; i++) {
            System.out.print("-");
        }
        System.out.println();
        
        // Print data
        for (int i = 0; i < users.size(); i++) {
            System.out.printf(format, (i + 1), users.get(i).getUsername());
        }
        System.out.println();
    }
    
    //(1) - Adder & Subtracter methods
    
    /**
     * Adds a new university to the system.
     * 
     * @param uni The university object to add
     * @return true if added successfully, false otherwise
     * @throws IllegalArgumentException if uni is null
     */
    public boolean addNewUniversity(University uni) {
        if (uni == null) throw new IllegalArgumentException("University cannot be null");
        return theSystemController.addNewUniversity(uni);
    }
    
    /**
     * Removes a university from the system.
     * 
     * @param uni The university to remove
     * @return true if removed successfully, false otherwise
     * @throws IllegalArgumentException if uni is null
     */
    public boolean removeUniversity(University uni) {
        if (uni == null) throw new IllegalArgumentException("University cannot be null");
        return theSystemController.removeUniversity(uni);
    }
    
    /**
     * Prompts the admin for details and attempts to add a user to the database.
     * 
     * @param s Scanner for input
     * @return true if user added successfully, false otherwise
     */
    public boolean addUser(Scanner s) {
        System.out.print("Username: ");
        String username = s.nextLine();
        System.out.print("Password: ");
        String password = s.nextLine();
        System.out.print("First Name: ");
        String firstName = s.nextLine();
        System.out.print("Last Name: ");
        String lastName = s.nextLine();
        System.out.print("Admin? (Y or N): ");
        boolean isAdmin = false;
        if (s.nextLine().trim().equalsIgnoreCase("y"))
            isAdmin = true;
        
        User u = new User(username, password, isAdmin, firstName, lastName);
        return this.theSystemController.addUser(u);
    }
    
    /**
     * Prompts the admin to select a user to remove from the database.
     * 
     * @param s Scanner for input
     * @return true if user removed successfully, false otherwise
     */
    public boolean removeUser(Scanner s) {
        List<User> allUsers = getAllUsers();
        
        if (allUsers.isEmpty()) {
            System.out.println("No users in the system to remove.");
            return false;
        }
        
        // Display users in a table format
        displaySimpleUserList(allUsers);
  
        System.out.print("Enter user number to remove: ");
  
        int usernum;
        try {
            usernum = s.nextInt();
        }
        catch(java.util.InputMismatchException e) {
            return false;
        }
        finally {
            s.nextLine();
        }
  
        // List starts at 1 so zero is not a valid option
        if(usernum <= 0 || usernum > allUsers.size()) {
            System.out.println("Invalid user number. Please select a number between 1 and " + allUsers.size());
            return false;
        }
        
        // Shift everything by 1 since list starts at 1
        User u = allUsers.get(usernum - 1);
        
        boolean success = this.theSystemController.removeUser(u);
        if (success) {
            System.out.println("User '" + u.getUsername() + "' successfully removed.");
        } else {
            System.out.println("Failed to remove user '" + u.getUsername() + "'.");
        }
        return success;
    }
    
    /**
     * Prompts the admin to select a user to deactivate.
     * 
     * @param s Scanner for input
     * @return true if user deactivated successfully, false otherwise
     */
    public boolean deactivateUser(Scanner s) {
        List<User> allUsers = getAllUsers();
        
        if (allUsers.isEmpty()) {
            System.out.println("No users in the system to deactivate.");
            return false;
        }
        
        // Display users in a table format with activation status
        displayUserTable(allUsers);
  
        System.out.print("Enter user number to deactivate: ");
  
        int usernum;
        try {
            usernum = s.nextInt();
        }
        catch(java.util.InputMismatchException e) {
            return false;
        }
        finally {
            s.nextLine();
        }
  
        // List starts at 1 so zero is not a valid option
        if(usernum <= 0 || usernum > allUsers.size()) {
            System.out.println("Invalid user number. Please select a number between 1 and " + allUsers.size());
            return false;
        }
        
        // Shift everything by 1 since list starts at 1
        User u = allUsers.get(usernum - 1);
        
        boolean success = this.theSystemController.deactivateUser(u);
        if (success) {
            System.out.println("User '" + u.getUsername() + "' successfully deactivated.");
        } else {
            System.out.println("Failed to deactivate user '" + u.getUsername() + "'.");
        }
        return success;
    }
    
    /**
     * Searches for users by username and displays matching results.
     * 
     * @param s Scanner for input
     * @return true if any matching users were found, false otherwise
     * @author tflynn001
     * @version April 14, 2025
     */
    public boolean searchUsers(Scanner s) {
        List<User> uList = getAllUsers();
        
        if (uList.isEmpty()) {
            System.out.println("No users in the system to search.");
            return false;
        }
        
        System.out.print("Username to search for: ");
        
        String searchName = s.nextLine();
        
        if (searchName.trim().isEmpty()) {
            System.out.println("Displaying all users:");
            displayUserTable(uList);
            return true;
        }

        // Find users matching the search term
        List<User> matchingUsers = new ArrayList<>();
        for (User user : uList) {
            if (user.getUsername().toLowerCase().contains(searchName.toLowerCase())) {
                matchingUsers.add(user);
            }
        }
        
        if (matchingUsers.isEmpty()) {
            System.out.println("No users found matching '" + searchName + "'.");
            return false;
        } else {
            System.out.println("Found " + matchingUsers.size() + " user(s) matching '" + searchName + "':");
            displayUserTable(matchingUsers);
            return true;
        }
    }
    
    //(2) - Admin Viewer Methods
    
    /**
     * Gets the list of all users in the system.
     * 
     * @return A list of all User objects in the system
     */
    
    public List<User> getAllUsers() {
        return acct.getAllUsers();
    }
    
    //(3) - Admin Editor Methods
    
    /**
     * Edits the details of a university based on user input.
     * 
     * @param s Scanner for input
     * @param editUni The university to edit
     * @return true if university edited successfully, false otherwise
     * @throws CMCException if there is an error editing the university
     */
    public boolean editUniversityDetails(Scanner s, University editUni) throws CMCException {
        University editedUniversity = AdminEditSchool.prompt(s, editUni);
        
        if (editedUniversity != null) {
            return theSystemController.editUniversity(editedUniversity);
        }
        
        return false; // if null
    }
}