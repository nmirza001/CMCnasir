package cmc.regression;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cmc.backend.SystemController;
import cmc.backend.entities.University;

public class SystemControllerSearchWoState {

	@Test
	public void test() {
		SystemController sc = new SystemController();
		List<University> results = sc.search("", -1);
		Assert.assertTrue(results.size() > 0);
	}

}
