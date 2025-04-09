package cmc.regression;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cmc.CMCException;
import cmc.backend.AccountController;
import cmc.backend.User;
import cmc.backend.controllers.DatabaseController;

public class SaveSchoolFalseOnDuplicate {

	private static final String USERNAME = "test_test_test_ayo";
	private static final String PASSWD = "onskibidi73";
	
	private DatabaseController db;
	private AccountController ac;
	
	@Before
	public void setUp() throws CMCException {
		db = new DatabaseController();
		ac = new AccountController();
		User u = new User(USERNAME, PASSWD, false, "lebron", "james");
		u.setActivated(false);
		ac.addUser(u);
	}
	
	@After
	public void tearDown() throws CMCException {
		User u = new User(USERNAME, PASSWD, false, "lebron", "james");
		ac.removeUser(u);
	}
	
	@Test
	public void test() throws CMCException {
		
		boolean succ = db.saveSchool(USERNAME, "CHALMERS UNIVERSITY OF TECHNOLOGY");
		Assert.assertTrue(succ);
		
		succ = db.saveSchool(USERNAME, "CHALMERS UNIVERSITY OF TECHNOLOGY");
		Assert.assertFalse(succ);
	}

}
