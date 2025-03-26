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

import cmc.backend.entities.University;
import dblibrary.project.csci230.UniversityDBLibrary;

/**
 * 
 */
public class UniversityController {

	
	private DatabaseController db;
	
	private UniversityDBLibrary database;

	private static final String SHOULDNT_HAPPEN = "If you're seeing this DatabaseController has a bug.";
	
	public UniversityController() {
		this.db = new DatabaseController();
		this.database = new UniversityDBLibrary("dei", "Csci230$");
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
		int result = database.university_addUniversity(
				u.getName(), u.getState(), u.getLocation(), u.getControl(),
				u.getNumStudents(), u.getPercentFemale(), u.getSatVerbal(),
				u.getSatMath(), u.getExpenses(), u.getPercentFinancialAid(),
				u.getNumApplicants(), u.getPercentAdmitted(),
				u.getPercentEnrolled(), u.getScaleAcademics(),
				u.getScaleSocial(), u.getScaleQualityOfLife());
		
		if(result != 1) return false;
		
		String uniName = u.getName();
		for(String e : u.getEmphases()) {
			if(!db.addEmphasis(uniName, e)) throw new IllegalStateException(SHOULDNT_HAPPEN);
		}
		
		return true;
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
		
		if(u == null) throw new IllegalArgumentException("u cannot be null.");
		
		String uniName = u.getName();
		// Since u's emphasis list can be out of sync with the database's
		// emphases, we must rely on database's reported emphases
		Map<String, List<String>> emphasesDict = getUniversitiesEmphases();
		List<String> emphases = emphasesDict.get(uniName);
		if(emphases != null) {
			for(String k : u.getEmphases()) {
				
				if(!db.removeEmphasis(uniName, k)) throw new IllegalStateException(SHOULDNT_HAPPEN);
			}
		}
		
		int result = database.university_deleteUniversity(u.getName());
		return result > 0;
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
		
		// # Emphases:
		// Anything that is present in the database but not u
		// was removed.
		// Anything that is present in u but not in the database
		// was added.
		String uniName = u.getName();
		University old = University.find(getAllSchools(), uniName);
		List<String> oldE = old.getEmphases();
		List<String> newE = u.getEmphases();
		// Create a total set of all emphases
		Set<String> both = new HashSet<>(oldE);
		both.addAll(newE);
		// Iterate through the total set of all emphases
		for(String em : both) {
			if(oldE.contains(em)) {
				// In old but not new; Newly removed
				if(!newE.contains(em)) {
					if(!db.removeEmphasis(uniName, em)) throw new IllegalStateException(SHOULDNT_HAPPEN);
				}
			}
			// Not in old (and therefore must be in new); Newly added
			else {
				if(!db.addEmphasis(uniName, em)) throw new IllegalStateException(SHOULDNT_HAPPEN);
			}
		}
		
		// Here's the easy part
		int result = database.university_editUniversity(
				u.getName(), u.getState(), u.getLocation(), u.getControl(),
				u.getNumStudents(), u.getPercentFemale(), u.getSatVerbal(),
				u.getSatMath(), u.getExpenses(), u.getPercentFinancialAid(),
				u.getNumApplicants(), u.getPercentAdmitted(),
				u.getPercentEnrolled(), u.getScaleAcademics(),
				u.getScaleSocial(), u.getScaleQualityOfLife());
		
		return result == 1;
	}
	
	/**
	 * Gets the list of all the universities in the DB
	 * @return A list of universities
	 * @author Roman Lefler
	 * @version Mar 13, 2025
	 */
	public List<University> getAllSchools() {
		String[][] dbUniversityList = this.database.university_getUniversities();

		Map<String, List<String>> emphases = getUniversitiesEmphases();
		ArrayList<University> result = new ArrayList<>();
		for (String[] k : dbUniversityList) {
			University u = new University(k[0]);
			u.setState(k[1]);
			u.setLocation(k[2]);
			u.setControl(k[3]);
			u.setNumStudents(Integer.parseInt(k[4]));
			u.setPercentFemale(Double.parseDouble(k[5]));
			u.setSatVerbal(Double.parseDouble(k[6]));
			u.setSatMath(Double.parseDouble(k[7]));
			u.setExpenses(Double.parseDouble(k[8]));
			u.setPercentFinancialAid(Double.parseDouble(k[9]));
			u.setNumApplicants(Integer.parseInt(k[10]));
			u.setPercentAdmitted(Double.parseDouble(k[11]));
			u.setPercentEnrolled(Double.parseDouble(k[12]));
			u.setScaleAcademics(Integer.parseInt(k[13]));
			u.setScaleSocial(Integer.parseInt(k[14]));
			u.setScaleQualityOfLife(Integer.parseInt(k[15]));
			List<String> schoolEmphases = emphases.get(k[0]);
			if(schoolEmphases != null) {
				for(String e : schoolEmphases) u.addEmphasis(e);
			}
			
			result.add(u);
		}

		return result;
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
		String[][] emphases = database.university_getNamesWithEmphases();
		Map<String, List<String>> dict = new HashMap<String, List<String>>();
		for(String[] kv : emphases) {
			
			List<String> list = dict.get(kv[0]);
			if(list == null) {
				list = new ArrayList<>(3);
				dict.put(kv[0], list);
			}
			list.add(kv[1]);
		}
		return dict;
	}
		
}
