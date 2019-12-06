package uk.ac.ed.inf.powergrab;

import java.util.List;

public class StatelessDrone extends Drone {

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}
	
	@Override
	public Direction chooseDirection(List<ChargingStation> chargingStations) {
		return super.chooseRandomDirection(chargingStations);
	}
	
}
