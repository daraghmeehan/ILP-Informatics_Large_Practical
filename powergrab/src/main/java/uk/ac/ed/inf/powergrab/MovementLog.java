package uk.ac.ed.inf.powergrab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
 * Keeps a record of the drone's moves and writes the record to a .txt file.
 */
public class MovementLog {
	
	private List<Move> moves = new ArrayList<Move>();
	
	public MovementLog() {}
	
	public void addMove(Move move) {
		moves.add(move);
	}
	
	/*
	 * Writes the movement log to a .txt file.
	 */
	public void writeLog(String day, String month, String year, String droneVersion) {
		String fileName = droneVersion + "-" + day + "-" + month + "-" + year + ".txt";
		
		try (PrintWriter out = new PrintWriter(new FileOutputStream(new File(fileName), false))) {
		    for (Move move : moves) {
		    	out.println(move.toString());
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
