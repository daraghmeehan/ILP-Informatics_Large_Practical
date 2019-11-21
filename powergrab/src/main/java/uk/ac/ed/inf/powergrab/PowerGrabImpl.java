package uk.ac.ed.inf.powergrab;

import java.util.List;

public class PowerGrabImpl implements PowerGrab { // Do I need an interface?
	
	// do I need this/is this the best way?
	private boolean gameSetup = false;
	
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
	
	@Override
	public void setup(String[] args) {
		this.day = args[0];
		this.month = args[1];
		this.year = args[2];
		this.droneVersion = args[6];
		// needs exception handling
		boolean successfulDroneLoad = true; //necessary?
		boolean successfulMapLoad = true;
		// name these args?
		this.drone = DroneCreator.create(args[3], args[4], args[5], args[6]);
		this.map = MapCreator.create(day, month, year);
		movementLog = new MovementLog();
//		this.PowerStations = map.getPowerStations;
		if (successfulDroneLoad && successfulMapLoad) {
			gameSetup = true;
		}
	}
	
	@Override
	public void play() {
		//here?
		List<ChargingStation> chargingStations = map.getChargingStations();
		if (gameSetup) {
			while (movesMade < 250) {
				if (drone.canMove()) {
					Move move = drone.makeMove(chargingStations);
					map.addDronePath(move.getPositionBefore(), move.getPositionAfter());
					movementLog.addMove(move);
					movesMade++;
				} else {
					// want this?
					System.out.println("Can't move. Power ran out.");
					return;
				}
			}
		}
	}
	
	@Override
	public void report() {
		if (gameSetup) {
			movementLog.writeLog(day, month, year, droneVersion);
			map.createGeoJSONMap(day, month, year, droneVersion);
		}
	}
	
}
