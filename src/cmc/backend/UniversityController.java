/**
 * 
 */
package cmc.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cmc.backend.controllers.DatabaseController;
import cmc.backend.entities.University;
import dblibrary.project.csci230.UniversityDBLibrary;

/**
 * University controller
 * @author Timmy Flynn, Roman Lefler
 * @version Apr 2, 2025
 */
public class UniversityController {

	
	private DatabaseController db;
	
	public UniversityController() {
		this(new DatabaseController());
	}
	
	/**
	 * The injected database controller.
	 * @param injectedDb The new database controller.
	 */
	public UniversityController(DatabaseController injectedDb) {
		this.db = injectedDb;
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
		
		return db.addNewUniversity(u);
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
		
		return db.removeUniversity(u);
	}
	
	
	/**
	 * Edits a university. The university must already be in
	 * the database.
	 * @param u University information
	 * @return {@code true} if successful.
	 * @see #addNewUniversity(University)
	 * @see #removeUniversity(University)
	 * @author Roman Lefler + Rick Masaana
	 * @version 4/15/2025
	 */
	public boolean editUniversity(University u){
		if (u == null) throw new IllegalArgumentException("University is null");
		return db.editUniversity(u);
	}
	
	/**
	 * Gets the list of all the universities in the DB
	 * @return A list of universities
	 * @author Roman Lefler
	 * @version Mar 13, 2025
	 */
	public List<University> getAllSchools() {
		
		return db.getAllSchools();
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
		
		return db.getUniversitiesEmphases();
	}
	
	/**
	 * Gets a university by name.
	 * @param name Exact name to search for
	 * @return The University if found, otherwise null.
	 */
	public University getUniversity(String name) {
		List<University> list = getAllSchools();
		
		if(!University.isValidName(name)) throw new IllegalArgumentException("Invalid university name.");
		for(University u : list) {
			if(u.getName().equals(name)) return u;
		}
		return null;
	}
		
}
