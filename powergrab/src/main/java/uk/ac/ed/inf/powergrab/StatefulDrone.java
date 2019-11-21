package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

public class StatefulDrone extends Drone {
	
	List<Direction> nextMoves = new ArrayList<Direction>();

	public StatefulDrone(Position position, int seed) {
		super(position, seed);
	}
	
	public Direction chooseDirection(List<ChargingStation> chargingStations) {
		if (nextMoves.size() != 0) {
			return nextMoves.remove(0);
		} else {
			return null;
		}
	}
	
}
