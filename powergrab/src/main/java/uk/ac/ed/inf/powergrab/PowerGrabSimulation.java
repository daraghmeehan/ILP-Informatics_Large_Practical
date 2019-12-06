package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

/*
 * The basic simulation ran by the stateful drone.
 */
public abstract class PowerGrabSimulation implements PowerGrab {
	
	private boolean gameSetup = false;
	
	private Position originalDronePosition;
	
	private List<ChargingStation> chargingStations;
	private List<ChargingStation> positiveStations;
	public final int n;
	
	private double[][] distanceMatrix;
	
	private List<int[]> stationOrders = new ArrayList<int[]>();
	
	private float bestResult = 0;
	private List<Direction> bestMoves = new ArrayList<Direction>();
	
	public PowerGrabSimulation(Position startingPosition, List<ChargingStation> testStations) {
		this.originalDronePosition = startingPosition;
		this.chargingStations = testStations;
		this.positiveStations = StatefulDrone.calculatePositiveStations(this.chargingStations);
		this.n = positiveStations.size();
	}
	
	public double[][] getDistanceMatrix() {
		return this.distanceMatrix;
	}
	
	/*
	 * The setup phase of the basic simulation.
	 */
	@Override
	public void setup() {
		
		List<Position> routePositions = new ArrayList<Position>(n+1);
		routePositions.add(this.originalDronePosition);
		for (ChargingStation positiveStation : positiveStations) {
			routePositions.add(positiveStation.position);
		}
		
		this.distanceMatrix = StatefulDrone.calculateDistanceMatrix(routePositions);
		this.stationOrders = this.chooseStationOrders();
		
		this.gameSetup = true;
		
	}
	
	/*
	 * Calculates the different route orders using the simulations algorithm.
	 */
	public abstract List<int[]> chooseStationOrders();
	
	/*
	 * The play phase of the basic simulation.
	 */
	@Override
	public void play() {
		
		if (this.gameSetup) {
			for (int[] stationOrder : this.stationOrders) {
				
				int movesMade = 0;
				
				Position dronePosition = originalDronePosition;
				List<Direction> nextMoves = new ArrayList<Direction>();
				float result = 0;
				List<Direction> totalMoves = new ArrayList<Direction>();
				boolean[] visitedPositiveStations = new boolean[n];
				
				navigateToNextStation:
				for (int index = 0; index < n; index++) {
					
					int nextStationNumber = stationOrder[index];
					if (visitedPositiveStations[nextStationNumber]) {
						continue;
					}
					
					ChargingStation nextStation = positiveStations.get(nextStationNumber);
					List<Direction> pathToNextStation = StatefulDrone.findShortestPath(
							dronePosition, nextStation, chargingStations);
					if (pathToNextStation == null) {
						continue;
					}
					
					nextMoves.addAll(pathToNextStation);
					
					while (nextMoves.size() > 0) {
						if (movesMade < GameParameters.MAX_MOVES) {
							Direction nextMove = nextMoves.remove(0);
							dronePosition = dronePosition.nextPosition(nextMove);
							ChargingStation closestPositiveStation = Drone.calculateClosestStation(
									dronePosition, positiveStations);
							int closestPositiveStationIndex = PowerGrabSimulation.findStationIndex(
									closestPositiveStation, this.positiveStations);
							if (!(closestPositiveStationIndex == -1)) {
								if (!visitedPositiveStations[closestPositiveStationIndex]
										&& closestPositiveStation.isInRange(dronePosition) && !closestPositiveStation.equals(nextStation)) {
									result += closestPositiveStation.getCoins();
									visitedPositiveStations[closestPositiveStationIndex] = true;
								}
							}
							totalMoves.add(nextMove);
							movesMade++;
						} else {
							break navigateToNextStation;
						}
					}
					
					result += nextStation.getCoins();
					visitedPositiveStations[nextStationNumber] = true;
				}
				
				if (((int) result > (int) this.bestResult)
						|| ((int) result == (int) bestResult) && totalMoves.size() < bestMoves.size()) {
					this.bestResult = result;
					this.bestMoves = totalMoves;
				}
				
			}
		}
	}
	
	public float getResult() {
		return this.bestResult;
	}
	
	public List<Direction> getMoves() {
		return this.bestMoves;
	}
	
	/*
	 * Finds the index of a given charging station.
	 */
	private static int findStationIndex(ChargingStation chargingStation, List<ChargingStation> chargingStations) {
		
		for (int i = 0; i < chargingStations.size(); i++) {
			if (chargingStation.equals(chargingStations.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	/*
	 * Finds the position to place a given station in the route order that minimises the increase in estimated distance.
	 */
	public static int findBestInsertionPosition(int[] stationOrder, double[][] distanceMatrix,
			int stationToInsert, int numberOfStationsVisited) {
		
		int bestInsertionPosition = 0;
		double smallestDistanceIncrease = distanceMatrix[0][stationToInsert + 1]
				+ distanceMatrix[stationToInsert + 1][stationOrder[0] + 1] - distanceMatrix[0][stationOrder[0] + 1];
		
		for (int positionToCheck = 1; positionToCheck <= numberOfStationsVisited; positionToCheck++) {
			if (positionToCheck != numberOfStationsVisited) {
				double distance = distanceMatrix[stationOrder[positionToCheck - 1] + 1][stationToInsert + 1]
						+ distanceMatrix[stationToInsert + 1][stationOrder[positionToCheck] + 1]
								- distanceMatrix[stationOrder[positionToCheck - 1] + 1][stationOrder[positionToCheck] + 1];
				if (distance < smallestDistanceIncrease) {
					bestInsertionPosition = positionToCheck;
					smallestDistanceIncrease = distance;
				}
			} else {
				double distance = distanceMatrix[stationOrder[positionToCheck - 1] + 1][stationToInsert + 1];
				if (distance < smallestDistanceIncrease) {
					bestInsertionPosition = positionToCheck;
					smallestDistanceIncrease = distance;
				}
			}
		}
		
		return bestInsertionPosition;
	}
	
	/*
	 * Inserts a given station in the route order in the specified position.
	 */
	public static int[] insertStationIntoRouteOrder(int stationToInsert, int bestInsertionPosition, int[] stationOrder) {

		int n = stationOrder.length;

		for (int i = n - 1; i > bestInsertionPosition; i--) {
			stationOrder[i] = stationOrder[i - 1];
		}

		stationOrder[bestInsertionPosition] = stationToInsert;

		return stationOrder;
	}
	
}
