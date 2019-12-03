package uk.ac.ed.inf.powergrab;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/*
 * Represents all the attributes of a drone's move.
 * Provides functionality to format the move to be printed in a log of the moves.
 */
public class Move {
	
	public final Position positionBefore;
	public final Direction moveDirection;
	public final Position positionAfter;
	public final float coinsAfter;
	public final float powerAfter;
	
	public Move(Position positionBefore, Direction moveDirection, Position positionAfter, float coinsAfter, float powerAfter) {
		this.positionBefore = positionBefore;
		this.moveDirection = moveDirection;
		this.positionAfter = positionAfter;
		this.coinsAfter = coinsAfter;
		this.powerAfter = powerAfter;
	}
	
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.0",  DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(340);
		
		return String.format("%s,%s,%s,%s,%s,%s,%s", df.format(positionBefore.latitude), df.format(positionBefore.longitude),
				moveDirection.toString(), df.format(positionAfter.latitude), df.format(positionAfter.longitude),
				df.format(coinsAfter), df.format(powerAfter));
	}
	
}
