package uk.ac.ed.inf.powergrab;

/*
 * This class controls the overall execution of the program.
 */
public class PowerGrabController {
	
	public static void playPowerGrab(String[] args) {
		
		if (InputValidator.isValid(args)) {
			PowerGrab powerGrab = new PowerGrabGame(args);
			powerGrab.setup();
			powerGrab.play();
			powerGrab.report();
		} else {
			System.out.println("Invalid input. Please try again");
		}
		
	}
	
}
