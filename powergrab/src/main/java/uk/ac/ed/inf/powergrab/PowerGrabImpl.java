package uk.ac.ed.inf.powergrab;

import java.io.*;
import java.net.*;

public class PowerGrabImpl implements PowerGrab {
	
	public PowerGrabImpl(String[] args) {
		if (!PowerGrabInputValidator.isValid(args)) {
			
		}
	}
	
	String date;
	float initialLatitude, initialLongitude;
	int seed;
	int maxMoves = 250;
	int movesMade = 0;
	
	PowerGrabMap map = MapLoader.load(args)
	
	public static void main(String[] args) {
		String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/2019/01/01/powergrabmap.geojson";
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
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
//		catch (Exception e) {
//			System.out.println("hello");
//		}
		System.out.println(1+1);
	}
	
}
