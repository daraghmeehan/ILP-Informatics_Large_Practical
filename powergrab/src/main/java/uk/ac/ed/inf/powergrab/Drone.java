package uk.ac.ed.inf.powergrab;

// Is this necessary here?
import java.util.*;
import javafx.util.Pair;

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
	
	public Move makeMove(PowerGrabMap map) {
		Position positionBefore = this.position;
		
		Pair<Direction, ChargingStation> directionAndChargingStation = chooseDirection(map);
		Direction moveDirection = directionAndChargingStation.getKey();
		ChargingStation chargingStation = directionAndChargingStation.getValue();
		
		if (chargingStation != null) {
			charge(chargingStation);
		}
		
		Position positionAfter = this.position.nextPosition(moveDirection);
		double coinsAfter = this.coins;
		double powerAfter = this.power;
		return new Move(positionBefore, moveDirection, positionAfter, coinsAfter, powerAfter);
	}
	
	public abstract Pair<Direction, ChargingStation> chooseDirection(PowerGrabMap map);

	public List<Direction> calculateAvailableDirections() {
		List<Direction> availableDirections = new ArrayList<Direction>();
		for (Direction d : Direction.values()) {
			Position newPosition = this.position.nextPosition(d);
			if (newPosition.inPlayArea()) {
				availableDirections.add(d);
			}
		}
		return availableDirections;
	}
	
	public Pair<Direction, ChargingStation> chooseRandomDirection(PowerGrabMap map) {
		List<ChargingStation> nearbyStations = map.calculateNearbyStations(this.position);
		List<Direction> availableDirections = this.calculateAvailableDirections();
		
		Move bestPositiveMove;
		Move bestNegativeMove;
		List<Move> neutralMoves = new ArrayList<Move>();
		
		return null;
//		for (Direction d : availableDirections) {
//			Position newPosition = this.position.nextPosition(d);
//			ChargingStation closestStation = map.closestStation(newPosition);
//			if (closestStation.isInRange(newPosition)) {
//				;
//				if (closestStation.isPositive()) {
//					positiveDirections++;
//				} else if (closestStation.isNegative()) {
//					negativeDirections++;
//				} else {
//					neutralStations++;
//				}
//			} else {
//				directionOutcomes.put(d, null);
//				
//			}
//		}
		
	}
	
	public void charge(ChargingStation chargingStation) {
		
	}
	
	
//	public static List<ChargingStation> calculateNearbyStations(Position position) {
//		List<ChargingStation> nearbyStations = new ArrayList<ChargingStation>();
//		for (ChargingStation chargingStation : ChargingStations) {
//			if (chargingStation)
//		}
//	}
	
	
//	testing
//	public static void main(String[] args) {
//		Drone d = new Drone(new Position(55.944,-3.18432), 40);
//		List<Direction> l = d.calculateAvailableMoves();
//		for (Direction dir : l) {
//			System.out.println(dir.toString());
//		}
//	}
	
}
