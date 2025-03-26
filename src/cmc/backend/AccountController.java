/**
 * 
 */
package cmc.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cmc.CMCException;
import dblibrary.project.csci230.UniversityDBLibrary;

/**
 * TUI to take information from an admin to add a school.
 * @author Timmy Flynn
 * @version Mar 25, 2025
 */
public class AccountController {

	private DatabaseController db;
	
	private UniversityDBLibrary database;
	
		// Construct a SystemController using the basic (no parameter)
		// DatabaseController as the underlying database access.
		public AccountController() {
			this.db = new DatabaseController();
			this.database = new UniversityDBLibrary("dei", "Csci230$");
		}
		
		// add a user to the db
		// TODO: it would be nice if this could take a User object instead
		// (so "higher-abstraction" classes don't have to worry about the order
		//  of properties)
		public boolean addUser(String username, String password, char type,
				String firstName, String lastName) throws CMCException {
			int result = this.database.user_addUser(firstName, lastName, username, password, type);
			
			if (result == -1) {
				throw new CMCException("Error adding user to the DB");
			}
			else {
				return true;
			}
		}
	
	// remove a user from the db
		public boolean removeUser(String username) throws CMCException {
			
			Map<String, List<String>> schoolMap = db.getUserSavedSchoolMap();
			List<String> schools = schoolMap.get(username);
			if(schools != null) {
				for(String s : schools)database.user_removeSchool(username, s);
			}
			
			int result = this.database.user_deleteUser(username);
			
			if (result != 1) {
				// TODO: How can we tell the difference?
				throw new CMCException("Error removing user \"" + username +
						"\" from the DB.  Not present?  DB error?");
			}
			else {
				return true;
			}
		}
		
		// deactivate a user in the database
		// This is messy, and it would be much cleaner to do
		// an editUser with an updated User object!
		public boolean deactivateUser(String username) throws CMCException {
			String[] theUser = db.getUser(username);
			if (theUser == null)
				return false;
			int result = this.database.user_editUser(theUser[2], theUser[0], theUser[1], theUser[3], theUser[4].charAt(0), 'N');
			if (result == -1) {
				throw new CMCException("Error editing user (to deactivate) in the DB");
			}
			else {
				return true;
			}
		}
		
		// get the list of all the users in the DB
		public List<String[]> getAllUsers() {
			String[][] dbUserList = this.database.user_getUsers();
			
			ArrayList<String[]> result = new ArrayList<String[]>();
			for (String[] user : dbUserList) {
				result.add(user);
			}
			
			return result;
		}

}
