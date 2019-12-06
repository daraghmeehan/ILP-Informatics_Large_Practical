package uk.ac.ed.inf.powergrab;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
	
	/*
	 * Nodes are ordered based on their f-score.
	 */
	@Override
	public int compare(Node n1, Node n2) {
		// need to check for null?
		if (n1.getFScore() < n2.getFScore()) {
			return -1;
		} else if (n1.getFScore() > n2.getFScore()) {
			return 1;
		} else {
			return 0;
		}
	}

}
