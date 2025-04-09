package cmc.userstory;


import static org.junit.Assert.*;

import cmc.CMCException;
import cmc.backend.User;
import cmc.backend.controllers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cmc.frontend.UserInteraction;

/**
 * Black box unit test for user story:
 * 
 * As a user, I should be able to log in with my username and password
 * so that only I can access my account.
 * 
 * @author Roman Lefler
 * @version Apr 8, 2025
 */
public class UserLoginTest {

	private UserInteraction ui;
	private DatabaseController db;
	private User usr = new User("testuser", "passwd", 'u', "Bron", "Jimmy");
	
	@Before
	public void setUp() throws CMCException {
		db = new DatabaseController();
		db.addUser(usr);
		
		ui = new UserInteraction();
	}
	
	@After
	public void after() throws CMCException {
		ui.logout();
		ui = null;
		
		db.removeUser(usr);
		db = null;
	}
	
	@Test
	public void testValidUsernameValidPassword() {
		boolean succ = ui.login("testuser", "passwd");
		Assert.assertTrue(succ);
	}
	
	@Test
	public void testValidUsernameInvalidPassword() {
		boolean succ = ui.login("testuser", "wrongpasswd");
		Assert.assertFalse(succ);
	}
	
	@Test
	public void testInvalidUsernameInvalidPassword() {
		boolean succ = ui.login("wronguser", "wrongpasswd");
		Assert.assertFalse(succ);
	}

}
