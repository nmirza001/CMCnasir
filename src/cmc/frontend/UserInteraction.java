package cmc.frontend;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cmc.CMCException;
import cmc.backend.AccountController;
import cmc.backend.SystemController;
import cmc.backend.User;
import cmc.backend.entities.University;

public class UserInteraction {
	
	private User loggedInUser;
	
	private SystemController theSystemController;
	private AccountController acct;
	
	// Construct a UserInteraction using the basic (no parameter)
	// SystemController as the single underlying controller object.
	// TODO: Someday, we should refactor the single SystemController class
	//       into multiple classes for better organization of functionalities.
	public UserInteraction() {
		this.theSystemController = new SystemController();
		acct = new AccountController();
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
	public List<String[]> getAllUsers() {
		return acct.getAllUsers();
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
		
		List<String[]> allU = getAllUsers();

		int len = allU.size();
		for(int i = 0; i < len; i++) {
			
			System.out.println(i + ". " + allU.get(i)[0]);
		}
 
		System.out.print("Username number:");
 
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
 
		if(usernum < 0 || usernum >= allU.size()) {
			return false;
		}
		
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
	
	/*
	 * A more specific method for getting a particular University
	 * @param String schoolName gets user entry for schoolName
	 * @return school gives the school info
	 * @return null is an alt case if school not found
	 * @author Rick Masaana
	 * 3/25/2025
	 */
	public University getUniversityDetails (String schoolName){
		List<University> allSchools = theSystemController.getAllUniversities();
		
		//iterate through list using University list
		for (University school : allSchools) {
			
			//confirm that named school exists
			if (school.getName().equals(schoolName)){
				return school; //return the school info
			}
		}
		return null; //else null
	}
	
	/*
	 * string builder for formating University details
	 * University university
	 * @author Rick Masaana
	 * 3/25/2025
	 */
	public String formatUniversityDetails(University university){
		
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(university.getName()).append("\n");
		sb.append("State: ").append(university.getState()).append("\n");
		sb.append("Location: ").append(university.getLocation()).append("\n");
		sb.append("Control: ").append(university.getControl()).append("\n");
		sb.append("Number of Students: ").append(university.getNumStudents()).append("\n");
		sb.append("Percent female: ").append(university.getPercentFemale()).append("\n");
		sb.append("SAT Verbal: ").append(university.getSatVerbal()).append("\n");
		sb.append("SAT Math: ").append(university.getSatMath()).append("\n");
		sb.append("expenses: ").append(university.getExpenses()).append("\n");
		sb.append("Percent Financial Aid: ").append(university.getPercentFinancialAid()).append("\n");
		sb.append("Applicants: ").append(university.getNumApplicants()).append("\n");
		sb.append("Percent Admitted: ").append(university.getPercentAdmitted()).append("\n");
		sb.append("Percent Enrolled: ").append(university.getPercentEnrolled()).append("\n");
		sb.append("Academic Scale: ").append(university.getScaleAcademics()).append("\n");
		sb.append("Social Scale: ").append(university.getScaleSocial()).append("\n");
		sb.append("Quality of Life Scale: ").append(university.getScaleQualityOfLife()).append("\n");
		
		return sb.toString();
	}
	
	public boolean editUniversityDetails (University dataBaseUniversity) throws CMCException {
		return SystemController.editUniversityDetails(dataBaseUniversity);
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
