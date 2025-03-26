package cmc.frontend;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cmc.CMCException;
import cmc.backend.SystemController;
import cmc.backend.User;
import cmc.backend.entities.University;

public class UserInteraction {
	
	private User loggedInUser;
	
	private SystemController theSystemController;
	
	// Construct a UserInteraction using the basic (no parameter)
	// SystemController as the single underlying controller object.
	// TODO: Someday, we should refactor the single SystemController class
	//       into multiple classes for better organization of functionalities.
	public UserInteraction() {
		this.theSystemController = new SystemController();
		this.loggedInUser = null;
	}

	// attempt to login, print message, and return success or failure
	public boolean login(String username, String password) {
		User result;
		try {
			result = this.theSystemController.login(username, password);
		}
		catch(CMCException e) {
			System.out.println(e);
			return false;
		}
		
		if (result != null) {
			System.out.println("Login successful!");
			this.loggedInUser = result;
			return true;
		}
		else {
			System.out.println("Login failed!  Incorrect username or password.");
			this.loggedInUser = null;
			return false;
		}
	}
	
	// returns true if there is a user to log out, otherwise false
	public boolean logout() {
		if (this.loggedInUser == null) {
			return false;
		}
		else {
			this.loggedInUser = null;
			return true;
		}
	}
	
	// for admins, this gets the list of all users in the system
	public Map<Integer, String[]> getAllUsers() {
		return this.theSystemController.getAllUsers();
	}
	
	// ask the admin for details and then attempt to add a user to the
	// database
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
		
		return this.theSystemController.addUser(username, password, firstName, lastName, isAdmin);
	}
	
	// ask the admin for a username and then remove that user from the
	// database
	public boolean removeUser(Scanner s) {
		
		Map<Integer, String[]> allU = getAllUsers();

		for(Map.Entry<Integer, String[]> entry : allU.entrySet()) {
			Integer key = entry.getKey();

			String[] val = entry.getValue();

			System.out.println(key + ". " + val[0]);	}
 
		System.out.print("Username number:");
 
		int usernum = s.nextInt();
		s.nextLine();
 
		String[] userName = allU.get(usernum);
		
 
		return this.theSystemController.removeUser(userName[2]);
	}
	
	public List<University> search(Scanner s) {
		// TODO: in the future, we would like to support searching by various
		//       criteria, but we'll settle for just state for now
		System.out.print("State (leave blank to not search by this criterion): ");
		String state = s.nextLine();
		
		return this.theSystemController.search(state);
	}
	
	// ask for a school name to save, and attempt to save that school
	// to the list for the currently-logged-in user
	public boolean saveSchool(Scanner s) {
		System.out.print("School Name: ");
		String schoolName = s.nextLine();

		if (this.loggedInUser == null) {
			return false;
		} else {
			try {
				return this.theSystemController.saveSchool(this.loggedInUser.username, schoolName);
			} catch(CMCException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	// get the list of saved school names for the currently-logged-in user
	public List<String> getSavedSchools() {
		return this.theSystemController.getSavedSchools(this.loggedInUser.username);
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
		return theSystemController.getAllUniversities();
	}
	
	/**
	 * Adds a new university to the database.
	 * @param uni University
	 * @return {@code true} if the operation succeeded.
	 * @throws IllegalArgumentException if uni is {@code null}.
	 * @author Roman Lefler
	 * @version Mar 13, 2025
	 */
	public boolean addNewUniversity(University uni) {
		if(uni == null) throw new IllegalArgumentException();
		return theSystemController.addNewUniversity(uni);
	}
	
	/**
	 * Removes a university from the database.
	 * @param u University
	 * @return {@code true} if the operation succeeded.
	 * @throws IllegalArgumentException if u is {@code null}.
	 * @author Roman Lefler
	 * @version Mar 24, 2025
	 */
	public boolean removeUniversity(University u) {
		if(u == null) throw new IllegalArgumentException();
		return theSystemController.removeUniversity(u);
	}

}
