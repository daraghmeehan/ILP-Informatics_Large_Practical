package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

public class PowerGrabImpl implements PowerGrab { // Do I need an interface?
	
	private String day;
	private String month;
	private String year;
	private String droneVersion;
	
	GameParameters gameParams; //or just maxMoves?
	int movesMade = 0;
	Drone drone;
	PowerGrabMap map;
	MovementLog movementLog;
//	List<PowerStation> PowerStations;
	
	public PowerGrabImpl(GameParameters gameParams) {
		this.gameParams = gameParams;
	}
	
	public void setup(String[] args) {
		this.day = args[0];
		this.month = args[1];
		this.year = args[2];
		this.droneVersion = args[6];
		// needs exception handling
		boolean successfulDroneLoad = true; //necessary?
		boolean successfulMapLoad = true;
		this.drone = DroneLoader.load(args[3], args[4], args[5], args[6]);
		this.map = MapLoader.load(day, month, year);
		movementLog = new MovementLog();
//		this.PowerStations = map.getPowerStations;
	}
	
	public void play() {
		while (movesMade < 250) {
			if (drone.canMove()) {
				Move move = drone.makeMove(map);
				movementLog.addMove(move);
				movesMade++;
			} else {
				// want this?
				System.out.println("Can't move. Power ran out.");
				return;
			}
		}
	}
	
	public void report() {
		movementLog.writeLog(day, month, year, droneVersion);
		map.createGeoJSONMap(day, month, year, droneVersion);
	}
	
}
