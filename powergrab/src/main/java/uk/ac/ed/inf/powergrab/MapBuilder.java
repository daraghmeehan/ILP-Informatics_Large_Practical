package uk.ac.ed.inf.powergrab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mapbox.geojson.FeatureCollection;

/*
 * Builds a Map for the given date.
 */
public class MapBuilder {
	
	/*
	 * Formats a valid URL. Connects to the address and reads the map, creates a feature collection, and constructs the map.
	 */
	public static Map build(String day, String month, String year) throws MalformedURLException, IOException {
		
		String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"
				+ year + "/" + month + "/" + day + "/powergrabmap.geojson";

		HttpURLConnection conn = convertToConnection(mapString);
		connect(conn);
		String mapSource = readMap(conn);
		return new Map(FeatureCollection.fromJson(mapSource));
	}
	
	/*
	 * Converts the URL String to HttpURLConnection and opens the connection.
	 */
	private static HttpURLConnection convertToConnection(String mapString) throws MalformedURLException, IOException {
		URL mapURL = new URL(mapString);
		HttpURLConnection conn = (HttpURLConnection) mapURL.openConnection();
		return conn;
	}
	
	/*
	 * Connects to the URL.
	 */
	private static void connect(HttpURLConnection conn) throws IOException {
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();
	}
	
	/*
	 * Reads the map's features from the address.
	 */
	private static String readMap(HttpURLConnection conn) throws IOException {
		InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
		BufferedReader reader = new BufferedReader(isReader);
		StringBuffer sb = new StringBuffer();
		String str;
		while((str = reader.readLine())!= null){
			sb.append(str);
		}
		return sb.toString();
	}
	
	// testing
	public static void main(String[] args) throws MalformedURLException, IOException {
		Map map = MapBuilder.build("05", "05", "2019");
		map.addDronePath(new Position(55.944425, -3.188396), new Position(55.945, -3.187));
		map.addDronePath(new Position(55.945, -3.187), new Position(55.946233, -3.192473));
		map.addDronePath(new Position(55.946233, -3.192473), new Position(55.946233, -3.184319));
		map.addDronePath(new Position(55.946233, -3.184319), new Position(55.942617, -3.184319));
		map.addDronePath(new Position(55.942617, -3.184319), new Position(55.942617, -3.192473));
		map.addDronePath(new Position(55.942617, -3.192473), new Position(55.946233, -3.192473));
		map.createGeoJSONMap("05", "05", "2019", "stateless");
		
		MovementLog movementLog = new MovementLog();
		Move move1 = new Move(new Position(55.944425, -3.188396), Direction.SSE,
				new Position(55.944147836140246, -3.1882811949702905), 0.0f, 248.75f);
		movementLog.addMove(move1);
		movementLog.writeLog("05", "05", "2019", "stateless");
		
		ChargingStation cS = map.getChargingStations().get(0);
		System.out.println("cS coins: " + cS.getCoins());
		Drone d = new StatelessDrone(new Position(0,0), 0);
		System.out.println("d coins: " + d.getCoins());
//		d.charge(cS);
		System.out.println("cS coins: " + cS.getCoins());
		System.out.println("d coins: " + d.getCoins());
		System.out.println(map.getChargingStations().get(0).getCoins());
	}
	
}
