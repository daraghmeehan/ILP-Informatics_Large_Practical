package uk.ac.ed.inf.powergrab;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Move {
	
	private Position positionBefore;
	Direction moveDirection;
	private Position positionAfter;
//	ChargingStation chargingStation;
	float coinsAfter;
	float powerAfter;
	
	public Move(Direction direction, ChargingStation chargingStation) {
		this.moveDirection = direction;
//		this.chargingStation = chargingStation;
	}
	
	public Move(Position positionBefore, Direction moveDirection, Position positionAfter, float coinsAfter, float powerAfter) {
		this.positionBefore = positionBefore;
		this.moveDirection = moveDirection;
		this.positionAfter = positionAfter;
		this.coinsAfter = coinsAfter;
		this.powerAfter = powerAfter;
	}
	
	public Position getPositionBefore() {
		return this.positionBefore;
	}

	public Position getPositionAfter() {
		return this.positionAfter;
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
