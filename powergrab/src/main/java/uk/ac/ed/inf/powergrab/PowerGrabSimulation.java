package uk.ac.ed.inf.powergrab;

import java.util.List;

public interface PowerGrabSimulation extends PowerGrab {
	
	public float getResult();
	public List<Direction> getMoves();
	
}
