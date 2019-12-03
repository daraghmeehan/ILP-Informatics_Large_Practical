package uk.ac.ed.inf.powergrab;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
 * Validates the user's input.
 */
public class InputValidator {
	
	/*
	 * Checks that the user's input arguments are valid to play PowerGrab.
	 */
	public static boolean isValid(String[] args) {
		
		if (args.length != 7) {
			System.out.println("Need 7 arguments to play PowerGrab");
			return false;
		}
		
		boolean validInput = true;
		
		String day = args[0];
		String month = args[1];
		String year = args[2];
		String initLatitudeAsString = args[3];
		String initLongitudeAsString = args[4];
		String seedAsString = args[5];
		String droneVersion = args[6];
		
		String date = day + "/" + month + "/" + year;
		SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
		sdfrmt.setLenient(false);
		try {
			sdfrmt.parse(date);
		} catch (ParseException e) {
			System.out.println("Date not in correct format. Need arguments DD MM YYYY");
			validInput = false;
		}
		
		try {
			double initLatitude = Double.parseDouble(initLatitudeAsString);
			double initLongitude = Double.parseDouble(initLongitudeAsString);
			Position initPosition = new Position(initLatitude, initLongitude);
			if (!initPosition.inPlayArea()) {
				System.out.println("Initial starting position is not valid. It must be in the play area");
				validInput = false;
			}
		} catch (NumberFormatException e) {
			System.out.println("Latitude and longitude are not in correct format. Both need to be a float");
			validInput = false;
		}
		
		try {
			Integer.parseInt(seedAsString);
		} catch (NumberFormatException e) {
			System.out.println("Seed argument is not in correct format. Needs to be an integer");
			validInput = false;
		}
		
		if (!droneVersion.matches("stateless|stateful")) {
			System.out.println("Drone version not in correct format. Needs to be \"stateless\" or \"stateful\"");
			validInput = false;
		}
		
		return validInput;
	}
	
}
