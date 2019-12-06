package uk.ac.ed.inf.powergrab;

/*
 * Represents a position in our game.
 * The constants defined specify the move distance and changes to latitude and longitude by different angled moves,
 * and the bounds of the game's map.
 */
public class Position {
	
	public final double latitude;
	public final double longitude;
	
	private static final double r = GameParameters.MOVE_DISTANCE;
	private static final double w2 = r * Math.cos(Math.toRadians(67.5));
	private static final double w3 = r * Math.cos(Math.toRadians(45));
	private static final double w4 = r * Math.cos(Math.toRadians(22.5));
	private static final double h2 = w4;
	private static final double h3 = w3;
	private static final double h4 = w2;
	
	private static final double minLatitude = GameParameters.MIN_LATITUDE;
	private static final double maxLatitude = GameParameters.MAX_LATITUDE;
	private static final double minLongitude = GameParameters.MIN_LONGITUDE;
	private static final double maxLongitude = GameParameters.MAX_LONGITUDE;
	
	public Position(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public boolean equals(Position p) {
		return (this.latitude == p.latitude && this.longitude == p.longitude);
	}
	
	/*
	 * Gives the resulting position after a move in a certain direction.
	 */
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
	
	/*
	 * Determines if the position is within the play bounds.
	 */
	public boolean inPlayArea() {
		return (latitude > minLatitude) && (latitude < maxLatitude)
				&& (longitude > minLongitude) && (longitude < maxLongitude);
	}
	
	/*
	 * Calculates the euclidean distance between two positions.
	 */
	public static double calculateDistance(Position p1, Position p2) {
		double latitudeDifference = p1.latitude - p2.latitude;
		double longitudeDifference = p1.longitude - p2.longitude;
		return Math.sqrt(latitudeDifference*latitudeDifference + longitudeDifference*longitudeDifference);
	}
	
	/*
	 * for testing/debugging
	 */
	@Override
	public String toString() {
		return "Latitude: " + latitude + " Longitude: " + longitude;
	}
	
}
