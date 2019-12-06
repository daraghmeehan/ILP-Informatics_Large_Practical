package uk.ac.ed.inf.powergrab;

/*
 * The charging stations of the PowerGrab map.
 * Their position can't be changed, and their coins and power change when the drone charges from this station.
 */
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
	
	public void transferCoins(float coinsTransfer) {
		this.coins -= coinsTransfer;
	}
	
	public void transferPower(float powerTransfer) {
		this.power -= powerTransfer;
	}
	
	public boolean equals(ChargingStation chargingStation) {
		return this.position.equals(chargingStation.position);
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
	
	/*
	 * Determines if a position is in range to charge from this station.
	 */
	public boolean isInRange(Position position) {
		return Position.calculateDistance(this.position, position) < GameParameters.CHARGING_DISTANCE;
	}
	
}
