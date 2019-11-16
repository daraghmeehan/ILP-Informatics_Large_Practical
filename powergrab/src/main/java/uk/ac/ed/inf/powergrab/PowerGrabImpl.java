package uk.ac.ed.inf.powergrab;

import java.util.List;

public class PowerGrabImpl implements PowerGrab {
	
	GameParameters gameParams; //or just maxMoves?
	int movesMade = 0;
	Drone drone;
	PowerGrabMap map;
//	List<PowerStation> PowerStations;
	
	public PowerGrabImpl(GameParameters gameParams) {
		this.gameParams = gameParams;
	}
	
	public void setup(String[] args) {
		// needs exception handling
		boolean successfulDroneLoad; //necessary?
		boolean successfulMapLoad;
		this.drone = DroneLoader.load(args[3], args[4], args[5], args[6]);
		this.map = MapLoader.load(args[0], args[1], args[2]);
//		this.PowerStations = map.getPowerStations;
	}
	
	public void play() {
		while (movesMade < 250) {
			if (drone.canMove()) {
				
			} else {
				// want this?
				System.out.println("Can't move. Power ran out.");
				return;
			}
			movesMade++;
		}
	}
	
	public void report() {
		
	}
	
	public static void main(String[] args) {
		
	}
	
}
