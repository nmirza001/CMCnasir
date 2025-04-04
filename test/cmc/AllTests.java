package cmc;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	cmc.backend.UniversityTest.class,
	cmc.backend.UserTest.class,
	
	cmc.regression.DeactivatedUserCanStillLogin.class,
	cmc.regression.RemoveUserDoesntWork.class,
	cmc.regression.SaveSchoolFalseOnDuplicate.class,
	cmc.regression.SystemControllerSearchWoState.class
})
public class AllTests {

}
