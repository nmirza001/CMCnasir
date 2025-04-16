package cmc.backend;

import static org.junit.Assert.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cmc.backend.entities.University;
import cmc.backend.controllers.*;

/**
 * Tests for the SearchController class which handles university search operations.
 * 
 * These tests verify that:
 * - State searches work regardless of capitalization
 * - Student number filtering works correctly
 * - Empty searches return all universities
 * - Combined searches filter properly
 * 
 * @author Nasir Mirza
 * @version Apr 14, 2025
 */
public class SearchControllerTest {
    
	private UniversityController uc;
    private SearchController searchController;
    
    @Before
    public void setUp() {
        // Create a new SearchController for each test
    	DatabaseController mock = new MockDatabaseController();
    	uc = new UniversityController(mock);
        searchController = new SearchController(uc);
    }
    
    @After
    public void tearDown() {
        searchController = null;
    }
    
    /**
     * Tests that searching by state works with different capitalizations.
     * Minnesota is used as a test case.
     */
    @Test
    public void testStateSearchIsCaseInsensitive() {
        // Test using different capitalizations
        List<University> upperResults = searchController.search("MINNESOTA", -1);
        List<University> lowerResults = searchController.search("minnesota", -1);
        List<University> mixedResults = searchController.search("Minnesota", -1);
        
        // Check we found some Minnesota schools
        Assert.assertFalse("Should find universities in Minnesota", upperResults.isEmpty());
        
        // Every result should be from Minnesota
        for (University uni : upperResults) {
            Assert.assertEquals("Results should be from Minnesota", "MINNESOTA", uni.getState());
        }
        
        // Results should be the same regardless of capitalization
        Assert.assertEquals("Case shouldn't affect number of results", 
                upperResults.size(), lowerResults.size());
        Assert.assertEquals("Case shouldn't affect number of results", 
                upperResults.size(), mixedResults.size());
        
        // Check actual contents are the same by comparing school names
        Set<String> upperNames = getUniversityNames(upperResults);
        Set<String> lowerNames = getUniversityNames(lowerResults);
        Set<String> mixedNames = getUniversityNames(mixedResults);
        
        Assert.assertEquals("Same schools should be found regardless of case", upperNames, lowerNames);
        Assert.assertEquals("Same schools should be found regardless of case", upperNames, mixedNames);
    }
    
    /**
     * Tests searching by student number.
     */
    @Test
    public void testSearchByStudentNumber() {
        // Try a common student population size
        int studentCount = 10000;
        List<University> results = searchController.search("", studentCount);
        
        // If we found schools with this number, verify each one
        if (!results.isEmpty()) {
            for (University uni : results) {
                Assert.assertEquals("Schools should have " + studentCount + " students", 
                        studentCount, uni.getNumStudents());
            }
        }
    }
    
    /**
     * Tests that an empty search returns all universities.
     */
    @Test
    public void testEmptySearchReturnsAllUniversities() {
        // Search with no criteria (empty state, -1 student count)
        List<University> results = searchController.search("", -1);
        
        // Get the full list directly for comparison
        List<University> allUniversities = uc.getAllSchools();
        
        // Both lists should have the same size
        Assert.assertEquals("Empty search should return all universities",
                allUniversities.size(), results.size());
    }
    
    /**
     * Tests combined search using both state and student number criteria.
     */
    @Test
    public void testCombinedSearch() {
        // Test both criteria together (using California as it has many schools)
        String state = "CALIFORNIA";
        int studentCount = 15000; // A typical size that might exist
        
        List<University> results = searchController.search(state, studentCount);
        
        // For each result, verify both criteria are met
        for (University uni : results) {
            Assert.assertEquals("State should match", state, uni.getState());
            Assert.assertEquals("Student count should match", studentCount, uni.getNumStudents());
        }
    }
    
    /**
     * Tests search with a non-existent state.
     */
    @Test
    public void testNonExistentStateReturnsEmpty() {
        List<University> results = searchController.search("NONEXISTENTSTATE", -1);
        Assert.assertTrue("Non-existent state should return empty list", results.isEmpty());
    }
    
    /**
     * Helper method to extract university names from a list of universities.
     */
    private Set<String> getUniversityNames(List<University> universities) {
        Set<String> names = new HashSet<>();
        for (University uni : universities) {
            names.add(uni.getName());
        }
        return names;
    }
}