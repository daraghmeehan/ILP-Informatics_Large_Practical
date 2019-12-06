package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

/*
 * Represents a node in our A* search algorithm.
 */
public class Node {
	
	public final Position position;
	private List<Direction> path;
	private double fScore;
	private int gScore;
	
	/*
	 * Sets the f-score to g-score + heuristic.
	 */
	public Node(Position position, List<Direction> path, Position goalPosition) {
		this.position = position;
		this.path = path;
		this.gScore = path.size();
		this.fScore = this.gScore + (StatefulDrone.aStarHeuristic(this.position, goalPosition)/0.0003);
	}
	
	public List<Direction> getPath() {
		return this.path;
	}

	public double getFScore() {
		return this.fScore;
	}
	
	public int getGScore() {
		return this.gScore;
	}
	
	/*
	 * Calculates if the given node has reached the goal.
	 */
	public boolean reachedGoal(ChargingStation goalStation, List<ChargingStation> chargingStations) {
		return (goalStation.isInRange(this.position)
				&& goalStation.equals(Drone.calculateClosestStation(this.position, chargingStations)));
	}
	
	public boolean equals(Node n) {
		return this.position.equals(n.position);
	}
	
	/*
	 * Calculates the neighbouring nodes of a given node.
	 */
	public List<Node> getNeighbours(Position goalPosition) {
		
		List<Node> neighbouringNodes = new ArrayList<Node>();
		List<Direction> availableDirections = Drone.calculateAvailableDirections(this.position);
		
		for (Direction d : availableDirections) {
			Position newNodePosition = this.position.nextPosition(d);
			
			List<Direction> newNodePath = new ArrayList<Direction>(this.path);
			newNodePath.add(d);
			
			Node node = new Node(newNodePosition, newNodePath, goalPosition);
			neighbouringNodes.add(node);
		}
		return neighbouringNodes;
	}
	
}
