package cmc.regression;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cmc.backend.AccountController;
import cmc.backend.SystemController;
import cmc.backend.UniversityController;
import cmc.backend.controllers.MockDatabaseController; //from Roman
import cmc.backend.entities.University;
import cmc.frontend.AdminInteraction;
import cmc.frontend.AdminUniversityMenu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class AdminUniversityDetailsWorksTest {
	
	private SystemController systemController;
	private AdminInteraction adminInteraction;
	private AdminUniversityMenu adminUniversityMenu;
	private MockDatabaseController mockDatabaseController;
	
	public final String UNIVERSITYNAME = "test University";
	
	@Before
	public void setUp(){
		mockDatabaseController = new MockDatabaseController();
		systemController = new SystemController(mockDatabaseController);
		adminInteraction = new AdminInteraction(systemController, new AccountController(mockDatabaseController), new UniversityController(mockDatabaseController));
		adminUniversityMenu = new AdminUniversityMenu(adminInteraction);
		
	    //Add a test university to mock database
	    University listUniversity = new University(UNIVERSITYNAME);
	    mockDatabaseController.addNewUniversity(listUniversity);
	}
	
	@After
	public void tearDown(){
		//clean up
		List<University> universities = mockDatabaseController.getAllSchools();
		universities.removeIf(u -> u.getName().equals(UNIVERSITYNAME));
	}
	
	@Test
	public void testSuccessfulEditUniversityState(){
		//simulate selecting the university and editing the state of the university
		String input = "2\n" //University Menu
				+ "4\n" //Edit University
				+ "1\n" //Selecting the first only uni
				+ "\n" //Name (don't change)
				+ "NY\n" //New State
				+ "\n\n\n\n\n\n\n\n\n\n\n\n\n" //all other fields ignored
				+ "5\n"; //Go Back
		
		InputStream enterStream = new ByteArrayInputStream(input.getBytes()); //reads input in bytes or one by one
		Scanner s = new Scanner(enterStream);
		
		//simulate admin menu interaction
		adminUniversityMenu.prompt(s);
		
		//checks if mock database updates
		List<University> updatedUniversities = mockDatabaseController.getAllSchools();
		Assert.assertEquals(1, updatedUniversities.size());
		Assert.assertEquals("NY", updatedUniversities.get(0).getState());
	}
	
	@Test
	public void testSuccessfulEditUniversityLocation(){
		//simulate selecting the university and editing the Location of the university
		
		    String input = "2\n" //University Menu
						+ "4\n" //Edit University
						+ "1\n" //Selecting the first only uni
						+ "\nURBAN" //Name (don't change)
						+ "\n" //New State
						+ "\n\n\n\n\n\n\n\n\n\n\n\n\n" //all other fields ignored
						+ "5\n"; //Go Back

			InputStream enterStream = new ByteArrayInputStream(input.getBytes());
			Scanner s = new Scanner(enterStream);
			
			adminUniversityMenu.prompt(s);
			
			List<University> updatedUniversities = mockDatabaseController.getAllSchools();
			Assert.assertEquals("URBAN", updatedUniversities.get(0).getLocation());
	}
}
