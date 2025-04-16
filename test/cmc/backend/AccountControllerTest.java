package cmc.backend;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmc.CMCException;
import cmc.backend.controllers.MockDatabaseController;
import org.junit.Assert;

public class AccountControllerTest {

	private AccountController ac;
	private User usr;
	
	@Before
	public void setUp() throws Exception {
		usr = new User("xxlebronxx", "secure", false, "Lebron", "James");
		ac = new AccountController(new MockDatabaseController());
	}

	@After
	public void tearDown() throws Exception {
		ac = null;
	}

	@Test
	public void testAddUser() throws CMCException {
		boolean succ = ac.addUser(usr);
		Assert.assertTrue(succ);
	}

	@Test
	public void testRemoveUser() throws CMCException {
		boolean succ = ac.removeUser(usr);
		Assert.assertFalse(succ);
	}

	@Test
	public void testGetAllUsers() {
		List<User> list = ac.getAllUsers();
		Assert.assertTrue(list.size() > 0);
	}

}
