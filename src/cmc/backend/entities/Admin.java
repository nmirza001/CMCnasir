package cmc.backend.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Admin entity
 * @author Alex
 * @version 3/25/2025
 */

public class Admin extends Account{
	public Admin(String username, String password, String firstName, String lastName) {
		super(username,password,firstName,lastName);
	}
	//Because admin always returns true
	@Override
	public boolean isAdmin() {
		return true;
	}
	/**
	 * 
	 * @param user the user to create
	 * @return true if user is created otherwise false
	 */
	//public boolean createUser (User user) {
	//	return false;
	//}
	/**
	 * 
	 * @param username the username of account to delete
	 * @return true if successful, false otherwise
	 */
	public boolean deleteUser(String username) {
		return false;
	}
	/**
	 * 
	 * @return a list of User objects
	 */
	//public List<User> listAllUsers(){
	//	return new ArrayList<User>();
	//}
	/**
	 * 
	 * @param universityName name of university to manage
	 * @return true if successful, false otherwise
	 */
	public boolean manageUniversity(String universityName) {
		return false;
	}
	/**
	 * 
	 * @param username the user to deactivate
	 * @return true if successful, false otherwise
	 */
	public boolean deactivateUser(String username) {
		return false;
	}
}