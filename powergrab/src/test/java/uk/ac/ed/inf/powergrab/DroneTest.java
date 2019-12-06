package uk.ac.ed.inf.powergrab;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class DroneTest {

	@Test
	public void testCalculateAvailableDirections() {
		
		Drone d1 = new StatelessDrone(new Position(GameParameters.MIN_LATITUDE - GameParameters.MOVE_DISTANCE,
				GameParameters.MIN_LONGITUDE - GameParameters.MOVE_DISTANCE), 0);
		List<Direction> l1 = Drone.calculateAvailableDirections(d1.getPosition());
		assertTrue(l1.size() == 0);
		
		Drone d2 = new StatelessDrone(new Position(
				GameParameters.MIN_LATITUDE, GameParameters.MIN_LONGITUDE), 0);
		List<Direction> l2 = Drone.calculateAvailableDirections(d2.getPosition());
		assertTrue(l2.size() == 3);
		
		Drone d3 = new StatelessDrone(new Position(
				GameParameters.MIN_LATITUDE + 0.00001, GameParameters.MIN_LONGITUDE + 0.00001), 0);
		List<Direction> l3 = Drone.calculateAvailableDirections(d3.getPosition());
		assertTrue(l3.size() == 5);
		
		Drone d4 = new StatelessDrone(new Position(
				(GameParameters.MIN_LATITUDE + GameParameters.MAX_LATITUDE) / 2,
				GameParameters.MIN_LONGITUDE), 0);
		List<Direction> l4 = Drone.calculateAvailableDirections(d4.getPosition());
		assertTrue(l4.size() == 7);
		
		Drone d5 = new StatelessDrone(new Position(
				(GameParameters.MIN_LATITUDE + GameParameters.MAX_LATITUDE) / 2,
				GameParameters.MIN_LONGITUDE + 0.00001), 0);
		List<Direction> l5 = Drone.calculateAvailableDirections(d5.getPosition());
		assertTrue(l5.size() == 9);
		
		Drone d6 = new StatelessDrone(new Position(
				(GameParameters.MIN_LATITUDE + GameParameters.MAX_LATITUDE) / 2,
				GameParameters.MIN_LONGITUDE + GameParameters.MOVE_DISTANCE), 0);
		List<Direction> l6 = Drone.calculateAvailableDirections(d6.getPosition());
		assertTrue(l6.size() == 15);
		
		Drone d7 = new StatelessDrone(new Position(
				(GameParameters.MIN_LATITUDE + GameParameters.MAX_LATITUDE) / 2,
				(GameParameters.MIN_LONGITUDE + GameParameters.MAX_LONGITUDE) / 2), 0);
		List<Direction> l7 = Drone.calculateAvailableDirections(d7.getPosition());
		assertTrue(l7.size() == 16);
		
	}
	
	@Test
	public void testCalculateClosestStation() {
		Drone d = new StatelessDrone(new Position(0, 0), 0);
		List<ChargingStation> chargingStations1 = new ArrayList<ChargingStation>();
		
		chargingStations1.add(new ChargingStation(new Position(1, 1), 0, 0));
		chargingStations1.add(new ChargingStation(new Position(-0.5, 1), 0, 0));
		chargingStations1.add(new ChargingStation(new Position(1, 1.5), 0, 0));
		chargingStations1.add(new ChargingStation(new Position(-1, 1.3), 0, 0));
		
		assertTrue(Drone.calculateClosestStation(d.getPosition(), chargingStations1).equals(chargingStations1.get(1)));
		chargingStations1.remove(1);
		assertTrue(Drone.calculateClosestStation(d.getPosition(), chargingStations1).equals(chargingStations1.get(0)));
		chargingStations1.remove(0);
		assertTrue(Drone.calculateClosestStation(d.getPosition(), chargingStations1).equals(chargingStations1.get(1)));
		chargingStations1.remove(1);
		assertTrue(Drone.calculateClosestStation(d.getPosition(), chargingStations1).equals(chargingStations1.get(0)));
	}

}
