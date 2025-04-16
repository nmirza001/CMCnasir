package cmc.frontend;

import java.util.ArrayList;
import java.util.List;
// import java.util.Map; // Not currently needed
import java.util.Scanner;

import cmc.CMCException;
import cmc.backend.AccountController;
import cmc.backend.SystemController;
import cmc.backend.User;
import cmc.backend.controllers.DatabaseController;
import cmc.backend.entities.University;

public class UserInteraction {

	private User loggedInUser;

	protected SystemController theSystemController; //changed for extension access
	protected AccountController acct; //changed for extension access

	// Construct a UserInteraction using the basic (no parameter)
	// SystemController as the single underlying controller object.
	// TODO: Someday, we should refactor the single SystemController class
	//       into multiple classes for better organization of functionalities.
	public UserInteraction() {
		this.theSystemController = new SystemController();
		acct = new AccountController();
	}

	public UserInteraction(DatabaseController injectDb) {
		theSystemController = new SystemController(injectDb);
		acct = new AccountController(injectDb);
	}

	// attempt to login, print message, and return success or failure
	public boolean login(String username, String password) {
		User result;
		try {
			result = this.theSystemController.login(username, password);
		}
		catch(CMCException e) {
			System.out.println("Login Error: " + e.getMessage()); // More specific error
			return false;
		}

		if (result != null) {
			System.out.println("Login successful!");
			this.loggedInUser = result;
			return true;
		}
		else {
			System.out.println("Login failed! Incorrect username or password, or account inactive."); // More informative
			this.loggedInUser = null;
			return false;
		}
	}

	// returns true if there is a user to log out, otherwise false
	public boolean logout() {
		if (this.loggedInUser == null) {
			System.out.println("Not logged in."); // Give feedback
			return false;
		}
		else {
			System.out.println("Logging out " + this.loggedInUser.getUsername() + "."); // Give feedback
			this.loggedInUser = null;
			return true;
		}
	}

	/**
	 * Searches for universities based on user input criteria.
	 *
	 * @param s Scanner for reading user input
	 * @return List of universities matching the search criteria
	 */
	public List<University> search(Scanner s) {
		// TODO: in the future, we would like to support searching by various
		//       criteria, but we'll settle for just state for now
		System.out.print("State (leave blank to not search by this criterion): ");
		String state = s.nextLine();

		System.out.print("Student Number (leave blank or enter number): "); // Clarified prompt
		String numStuStr = s.nextLine(); // Renamed variable for clarity
		int dNumStu;
		try {
			// Use -1 to indicate "ignore this criterion" if blank
			dNumStu = numStuStr.trim().isEmpty() ? -1 : Integer.parseInt(numStuStr.trim());
		}
		catch(NumberFormatException e)
		{
			System.out.println("Invalid number entered. Ignoring student number criterion.");
			dNumStu = -1; // Treat invalid input as "ignore"
		}

		// Use the SearchController via SystemController
        if (theSystemController == null || theSystemController.getSearchController() == null) {
             System.err.println("Error: SystemController or SearchController not initialized in UserInteraction.");
             return new ArrayList<>();
        }
		return this.theSystemController.getSearchController().search(state, dNumStu);
	}

	// ask for a school name to save, and attempt to save that school
	// to the list for the currently-logged-in user
	public boolean saveSchool(Scanner s) {
		System.out.print("Enter the EXACT name of the school to save: "); // Emphasize exact name
		String schoolName = s.nextLine().toUpperCase(); // Standardize to uppercase

		if (this.loggedInUser == null) {
            System.out.println("You must be logged in to save a school.");
			return false;
		} else {
			try {
                // Check if school exists before trying to save (optional but good practice)
                if (getUniversityObjectByName(schoolName) == null) {
                    System.out.println("School '" + schoolName + "' not found in the database.");
                    return false;
                }
				return this.theSystemController.saveSchool(this.loggedInUser.getUsername(), schoolName);
			} catch(CMCException e) {
                System.err.println("Error saving school: " + e.getMessage());
				// Consider re-throwing or handling more gracefully depending on CMCException details
				// throw new RuntimeException(e); // Avoid RuntimeException unless necessary
                return false;
			} catch(Error e) { // Catch the specific Error thrown by the current saveSchool implementation
                 System.err.println("Error saving school: " + e.getMessage());
                 return false;
            }
		}
	}

	// get the list of saved school names for the currently-logged-in user
	public List<String> getSavedSchools() {
        if (this.loggedInUser == null) {
            System.out.println("You must be logged in to view saved schools.");
            return new ArrayList<>(); // Return empty list
        }
		return this.theSystemController.getSavedSchools(this.loggedInUser.getUsername());
	}

	/**
	 * Get the current username for the current user logged in via
	 * this UserInteraction class.
	 *
	 * @return the username for the logged in user
	 */
	public User getLoggedInUser() {
		return this.loggedInUser;
	}

	/**
	 * Gets a list of every university in the database.
	 * @return All universities in the database.
	 * @author Roman Lefler
	 * @version Mar 24, 2025
	 */
	public List<University> getAllUniversities() {
        if (theSystemController == null) {
             System.err.println("Error: SystemController not initialized in UserInteraction.");
             return new ArrayList<>();
        }
		return theSystemController.getAllUniversities();
	}

	/**
	 * Gets detailed information string for a specific university.
	 * @param schoolName The exact, uppercase name of the school.
	 * @return Formatted string of school details, or an error message if not found.
	 */
	public String getUniversityDetails (String schoolName){
        if (theSystemController == null) {
             return "Error: SystemController not initialized.";
        }
        // Ensure name is uppercase for consistency with potential backend expectations
        String upperCaseSchoolName = (schoolName == null) ? "" : schoolName.toUpperCase();
		return theSystemController.viewSchool(upperCaseSchoolName);
	}


    // <<< NEW METHOD START >>>
    /**
     * Finds a University object by its exact name (case-sensitive, usually uppercase).
     * Needed to pass the correct object to findSimilarUniversities.
     *
     * @param name The exact name of the university.
     * @return The University object if found, otherwise null.
     */
    public University getUniversityObjectByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        // Assumes names in the database/list are consistently cased (e.g., uppercase)
        String searchName = name.toUpperCase();

        List<University> allUnis = getAllUniversities(); // Use the existing method
        if (allUnis == null) return null; // Handle case where getAllUniversities fails

        for (University u : allUnis) {
            if (u.getName().equals(searchName)) {
                return u; // Found the match
            }
        }
        return null; // Not found
    }
    // <<< NEW METHOD END >>>


    // <<< NEW METHOD START >>>
    /**
     * Finds universities similar to the provided one by delegating to the SearchController.
     *
     * @param targetUniversity The university to find similar ones for.
     * @return A list of similar universities. Returns an empty list on error or if none found.
     */
    public List<University> findSimilarUniversities(University targetUniversity) {
        if (targetUniversity == null) {
            System.err.println("Cannot find similar universities for a null target.");
            return new ArrayList<>();
        }
        if (theSystemController == null || theSystemController.getSearchController() == null) {
             System.err.println("Error: SystemController or SearchController not initialized in UserInteraction.");
             return new ArrayList<>();
        }
        // Delegate the call to the backend SearchController
        return theSystemController.getSearchController().findSimilar(targetUniversity);
    }
    // <<< NEW METHOD END >>>

}