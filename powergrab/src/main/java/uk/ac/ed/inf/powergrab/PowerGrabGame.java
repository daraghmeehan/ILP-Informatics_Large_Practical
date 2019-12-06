package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.net.MalformedURLException;
import java.io.IOException;

/*
 * This is the implementation of the PowerGrab interface as specified in the coursework documentation.
 */
public class PowerGrabGame implements PowerGrab { // Do I need an interface?
	
	private boolean gameSetup = false;
	private boolean gamePlayed = false;
	
	private final String day;
	private final String month;
	private final String year;
	private final String initLatitudeAsString;
	private final String initLongitudeAsString;
	private final String seedAsString;
	private final String droneVersion;
	
	private Drone drone;
	private Map map;
	private List<ChargingStation> chargingStations;
	private MovementLog movementLog;
	
	private int movesMade = 0;
	
	public PowerGrabGame(String[] args) {
		this.day = args[0];
		this.month = args[1];
		this.year = args[2];
		this.initLatitudeAsString = args[3];
		this.initLongitudeAsString = args[4];
		this.seedAsString = args[5];
		this.droneVersion = args[6];
	}
	
	/*
	 * The game is setup by calling the Drone and Map builders, and instantiating chargingStations and movementLog.
	 * Exceptions thrown when building the Map result in the game terminating early.
	 */
	@Override
	public void setup() {
		
		boolean successfulMapBuild = true;
		this.drone = DroneBuilder.build(initLatitudeAsString, initLongitudeAsString, seedAsString, droneVersion);
		try {
			this.map = MapBuilder.build(day, month, year);
		} catch (MalformedURLException e) {
			System.out.println("This URL is invalid. Please choose a date for which there is a PowerGrab map.");
			successfulMapBuild = false;
		} catch (IOException e) {
			System.out.println("Ran into difficulty downloading the map. Please try again.");
			successfulMapBuild = false;
		}
		if (!successfulMapBuild) {
			System.out.println("Building the map was unsuccessful. Please try again.");
			System.exit(1);
		} else {
			this.chargingStations = map.getChargingStations();
			this.movementLog = new MovementLog();
			this.gameSetup = true;
		}
	}
	
	/*
	 * Plays the game until the drone runs out of power, or the maximum moves have been made.
	 * Records all of the drone's moves.
	 */
	@Override
	public void play() {
		
		if (gameSetup) {
			while (movesMade < GameParameters.MAX_MOVES) {
				if (drone.canMove()) {
					Move move = drone.makeMove(chargingStations);
					map.addDronePath(move.positionBefore, move.positionAfter);
					movementLog.addMove(move);
					movesMade++;
				} else {
					System.out.println("Can't move. Power ran out.");
					gamePlayed = true;
					return;
				}
			}
			gamePlayed = true;
		} else {
			System.out.println("Can't play as setup has not been completed.");
		}
	}
	
	/*
	 * Reports on the result of the game by writing the two .txt and .geojson output files.
	 */
	@Override
	public void report() {
		
		if (gamePlayed) {
			map.createGeoJSONMap(day, month, year, droneVersion);
			movementLog.writeLog(day, month, year, droneVersion);
		} else {
			System.out.println("Can't report as the game has not been played.");
		}
	}
	
}
