package cmc.userstory;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmc.backend.SearchController;
import cmc.backend.SystemController;
import cmc.backend.UniversityController;
import cmc.backend.controllers.MockDatabaseController;
import cmc.backend.entities.University;
import cmc.frontend.UserInteraction;

/**
 * User story test: As a user, I want to find universities similar to ones I like
 * so that I can explore more options matching my preferences.
 * 
 * This test class validates the functionality of finding similar universities based
 * on various criteria like location, control type, size, and academic metrics.
 * 
 * @author Nasir Mirza
 * @version April 15, 2025
 */
public class FindSimilarUniversitiesTest {
    
    private UserInteraction ui;
    private MockDatabaseController mockDb;
    private SearchController searchController;
    private University targetUniversity;
    private UniversityController universityController;
    
    @Before
    public void setUp() {
        // Initialize the controllers and mock database
        mockDb = new MockDatabaseController();
        ui = new UserInteraction(mockDb);
        universityController = new UniversityController(mockDb);
        searchController = new SearchController(universityController);
        
        // Get a specifically configured target university from the mock data
        targetUniversity = findUniversityByName("TARGET_U");
        
        // Ensure our test data is properly configured
        assertNotNull("Test setup failed: TARGET_U university must exist in mock data", targetUniversity);
    }
    
    @After
    public void tearDown() {
        ui = null;
        mockDb = null;
        searchController = null;
        targetUniversity = null;
        universityController = null;
    }
    
    /**
     * Helper method to find a university by its exact name.
     * 
     * @param name The name of the university to find
     * @return The University object or null if not found
     */
    private University findUniversityByName(String name) {
        List<University> allUniversities = universityController.getAllSchools();
        for (University uni : allUniversities) {
            if (uni.getName().equals(name)) {
                return uni;
            }
        }
        return null;
    }
    
    /**
     * Tests that the findSimilar method correctly identifies universities
     * that match the similarity criteria with a target university.
     * Uses TARGET_U as the target and expects SIMILAR_U to be found.
     */
    @Test
    public void testFindSimilarUniversities() {
        // Execute the method being tested
        List<University> similarUniversities = searchController.findSimilar(targetUniversity);
        
        // Verify results
        assertNotNull("Similar universities list should not be null", similarUniversities);
        assertFalse("Similar universities list should not be empty", similarUniversities.isEmpty());
        
        // Check that the target university itself is not in the results
        for (University university : similarUniversities) {
            assertNotEquals("Target university should not be in the similar results", 
                           targetUniversity.getName(), university.getName());
        }
        
        // Verify that SIMILAR_U is in the results (it should match the criteria)
        University similarU = findUniversityByName("SIMILAR_U");
        assertNotNull("Test setup failed: SIMILAR_U university must exist in mock data", similarU);
        
        boolean foundSimilarU = false;
        for (University university : similarUniversities) {
            if (university.getName().equals("SIMILAR_U")) {
                foundSimilarU = true;
                break;
            }
        }
        assertTrue("SIMILAR_U should be found in the similar universities list", foundSimilarU);
        
        // Verify that DIFFERENT_U is NOT in the results (it shouldn't match the criteria)
        University differentU = findUniversityByName("DIFFERENT_U");
        assertNotNull("Test setup failed: DIFFERENT_U university must exist in mock data", differentU);
        
        boolean foundDifferentU = false;
        for (University university : similarUniversities) {
            if (university.getName().equals("DIFFERENT_U")) {
                foundDifferentU = true;
                break;
            }
        }
        assertFalse("DIFFERENT_U should NOT be found in the similar universities list", foundDifferentU);
    }
    
    /**
     * Tests the similarity criteria validation logic.
     * Confirms that universities in the result list match at least
     * the minimum number of criteria with the target university.
     */
    @Test
    public void testSimilarityMatchesCriteria() {
        // Get similar universities
        List<University> similarUniversities = searchController.findSimilar(targetUniversity);
        
        // Check that each university in the results matches at least 3 criteria
        for (University similar : similarUniversities) {
            int matchCount = 0;
            
            // Check state match
            if (similar.getState() != null && targetUniversity.getState() != null &&
                similar.getState().equals(targetUniversity.getState())) {
                matchCount++;
            }
            
            // Check location match
            if (similar.getLocation() != null && targetUniversity.getLocation() != null &&
                similar.getLocation().equals(targetUniversity.getLocation())) {
                matchCount++;
            }
            
            // Check control match
            if (similar.getControl() != null && targetUniversity.getControl() != null &&
                similar.getControl().equals(targetUniversity.getControl())) {
                matchCount++;
            }
            
            // Check student size within range (25% tolerance)
            if (targetUniversity.getNumStudents() > 0 && similar.getNumStudents() > 0) {
                double lowerBound = targetUniversity.getNumStudents() * 0.75; 
                double upperBound = targetUniversity.getNumStudents() * 1.25;
                if (similar.getNumStudents() >= lowerBound && similar.getNumStudents() <= upperBound) {
                    matchCount++;
                }
            }
            
            // Check SAT scores within range (75 points tolerance)
            double targetSat = targetUniversity.getSatVerbal() + targetUniversity.getSatMath();
            double similarSat = similar.getSatVerbal() + similar.getSatMath();
            if (targetSat >= 400 && similarSat >= 400 && Math.abs(targetSat - similarSat) <= 75) {
                matchCount++;
            }
            
            // Check acceptance rate within range (15 percentage points tolerance)
            if (targetUniversity.getPercentAdmitted() >= 0 && similar.getPercentAdmitted() >= 0) {
                if (Math.abs(targetUniversity.getPercentAdmitted() - similar.getPercentAdmitted()) <= 15.0) {
                    matchCount++;
                }
            }
            
            // Check academic scale within range (1 point tolerance)
            if (targetUniversity.getScaleAcademics() > 0 && similar.getScaleAcademics() > 0) {
                if (Math.abs(targetUniversity.getScaleAcademics() - similar.getScaleAcademics()) <= 1) {
                    matchCount++;
                }
            }
            
            // Based on the SearchController.findSimilar implementation, at least 3 criteria must match
            assertTrue("Similar university should match at least 3 criteria with target university", matchCount >= 3);
        }
    }
    
    /**
     * Tests that the UserInteraction wrapper method for findSimilarUniversities
     * correctly calls the underlying SearchController functionality.
     */
    @Test
    public void testUserInteractionFindSimilarUniversities() {
        // Test the UserInteraction wrapper method
        List<University> similarUniversities = ui.findSimilarUniversities(targetUniversity);
        
        // Verify results
        assertNotNull("Similar universities list from UserInteraction should not be null", similarUniversities);
        
        // Test that target university is not in results
        boolean containsTarget = false;
        for (University uni : similarUniversities) {
            if (uni.getName().equals(targetUniversity.getName())) {
                containsTarget = true;
                break;
            }
        }
        assertFalse("Similar universities list should not contain the target university itself", containsTarget);
    }
    
    /**
     * Tests finding similar universities for a university with no similar matches.
     * Uses TARGET_ALONE_U which is configured to have no similar universities in mock data.
     */
    @Test
    public void testFindSimilarWithNoMatches() {
        // Get the test target university with no similar matches
        University targetAlone = findUniversityByName("TARGET_ALONE_U");
        assertNotNull("Test setup failed: TARGET_ALONE_U university must exist in mock data", targetAlone);
        
        // Find similar universities for this target
        List<University> similarUniversities = searchController.findSimilar(targetAlone);
        
        // Verify no matches are found
        assertNotNull("Similar universities list should not be null", similarUniversities);
        assertTrue("Similar universities list should be empty for TARGET_ALONE_U", similarUniversities.isEmpty());
    }
    
    /**
     * Tests handling of null target university parameter.
     */
    @Test
    public void testNullTargetHandling() {
        // Test handling of null target university
        List<University> similarUniversities = searchController.findSimilar(null);
        
        // Verify proper error handling
        assertNotNull("Result should be non-null even for null input", similarUniversities);
        assertTrue("Result should be empty for null input", similarUniversities.isEmpty());
        
        // Also test the UserInteraction wrapper method with null
        List<University> uiResults = ui.findSimilarUniversities(null);
        assertTrue("UserInteraction result should be empty for null input", uiResults.isEmpty());
    }
    
    /**
     * Tests that findSimilar works with an external University object
     * that's not from the controller's database but matches criteria
     * with universities in the database.
     */
    @Test
    public void testFindSimilarWithExternalTarget() {
        // Create an external university not from the database
        University externalTarget = new University("EXTERNAL_TARGET");
        externalTarget.setState("SOME_STATE");
        externalTarget.setLocation("SOME_LOCATION");
        externalTarget.setControl("SOME_CONTROL");
        externalTarget.setNumStudents(10000);
        externalTarget.setSatVerbal(600);
        externalTarget.setSatMath(600);
        externalTarget.setPercentAdmitted(50);
        externalTarget.setScaleAcademics(4);
        
        // Find similar universities for this external target
        List<University> similarUniversities = searchController.findSimilar(externalTarget);
        
        // Verify results
        assertNotNull("Similar universities list should not be null for external target", similarUniversities);
        
        // Check that SIMILAR_TO_EXTERNAL_U is in the results
        boolean foundSimilarToExternal = false;
        for (University uni : similarUniversities) {
            if (uni.getName().equals("SIMILAR_TO_EXTERNAL_U")) {
                foundSimilarToExternal = true;
                break;
            }
        }
        assertTrue("SIMILAR_TO_EXTERNAL_U should be found for the external target", foundSimilarToExternal);
    }
}