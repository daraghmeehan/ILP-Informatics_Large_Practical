package uk.ac.ed.inf.powergrab;

public class DroneLoader {

	public static Drone load(String initLatitudeAsString, String initLongitudeAsString, String seedAsString, String droneVersion) {
		// needs exception handling? where
		// assert drone in game area
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
