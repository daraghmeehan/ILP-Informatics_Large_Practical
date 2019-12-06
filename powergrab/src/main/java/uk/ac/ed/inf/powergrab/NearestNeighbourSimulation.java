package uk.ac.ed.inf.powergrab;

import java.util.List;
import java.util.ArrayList;

/*
 * A basic simulation that uses the nearest neighbour algorithm to decide its route order.
 */
public class NearestNeighbourSimulation extends PowerGrabSimulation {
	
	private int[] basicOrder;
	private int[] twoOptOptimisedOrder;
	private int[] threeOptOptimisedOrder;
	
	public NearestNeighbourSimulation(Position startingPosition, List<ChargingStation> testStations) {
		super(startingPosition, testStations);
	}
	
	/*
	 * Chooses the route order using the nearest neighbour algorithm.
	 */
	@Override
	public List<int[]> chooseStationOrders() {
		
		List<int[]> stationOrders = new ArrayList<int[]>(3);
		
		double[][] distanceMatrix = super.getDistanceMatrix();
		
		basicOrder = NearestNeighbourSimulation.calculateNearestNeighbourOrder(
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
		
		System.out.println("Estimated nearest neighbour distance before optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.basicOrder, super.getDistanceMatrix()));
		System.out.println("Estimated distance after 2-opt optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.twoOptOptimisedOrder, super.getDistanceMatrix()));
		System.out.println("Estimated distance after 3-opt optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.threeOptOptimisedOrder, super.getDistanceMatrix()));
	}
	
	/*
	 * Calculates the nearest neighbour route order.
	 */
	private static int[] calculateNearestNeighbourOrder(double[][] distanceMatrix) {
		
		int n = distanceMatrix[0].length - 1;
		int[] stationOrder = new int[n];
		boolean[] visitedStations = new boolean[n];
		
		int initialMinStation = 0;
		double initialMinDistance = distanceMatrix[0][0];
		
		for (int stationNumber = 0; stationNumber < n; stationNumber++) {
			double distance = distanceMatrix[0][stationNumber + 1];
			if (distance < initialMinDistance) {
				initialMinStation = stationNumber;
				initialMinDistance = distance;
			}
		}
		
		stationOrder[0] = initialMinStation;
		visitedStations[initialMinStation] = true;
		
		for (int stationOrderIndex = 0; stationOrderIndex < n - 1; stationOrderIndex++) {
			int currentStation = stationOrder[stationOrderIndex];
			
			int minStation = currentStation;
			double minDistance = distanceMatrix[currentStation + 1][currentStation + 1];
			
			for (int stationNumber = 0; stationNumber < n; stationNumber++) {
				if (visitedStations[stationNumber]) {
					continue;
				}
				double distance = distanceMatrix[currentStation + 1][stationNumber + 1];
				if (distance < minDistance) {
					minStation = stationNumber;
					minDistance = distance;
				}
			}
			
			stationOrder[stationOrderIndex + 1] = minStation;
			visitedStations[minStation] = true;
		}
		
		return stationOrder;
	}
	
}
