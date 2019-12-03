package uk.ac.ed.inf.powergrab;

import java.util.List;

public class PowerGrabImpl implements PowerGrab { // Do I need an interface?
	
	// do I need this/is this the best way?
	private boolean gameSetup = false;
	
	private final String day;
	private final String month;
	private final String year;
	private final String initLatitudeAsString;
	private final String initLongitudeAsString;
	private final String seedAsString;
	private final String droneVersion;
	
	private int movesMade = 0;
	private int maxMoves = GameParameters.maxMoves;
	
	private Drone drone;
	private PowerGrabMap map;
	private MovementLog movementLog;
//	List<PowerStation> PowerStations;
	
	public PowerGrabImpl(String[] args) {
		this.day = args[0];
		this.month = args[1];
		this.year = args[2];
		this.initLatitudeAsString = args[3];
		this.initLongitudeAsString = args[4];
		this.seedAsString = args[5];
		this.droneVersion = args[6];
	}
	
	@Override
	public void setup() {
		
		// needs exception handling
		boolean successfulDroneLoad = true; //necessary?
		boolean successfulMapLoad = true;
		// name these args?
		this.drone = DroneBuilder.build(initLatitudeAsString, initLongitudeAsString, seedAsString, droneVersion);
		this.map = MapBuilder.build(day, month, year);
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
			while (movesMade < maxMoves) {
				if (drone.canMove()) {
					Move move = drone.makeMove(chargingStations);
					map.addDronePath(move.getPositionBefore(), move.getPositionAfter());
					movementLog.addMove(move);
					System.out.println("Move: " + movesMade);
					System.out.println("Coins :" + move.coinsAfter);
					movesMade++;
				} else {
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
