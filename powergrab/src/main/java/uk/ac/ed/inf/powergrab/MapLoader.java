package uk.ac.ed.inf.powergrab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mapbox.geojson.FeatureCollection;

public class MapLoader {
	
	public static PowerGrabMap load(String day, String month, String year) {
		
		String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/"
				+ year + "/" + month + "/" + day + "/powergrabmap.geojson";
		
		try {
			HttpURLConnection conn = convertToConnection(mapString);
			connect(conn);
			String mapSource = readMap(conn);
			return new PowerGrabMap(FeatureCollection.fromJson(mapSource));
//			System.out.println(mapSource.substring(0, 500));
//			for (com.mapbox.geojson.Feature f : fc.features()) {
//				System.out.println(f.getProperty("coins").getAsString());
//			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return null;
		}
//		catch (Exception e) {
//			System.out.println("hello");
//		}
	}

	private static HttpURLConnection convertToConnection(String mapString) {
		URL mapURL = new URL(mapString);
		HttpURLConnection conn = (HttpURLConnection) mapURL.openConnection();
		return conn;
	}
	
	private static void connect(HttpURLConnection conn) {
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();
	}
	
	private static String readMap(HttpURLConnection conn) {
		InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
		BufferedReader reader = new BufferedReader(isReader);
		StringBuffer sb = new StringBuffer();
		String str;
		while((str = reader.readLine())!= null){
			sb.append(str);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		MapLoader.load("05", "05", "2019");
	}
	
}
