package uk.ac.ed.inf.powergrab;

public class Move {
	
	Position positionBefore;
	Direction direction;
	Position positionAfter;
//	ChargingStation chargingStation;
	double coinsAfter;
	double powerAfter;
	
	public Move(Direction direction, ChargingStation chargingStation) {
		this.direction = direction;
//		this.chargingStation = chargingStation;
	}
	
	@Override
	public String toString() {
		return "";
	}
	
}
