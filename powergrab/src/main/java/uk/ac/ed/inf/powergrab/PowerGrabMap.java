package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;
import com.mapbox.geojson.*;

public class PowerGrabMap {
	
	private List<Feature> mapFeatures;
	List<ChargingStation> chargingStations = new ArrayList<ChargingStation>(50);
	
	public PowerGrabMap(FeatureCollection f) {
		mapFeatures = f.features();
		
		for (Feature chargingStation : mapFeatures) {
			Point location = (Point) chargingStation.geometry();
			List<Double> coordinates = location.coordinates();
			double latitude = coordinates.get(1);
			double longitude = coordinates.get(0);
			double coins = chargingStation.getProperty("coins").getAsDouble();
			double power = chargingStation.getProperty("power").getAsDouble();
			chargingStations.add(new ChargingStation(new Position(latitude, longitude), coins, power));
		}
	}

	public List<ChargingStation> calculateNearbyStations(Position dronePosition) {
		List<ChargingStation> nearbyStations = new ArrayList<ChargingStation>();
		for (ChargingStation chargingStation : this.chargingStations) {
			// Need game parameters
			if (Position.calculateDistance(chargingStation.getPosition(), dronePosition) < (0.0003 + 0.00025)) {
				nearbyStations.add(chargingStation);
			}
		}
		return nearbyStations;
	}

	public ChargingStation closestStation(Position p) {
		
	}
	
	public void createGeoJSONMap(String day, String month, String year, String droneVersion) {
		
	}
	
}
