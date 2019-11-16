package uk.ac.ed.inf.powergrab;

public class ChargingStation {
	
	Position position;
	double coins;
	double power;
	
	public ChargingStation(Position position, double coins, double power) {
		this.position = position;
		this.coins = coins;
		this.power = power;
	}
	
}
