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
 * JUnit tests for the IntegerList class.
 */
public class IntegerListTests {
	
	@Test
	public void testEmptyList() {
		IntegerList list = new IntegerList();
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
		
		IntegerList list2 = new IntegerList(1);
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
		
		IntegerList list3 = list.copy();
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
			() -> new IntegerList(0)
		);
	}
	
	@Test
	public void testFromArray() {
		int[][] cases = {
			{ 100 },
			{ 100, 101},
			{ 100, 101, 102}
		};
		String[] s = { "[100]", "[100, 101]", "[100, 101, 102]" };
		int sIndex = 0;
		for (int[] array : cases) {
			IntegerList list = new IntegerList(array.clone());
			assertFalse(list.isEmpty());
			assertEquals(array.length, list.size());
			for (int e : array) {
				assertTrue(list.contains(e));
			}
			assertFalse(list.contains(99));
			for (int i = 0; i < array.length; i++) {
				assertEquals(array[i], list.get(i));
			}
			int[] fromList = list.toArray();
			assertEquals(array.length, fromList.length);
			assertArrayEquals(array, fromList);
			assertEquals(s[sIndex], list.toString());
			assertTrue(s.hashCode() != 0);
			
			IntegerList list2 = list.copy();
			assertTrue(list != list2);
			assertEquals(list, list2);
			assertEquals(list.hashCode(), list2.hashCode());
			assertFalse(list2.isEmpty());
			assertEquals(array.length, list2.size());
			for (int e : array) {
				assertTrue(list2.contains(e));
			}
			assertFalse(list2.contains(99));
			for (int i = 0; i < array.length; i++) {
				assertEquals(array[i], list2.get(i));
			}
			fromList = list2.toArray();
			assertEquals(array.length, fromList.length);
			assertArrayEquals(array, fromList);
			assertEquals(s[sIndex], list.toString());
			
			sIndex++;
		}
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> new IntegerList(new int[0])
		);
	}
	
	@Test
	public void testAddToEnd() {
		IntegerList list = new IntegerList(1);
		final int START = 100;
		assertEquals(0, list.size());
		for (int i = 1; i <= 32; i++) {
			list.add(START + i);
			assertEquals(i, list.size());
			for (int j = 0; j < i; j++) {
				assertEquals(START + j + 1, list.get(j));
			}
		}
		list.clear();
		assertEquals(0, list.size());
		assertEquals(new IntegerList(), list);
		assertEquals(0, list.hashCode());
	}
	
	@Test
	public void testAddToIndexSize() {
		final IntegerList list = new IntegerList(1);
		final int START = 100;
		assertEquals(0, list.size());
		for (int i = 1; i <= 32; i++) {
			list.add(i-1, START + i);
			assertEquals(i, list.size());
			for (int j = 0; j < i; j++) {
				assertEquals(START + j + 1, list.get(j));
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
		assertEquals(new IntegerList(), list);
		assertEquals(0, list.hashCode());
	}
	
	@Test
	public void testAddToFront() {
		IntegerList list = new IntegerList(1);
		final int START = 100;
		assertEquals(0, list.size());
		for (int i = 1; i <= 32; i++) {
			list.add(0, START + i);
			assertEquals(i, list.size());
			for (int j = 0; j < i; j++) {
				assertEquals(START + i - j, list.get(j));
			}
		}
		list.clear();
		assertEquals(0, list.size());
		assertEquals(new IntegerList(), list);
		assertEquals(0, list.hashCode());
	}
	
	@Test
	public void testAddToMiddle() {
		IntegerList list = new IntegerList(new int[] {100, 102});
		list.add(1, 101);
		assertEquals(3, list.size());
		assertEquals(100, list.get(0));
		assertEquals(101, list.get(1));
		assertEquals(102, list.get(2));
	}
	
	@Test
	public void testIndexOf() {
		int[][] testCases = {
			{ 100 },
			{ 100, 101, 100 },
			{ 100, 101, 102, 101, 100 }
		};
		for (int[] array : testCases) {
			IntegerList list = new IntegerList(array.clone());
			for (int i = 0; i <= array.length / 2; i++) {
				assertEquals(i, list.indexOf(array[i]));
			}
		}
	}
	
	@Test
	public void testLastIndexOf() {
		int[][] testCases = {
			{ 100 },
			{ 100, 101, 100 },
			{ 100, 101, 102, 101, 100 }
		};
		for (int[] array : testCases) {
			IntegerList list = new IntegerList(array.clone());
			for (int i = 0; i <= array.length / 2; i++) {
				assertEquals(array.length - 1 - i, list.lastIndexOf(array[i]));
			}
		}
	}
	
	@Test
	public void testRemoveFromEnd() {
		int[] array = {100, 101, 102, 103, 104, 105, 106, 107};
		final IntegerList list = new IntegerList(array.clone());
		for (int i = array.length - 1; i >= 0; i--) {
			int value = list.remove(i);
			assertEquals(array[i], value);
			assertEquals(i, list.size());
			if (i > 0) assertFalse(list.isEmpty());
			for (int j = 0; j < i; j++) {
				assertEquals(array[j], list.get(j));
			}
			IndexOutOfBoundsException thrown = assertThrows( 
				IndexOutOfBoundsException.class,
				() -> list.remove(list.size())
			);
		}
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testRemoveFromFront() {
		int[] array = {100, 101, 102, 103, 104, 105, 106, 107};
		IntegerList list = new IntegerList(array.clone());
		for (int i = 0; i < array.length; i++) {
			int value = list.remove(0);
			assertEquals(array[i], value);
			assertEquals(array.length - i - 1, list.size());
			if (list.size() > 0) assertFalse(list.isEmpty());
			for (int j = i + 1; j < array.length; j++) {
				assertEquals(array[j], list.get(j - i - 1));
			}
		}
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testRemoveFromMiddle() {
		int[] array = {100, 101, 102};
		IntegerList list = new IntegerList(array.clone());
		int value = list.remove(1);
		assertEquals(101, value);
		assertEquals(2, list.size());
		assertEquals(100, list.get(0));
		assertEquals(102, list.get(1));
		assertFalse(list.isEmpty());
	}
	
	@Test
	public void testSet() {
		int[][] testCases = {
			{ 100 },
			{ 100, 101, 100 },
			{ 100, 101, 102, 101, 100 }
		};
		final int START = 200;
		for (int[] array : testCases) {
			final IntegerList list = new IntegerList(array.clone());
			for (int i = 0; i < array.length; i++) {
				list.set(i, START + 1 + i);
				for (int j = 0; j <= i; j++) {
					assertEquals(START + 1 + j, list.get(j));
				}
				for (int j = i+1; j < array.length; j++) {
					assertEquals(array[j], list.get(j));
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
		int[][] testCases = {
			{100},
			{100, 101},
			{100, 101, 102}
		};
		for (int[] array : testCases) {
			IntegerList list1 = new IntegerList(array.clone());
			IntegerList list2 = new IntegerList(array.clone());
			assertEquals(list1, list2);
			assertEquals(list1.hashCode(), list2.hashCode());
		}
		for (int i = 0; i < testCases.length; i++) {
			IntegerList list1 = new IntegerList(testCases[i].clone());
			for (int j = i+1; j < testCases.length; j++) {
				IntegerList list2 = new IntegerList(testCases[j].clone());
				assertNotEquals(list1, list2);
				assertNotEquals(list1.hashCode(), list2.hashCode());
			}
		}
		IntegerList list1 = new IntegerList(testCases[0].clone());
		IntegerList list2 = new IntegerList(new int[] {101});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list1 = new IntegerList(testCases[1].clone());
		list2 = new IntegerList(new int[] {100, 100});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list2 = new IntegerList(new int[] {101, 101});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list1 = new IntegerList(testCases[2].clone());
		list2 = new IntegerList(new int[] {103, 101, 102});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list2 = new IntegerList(new int[] {100, 103, 102});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		list2 = new IntegerList(new int[] {100, 101, 103});
		assertNotEquals(list1, list2);
		assertNotEquals(list1.hashCode(), list2.hashCode());
		
		assertNotEquals(list1, "hello");
		assertNotEquals(list1, null);
	}
	
	@Test
	public void testEnsureCapacity() {
		int[] testCase = new int[18];
		for (int i = 0; i < testCase.length; i++) {
			testCase[i] = 100 + i;
		}
		IntegerList list = new IntegerList(testCase.clone());
		list.ensureCapacity(17);
		assertEquals(testCase.length, list.size());
		for (int i = 0; i < testCase.length; i++) {
			assertEquals(testCase[i], list.get(i));
		}
		list.ensureCapacity(18);
		assertEquals(testCase.length, list.size());
		for (int i = 0; i < testCase.length; i++) {
			assertEquals(testCase[i], list.get(i));
		}
		list.ensureCapacity(19);
		assertEquals(testCase.length, list.size());
		for (int i = 0; i < testCase.length; i++) {
			assertEquals(testCase[i], list.get(i));
		}
		list.ensureCapacity(32);
		assertEquals(testCase.length, list.size());
		for (int i = 0; i < testCase.length; i++) {
			assertEquals(testCase[i], list.get(i));
		}
	}
	
	@Test
	public void testTrimToSize() {
		int[][] testCases = {
			{},
			{100},
			{100, 101},
			{100, 101, 102}
		};
		for (int[] array : testCases) {
			IntegerList list = new IntegerList();
			for (int e : array) {
				list.add(e);
			}
			assertEquals(array.length, list.size());
			list.trimToSize();
			assertEquals(array.length, list.size());
			for (int i = 0; i < array.length; i++) {
				assertEquals(array[i], list.get(i));
			}
			list.add(500);
			assertEquals(1+array.length, list.size());
			for (int i = 0; i < array.length; i++) {
				assertEquals(array[i], list.get(i));
			}
			assertEquals(500, list.get(array.length));
		}
	}
}
