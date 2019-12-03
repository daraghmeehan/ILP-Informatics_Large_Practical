package uk.ac.ed.inf.powergrab;

/*
 * This is launcher for the simulation of PowerGrab.
 */
public class App {
	
	/*
	 * This is the entry point for our program.
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		PowerGrabController.playPowerGrab(args);
		long endTime = System.nanoTime();
		double totalTime = ((double) endTime - startTime)/1000000000.0;
		System.out.println(totalTime);
	}

}
