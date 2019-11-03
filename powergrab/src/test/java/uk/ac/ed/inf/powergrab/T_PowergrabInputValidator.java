package uk.ac.ed.inf.powergrab;

import static org.junit.Assert.*;

import org.junit.Test;

public class T_PowergrabInputValidator {

	@Test
	public void testBadArgsLength() {
		assertFalse(PowerGrabInputValidator.isValid(new String[]{""}));
		assertFalse(PowerGrabInputValidator.isValid(new String[]{"one"}));
		assertFalse(PowerGrabInputValidator.isValid(new String[]{"one", "two", "three", "four", "five", "six", "seven"}));
	}
	
	@Test
	public void testGoodArgsLength() {
		assertTrue(PowerGrabInputValidator.isValid(new String[]{"one", "two", "three", "four", "five", "six"}));
	}

}
