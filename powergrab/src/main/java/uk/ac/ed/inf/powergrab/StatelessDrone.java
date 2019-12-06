package uk.ac.ed.inf.powergrab;

import java.util.List;

/*
 * The implementation of the stateless drone as specified in the coursework document.
 */
public class StatelessDrone extends Drone {

	public StatelessDrone(Position position, int seed) {
		super(position, seed);
	}
	
	/*
	 * Uses the abstract Drone class's pseudorandom decision of move direction to make its choice.
	 */
	@Override
	public Direction chooseDirection(List<ChargingStation> chargingStations) {
		return super.chooseRandomDirection(chargingStations);
	}
	
}
