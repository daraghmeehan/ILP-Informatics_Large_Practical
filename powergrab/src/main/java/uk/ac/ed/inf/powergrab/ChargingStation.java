package uk.ac.ed.inf.powergrab;

public class ChargingStation {
	
	private Position position;
	private double coins;
	private double power;
	
	public ChargingStation(Position position, double coins, double power) {
		this.position = position;
		this.coins = coins;
		this.power = power;
	}
	
	// necessary?
	public Position getPosition() {
		return this.position;
	}
	
	public double getCoins() {
		return this.coins;
	}
	
	public double getPower() {
		return this.power;
	}
	
	public boolean isPositive() {
		return this.coins > 0;
	}
	
	public boolean isNegative() {
		return this.coins < 0;
	}
	
	public boolean isNeutral() {
		return this.coins == 0;
	}

	public boolean isInRange(Position position) {
		// game parameters
		return Position.calculateDistance(this.position, position) < 0.00025;
	}
	
}
