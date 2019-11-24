package uk.ac.ed.inf.powergrab;

public class Position {
	private double latitude;
	private double longitude;
	
	static double r = 0.0003;
	static double w2 = r * Math.cos(Math.toRadians(67.5));
	static double w3 = r * Math.cos(Math.toRadians(45));
	static double w4 = r * Math.cos(Math.toRadians(22.5));
	static double h2 = w4;
	static double h3 = w3;
	static double h4 = w2;
	
	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public boolean equals(Position p) {
		return (this.latitude == p.getLatitude() && this.longitude == p.getLongitude());
	}
	
	public Position nextPosition(Direction direction) {
		
		switch(direction) {
		case N:
			return new Position(latitude + r, longitude);
		case NNE:
			return new Position(latitude + h2, longitude + w2);
		case NE:
			return new Position(latitude + h3, longitude + w3);
		case ENE:
			return new Position(latitude + h4, longitude + w4);
		case E:
			return new Position(latitude, longitude + r);
		case ESE:
			return new Position(latitude - h4, longitude + w4);
		case SE:
			return new Position(latitude - h3, longitude + w3);
		case SSE:
			return new Position(latitude - h2, longitude + w2);
		case S:
			return new Position(latitude - r, longitude);
		case SSW:
			return new Position(latitude - h2, longitude - w2);
		case SW:
			return new Position(latitude - h3, longitude - w3);
		case WSW:
			return new Position(latitude - h4, longitude - w4);
		case W:
			return new Position(latitude, longitude - r);
		case WNW:
			return new Position(latitude + h4, longitude - w4);
		case NW:
			return new Position(latitude + h3, longitude - w3);
		case NNW:
			return new Position(latitude + h2, longitude - w2);
		default: return null;
		}
		
	}
	
	public boolean inPlayArea() {
		return (latitude > 55.942617) && (latitude < 55.946233)
				&& (longitude > -3.192473) && (longitude < -3.184319);
	}
	
	public static double calculateDistance(Position p1, Position p2) {
		double latitudeDifference = p1.latitude - p2.latitude;
		double longitudeDifference = p1.longitude - p2.longitude;
		return Math.sqrt(latitudeDifference*latitudeDifference + longitudeDifference*longitudeDifference);
	}
	
	// for testing/debugging
	@Override
	public String toString() {
		return "Latitude: " + this.latitude + " Longitude: " + this.longitude;
	}
	
}
