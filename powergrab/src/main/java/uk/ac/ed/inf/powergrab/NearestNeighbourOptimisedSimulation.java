package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class NearestNeighbourOptimisedSimulation implements PowerGrabSimulation {
	
	private boolean gameSetup = false;
	
	private int movesMade = 0;
	private Position dronePosition;
	
	private List<ChargingStation> chargingStations;
	
	private List<ChargingStation> positiveStations;
	private List<Position> routePositions = new ArrayList<Position>();
	private int[] stationOrder;
	// need to get this to work for better optimisation
	private int[] visitedPositiveStations;
//	private int[] optimisedStationOrder;
	
	private List<ChargingStation> negativeStations;
	
	private double[][] distanceMatrix;
	
	private List<Direction> nextMoves = new ArrayList<Direction>();;
	private float result = 0;
	private List<Direction> totalMoves = new ArrayList<Direction>();
	
	public NearestNeighbourOptimisedSimulation(Position startingPosition, List<ChargingStation> testStations) {
		this.dronePosition = startingPosition;
		this.chargingStations = testStations;
	}
	
	@Override
	public void setup() {
		this.positiveStations = StatefulDrone.calculatePositiveStations(this.chargingStations);
		
		routePositions.add(this.dronePosition);
		for (ChargingStation positiveStation : positiveStations) {
			routePositions.add(positiveStation.position);
		}
		
		this.negativeStations = StatefulDrone.calculateNegativeStations(this.chargingStations);
		
		this.distanceMatrix = StatefulDrone.calculateDistanceMatrix(routePositions);
		this.calculateNearestNeighbourOrder();
		System.out.println("Estimated distance before optimisation: "
				+ NearestNeighbourOptimisedSimulation.calculateTotalRouteDistance(stationOrder, distanceMatrix));
		this.twoOptOptimise();
		System.out.println("Estimated distance after 2-opt optimisation: "
				+ NearestNeighbourOptimisedSimulation.calculateTotalRouteDistance(stationOrder, distanceMatrix));
//		this.threeOptOptimise();
//		System.out.println("Estimated distance after 3-opt optimisation: "
//				+ NearestNeighbourOptimisedSimulation.calculateTotalRouteDistance(stationOrder, distanceMatrix));
		
		this.gameSetup = true;
	}

	@Override
	public void play() {
		if (this.gameSetup) {
			// moves made later in logic
			for (int index = 0; index < stationOrder.length; index++) {
				ChargingStation nextStation = positiveStations.get(stationOrder[index]);
				List<Direction> pathToNextStation = StatefulDrone.findShortestPath(
						this.dronePosition, nextStation.position, negativeStations);
				//// testing
				// what should do here?
				if (pathToNextStation == null) {
					continue;
				}
				
				nextMoves.addAll(pathToNextStation);
				
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
				// System.out.println("Total move count: " + totalMoves.size());
				result += nextStation.getCoins();
				// System.out.println("Total coins so far: " + result);
			}
		}
	}

	@Override
	public void report() {
		if (this.gameSetup) {
			System.out.println("Nearest Neighbour Optimised Simulation coins: " + result);
			System.out.println("Nearest Neighbour Optimised Simulation total move count: " + totalMoves.size());
		}
	}
	
	@Override
	public float getResult() {
		return this.result;
	}
	
	@Override
	public List<Direction> getMoves() {
		return this.totalMoves;
	}
	
	public static double calculateTotalRouteDistance(int[] stationOrder, double[][] distanceMatrix) {
		
		int numberOfStations = stationOrder.length;
		double distance = 0;
		distance += distanceMatrix[0][stationOrder[0] + 1];
		
		for (int stationNumber = 0; stationNumber < numberOfStations - 1; stationNumber++) {
			int firstStation = stationOrder[stationNumber];
			int secondStation = stationOrder[stationNumber + 1];
			distance += distanceMatrix[firstStation + 1][secondStation + 1];
		}
		
		return distance;
	}
	
	private void calculateNearestNeighbourOrder() {
		
		int n = positiveStations.size();
		this.stationOrder = new int[n];
		boolean[] visitedStations = new boolean[n];
		
		int initialMinStation = 0;
		double initialMinDistance = distanceMatrix[0][0];
		
		for (int stationNumber = 0; stationNumber < n; stationNumber++) {
			double distance = distanceMatrix[0][stationNumber + 1];
			if (distance < initialMinDistance) {
				initialMinStation = stationNumber;
				initialMinDistance = distance;
			}
		}
		stationOrder[0] = initialMinStation;
		visitedStations[initialMinStation] = true;
		
		for (int stationOrderIndex = 0; stationOrderIndex < n - 1; stationOrderIndex++) {
			int currentStation = stationOrder[stationOrderIndex];
			
			int minStation = currentStation;
			double minDistance = distanceMatrix[currentStation + 1][currentStation + 1];
			
			for (int stationNumber = 0; stationNumber < n; stationNumber++) {
				if (visitedStations[stationNumber]) {
					continue;
				}
				double distance = distanceMatrix[currentStation + 1][stationNumber + 1];
				if (distance < minDistance) {
					minStation = stationNumber;
					minDistance = distance;
				}
			}
			
			stationOrder[stationOrderIndex + 1] = minStation;
			visitedStations[minStation] = true;
		}
	}
	
	private void twoOptOptimise() {
		
		int n = stationOrder.length;
		double bestDistance = NearestNeighbourOptimisedSimulation.calculateTotalRouteDistance(stationOrder, distanceMatrix);
		
		while (true) {
			boolean improvementMade = false;
			
			//necessary?
			currentIteration:
				// need i to be 1 or 0? changing first one move?
				for (int i = 1; i < n - 1; i++) {
					for (int j = i + 1; j < n; j++) {
						int[] newOrder = NearestNeighbourOptimisedSimulation.twoOptSwap(stationOrder, i, j);
						double newDistance = NearestNeighbourOptimisedSimulation.calculateTotalRouteDistance(newOrder, distanceMatrix);
						if (newDistance < bestDistance) {
							stationOrder = newOrder;
							bestDistance = newDistance;
//							System.out.println("New best distance: " + bestDistance);
							improvementMade = true;
							break currentIteration;
						}
				}
			}
			if (!improvementMade) {
				return;
			}
		}
	}
	
	private static int[] twoOptSwap(int[] order, int i, int j) {
		
		int n = order.length;
		
		if (i <= 0 || j <= i || j >= n - 1) {
			return order;
		}
		
		List<Integer> newRoute = new ArrayList<Integer>(n);
		
		// can be optimised!
		for (int x : Arrays.copyOfRange(order, 0, i)) {
			newRoute.add(x);
		}
		
		List<Integer> reversedSegment = new ArrayList<Integer>();
		for (int x : Arrays.copyOfRange(order, i, j + 1)) {
			reversedSegment.add(x);
		}
		Collections.reverse(reversedSegment);
		newRoute.addAll(reversedSegment);
		
		for (int x : Arrays.copyOfRange(order, j + 1, n)) {
			newRoute.add(x);
		}
		
		int[] newOrder = new int[n];
		
		for (int k = 0; k < n; k++) {
			newOrder[k] = newRoute.get(k);
		}
		return newOrder;
	}

	private void threeOptOptimise() {

		int n = stationOrder.length;
		double bestDistance = NearestNeighbourOptimisedSimulation.calculateTotalRouteDistance(stationOrder, distanceMatrix);

		while (true) {
			double delta = 0;

			//			necessary?
			for (int i = 1; i < n - 4; i++) {
				for (int j = i + 2; j < n - 2; j++) {
					for (int k = j + 2; k < n; k++ ) {
						delta += reverseSegmentIfBetter(i, j, k);
					}
				}
			}
			if (delta >= 0) {
				return;
			}
		}
	}

	private double reverseSegmentIfBetter(int i, int j, int k) {
		
		int n = stationOrder.length;
		if (i < 1 || j <= i + 1 || k <= j + 1 || k >= n) {
			return 0;
		}
		
		int A = this.stationOrder[i-1];
		int B = this.stationOrder[i];
		int C = this.stationOrder[j-1];
		int D = this.stationOrder[j];
		int E = this.stationOrder[k-1];
		int F = this.stationOrder[k];
		
		double d0 = distanceMatrix[A+1][B+1] + distanceMatrix[C+1][D+1] + distanceMatrix[E+1][F+1];
		double d1 = distanceMatrix[A+1][C+1] + distanceMatrix[B+1][D+1] + distanceMatrix[E+1][F+1];
		double d2 = distanceMatrix[A+1][B+1] + distanceMatrix[C+1][E+1] + distanceMatrix[D+1][F+1];
		double d3 = distanceMatrix[A+1][D+1] + distanceMatrix[E+1][B+1] + distanceMatrix[C+1][F+1];
		double d4 = distanceMatrix[F+1][B+1] + distanceMatrix[C+1][D+1] + distanceMatrix[E+1][A+1];
		
		if (d0 > d1) {
//			reverseTour(i, j);
			return -d0 + d1;
		} else if (d0 > d2) {
//			reverseTour(j, k);
			return -d0 + d2;
		} else if (d0 > d4) {
//			reverseTour(i, k);
			return -d0 + d4;
		} else if (d0 > d3) {
//			swapTourSegments(i, j, k);
			return -d0 + d3;
		} else {
			return 0;
		}
		
//		return 0;
	}

}
