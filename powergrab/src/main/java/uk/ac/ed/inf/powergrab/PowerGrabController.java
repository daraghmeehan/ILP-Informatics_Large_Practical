package uk.ac.ed.inf.powergrab;

public class PowerGrabController {
	
	public static void playPowerGrab(String[] args) {
		
		if (InputValidator.isValid(args)) {
			PowerGrab powerGrab = new PowerGrabImpl(
					new GameParameters(250, 0.0003, 55.946233, 55.942617, -3.184319, -3.192473, 1.25));
			powerGrab.setup(args);
			powerGrab.play();
			powerGrab.report();
		} else {
			System.out.println("Invalid input. Please try again");
		}
		
	}
	
}
