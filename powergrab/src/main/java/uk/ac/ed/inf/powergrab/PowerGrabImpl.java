package uk.ac.ed.inf.powergrab;

public class PowerGrabImpl implements PowerGrab {
	
	GameParameters gameParams; //or just maxMoves?
	int movesMade = 0;
	Drone drone;
	PowerGrabMap map;
	
	public PowerGrabImpl(GameParameters gameParams) {
		this.gameParams = gameParams;
	}
	
	public void setup(String[] args) {
		// needs exception handling
		this.map = MapLoader.load(args[0], args[1], args[2]);
		this.drone = DroneLoader.load(args[3], args[4], args[5], args[6]);
	}
	
	public void play() {
		
	}
	
	public void report() {
		
	}
	
	public static void main(String[] args) {
		
	}
	
}
