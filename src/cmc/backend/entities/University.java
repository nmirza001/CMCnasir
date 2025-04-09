package cmc.backend.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a University entity.
 * @author Roman Lefler
 * @version Mar 13, 2025
 */
public class University {

	private final String name;
	
	private String state = "-1";
	private String location = "-1";
	private String control = "-1";
	
	private int numStudents = -1;
	private int numApplicants = -1;
	private int scaleAcademics = -1;
	private int scaleSocial = -1;
	private int scaleQualityOfLife = -1;
	
	private double percentFemale = -1d;
	private double satVerbal = -1d;
	private double satMath = -1d;
	private double expenses = -1d;
	private double percentFinancialAid = -1d;
	private double percentAdmitted = -1d;
	private double percentEnrolled = -1d;
	
	private final ArrayList<String> emphases = new ArrayList<>();
	
	private String webpageUrl;
	private String imageUrl;
	
	/**
	 * Creates a university with a name and otherwise unknown information.
	 * @param name The name of the university. Should be all caps and non-null.
	 * @throws IllegalArgumentException if name is not all caps or is null
	 */
	public University(String name) {
		ensureCaps(name);
		this.name = name;
	}
	
	/**
	 * Ensures that a given value is -1 or between the given range.
	 * @param lo Inclusive minimum
	 * @param x Test value
	 * @param hi Inclusive maximum
	 * @throws IllegalArgumentException If x is not -1 and not within range
	 */
	private static void ensure(double lo, double x, double hi) {
		
		if(x != -1 && (x < lo || x > hi)) {
			String msg = String.format("Number %d not within [%d, %d].", x, lo, hi);
			throw new IllegalArgumentException(msg);
		}
	}
	
	/**
	 * Ensures that a string is all-caps and not null.
	 * @param s Given string
	 * @throws IllegalArgumentException If s is null or not equal to itself when {@link String#toUpperCase()}
	 */
	private static void ensureCaps(String s) {
		if(!isAllCaps(s))
			throw new IllegalArgumentException("'" + s + "' must be in all caps.");
	}
	
	/**
	 * Checks if a string is all-caps and not null.
	 * @param s Given string
	 * @return {@code true} if not null and equal to it's {@link String#toUpperCase()}
	 */
	private static boolean isAllCaps(String s) {
		if(s == null) throw new IllegalArgumentException("String cannot be null.");
		return s.toUpperCase().equals(s);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		ensureCaps(state);
		this.state = state;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		ensureCaps(location);
		this.location = location;
	}

	/**
	 * @return the control
	 */
	public String getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(String control) {
		ensureCaps(control);
		this.control = control;
	}

	/**
	 * @return the numStudents
	 */
	public int getNumStudents() {
		return numStudents;
	}

	/**
	 * @param numStudents the numStudents to set
	 */
	public void setNumStudents(int numStudents) {
		ensure(0, numStudents, Integer.MAX_VALUE);
		this.numStudents = numStudents;
	}

	/**
	 * @return the numApplicants
	 */
	public int getNumApplicants() {
		return numApplicants;
	}

	/**
	 * @param numApplicants the numApplicants to set
	 */
	public void setNumApplicants(int numApplicants) {
		ensure(0, numStudents, Integer.MAX_VALUE);
		this.numApplicants = numApplicants;
	}

	/**
	 * @return the scaleAcademics
	 */
	public int getScaleAcademics() {
		return scaleAcademics;
	}

	/**
	 * @param scaleAcademics the scaleAcademics to set
	 */
	public void setScaleAcademics(int scaleAcademics) {
		ensure(0, scaleAcademics, 5);
		this.scaleAcademics = scaleAcademics;
	}

	/**
	 * @return the scaleSocial
	 */
	public int getScaleSocial() {
		return scaleSocial;
	}

	/**
	 * @param scaleSocial the scaleSocial to set
	 */
	public void setScaleSocial(int scaleSocial) {
		ensure(0, scaleSocial, 5);
		this.scaleSocial = scaleSocial;
	}

	/**
	 * @return the scaleQualityOfLife
	 */
	public int getScaleQualityOfLife() {
		return scaleQualityOfLife;
	}

	/**
	 * @param scaleQualityOfLife the scaleQualityOfLife to set
	 */
	public void setScaleQualityOfLife(int scaleQualityOfLife) {
		ensure(0, scaleQualityOfLife, 5);
		this.scaleQualityOfLife = scaleQualityOfLife;
	}

	/**
	 * @return the percentFemale
	 */
	public double getPercentFemale() {
		return percentFemale;
	}

	/**
	 * @param percentFemale the percentFemale to set
	 */
	public void setPercentFemale(double percentFemale) {
		ensure(0d, percentFemale, 100d);
		this.percentFemale = percentFemale;
	}

	/**
	 * @return the satVerbal
	 */
	public double getSatVerbal() {
		return satVerbal;
	}

	/**
	 * @param satVerbal the satVerbal to set
	 */
	public void setSatVerbal(double satVerbal) {
		ensure(200d, satVerbal, 800d);
		this.satVerbal = satVerbal;
	}

	/**
	 * @return the satMath
	 */
	public double getSatMath() {
		return satMath;
	}

	/**
	 * @param satMath the satMath to set
	 */
	public void setSatMath(double satMath) {
		ensure(200d, satMath, 800d);
		this.satMath = satMath;
	}

	/**
	 * @return the expenses
	 */
	public double getExpenses() {
		return expenses;
	}

	/**
	 * @param expenses the expenses to set
	 */
	public void setExpenses(double expenses) {
		ensure(0d, expenses, Double.POSITIVE_INFINITY);
		this.expenses = expenses;
	}

	/**
	 * @return the percentFinancialAid
	 */
	public double getPercentFinancialAid() {
		return percentFinancialAid;
	}

	/**
	 * @param percentFinancialAid the percentFinancialAid to set
	 */
	public void setPercentFinancialAid(double percentFinancialAid) {
		ensure(0d, percentFinancialAid, 100d);
		this.percentFinancialAid = percentFinancialAid;
	}

	/**
	 * @return the percentAdmitted
	 */
	public double getPercentAdmitted() {
		return percentAdmitted;
	}

	/**
	 * @param percentAdmitted the percentAdmitted to set
	 */
	public void setPercentAdmitted(double percentAdmitted) {
		ensure(0d, percentAdmitted, 100d);
		this.percentAdmitted = percentAdmitted;
	}

	/**
	 * @return the percentEnrolled
	 */
	public double getPercentEnrolled() {
		return percentEnrolled;
	}

	/**
	 * @param percentEnrolled the percentEnrolled to set
	 */
	public void setPercentEnrolled(double percentEnrolled) {
		ensure(0d, percentEnrolled, 100d);
		this.percentEnrolled = percentEnrolled;
	}
	
	/**
	 * @return the webpageUrl
	 */
	public String getWebpageUrl() {
		return webpageUrl;
	}

	/**
	 * @param webpageUrl the webpageUrl to set
	 */
	public void setWebpageUrl(String webpageUrl) {
		this.webpageUrl = webpageUrl;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void addEmphasis(String e) {
		emphases.add(e);
	}
	
	public boolean removeEmphasis(String e) {
		return emphases.remove(e);
	}
	
	public List<String> getEmphases() {
		return emphases;
	}
	
	/**
	 * Checks if a given name is a valid university name.
	 * @param name Name to test
	 * @return {@code true} if valid
	 */
	public static boolean isValidName(String name) {
		return isAllCaps(name);
	}
	
}
