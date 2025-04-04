package regression;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmc.CMCException;
import cmc.backend.AccountController;
import cmc.backend.SystemController;
import cmc.backend.User;
import cmc.backend.controllers.DatabaseController;
import junit.framework.Assert;

public class DeactivatedUserCanStillLogin {

	private static final String USERNAME = "test_test_test_acct";
	private static final String PASSWD = "pass123$";
	
	private AccountController db;
	
	@Before
	public void setUp() throws CMCException {
		db = new AccountController();
		db.addUser(USERNAME, PASSWD, 'u', "Test", "Acct");
		db.deactivateUser(USERNAME);
	}
	
	@After
	public void tearDown() throws CMCException {
		db.removeUser(USERNAME);
	}
	
	@Test
	public void test() throws CMCException {
		SystemController sc = new SystemController();
		User user = sc.login(USERNAME, PASSWD);
		Assert.assertNull(user);
	}

}
