package uk.ac.ed.inf.powergrab;

// Is this necessary here?
import java.util.*;
import javafx.util.Pair;

public abstract class Drone {
	
	private Position position;
	private double coins;
	private double power;
	private Random rnd;
	
	public Drone(Position position, int seed) {
		this.position = position;
		// game parameters
		this.coins = 0;
		this.power = 250;
		this.rnd = new Random(seed);
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public double getCoins() {
		return this.coins;
	}
	
	public double getPower() {
		return this.power;
	}
	
	public boolean canMove() {
		return power >= 1.25;
	}
	
	public void consumePower() {
		// game params
		this.power -= 1.25;
	}
	
	public Move makeMove(List<ChargingStation> chargingStations) {
		Position positionBefore = this.position;
//		System.out.println("Before: " + positionBefore.toString());
		
		Direction moveDirection = this.chooseDirection(chargingStations);
//		System.out.println("Direction: " + moveDirection);
		Position positionAfter = this.position.nextPosition(moveDirection);
		this.consumePower();
		this.position = positionAfter;
//		System.out.println("After: " + positionAfter.toString());
		
		// doesn't change map's station values?
		ChargingStation closestStation = Drone.calculateClosestStation(positionAfter, chargingStations);
//		System.out.println("Distance to closestStation = " + Position.calculateDistance(positionAfter, closestStation.getPosition()));
		// game parameters
		if (closestStation.isInRange(this.position)) {
			this.charge(closestStation);
		}
		
		double coinsAfter = this.coins;
		double powerAfter = this.power;
		
		return new Move(positionBefore, moveDirection, positionAfter, coinsAfter, powerAfter);
	}
	
	public abstract Direction chooseDirection(List<ChargingStation> chargingStations);

	public Direction chooseRandomDirection(List<ChargingStation> chargingStations) {
		
		//need to check if null?
		List<Direction> availableDirections = Drone.calculateAvailableDirections(this.position);
		
		// do this for all directions?
		List<ChargingStation> nearbyStations = this.calculateNearbyStations(chargingStations);
		
		Direction bestMove = null;
		double bestMoveCoins = Double.NEGATIVE_INFINITY;
		List<Direction> neutralMoves = new ArrayList<Direction>();
		
		if (nearbyStations.size() == 0) {
			neutralMoves.addAll(availableDirections);
		} else {
			for (Direction d : availableDirections) {
				Position newPosition = this.position.nextPosition(d);
				ChargingStation closestStation = Drone.calculateClosestStation(newPosition, nearbyStations);
				if (closestStation.isInRange(newPosition)) {
					double coins = closestStation.getCoins();
					// how sophisticated needed?
					if (bestMove == null) {
						bestMove = d;
						bestMoveCoins = coins;
					}

					if (coins > bestMoveCoins) {
						bestMove = d;
						bestMoveCoins = coins;
					} 

					if (coins == 0) {
						neutralMoves.add(d);
					}
				} else {
					neutralMoves.add(d);
				}
			}
		}
		//spacing?
		if (bestMoveCoins > 0) {		
			return bestMove;
		} else {
			return this.makeRandomChoice(neutralMoves);
		}
	}
	
	public static List<Direction> calculateAvailableDirections(Position currentPosition) {
		
		List<Direction> availableDirections = new ArrayList<Direction>();
		
		for (Direction d : Direction.values()) {
			Position newPosition = currentPosition.nextPosition(d);
			if (newPosition.inPlayArea()) {
				availableDirections.add(d);
			}
		}
		
		return availableDirections;
		
	}
	
	public static ChargingStation calculateClosestStation(Position position, List<ChargingStation> chargingStations) {
		
		ChargingStation closestStation;
		
		if (chargingStations.isEmpty()) {
			closestStation = null;
		} else {
			closestStation = chargingStations.get(0);
			
			if (chargingStations.size() > 1) {
				double closestDistance = Position.calculateDistance(position, closestStation.getPosition());
				
				for (ChargingStation chargingStation : chargingStations.subList(1, chargingStations.size())) {
					double distance = Position.calculateDistance(position, chargingStation.getPosition());
					if (distance < closestDistance){
						closestStation = chargingStation;
						closestDistance = distance;
					}
				}	
			}
		}
		// spacing here?
		return closestStation;
	}

	public List<ChargingStation> calculateNearbyStations(List<ChargingStation> chargingStations) {
		
		List<ChargingStation> nearbyStations = new ArrayList<ChargingStation>();
		for (ChargingStation chargingStation : chargingStations) {
			// Need game parameters (maybe with isInRange with variable parameters)
			if (Position.calculateDistance(this.position, chargingStation.getPosition()) < (0.0003 + 0.00025)) {
				nearbyStations.add(chargingStation);
			}
		}
		return nearbyStations;
	}
	
	public Direction makeRandomChoice(List<Direction> directions) {
		int n = directions.size();
		int chosenDirection = rnd.nextInt(n);
		return directions.get(chosenDirection);
	}

	public void charge(ChargingStation chargingStation) {
		double maxCoinsToLose = - this.coins;
		double maxPowerToLose = - this.power;
		
		double stationCoins = chargingStation.getCoins();
		double stationPower = chargingStation.getPower();
		
		double coinsTransfer;
		double powerTransfer;
		
		if (stationCoins < maxCoinsToLose) {
			coinsTransfer = maxCoinsToLose;
		} else {
			coinsTransfer = stationCoins;
		}
		
		if (stationPower < maxPowerToLose) {
			powerTransfer = maxPowerToLose;
		} else {
			powerTransfer = stationPower;
		}
		
		this.coins += coinsTransfer;
		this.power += powerTransfer;
		chargingStation.transferCoins(coinsTransfer);
		chargingStation.transferPower(powerTransfer);
	}

}
