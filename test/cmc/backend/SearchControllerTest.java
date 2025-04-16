package cmc.backend;

import static org.junit.Assert.*; // Using static imports for JUnit 4 assertions

import java.util.List;
import java.util.ArrayList; // Explicitly importing ArrayList, though List interface is usually sufficient
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert; // Using Assert class directly is also possible
import org.junit.Before;
import org.junit.Test;

import cmc.backend.entities.University;
import cmc.backend.controllers.*; // Importing controllers package, assuming MockDatabaseController resides here

/**
 * Provides JUnit tests for the {@link SearchController} class.
 * This test suite focuses on verifying the functionality of university search operations,
 * such as searching by state, student population, combined criteria, and finding
 * universities with similar characteristics. It ensures that searches handle
 * case-insensitivity, empty inputs, and specific filtering correctly, along with
 * validating the logic for identifying similar schools based on predefined criteria.
 * Edge cases, like searching for non-existent states or providing null targets for
 * similarity checks, are also covered.
 *
 * @author Nasir Mirza // Original author credited
 * @version April 15, 2025 // Updated version date
 */
public class SearchControllerTest {

    private UniversityController uc;
    private SearchController searchController;
    private List<University> allMockUniversities; // Caches the full list of mock universities for reuse

    /**
     * Sets up the test environment before each test method.
     * Initializes the mock database controller, university controller, and search controller.
     * It also retrieves and caches the list of all mock universities provided by the
     * {@link MockDatabaseController} to be used as a baseline or for comparison in tests.
     * Basic checks ensure the mock data is available and not empty.
     */
    @Before
    public void setUp() {
        // Initialize controllers using the mock database for testing purposes
        DatabaseController mockDbController = new MockDatabaseController(); // Assumes MockDatabaseController provides necessary test data
        uc = new UniversityController(mockDbController);
        searchController = new SearchController(uc);

        // Fetch the mock university data once for efficiency
        allMockUniversities = uc.getAllSchools();
        Assert.assertNotNull("Setup failed: MockDatabaseController must provide a non-null list of universities", allMockUniversities);
        Assert.assertFalse("Setup failed: MockDatabaseController must provide a non-empty list for these tests to be meaningful", allMockUniversities.isEmpty());
    }

    /**
     * Cleans up resources after each test method executes.
     * Resets controller instances and the cached university list to null to ensure
     * test isolation and potentially aid garbage collection.
     */
    @After
    public void tearDown() {
        searchController = null;
        uc = null;
        allMockUniversities = null;
    }

    // --- Existing Tests (Comments added/refined for clarity) ---

    /**
     * Verifies that searching by state name ignores capitalization.
     * It performs searches using uppercase, lowercase, and mixed-case state names
     * and asserts that the results are identical in content and size, ensuring
     * the search logic is case-insensitive as expected.
     */
    @Test
    public void testStateSearchIsCaseInsensitive() {
        // Search using different capitalization for the same state
        List<University> upperResults = searchController.search("MINNESOTA", -1);
        List<University> lowerResults = searchController.search("minnesota", -1);
        List<University> mixedResults = searchController.search("Minnesota", -1);

        // Optional Check: Assert that some results were found (depends on mock data)
        // Assert.assertFalse("Should find universities in Minnesota", upperResults.isEmpty()); // This could fail if MockData lacks MN schools

        // Check that all results found (if any) are indeed from the correct state
        for (University uni : upperResults) {
            Assert.assertEquals("All results should be from Minnesota", "MINNESOTA", uni.getState());
        }

        // Assert that the number of results is consistent regardless of case
        Assert.assertEquals("Case should not affect the number of results found",
                upperResults.size(), lowerResults.size());
        Assert.assertEquals("Case should not affect the number of results found",
                upperResults.size(), mixedResults.size());

        // Convert results to sets of names for content comparison (order doesn't matter)
        Set<String> upperNames = getUniversityNames(upperResults);
        Set<String> lowerNames = getUniversityNames(lowerResults);
        Set<String> mixedNames = getUniversityNames(mixedResults);

        // Assert that the sets of found universities are identical
        Assert.assertEquals("The same set of schools should be found regardless of case", upperNames, lowerNames);
        Assert.assertEquals("The same set of schools should be found regardless of case", upperNames, mixedNames);
    }

    /**
     * Tests the functionality of searching for universities based on their exact
     * number of students. It first identifies a valid student count from the mock data
     * and then searches using that count, verifying that all returned universities
     * match the specified student population size.
     */
    @Test
    public void testSearchByStudentNumber() {
        // Dynamically find a student count present in the mock data to ensure the test is relevant
        int studentCount = -1;
        for(University u : allMockUniversities) {
            if (u.getNumStudents() > 0) { // Look for a school with a known, positive student count
                studentCount = u.getNumStudents();
                break;
            }
        }
        // Ensure we found a usable student count from the mock data
        Assert.assertTrue("Test precondition failed: Mock data needs at least one school with a positive student count.", studentCount > 0);

        // Perform the search using the found student count
        List<University> results = searchController.search("", studentCount); // Empty state means search only by student count

        // Verify that results were found and all match the criteria
        Assert.assertFalse("Search should find at least one school with student count " + studentCount, results.isEmpty());
        for (University uni : results) {
            Assert.assertEquals("All resulting schools should have exactly " + studentCount + " students",
                    studentCount, uni.getNumStudents());
        }
    }

    /**
     * Verifies that performing a search with empty criteria (empty state string and -1 for student count)
     * returns the complete list of all universities available in the mock data source.
     */
    @Test
    public void testEmptySearchReturnsAllUniversities() {
        // Perform a search with no specific criteria
        List<University> results = searchController.search("", -1);

        // Compare the size of the result list with the cached full list
        Assert.assertEquals("An empty search should return all universities from the mock data source",
                allMockUniversities.size(), results.size());

        // Optional but recommended: Compare the content (names) to ensure the lists contain the same universities
         Assert.assertEquals("The set of universities from an empty search should match the full mock set",
                 getUniversityNames(allMockUniversities), getUniversityNames(results));
    }

    /**
     * Tests the combination of search criteria, specifically state and student number.
     * It finds a state and student count combination that exists within the mock data
     * and performs a search using both. The test asserts that results are found and
     * that all returned universities match both the specified state and student count.
     */
    @Test
    public void testCombinedSearch() {
        // Find a state and student count pair that co-exists in the mock data
         String state = null;
         int studentCount = -1;
         for (University u : allMockUniversities) {
             // Look for a university with both a valid state and a positive student count
             if (u.getState() != null && !u.getState().equals("-1") && u.getNumStudents() > 0) {
                 state = u.getState();
                 studentCount = u.getNumStudents();
                 break; // Use the first valid combination found
             }
         }
         // Ensure we found a valid combination to test with
         Assert.assertNotNull("Test precondition failed: Mock data needs a school with a valid state and positive student count.", state);
         Assert.assertTrue("Test precondition failed: Mock data needs a school with a valid state and positive student count.", studentCount > 0);


        // Perform the search using both criteria
        List<University> results = searchController.search(state, studentCount);

        // Assert that the combined search yields results (assuming our chosen criteria exist)
        Assert.assertFalse("Combined search for state '" + state + "' and count " + studentCount + " should find at least one result", results.isEmpty());

        // Verify that all results match both specified criteria
        for (University uni : results) {
            Assert.assertEquals("Resulting university's state should match the search criteria", state, uni.getState());
            Assert.assertEquals("Resulting university's student count should match the search criteria", studentCount, uni.getNumStudents());
        }
    }

    /**
     * Verifies that searching for a state name that does not exist in the data source
     * correctly returns an empty list of universities.
     */
    @Test
    public void testNonExistentStateReturnsEmpty() {
        // Perform a search using a state name highly unlikely to exist
        List<University> results = searchController.search("NONEXISTENTSTATE_XYZ123", -1);
        // Assert that the result list is empty
        Assert.assertTrue("Searching for a non-existent state should return an empty list", results.isEmpty());
    }

    // --- New Tests for findSimilar (Comments added) ---

    /**
     * Helper method to locate a specific university within the cached mock list
     * by its exact name. This is useful for setting up tests involving specific
     * universities from the mock data.
     *
     * @param name The exact name of the university to find.
     * @return The {@link University} object if found, or {@code null} if no university
     *         with that name exists in the cached list.
     */
    private University findMockUniversityByName(String name) {
        for (University u : allMockUniversities) {
            if (u.getName().equals(name)) {
                return u;
            }
        }
        return null; // Return null if not found
    }

    /**
     * Tests the {@code findSimilar} method's ability to identify universities
     * that meet the similarity criteria relative to a target university.
     * This test assumes the mock data contains a 'TARGET_U', a 'SIMILAR_U' (which should
     * be found), and a 'DIFFERENT_U' (which should not be found). It verifies that
     * 'SIMILAR_U' is in the results and that 'DIFFERENT_U' and the target itself are not.
     */
    @Test
    public void testFindSimilar_FindsSimilarSchool() {
        // --- Arrange ---
        // Assumptions about mock data are crucial here. Replace placeholder names with actual names from MockDatabaseController.
        final String targetName = "TARGET_U";       // Placeholder: Name of the target school in mock data
        final String similarName = "SIMILAR_U";    // Placeholder: Name of a school expected to be similar
        final String differentName = "DIFFERENT_U"; // Placeholder: Name of a school expected NOT to be similar

        // Get the target university from the mock list
        University target = findMockUniversityByName(targetName);
        Assert.assertNotNull("Test setup error: Mock data must contain '" + targetName + "' for this test.", target);

        // --- Act ---
        // Execute the method under test
        List<University> similarResults = searchController.findSimilar(target);

        // --- Assert ---
        Assert.assertNotNull("The list of similar universities should never be null.", similarResults);
        Assert.assertFalse("Expected to find at least one similar school ('" + similarName + "') based on mock data configuration.", similarResults.isEmpty());

        // Verify the expected similar school is present
        boolean foundSimilar = similarResults.stream().anyMatch(u -> u.getName().equals(similarName));
        Assert.assertTrue("The result list should contain the expected similar university: '" + similarName + "'.", foundSimilar);

        // Verify the non-similar school is absent
         boolean foundDifferent = similarResults.stream().anyMatch(u -> u.getName().equals(differentName));
         Assert.assertFalse("The result list should NOT contain the non-similar university: '" + differentName + "'.", foundDifferent);

         // Verify the target school itself is absent from the results
         boolean foundTarget = similarResults.stream().anyMatch(u -> u.getName().equals(target.getName()));
         Assert.assertFalse("The result list should NOT contain the target university itself.", foundTarget);
    }

    /**
     * Tests the {@code findSimilar} method for a scenario where the target university
     * exists in the mock data, but no other universities meet the similarity criteria.
     * Assumes the mock data includes a 'TARGET_ALONE_U' specifically for this purpose.
     * The test expects an empty list of results.
     */
    @Test
    public void testFindSimilar_NoSimilarSchoolFound() {
         // --- Arrange ---
         // Assumption: Mock data contains a university designed to have no similar matches.
         final String targetAloneName = "TARGET_ALONE_U"; // Placeholder: Name of the isolated target school

         // Get the target university
         University target = findMockUniversityByName(targetAloneName);
         Assert.assertNotNull("Test setup error: Mock data must contain '" + targetAloneName + "' for this test.", target);

         // --- Act ---
         List<University> similarResults = searchController.findSimilar(target);

         // --- Assert ---
         Assert.assertNotNull("The list of similar universities should never be null.", similarResults);
         Assert.assertTrue("Expected an empty list as no schools should be similar to '" + targetAloneName + "' based on mock data.", similarResults.isEmpty());
    }

    /**
     * Tests the behavior of the {@code findSimilar} method when the target university
     * provided is {@code null}. It expects the method to handle this gracefully by
     * returning an empty list rather than throwing an exception.
     */
    @Test
    public void testFindSimilar_NullTarget() {
        // --- Arrange ---
        // Target is explicitly null.

        // --- Act ---
        List<University> similarResults = searchController.findSimilar(null);

        // --- Assert ---
        Assert.assertNotNull("findSimilar(null) should return a non-null list.", similarResults);
        Assert.assertTrue("findSimilar(null) should return an empty list.", similarResults.isEmpty());
    }

     /**
      * Tests if {@code findSimilar} can find matching universities even if the target
      * university object itself is not part of the controller's underlying list (e.g., it's a new object).
      * The test creates an 'external' target object with specific characteristics designed
      * to match a university ('SIMILAR_TO_EXTERNAL_U') present in the mock data. It verifies
      * that this mock university is correctly identified as similar.
      */
     @Test
     public void testFindSimilar_TargetNotInMockList() {
         // --- Arrange ---
         // Assumption: Mock data contains a school ("SIMILAR_TO_EXTERNAL_U") that will match the criteria set below.
         final String expectedSimilarName = "SIMILAR_TO_EXTERNAL_U"; // Placeholder: Name of school in mock data

         // Create a target object that doesn't come from the mock list
         University externalTarget = new University("EXTERNAL_TARGET_INSTANCE");
         // Set properties on this external target to match the criteria of 'SIMILAR_TO_EXTERNAL_U' in the mock data.
         // These values must align with how `findSimilar` calculates similarity and the data in MockDatabaseController.
         externalTarget.setState("SOME_STATE");       // Example criteria
         externalTarget.setLocation("SOME_LOCATION"); // Example criteria
         externalTarget.setControl("SOME_CONTROL");   // Example criteria
         externalTarget.setNumStudents(10000);        // Example value within a similar range
         externalTarget.setSatVerbal(600);            // Example value within a similar range
         externalTarget.setSatMath(600);              // Example value within a similar range
         externalTarget.setPercentAdmitted(50);       // Example value within a similar range
         externalTarget.setScaleAcademics(4);         // Example value within a similar range
         // Ensure at least 3 criteria will match 'SIMILAR_TO_EXTERNAL_U' based on the logic in SearchController.

         // --- Act ---
         List<University> similarResults = searchController.findSimilar(externalTarget);

         // --- Assert ---
         Assert.assertNotNull("findSimilar should return a non-null list even for an external target.", similarResults);
         Assert.assertFalse("Should find similar schools even if the target object isn't from the internal list, provided criteria match.", similarResults.isEmpty());

         // Verify that the expected similar school from the mock data *was* found
         boolean foundExpectedSimilar = similarResults.stream()
                                                     .anyMatch(u -> u.getName().equals(expectedSimilarName));
         Assert.assertTrue("The result list should contain '" + expectedSimilarName + "' as it matches the external target's criteria.", foundExpectedSimilar);
     }


    // --- Helper Method (Comment refined) ---

    /**
     * Utility method to extract the names of universities from a list into a {@link Set}.
     * Using a Set allows for easy comparison of the *contents* of two lists of universities,
     * ignoring order and duplicates. Handles null input list and null universities or names within the list gracefully.
     *
     * @param universities A {@link List} of {@link University} objects. Can be null.
     * @return A {@link Set} containing the non-null names of the universities in the input list.
     *         Returns an empty set if the input list is null or empty, or if universities/names are null.
     */
    private Set<String> getUniversityNames(List<University> universities) {
        Set<String> names = new HashSet<>();
        if (universities != null) { // Check if the list itself is null
            for (University uni : universities) {
                // Check if the university object and its name are not null before adding
                if (uni != null && uni.getName() != null) {
                    names.add(uni.getName());
                }
            }
        }
        return names;
    }
}