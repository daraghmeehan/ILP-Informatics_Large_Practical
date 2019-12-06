package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.mapbox.geojson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/*
 * Here the features of the GeoJSON representation of our PowerGrab map are stored, as well as our drone's flight path.
 */
public class Map {
	
	private List<Feature> mapFeatures;
	private LineString dronePath = LineString.fromLngLats(new ArrayList<Point>());
	
	// should be public?
	public Map(FeatureCollection f) {
		mapFeatures = f.features();
	}
	
	/*
	 * Builds a list of charging stations from the features.
	 */
	public List<ChargingStation> getChargingStations() {
		
		List<ChargingStation> chargingStations = new ArrayList<ChargingStation>(50);
		
		for (Feature chargingStation : mapFeatures) {
			Point location = (Point) chargingStation.geometry();
			List<Double> coordinates = location.coordinates();
			double latitude = coordinates.get(1);
			double longitude = coordinates.get(0);
			float coins = chargingStation.getProperty("coins").getAsFloat();
			float power = chargingStation.getProperty("power").getAsFloat();
			chargingStations.add(new ChargingStation(new Position(latitude, longitude), coins, power));
		}
		
		return chargingStations;
	}
	
	/*
	 * Adds a move to the drone's path.
	 */
	public void addDronePath(Position positionBefore, Position positionAfter) {
		
		if (this.dronePath.coordinates().size() == 0) {
			Point coordinatesBefore = Point.fromLngLat(positionBefore.longitude, positionBefore.latitude);
			dronePath.coordinates().add(coordinatesBefore);
		}
		
		Point coordinatesAfter = Point.fromLngLat(positionAfter.longitude, positionAfter.latitude);
		dronePath.coordinates().add(coordinatesAfter);			
	}
	
	/*
	 * Builds a .geojson file from the charging station and drone path features.
	 */
	public void createGeoJSONMap(String day, String month, String year, String droneVersion) {
		mapFeatures.add(Feature.fromGeometry(this.dronePath, new JsonObject()));
		FeatureCollection mapFeatureCollection = FeatureCollection.fromFeatures(mapFeatures);
		String mapJSON = mapFeatureCollection.toJson();
		String fileName = droneVersion + "-" + day + "-" + month + "-" + year + ".geojson";
		
		try (PrintWriter out = new PrintWriter(new FileOutputStream(new File(fileName), false))) {
		    out.println(mapJSON);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
