package uk.ac.ed.inf.powergrab;

import static org.junit.Assert.*;

import org.junit.Test;

public class InputValidatorTest {
	
	String validLatitude = Double.toString((GameParameters.MAX_LATITUDE + GameParameters.MIN_LATITUDE) / 2);
	String validLongitude = Double.toString((GameParameters.MAX_LONGITUDE + GameParameters.MIN_LONGITUDE) / 2);

	@Test
	public void testBadArgsLength() {
		assertFalse(InputValidator.isValid(new String[]{""}));
		assertFalse(InputValidator.isValid(new String[]{"one"}));
		assertFalse(InputValidator.isValid(new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight"}));
	}
	
	@Test
	public void testGoodArgsLength() {
		assertTrue(InputValidator.isValid(new String[]{"15", "09", "2019", validLatitude, validLongitude, "5678", "stateless"}));
	}
	
	@Test
	public void testBadDay() {
		assertFalse(InputValidator.isValid(new String[]{"35", "09", "2019", validLatitude, validLongitude, "5678", "stateless"}));
	}
	
	@Test
	public void testBadMonth() {
		assertFalse(InputValidator.isValid(new String[]{"15", "19", "2019", validLatitude, validLongitude, "5678", "stateless"}));
	}
	
	@Test
	public void testBadYear() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "00", validLatitude, validLongitude, "5678", "stateless"}));
	}
	
	@Test
	public void testBadLatitude() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", "a", validLongitude, "5678", "stateless"}));
	}
	
	@Test
	public void testBadLongitude() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", validLatitude, "b", "5678", "stateless"}));
	}
	
	@Test
	public void testBadStartingPosition() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", "0", "0", "5678", "stateless"}));
	}
	
	@Test
	public void testBadSeed() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", validLatitude, validLongitude, "5678.5", "stateless"}));
	}
	
	@Test
	public void testBadDroneVersion() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", validLatitude, validLongitude, "5678", "state"}));
	}

}
