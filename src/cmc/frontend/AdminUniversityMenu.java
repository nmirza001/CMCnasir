package cmc.frontend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cmc.backend.entities.University;

/**
 * The admin menu for universities.
 * @author Roman Lefler
 * @version Mar 24, 2025
 */
public class AdminUniversityMenu {
	
	private final AdminInteraction ui;
	
	public AdminUniversityMenu(AdminInteraction ui) {
		this.ui = ui;
	}
	
	public void prompt(Scanner s) {
		while(promptCycle(s)) ;
	}
	
	/**
	 * This will keep running within {@link #prompt(Scanner)}
	 * till it returns {@code false}.
	 */
	private boolean promptCycle(Scanner s) {
		
		int choice = ConsoleUtils.getMenuOption(s, Arrays.asList(
				"View Universities", "Add Universities",
				"Remove University", "Go Back"));
		switch(choice) {
		case 1:
			printSchools();
			break;
		case 2:
			addSchoolPrompt(s);
			break;
		case 3:
			removeSchoolPrompt(s);
			break;
		case 4:
			// Return false to end loop
			return false;
		default:
			System.out.println("Invalid option.");
			break;
		}
		return true;
	}
	
	/**
	 * Prints all universities, numbered.
	 * @return Count of universities.
	 */
	private int printSchools() {
		List<University> us = ui.getAllUniversities();
		for(int i = 0; i < us.size(); i++) {
			System.out.print(i + 1);
			System.out.print(") ");
			System.out.println(us.get(i).getName());
		}
		return us.size();
	}
	
	private void removeSchoolPrompt(Scanner s) {
		int count = printSchools();
		int choice = ConsoleUtils.getSingleMenuEntry(s, 1, count);
		if(choice == -1) {
			System.out.println("Invalid input.");
			return;
		}
		
		University u = ui.getAllUniversities().get(choice - 1);
		if(ui.removeUniversity(u)) System.out.println("Removed.");
	}
	
	private void addSchoolPrompt(Scanner s) {
		University uni = AdminAddSchool.prompt(s);
		if(uni == null) {
			System.out.println("\nAdd University canceled.");
		}
		else {
			boolean succ = ui.addNewUniversity(uni);
			if(succ) System.out.println("Successfully added university to system.");
			else System.out.println("Failed to insert to database.");
		}
	}
	
}
