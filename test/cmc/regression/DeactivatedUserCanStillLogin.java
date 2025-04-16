package cmc.regression;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cmc.CMCException;
import cmc.backend.AccountController;
import cmc.backend.SystemController;
import cmc.backend.User;
import cmc.backend.controllers.DatabaseController;
import cmc.backend.controllers.MockDatabaseController;

public class DeactivatedUserCanStillLogin {

	private static final String USERNAME = "test_test_test_acct";
	private static final String PASSWD = "pass123$";
	private static final User usr = new User(USERNAME, PASSWD, false, "Test", "Acct");
	
	private AccountController ac;
	private SystemController sc;
	
	@Before
	public void setUp() throws CMCException {
		DatabaseController db = new MockDatabaseController();
		ac = new AccountController(db);
		sc = new SystemController(db);
		usr.setActivated(false);
		ac.addUser(usr);
	}
	
	@After
	public void tearDown() throws CMCException {
		ac.removeUser(usr);
	}
	
	@Test
	public void testDeactiatedUserLogin() throws CMCException {
		User user = sc.login(USERNAME, PASSWD);
		Assert.assertNull(user);
	}

}
