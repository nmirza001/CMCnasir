package cmc.frontend;

import java.util.Scanner;

import cmc.backend.entities.University;

/**
 * TUI to take information from an admin to add a school.
 * @author Roman Lefler
 * @version Mar 11, 2025
 */
public class AdminAddSchool {
	
	/**
	 * Tries to get the next line of a scanner, but returns {@code null}
	 * if the line is "EXIT"
	 */
	private static String tryLine(Scanner s) {
		String line = s.nextLine();
		return "EXIT".equals(line) ? null : line;
	}
	
	/**
	 * Parses an int.
	 * @param s The string to parse.
	 * @return Parses an int or returns -1 if invalid.
	 */
	private static int tryParseInt(String s) {
		try {
			return Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
	}
	
	/**
	 * Prompts the user for school information with std I/O and returns
	 * a created university.
	 * Note that this method does NOT interact with the DB at all.
	 * @param s A scanner to read input from.
	 * @return A university or {@code null} if the user aborts.
	 * @author Roman Lefler
	 * @version Mar 13, 2025
	 */
	public static University prompt(Scanner s) {
		
		System.out.println("Fill out the university's information or");
		System.out.println("enter 'EXIT' at any point to abort.\n");
		
		System.out.print("Name: ");
		String name = tryLine(s);
		if(name == null) return null;
		name = name.toUpperCase();
		
		System.out.println("Put a state or 'FOREIGN'");
		System.out.print("State: ");
		String state = tryLine(s);
		if(state == null) return null;
		state = state.toUpperCase();
		
		
		String location = null;
		// This loop will only repeat if it is given invalid input
		do {
			String[] friendly = new String[] { "Urban", "Suburban", "Small-City", "Unknown" };
			String[] options = new String[] { "URBAN", "SUBURBAN", "SMALL-CITY", "-1" };
			
			int chosen = listPrompt("Location: ", friendly, s);
			if(chosen == -2) return null;
			else if(chosen == -1) System.out.println("Pick a number 1-4!");
			else location = options[chosen];
		}
		while(location == null);
		
		String control = null;
		do {
			String[] friendly = new String[] { "State", "City", "Private", "Unknown" };
			String[] options = new String[] { "STATE", "CITY", "PRIVATE", "-1" };
			
			int chosen = listPrompt("Location: ", friendly, s);
			if(chosen == -2) return null;
			else if(chosen == -1) System.out.println("Pick a number 1-4!");
			else control = options[chosen];
		}
		while(control == null);
		
		System.out.println("Just press enter for any of the following if unknown.");
		NumberItem[] numQs = new NumberItem[] {
			new NumberItem("# of Students: ", 0, Integer.MAX_VALUE, false),
			new NumberItem("% Female: ", 0, 100),
			new NumberItem("Verbal SAT Score: ", 200, 800),
			new NumberItem("Math SAT Score: ", 200, 800),
			new NumberItem("Expenses", 0, Double.MAX_VALUE),
			new NumberItem("% Financial Aid: ", 0, 100),
			new NumberItem("# of Applicants:", 0, Integer.MAX_VALUE, false),
			new NumberItem("% Admitted: ", 0, 100),
			new NumberItem("% Enrolled: ", 0, 100),
			new NumberItem("Academics Scale: ", 0, 5, false),
			new NumberItem("Social Scale: ", 0, 5, false),
			new NumberItem("Quality of Life Scale: ", 0, 5, false)
		};
		String[] numAns = queryAll(numQs, s);
		if(numAns == null) return null;
		
		University u = new University(name);
		u.setState(state);
		u.setLocation(location);
		u.setControl(control);
		u.setNumStudents(Integer.parseInt(numAns[0]));
		u.setPercentFemale(Double.parseDouble(numAns[1]));
		u.setSatVerbal(Double.parseDouble(numAns[2]));
		u.setSatMath(Double.parseDouble(numAns[3]));
		u.setExpenses(Double.parseDouble(numAns[4]));
		u.setPercentFinancialAid(Double.parseDouble(numAns[5]));
		u.setNumApplicants(Integer.parseInt(numAns[6]));
		u.setPercentAdmitted(Double.parseDouble(numAns[7]));
		u.setPercentEnrolled(Double.parseDouble(numAns[8]));
		u.setScaleAcademics(Integer.parseInt(numAns[9]));
		u.setScaleSocial(Integer.parseInt(numAns[10]));
		u.setScaleQualityOfLife(Integer.parseInt(numAns[11]));
		
		return u;
	}
	
	/**
	 * Information to use with {@link AdminAddSchool#queryAll}.
	 */
	static class NumberItem {
		
		public NumberItem(String q, double lo, double hi) {
			this(q, lo, hi, true);
		}
		
		public NumberItem(String q, double lo, double hi, boolean isDouble) {
			question = q;
			min = lo;
			max = hi;
			this.isDouble = isDouble;
		}
		
		protected String question;
		protected double min;
		protected double max;
		protected boolean isDouble;
	}
	
	/**
	 * Asks all questions and returns an array with their answers
	 * as numbers in an array corresponding to the inputed array.
	 * @author Roman Lefler
	 * @version Mar 13, 2025
	 */
	private static String[] queryAll(NumberItem[] items, Scanner s) {
		int sz = items.length;
		String[] output = new String[sz];
		
		for(int i = 0; i < sz; i++) {
			NumberItem k = items[i];
			System.out.println(k.question);
			
			String input = tryLine(s);
			if(input == null) return null;
			double num = numInRange(input, k.min, k.max);
			// If it's an int type truncate it
			output[i] = k.isDouble ? num + "" : (int)num + "";
		}
		
		return output;
	}
	
	/**
	 * Returns the number from a string if it's in range or
	 * -1 if out of range or invalid.
	 * @param s Input string
	 * @param lo Minimum inclusive
	 * @param hi Maximum inclusive
	 * @return Number in range or -1.
	 */
	private static double numInRange(String s, double lo, double hi) {
		int num = tryParseInt(s);
		if(num < lo || num > hi) return -1;
		else return num;
	}
	
	/**
	 * Prompts the user to pick an item of a list by number.
	 * The user's options will start at 1 but the return value
	 * is zero-indexed.
	 * @param question The question to print (e.g. "Location: ")
	 * @param friendlyOptions User-friendly names to put by the numbers
	 * @param sc Scanner to query
	 * @return The picked item's index, -1 if invalid, or -2 if canceled (received
	 *         EOF).
	 */
	private static int listPrompt(String question, String[] friendlyOptions, Scanner sc) {
		
		int sz = friendlyOptions.length;
		
		for(int i = 0; i < sz; i++) {
			String msg = String.format("%d) %s", i + 1, friendlyOptions[i]);
			System.out.println(msg);
		}
		
		System.out.print("\n" + question);
		
		String given = tryLine(sc);
		if(given == null) return -2;
		
		int num = tryParseInt(given);
		if(num <= 0 || num > sz) return -1;
		else return num - 1;
		
	}
	
}
