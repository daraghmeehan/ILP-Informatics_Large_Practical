package uk.ac.ed.inf.powergrab;

// Is this necessary here?
import java.util.*;

public abstract class Drone {
	
	Position position;
	double power;
	double coins;
//	int seed;
	private Random rnd;
	
	public Drone(Position position, int seed) {
		this.position = position;
//		this.seed = seed;
		power = 250;
		coins = 0;
		rnd = new Random(seed);
		// set in game parameters
	}
	
	public boolean canMove() {
		return power >= 1.25;
	}
	
	public abstract void makeMove();
	
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
	
	public void makeRandomMove() {
		List<Direction> availableMoves = this.calculateAvailableMoves();
		
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
