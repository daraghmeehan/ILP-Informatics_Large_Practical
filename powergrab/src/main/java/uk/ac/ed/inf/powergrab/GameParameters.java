package uk.ac.ed.inf.powergrab;

public class GameParameters {
	
	public final int maxMoves;
	public final double moveDistance, maxLatitude, minLatitude, maxLongitude, minLongitude, movePowerCost;
	
	public GameParameters(int maxMoves, double moveDistance, double maxLatitude, double minLatitude,
			double maxLongitude, double minLongitude, double movePowerCost) {
		this.maxMoves = maxMoves;
		this.moveDistance = moveDistance;
		this.maxLatitude = maxLatitude;
		this.minLatitude = minLatitude;
		this.maxLongitude = maxLongitude;
		this.minLongitude = minLongitude;
		this.movePowerCost = movePowerCost;
	}
	
	
	
}
