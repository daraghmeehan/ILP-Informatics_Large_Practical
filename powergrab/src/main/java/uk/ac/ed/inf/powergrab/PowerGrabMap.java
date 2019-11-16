package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.geojson.*;

public class PowerGrabMap {
	
	List<Feature> MapFeatures;
	List<ChargingStation> ChargingStations = new ArrayList<ChargingStation>(50);
	
	public PowerGrabMap(FeatureCollection f) {
		MapFeatures = f.features();
		
		for (Feature chargingStation : MapFeatures) {
			Point location = (Point) chargingStation.geometry();
			List<Double> coordinates = location.coordinates();
			double latitude = coordinates.get(1);
			double longitude = coordinates.get(0);
			double coins = chargingStation.getProperty("coins").getAsDouble();
			double power = chargingStation.getProperty("power").getAsDouble();
			ChargingStations.add(new ChargingStation(new Position(latitude, longitude), coins, power));
		}
	}
	
}
