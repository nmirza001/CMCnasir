package cmc.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cmc.CMCException;
import cmc.backend.UniversityController;
import cmc.backend.User;
import cmc.backend.entities.University;

/**
 * A mock database controller for testing.
 * @author Roman Lefler
 * @version Apr 14, 2025
 */
public class MockDatabaseController extends DatabaseController {
	
	private Map<String, User> users;
	private Map<String, University> unis;
	private Map<String, List<String>> savedSchools;
	
	public MockDatabaseController() {
		users = new HashMap<>();
		savedSchools = new HashMap<>();
		
		unis = new HashMap<>();
		University augsburg = new University("AUGSBURG");
		augsburg.setState("MINNESOTA");
		augsburg.setNumStudents(10000);
		unis.put("AUGSBURG", augsburg);
	}
	
	public void close() {
		
	}
	
	public boolean addUser(User u) throws CMCException {
		return users.put(u.getUsername(), u.uClone()) == null;
	}

	
	public boolean removeUser(User u) throws CMCException {
		return users.remove(u.getUsername()) != null;
	}

	
	public User getUser(String username) {
		return users.get(username);
	}


	public List<User> getAllUsers() {
		return new ArrayList<User>(users.values());
	}
	
	public boolean editUser(User u) {
		return users.replace(u.getUsername(), u.uClone()) != null;
	}
	
	public boolean saveSchool(String username, String schoolName) throws CMCException {
		
		savedSchools.putIfAbsent(username, new ArrayList<String>());
		
		List<String> list = savedSchools.get(username);
		return list.add(schoolName);
	}
	
	// get the mapping from users to their saved universities in the DB
	// e.g., peter -> {CSBSJU, HARVARD}
	//       juser -> {YALE, AUGSBURG, STANFORD}
	public Map<String, List<String>> getUserSavedSchoolMap() {
		
		return savedSchools;
		
	}
	

	/**
	 * Gets all universities' emphases.
	 * Note that the caller should not assume that all universities
	 * are in the dictionary and if a university has no emphases
	 * it will not be in the dictionary and hence 'get' would
	 * return {@code null}.
	 * @return A map of university names to a list of emphases.
	 * @author Roman Lefler
	 * @version Mar 14, 2025
	 */
	public Map<String, List<String>> getUniversitiesEmphases() {
		
		return new HashMap<>();
		
	}
	

	/**
	 * Gets the list of all the universities in the DB
	 * @return A list of universities
	 * @author Roman Lefler
	 * @version Mar 13, 2025
	 */
	public List<University> getAllSchools() {
		
		return new ArrayList<>(unis.values());
		
	}
	
	/**
	 * Gets a list of all possible emphases.
	 * @return A list of all emphases.
	 * @author Roman Lefler
	 * @version Mar 14, 2025
	 */
	public List<String> getAllEmphases() {
		
		return new ArrayList<>();
		
	}

	/**
	 * Adds a new university to the database.
	 * @param u University with attributes to add.
	 * @return {@code true} if the operation succeeded.
	 * @see #editUniversity(University)
	 * @see #removeUniversity(University)
	 * @author Roman Lefler
	 * @version Mar 13, 2025
	 */
	public boolean addNewUniversity(University u) {
		return unis.putIfAbsent(u.getName(), u) == null;
	}

	/**
	 * Removes a university from the database.
	 * @param u Removes a university by name (only the name is used)
	 * @return {@code true} if the operation succeeded.
	 * @throws IllegalArgumentException if u is {@code null}.
	 * @see #addNewUniversity(University)
	 * @see #editUniversity(University)
	 * @author Roman Lefler
	 * @version Mar 14, 2025
	 */
	public boolean removeUniversity(University u) {
		
		return unis.remove(u.getName()) != null;
	}
	
	/**
	 * Edits a university. The university must already be in
	 * the database.
	 * @param u University information
	 * @return {@code true} if successful.
	 * @see #addNewUniversity(University)
	 * @see #removeUniversity(University)
	 * @author Roman Lefler
	 * @version Mar 16, 2025
	 */
	public boolean editUniversity(University u) {
		
		return unis.put(u.getName(), u) != null;
		
	}


}
