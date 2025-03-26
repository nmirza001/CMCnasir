package cmc.backend;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import cmc.backend.entities.University;

/**
 * Test the database and its interactions with the University class.
 * @author Roman Lefler
 * @version Mar 15, 2024
 */
public class UniversityTest {
	
	private UniversityController db;
	private static final String NAME = "TEST SCHOOL NAME";
	
	private University getUni(String name) {
		List<University> us = db.getAllSchools();
		return University.find(us, name);
	}
	
	@Before
	public void setUp() {
		db = new UniversityController();
		University uni = new University(NAME);
		boolean succ = db.addNewUniversity(uni);
		Assert.assertTrue(succ);
	}
	
	@After
	public void tearDown() {
		University u = getUni(NAME);
		Assert.assertNotNull(u);
		boolean succ = db.removeUniversity(u);
		Assert.assertTrue(succ);
		db = null;
	}
	
	@Test
	public void testEditEmphasis() {
		University initial = getUni(NAME);
		initial.addEmphasis("LIBERAL ARTS");
		db.editUniversity(initial);
		initial = null;
		
		// Add a emphasis
		University later = getUni(NAME);
		List<String> emphases = later.getEmphases();
		Assert.assertEquals(emphases.size(), 1);
		Assert.assertEquals(emphases.get(0), "LIBERAL ARTS");
		
		// Remove an emphasis
		later.removeEmphasis("LIBERAL ARTS");
		db.editUniversity(later);
		later = null;
		
		University fffinal = getUni(NAME);
		Assert.assertEquals(fffinal.getEmphases().size(), 0);
	}
	
	@Test
	public void deleteWithEmphasis() {
		University u = new University("TEST SCHOOL NO 2");
		u.addEmphasis("LIBERAL ARTS");
		db.addNewUniversity(u);
		boolean success = db.removeUniversity(u);
		Assert.assertTrue(success);
	}

}
