package uk.ac.ed.inf.powergrab;

import static org.junit.Assert.*;

import org.junit.Test;

public class T_PowergrabInputValidator {

	@Test
	public void testBadArgsLength() {
		assertFalse(InputValidator.isValid(new String[]{""}));
		assertFalse(InputValidator.isValid(new String[]{"one"}));
		assertFalse(InputValidator.isValid(new String[]{"one", "two", "three", "four", "five", "six", "seven"}));
	}
	
	@Test
	public void testGoodArgsLength() {
		assertTrue(InputValidator.isValid(new String[]{"one", "two", "three", "four", "five", "six"}));
	}

}
