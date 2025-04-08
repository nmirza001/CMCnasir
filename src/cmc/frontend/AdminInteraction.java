package cmc.frontend;

import cmc.backend.SystemController;
import cmc.backend.AccountController;
import cmc.backend.UniversityController;

public class AdminInteraction {
	private final SystemController sController;
	private final AccountController accController;
	private final UniversityController uniController;
	
	
	public AdminInteraction(SystemController sController, AccountController accController, UniversityController uniController){
		this.sController = sController;
		this.accController = accController;
		this.uniController = uniController;
	}
	public void displayAdminMenu(){
		
	}
    public void handleUserManagement(){
		
	}

}
