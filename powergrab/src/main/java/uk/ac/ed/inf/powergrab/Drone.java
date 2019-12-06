package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/*
 * The Drone of the PowerGrab simulation, instantiated by a StatelessDrone or StatefulDrone object.
 */
public abstract class Drone {
	
	private Position position;
	private float coins;
	private float power;
	private final Random rnd;
	
	public Drone(Position position, int seed) {
		this.position = position;
		this.coins = GameParameters.INITIAL_DRONE_COINS;
		this.power = GameParameters.INITIAL_DRONE_POWER;
		this.rnd = new Random(seed);
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	/*
	 * Calculates if the drone has enough power to move.
	 */
	public boolean canMove() {
		return power >= GameParameters.MOVE_POWER_COST;
	}
	
	/*
	 * Determines the drone's next direction to move in, moves in this direction, charges from the
	 * nearest power station if in range and constructs a Move object with the details of the moves.
	 */
	public Move makeMove(List<ChargingStation> chargingStations) {
		
		Position positionBefore = this.position;
		Direction moveDirection = this.chooseDirection(chargingStations);
		Position positionAfter = this.position.nextPosition(moveDirection);
		this.consumePower();
		this.position = positionAfter;
		
		ChargingStation closestStation = Drone.calculateClosestStation(positionAfter, chargingStations);
		if (closestStation.isInRange(this.position)) {
			this.charge(closestStation);
		}
		
		float coinsAfter = this.coins;
		float powerAfter = this.power;
		
		return new Move(positionBefore, moveDirection, positionAfter, coinsAfter, powerAfter);
	}
	
	/*
	 * The direction chosen is determined by the individual drone's strategy algorithm.
	 */
	public abstract Direction chooseDirection(List<ChargingStation> chargingStations);
	
	/*
	 * Consumes power after a move.
	 */
	private void consumePower() {
		this.power -= GameParameters.MOVE_POWER_COST;
	}
	
	/*
	 * Charges the drone from a given station. Assumes this station is the closest and in range.
	 */
	private void charge(ChargingStation chargingStation) {
		float maxCoinsToLose = - this.coins;
		float maxPowerToLose = - this.power;
		
		float stationCoins = chargingStation.getCoins();
		float stationPower = chargingStation.getPower();
		
		float coinsTransfer;
		float powerTransfer;
		
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
	
	/*
	 * Makes a pseudorandom decision of the direction to move in favouring
	 * nearby positive stations and avoiding negative ones.
	 */
	public Direction chooseRandomDirection(List<ChargingStation> chargingStations) {

		List<Direction> availableDirections = Drone.calculateAvailableDirections(this.position);
		List<ChargingStation> nearbyStations = this.calculateNearbyStations(chargingStations);

		Direction bestMove = null;
		float bestMoveCoins = Float.NEGATIVE_INFINITY;
		List<Direction> neutralMoves = new ArrayList<Direction>();

		if (nearbyStations.size() == 0) {
			neutralMoves.addAll(availableDirections);
		} else {
			for (Direction d : availableDirections) {
				Position newPosition = this.position.nextPosition(d);
				ChargingStation closestStation = Drone.calculateClosestStation(newPosition, nearbyStations);
				if (closestStation.isInRange(newPosition)) {
					float coins = closestStation.getCoins();
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
		
		if (bestMoveCoins > 0 || neutralMoves.size() == 0) {		
			return bestMove;
		} else {
			return this.makeRandomChoice(neutralMoves);
		}
		
	}
	
	/*
	 * Calculates the available directions to move in for a given position.
	 */
	public static List<Direction> calculateAvailableDirections(Position position) {
		
		List<Direction> availableDirections = new ArrayList<Direction>();
		
		for (Direction d : Direction.values()) {
			Position newPosition = position.nextPosition(d);
			if (newPosition.inPlayArea()) {
				availableDirections.add(d);
			}
		}
		
		return availableDirections;
		
	}
	
	/*
	 * Calculates the closest charging station to a given position.
	 */
	public static ChargingStation calculateClosestStation(Position position, List<ChargingStation> chargingStations) {
		
		ChargingStation closestStation;
		
		if (chargingStations.isEmpty()) {
			closestStation = null;
		} else {
			closestStation = chargingStations.get(0);
			
			if (chargingStations.size() > 1) {
				double closestDistance = Position.calculateDistance(position, closestStation.position);
				
				for (ChargingStation chargingStation : chargingStations.subList(1, chargingStations.size())) {
					double distance = Position.calculateDistance(position, chargingStation.position);
					if (distance < closestDistance){
						closestStation = chargingStation;
						closestDistance = distance;
					}
				}	
			}
		}
		
		return closestStation;
	}
	
	/*
	 * Calculates the stations possible to visit in only one move to avoid calculating for every station when making
	 * a pseudorandom decision about the direction to move in.
	 */
	private List<ChargingStation> calculateNearbyStations(List<ChargingStation> chargingStations) {
		
		List<ChargingStation> nearbyStations = new ArrayList<ChargingStation>();
		for (ChargingStation chargingStation : chargingStations) {
			if (Position.calculateDistance(this.position, chargingStation.position)
					< (GameParameters.MOVE_DISTANCE + GameParameters.CHARGING_DISTANCE)) {
				nearbyStations.add(chargingStation);
			}
		}
		return nearbyStations;
	}
	
	/*
	 * Chooses pseudorandomly between the given directions using the Drone's Random rnd instance.
	 */
	private Direction makeRandomChoice(List<Direction> directions) {
		int n = directions.size();
		int chosenDirection = rnd.nextInt(n);
		return directions.get(chosenDirection);
	}

}
