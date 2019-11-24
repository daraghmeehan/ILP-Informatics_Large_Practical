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
		
		if (this.isStrategyChosen()) {
			this.formBestStrategy(chargingStations);
		}
		
		if (nextMoves.size() != 0) {
			return nextMoves.remove(0);
		} else {
			return super.chooseRandomDirection(chargingStations);
		}
	}

	public void formBestStrategy(List<ChargingStation> chargingStations) {
		List<ChargingStation> testStations1 = new ArrayList<ChargingStation>(chargingStations);
		List<ChargingStation> testStations2 = new ArrayList<ChargingStation>(chargingStations);
	}
	
	// A* search
	public List<Direction> findShortestPath(Position startPosition, Position goalPosition) {
		
//		List<Direction> moves = new ArrayList<Direction>();
		TreeMap<Node, Integer> openSet = new TreeMap<Node, Integer>(new NodeComparator());
		List<Node> closedSet = new ArrayList<Node>();

		Node startNode = new Node(startPosition, new ArrayList<Direction>(), goalPosition);
		//		startNode.calculateF
		openSet.put(startNode, 0);

		while (!openSet.isEmpty()) {
			Node current = openSet.firstKey();
			if (current.reachedGoal(goalPosition)) {
				return current.getPath();
			}

			for (Node neighbour : current.getNeighbours(goalPosition)) {
				if (closedSet.contains(neighbour)) {
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

				closedSet.add(current);
			}
		}
		// need to check if null returned!
		return null;
	}
	
	// admissible and consistent?
	public static double aStarHeuristic(Position currentPosition, Position goalPosition) {
		return Position.calculateDistance(currentPosition, goalPosition);
	}
	
}
