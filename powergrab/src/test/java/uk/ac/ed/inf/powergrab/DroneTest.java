package uk.ac.ed.inf.powergrab;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class DroneTest {

	@Test
	public void testCalculateAvailableDirections() {
		Drone d1 = new StatelessDrone(new Position(55.944,-3.18432), 0);
		List<Direction> l = Drone.calculateAvailableDirections(d1.getPosition());
		for (Direction dir : l) {
//			System.out.println(dir.toString());
		}
		
		Drone d2 = new StatelessDrone(new Position(0, 0), 0);
		assertTrue(Drone.calculateAvailableDirections(d2.getPosition()).size() == 0);
	}
	
	@Test
	public void testCalculateClosestStation() {
		Drone d = new StatelessDrone(new Position(0, 0), 0);
		List<ChargingStation> chargingStations1 = new ArrayList<ChargingStation>();
//		List<ChargingStation> chargingStations2 = new ArrayList<ChargingStation>();
		
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

	@Test
	public void testCalculateNearbyStations() {
		
	}
	
	@Test
	public void testRandomChoice() {
		
	}
	
//	// can't?
//	@Test
//	public void testCharge() {
//		Drone d1 = new StatelessDrone(new Position(0,0), 0);
//		ChargingStation cS1 = new ChargingStation(new Position(0,0), 5, 10);
//		d1.charge(cS1);
//		assertTrue(d1.getCoins() == 5);
//		assertTrue(d1.getPower() == 260);
//		assertTrue(cS1.getCoins() == 0);
//		assertTrue(cS1.getPower() == 0);
//		
//		Drone d2 = new StatelessDrone(new Position(0,0), 0);
//		ChargingStation cS2 = new ChargingStation(new Position(0,0), -5, -2);
//		d2.charge(cS2);
//		assertTrue(d2.getCoins() == 0);
//		assertTrue(d2.getPower() == 248);
//		assertTrue(cS2.getCoins() == -5);
//		assertTrue(cS2.getPower() == 0);
//		
//		Drone d3 = new StatelessDrone(new Position(0,0), 0);
//		ChargingStation cS3 = new ChargingStation(new Position(0,0), 0, -250);
//		d3.charge(cS3);
//		assertTrue(d3.getCoins() == 0);
//		assertTrue(d3.getPower() == 0);
//		assertTrue(cS3.getCoins() == 0);
//		assertTrue(cS3.getPower() == 0);
//		
//		Drone d4 = new StatelessDrone(new Position(0,0), 0);
//		ChargingStation cS4 = new ChargingStation(new Position(0,0), 0, -300);
//		d4.charge(cS4);
//		assertTrue(d4.getCoins() == 0);
//		assertTrue(d4.getPower() == 0);
//		assertTrue(cS4.getCoins() == 0);
//		assertTrue(cS4.getPower() == -50);
//		
//	}

}
