package uk.ac.ed.inf.powergrab;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.List;

public class DroneTest {

	@Test
	public void testCalculateAvailableDirections() {
		Drone d = new StatelessDrone(new Position(55.944,-3.18432), 40);
		List<Direction> l = d.calculateAvailableDirections();
		for (Direction dir : l) {
			System.out.println(dir.toString());
		}
	}
	
	@Test
	public void testCalculateClosestStation() {
		
	}

	@Test
	public void testCalculateNearbyStations() {
		
	}
	
	@Test
	public void testRandomChoice() {
		
	}
	
	@Test
	public void testCharge() {
		Drone d1 = new StatelessDrone(new Position(0,0), 0);
		ChargingStation cS1 = new ChargingStation(new Position(0,0), 5, 10);
		d1.charge(cS1);
		assertTrue(d1.getCoins() == 5);
		assertTrue(d1.getPower() == 260);
		assertTrue(cS1.getCoins() == 0);
		assertTrue(cS1.getPower() == 0);
		
		Drone d2 = new StatelessDrone(new Position(0,0), 0);
		ChargingStation cS2 = new ChargingStation(new Position(0,0), -5, -2);
		d2.charge(cS2);
		assertTrue(d2.getCoins() == 0);
		assertTrue(d2.getPower() == 248);
		assertTrue(cS2.getCoins() == -5);
		assertTrue(cS2.getPower() == 0);
		
		Drone d3 = new StatelessDrone(new Position(0,0), 0);
		ChargingStation cS3 = new ChargingStation(new Position(0,0), 0, -250);
		d3.charge(cS3);
		assertTrue(d3.getCoins() == 0);
		assertTrue(d3.getPower() == 0);
		assertTrue(cS3.getCoins() == 0);
		assertTrue(cS3.getPower() == 0);
		
		Drone d4 = new StatelessDrone(new Position(0,0), 0);
		ChargingStation cS4 = new ChargingStation(new Position(0,0), 0, -300);
		d4.charge(cS4);
		assertTrue(d4.getCoins() == 0);
		assertTrue(d4.getPower() == 0);
		assertTrue(cS4.getCoins() == 0);
		assertTrue(cS4.getPower() == -50);
		
	}

}
