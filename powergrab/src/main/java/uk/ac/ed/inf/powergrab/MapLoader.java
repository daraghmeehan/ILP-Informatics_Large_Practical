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
			URL mapURL = new URL(mapString);
			HttpURLConnection conn = (HttpURLConnection) mapURL.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();
			String str;
			while((str = reader.readLine())!= null){
				sb.append(str);
			}
			String mapSource = sb.toString();
			System.out.println(mapSource.substring(0, 500));
			FeatureCollection fc = FeatureCollection.fromJson(mapSource);
			for (com.mapbox.geojson.Feature f : fc.features()) {
				System.out.println(f.getProperty("coins").getAsString());
			}
			
			return new PowerGrabMap(fc);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
//		catch (Exception e) {
//			System.out.println("hello");
//		}
	}
	
	public static void main(String[] args) {
		MapLoader.load("05", "05", "2019");
	}
	
}
