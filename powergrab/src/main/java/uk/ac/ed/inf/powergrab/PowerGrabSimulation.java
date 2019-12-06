package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

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
	
	protected PowerGrabSimulation(Position startingPosition, List<ChargingStation> testStations) {
		this.originalDronePosition = startingPosition;
		this.chargingStations = testStations;
		this.positiveStations = StatefulDrone.calculatePositiveStations(this.chargingStations);
		this.n = positiveStations.size();
	}
	
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
	
//	protected List<ChargingStation> getChargingStations() {
//		return this.chargingStations;
//	}
//	
//	protected List<ChargingStation> getPositiveStations() {
//		return this.positiveStations;
//	}
//	
//	protected List<Position> getRoutePositions() {
//		return this.routePositions;
//	}
	
	protected double[][] getDistanceMatrix() {
		return this.distanceMatrix;
	}
	
	protected abstract List<int[]> chooseStationOrders();
	
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
					// what should do here?
					// should check if move made yet and then must choose a random direction
					if (pathToNextStation == null) {
						continue;
					}
					
					nextMoves.addAll(pathToNextStation);
					
					while (nextMoves.size() > 0) {
						if (movesMade < GameParameters.MAX_MOVES) {
							Direction nextMove = nextMoves.remove(0);
							dronePosition = dronePosition.nextPosition(nextMove);
							ChargingStation closestPositiveStation = Drone.calculateClosestStation(
									dronePosition, positiveStations); // charging or positive?
							// need to charge if close enough and mark this station off the ones to visit
							// need to check if already visited
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
					// System.out.println("Total move count: " + totalMoves.size());
					result += nextStation.getCoins();
					visitedPositiveStations[nextStationNumber] = true;
					// System.out.println("Total coins so far: " + result);
				}
				
				if (((int) result > (int) this.bestResult)
						|| ((int) result == (int) bestResult) && totalMoves.size() < bestMoves.size()) {
//					System.out.println("better result: " + result);
//					System.out.println("better move count?: " + totalMoves.size());
					this.bestResult = result;
					this.bestMoves = totalMoves;
				}
				
			}
		}
	}
	
	public float getResult() {
		// if 0?
		return this.bestResult;
	}
	
	public List<Direction> getMoves() {
		return this.bestMoves;
	}
	
	private static int findStationIndex(ChargingStation chargingStation, List<ChargingStation> chargingStations) {
		
		for (int i = 0; i < chargingStations.size(); i++) {
			if (chargingStation.equals(chargingStations.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	
	
}
