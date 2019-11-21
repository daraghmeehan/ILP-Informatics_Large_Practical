package uk.ac.ed.inf.powergrab;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Move {
	
	private Position positionBefore;
	Direction moveDirection;
	private Position positionAfter;
//	ChargingStation chargingStation;
	double coinsAfter;
	double powerAfter;
	
	public Move(Direction direction, ChargingStation chargingStation) {
		this.moveDirection = direction;
//		this.chargingStation = chargingStation;
	}
	
	public Move(Position positionBefore, Direction moveDirection, Position positionAfter, double coinsAfter, double powerAfter) {
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
		
		return String.format("%s,%s,%s,%s,%s,%s,%s", df.format(positionBefore.getLatitude()), df.format(positionBefore.getLongitude()),
				moveDirection.toString(), df.format(positionAfter.getLatitude()), df.format(positionAfter.getLongitude()),
				df.format(coinsAfter), df.format(powerAfter));
	}
	
}
