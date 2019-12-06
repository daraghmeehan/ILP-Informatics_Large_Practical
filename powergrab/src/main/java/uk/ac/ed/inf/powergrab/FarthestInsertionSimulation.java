package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

/*
 * A basic simulation that uses the farthest insertion algorithm to decide its route order.
 */
public class FarthestInsertionSimulation extends PowerGrabSimulation {
	
	private int[] basicOrder;
	private int[] twoOptOptimisedOrder;
	private int[] threeOptOptimisedOrder;
	
	public FarthestInsertionSimulation(Position startingPosition, List<ChargingStation> testStations) {
		super(startingPosition, testStations);
	}
	
	/*
	 * Chooses the route order using the farthest insertion algorithm.
	 */
	@Override
	public List<int[]> chooseStationOrders() {
		
		List<int[]> stationOrders = new ArrayList<int[]>(3);
		double[][] distanceMatrix = super.getDistanceMatrix();
		
		basicOrder = FarthestInsertionSimulation.calculateFarthestInsertionOrder(
				distanceMatrix);
		stationOrders.add(basicOrder);
		
		twoOptOptimisedOrder = StatefulDrone.twoOptOptimise(basicOrder, distanceMatrix);
		stationOrders.add(twoOptOptimisedOrder);
		
		threeOptOptimisedOrder = StatefulDrone.threeOptOptimise(basicOrder, distanceMatrix);
		stationOrders.add(threeOptOptimisedOrder);
		
		return stationOrders;
		
	}
	
	/*
	 * The report phase of the basic simulation.
	 */
	@Override
	public void report() {
		
		System.out.println("Estimated farthest insertion distance before optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.basicOrder, super.getDistanceMatrix()));
		System.out.println("Estimated distance after 2-opt optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.twoOptOptimisedOrder, super.getDistanceMatrix()));
		System.out.println("Estimated distance after 3-opt optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.threeOptOptimisedOrder, super.getDistanceMatrix()));
	}
	
	/*
	 * Calculates the farthest insertion route order.
	 */
	private static int[] calculateFarthestInsertionOrder(double[][] distanceMatrix) {
		
		int n = distanceMatrix[0].length - 1;
		int[] stationOrder = new int[n];
		boolean[] visitedStations = new boolean[n];
		int numberOfStationsVisited = 0;
		
		int initialMaxStation = 0;
		double initialMaxDistance = Double.NEGATIVE_INFINITY;
		
		for (int stationNumber = 0; stationNumber < n; stationNumber++) {
			double distance = distanceMatrix[0][stationNumber + 1];
			if (distance > initialMaxDistance) {
				initialMaxStation = stationNumber;
				initialMaxDistance = distance;
			}
		}
		
		stationOrder[0] = initialMaxStation;
		visitedStations[initialMaxStation] = true;
		numberOfStationsVisited++;
		
		for (int i = 1; i < n; i++) {
			
			int farthestStation = -1;//??
			double maxDistance = Double.NEGATIVE_INFINITY;
			
			for (int stationNumber = 0; stationNumber < n; stationNumber++) {
				if (visitedStations[stationNumber]) {
					continue;
				}
				double initDistance = distanceMatrix[0][stationNumber + 1];
				if (initDistance > maxDistance) {
					farthestStation = stationNumber;
					maxDistance = initDistance;
				}
				for (int indexToCheck = 0; indexToCheck < numberOfStationsVisited; indexToCheck++) {
					int stationToCheck = stationOrder[indexToCheck];
					double distance = distanceMatrix[stationToCheck + 1][stationNumber + 1];
					if (distance > maxDistance) {
						farthestStation = stationNumber;
						maxDistance = distance;
					}
				}
			}
			
			int bestInsertionPosition = PowerGrabSimulation.findBestInsertionPosition(stationOrder, distanceMatrix,
					farthestStation, numberOfStationsVisited);
			
			stationOrder = PowerGrabSimulation.insertStationIntoRouteOrder(farthestStation, bestInsertionPosition, stationOrder);
			visitedStations[farthestStation] = true;
			numberOfStationsVisited++;
		}
		
		return stationOrder;
	}

}
