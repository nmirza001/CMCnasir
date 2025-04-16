package cmc.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		
		System.out.print("Student Number (or leave empty): ");
		String numStu = s.nextLine();
		int dNumStu;
		try {
			dNumStu = numStu.isEmpty() ? -1 : Integer.parseInt(numStu);
		}
		catch(NumberFormatException e)
		{
			System.out.println("Invalid number.");
			return new ArrayList<University>();
		}
		
		// Use the SearchController via SystemController
		return this.theSystemController.getSearchController().search(state, dNumStu);
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
	
	/*
	 * A more specific method for getting a particular University
	 * @param String schoolName gets user entry for schoolName
	 * @return school gives the school info
	 * @return null is an alt case if school not found
	 * @author Rick Masaana
	 * 3/25/2025
	 */
	public String getUniversityDetails (String schoolName){
		return theSystemController.viewSchool(schoolName);
	}
}
