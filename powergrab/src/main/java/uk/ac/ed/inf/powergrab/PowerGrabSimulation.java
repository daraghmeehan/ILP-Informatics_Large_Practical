package uk.ac.ed.inf.powergrab;

import java.util.List;

public interface PowerGrabSimulation extends PowerGrab {
	
	public double getResult();
	public List<Direction> getMoves();
	
}
