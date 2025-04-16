package cmc.userstory;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cmc.CMCException;
import cmc.backend.AccountController;
import cmc.backend.SystemController;
import cmc.backend.UniversityController;
import cmc.backend.User;
import cmc.backend.controllers.DatabaseController;
import cmc.backend.controllers.MockDatabaseController;
import cmc.frontend.AdminInteraction;
import cmc.frontend.UserInteraction;

/**
 * Black box unit test for user story:
 * 
 * As a admin, I should be able to remover other accounts
 * so that they are no longer in the database.
 * 
 * @author Timmy Flynn
 * @version Apr 16, 2025
 */

public class AdminRemoveUserTest {
	
	private AdminInteraction ai;
	private DatabaseController db;
	private SystemController sc;
	private AccountController ac;
	private UniversityController uc;
	private User usr = new User("testuser", "passwd", false, "Bron", "Jimmy");
	
	@Before
	public void setUp() throws Exception {
		db = new MockDatabaseController();
		db.addUser(usr);
		
		ai = new AdminInteraction(sc = new SystemController(),ac = new AccountController(),uc = new UniversityController());
				
	}

	@After
	public void tearDown() throws Exception {
		db.removeUser(usr);
	}

	@Test
	public void testValidUser() throws CMCException {
		boolean succ = db.removeUser(usr);
		Assert.assertTrue(succ);
	}
	
	@Test
	public void testInvalidValidUser() throws CMCException {
		User usr1 = new User("testusers", "passwd", false, "Bron", "Jimmy");
		boolean succ = db.removeUser(usr1);
		Assert.assertTrue(!succ);
	}

}
