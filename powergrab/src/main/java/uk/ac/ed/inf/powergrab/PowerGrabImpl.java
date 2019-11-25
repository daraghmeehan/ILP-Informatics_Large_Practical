package uk.ac.ed.inf.powergrab;

import java.util.List;

public class PowerGrabImpl implements PowerGrab { // Do I need an interface?
	
	// do I need this/is this the best way?
	private boolean gameSetup = false;
	
	private String day;
	private String month;
	private String year;
	private String initLatitudeAsString;
	private String initLongitudeAsString;
	private String seedAsString;
	private String droneVersion;
	
	GameParameters gameParams; //or just maxMoves?
	int movesMade = 0;
	Drone drone;
	PowerGrabMap map;
	MovementLog movementLog;
//	List<PowerStation> PowerStations;
	
	public PowerGrabImpl(String[] args, GameParameters gameParams) {
		this.day = args[0];
		this.month = args[1];
		this.year = args[2];
		this.initLatitudeAsString = args[3];
		this.initLongitudeAsString = args[4];
		this.seedAsString = args[5];
		this.droneVersion = args[6];
		this.gameParams = gameParams;
	}
	
	@Override
	public void setup() {
		// needs exception handling
		boolean successfulDroneLoad = true; //necessary?
		boolean successfulMapLoad = true;
		// name these args?
		this.drone = DroneCreator.create(this.initLatitudeAsString, this.initLongitudeAsString, this.seedAsString, this.droneVersion);
		this.map = MapCreator.create(this.day, this.month, this.year);
		this.movementLog = new MovementLog();
//		this.PowerStations = map.getPowerStations;
		if (successfulDroneLoad && successfulMapLoad) {
			this.gameSetup = true;
		}
	}
	
	@Override
	public void play() {
		//here?
		List<ChargingStation> chargingStations = map.getChargingStations();
		if (gameSetup) {
			//game params
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
