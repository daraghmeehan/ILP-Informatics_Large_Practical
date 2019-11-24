package uk.ac.ed.inf.powergrab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MovementLog {
	
	private List<Move> movementLog = new ArrayList<Move>();
	
	public MovementLog() {}
	
	public void addMove(Move move) {
		movementLog.add(move);
	}
	
	public void writeLog(String day, String month, String year, String droneVersion) {
		String fileName = droneVersion + "-" + day + "-" + month + "-" + year + ".txt";
		
		try (PrintWriter out = new PrintWriter(new FileOutputStream(new File(
				"src" + File.separator + "Output Files" + File.separator + fileName), false))) {
		    for (Move move : movementLog) {
		    	out.println(move.toString());
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
