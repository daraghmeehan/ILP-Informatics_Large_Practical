package uk.ac.ed.inf.powergrab;

public class PowerGrabController {
	
	public static void playPowerGrab(String[] args) {
		
		if (InputValidator.isValid(args)) {
			PowerGrab powerGrab = new PowerGrabImpl(args);
			powerGrab.setup();
			powerGrab.play();
			powerGrab.report();
		} else {
			System.out.println("Invalid input. Please try again");
		}
		
	}
	
}
