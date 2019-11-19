package uk.ac.ed.inf.powergrab;

import javafx.util.Pair;

public class StatelessDrone extends Drone {

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}
	
	public Move makeMove(PowerGrabMap map) {
		Position positionBefore = this.position;
		Pair<Direction, ChargingStation> directionAndChargingStation;
		charge(chargingStation);
		System.out.println(chargingStation == null);
		return null;
	}
	
	public static void main(String[] args) {
		Drone d = new StatelessDrone(new Position(0,0), 0);
		d.makeMove(null);
	}

}
