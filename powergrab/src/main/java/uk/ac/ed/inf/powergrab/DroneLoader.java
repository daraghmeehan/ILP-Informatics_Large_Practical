package uk.ac.ed.inf.powergrab;

public class DroneLoader {

	public static Drone load(String initLatitude, String initLongitude, String seed, String droneVersion) {
		// needs exception handling? where
		if (droneVersion.equals("stateful")) {
			return new StatefulDrone(new Position(initLatitude, initLongitude, Integer.parseInt(seed)));
		} else {
			return new StatelessDrone(new Position(initLatitude, initLongitude, Integer.parseInt(seed));
		}
	}

}
