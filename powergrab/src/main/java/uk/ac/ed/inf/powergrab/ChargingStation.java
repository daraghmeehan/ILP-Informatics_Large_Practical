package uk.ac.ed.inf.powergrab;

public class ChargingStation {
	
	public final Position position;
	private float coins;
	private float power;
	
	public ChargingStation(Position position, float coins, float power) {
		this.position = position;
		this.coins = coins;
		this.power = power;
	}
	
	public float getCoins() {
		return this.coins;
	}
	
	public float getPower() {
		return this.power;
	}
	
	public boolean isPositive() {
		return this.coins > 0;
	}
	
	public boolean isNegative() {
		return this.coins < 0;
	}
	
	// necessary?
	public boolean isNeutral() {
		return this.coins == 0;
	}

	public boolean isInRange(Position position) {
		// game parameters
		return Position.calculateDistance(this.position, position) < GameParameters.chargingDistance;
	}

	public void transferCoins(float coinsTransfer) {
		this.coins -= coinsTransfer;
	}

	public void transferPower(float powerTransfer) {
		this.power -= powerTransfer;
	}
	
}
