package cmc.frontend;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import cmc.*; // For CMCException

// Use the specific User class that UserInteraction holds
import cmc.backend.User;
import cmc.backend.UniversityController;
import cmc.backend.AccountController;

// Use the entity classes for data representation
import cmc.backend.entities.University;

/**
 * Driver class that controls the flow of the CMC application.
 * Provides menu-driven interfaces for regular users and admin users.
 * 
 * @author Nasir Mirza
 * @version April 15, 2025
 */
public class Driver {

    // Initialize UserInteraction which handles controller access
    private static UserInteraction ui = new UserInteraction();
    
    // AdminInteraction instance to handle admin-specific operations
    private static AdminInteraction adminUi = null;

    // Private constructor to prevent instantiation
    private Driver() throws CMCException {
        throw new CMCException("Attempt to instantiate a Driver");
    }

    /**
     * Prints a formatted header for menus.
     * 
     * @param title The title to display in the header
     */
    private static void printHeader(String title) {
        String dashes = "";
        for (int i = 0; i < title.length(); i++)
            dashes += "-";

        System.out.println("\n" + dashes);
        System.out.println(title);
        System.out.println(dashes);
    }

    /**
     * Displays search results and provides options to interact with them.
     * 
     * @param s Scanner for input
     * @param results List of Universities from the search
     * @param currentUi The UserInteraction object for actions like saving schools
     */
    private static void searchResultsMenu(Scanner s, List<University> results, UserInteraction currentUi) {
        printHeader("Search Results");

        if (results == null || results.isEmpty()) {
            System.out.println("No schools found matching your criteria.");
            System.out.println("Press Enter to continue...");
            if (s.hasNextLine()) s.nextLine(); 
            return;
        }

        // Find maximum lengths for formatting
        int maxNameLength = "Name".length();
        int maxStateLength = "State".length();
        int maxLocationLength = "Location".length();
        int maxControlLength = "Control".length();
        
        for (University school : results) {
            maxNameLength = Math.max(maxNameLength, school.getName().length());
            maxStateLength = Math.max(maxStateLength, 
                (school.getState() != null && !school.getState().equals("-1")) ? 
                school.getState().length() : "N/A".length());
            maxLocationLength = Math.max(maxLocationLength, 
                (school.getLocation() != null && !school.getLocation().equals("-1")) ? 
                school.getLocation().length() : "N/A".length());
            maxControlLength = Math.max(maxControlLength, 
                (school.getControl() != null && !school.getControl().equals("-1")) ? 
                school.getControl().length() : "N/A".length());
        }
        
        // Add some padding
        maxNameLength += 2;
        maxStateLength += 2;
        maxLocationLength += 2;
        maxControlLength += 2;
        
        // Create format string
        String format = "%-3s | %-" + maxNameLength + "s | %-" + maxStateLength + 
                       "s | %-" + maxLocationLength + "s | %-" + maxControlLength + "s\n";
        
        // Print header
        System.out.printf(format, "#", "Name", "State", "Location", "Control");
        
        // Print separator line
        int totalLength = 4 + maxNameLength + 3 + maxStateLength + 3 + 
                         maxLocationLength + 3 + maxControlLength;
        for (int i = 0; i < totalLength; i++) {
            System.out.print("-");
        }
        System.out.println();
        
        // Print data rows
        for (int i = 0; i < results.size(); i++) {
            University school = results.get(i);
            String state = (school.getState() != null && !school.getState().equals("-1")) ? 
                         school.getState() : "N/A";
            String location = (school.getLocation() != null && !school.getLocation().equals("-1")) ? 
                            school.getLocation() : "N/A";
            String control = (school.getControl() != null && !school.getControl().equals("-1")) ? 
                           school.getControl() : "N/A";
            
            System.out.printf(format, (i + 1), school.getName(), state, location, control);
        }
        System.out.println();

        int choice = ConsoleUtils.getMenuOption(s, Arrays.asList("View Details", "Save School", "Go Back"));

        switch(choice) {
        case 1: // View Details
             System.out.print("Enter the number of the school to view details: ");
             int viewChoice = ConsoleUtils.getSingleMenuEntry(s, 1, results.size());
             if (viewChoice != -1) {
                 University selected = results.get(viewChoice - 1);
                 displaySchoolDetailsAndSimilar(s, selected, currentUi);
             } else {
                 System.out.println("Invalid selection.");
             }
             break;
        case 2: // Save School
            if (!currentUi.saveSchool(s))
                System.out.println("Failed to save school. (Already saved or school name incorrect?)");
            else
                System.out.println("School saved successfully (if the name was correct and not already saved).");
            break;
        case 3: // Go Back
            return;
        default:
            System.err.println("Internal error: Unsupported option in searchResultsMenu.");
            break;
        }
    }

    /**
     * Displays the user's saved school list and provides interaction options.
     * 
     * @param s Scanner for input
     * @param currentUi The UserInteraction object to get saved schools
     */
    private static void userSavedSchoolListMenu(Scanner s, UserInteraction currentUi) {
        printHeader("User Saved School List");

        List<String> schools = currentUi.getSavedSchools();

        if (schools == null || schools.isEmpty()) {
            System.out.println("You have no saved schools.");
        } else {
            // Find the maximum length of school names for formatting
            int maxNameLength = "School Name".length();
            for (String school : schools) {
                maxNameLength = Math.max(maxNameLength, school.length());
            }
            
            // Add padding and create format string
            maxNameLength += 2;
            String format = "%-3s | %-" + maxNameLength + "s\n";
            
            // Print header
            System.out.printf(format, "#", "School Name");
            
            // Print separator
            int totalLength = 4 + maxNameLength;
            for (int i = 0; i < totalLength; i++) {
                System.out.print("-");
            }
            System.out.println();
            
            // Print data rows
            for (int i = 0; i < schools.size(); i++) {
                System.out.printf(format, (i + 1), schools.get(i));
            }
            System.out.println();
        }

        // Provide options relevant to the saved list
        int choice = ConsoleUtils.getMenuOption(s, Arrays.asList("View Details", "Remove School (Not Implemented)", "Go Back"));

        switch(choice) {
        case 1: // View Details
             if (schools == null || schools.isEmpty()) {
                 System.out.println("No saved schools to view details for.");
                 break;
             }
             System.out.print("Enter the number of the saved school to view details: ");
             int viewChoice = ConsoleUtils.getSingleMenuEntry(s, 1, schools.size());
             if (viewChoice != -1) {
                 String schoolName = schools.get(viewChoice - 1);
                 University selected = currentUi.getUniversityObjectByName(schoolName);
                 if (selected != null) {
                     displaySchoolDetailsAndSimilar(s, selected, currentUi);
                 } else {
                     System.out.println("Could not retrieve details for saved school: " + schoolName);
                     System.out.println("(It might have been removed from the system).");
                 }
             } else {
                 System.out.println("Invalid selection.");
             }
             break;
        case 2: // Remove School
            System.out.println("Remove saved school functionality is not yet implemented.");
            System.out.println("Press Enter to continue...");
            if (s.hasNextLine()) s.nextLine();
            break;
        case 3: // Go Back
            return;
        default:
            System.err.println("Internal error: Unsupported option in userSavedSchoolListMenu.");
            break;
        }
    }

    /**
     * Main menu for regular (non-admin) users.
     * 
     * @param s Scanner for input
     */
    private static void regularUserMenu(Scanner s) {
        printHeader("User Menu");

        int choice = ConsoleUtils.getMenuOption(s, Arrays.asList("Search Universities", "View Saved Schools", "View Specific School", "Logout"));

        switch(choice) {
        case 1: // Search
            List<University> searchResult = ui.search(s);
            searchResultsMenu(s, searchResult, ui);
            break;
        case 2: // View Saved
            userSavedSchoolListMenu(s, ui);
            break;
        case 3: // View Specific School
            System.out.print("Enter the EXACT school name to view (usually ALL CAPS): ");
            String schoolNameInput = s.nextLine();
            if (schoolNameInput.trim().isEmpty()) {
                System.out.println("No school name entered.");
                break;
            }
            // Standardize to uppercase for lookup
            String schoolName = schoolNameInput.toUpperCase();
            University viewedUniversity = ui.getUniversityObjectByName(schoolName);

            if (viewedUniversity == null) {
                System.out.println("\nCould not find details for university: " + schoolName);
                System.out.println("Press Enter to continue...");
                if (s.hasNextLine()) s.nextLine();
            } else {
                displaySchoolDetailsAndSimilar(s, viewedUniversity, ui);
            }
            break;
        case 4: // Logout
            ui.logout();
            break;
        default:
            System.err.println("Internal error: Unsupported option in regularUserMenu.");
            break;
        }
    }

    /**
     * Helper function to display school details and then prompt for similar schools.
     * 
     * @param s Scanner for input
     * @param school The University object to display. Must not be null.
     * @param currentUi The UserInteraction object for backend calls.
     */
    private static void displaySchoolDetailsAndSimilar(Scanner s, University school, UserInteraction currentUi) {
        String schoolName = school.getName();
        printHeader(schoolName + " Details");

        String schoolInfo = currentUi.getUniversityDetails(schoolName);
        System.out.println(schoolInfo);
        System.out.println();

        // --- Similar Schools Prompt ---
        System.out.print("Find schools similar to " + schoolName + "? (Y/N): ");
        String findSimilarChoice = s.nextLine();
        if (findSimilarChoice.trim().equalsIgnoreCase("Y")) {
            List<University> similarSchools = currentUi.findSimilarUniversities(school);

            if (similarSchools == null || similarSchools.isEmpty()) {
                System.out.println("No similar schools found based on the defined criteria.");
            } else {
                printHeader("Similar Schools Found:");
                int displayLimit = Math.min(similarSchools.size(), 10);
                
                // Find maximum lengths for formatting
                int maxNameLength = "Name".length();
                int maxStateLength = "State".length();
                int maxLocationLength = "Location".length();
                int maxStudentsLength = "Students".length();
                int maxAcceptanceLength = "Acceptance".length();
                
                for (int i = 0; i < displayLimit; i++) {
                    University simUni = similarSchools.get(i);
                    String numStudentsStr = simUni.getNumStudents() > 0 ? String.valueOf(simUni.getNumStudents()) : "N/A";
                    String acceptanceRateStr = simUni.getPercentAdmitted() >= 0 ? String.format("%.1f%%", simUni.getPercentAdmitted()) : "N/A";
                    String locationStr = (simUni.getLocation() != null && !simUni.getLocation().equals("-1")) ? simUni.getLocation() : "N/A";
                    
                    maxNameLength = Math.max(maxNameLength, simUni.getName().length());
                    maxStateLength = Math.max(maxStateLength, 
                        (simUni.getState() != null) ? simUni.getState().length() : "N/A".length());
                    maxLocationLength = Math.max(maxLocationLength, locationStr.length());
                    maxStudentsLength = Math.max(maxStudentsLength, numStudentsStr.length());
                    maxAcceptanceLength = Math.max(maxAcceptanceLength, acceptanceRateStr.length());
                }
                
                // Add padding
                maxNameLength += 2;
                maxStateLength += 2;
                maxLocationLength += 2;
                maxStudentsLength += 2;
                maxAcceptanceLength += 2;
                
                // Create format string
                String format = "%-3s | %-" + maxNameLength + "s | %-" + maxStateLength + 
                               "s | %-" + maxLocationLength + "s | %-" + maxStudentsLength + 
                               "s | %-" + maxAcceptanceLength + "s\n";
                
                // Print header
                System.out.printf(format, "#", "Name", "State", "Location", "Students", "Acceptance");
                
                // Print separator
                int totalLength = 4 + maxNameLength + 3 + maxStateLength + 3 + maxLocationLength + 
                                 3 + maxStudentsLength + 3 + maxAcceptanceLength;
                for (int i = 0; i < totalLength; i++) {
                    System.out.print("-");
                }
                System.out.println();
                
                // Print data rows
                for (int i = 0; i < displayLimit; i++) {
                    University simUni = similarSchools.get(i);
                    String numStudentsStr = simUni.getNumStudents() > 0 ? String.valueOf(simUni.getNumStudents()) : "N/A";
                    String acceptanceRateStr = simUni.getPercentAdmitted() >= 0 ? String.format("%.1f%%", simUni.getPercentAdmitted()) : "N/A";
                    String locationStr = (simUni.getLocation() != null && !simUni.getLocation().equals("-1")) ? simUni.getLocation() : "N/A";
                    String stateStr = (simUni.getState() != null) ? simUni.getState() : "N/A";
                    
                    System.out.printf(format, (i + 1), simUni.getName(), stateStr, locationStr, numStudentsStr, acceptanceRateStr);
                }
                
                if (similarSchools.size() > displayLimit) {
                    System.out.println("... and " + (similarSchools.size() - displayLimit) + " more.");
                }
                System.out.println();
            }
        }
        // --- End Similar Schools ---

        System.out.println("Press Enter to continue...");
        if (s.hasNextLine()) s.nextLine();
    }

    /**
     * Main menu for admin users. Checks for admin privileges before proceeding.
     * Creates an AdminInteraction instance to handle admin-specific operations.
     * 
     * @param s Scanner for input
     */
    private static void adminMenu(Scanner s) {
        // Get the logged-in user ONCE
        User currentUser = ui.getLoggedInUser();

        // Safety check: Ensure a user is logged in
        if (currentUser == null) {
            System.out.println("Error: No user logged in for admin menu. Returning to login.");
            return; // Let main loop handle login
        }

        // Correctly check for admin rights using the isAdmin() method
        if (!currentUser.isAdmin()) {
             System.out.println("Access denied. Admin privileges required.");
             ui.logout(); // Log out the non-admin user
             return; // Let main loop handle login
        }

        // Create AdminInteraction if it's not already created
        if (adminUi == null) {
            // Initialize the AdminInteraction with the required controllers
            adminUi = new AdminInteraction(
                ui.theSystemController,
                ui.acct,
                new UniversityController()
            );
        }

        // --- User is confirmed Admin ---
        printHeader("Admin Menu");

        int choice = ConsoleUtils.getMenuOption(s, Arrays.asList("Manage Users",
                "Manage Universities",
                "Logout"));

        switch(choice) {
        case 1: // Manage Users
            // Pass the AdminInteraction object for admin-specific functions
            adminUserManagementMenu(s, adminUi);
            break;
        case 2: // Manage Universities
            // Pass the AdminInteraction object for admin-specific functions
            adminUniversityManagementMenu(s, adminUi);
            break;
        case 3: // Logout
            ui.logout();
            adminUi = null; // Clear the AdminInteraction on logout
            break;
        default:
            System.err.println("Internal error: Unsupported option in adminMenu.");
            break;
        }
    }

    /**
     * Handles the initial login prompt.
     * 
     * @param s Scanner for input
     * @return true if login was successful, false otherwise (including user entering 'quit')
     */
    private static boolean topMenu(Scanner s) {
        printHeader("Welcome to Choose My College (CMC)!");
        System.out.println("Please log in, or enter 'quit' as username to exit.");

        String username = "";
        // Loop until non-empty username or 'quit' is entered
        while (username.trim().isEmpty()) {
            System.out.print("Username: ");
            username = s.nextLine();
            if (username.equalsIgnoreCase("quit")) return false; // Signal to exit
        }

        System.out.print("Password: ");
        String password = s.nextLine();

        boolean success = ui.login(username, password);
        if (success) {
            System.out.println("Redirecting to main menu...");
            return true;
        } else {
            // Login failure message handled inside ui.login()
            System.out.println("Please try again.");
            return false; // Login failed
        }
    }

    /**
     * Main application loop.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            User currentUser = ui.getLoggedInUser();

            if (currentUser == null) {
                boolean loggedIn = topMenu(s);
                if (!loggedIn && ui.getLoggedInUser() == null) {
                    // This implies user typed 'quit' or login failed repeatedly
                    System.out.println("Exiting requested or login failed.");
                    keepRunning = false; // Exit the loop
                }
            } else if (currentUser.isAdmin()) {
                adminMenu(s);
            } else {
                regularUserMenu(s);
            }

            // Check if user logged out during menu action
            if (ui.getLoggedInUser() == null && currentUser != null) {
                System.out.println("You have been logged out.");
                // Loop continues, will prompt for login again via topMenu
            }
        }

        System.out.println("\nExiting Choose My College. Goodbye!");
        s.close(); // Close scanner resource
    }

    /**
     * Menu for Admin User Management. Uses AdminInteraction for admin-specific operations.
     * 
     * @param s Scanner for input
     * @param baseUi UserInteraction object (should be an AdminInteraction)
     */
    public static void adminUserManagementMenu(Scanner s, UserInteraction baseUi) {
        // Check if baseUi is an AdminInteraction
        if (!(baseUi instanceof AdminInteraction)) {
            System.out.println("Error: Admin functionality requires AdminInteraction.");
            System.out.println("Press Enter to continue...");
            if (s.hasNextLine()) s.nextLine();
            return;
        }
        
        AdminInteraction adminUi = (AdminInteraction)baseUi;
        printHeader("Admin User Management");

        // Get all users from the AdminInteraction instance
        List<User> allUsers = adminUi.getAllUsers();
        
        // Display users in a table
        if (allUsers != null && !allUsers.isEmpty()) {
            // Use the display method from AdminInteraction
            adminUi.displayUserTable(allUsers);
        } else {
            System.out.println("No users found or error retrieving user list.");
        }

        int choice = ConsoleUtils.getMenuOption(s, Arrays.asList(
            "Add User", 
            "Remove User", 
            "Deactivate User", 
            "Search Users", 
            "Go Back"));

        switch (choice) {
        case 1: // Add User
            if (adminUi.addUser(s)) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }
            break;
        case 2: // Remove User
            if (adminUi.removeUser(s)) {
                System.out.println("User removed successfully.");
            } else {
                System.out.println("Failed to remove user.");
            }
            break;
        case 3: // Deactivate User
            if (adminUi.deactivateUser(s)) {
                System.out.println("User deactivated successfully.");
            } else {
                System.out.println("Failed to deactivate user.");
            }
            break;
        case 4: // Search Users
            adminUi.searchUsers(s);
            break;
        case 5: // Go Back
            return;
        default:
            System.out.println("Invalid option selected.");
            break;
        }
        
        System.out.println("Press Enter to continue...");
        if (s.hasNextLine()) s.nextLine();
    }

    /**
     * Menu for Admin University Management. Uses AdminInteraction for admin-specific operations.
     * 
     * @param s Scanner for input
     * @param baseUi UserInteraction object (should be an AdminInteraction)
     */
    public static void adminUniversityManagementMenu(Scanner s, UserInteraction baseUi) {
        // Check if baseUi is an AdminInteraction
        if (!(baseUi instanceof AdminInteraction)) {
            System.out.println("Error: Admin functionality requires AdminInteraction.");
            System.out.println("Press Enter to continue...");
            if (s.hasNextLine()) s.nextLine();
            return;
        }
        
        AdminInteraction adminUi = (AdminInteraction)baseUi;
        printHeader("Admin University Management");

        int choice = ConsoleUtils.getMenuOption(s, Arrays.asList(
            "View All Universities",
            "Add University",
            "Edit University",
            "Remove University",
            "Go Back"));

        switch (choice) {
            case 1: // View All Universities
                List<University> allUnis = adminUi.getAllUniversities();
                if (allUnis == null || allUnis.isEmpty()) {
                    System.out.println("No universities found in the system.");
                } else {
                    printHeader("All Universities");
                    
                    // Find maximum name length for formatting
                    int maxNameLength = "University Name".length();
                    for (University uni : allUnis) {
                        maxNameLength = Math.max(maxNameLength, uni.getName().length());
                    }
                    
                    // Add padding and create format string
                    maxNameLength += 2;
                    String format = "%-3s | %-" + maxNameLength + "s\n";
                    
                    // Print header
                    System.out.printf(format, "#", "University Name");
                    
                    // Print separator
                    int totalLength = 4 + maxNameLength;
                    for (int i = 0; i < totalLength; i++) {
                        System.out.print("-");
                    }
                    System.out.println();
                    
                    // Print data rows
                    for (int i = 0; i < allUnis.size(); i++) {
                        System.out.printf(format, (i + 1), allUnis.get(i).getName());
                    }
                    System.out.println();
                }
                break;
            case 2: // Add University
                System.out.println("Add university functionality is being implemented.");
                // ToDo: Implement university addition
                break;
            case 3: // Edit University
                System.out.println("Edit university functionality is being implemented.");
                // ToDo: Implement university editing
                break;
            case 4: // Remove University
                System.out.println("Remove university functionality is being implemented.");
                // ToDo: Implement university removal
                break;
            case 5: // Go Back
                return;
            default:
                System.out.println("Invalid option selected.");
                break;
        }
        System.out.println("Press Enter to continue...");
        if (s.hasNextLine()) s.nextLine();
    }
}