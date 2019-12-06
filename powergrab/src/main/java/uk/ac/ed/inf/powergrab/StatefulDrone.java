package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

public class StatefulDrone extends Drone {
	
	private boolean strategyChosen = false;
	private List<Direction> nextMoves = new ArrayList<Direction>();

	public StatefulDrone(Position position, int seed) {
		super(position, seed);
	}
	
	public boolean isStrategyChosen() {
		return strategyChosen;
	}
	
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

	private void formBestStrategy(List<ChargingStation> chargingStations) {
		
		List<ChargingStation> stationsCopy1 = new ArrayList<ChargingStation>(chargingStations);
		List<ChargingStation> stationsCopy2 = new ArrayList<ChargingStation>(chargingStations);
		
//		PowerGrabSimulation nnSimulation = new NearestNeighbourSimulationUnused(this.getPosition(), stationsCopy1);
//		nnSimulation.setup();
//		nnSimulation.play();
//		nnSimulation.report();
//		float nnResult = nnSimulation.getResult();
//		List<Direction> nnMoves = nnSimulation.getMoves();
//		int nnMoveCount = nnMoves.size();
		
		PowerGrabSimulation nnOptimisedSimulation = new NearestNeighbourSimulation(this.getPosition(), stationsCopy2);
		nnOptimisedSimulation.setup();
		nnOptimisedSimulation.play();
		nnOptimisedSimulation.report();
		float nnOptimisedResult = nnOptimisedSimulation.getResult();
		System.out.println("Result: " + nnOptimisedResult);
		List<Direction> nnOptimisedMoves = nnOptimisedSimulation.getMoves();
		System.out.println("Moves: " + nnOptimisedMoves.size());
//		int nnOptimisedMoveCount = nnOptimisedMoves.size();
		
		PowerGrabSimulation nearestInsertionSimulation = new NearestInsertionSimulation(this.getPosition(), stationsCopy1);
		nearestInsertionSimulation.setup();
		nearestInsertionSimulation.play();
		nearestInsertionSimulation.report();
		float nearestInsertionResult = nearestInsertionSimulation.getResult();
		System.out.println("ni Result: " + nearestInsertionResult);
		List<Direction> nearestInsertionMoves = nearestInsertionSimulation.getMoves();
		System.out.println("ni Moves: " + nearestInsertionMoves.size());
//		int nearestInsertionMoveCount = nearestInsertionMoves.size();
		
		PowerGrabSimulation furthestInsertionSimulation = new FurthestInsertionSimulation(this.getPosition(), stationsCopy1);
		furthestInsertionSimulation.setup();
		furthestInsertionSimulation.play();
		furthestInsertionSimulation.report();
		float furthestInsertionResult = furthestInsertionSimulation.getResult();
		System.out.println("ni Result: " + furthestInsertionResult);
		List<Direction> furthestInsertionMoves = furthestInsertionSimulation.getMoves();
		System.out.println("ni Moves: " + furthestInsertionMoves.size());
		
		List<Direction> bestStrategyMoves;
		
		// turn into method!
		// instead calculate better move count?
//		if ((int) nnResult == (int) nnOptimisedResult) {
//			if (nnMoveCount < nnOptimisedMoveCount) {
//				System.out.println("Nearest Neighbour was better");
//				bestStrategyMoves = nnMoves;
//			} else {
//				System.out.println("Nearest Neighbour Optimised was better");
				bestStrategyMoves = furthestInsertionMoves;
//			}
//		} else {
//			if (nnResult > nnOptimisedResult) {
//				System.out.println("Nearest Neighbour was better");
//				bestStrategyMoves = nnMoves;			
//			} else {
//				System.out.println("Nearest Neighbour Optimised was better");
//				bestStrategyMoves = nnOptimisedMoves;
//			}
//			
//		}
		
		nextMoves.addAll(bestStrategyMoves);
	}
	
	// A* search // need a way of failing to get to a station
	// need to check if can go through a bad station to get more coins overall
	protected static List<Direction> findShortestPath(
			Position startPosition, ChargingStation goalStation, List<ChargingStation> chargingStations) {
//		System.out.println("Finding shortest path between " + startPosition.toString() + " and " + goalPosition.toString());
		
		Position goalPosition = goalStation.position;
		// node to g-score
		TreeMap<Node, Integer> openSet = new TreeMap<Node, Integer>(new NodeComparator());
		List<Node> closedSet = new ArrayList<Node>();

		Node startNode = new Node(startPosition, new ArrayList<Direction>(), goalPosition);
		openSet.put(startNode, 0);
		
		int nodesChecked = 0;

		// right amount of nodes checked?
		while (!openSet.isEmpty() && nodesChecked < 10000) {
			
			Node current = openSet.pollFirstEntry().getKey();
			
//			//testing
//			System.out.println("Current node position: " + current.position.toString());
//			System.out.println("Current node f-score so far: " + current.getFScore());
//			System.out.print("Current node path so far: ");
//			for (Direction d : current.getPath()) {
//				System.out.print(d + " ");		
//			}
//			System.out.println("");
			
			// only second condition if in initial position
			if (current.reachedGoal(goalStation, chargingStations) && !(current.getPath().size() == 0)) {
//				System.out.println("Reached goal!");
//				System.out.println("Path length = " + current.getPath().size());
//				System.out.println("Nodes checked: " + nodesChecked);
				return current.getPath();
			}

			for (Node neighbour : current.getNeighbours(goalPosition)) {
				// what happens if all neighbours are negative?
				// but getNeighbours never returns negative results?
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
					// Node sameNeighbour = openSet.
					if (tentativeGScore < openSet.get(neighbour)) {
						openSet.remove(neighbour);
						openSet.put(neighbour, tentativeGScore);
					}
				}
			}
			closedSet.add(current);
			nodesChecked++;
		}
		// need to check if null returned!
		
		// testing
		System.out.println("Failed to find route. Nodes checked: " + nodesChecked + ". "
				+ "Looking for station with position: " + goalPosition);
		return null;
	}
	
	// admissible and consistent?
	protected static double aStarHeuristic(Position currentPosition, Position goalPosition) {
		return Position.calculateDistance(currentPosition, goalPosition);
	}
//	
//	// should be in Drone?
//	public static boolean isAtNegativeStation(Position position, List<ChargingStation> badStations) {
//		
//		for (ChargingStation badStation : badStations) {
//			if (badStation.isInRange(position)) {
//				return true;
//			}
//		}
//		
//		return false;
//	}

	public static List<ChargingStation> calculatePositiveStations(List<ChargingStation> testStations) {

		
		List<ChargingStation> positiveStations = new ArrayList<ChargingStation>();
		
		for (ChargingStation chargingStation : testStations) {
			if (chargingStation.isPositive()) {
				positiveStations.add(chargingStation);
			}
		}
		
		return positiveStations;
	}
	
//	public static List<ChargingStation> calculateNegativeStations(List<ChargingStation> testStations) {
//
//		List<ChargingStation> negativeStations = new ArrayList<ChargingStation>();
//		
//		for (ChargingStation chargingStation : testStations) {
//			if (chargingStation.isNegative()) {
//				negativeStations.add(chargingStation);
//			}
//		}
//		
//		return negativeStations;
//	}
	
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

	protected static double calculateTotalRouteDistance(int[] stationOrder, double[][] distanceMatrix) {

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

	private static int[] reverseRouteSegment(int[] order, int i, int j) {

		int n = order.length;

		if (i < 0 || j <= i || j > n - 1) {
			System.out.println("Can't");
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

	private static int[] swapRouteSegments(int[] order, int i, int j, int k) {
		
		int n = order.length;
		
		// necessary?
		if (i < 0 || j <= i || k <= j || k > n) {
//			System.out.println("Can't");
			return order;
		}

		List<Integer> newRoute = new ArrayList<Integer>(n);

		// can be optimised!
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
//		System.out.println(n);
//		System.out.println(newRoute.size());

		int[] newOrder = new int[n];

		for (int m = 0; m < n; m++) {
			newOrder[m] = newRoute.get(m);
		}
		return newOrder;
		
	}
	
	protected static int[] twoOptOptimise(int[] stationOrder, double[][] distanceMatrix) {
		
		int n = stationOrder.length;
		int[] bestOrder = stationOrder;
//		int improvements = 0;
		
		double initialDistance = distanceMatrix[0][stationOrder[0]];
		double otherDistance = distanceMatrix[0][stationOrder[4]];
//		System.out.println(initialDistance);
//		System.out.println(otherDistance);
		
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
//							System.out.println("Distance before = " + StatefulDrone.calculateTotalRouteDistance(bestOrder, distanceMatrix));
//							System.out.println("i = " + i + ", j = " + j);
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, i, j - 1);
//							System.out.println("New best distance = " + StatefulDrone.calculateTotalRouteDistance(bestOrder, distanceMatrix));
							improvementMade = true;
//							improvements++;
							break currentIteration;
						}
				}
			}
			if (!improvementMade) {
//				System.out.println(improvements);
				return bestOrder;
			}
		}
	}
	
	// testing
	private static void printOrder(int[] order) {
		int n = order.length;
		
		for (int i = 0; i < n; i++) {
			System.out.print(order[i] + " ");
		}
		
		System.out.println("");
	}
	
	protected static int[] threeOptOptimise(int[] stationOrder, double[][] distanceMatrix) {

		int n = stationOrder.length;
		int[] bestOrder = stationOrder;

		while (true) {
			boolean improvementMade = false;
//			necessary?
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
						
						double d0, d1, d2, d3, d4, d5, d6;
						
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
//							System.out.println("d1");
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, i, j - 1);
//							StatefulDrone.printOrder(bestOrder);
							improvementMade = true;
							break currentIteration;
						} else if (d2 < d0) {
//							System.out.println("d2");
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, j, k - 1);
//							StatefulDrone.printOrder(bestOrder);
							improvementMade = true;
							break currentIteration;
						} else if (d4 < d0) {
//							System.out.println("d4");
							bestOrder = StatefulDrone.reverseRouteSegment(bestOrder, i, k - 1);
//							StatefulDrone.printOrder(bestOrder);
							improvementMade = true;
							break currentIteration;
						} else if (d3 < d0) {
//							System.out.println("d3");
							bestOrder = StatefulDrone.swapRouteSegments(bestOrder, i, j, k);
//							StatefulDrone.printOrder(bestOrder);
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
	
	// original which worked
//	protected static int[] twoOptOptimise(int[] stationOrder, double[][] distanceMatrix) {
//
//		int n = stationOrder.length;
//		int[] bestOrder = stationOrder;
//		double bestDistance = StatefulDrone.calculateTotalRouteDistance(stationOrder, distanceMatrix);
//
//		while (true) {
//			boolean improvementMade = false;
//
//			//necessary?
//			currentIteration:
//				// need i to be 1 or 0? changing first one move?
//				for (int i = 0; i < n - 1; i++) {
//					for (int j = i + 1; j < n; j++) {
//						int[] newOrder = StatefulDrone.reverseRouteSegment(bestOrder, i, j);
//						double newDistance = StatefulDrone.calculateTotalRouteDistance(newOrder, distanceMatrix);
//						if (newDistance < bestDistance) {
////							System.out.println("i = " + i + ", j = " + j);
//							bestOrder = newOrder;
//							bestDistance = newDistance;
//							improvementMade = true;
//							break currentIteration;
//						}
//					}
//				}
//			if (!improvementMade) {
//				return bestOrder;
//			}
//		}
//	}
	
}
