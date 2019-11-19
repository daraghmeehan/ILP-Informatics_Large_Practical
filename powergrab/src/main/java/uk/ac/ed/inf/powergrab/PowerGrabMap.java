package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.mapbox.geojson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PowerGrabMap {
	
	private List<Feature> mapFeatures;
	LineString dronePath = LineString.fromLngLats(new ArrayList<Point>());
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

//	public ChargingStation closestStation(Position p) {
//		
//	}
	
	public void addDronePath(Position positionBefore, Position positionAfter) {
		
		if (this.dronePath.coordinates().size() == 0) {
			Point coordinatesBefore = Point.fromLngLat(positionBefore.getLongitude(), positionBefore.getLatitude());
			dronePath.coordinates().add(coordinatesBefore);
		}
		
		Point coordinatesAfter = Point.fromLngLat(positionAfter.getLongitude(), positionAfter.getLatitude());
		dronePath.coordinates().add(coordinatesAfter);			
	}
	
	public void createGeoJSONMap(String day, String month, String year, String droneVersion) {
		mapFeatures.add(Feature.fromGeometry(this.dronePath, new JsonObject()));
		FeatureCollection mapFeatureCollection = FeatureCollection.fromFeatures(mapFeatures);
		String mapJSON = mapFeatureCollection.toJson();
		String fileName = droneVersion + "-" + day + "-" + month + "-" + year + ".geojson";
		
		try (PrintWriter out = new PrintWriter(new FileOutputStream(new File(
				"src" + File.separator + "Output Files" + File.separator + fileName), false))) {
		    out.println(mapJSON);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
