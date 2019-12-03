package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

// assumes the drone won't run out of power
public class NearestNeighbourSimulation implements PowerGrabSimulation {
	
	private boolean gameSetup = false;
	
	private int movesMade = 0;
	private Position dronePosition;
	
	private List<ChargingStation> chargingStations;
	private List<ChargingStation> positiveStations;
	private List<ChargingStation> negativeStations;
	
	private List<Direction> nextMoves = new ArrayList<Direction>();
	private float result = 0;
	private List<Direction> totalMoves = new ArrayList<Direction>();
	
	public NearestNeighbourSimulation(Position startingPosition, List<ChargingStation> testStations) {
		this.dronePosition = startingPosition;
		this.chargingStations = testStations;
	}
	
	@Override
	public void setup() {
		this.positiveStations = StatefulDrone.calculatePositiveStations(chargingStations);
		this.negativeStations = StatefulDrone.calculateNegativeStations(chargingStations);
		this.gameSetup = true;
	}
	
	// params
	@Override
	public void play() {
		if (this.gameSetup) {
			// moves made later in logic
			while (this.positiveStations.size() > 0) {
				ChargingStation closestPositiveStation = Drone.calculateClosestStation(this.dronePosition, positiveStations);
				List<Direction> pathToClosestPositiveStation = StatefulDrone.findShortestPath(
						this.dronePosition, closestPositiveStation.position, negativeStations);
//				//testing
//				for (Direction d : pathToClosestPositiveStation) {
//					System.out.println(d.toString());
//				}
				// what should do here?
				if (pathToClosestPositiveStation == null) {
					positiveStations.remove(closestPositiveStation);
					continue;
				}
				
				nextMoves.addAll(pathToClosestPositiveStation);
				
				while (nextMoves.size() > 0) {
					if (movesMade < GameParameters.MAX_MOVES) {
						Direction nextMove = nextMoves.remove(0);
						dronePosition = dronePosition.nextPosition(nextMove);
						totalMoves.add(nextMove);
						movesMade++;
					} else {
						return;
					}
				}
//				System.out.println("Total move count: " + totalMoves.size());
				result += closestPositiveStation.getCoins();
//				System.out.println("Total coins so far: " + result);
				positiveStations.remove(closestPositiveStation);
			}
		}
	}
	
	@Override
	public void report() {
		if (this.gameSetup) {
			System.out.println("Nearest Neighbour Simulation coins: " + result);
			System.out.println("Nearest Neighbour Simulation total move count: " + totalMoves.size());
		}
	}
	
	public float getResult() {
		// if 0?
		return this.result;
	}
	
	public List<Direction> getMoves() {
		return this.totalMoves;
	}
	
//	public List<ChargingStation> getPositiveStations() {
//		return this.positiveStations;
//	}
//	
//	public List<ChargingStation> getNegativeStations() {
//		return this.negativeStations;
//	}
	
}
