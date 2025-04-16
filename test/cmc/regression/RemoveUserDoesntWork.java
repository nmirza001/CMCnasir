package cmc.regression;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import cmc.backend.*;
import cmc.*;
import cmc.backend.controllers.*;

public class RemoveUserDoesntWork {

	private static final String PASSWD = "pass123$";
	private static final String U_USER = "test_test_test_regular_user";
	
	@Test
	public void test() throws CMCException {
		DatabaseController db = new MockDatabaseController();
		AccountController ac = new AccountController(db);
		SystemController sc = new SystemController(db);
		
		User u = new User(U_USER, PASSWD, false, "Admin", "McAdministrator");
		ac.addUser(u);
		sc.saveSchool(U_USER, "BARD");
		
		boolean succ = ac.removeUser(u);
		Assert.assertTrue(succ);

				
	}

}
