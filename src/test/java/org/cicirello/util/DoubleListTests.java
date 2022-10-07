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
 * JUnit tests for the DoubleList class.
 */
public class DoubleListTests {
	
	@Test
	public void testSort() {
		DoubleList list = new DoubleList(new double[] {8, 2, 5, 1, 6, 3, 7, 9, 0, 4});
		list.sort();
		assertArrayEquals(new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, list.toArray());
	}
	
	@Test
	public void testEmptyList() {
		DoubleList list = new DoubleList();
		assertTrue(list.isEmpty());
		assertEquals(0, list.size());
		assertEquals(0, list.toArray().length);
		assertEquals("[]", list.toString());
		assertFalse(list.contains(0));
		assertFalse(list.contains(1));
		assertEquals(-1, list.indexOf(0));
		assertEquals(-1, list.indexOf(1));
		assertEquals(-1, list.lastIndexOf(0));
		assertEquals(-1, list.lastIndexOf(1));
		
		DoubleList list2 = new DoubleList(1);
		assertTrue(list2.isEmpty());
		assertEquals(0, list2.size());
		assertEquals(0, list2.toArray().length);
		assertEquals("[]", list2.toString());
		assertFalse(list2.contains(0));
		assertFalse(list2.contains(1));
		assertEquals(-1, list2.indexOf(0));
		assertEquals(-1, list2.indexOf(1));
		assertEquals(-1, list2.lastIndexOf(0));
		assertEquals(-1, list2.lastIndexOf(1));
		
		assertEquals(list, list2);
		assertEquals(list.hashCode(), list2.hashCode());
		assertEquals(0, list.hashCode());
		
		DoubleList list3 = list.copy();
		assertTrue(list != list3);
		assertEquals(list, list3);
		assertEquals(0, list3.hashCode());
		assertTrue(list3.isEmpty());
		assertEquals(0, list3.size());
		assertEquals(0, list3.toArray().length);
		assertEquals("[]", list3.toString());
		assertFalse(list3.contains(0));
		assertFalse(list3.contains(1));
		assertEquals(-1, list3.indexOf(0));
		assertEquals(-1, list3.indexOf(1));
		assertEquals(-1, list3.lastIndexOf(0));
		assertEquals(-1, list3.lastIndexOf(1));
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new DoubleList(0)
		);
	}
	
	@Test
	public void testFromArray() {
		double[][] cases = {
			{ 100 },
			{ 100, 101},
			{ 100, 101, 102}
		};
		String[] s = { "[100.0]", "[100.0, 101.0]", "[100.0, 101.0, 102.0]" };
		int sIndex = 0;
		for (double[] array : cases) {
			DoubleList list = new DoubleList(array.clone());
			fromArrayTestHelper(list, array);
			assertEquals(s[sIndex], list.toString());
			assertTrue(s.hashCode() != 0);
			
			DoubleList list2 = list.copy();
			assertTrue(list != list2);
			assertEquals(list, list2);
			assertEquals(list.hashCode(), list2.hashCode());
			assertFalse(list2.isEmpty());
			fromArrayTestHelper(list2, array);
			assertEquals(s[sIndex], list.toString());
			
			sIndex++;
		}
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new DoubleList(new double[0])
		);
	}
	
	@Test
	public void testAddToEnd() {
		DoubleList list = new DoubleList(1);
		final double START = 100;
		assertEquals(0, list.size());
		for (int i = 1; i <= 32; i++) {
			list.add(START + i);
			assertEquals(i, list.size());
			for (int j = 0; j < i; j++) {
				assertEquals(START + j + 1, list.get(j), 0.0);
			}
		}
		list.clear();
		assertEquals(0, list.size());
		assertEquals(new DoubleList(), list);
		assertEquals(0, list.hashCode());
	}
	
	@Test
	public void testAddToIndexSize() {
		final DoubleList list = new DoubleList(1);
		final double START = 100;
		assertEquals(0, list.size());
		for (int i = 1; i <= 32; i++) {
			list.add(i-1, START + i);
			assertEquals(i, list.size());
			for (int j = 0; j < i; j++) {
				assertEquals(START + j + 1, list.get(j), 0.0);
			}
			IndexOutOfBoundsException thrown = assertThrows( 
				IndexOutOfBoundsException.class,
				() -> list.add(list.size()+1, 1000)
			);
			thrown = assertThrows( 
				IndexOutOfBoundsException.class,
				() -> list.get(list.size())
			);
		}
		list.clear();
		assertEquals(0, list.size());
		assertEquals(new DoubleList(), list);
		assertEquals(0, list.hashCode());
	}
	
	@Test
	public void testAddToFront() {
		DoubleList list = new DoubleList(1);
		final double START = 100;
		assertEquals(0, list.size());
		for (int i = 1; i <= 32; i++) {
			list.add(0, START + i);
			assertEquals(i, list.size());
			for (int j = 0; j < i; j++) {
				assertEquals(START + i - j, list.get(j), 0.0);
			}
		}
		list.clear();
		assertEquals(0, list.size());
		assertEquals(new DoubleList(), list);
		assertEquals(0, list.hashCode());
	}
	
	@Test
	public void testAddToMiddle() {
		DoubleList list = new DoubleList(new double[] {100, 102});
		list.add(1, 101);
		assertEquals(3, list.size());
		assertEquals(100, list.get(0), 0.0);
		assertEquals(101, list.get(1), 0.0);
		assertEquals(102, list.get(2), 0.0);
	}
	
	@Test
	public void testIndexOf() {
		double[][] testCases = {
			{ 100 },
			{ 100, 101, 100 },
			{ 100, 101, 102, 101, 100 }
		};
		for (double[] array : testCases) {
			DoubleList list = new DoubleList(array.clone());
			for (int i = 0; i <= array.length / 2; i++) {
				assertEquals(i, list.indexOf(array[i]));
			}
		}
	}
	
	@Test
	public void testLastIndexOf() {
		double[][] testCases = {
			{ 100 },
			{ 100, 101, 100 },
			{ 100, 101, 102, 101, 100 }
		};
		for (double[] array : testCases) {
			DoubleList list = new DoubleList(array.clone());
			for (int i = 0; i <= array.length / 2; i++) {
				assertEquals(array.length - 1 - i, list.lastIndexOf(array[i]));
			}
		}
	}
	
	@Test
	public void testRemoveFromEnd() {
		double[] array = {100, 101, 102, 103, 104, 105, 106, 107};
		final DoubleList list = new DoubleList(array.clone());
		for (int i = array.length - 1; i > 0; i--) {
			double value = list.remove(i);
			assertEquals(array[i], value, 0.0);
			assertEquals(i, list.size());
			assertFalse(list.isEmpty());
			for (int j = 0; j < i; j++) {
				assertEquals(array[j], list.get(j), 0.0);
			}
			IndexOutOfBoundsException thrown = assertThrows( 
				IndexOutOfBoundsException.class,
				() -> list.remove(list.size())
			);
		}
		double value = list.remove(0);
		assertEquals(array[0], value);
		assertEquals(0, list.size());
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testRemoveFromFront() {
		double[] array = {100, 101, 102, 103, 104, 105, 106, 107};
		DoubleList list = new DoubleList(array.clone());
		for (int i = 0; i < array.length - 1; i++) {
			double value = list.remove(0);
			assertEquals(array[i], value, 0.0);
			assertEquals(array.length - i - 1, list.size());
			assertFalse(list.isEmpty());
			for (int j = i + 1; j < array.length; j++) {
				assertEquals(array[j], list.get(j - i - 1), 0.0);
			}
		}
		double value = list.remove(0);
		assertEquals(array[array.length-1], value);
		assertEquals(0, list.size());
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testRemoveFromMiddle() {
		double[] array = {100, 101, 102};
		DoubleList list = new DoubleList(array.clone());
		double value = list.remove(1);
		assertEquals(101, value, 0.0);
		assertEquals(2, list.size());
		assertEquals(100, list.get(0), 0.0);
		assertEquals(102, list.get(1), 0.0);
		assertFalse(list.isEmpty());
	}
	
	@Test
	public void testSet() {
		double[][] testCases = {
			{ 100 },
			{ 100, 101, 100 },
			{ 100, 101, 102, 101, 100 }
		};
		final double START = 200;
		for (double[] array : testCases) {
			final DoubleList list = new DoubleList(array.clone());
			for (int i = 0; i < array.length; i++) {
				list.set(i, START + 1 + i);
				for (int j = 0; j <= i; j++) {
					assertEquals(START + 1 + j, list.get(j), 0.0);
				}
				for (int j = i+1; j < array.length; j++) {
					assertEquals(array[j], list.get(j), 0.0);
				}
			}
			IndexOutOfBoundsException thrown = assertThrows( 
				IndexOutOfBoundsException.class,
				() -> list.set(list.size(), 1000)
			);
		}
	}
	
	@Test
	public void testEqualsAndHashCode() {
		double[][] testCases = {
			{100},
			{100, 101},
			{100, 101, 102}
		};
		for (int i = 0; i < testCases.length; i++) {
			DoubleList list1 = new DoubleList(testCases[i].clone());
			DoubleList list3 = new DoubleList(testCases[i].clone());
			assertEquals(list1, list3);
			assertEquals(list1.hashCode(), list3.hashCode());
			for (int j = i+1; j < testCases.length; j++) {
				DoubleList list2 = new DoubleList(testCases[j].clone());
				assertNotEquals(list1, list2);
				assertNotEquals(list1.hashCode(), list2.hashCode());
			}
		}
		DoubleList list1 = new DoubleList(testCases[0].clone());
		DoubleList list2 = new DoubleList(new double[] {101});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list1 = new DoubleList(testCases[1].clone());
		list2 = new DoubleList(new double[] {100, 100});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list2 = new DoubleList(new double[] {101, 101});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list1 = new DoubleList(testCases[2].clone());
		list2 = new DoubleList(new double[] {103, 101, 102});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list2 = new DoubleList(new double[] {100, 103, 102});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list2 = new DoubleList(new double[] {100, 101, 103});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		
		assertNotEquals(list1, "hello");
		assertNotEquals(list1, null);
	}
	
	@Test
	public void testEnsureCapacity() {
		double[] testCase = new double[18];
		for (int i = 0; i < testCase.length; i++) {
			testCase[i] = 100 + i;
		}
		DoubleList list = new DoubleList(testCase.clone());
		list.ensureCapacity(17);
		ensureCapacityTestHelper(testCase, list);
		list.ensureCapacity(18);
		ensureCapacityTestHelper(testCase, list);
		list.ensureCapacity(19);
		ensureCapacityTestHelper(testCase, list);
		list.ensureCapacity(32);
		assertEquals(testCase.length, list.size());
		ensureCapacityTestHelper(testCase, list);
	}
	
	@Test
	public void testTrimToSize() {
		double[][] testCases = {
			{},
			{100},
			{100, 101},
			{100, 101, 102}
		};
		for (double[] array : testCases) {
			DoubleList list = new DoubleList();
			for (double e : array) {
				list.add(e);
			}
			assertEquals(array.length, list.size());
			list.trimToSize();
			assertEquals(array.length, list.size());
			assertArrayEquals(array, list.toArray());
			list.add(500);
			assertEquals(1+array.length, list.size());
			for (int i = 0; i < array.length; i++) {
				assertEquals(array[i], list.get(i), 0.0);
			}
			assertEquals(500, list.get(array.length), 0.0);
		}
	}
	
	private void ensureCapacityTestHelper(double[] testCase, DoubleList list) {
		assertEquals(testCase.length, list.size());
		for (int i = 0; i < testCase.length; i++) {
			assertEquals(testCase[i], list.get(i));
		}
	}
	
	private void fromArrayTestHelper(DoubleList list, double[] array) {
		assertFalse(list.isEmpty());
		assertEquals(array.length, list.size());
		for (double e : array) {
			assertTrue(list.contains(e));
		}
		assertFalse(list.contains(99));
		for (int i = 0; i < array.length; i++) {
			assertEquals(array[i], list.get(i));
		}
		double[] fromList = list.toArray();
		assertEquals(array.length, fromList.length);
		assertArrayEquals(array, fromList);
	}
}
