/**
 * 
 */
package cmc.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmc.CMCException;
import cmc.backend.controllers.DatabaseController;
import dblibrary.project.csci230.UniversityDBLibrary;

/**
 * Account controller
 * @author Timmy Flynn, Roman Lefler
 * @version Mar 25, 2025
 */
public class AccountController {

	private DatabaseController db;
	
	// Construct a SystemController using the basic (no parameter)
	// DatabaseController as the underlying database access.
	public AccountController() {
		this.db = new DatabaseController();
	}
	
	// add a user to the db
	// TODO: it would be nice if this could take a User object instead
	// (so "higher-abstraction" classes don't have to worry about the order
	//  of properties)
	public boolean addUser(User u) throws CMCException {
		
		return db.addUser(u);
	}

	// remove a user from the db
	public boolean removeUser(User u) throws CMCException {
		
		return db.removeUser(u);
	}
	
	// deactivate a user in the database
	// This is messy, and it would be much cleaner to do
	// an editUser with an updated User object!
	public boolean deactivateUser(String username) throws CMCException {
		
		return db.deactivateUser(username);
	}
	
	// get the list of all the users in the DB
	public List<User> getAllUsers() {
		
		return db.getAllUsers();
	}
	
}
