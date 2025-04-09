package cmc.systemtest;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmc.backend.User;
import cmc.backend.controllers.DatabaseController;
import cmc.backend.entities.University;
import cmc.frontend.UserInteraction;

/**
 * System test for user story:
 * 
 * As a user, I should be able to log
 * into the system and search and find
 * Augsburg based on search criteria so
 * I can find it quickly.
 * 
 * @author Roman Lefler
 * @version Apr 8, 2025
 */
public class UserSearchSystemTest {
	
	private UserInteraction ui;
	private DatabaseController db;
	private User usr;

	@Before
	public void setUp() throws Exception {
		db = new DatabaseController();
		usr = new User("xXlebronXx", "jordan", false, "LeBron", "Jim");
		db.addUser(usr);
		
		ui = new UserInteraction();
	}

	@After
	public void tearDown() throws Exception {
		db.removeUser(usr);
		db = null;
		
		ui = null;
	}

	@Test
	public void searchForAugsburg() {
		ui.login(usr.getUsername(), usr.getPassword());
		List<University> list = ui.search(new Scanner("MINNESOTA\n\n"));
		
		boolean foundAugsburg = false;
		for(University uni : list) {
			if(uni.getName().equals("AUGSBURG")) foundAugsburg = true;
		}
		if(!foundAugsburg) fail("Augsburg not found in search results.");
	}

}
