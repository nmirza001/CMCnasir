package cmc.backend.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a regular user in the system
 * @author Nasir Mirza
 * @version Apr 15, 2025
 */
public class User extends Account {
    private List<SavedSchool> savedSchools;
    
    /**
     * Creates a new User
     * @param username Username
     * @param password Password
     * @param firstName First name
     * @param lastName Last name
     */
    public User(String username, String password, String firstName, String lastName) {
        super(username, password, firstName, lastName);
        this.savedSchools = new ArrayList<>();
    }
    
    @Override
    public boolean isAdmin() {
        return false;
    }
    
    /**
     * Saves a university to the user's list
     * @param university The university to save
     * @return true if added successfully, false if the school was already saved
     */
    public boolean saveSchool(University university) {
        SavedSchool newEntry = new SavedSchool(university);
        if (!savedSchools.contains(newEntry)) {
            savedSchools.add(newEntry);
            return true;
        }
        return false;
    }
    
    /**
     * Removes a university from the user's saved list
     * @param university The university to remove
     * @return true if removed, false if it wasn't in the list
     */
    public boolean removeSavedSchool(University university) {
        for (SavedSchool saved : savedSchools) {
            if (saved.getUniversity().getName().equals(university.getName())) {
                savedSchools.remove(saved);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets all the user's saved schools
     * @return A list of saved schools
     */
    public List<SavedSchool> getSavedSchools() {
        return this.savedSchools;
    }
    
    /**
     * Search through saved schools by criteria
     * @param searchCriteria Text to search for in school names
     * @return A filtered list of saved schools
     */
    public List<SavedSchool> search(String searchCriteria) {
        if (searchCriteria == null || searchCriteria.isEmpty()) {
            return new ArrayList<>(savedSchools);
        }
        
        List<SavedSchool> results = new ArrayList<>();
        String upperCriteria = searchCriteria.toUpperCase();
        
        for (SavedSchool school : savedSchools) {
            University uni = school.getUniversity();
            if (uni.getName().contains(upperCriteria) || 
                uni.getState().contains(upperCriteria) ||
                uni.getLocation().contains(upperCriteria)) {
                results.add(school);
            }
        }
        
        return results;
    }
}