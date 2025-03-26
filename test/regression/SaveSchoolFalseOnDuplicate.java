package regression;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmc.CMCException;
import cmc.backend.AccountController;
import cmc.backend.DatabaseController;
import junit.framework.Assert;

public class SaveSchoolFalseOnDuplicate {

	private static final String USERNAME = "test_test_test_ayo";
	private static final String PASSWD = "onskibidi73";
	
	private DatabaseController db;
	private AccountController ac;
	
	@Before
	public void setUp() throws CMCException {
		db = new DatabaseController();
		ac = new AccountController();
		ac.addUser(USERNAME, PASSWD, 'u', "lebron", "james");
		ac.deactivateUser(USERNAME);
	}
	
	@After
	public void tearDown() throws CMCException {
		ac.removeUser(USERNAME);
	}
	
	@Test
	public void test() throws CMCException {
		
		boolean succ = db.saveSchool(USERNAME, "CHALMERS UNIVERSITY OF TECHNOLOGY");
		Assert.assertTrue(succ);
		
		succ = db.saveSchool(USERNAME, "CHALMERS UNIVERSITY OF TECHNOLOGY");
		Assert.assertFalse(succ);
	}

}
