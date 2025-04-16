package cmc.userstory;


import static org.junit.Assert.*;

import java.util.List;
import java.util.Scanner;

import cmc.CMCException;
import cmc.backend.User;
import cmc.backend.controllers.*;
import cmc.backend.entities.University;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cmc.frontend.UserInteraction;

/**
 * White box unit test for user story:
 * 
 * As a user, I should be able to search for
 * universities with certain criteria so that
 * I can limit my search.
 * 
 * @author Roman Lefler
 * @version Apr 8, 2025
 */
public class UniversitySearchTest {

	private UserInteraction ui;
	
	@Before
	public void setUp() throws CMCException {
		DatabaseController mock = new MockDatabaseController();
		ui = new UserInteraction(mock);
	}
	
	@After
	public void after() throws CMCException {
		ui = null;
	}
	
	@Test
	public void testStateAndStudentNum() {
		Scanner s = new Scanner("MINNESOTA\n10000\n");
		List<University> list = ui.search(s);
		for(University uni : list) {
			String state = uni.getState();
			int numStu = uni.getNumStudents();
			Assert.assertEquals("MINNESOTA", state);
			Assert.assertEquals(10000, numStu);
		}
		Assert.assertTrue(list.size() > 0);
	}
	
	@Test
	public void testOnlyState() {
		Scanner s = new Scanner("MINNESOTA\n\n");
		List<University> list = ui.search(s);
		for(University uni : list) {
			String state = uni.getState();
			Assert.assertEquals("MINNESOTA", state);
		}
		Assert.assertTrue(list.size() > 0);
	}
	
	@Test
	public void testOnlyStudentNum() {
		Scanner s = new Scanner("\n10000\n");
		List<University> list = ui.search(s);
		for(University uni : list) {
			int numStu = uni.getNumStudents();
			Assert.assertEquals(10000, numStu);
		}
		Assert.assertTrue(list.size() > 0);
	}

}
