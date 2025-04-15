package cmc.backend;
import java.util.ArrayList;
import java.util.List;
import cmc.backend.entities.University;
/**
 * Controller for handling search operations related to universities.
 * @author Nasir Mirza
 * @version Apr 14, 2025
 */
public class SearchController {
    
    private UniversityController universityController;
    
    /**
     * Constructs a SearchController with a default UniversityController.
     */
    public SearchController() {
        this.universityController = new UniversityController();
    }
    
    /**
     * Constructs a SearchController with a specified UniversityController.
     * Useful for dependency injection in testing.
     * 
     * @param universityController The university controller to use
     */
    public SearchController(UniversityController universityController) {
        this.universityController = universityController;
    }
    
    /**
     * Searches for universities based on provided criteria.
     * Search is case-insensitive for string criteria.
     * 
     * @param state State name (case-insensitive) or empty string to ignore
     * @param stuNum Exact student number or -1 to ignore
     * @return A list of universities that match the given criteria
     */
    public List<University> search(String state, int stuNum) {
        List<University> schoolList = universityController.getAllSchools();
        List<University> filteredList = new ArrayList<University>();
        
        // Convert search state to uppercase for case-insensitive comparison
        String searchState = state.toUpperCase();
        
        for (int i = 0; i < schoolList.size(); i++) {
            University uni = schoolList.get(i);
            
            boolean ignoreState = searchState.equals("");
            if(!ignoreState && !uni.getState().equals(searchState)) continue;
            
            boolean ignoreStuNum = stuNum < 0;
            if(!ignoreStuNum && uni.getNumStudents() != stuNum) continue;
            
            filteredList.add(uni);
        }
        
        return filteredList;
    }
    
    /**
     * Advanced search for universities with multiple criteria.
     * This is an extension point for future development to add more search criteria.
     * 
     * @param criteria A map of criteria field names to their values
     * @return A list of universities matching the criteria
     */
    /* For future extension:
    public List<University> advancedSearch(Map<String, Object> criteria) {
        // Implementation for future extension
        return new ArrayList<University>();
    }
    */
}