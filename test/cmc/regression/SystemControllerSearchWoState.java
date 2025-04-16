package cmc.regression;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cmc.backend.SystemController;
import cmc.backend.entities.University;
import cmc.backend.controllers.*;

public class SystemControllerSearchWoState {

	@Test
	public void test() {
		DatabaseController db = new MockDatabaseController();
		SystemController sc = new SystemController(db);
		List<University> results = sc.getSearchController().search("", -1);
		Assert.assertTrue(results.size() > 0);
	}
}