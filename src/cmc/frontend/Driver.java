package cmc.frontend;

import java.util.Arrays;
import java.util.List;
// import java.util.Map; // Not currently needed for these features
import java.util.Scanner;
import cmc.*; // For CMCException

// Use the specific User class that UserInteraction holds
import cmc.backend.User;

// Use the entity classes for data representation
import cmc.backend.entities.University;
// Note: cmc.backend.entities.Admin is not directly used here due to type mismatch issue

public class Driver {

	// Initialize UserInteraction which handles controller access
	private static UserInteraction ui = new UserInteraction();

	// Private constructor to prevent instantiation
	private Driver() throws CMCException {
		throw new CMCException("Attempt to instantiate a Driver");
	}

	// print the header for the current menu
	private static void printHeader(String title) {
		String dashes = "";
		for (int i = 0; i < title.length(); i++)
			dashes += "-";

		System.out.println("\n" + dashes); // Add newline before header
		System.out.println(title);
		System.out.println(dashes);
	}

	/**
	 * Displays search results and provides options to interact with them.
	 * @param s Scanner for input
	 * @param results List of Universities from the search
	 * @param currentUi The UserInteraction object for actions like saving schools.
	 */
	private static void searchResultsMenu(Scanner s, List<University> results, UserInteraction currentUi) {
		printHeader("Search Results");

        if (results == null || results.isEmpty()) {
            System.out.println("No schools found matching your criteria.");
            System.out.println("Press Enter to continue...");
            if (s.hasNextLine()) s.nextLine(); // Consume potential leftover newline
            return;
        }

		for (int i = 0; i < results.size(); i++) {
            University school = results.get(i);
            // Display basic info in results
            System.out.printf("%d. %s (%s, %s)\n",
                i + 1,
                school.getName(),
                school.getState() != null ? school.getState() : "N/A", // Handle potential nulls
                school.getLocation() != null && !school.getLocation().equals("-1") ? school.getLocation() : "N/A"
            );
		}
		System.out.println();

		int choice = ConsoleUtils.getMenuOption(s, Arrays.asList("View Details", "Save School", "Go Back"));

		switch(choice) {
        case 1: // View Details
             System.out.print("Enter the number of the school to view details: ");
             int viewChoice = ConsoleUtils.getSingleMenuEntry(s, 1, results.size());
             if (viewChoice != -1) {
                 University selected = results.get(viewChoice - 1);
                 displaySchoolDetailsAndSimilar(s, selected, currentUi); // Use helper
             } else {
                 System.out.println("Invalid selection.");
             }
             break;
		case 2: // Save School
            // UserInteraction.saveSchool asks for the name again.
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
	 * @param s Scanner for input
	 * @param currentUi The UserInteraction object to get saved schools.
	 */
	private static void userSavedSchoolListMenu(Scanner s, UserInteraction currentUi) {
		printHeader("User Saved School List");

		List<String> schools = currentUi.getSavedSchools();

        if (schools == null || schools.isEmpty()) {
            System.out.println("You have no saved schools.");
        } else {
            System.out.println("Your saved schools:");
            for (int i = 0; i < schools.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, schools.get(i));
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
                 University selected = currentUi.getUniversityObjectByName(schoolName); // Get the object
                 if (selected != null) {
                     displaySchoolDetailsAndSimilar(s, selected, currentUi); // Use helper
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
            // TODO: Implement remove school logic (needs backend support)
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

            if (similarSchools == null || similarSchools.isEmpty()) { // Check for null just in case
                System.out.println("No similar schools found based on the defined criteria.");
            } else {
                printHeader("Similar Schools Found:");
                int displayLimit = Math.min(similarSchools.size(), 10);
                for (int i = 0; i < displayLimit; i++) {
                    University simUni = similarSchools.get(i);
                    String numStudentsStr = simUni.getNumStudents() > 0 ? String.valueOf(simUni.getNumStudents()) : "N/A";
                    String acceptanceRateStr = simUni.getPercentAdmitted() >= 0 ? String.format("%.1f%%", simUni.getPercentAdmitted()) : "N/A";
                    String locationStr = (simUni.getLocation() != null && !simUni.getLocation().equals("-1")) ? simUni.getLocation() : "N/A";

                    System.out.printf("%d. %s (%s, %s) - Students: %s, Acceptance: %s\n",
                            i + 1,
                            simUni.getName(),
                            simUni.getState() != null ? simUni.getState() : "N/A",
                            locationStr,
                            numStudentsStr,
                            acceptanceRateStr);
                }
                 if (similarSchools.size() > displayLimit) {
                     System.out.println("... and " + (similarSchools.size() - displayLimit) + " more.");
                 }
                System.out.println();
            }
        }
        // --- End Similar Schools ---

        System.out.println("Press Enter to continue...");
        if (s.hasNextLine()) s.nextLine(); // Consume newline after potential Y/N input
    }


	/**
	 * Main menu for admin users. Checks for admin privileges before proceeding.
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

        // --- User is confirmed Admin ---
		printHeader("Admin Menu");

		int choice = ConsoleUtils.getMenuOption(s, Arrays.asList("Manage Users",
				"Manage Universities",
				"Logout"));

		switch(choice) {
		case 1: // Manage Users
            // Pass the UserInteraction object; the called method must handle admin actions
			adminUserManagementMenu(s, ui);
			break;
		case 2: // Manage Universities
            // Pass the UserInteraction object; the called method must handle admin actions
			adminUniversityManagementMenu(s, ui);
			break;
		case 3: // Logout
			ui.logout();
			break;
		default:
			System.err.println("Internal error: Unsupported option in adminMenu.");
            break;
		}
	}

	/**
	 * Handles the initial login prompt.
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
                    // This implies user typed 'quit' or login failed repeatedly.
                    // Check if login failed specifically vs quit? For now, assume quit intent.
                    // A more robust approach would have topMenu return distinct statuses.
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
     * Placeholder menu for Admin User Management. Requires Admin-specific logic.
     * @param s Scanner for input
     * @param baseUi UserInteraction object (needs methods for admin actions)
     */
	public static void adminUserManagementMenu(Scanner s, UserInteraction baseUi){
        // This requires methods on UserInteraction or a separate AdminInteraction
        // class/interface to perform actions like adding users, deactivating, etc.
        // The current UserInteraction does not have these admin-specific methods.
        // Providing a basic structure:
        printHeader("Admin User Management (Placeholder)");

        // Ideally, call a method like baseUi.getAllUserDetailsForAdmin()
        // List<User> allUsers = baseUi.someAdminGetAllUsersMethod();
        // if (allUsers...) display them with status

        System.out.println("1. Add User (Not Implemented)");
        System.out.println("2. Edit User (Not Implemented)");
        System.out.println("3. Deactivate/Reactivate User (Not Implemented)");
        System.out.println("4. Go Back");

        System.out.print("Choose an option: ");
        String choiceStr = s.nextLine();
        int choice = -1;
        try {
            choice = Integer.parseInt(choiceStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            choice = -1;
        }

		switch (choice){
		case 1:
            System.out.println("Functionality to add users needs to be implemented in the backend/interaction layer.");
			break;
		case 2:
            System.out.println("Functionality to edit users needs to be implemented.");
			break;
		case 3:
            System.out.println("Functionality to change user activation status needs to be implemented.");
			break;
		case 4: // Go Back
			return;
		default:
			System.out.println("Invalid option selected.");
            break;
		}
        System.out.println("Press Enter to continue...");
        if (s.hasNextLine()) s.nextLine();
	}

    /**
     * Placeholder menu for Admin University Management. Requires Admin-specific logic.
     * @param s Scanner for input
     * @param baseUi UserInteraction object (needs methods for admin actions)
     */
    public static void adminUniversityManagementMenu(Scanner s, UserInteraction baseUi) {
        // This requires methods on UserInteraction or a separate AdminInteraction
        // class/interface to perform actions like adding/editing/removing universities.
        printHeader("Admin University Management (Placeholder)");

        System.out.println("1. View All Universities");
        System.out.println("2. Add University (Not Implemented)");
        System.out.println("3. Edit University (Not Implemented)");
        System.out.println("4. Remove University (Not Implemented)");
        System.out.println("5. Go Back");

        System.out.print("Choose an option: ");
        String choiceStr = s.nextLine();
        int choice = -1;
         try {
            choice = Integer.parseInt(choiceStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            choice = -1;
        }

        switch (choice) {
            case 1:
                List<University> allUnis = baseUi.getAllUniversities();
                if (allUnis == null || allUnis.isEmpty()) {
                    System.out.println("No universities found in the system.");
                } else {
                    printHeader("All Universities");
                    for (int i = 0; i < allUnis.size(); i++) {
                        System.out.printf("%d. %s\n", i + 1, allUnis.get(i).getName());
                    }
                }
                break;
            case 2:
                System.out.println("Functionality to add universities needs to be implemented.");
                break;
            case 3:
                System.out.println("Functionality to edit universities needs to be implemented.");
                break;
            case 4:
                System.out.println("Functionality to remove universities needs to be implemented.");
                break;
            case 5:
                return; // Go back
            default:
                System.out.println("Invalid option selected.");
                break;
        }
        System.out.println("Press Enter to continue...");
        if (s.hasNextLine()) s.nextLine();
    }
}