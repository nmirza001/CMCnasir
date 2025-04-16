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
	
	@Before
	public void setUp() throws CMCException {
		ac = new AccountController(new MockDatabaseController());
		usr.setActivated(false);
		ac.addUser(usr);
	}
	
	@After
	public void tearDown() throws CMCException {
		ac.removeUser(usr);
	}
	
	@Test
	public void test() throws CMCException {
		SystemController sc = new SystemController();
		User user = sc.login(USERNAME, PASSWD);
		Assert.assertNull(user);
	}

}
