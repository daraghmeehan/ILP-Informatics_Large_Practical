package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

public class Node {
	
	private Position position;
	private List<Direction> path;
	private double fScore;
	private int gScore;
	
	public Node(Position position, List<Direction> path, Position goalPosition) {
		this.position = position;
		this.path = path;
		this.gScore = path.size();
//		 should this be here instead of drone?
		this.fScore = this.gScore + StatefulDrone.aStarHeuristic(this.position, goalPosition);
	}
	

	
	public Position getPosition() {
		return this.position;
	}
	
	public List<Direction> getPath() {
		return this.path;
	}

	public double getFScore() {
		return this.fScore;
	}
	
	public int getGScore() {
		return this.getGScore();
	}
	
	public boolean reachedGoal(Position goalPosition) {
		// game parameters
		return Position.calculateDistance(this.position, goalPosition) < 0.00025;
	}
	
	public boolean equals(Node n) {
		return this.position.equals(n.getPosition());
	}
	
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
