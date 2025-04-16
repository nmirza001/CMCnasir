package cmc.backend;

import java.util.ArrayList;
import java.util.List;

// import java.util.stream.Collectors; // Only needed if you want to limit results easily later

import cmc.backend.entities.University;

/**
 * Provides functionality for searching and finding universities based on various criteria.
 * This controller interacts with the {@link UniversityController} to retrieve university data
 * and then filters or analyzes it according to the specific search requirements, including
 * finding schools with similar characteristics.
 *
 * @author Nasir Mirza
 * @version Apr 14, 2025 // Updated version date based on previous file context
 */
public class SearchController {

    private UniversityController universityController;

    /**
     * Constructs a SearchController using a default instance of {@link UniversityController}.
     * This is typically used when the standard data source configuration is sufficient.
     */
    public SearchController() {
        this.universityController = new UniversityController();
    }

    /**
     * Constructs a SearchController with a specific {@link UniversityController}.
     * This constructor is particularly useful for dependency injection, especially during
     * testing where a mock controller (like {@code MockDatabaseController}) might be provided.
     *
     * @param universityController The university controller instance this search controller will use
     *                             to fetch university data.
     */
    public SearchController(UniversityController universityController) {
        this.universityController = universityController;
    }

    /**
     * Performs a basic search for universities based on state and/or student population size.
     * The search for the state name ignores case. Providing an empty string for the state
     * or -1 for the student number effectively disables filtering by that criterion.
     *
     * @param state The name of the state to filter by (case-insensitive). An empty string or null ignores this filter.
     * @param stuNum The exact number of students to filter by. A value less than 0 (typically -1) ignores this filter.
     * @return A {@link List} of {@link University} objects that match all specified criteria.
     *         Returns an empty list if no matches are found or if the underlying data source is empty.
     */
    public List<University> search(String state, int stuNum) {
        List<University> schoolList = universityController.getAllSchools();
        List<University> filteredList = new ArrayList<University>();

        // Normalize the search state to uppercase for consistent, case-insensitive comparison.
        // Safely handles null input for the state parameter.
        String searchState = (state == null) ? "" : state.toUpperCase();

        for (int i = 0; i < schoolList.size(); i++) {
            University uni = schoolList.get(i);

            // Prepare the university's state for comparison, handling potential null values.
            // Assuming database states are consistently stored (e.g., uppercase).
            String uniState = uni.getState() == null ? "" : uni.getState().toUpperCase();

            // Determine if the state criterion should be ignored or if it matches.
            boolean ignoreState = searchState.isEmpty(); // Use isEmpty for clarity
            boolean stateMatch = ignoreState || uniState.equals(searchState);

            // Determine if the student number criterion should be ignored or if it matches.
            boolean ignoreStuNum = stuNum < 0;
            boolean stuNumMatch = ignoreStuNum || uni.getNumStudents() == stuNum;

            // Only add the university to the results if all active criteria are met.
            if (stateMatch && stuNumMatch) {
                filteredList.add(uni);
            }
        }

        return filteredList;
    }

    // <<< NEW METHOD START >>>
    /**
     * Finds universities that are considered "similar" to a given target university.
     * Similarity is determined by comparing several key attributes (like location, control,
     * student size, SAT scores, acceptance rate, and academic scale) against predefined
     * criteria and tolerances. A university is deemed similar if it matches at least a
     * minimum number of these criteria.
     *
     * @param targetUniversity The {@link University} object representing the school to which
     *                         others should be compared. If this is {@code null}, an empty list
     *                         is returned immediately.
     * @return A {@link List} of {@link University} objects considered similar to the target.
     *         This list will not include the {@code targetUniversity} itself. Returns an
     *         empty list if the target is null, no other universities exist, or none meet
     *         the similarity threshold.
     */
    public List<University> findSimilar(University targetUniversity) {
        // Handle the edge case where the target university is not provided.
        if (targetUniversity == null) {
            return new ArrayList<>(); // Return an empty list, as no comparison is possible.
        }

        List<University> allUniversities = universityController.getAllSchools();
        List<University> similarUniversities = new ArrayList<>();

        // --- Define Similarity Criteria ---
        // These values determine how closely attributes must match.
        final double studentSizeTolerance = 0.25; // Allows student count within +/- 25% of the target.
        final double satTolerance = 75;           // Allows combined SAT score within +/- 75 points of the target.
        final double acceptanceRateTolerance = 0.15; // Allows acceptance rate within +/- 15 percentage points (e.g., 40% vs 55%).
        final int academicScaleTolerance = 1;     // Allows academic scale rating within +/- 1 point of the target.
        final int requiredMatches = 3; // Minimum number of criteria that must match for a school to be "similar".

        for (University candidate : allUniversities) {
            // Ensure we don't compare a university to itself. Check based on name, assuming names are unique identifiers here.
            // Added null check for targetUniversity name for robustness, although handled by the initial null check.
            if (targetUniversity.getName() != null &&
                targetUniversity.getName().equals(candidate.getName())) {
                continue; // Skip to the next candidate.
            }

            int similarityScore = 0; // Tracks how many criteria match for the current candidate.
            // int criteriaConsidered = 0; // Not strictly needed for the current logic but could be useful for debugging/analysis.

            // --- Compare Criteria ---
            // Each comparison block checks if both target and candidate have valid data for the criterion
            // before attempting the comparison. It increments the score if they match.

            // Criterion 1: Location Type (e.g., SUBURBAN, URBAN, SMALL-CITY) - Requires exact match.
            if (isValidValue(targetUniversity.getLocation()) && isValidValue(candidate.getLocation())) {
                // criteriaConsidered++;
                if (targetUniversity.getLocation().equals(candidate.getLocation())) {
                    similarityScore++;
                }
            }

            // Criterion 2: Control (e.g., PRIVATE, STATE, CITY) - Requires exact match.
            if (isValidValue(targetUniversity.getControl()) && isValidValue(candidate.getControl())) {
                // criteriaConsidered++;
                if (targetUniversity.getControl().equals(candidate.getControl())) {
                    similarityScore++;
                }
            }

            // Criterion 3: State - Requires exact match.
            if (isValidValue(targetUniversity.getState()) && isValidValue(candidate.getState())) {
                // criteriaConsidered++;
                if (targetUniversity.getState().equals(candidate.getState())) {
                    similarityScore++;
                }
            }

            // Criterion 4: Number of Students - Checks if candidate's size is within the tolerance range of the target.
            // Requires both schools to have a positive student count.
            if (targetUniversity.getNumStudents() > 0 && candidate.getNumStudents() > 0) {
                // criteriaConsidered++;
                double lowerBound = targetUniversity.getNumStudents() * (1.0 - studentSizeTolerance);
                double upperBound = targetUniversity.getNumStudents() * (1.0 + studentSizeTolerance);
                if (candidate.getNumStudents() >= lowerBound && candidate.getNumStudents() <= upperBound) {
                    similarityScore++;
                }
            }

            // Criterion 5: Combined SAT Score (Verbal + Math) - Checks if scores are within tolerance.
            // Requires both schools to have a plausible combined score (>= 400). Assumes -1 indicates missing data.
            double targetSat = targetUniversity.getSatVerbal() + targetUniversity.getSatMath();
            double candidateSat = candidate.getSatVerbal() + candidate.getSatMath();
            // Check if scores seem valid (not resulting from default -1 values)
            if (targetSat >= 400 && candidateSat >= 400) { // Basic validity check for combined score
                // criteriaConsidered++;
                if (Math.abs(targetSat - candidateSat) <= satTolerance) {
                    similarityScore++;
                }
            }

            // Criterion 6: Acceptance Rate (% admitted) - Checks if rates are within tolerance.
            // Requires both schools to have a non-negative acceptance rate (0% to 100%). Assumes -1 indicates missing data.
            if (targetUniversity.getPercentAdmitted() >= 0 && candidate.getPercentAdmitted() >= 0) {
                // criteriaConsidered++;
                 // Convert percentage points tolerance (0.15) to the scale of getPercentAdmitted (0-100)
                if (Math.abs(targetUniversity.getPercentAdmitted() - candidate.getPercentAdmitted()) <= (acceptanceRateTolerance * 100.0)) {
                    similarityScore++;
                }
            }

            // Criterion 7: Academic Scale (typically 1-5) - Checks if ratings are within tolerance.
            // Requires both schools to have a positive academic scale rating. Assumes -1 indicates missing data.
            if (targetUniversity.getScaleAcademics() > 0 && candidate.getScaleAcademics() > 0) {
                // criteriaConsidered++;
                if (Math.abs(targetUniversity.getScaleAcademics() - candidate.getScaleAcademics()) <= academicScaleTolerance) {
                    similarityScore++;
                }
            }

            // --- Decision ---
            // If the candidate meets the minimum number of required matches, add it to the results.
            if (similarityScore >= requiredMatches) {
                similarUniversities.add(candidate);
            }
        }

        return similarUniversities;
    }

    /**
     * A private helper method to determine if a String attribute from a {@link University}
     * object contains meaningful data for comparison purposes. It checks if the value
     * is not null, not an empty string, and not the common placeholder "-1" often used
     * to indicate missing or inapplicable data.
     *
     * @param value The String value retrieved from a University attribute (e.g., location, control, state).
     * @return {@code true} if the string is considered a valid, meaningful value for comparison;
     *         {@code false} otherwise.
     */
    private boolean isValidValue(String value) {
        // A value is valid if it exists, is not empty, and isn't the "-1" placeholder.
        return value != null && !value.isEmpty() && !value.equals("-1");
    }
    // <<< NEW METHOD END >>>

    /**
     * Placeholder for a more advanced search method intended for future development.
     * This method could potentially accept a map of various criteria (beyond just state
     * and student number) for more complex filtering needs.
     *
     * @param criteria A map where keys are attribute names (e.g., "location", "minSAT")
     *                 and values are the desired search values. (Currently not implemented).
     * @return A list of universities matching the advanced criteria. (Currently returns an empty list).
     */
    /* For future extension:
    public List<University> advancedSearch(Map<String, Object> criteria) {
        // Actual implementation would parse the map and apply multiple filters.
        System.out.println("Advanced search called with criteria: " + criteria); // Example placeholder action
        return new ArrayList<University>(); // Return empty list until implemented
    }
    */
}