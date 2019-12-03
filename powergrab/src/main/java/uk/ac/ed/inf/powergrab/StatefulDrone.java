package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;
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

	public void formBestStrategy(List<ChargingStation> chargingStations) {
		
		List<ChargingStation> stationsCopy1 = new ArrayList<ChargingStation>(chargingStations);
		List<ChargingStation> stationsCopy2 = new ArrayList<ChargingStation>(chargingStations);
		
		PowerGrabSimulation nnSimulation = new NearestNeighbourSimulation(this.getPosition(), stationsCopy1);
		nnSimulation.setup();
		nnSimulation.play();
		nnSimulation.report();
		float nnResult = nnSimulation.getResult();
		List<Direction> nnMoves = nnSimulation.getMoves();
		int nnMoveCount = nnMoves.size();
		
		PowerGrabSimulation nnOptimisedSimulation = new NearestNeighbourOptimisedSimulation(this.getPosition(), stationsCopy2);
		nnOptimisedSimulation.setup();
		nnOptimisedSimulation.play();
		nnOptimisedSimulation.report();
		float nnOptimisedResult = nnOptimisedSimulation.getResult();
		List<Direction> nnOptimisedMoves = nnOptimisedSimulation.getMoves();
		int nnOptimisedMoveCount = nnOptimisedMoves.size();
		
		List<Direction> bestStrategyMoves;
		
		// turn into method!
		// instead calculate better move count?
		if ((int) nnResult == (int) nnOptimisedResult) {
			if (nnMoveCount < nnOptimisedMoveCount) {
				System.out.println("Nearest Neighbour was better");
				bestStrategyMoves = nnMoves;
			} else {
				System.out.println("Nearest Neighbour Optimised was better");
				bestStrategyMoves = nnOptimisedMoves;
			}
		} else {
			if (nnResult > nnOptimisedResult) {
				System.out.println("Nearest Neighbour was better");
				bestStrategyMoves = nnMoves;			
			} else {
				System.out.println("Nearest Neighbour Optimised was better");
				bestStrategyMoves = nnOptimisedMoves;
			}
			
		}
		
		nextMoves.addAll(bestStrategyMoves);
	}
	
	// A* search // need a way of failing to get to a station
	// need to check if can go through a bad station to get more coins overall
	public static List<Direction> findShortestPath(Position startPosition, Position goalPosition, List<ChargingStation> badStations) {
//		System.out.println("Finding shortest path between " + startPosition.toString() + " and " + goalPosition.toString());
		
		// node to g-score
		TreeMap<Node, Integer> openSet = new TreeMap<Node, Integer>(new NodeComparator());
		List<Node> closedSet = new ArrayList<Node>();

		Node startNode = new Node(startPosition, new ArrayList<Direction>(), goalPosition);
		openSet.put(startNode, 0);
		
		int nodesChecked = 0;

		// right amount of nodes checked?
		while (!openSet.isEmpty() && nodesChecked < 300) {
			
			Node current = openSet.pollFirstEntry().getKey();
			
			//testing
//			System.out.println("Current node position: " + current.getPosition().toString());
//			System.out.println("Current node f-score so far: " + current.getFScore());
//			System.out.print("Current node path so far: ");
//			for (Direction d : current.getPath()) {
//				System.out.print(d + " ");		
//			}
//			System.out.println("");
			
			if (current.reachedGoal(goalPosition) && !(current.getPath().size() == 0)) {
//				System.out.println("Reached goal!");
//				System.out.println("Path length = " + current.getPath().size());
//				System.out.println("Nodes checked: " + nodesChecked);
				return current.getPath();
			}

			for (Node neighbour : current.getNeighbours(goalPosition)) {
				// what happens if all neighbours are negative?
				// but getNeighbours never returns negative results?
				if (closedSet.contains(neighbour) || StatefulDrone.isAtNegativeStation(neighbour.getPosition(), badStations)) {
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
		return null;
	}
	
	// admissible and consistent?
	public static double aStarHeuristic(Position currentPosition, Position goalPosition) {
		return Position.calculateDistance(currentPosition, goalPosition);
	}
	
	// should be in Drone?
	public static boolean isAtNegativeStation(Position position, List<ChargingStation> badStations) {
		
		for (ChargingStation badStation : badStations) {
			if (badStation.isInRange(position)) {
				return true;
			}
		}
		
		return false;
	}

	public static List<ChargingStation> calculatePositiveStations(List<ChargingStation> testStations) {

		
		List<ChargingStation> positiveStations = new ArrayList<ChargingStation>();
		
		for (ChargingStation chargingStation : testStations) {
			if (chargingStation.isPositive()) {
				positiveStations.add(chargingStation);
			}
		}
		
		return positiveStations;
	}
	
	public static List<ChargingStation> calculateNegativeStations(List<ChargingStation> testStations) {

		List<ChargingStation> negativeStations = new ArrayList<ChargingStation>();
		
		for (ChargingStation chargingStation : testStations) {
			if (chargingStation.isNegative()) {
				negativeStations.add(chargingStation);
			}
		}
		
		return negativeStations;
	}
	
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
	
}
