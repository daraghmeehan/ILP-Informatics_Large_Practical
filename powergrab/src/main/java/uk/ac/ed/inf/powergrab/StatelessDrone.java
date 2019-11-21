package uk.ac.ed.inf.powergrab;

import java.util.List;
import javafx.util.Pair;

public class StatelessDrone extends Drone {

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}
	
	@Override
	public Direction chooseDirection(List<ChargingStation> chargingStations) {
		return super.chooseRandomDirection(chargingStations);
	}
	
	public static void main(String[] args) {
		Drone d = new StatelessDrone(new Position(0,0), 0);
		d.makeMove(null);
	}
	
}
