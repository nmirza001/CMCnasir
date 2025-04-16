package cmc.frontend;

import java.util.Scanner;

import cmc.backend.entities.University;

public class AdminEditSchool{
	
	/*
	 * Gets an existing value from a scanner entry
	 * if the line is blank or "Exit" abort
	 * @return entries to uppercase
	 * @author Rick Masaana
	 * borrowed code from Roman's structure
	 * 3/25/2025
	 */
	public static String tryLineExist(Scanner s, String enterValue){
		String line = s.nextLine();
		System.out.println("(" + enterValue + "): ");
		if ("EXIT".equals(line)) return null;
		return line.trim().isEmpty() ? enterValue : line.toUpperCase();
	}
	
	/*
	 * serves as the method for editing and enacting changes
	 * @param Scanner s gets the user input
	 * @param University dataBaseUni gets the information from a University
	 * @return edittedUniversity gives an object with the changes user made or null
	 * @author Rick Masaana
	 * inspired code from Roman's structure
	 * 3/25/2025
	 */
	public static University prompt(Scanner s, University dataBaseUni){
		
		System.out.println("Enter the updated info for " + dataBaseUni + " or ");
		System.out.println("press Enter to keep the current value, or 'EXIT' at any point to abort.\n");
		
		String name = dataBaseUni.getName().toUpperCase();
		System.out.print("Name: " + name); //the name of the University being edited
		
		System.out.print(" State: ");
		String state = tryLineExist(s, dataBaseUni.getState());
		if(state == null) return null;
		state = state.toUpperCase();
		
		
		String location = null;
		do {
			String[] friendly = new String[] { "Urban", "Suburban", "Small-City", "Unknown" };
			String[] options = new String[] { "URBAN", "SUBURBAN", "SMALL-CITY", "-1" };
			
			int chosen = listPromptExist("Location: ", friendly, s, dataBaseUni.getLocation());
			if(chosen == -2) return null;
			else if(chosen == -1) System.out.println("Pick a number 1-4!");
			else location = options[chosen];
		} while (location == null);
		
		//each of the below is meant to retrieve and edit school info except the name since you are editing that school
		
		System.out.println(" Control: ");
		String control = tryLineExist (s, dataBaseUni.getControl());
		if (control == null) return null;
		
		System.out.println(" # of Students: ");
		String numberStudentsString = tryLineExist (s, String.valueOf(dataBaseUni.getNumStudents()));
		if (numberStudentsString == null) return null;
		int numberStudents = numberStudentsString.equals(String.valueOf(dataBaseUni.getNumStudents())) ? dataBaseUni.getNumStudents(): Integer.parseInt(numberStudentsString);
		
		System.out.println(" % of Female: ");
		String percentFemaleString = tryLineExist (s, String.valueOf(dataBaseUni.getPercentFemale()));
		if (percentFemaleString == null) return null;
		double percentFemale = percentFemaleString.equals(String.valueOf(dataBaseUni.getPercentFemale())) ? dataBaseUni.getPercentFemale(): Double.parseDouble(percentFemaleString);
		
		System.out.println(" Verbal SAT score: ");
		String verbalScoreString = tryLineExist (s, String.valueOf(dataBaseUni.getSatVerbal()));
		if (verbalScoreString == null) return null;
		double satVerbal = verbalScoreString.equals(String.valueOf(dataBaseUni.getSatVerbal())) ? dataBaseUni.getSatVerbal(): Double.parseDouble(verbalScoreString);
		
		System.out.println(" Math SAT score: ");
		String mathScoreString = tryLineExist (s, String.valueOf(dataBaseUni.getSatMath()));
		if (mathScoreString == null) return null;
		double satMath = mathScoreString.equals(String.valueOf(dataBaseUni.getSatMath())) ? dataBaseUni.getSatMath(): Double.parseDouble (mathScoreString);
		
		
		System.out.println(" Expenses: ");
		String expensesString = tryLineExist (s, String.valueOf(dataBaseUni.getExpenses()));
		if (expensesString == null) return null;
		double expenses = expensesString.equals(String.valueOf(dataBaseUni.getExpenses())) ? dataBaseUni.getExpenses(): Double.parseDouble(expensesString);
		
		System.out.println(" % of Financial Aid: ");
		String percentFinancialAidString = tryLineExist (s, String.valueOf(dataBaseUni.getPercentFinancialAid()));
		if (percentFinancialAidString == null) return null;
		double percentFinancialAid = percentFinancialAidString.equals(String.valueOf(dataBaseUni.getPercentFinancialAid())) ? dataBaseUni.getPercentFinancialAid(): Double.parseDouble(percentFinancialAidString);
		
		System.out.println(" # of Applicants: ");
		String applicantsString = tryLineExist (s, String.valueOf(dataBaseUni.getNumApplicants()));
		if (applicantsString == null) return null;
		int applicants = applicantsString.equals(String.valueOf(dataBaseUni.getNumApplicants())) ? dataBaseUni.getNumApplicants(): Integer.parseInt(applicantsString);
		
		System.out.println(" % Admitted: ");
		String percentAdmittedString = tryLineExist (s, String.valueOf(dataBaseUni.getPercentAdmitted()));
		if (percentAdmittedString == null) return null;
		double percentAdmitted = percentAdmittedString.equals(String.valueOf(dataBaseUni.getPercentAdmitted())) ? dataBaseUni.getPercentAdmitted(): Double.parseDouble (percentAdmittedString);
		
		System.out.println(" % Enrolled: ");
		String percentEnrolledString = tryLineExist (s, String.valueOf(dataBaseUni.getPercentEnrolled()));
		if (percentEnrolledString == null) return null;
		double percentEnrolled = percentEnrolledString.equals(String.valueOf(dataBaseUni.getPercentEnrolled())) ? dataBaseUni.getPercentEnrolled(): Double.parseDouble(percentFemaleString);
		
		System.out.println(" Academic Scale: ");
		String academicScaleString = tryLineExist (s, String.valueOf(dataBaseUni.getScaleAcademics()));
		if (academicScaleString == null) return null;
		int academicScale = academicScaleString.equals(String.valueOf(dataBaseUni.getScaleAcademics())) ? dataBaseUni.getScaleAcademics(): Integer.parseInt(academicScaleString);
		
		System.out.println(" Social Scale: ");
		String socialScaleString = tryLineExist (s, String.valueOf(dataBaseUni.getScaleSocial()));
		if (socialScaleString == null) return null;
		int socialScale = socialScaleString.equals(String.valueOf(dataBaseUni.getScaleSocial())) ? dataBaseUni.getScaleSocial(): Integer.parseInt(socialScaleString);
		
		System.out.println(" Quality of Life Scale: ");
		String qualLifeScaleString = tryLineExist (s, String.valueOf(dataBaseUni.getScaleQualityOfLife()));
		if (qualLifeScaleString == null) return null;
		int qualLifeScale = qualLifeScaleString.equals(String.valueOf(dataBaseUni.getScaleQualityOfLife())) ? dataBaseUni.getScaleQualityOfLife(): Integer.parseInt(qualLifeScaleString);
		
		University edittedUniversity = new University(name);
		edittedUniversity.setState(state);
		edittedUniversity.setLocation(location);
		edittedUniversity.setControl(control);
		edittedUniversity.setNumStudents(numberStudents);
		edittedUniversity.setPercentFemale(percentFemale);
		edittedUniversity.setSatVerbal(satVerbal);
		edittedUniversity.setSatMath(satMath);
		edittedUniversity.setExpenses(expenses);
		edittedUniversity.setPercentFinancialAid(percentFinancialAid);
		edittedUniversity.setNumApplicants(applicants);
		edittedUniversity.setPercentAdmitted(percentAdmitted);
		edittedUniversity.setPercentEnrolled(percentEnrolled);
		edittedUniversity.setScaleAcademics(academicScale);
		edittedUniversity.setScaleSocial(socialScale);
		edittedUniversity.setScaleQualityOfLife(qualLifeScale);
		
		return edittedUniversity;
	}
	
	private static int listPromptExist(String question, String[] friendlyOptions, Scanner sc, String existingVal) {
		
		int sz = friendlyOptions.length;
		
		for(int i = 0; i < sz; i++) {
			String msg = String.format("%d) %s", i + 1, friendlyOptions[i]);
			System.out.println(msg);
		}
		
		System.out.print("\n" + question + "(current: " + existingVal + "): ");
		
		String given = tryLine(sc);
		if(given == null) return -2;
		if (given.trim().isEmpty()) {
			for (int i = 0; i < friendlyOptions.length; i++){
				if (friendlyOptions[i].equalsIgnoreCase(existingVal));
				return i;
			}
			return -1;
		}
		
		int num = tryParseInt(given);
		if(num <= 0 || num > sz) return -1;
		else return num - 1;
		
	}
	
	public static String tryLine(Scanner s){
		String line = s.nextLine();
		return "Exit".equals(line) ? null : line;
	}
	
	public static int tryParseInt(String s){
		try {
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e){
			return -1;
		}
	}
}
