package uk.ac.ed.inf.powergrab;

/*
 * Builds a stateless or stateful drone with the given starting position and seed.
 */
public class DroneBuilder {
	
	/*
	 * Parses the arguments and constructs the right type of drone. Assumes format has been verified by InputValidator.
	 */
	public static Drone build(
			String initLatitudeAsString, String initLongitudeAsString, String seedAsString, String droneVersion) {
		
		double initLatitude = Double.parseDouble(initLatitudeAsString);
		double initLongitude = Double.parseDouble(initLongitudeAsString);
		int seed = Integer.parseInt(seedAsString);
		
		if (droneVersion.equals("stateless")) {
			return new StatelessDrone(new Position(initLatitude, initLongitude), seed);
		} else {
			return new StatefulDrone(new Position(initLatitude, initLongitude), seed);
		}
	}

}
