package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.List;

public class MovementLog {
	
	private List<Move> movementLog = new ArrayList<Move>();
	
	public MovementLog() {
		
	}
	
	public void addMove(Move move) {
		movementLog.add(move);
	}
	
	public void writeLog(String day, String month, String year, String droneVersion) {
		
	}
	
}
