package uk.ac.ed.inf.powergrab;

// Is this necessary here?
import java.util.*;

public class Drone {
	
	Position position;
	int seed;
	
	public Drone(Position position, int seed) {
		this.position = position;
		this.seed = seed;
	}
	
	public List<Direction> calculateAvailableMoves() {
		List<Direction> availableMoves = new ArrayList<Direction>();
		for (Direction d : Direction.values()) {
			Position newPosition = this.position.nextPosition(d);
			if (newPosition.inPlayArea()) {
				availableMoves.add(d);
			}
		}
		return availableMoves;
	}
	
	
	
//	testing
//	public static void main(String[] args) {
//		Drone d = new Drone(new Position(55.944,-3.18432), 40);
//		List<Direction> l = d.calculateAvailableMoves();
//		for (Direction dir : l) {
//			System.out.println(dir.toString());
//		}
//	}
	
}
