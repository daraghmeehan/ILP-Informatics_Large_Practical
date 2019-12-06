package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

public class FurthestInsertionSimulation  extends PowerGrabSimulation {
	
	private int[] basicOrder;
	private int[] twoOptOptimisedOrder;
	private int[] threeOptOptimisedOrder;
	
	public FurthestInsertionSimulation(Position startingPosition, List<ChargingStation> testStations) {
		super(startingPosition, testStations);
	}
	
	protected List<int[]> chooseStationOrders() {
		
		List<int[]> stationOrders = new ArrayList<int[]>(3);
		double[][] distanceMatrix = super.getDistanceMatrix();
		
		basicOrder = FurthestInsertionSimulation.calculateFurthestInsertionOrder(
				distanceMatrix);
		stationOrders.add(basicOrder);
		
		twoOptOptimisedOrder = StatefulDrone.twoOptOptimise(basicOrder, distanceMatrix);
		stationOrders.add(twoOptOptimisedOrder);
		
		threeOptOptimisedOrder = StatefulDrone.threeOptOptimise(basicOrder, distanceMatrix);
		stationOrders.add(threeOptOptimisedOrder);
		
		return stationOrders;
		
	}

	@Override
	public void report() {
		
		System.out.println("Estimated furthest insertion distance before optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.basicOrder, super.getDistanceMatrix()));
		System.out.println("Estimated distance after 2-opt optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.twoOptOptimisedOrder, super.getDistanceMatrix()));
		System.out.println("Estimated distance after 3-opt optimisation: "
				+ StatefulDrone.calculateTotalRouteDistance(this.threeOptOptimisedOrder, super.getDistanceMatrix()));
	}
	
	private static int[] calculateFurthestInsertionOrder(double[][] distanceMatrix) {
		
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
			
			int furthestStation = -1;//??
			double maxDistance = Double.NEGATIVE_INFINITY;
			
			for (int stationNumber = 0; stationNumber < n; stationNumber++) {
				if (visitedStations[stationNumber]) {
					continue;
				}
				double initDistance = distanceMatrix[0][stationNumber + 1];
				if (initDistance > maxDistance) {
					furthestStation = stationNumber;
					maxDistance = initDistance;
				}
				for (int indexToCheck = 0; indexToCheck < numberOfStationsVisited; indexToCheck++) {
					int stationToCheck = stationOrder[indexToCheck];
					double distance = distanceMatrix[stationToCheck + 1][stationNumber + 1];
					if (distance > maxDistance) {
						furthestStation = stationNumber;
						maxDistance = distance;
					}
				}
			}
			
			// found furthestStation
			// inserting closestStation into stationOrder?
			int bestInsertionPosition = 0;
			double smallestDistanceIncrease = distanceMatrix[0][furthestStation + 1]
					+ distanceMatrix[furthestStation + 1][stationOrder[0] + 1] - distanceMatrix[0][stationOrder[0] + 1];
			
			for (int positionToCheck = 1; positionToCheck <= numberOfStationsVisited; positionToCheck++) {
				if (positionToCheck != numberOfStationsVisited) {
					double distance = distanceMatrix[stationOrder[positionToCheck - 1] + 1][furthestStation + 1]
							+ distanceMatrix[furthestStation + 1][stationOrder[positionToCheck] + 1]
									- distanceMatrix[stationOrder[positionToCheck - 1] + 1][stationOrder[positionToCheck] + 1];
					if (distance < smallestDistanceIncrease) {
						bestInsertionPosition = positionToCheck;
						smallestDistanceIncrease = distance;
					}
				} else {
					double distance = distanceMatrix[stationOrder[positionToCheck - 1] + 1][furthestStation + 1];
					if (distance < smallestDistanceIncrease) {
						bestInsertionPosition = positionToCheck;
						smallestDistanceIncrease = distance;
					}
				}
			}
			
			stationOrder = FurthestInsertionSimulation.insertFurthestStation(furthestStation, bestInsertionPosition, stationOrder);
			visitedStations[furthestStation] = true;
			numberOfStationsVisited++;
		}
		
		return stationOrder;
	}

	private static int[] insertFurthestStation(int closestStation, int bestInsertionPosition, int[] stationOrder) {
		
		int n = stationOrder.length;
		
		for (int i = n - 1; i > bestInsertionPosition; i--) {
			stationOrder[i] = stationOrder[i - 1];
		}
		
		stationOrder[bestInsertionPosition] = closestStation;
		
		return stationOrder;
	}
	
//	private static int closestStationToCycle()

}
