
package cmc.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmc.CMCException;
import cmc.backend.User;
import cmc.backend.entities.University;

/**
 * A mock database controller for testing, providing predefined data.
 * Includes data specifically for testing findSimilar functionality.
 *
 * @author Roman Lefler, NAsir with findSimilar test data added
 * @version Apr 15, 2025 // Updated version date
 */
public class MockDatabaseController extends DatabaseController {

    private Map<String, User> users;
    private Map<String, University> unis;
    private Map<String, List<String>> savedSchools;

    public MockDatabaseController() {
        super(false);
        users = new HashMap<>();
        User u = new User("mjordan", "securepwd", false, "Michael", "Jordan");
		users.put("mjordan", u);
        savedSchools = new HashMap<>();
        unis = new HashMap<>();

        University augsburg = new University("AUGSBURG");
        augsburg.setState("MINNESOTA");
        augsburg.setLocation("SMALL-CITY");
        augsburg.setControl("PRIVATE");
        augsburg.setNumStudents(10000);
        augsburg.setSatVerbal(550);
        augsburg.setSatMath(550);
        augsburg.setPercentAdmitted(70);
        augsburg.setScaleAcademics(3);
        augsburg.setExpenses(45000);
        augsburg.setPercentFemale(60);
        augsburg.setPercentFinancialAid(95);
        augsburg.setNumApplicants(5000);
        augsburg.setPercentEnrolled(30);
        augsburg.setScaleSocial(4);
        augsburg.setScaleQualityOfLife(4);
        unis.put("AUGSBURG", augsburg);

        University targetU = new University("TARGET_U");
        targetU.setState("MINNESOTA");
        targetU.setLocation("URBAN");
        targetU.setControl("STATE");
        targetU.setNumStudents(10000);
        targetU.setSatVerbal(600);
        targetU.setSatMath(600);
        targetU.setPercentAdmitted(50);
        targetU.setScaleAcademics(4);
        targetU.setExpenses(50000);
        targetU.setPercentFemale(52);
        targetU.setPercentFinancialAid(80);
        targetU.setNumApplicants(15000);
        targetU.setPercentEnrolled(35);
        targetU.setScaleSocial(4);
        targetU.setScaleQualityOfLife(4);
        unis.put("TARGET_U", targetU);

        University similarU = new University("SIMILAR_U");
        similarU.setState("MINNESOTA");
        similarU.setLocation("URBAN");
        similarU.setControl("STATE");
        similarU.setNumStudents(11000);
        similarU.setSatVerbal(620);
        similarU.setSatMath(630);
        similarU.setPercentAdmitted(60);
        similarU.setScaleAcademics(4);
        similarU.setExpenses(52000);
        similarU.setPercentFemale(55);
        similarU.setPercentFinancialAid(78);
        similarU.setNumApplicants(14000);
        similarU.setPercentEnrolled(40);
        similarU.setScaleSocial(5);
        similarU.setScaleQualityOfLife(4);
        unis.put("SIMILAR_U", similarU);

        University differentU = new University("DIFFERENT_U");
        differentU.setState("CALIFORNIA");
        differentU.setLocation("SUBURBAN");
        differentU.setControl("PRIVATE");
        differentU.setNumStudents(1000);
        differentU.setSatVerbal(500);
        differentU.setSatMath(500);
        differentU.setPercentAdmitted(90);
        differentU.setScaleAcademics(2);
        differentU.setExpenses(65000);
        differentU.setPercentFemale(48);
        differentU.setPercentFinancialAid(60);
        differentU.setNumApplicants(8000);
        differentU.setPercentEnrolled(50);
        differentU.setScaleSocial(3);
        differentU.setScaleQualityOfLife(3);
        unis.put("DIFFERENT_U", differentU);

        University targetAloneU = new University("TARGET_ALONE_U");
        targetAloneU.setState("NEW YORK");
        targetAloneU.setLocation("REMOTE");
        targetAloneU.setControl("COMMUNITY");
        targetAloneU.setNumStudents(50000);
        targetAloneU.setSatVerbal(800);
        targetAloneU.setSatMath(800);
        targetAloneU.setPercentAdmitted(5);
        targetAloneU.setScaleAcademics(1);
        targetAloneU.setExpenses(90000);
        targetAloneU.setPercentFemale(45);
        targetAloneU.setPercentFinancialAid(60);
        targetAloneU.setNumApplicants(50000);
        targetAloneU.setPercentEnrolled(10);
        targetAloneU.setScaleSocial(1);
        targetAloneU.setScaleQualityOfLife(2);
        unis.put("TARGET_ALONE_U", targetAloneU);

        University similarToExternalU = new University("SIMILAR_TO_EXTERNAL_U");
        similarToExternalU.setState("SOME_STATE");
        similarToExternalU.setLocation("SOME_LOCATION");
        similarToExternalU.setControl("SOME_CONTROL");
        similarToExternalU.setNumStudents(9000);
        similarToExternalU.setSatVerbal(580);
        similarToExternalU.setSatMath(590);
        similarToExternalU.setPercentAdmitted(40);
        similarToExternalU.setScaleAcademics(3);
        similarToExternalU.setExpenses(48000);
        similarToExternalU.setPercentFemale(51);
        similarToExternalU.setPercentFinancialAid(85);
        similarToExternalU.setNumApplicants(12000);
        similarToExternalU.setPercentEnrolled(33);
        similarToExternalU.setScaleSocial(4);
        similarToExternalU.setScaleQualityOfLife(4);
        unis.put("SIMILAR_TO_EXTERNAL_U", similarToExternalU);

        University ucla = new University("UCLA");
        ucla.setState("CALIFORNIA");
        ucla.setLocation("URBAN");
        ucla.setControl("STATE");
        ucla.setNumStudents(30000);
        ucla.setSatVerbal(680);
        ucla.setSatMath(720);
        ucla.setPercentAdmitted(15);
        ucla.setScaleAcademics(5);
        unis.put("UCLA", ucla);
    }

    @Override
    public void close() {
        System.out.println("MockDatabaseController close() called.");
    }

    @Override
    public boolean addUser(User u) throws CMCException {
        if (u == null || u.getUsername() == null) {
            throw new IllegalArgumentException("User and username cannot be null");
        }
        if (users.containsKey(u.getUsername())) {
            return false;
        }
        users.put(u.getUsername(), u.uClone());
        return true;
    }

    @Override
    public boolean removeUser(User u) throws CMCException {
        if (u == null || u.getUsername() == null) {
            throw new IllegalArgumentException("User and username cannot be null");
        }
        savedSchools.remove(u.getUsername());
        return users.remove(u.getUsername()) != null;
    }

    @Override
    public User getUser(String username) {
        if (username == null) return null;
        User found = users.get(username);
        return (found != null) ? found.uClone() : null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        for (User u : users.values()) {
            userList.add(u.uClone());
        }
        return userList;
    }

    @Override
    public boolean editUser(User u) {
        if (u == null || u.getUsername() == null) {
            throw new IllegalArgumentException("User and username cannot be null");
        }
        if (!users.containsKey(u.getUsername())) {
            return false;
        }
        users.put(u.getUsername(), u.uClone());
        return true;
    }

    @Override
    public boolean saveSchool(String username, String schoolName) throws CMCException {
        if (username == null || schoolName == null) {
            throw new IllegalArgumentException("Username and schoolName cannot be null");
        }
        if (!users.containsKey(username)) {
            throw new CMCException("User '" + username + "' does not exist.");
        }
        if (!unis.containsKey(schoolName)) {
            System.err.println("Mock Warning: Attempt to save non-existent school: " + schoolName);
        }
        savedSchools.putIfAbsent(username, new ArrayList<String>());
        List<String> list = savedSchools.get(username);
        if (list.contains(schoolName)) {
            return false;
        }
        return list.add(schoolName);
    }

    @Override
    public Map<String, List<String>> getUserSavedSchoolMap() {
        return new HashMap<>(savedSchools);
    }

    @Override
    public Map<String, List<String>> getUniversitiesEmphases() {
        System.out.println("MockDatabaseController getUniversitiesEmphases() called (returning empty).");
        return new HashMap<>();
    }

    @Override
    public List<University> getAllSchools() {
        return new ArrayList<>(unis.values());
    }

    @Override
    public List<String> getAllEmphases() {
        System.out.println("MockDatabaseController getAllEmphases() called (returning empty).");
        return new ArrayList<>();
    }

    @Override
    public boolean addNewUniversity(University u) {
        if (u == null || u.getName() == null) {
            throw new IllegalArgumentException("University and name cannot be null");
        }
        return unis.putIfAbsent(u.getName(), u) == null;
    }

    @Override
    public boolean removeUniversity(University u) {
        if (u == null || u.getName() == null) {
            throw new IllegalArgumentException("University and name cannot be null");
        }
        for (List<String> saved : savedSchools.values()) {
            saved.remove(u.getName());
        }
        return unis.remove(u.getName()) != null;
    }

    @Override
    public boolean editUniversity(University u) {
        if (u == null || u.getName() == null) {
            throw new IllegalArgumentException("University and name cannot be null");
        }
        if (!unis.containsKey(u.getName())) {
            return false;
        }
        unis.put(u.getName(), u);
        return true;
    }
}