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
	
	/*
	 * Makes a transfer of coins when the drone is charged.
	 */
	public void transferCoins(float coinsTransfer) {
		this.coins -= coinsTransfer;
	}
	
	/*
	 * Makes a transfer of power when the drone is charged.
	 */
	public void transferPower(float powerTransfer) {
		this.power -= powerTransfer;
	}
	
	/*
	 * ChargingStation objects are equivalent if they are in the same position, but may have different coins/power.
	 * No two stations can be in the same position.
	 */
	public boolean equals(ChargingStation chargingStation) {
		return this.position.equals(chargingStation.position);
	}
	
	public boolean isPositive() {
		return this.coins > 0;
	}
	
	public boolean isNegative() {
		return this.coins < 0;
	}
	
	/*
	 * Determines if a position is in range to charge from this station.
	 */
	public boolean isInRange(Position position) {
		return Position.calculateDistance(this.position, position) < GameParameters.CHARGING_DISTANCE;
	}
	
}
