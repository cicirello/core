/*
 * Module org.cicirello.core
 * Copyright 2019-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * This file is part of module org.cicirello.core.
 *
 * Module org.cicirello.core is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 *
 * Module org.cicirello.core is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with module org.cicirello.core.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package org.cicirello.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the ArrayEnforcer class.
 */
public class ArrayEnforcerTests {
	
	@Test
	public void testEqualLengthInt() {
		int[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testEqualLengthLong() {
		long[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testEqualLengthShort() {
		short[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testEqualLengthByte() {
		byte[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testEqualLengthDouble() {
		double[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testEqualLengthFloat() {
		float[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testEqualLengthChar() {
		char[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testEqualLengthBoolean() {
		boolean[] observed = null;
		observed = ArrayLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayLengthEnforcer.enforce(observed, 5));
		observed = ArrayLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
		observed = ArrayLengthEnforcer.enforce(observed, 4);
		assertEquals(4, observed.length);
	}
	
	@Test
	public void testMinLengthInt() {
		int[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
	
	@Test
	public void testMinLengthLong() {
		long[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
	
	@Test
	public void testMinLengthShort() {
		short[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
	
	@Test
	public void testMinLengthByte() {
		byte[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
	
	@Test
	public void testMinLengthDouble() {
		double[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
	
	@Test
	public void testMinLengthFloat() {
		float[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
	
	@Test
	public void testMinLengthChar() {
		char[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
	
	@Test
	public void testMinLengthBoolean() {
		boolean[] observed = null;
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 5);
		assertEquals(5, observed.length);
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 5));
		assertSame(observed, ArrayMinimumLengthEnforcer.enforce(observed, 4));
		observed = ArrayMinimumLengthEnforcer.enforce(observed, 6);
		assertEquals(6, observed.length);
	}
}
