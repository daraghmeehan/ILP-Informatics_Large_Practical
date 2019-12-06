package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

/*
 * The implementation of the stateful drone as specified in the coursework document.
 */
public class StatefulDrone extends Drone {
	
	private boolean strategyChosen = false;
	private List<Direction> nextMoves = new ArrayList<Direction>();

	public StatefulDrone(Position position, int seed) {
		super(position, seed);
	}
	
	public boolean isStrategyChosen() {
		return strategyChosen;
	}
	
	/*
	 * Forms the stateful drone's strategy if not yet chosen.
	 * Returns the next move direction in the move schedule,
	 * or a pseudorandomly chosen direction if the move schedule is empty.
	 */
	@Override
	public Direction chooseDirection(List<ChargingStation> chargingStations) {
		
		if (!this.isStrategyChosen()) {
			this.formBestStrategy(chargingStations);
			strategyChosen = true;
		}
		
		if (nextMoves.size() != 0) {
			return nextMoves.remove(0);
		} else {
			return super.chooseRandomDirection(chargingStations);
		}
	}
	
	/*
	 * Runs three basic simulations of the PowerGrab map and chooses the best to determine its best strategy.
	 */
	private void formBestStrategy(List<ChargingStation> chargingStations) {
		
		List<ChargingStation> stationsCopy1 = new ArrayList<ChargingStation>(chargingStations);
		List<ChargingStation> stationsCopy2 = new ArrayList<ChargingStation>(chargingStations);
		List<ChargingStation> stationsCopy3 = new ArrayList<ChargingStation>(chargingStations);
		
		List<Float> simulationResults = new ArrayList<Float>(3);
		List<List<Direction>> simulationMoves = new ArrayList<List<Direction>>(3);
		
		PowerGrabSimulation nearestNeighbourSimulation = new NearestNeighbourSimulation(this.getPosition(), stationsCopy1);
		nearestNeighbourSimulation.setup();
		nearestNeighbourSimulation.play();
		nearestNeighbourSimulation.report();
		
		float nearestNeighbourResult = nearestNeighbourSimulation.getResult();
		System.out.println("Nearest Neighbour Result: " + nearestNeighbourResult);
		simulationResults.add(nearestNeighbourResult);
		
		List<Direction> nearestNeighbourMoves = nearestNeighbourSimulation.getMoves();
		System.out.println("Nearest Neighbour Moves: " + nearestNeighbourMoves.size());
		simulationMoves.add(nearestNeighbourMoves);
		
		
		PowerGrabSimulation nearestInsertionSimulation = new NearestInsertionSimulation(this.getPosition(), stationsCopy2);
		nearestInsertionSimulation.setup();
		nearestInsertionSimulation.play();
		nearestInsertionSimulation.report();
		
		float nearestInsertionResult = nearestInsertionSimulation.getResult();
		System.out.println("Nearest Insertion Result: " + nearestInsertionResult);
		simulationResults.add(nearestInsertionResult);
		
		List<Direction> nearestInsertionMoves = nearestInsertionSimulation.getMoves();
		System.out.println("Nearest Insertion Moves: " + nearestInsertionMoves.size());
		simulationMoves.add(nearestInsertionMoves);
		
		
		PowerGrabSimulation farthestInsertionSimulation = new FarthestInsertionSimulation(this.getPosition(), stationsCopy3);
		farthestInsertionSimulation.setup();
		farthestInsertionSimulation.play();
		farthestInsertionSimulation.report();
		
		float farthestInsertionResult = farthestInsertionSimulation.getResult();
		System.out.println("Farthest Insertion Result: " + farthestInsertionResult);
		simulationResults.add(farthestInsertionResult);
		
		List<Direction> farthestInsertionMoves = farthestInsertionSimulation.getMoves();
		System.out.println("Farthest Insertion Moves: " + farthestInsertionMoves.size());
		simulationMoves.add(farthestInsertionMoves);
		
		
		List<Direction> bestStrategyMoves = StatefulDrone.chooseBestStrategy(simulationResults, simulationMoves);
		nextMoves.addAll(bestStrategyMoves);
		
	}
	
	/*
	 * Chooses the best strategy from all the basic simulations.
	 * Assumes both lists are the same length and both are non-null.
	 */
	private static List<Direction> chooseBestStrategy(List<Float> simulationResults, List<List<Direction>> simulationMoves) {
		
		int numberOfSimulations = simulationResults.size();
		
		float bestStrategyResult = simulationResults.get(0);
		List<Direction> bestStrategy = simulationMoves.get(0);
		int bestStrategyMoveNumber = bestStrategy.size();
		
		if (!(numberOfSimulations == 1)) {
			for (int i = 1; i < numberOfSimulations; i++) {
				float simulationResult = simulationResults.get(i);
				if (((int) simulationResult > (int) bestStrategyResult)
						|| ((int) simulationResult == (int) bestStrategyResult
						&& simulationMoves.get(i).size() < bestStrategyMoveNumber)) {
					bestStrategyResult = simulationResult;
					bestStrategy = simulationMoves.get(i);
					bestStrategyMoveNumber = bestStrategy.size();
				}
			}
		}
		
		return bestStrategy;
	}
	
	/*
	 * Finds the optimal path to a charging station using A* search.
	 */
	public static List<Direction> findShortestPath(
			Position startPosition, ChargingStation goalStation, List<ChargingStation> chargingStations) {
		
		Position goalPosition = goalStation.position;
		
		TreeMap<Node, Integer> openSet = new TreeMap<Node, Integer>(new NodeComparator());
		List<Node> closedSet = new ArrayList<Node>();

		Node startNode = new Node(startPosition, new ArrayList<Direction>(), goalPosition);
		openSet.put(startNode, 0);
		
		int nodesChecked = 0;

		while (!openSet.isEmpty() && nodesChecked < 15000) {
			
			Node current = openSet.pollFirstEntry().getKey();
			
			if (current.reachedGoal(goalStation, chargingStations) && !(current.getPath().size() == 0)) {
				return current.getPath();
			}

			for (Node neighbour : current.getNeighbours(goalPosition)) {
				if (closedSet.contains(neighbour)) {
					continue;
				}
				
				ChargingStation closestStation = StatefulDrone.calculateClosestStation(neighbour.position, chargingStations);
				if (closestStation.isNegative() && closestStation.isInRange(neighbour.position)) {
					continue;
				}
				
				int tentativeGScore = current.getGScore() + 1;
				if (!openSet.containsKey(neighbour)) {
					openSet.put(neighbour, tentativeGScore);
				} else {
					if (tentativeGScore < openSet.get(neighbour)) {
						openSet.remove(neighbour);
						openSet.put(neighbour, tentativeGScore);
					}
				}
			}
			closedSet.add(current);
			nodesChecked++;
		}
		
		return null;
	}
	
	/*
	 * The admissible and consistent heuristic used by A* Search.
	 */
	public static double aStarHeuristic(Position currentPosition, Position goalPosition) {
		return Position.calculateDistance(currentPosition, goalPosition);
	}
	
	/*
	 * Calculates the positive stations from a given List of ChargingStation objects.
	 */
	public static List<ChargingStation> calculatePositiveStations(List<ChargingStation> chargingStations) {

		List<ChargingStation> positiveStations = new ArrayList<ChargingStation>();
		
		for (ChargingStation chargingStation : chargingStations) {
			if (chargingStation.isPositive()) {
				positiveStations.add(chargingStation);
			}
		}
		
		return positiveStations;
	}
	
	/*
	 * Calculates the distance matrix of a given List of Position objects.
	 */
	public static double[][] calculateDistanceMatrix(List<Position> positions) {
		
		int n = positions.size();
		double[][] distanceMatrix = new double[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (i == j) {
					distanceMatrix[i][j] = Double.POSITIVE_INFINITY;
				} else {
					double distance = Position.calculateDistance(positions.get(i), positions.get(j));
					distanceMatrix[i][j] = distance;
					distanceMatrix[j][i] = distance;
				}
			}
		}
		
		return distanceMatrix;
	}
	
	/*
	 * Calculates the estimated total route distance of a route order.
	 */
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
	
	/*
	 * Reverses the segment of the route order from index i to j.
	 */
	private static int[] reverseRouteSegment(int[] order, int i, int j) {

		int n = order.length;

		if (i < 0 || j <= i || j > n - 1) {
			System.out.println("Can't");
			return order;
		}

		List<Integer> newRoute = new ArrayList<Integer>(n);

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

	/*
	 * Swaps the segments of the order from index i to j - 1, and j to k - 1.
	 */
	private static int[] swapRouteSegments(int[] order, int i, int j, int k) {
		
		int n = order.length;
		
		if (i < 0 || j <= i || k <= j || k > n) {
			return order;
		}

		List<Integer> newRoute = new ArrayList<Integer>(n);

		for (int x : Arrays.copyOfRange(order, 0, i)) {
			newRoute.add(x);
		}

		List<Integer> swappedSegment = new ArrayList<Integer>();
		for (int x : Arrays.copyOfRange(order, j, k)) {
			swappedSegment.add(x);
		}
		
		for (int x : Arrays.copyOfRange(order, i, j)) {
			swappedSegment.add(x);
		}
		newRoute.addAll(swappedSegment);

		for (int x : Arrays.copyOfRange(order, k, n)) {
			newRoute.add(x);
		}

		int[] newOrder = new int[n];

		for (int m = 0; m < n; m++) {
			newOrder[m] = newRoute.get(m);
		}
		return newOrder;
		
	}
	
	/*
	 * Optimises a route order using the 2-opt algorithm.
	 */
	public static int[] twoOptOptimise(int[] stationOrder, double[][] distanceMatrix) {
		
		int n = stationOrder.length;
		int[] bestOrder = stationOrder;
		
		while (true) {
			boolean improvementMade = false;
			
			currentIteration:
				for (int i = 0; i < n - 1; i++) {
					
					int A;
					if (i == 0) {
						A = -1;
					} else {
						A = bestOrder[i-1];
					}
					int B = bestOrder[i];
					
					for (int j = i + 2; j <= n; j++) {

						int C = bestOrder[j-1];
						
						double d0, d1;
						
						if (j == n) {
							d0 = distanceMatrix[A+1][B+1];
							d1 = distanceMatrix[A+1][C+1];
						} else {
							int D = bestOrder[j];							
							d0 = distanceMatrix[A+1][B+1] + distanceMatrix[C+1][D+1];
							d1 = distanceMatrix[A+1][C+1] + distanceMatrix[B+1][D+1];
						}
						
						if (d1 < d0) {
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, i, j - 1);
							improvementMade = true;
							break currentIteration;
						}
				}
			}
			if (!improvementMade) {
				return bestOrder;
			}
		}
	}
	
	/*
	 * Optimises a route order using the 3-opt algorithm.
	 */
	public static int[] threeOptOptimise(int[] stationOrder, double[][] distanceMatrix) {

		int n = stationOrder.length;
		int[] bestOrder = stationOrder;

		while (true) {
			boolean improvementMade = false;
			
			currentIteration:
			for (int i = 0; i < n - 4; i++) {
				
				int A;
				if (i == 0) {
					A = -1;
				} else {
					A = bestOrder[i-1];
				}
				int B = bestOrder[i];
				
				for (int j = i + 2; j < n - 2; j++) {
					
					int C = bestOrder[j-1];
					int D = bestOrder[j];
					
					for (int k = j + 2; k <= n; k++ ) {
						
						int E = bestOrder[k-1];
						
						double d0, d1, d2, d3, d4;
						
						if (k == n) {
							d0 = distanceMatrix[A+1][B+1] + distanceMatrix[C+1][D+1];
							d1 = distanceMatrix[A+1][C+1] + distanceMatrix[B+1][D+1];
							d2 = distanceMatrix[A+1][B+1] + distanceMatrix[C+1][E+1];
							d3 = distanceMatrix[A+1][D+1] + distanceMatrix[E+1][B+1];
							d4 = distanceMatrix[A+1][E+1] + distanceMatrix[C+1][D+1];
						} else {
							int F = bestOrder[k];
							
							d0 = distanceMatrix[A+1][B+1] + distanceMatrix[C+1][D+1] + distanceMatrix[E+1][F+1];
							d1 = distanceMatrix[A+1][C+1] + distanceMatrix[B+1][D+1] + distanceMatrix[E+1][F+1];
							d2 = distanceMatrix[A+1][B+1] + distanceMatrix[C+1][E+1] + distanceMatrix[D+1][F+1];
							d3 = distanceMatrix[A+1][D+1] + distanceMatrix[E+1][B+1] + distanceMatrix[C+1][F+1];
							d4 = distanceMatrix[F+1][B+1] + distanceMatrix[C+1][D+1] + distanceMatrix[E+1][A+1];
						}
						
						if (d1 < d0) {
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, i, j - 1);
							improvementMade = true;
							break currentIteration;
						} else if (d2 < d0) {
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, j, k - 1);
							improvementMade = true;
							break currentIteration;
						} else if (d4 < d0) {
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, i, k - 1);
							improvementMade = true;
							break currentIteration;
						} else if (d3 < d0) {
							bestOrder = StatefulDrone.swapRouteSegments(bestOrder, i, j, k);
							improvementMade = true;
							break currentIteration;
						}
						
					}
				}
			}
			if (!improvementMade) {
				return bestOrder;
			}
		}
	}
	
	/*
	 * for testing/debugging
	 */
	private static void printOrder(int[] order) {
		int n = order.length;
		
		for (int i = 0; i < n; i++) {
			System.out.print(order[i] + " ");
		}
		
		System.out.println("");
	}
	
}
