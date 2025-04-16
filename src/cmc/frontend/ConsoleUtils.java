package cmc.frontend;

import java.util.List;
import java.util.Scanner;

/**
 * Utilities for dealing with stdio.
 */
public class ConsoleUtils {
	
	/**
	 * Get the selected menu option based on user entry.
	 * This reads one line from the provided Scanner.
	 * 
	 * @param s the Scanner from which to read the user's input
	 * @param minChoice the minimum allowed option (inclusive)
	 * @param maxChoice the maximum allowed option (inclusive)
	 * @return the selected integer, or -1 if invalid input is entered
	 * @author Peter Ohmann
	 */
	public static int getSingleMenuEntry(Scanner s, int minChoice, int maxChoice) {
		String choice = s.nextLine();
		try {
			int numChoice = Integer.parseInt(choice);
			if (numChoice < minChoice || numChoice > maxChoice)
				throw new NumberFormatException("Invalid selection");
			return numChoice;
		}
		catch (Exception e) {
			// here if either a non-integer is entered or it is outside
			// the legal range of values (per min/maxChoice)
			return -1;
		}
	}
	
	/**
	 * Get the selected menu option based on user entry.
	 * This reads lines from the provided Scanner until the user enters
	 * a menu option (number) that matches one of the options provided
	 * (i.e., is between 1 and the number of options).
	 * 
	 * @param s the Scanner from which to read the user's input
	 * @param options the menu options the user has
	 * @return the selected menu option, as an integer (1 more than the
	 * position in the options array)
	 * @author Peter Ohmann
	 */
	public static int getMenuOption(Scanner s, List<String> options) {		
		int choice = -1;
		while (choice == -1) {
			System.out.println("Choose an option:");
			for (int i = 0; i < options.size(); i++) {
				System.out.println((i+1) + ": " + options.get(i));
			}
			choice = getSingleMenuEntry(s, 1, options.size());
			if (choice == -1)
				System.out.println("Invalid option.");
		}
		
		return choice;
	}
	/**
	 * Reads integer from s and handles non-integer inputs
	 * 
	 * @param Scanner s gets the input that is being read
	 * @return integer entered by the user, or -1 if non-integer input given.
	 * @author Rick Masaana
	 * @version 4/15/2025
	 */
	public static int tryGetInt(Scanner s){
		try {
			return s.nextInt();
		}
		catch(java.util.InputMismatchException e){
			System.out.println("invalid input. Please enter a valid number");
			s.next();
			return -1; //failed val
		}finally {
			//finally insures that this part is always executed 
			//regardless of try or catch
			if (s.hasNextLine()){
				s.nextLine();
			}
		}
		
	}

}
