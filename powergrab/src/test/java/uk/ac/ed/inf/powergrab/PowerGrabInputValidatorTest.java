package uk.ac.ed.inf.powergrab;

import static org.junit.Assert.*;

import org.junit.Test;

public class PowerGrabInputValidatorTest {

	@Test
	public void testBadArgsLength() {
		assertFalse(InputValidator.isValid(new String[]{""}));
		assertFalse(InputValidator.isValid(new String[]{"one"}));
		assertFalse(InputValidator.isValid(new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight"}));
	}
	
	@Test
	public void testGoodArgsLength() {
		assertTrue(InputValidator.isValid(new String[]{"15", "09", "2019", "55.944425", "3.188396", "5678", "stateless"}));
	}
	
	@Test
	public void testBadDay() {
		assertFalse(InputValidator.isValid(new String[]{"35", "09", "2019", "55.944425", "3.188396", "5678", "stateless"}));
	}
	
	@Test
	public void testBadMonth() {
		assertFalse(InputValidator.isValid(new String[]{"15", "19", "2019", "55.944425", "3.188396", "5678", "stateless"}));
	}
	
	@Test
	public void testBadYear() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "00", "55.944425", "3.188396", "5678", "stateless"}));
	}
	
	@Test
	public void testBadLatitude() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", "a", "3.188396", "5678", "stateless"}));
	}
	
	@Test
	public void testBadLongitude() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", "55.944425", "b", "5678", "stateless"}));
	}
	
	@Test
	public void testBadSeed() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", "55.944425", "3.188396", "5678.5", "stateless"}));
	}
	
	@Test
	public void testBadDroneVersion() {
		assertFalse(InputValidator.isValid(new String[]{"15", "09", "2019", "55.944425", "3.188396", "5678", "state"}));
	}

}
