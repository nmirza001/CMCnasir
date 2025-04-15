package cmc.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import cmc.backend.AccountController;
import cmc.backend.UniversityController;
import cmc.backend.User;
import cmc.backend.entities.University;
import cmc.CMCException;
import cmc.backend.SystemController;

public class AdminInteraction extends UserInteraction{
	@SuppressWarnings("unused")
	private final UniversityController theUniversityController;
	
	
	public AdminInteraction(SystemController theSystemController, AccountController acct, UniversityController theUniversityController){
		this.theUniversityController = theUniversityController;
	}
	
	//(0) - display methods
	
	public void displayAdminMenu(){
		
	}
    public void handleUserManagement(){
		
	}
    
    //(1) - Adder & Subtracter methods
    
    //accidentally deleted these - so I retyped them Roman
    public boolean addNewUniversity(University uni){
    	if (uni == null) throw new IllegalArgumentException();
    	return theSystemController.addNewUniversity(uni);
    }
    
    public boolean removeUniversity (University uni){
    	if (uni == null) throw new IllegalArgumentException();
    	return theSystemController.removeUniversity(uni);
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
 		
 		User u = new User(username, password, isAdmin, firstName, lastName);
 		return this.theSystemController.addUser(u);
 	}
 	
 // ask the admin for a username and then remove that user from the
 	// database
 	public boolean removeUser(Scanner s) {
 		
 		List<User> allUsers = getAllUsers();

 		int len = allUsers.size();
 		// List starts at 1
 		for(int i = 0; i < len; i++) {
 			String msg = String.format("%d. %s", i + 1, allUsers.get(i).getUsername());
 			System.out.println(msg);
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
  
 		// List starts at 1 so zero is not a valid option
 		if(usernum <= 0 || usernum > allUsers.size()) {
 			return false;
 		}
 		
 		// Shift everything by 1 since list starts at 1
 		User u = allUsers.get(usernum - 1);
 		
 		return this.theSystemController.removeUser(u);
 	}
 	
 	//ask the admin for a username and then deactivates that user
 	public boolean deactivateUser(Scanner s) {
 		
 		List<User> allUsers = getAllUsers();

 		int len = allUsers.size();
 		// List starts at 1
 		for(int i = 0; i < len; i++) {
 			String msg = String.format("%d. %s", i + 1, allUsers.get(i).getUsername());
 			System.out.println(msg);
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
  
 		// List starts at 1 so zero is not a valid option
 		if(usernum <= 0 || usernum > allUsers.size()) {
 			return false;
 		}
 		
 		// Shift everything by 1 since list starts at 1
 		User u = allUsers.get(usernum - 1);
 		
 		return this.theSystemController.deactivateUser(u);
 	}
 	
 	/**
 	 * Search for user and return their number in list and there username name and last name
 	 * @param s scans for username to search for
 	 * @return returns true if username was found and false if not found
 	 * @author tflynn001
 	 * Version April 14, 2025
 	 */
 	
 	public boolean searchUsers(Scanner s) {
        List<User> uList = getAllUsers();
        
        System.out.print("Username name to search for:");
        
 		String searchName = s.nextLine();

        int nameSpot = 0;
        
        for (int i = 0; i < uList.size(); i++) {
            User us = uList.get(i);
            
            boolean ignoreState = searchName.equals("");
            if(!ignoreState && !us.getUsername().equals(searchName)) continue;
            
            nameSpot = i + 1;
            System.out.println("The user you are looking for has the number " + nameSpot);
            System.out.println(us.getUsername() + " | " + us.getFirstName() + " | " + us.getLastName());
            break;
        }
        if(nameSpot > 0)
        	return true;
        else
        	return false;
    }
 	
 	
 	//(2) - Admin Viewer Methods
 	
    // for admins, this gets the list of all users in the system
 	public List<User> getAllUsers() {
 		return acct.getAllUsers();
 	}
 	
 	//(3) - Admin Editor Methods
 	
	public boolean editUniversityDetails (University dataBaseUniversity) throws CMCException {
		return SystemController.editUniversityDetails(dataBaseUniversity);
	}
 	
 	
 	//(4) - Admin Function Formatter Methods
 	
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

}
