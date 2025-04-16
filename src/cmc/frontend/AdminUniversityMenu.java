package cmc.frontend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cmc.CMCException;
import cmc.backend.entities.University;

/**
 * The admin menu for universities.
 * @author Roman Lefler
 * @version Mar 24, 2025
 */
public class AdminUniversityMenu {
	
	private final AdminInteraction ai; //adminInteraction / ai
	
	public AdminUniversityMenu(AdminInteraction ai) {
		this.ai = ai;
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
				"View Universities", "Add Universities", "Edit University",
				"Remove University", "Go Back"));
		switch(choice) {
		case 1:
			printSchools();
			break;
		case 2:
			addSchoolPrompt(s);
			break;
		case 3:
			List <University> universitiesList = ai.getAllUniversities(); //get all universities into a list
			
			//account for an empty list case
			if (universitiesList.isEmpty()) {
				System.out.println("No universities to edit.");
				break; 
			}
			System.out.println("Select univeristy to edit: ");
			//prints list for choice
			for(int i = 0; i < universitiesList.size(); i++) {
				System.out.println((i + 1) + ". " + universitiesList.get(i).getName());
			}
			//enter a valid number to choose university
			System.out.println("Enter a number to choose the university (or '0' to go back): ");
			int universityChoice = ConsoleUtils.tryGetInt(s);
			
			if (universityChoice > 0 && universityChoice <= universitiesList.size()){
				University uni = universitiesList.get(universityChoice - 1);
				try {
					editUniversityPrompt(s, uni);
				}catch (CMCException e) {
					System.err.println("Edit error: " + e.getMessage());
				}
			}
			break;
		case 4:
			removeSchoolPrompt(s);
			break;
		case 5:
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
		List<University> us = ai.getAllUniversities();
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
		
		University u = ai.getAllUniversities().get(choice - 1);
		if(ai.removeUniversity(u)) System.out.println("Removed.");
	}
	
	private void addSchoolPrompt(Scanner s) {
		University uni = AdminAddSchool.prompt(s);
		if(uni == null) {
			System.out.println("\nAdd University canceled.");
		}
		else {
			boolean succ = ai.addNewUniversity(uni);
			if(succ) System.out.println("Successfully added university to system.");
			else System.out.println("Failed to insert to database.");
		}
	}
	
	private void editUniversityPrompt(Scanner s, University u) throws CMCException {
		System.out.println("Currently editting: " + u.getName());
		University uni = AdminEditSchool.prompt(s, u);
		
		if(uni == null) {
			System.out.println("\nEdit University canceled.");
		}
		else {
			boolean succ = ai.editUniversityDetails(s, u);
			if(succ) System.out.println("Successfully edited the university.");
			else System.out.println("Failed to edit school database.");
		}
	}
}
