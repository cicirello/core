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
 
package org.cicirello.ds;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SplittableRandom;

/**
 * JUnit tests for the FibonacciHeapDouble class.
 */
public class FibonacciHeapDoubleTests {
	
	// TESTS THAT ARE NEITHER STRICTLY MIN HEAP TESTS NOW MAX HEAP TESTS
	
	@Test
	public void testRetainAll() {
		String[] elements = {"A", "B", "C", "D"};
		double[] priorities = { 8, 6, 4, 2 };
		final FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
		}
		assertEquals(elements.length, pq.size());
		String[] retain = {"E", "A", "F", "C"};
		ArrayList<Object> keepThese = new ArrayList<Object>();
		keepThese.add(elements[0]);
		keepThese.add(elements[1]);
		keepThese.add(elements[2]);
		keepThese.add(elements[3]);
		assertFalse(pq.retainAll(keepThese));
		assertEquals(elements.length, pq.size());
		
		keepThese.clear();
		keepThese.add(retain[0]);
		keepThese.add(retain[1]);
		keepThese.add(new PriorityQueueNode.Double<String>(retain[2], 5));
		keepThese.add(new PriorityQueueNode.Double<String>(retain[3], 15));
		assertTrue(pq.retainAll(keepThese));
		assertEquals(elements.length-2, pq.size());
		assertTrue(pq.contains(elements[0]));
		assertTrue(pq.contains(elements[2]));
		assertFalse(pq.contains(elements[1]));
		assertFalse(pq.contains(elements[3]));
		assertFalse(pq.retainAll(keepThese));
		
		keepThese.clear();
		keepThese.add(retain[1]);
		keepThese.add(retain[3]);
		assertFalse(pq.retainAll(keepThese));
		
		keepThese.clear();
		keepThese.add(retain[0]);
		keepThese.add(retain[2]);
		assertTrue(pq.retainAll(keepThese));
		assertEquals(0, pq.size());
	}
	
	@Test
	public void testRemoveAll() {
		String[] elements = {"A", "B", "C", "D"};
		double[] priorities = { 8, 6, 4, 2 };
		final FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (int i = 0; i < elements.length; i++) {
			list.add(new PriorityQueueNode.Double<String>(elements[i], priorities[i]));
		}
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		assertTrue(pq.removeAll(list));
		assertEquals(0, pq.size());
		for (String e : elements) {
			assertFalse(pq.contains(e));
		}
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		list.remove(list.size()-1);
		assertTrue(pq.removeAll(list));
		assertEquals(1, pq.size());
		for (int i = 0; i < elements.length-1; i++) {
			String e = elements[i];
			assertFalse(pq.contains(e));
		}
		assertTrue(pq.contains(elements[elements.length-1]));
		
		list.clear();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> list3 = new ArrayList<String>();
		for (int i = 0; i < elements.length; i++) {
			list2.add(elements[i]);
			list3.add(elements[i]);
			list.add(new PriorityQueueNode.Double<String>(elements[i], priorities[i]));
		}
		pq.clear();
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		assertTrue(pq.removeAll(list2));
		assertEquals(0, pq.size());
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		list2.remove(list.size()-1);
		assertTrue(pq.removeAll(list2));
		assertEquals(1, pq.size());
		assertFalse(pq.removeAll(list2));
		assertEquals(1, pq.size());
		assertTrue(pq.contains(elements[elements.length-1]));
		for (int i = 0; i < elements.length-1; i++) {
			String e = elements[i];
			assertFalse(pq.contains(e));
		}
		
		pq.clear();
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		list3.remove(0);
		assertTrue(pq.removeAll(list3));
		assertEquals(1, pq.size());
		assertFalse(pq.removeAll(list3));
		assertEquals(1, pq.size());
		assertTrue(pq.contains(elements[0]));
		for (int i = 1; i < elements.length; i++) {
			String e = elements[i];
			assertFalse(pq.contains(e));
		}
	}
	
	@Test
	public void testIterator() {
		int n = 4;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			int count = 0;
			for (PriorityQueueNode.Double<String> e : pq) {
				count++;
			}
			assertEquals(m, count);
			count = 0;
			final Iterator<PriorityQueueNode.Double<String>> iter = pq.iterator();
			while (iter.hasNext()) {
				PriorityQueueNode.Double<String> e = iter.next();
				count++;
			}
			assertEquals(m, count);
			NoSuchElementException thrown = assertThrows( 
				NoSuchElementException.class,
				() -> iter.next()
			);
		}
	}
	
	@Test
	public void testIteratorWithChildren() {
		int n = 8;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			String minElement = pq.pollElement();
			int count = 0;
			for (PriorityQueueNode.Double<String> e : pq) {
				count++;
			}
			assertEquals(m > 0 ? m-1 : 0, count);
			count = 0;
			final Iterator<PriorityQueueNode.Double<String>> iter = pq.iterator();
			while (iter.hasNext()) {
				PriorityQueueNode.Double<String> e = iter.next();
				count++;
			}
			assertEquals(m > 0 ? m-1 : 0, count);
			NoSuchElementException thrown = assertThrows( 
				NoSuchElementException.class,
				() -> iter.next()
			);
		}
	}
	
	@Test
	public void testToArray() {
		int n = 4;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			Object[] array = pq.toArray();
			assertEquals(m, array.length);
			int j = 0;
			for (PriorityQueueNode.Double<String> e : pq) {
				assertEquals(e, (PriorityQueueNode.Double)array[j]);
				j++;
			}
		}
	}
	
	@Test
	public void testToArrayExistingArray() {
		int n = 4;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		for (int m = 0; m <= n; m++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			PriorityQueueNode.Double[] a1 = new PriorityQueueNode.Double[n];
			PriorityQueueNode.Double[] a2 = pq.toArray(a1);
			assertTrue(a1 == a2);
			int j = 0;
			for (PriorityQueueNode.Double<String> e : pq) {
				assertEquals(e, a2[j]);
				j++;
			}
			assertEquals(m, j);
			if (m<n) {
				assertNull(a2[j]);
			}
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
		PriorityQueueNode.Double[] a1 = new PriorityQueueNode.Double[n-1];
		PriorityQueueNode.Double[] a2 = pq.toArray(a1);
		assertTrue(a1 != a2);
		assertEquals(n, a2.length);
		int j = 0;
		for (PriorityQueueNode.Double<String> e : pq) {
			assertEquals(e, a2[j]);
			j++;
		}
		assertEquals(n, j);
	}
	
	@Test
	public void testClear() {
		int n = 11;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap(list);
		assertEquals(n, pq.size());
		pq.clear();
		assertEquals(0, pq.size());
		for (int i = 0; i < n; i++) {
			assertFalse(pq.contains(pairs[i].element));
		}
	}
	
	@Test
	public void testCopy() {
		int n = 24;
		String[] elements = createStringsArbitrary(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list1 = new ArrayList<PriorityQueueNode.Double<String>>();
		ArrayList<PriorityQueueNode.Double<String>> list2 = new ArrayList<PriorityQueueNode.Double<String>>();
		ArrayList<PriorityQueueNode.Double<String>> list3 = new ArrayList<PriorityQueueNode.Double<String>>();
		ArrayList<PriorityQueueNode.Double<String>> list4 = new ArrayList<PriorityQueueNode.Double<String>>();
		FibonacciHeapDouble<String> pq5 = FibonacciHeapDouble.createMinHeap();
		FibonacciHeapDouble<String> pq6 = FibonacciHeapDouble.createMaxHeap();
		int iter = 0;
		for (PriorityQueueNode.Double<String> next : pairs) {
			list1.add(next);
			list2.add(next);
			iter++;
			pq5.offer(next);
			pq6.offer(next);
			if (iter % 8 == 0) {
				pq5.poll();
				pq6.poll();
			}
		}
		for (int i = 0; i < n; i++) {
			list3.add(new PriorityQueueNode.Double<String>(elements[i], 42));
			list4.add(new PriorityQueueNode.Double<String>(elements[i], 42));
		}
		FibonacciHeapDouble<String> pq1 = FibonacciHeapDouble.createMinHeap(list1);
		FibonacciHeapDouble<String> pq2 = FibonacciHeapDouble.createMaxHeap(list2);
		FibonacciHeapDouble<String> pq3 = FibonacciHeapDouble.createMinHeap(list3);
		FibonacciHeapDouble<String> pq4 = FibonacciHeapDouble.createMaxHeap(list4);
		FibonacciHeapDouble<String> copy1 = pq1.copy();
		FibonacciHeapDouble<String> copy2 = pq2.copy();
		FibonacciHeapDouble<String> copy3 = pq3.copy();
		FibonacciHeapDouble<String> copy4 = pq4.copy();
		FibonacciHeapDouble<String> copy5 = pq5.copy();
		FibonacciHeapDouble<String> copy6 = pq6.copy();
		assertEquals(pq1, copy1);
		assertEquals(pq2, copy2);
		assertEquals(pq3, copy3);
		assertEquals(pq4, copy4);
		assertEquals(pq5, copy5);
		assertEquals(pq6, copy6);
		assertTrue(pq1 != copy1);
		assertTrue(pq2 != copy2);
		assertTrue(pq3 != copy3);
		assertTrue(pq4 != copy4);
		assertTrue(pq5 != copy5);
		assertTrue(pq6 != copy6);
		assertNotEquals(pq2, copy1);
		assertNotEquals(pq3, copy1);
		assertNotEquals(pq4, copy1);
		assertNotEquals(pq1, copy2);
		assertNotEquals(pq3, copy2);
		assertNotEquals(pq4, copy2);
		assertNotEquals(pq1, copy3);
		assertNotEquals(pq2, copy3);
		assertNotEquals(pq4, copy3);
		assertNotEquals(pq1, copy4);
		assertNotEquals(pq2, copy4);
		assertNotEquals(pq3, copy4);
		assertNotEquals(pq6, copy5);
		assertNotEquals(pq5, copy6);
	}
	
	@Test
	public void testCopyEmptyHeap() {	
		FibonacciHeapDouble<String> pqEmptyMin = FibonacciHeapDouble.createMinHeap();
		FibonacciHeapDouble<String> pqEmptyMax = FibonacciHeapDouble.createMaxHeap();
		FibonacciHeapDouble<String> pqEmptyMinCopy = pqEmptyMin.copy();
		FibonacciHeapDouble<String> pqEmptyMaxCopy = pqEmptyMax.copy();
		assertEquals(pqEmptyMin, pqEmptyMinCopy);
		assertEquals(pqEmptyMax, pqEmptyMaxCopy);
		assertNotEquals(pqEmptyMin, pqEmptyMaxCopy);
		assertNotEquals(pqEmptyMax, pqEmptyMinCopy);
		assertTrue(pqEmptyMin != pqEmptyMinCopy);
		assertTrue(pqEmptyMax != pqEmptyMaxCopy);
		assertEquals(0, pqEmptyMinCopy.size());
		assertEquals(0, pqEmptyMaxCopy.size());
		assertEquals(0, pqEmptyMin.size());
		assertEquals(0, pqEmptyMax.size());
		assertTrue(pqEmptyMinCopy.isEmpty());
		assertTrue(pqEmptyMaxCopy.isEmpty());
		assertTrue(pqEmptyMin.isEmpty());
		assertTrue(pqEmptyMax.isEmpty());
	}
	
	@Test
	public void testEqualsAndHashCode() {
		int n = 11;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list1 = new ArrayList<PriorityQueueNode.Double<String>>();
		ArrayList<PriorityQueueNode.Double<String>> list2 = new ArrayList<PriorityQueueNode.Double<String>>();
		ArrayList<PriorityQueueNode.Double<String>> list3 = new ArrayList<PriorityQueueNode.Double<String>>();
		ArrayList<PriorityQueueNode.Double<String>> list4 = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list1.add(next);
			list2.add(next);
			list3.add(next);
			list4.add(next);
		}
		FibonacciHeapDouble<String> pq1 = FibonacciHeapDouble.createMinHeap(list1);
		FibonacciHeapDouble<String> pq2 = FibonacciHeapDouble.createMinHeap(list2);
		FibonacciHeapDouble<String> pq3 = FibonacciHeapDouble.createMaxHeap(list3);
		FibonacciHeapDouble<String> pq4 = FibonacciHeapDouble.createMaxHeap(list4);
		assertEquals(pq1, pq2);
		assertEquals(pq1.hashCode(), pq2.hashCode());
		assertEquals(pq3, pq4);
		assertEquals(pq3.hashCode(), pq4.hashCode());
		assertNotEquals(pq1, pq3);
		assertNotEquals(pq1.hashCode(), pq3.hashCode());
		pq2.offer(""+((char)0), 0);
		assertNotEquals(pq1, pq2);
		assertNotEquals(pq1.hashCode(), pq2.hashCode());
		pq1.offer(""+((char)0), 1);
		assertNotEquals(pq1, pq2);
		assertNotEquals(pq1.hashCode(), pq2.hashCode());
		pq1.clear();
		pq2.clear();
		pq3.clear();
		pq4.clear();
		for (int i = 0; i < n; i++) {
			pq1.offer(""+((char)('A'+i)), 42);
			pq3.offer(""+((char)('A'+i)), 42);
			pq2.offer(""+((char)('A'+(n-1)-i)), 42);
			pq4.offer(""+((char)('A'+(n-1)-i)), 42);
		}
		assertNotEquals(pq1, pq3);
		assertNotEquals(pq3, pq1);
		assertNotEquals(pq3, pq4);
		assertNotEquals(pq1, pq2);
		assertNotEquals(pq1.hashCode(), pq2.hashCode());
		assertNotEquals(pq3.hashCode(), pq4.hashCode());
		assertNotEquals(pq1, null);
		assertNotEquals(pq3, null);
		assertNotEquals(pq1, "hello");
		assertNotEquals(pq3, "hello");
	}
	
	// MIN HEAP TESTS
	
	@Test
	public void testRemoveMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = (2 + 2*i);
		}
		// Via element
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.remove(elements[i]));
			assertFalse(pq.contains(elements[i]));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(elements[i]));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// one element left
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			pq.offer(elements[0], priorities[0]);
			assertTrue(pq.remove(elements[0]));
			assertFalse(pq.contains(elements[0]));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(elements[0]));
			assertEquals(0, pq.size());
		}
		// Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], 42);
			}
			assertTrue(pq.remove(elements[i]));
			assertFalse(pq.contains(elements[i]));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(elements[i]));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertNotEquals(elements[i], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// Percolate Up
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			double[] p = {0, 3, 1, 7, 4, 5, 2};
			for (int i = 0; i < p.length; i++) {
				pq.offer(elements[i], p[i]);
			}
			assertTrue(pq.remove(elements[3]));
			int[] expectedIndexOrder = {0, 2, 6, 1, 4, 5};
			for (int i = 0; i < expectedIndexOrder.length; i++) {
				assertEquals(elements[expectedIndexOrder[i]], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// VIA PAIR
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[i], priorities[i]);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// one element left via pair
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			pq.offer(elements[0], priorities[0]);
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[0], priorities[0]);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(0, pq.size());
		}
		// Via pair: Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], 42);
			}
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[i], 42);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertNotEquals(elements[i], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// Percolate Up Via Pair
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			double[] p = {0, 3, 1, 7, 4, 5, 2};
			for (int i = 0; i < p.length; i++) {
				pq.offer(elements[i], p[i]);
			}
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[3], p[3]);
			assertTrue(pq.remove(pair));
			int[] expectedIndexOrder = {0, 2, 6, 1, 4, 5};
			for (int i = 0; i < expectedIndexOrder.length; i++) {
				assertEquals(elements[expectedIndexOrder[i]], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
	}
	
	@Test
	public void testChangePriorityMinHeapCascadingCut() {
		double[] priorities = { 1, 5, 2, 11, 4, 13, 17, 15, 8, 31, 40, 37, 14, 100, 50, 70, 30, 45, 60, 0, 33, 99, 16, 97};
		int[] orderedIndexes = {};
		int n = priorities.length;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
					for (int j = 0; j < n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e1 = pq.pollElement();
					int index = Arrays.binarySearch(elements, e1);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("A", e1);
					assertEquals(n/3-1, pq.size());
					for (int j = n/3; j < 2*n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e2 = pq.pollElement();
					index = Arrays.binarySearch(elements, e2);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("C", e2);
					assertEquals(2*n/3-2, pq.size());
					for (int j = 2*n/3; j < n; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e3 = pq.pollElement();
					index = Arrays.binarySearch(elements, e3);
					if (index == i || index == k) {
						continue;
					}
					double p3 = priorities[Arrays.binarySearch(elements, e3)];
					assertEquals("T", e3);
					assertEquals(n-3, pq.size());
					if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
						assertTrue(pq.change(elements[i], -1));
						assertEquals(-1.0, pq.peekPriority(elements[i]), 0.0);
						assertEquals(n-3, pq.size());
					}
					if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
						assertTrue(pq.change(elements[k], -2));
						assertEquals(-2.0, pq.peekPriority(elements[k]), 0.0);
						assertEquals(n-3, pq.size());
					}
					double lastP = -1000;
					int count = 0;
					while (!pq.isEmpty()) {
						PriorityQueueNode.Double<String> e = pq.poll();
						String msg = "count,e,p,i,k="+count+","+e.element+","+e.value+","+i+","+k;
						assertTrue(e.value >= lastP, msg);
						lastP = e.value;
						int j = Arrays.binarySearch(elements, e.element);
						if (j != i && j != k) {
							assertEquals(priorities[j], e.value, 0.0);
						} else if (j == i) {
							assertEquals(-1, e.value, 0.0);
						} else if (j == k) {
							assertEquals(-2, e.value, 0.0);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	@Test
	public void testChangePriorityMinHeapMultiLevelIncrease() {
		double[] priorities = { 1, 5, 2, 11, 4, 13, 17, 15, 8, 31, 40, 37, 14, 100, 50, 70, 30, 45, 60, 0, 33, 99, 16, 97};
		int[] orderedIndexes = {};
		int n = priorities.length;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
					for (int j = 0; j < n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e1 = pq.pollElement();
					int index = Arrays.binarySearch(elements, e1);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("A", e1);
					assertEquals(n/3-1, pq.size());
					for (int j = n/3; j < 2*n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e2 = pq.pollElement();
					index = Arrays.binarySearch(elements, e2);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("C", e2);
					assertEquals(2*n/3-2, pq.size());
					for (int j = 2*n/3; j < n; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e3 = pq.pollElement();
					index = Arrays.binarySearch(elements, e3);
					if (index == i || index == k) {
						continue;
					}
					double p3 = priorities[Arrays.binarySearch(elements, e3)];
					assertEquals("T", e3);
					assertEquals(n-3, pq.size());
					if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
						assertTrue(pq.change(elements[i], 95));
						assertEquals(95.0, pq.peekPriority(elements[i]), 0.0);
						assertEquals(n-3, pq.size());
					}
					if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
						assertTrue(pq.change(elements[k], 102));
						assertEquals(102.0, pq.peekPriority(elements[k]), 0.0);
						assertEquals(n-3, pq.size());
					}
					double lastP = -1000;
					int count = 0;
					while (!pq.isEmpty()) {
						PriorityQueueNode.Double<String> e = pq.poll();
						String msg = "count,e,p,i,k="+count+","+e.element+","+e.value+","+i+","+k;
						assertTrue(e.value >= lastP, msg);
						lastP = e.value;
						int j = Arrays.binarySearch(elements, e.element);
						if (j != i && j != k) {
							assertEquals(priorities[j], e.value, 0.0);
						} else if (j == i) {
							assertEquals(95, e.value, 0.0);
						} else if (j == k) {
							assertEquals(102, e.value, 0.0);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	@Test
	public void testChangePriorityMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2 + 2*i;
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], 1));
			assertEquals(1.0, pq.peekPriority(elements[i]), 0.0);
			assertEquals(elements[i], pq.pollElement());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
		// to back tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], 100));
			assertEquals(100.0, pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.isEmpty());
		}
		// to interior tests
		double maxP = 2*(n-1) + 2;
		for (double p = 3; p <= maxP; p += 2.0) {
			for (int i = 0; i < n; i++) {
				FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				assertTrue(pq.change(elements[i], p));
				assertEquals(p, pq.peekPriority(elements[i]), 0.0);
				int j = 0;
				for (; j < n; j++) {
					if (i != j) {
						if (priorities[j] < p) {
							assertEquals(elements[j], pq.pollElement(), "p,i,j="+p+","+i+","+j);
						} else {
							break;
						}
					}
				}
				assertEquals(elements[i], pq.pollElement(), "p,i,j="+p+","+i+","+j);
				for (; j < n; j++) {
					if (i!=j && priorities[j] > p) {
						assertEquals(elements[j], pq.pollElement());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// equal change test
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.change(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (double p = 1; p <= maxP; p += 2.0) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change("YYY", p));
			assertEquals(p, pq.peekPriority("YYY"), 0.0);
			int j = 0;
			for (; j < n; j++) {
				if (priorities[j] < p) {
					assertEquals(elements[j], pq.pollElement());
				} else {
					break;
				}
			}
			assertEquals("YYY", pq.pollElement());
			for (; j < n; j++) {
				if (priorities[j] > p) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
		// change not lower than parent
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			String minElement = pq.pollElement();
			if (!minElement.equals(elements[i])) {
				assertTrue(pq.change(elements[i], priorities[i]-0.5));
				assertEquals(priorities[i]-0.5, pq.peekPriority(elements[i]), 0.0);
			}
			for (int j = 0; j < n; j++) {
				if (!minElement.equals(elements[j])) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	@Test
	public void testPromoteDemoteMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2 + 2*i;
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.promote(elements[i], 1));
			assertEquals(1, pq.peekPriority(elements[i]), 0.0);
			assertEquals(elements[i], pq.pollElement());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
		// to back tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.demote(elements[i], 100));
			assertEquals(100, pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.isEmpty());
		}
		// to interior tests
		double maxP = 2*(n-1) + 2;
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				if (p < priorities[i]) {
					assertTrue(pq.promote(elements[i], p));
				} else {
					assertTrue(pq.demote(elements[i], p));
				}
				assertEquals(p, pq.peekPriority(elements[i]), 0.0);
				int j = 0;
				for (; j < n; j++) {
					if (i != j) {
						if (priorities[j] < p) {
							assertEquals(elements[j], pq.pollElement(), "p,i,j="+p+","+i+","+j);
						} else {
							break;
						}
					}
				}
				assertEquals(elements[i], pq.pollElement(), "p,i,j="+p+","+i+","+j);
				for (; j < n; j++) {
					if (i!=j && priorities[j] > p) {
						assertEquals(elements[j], pq.pollElement());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// equal change test
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.promote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			assertFalse(pq.demote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.promote("YYY", p));
			assertFalse(pq.contains("YYY"));
			assertFalse(pq.demote("YYY", p));
			assertFalse(pq.contains("YYY"));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	@Test
	public void testDefaultMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((double)elements[i].charAt(0), pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Double<String>("A",(int)'A'), pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(new PriorityQueueNode.Double<String>(expected, (int)('A'+i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
		
		pq = FibonacciHeapDouble.createMinHeap(new ArrayList<PriorityQueueNode.Double<String>>());
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		
		final ArrayList<PriorityQueueNode.Double<String>> list2 = new ArrayList<PriorityQueueNode.Double<String>>();
		list2.add(pairs[0]);
		list2.add(pairs[0]);
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> FibonacciHeapDouble.createMinHeap(list2)
		);
	}
	
	@Test
	public void testListMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Double<String>("A",(int)'A'), pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(new PriorityQueueNode.Double<String>(expected, (int)('A'+i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
		
	// MAX HEAP TESTS
	
	@Test
	public void testRemoveMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = -(2 + 2*i);
		}
		// Via element
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.remove(elements[i]));
			assertFalse(pq.contains(elements[i]));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(elements[i]));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// one element left
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			pq.offer(elements[0], priorities[0]);
			assertTrue(pq.remove(elements[0]));
			assertFalse(pq.contains(elements[0]));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(elements[0]));
			assertEquals(0, pq.size());
		}
		// Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], 42);
			}
			assertTrue(pq.remove(elements[i]));
			assertFalse(pq.contains(elements[i]));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(elements[i]));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertNotEquals(elements[i], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// Percolate Up
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			double[] p = {0, -3, -1, -7, -4, -5, -2};
			for (int i = 0; i < p.length; i++) {
				pq.offer(elements[i], p[i]);
			}
			assertTrue(pq.remove(elements[3]));
			int[] expectedIndexOrder = {0, 2, 6, 1, 4, 5};
			for (int i = 0; i < expectedIndexOrder.length; i++) {
				assertEquals(elements[expectedIndexOrder[i]], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// VIA PAIR
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[i], priorities[i]);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// one element left via pair
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			pq.offer(elements[0], priorities[0]);
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[0], priorities[0]);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(0, pq.size());
		}
		// Via pair: Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], 42);
			}
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[i], 42);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(n-1, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(n-1, pq.size());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertNotEquals(elements[i], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
		// Percolate Up Via Pair
		{
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			double[] p = {0, -3, -1, -7, -4, -5, -2};
			for (int i = 0; i < p.length; i++) {
				pq.offer(elements[i], p[i]);
			}
			PriorityQueueNode.Double<String> pair = new PriorityQueueNode.Double<String>(elements[3], p[3]);
			assertTrue(pq.remove(pair));
			int[] expectedIndexOrder = {0, 2, 6, 1, 4, 5};
			for (int i = 0; i < expectedIndexOrder.length; i++) {
				assertEquals(elements[expectedIndexOrder[i]], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
			assertEquals(0, pq.size());
		}
	}
	
	@Test
	public void testChangePriorityMaxHeapCascadingCut() {
		double[] priorities = { -1, -5, -2, -11, -4, -13, -17, -15, -8, -31, -40, -37, -14, -100, -50, -70, -30, -45, -60, 0, -33, -99, -16, -97};
		int n = priorities.length;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
					for (int j = 0; j < n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e1 = pq.pollElement();
					int index = Arrays.binarySearch(elements, e1);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("A", e1);
					assertEquals(n/3-1, pq.size());
					for (int j = n/3; j < 2*n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e2 = pq.pollElement();
					index = Arrays.binarySearch(elements, e2);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("C", e2);
					assertEquals(2*n/3-2, pq.size());
					for (int j = 2*n/3; j < n; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e3 = pq.pollElement();
					index = Arrays.binarySearch(elements, e3);
					if (index == i || index == k) {
						continue;
					}
					double p3 = priorities[Arrays.binarySearch(elements, e3)];
					assertEquals("T", e3);
					assertEquals(n-3, pq.size());
					if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
						assertTrue(pq.change(elements[i], 2));
						assertEquals(2.0, pq.peekPriority(elements[i]), 0.0);
						assertEquals(n-3, pq.size());
					}
					if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
						assertTrue(pq.change(elements[k], 1));
						assertEquals(1.0, pq.peekPriority(elements[k]), 0.0);
						assertEquals(n-3, pq.size());
					}
					double lastP = 1000;
					int count = 0;
					while (!pq.isEmpty()) {
						PriorityQueueNode.Double<String> e = pq.poll();
						String msg = "count,e,p,i,k="+count+","+e.element+","+e.value+","+i+","+k;
						assertTrue(e.value <= lastP, msg);
						lastP = e.value;
						int j = Arrays.binarySearch(elements, e.element);
						if (j != i && j != k) {
							assertEquals(priorities[j], e.value, 0.0);
						} else if (j == i) {
							assertEquals(2, e.value, 0.0);
						} else if (j == k) {
							assertEquals(1, e.value, 0.0);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	@Test
	public void testChangePriorityMaxHeapMultiLevelIncrease() {
		double[] priorities = { -1, -5, -2, -11, -4, -13, -17, -15, -8, -31, -40, -37, -14, -100, -50, -70, -30, -45, -60, 0, -33, -99, -16, -97};
		int[] orderedIndexes = {};
		int n = priorities.length;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
					for (int j = 0; j < n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e1 = pq.pollElement();
					int index = Arrays.binarySearch(elements, e1);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("A", e1);
					assertEquals(n/3-1, pq.size());
					for (int j = n/3; j < 2*n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e2 = pq.pollElement();
					index = Arrays.binarySearch(elements, e2);
					if (index == i || index == k) {
						continue;
					}
					assertEquals("C", e2);
					assertEquals(2*n/3-2, pq.size());
					for (int j = 2*n/3; j < n; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					String e3 = pq.pollElement();
					index = Arrays.binarySearch(elements, e3);
					if (index == i || index == k) {
						continue;
					}
					double p3 = priorities[Arrays.binarySearch(elements, e3)];
					assertEquals("T", e3);
					assertEquals(n-3, pq.size());
					if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
						assertTrue(pq.change(elements[i], -102));
						assertEquals(-102.0, pq.peekPriority(elements[i]), 0.0);
						assertEquals(n-3, pq.size());
					}
					if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
						assertTrue(pq.change(elements[k], -95));
						assertEquals(-95.0, pq.peekPriority(elements[k]), 0.0);
						assertEquals(n-3, pq.size());
					}
					double lastP = 1000;
					int count = 0;
					while (!pq.isEmpty()) {
						PriorityQueueNode.Double<String> e = pq.poll();
						String msg = "count,e,p,i,k="+count+","+e.element+","+e.value+","+i+","+k;
						assertTrue(e.value <= lastP, msg);
						lastP = e.value;
						int j = Arrays.binarySearch(elements, e.element);
						if (j != i && j != k) {
							assertEquals(priorities[j], e.value, 0.0);
						} else if (j == i) {
							assertEquals(-102, e.value, 0.0);
						} else if (j == k) {
							assertEquals(-95, e.value, 0.0);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	@Test
	public void testChangePriorityMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = -(2 + 2*i);
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], -1));
			assertEquals(-1.0, pq.peekPriority(elements[i]), 0.0);
			assertEquals(elements[i], pq.pollElement());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
		// to back tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], -100));
			assertEquals(-100.0, pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.isEmpty());
		}
		// to interior tests
		int maxP = 2*(n-1) + 2;
		for (double p = 3; p <= maxP; p += 2.0) {
			for (int i = 0; i < n; i++) {
				FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				assertTrue(pq.change(elements[i], -p));
				assertEquals(-p, pq.peekPriority(elements[i]), 0.0);
				int j = 0;
				for (; j < n; j++) {
					if (i != j) {
						if (priorities[j] > -p) {
							assertEquals(elements[j], pq.pollElement(), "p,i,j="+p+","+i+","+j);
						} else {
							break;
						}
					}
				}
				assertEquals(elements[i], pq.pollElement(), "p,i,j="+p+","+i+","+j);
				for (; j < n; j++) {
					if (i!=j && priorities[j] < -p) {
						assertEquals(elements[j], pq.pollElement());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// equal change test
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.change(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (double p = 1; p <= maxP; p += 2.0) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change("YYY", -p));
			assertEquals(-p, pq.peekPriority("YYY"), 0.0);
			int j = 0;
			for (; j < n; j++) {
				if (priorities[j] > -p) {
					assertEquals(elements[j], pq.pollElement());
				} else {
					break;
				}
			}
			assertEquals("YYY", pq.pollElement());
			for (; j < n; j++) {
				if (priorities[j] < -p) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
		// change not higher than parent
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			String maxElement = pq.pollElement();
			if (!maxElement.equals(elements[i])) {
				assertTrue(pq.change(elements[i], priorities[i]+0.5));
				assertEquals(priorities[i]+0.5, pq.peekPriority(elements[i]), 0.0);
			}
			for (int j = 0; j < n; j++) {
				if (!maxElement.equals(elements[j])) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	@Test
	public void testPromoteDemoteMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = -(2 + 2*i);
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.promote(elements[i], -1));
			assertEquals(-1, pq.peekPriority(elements[i]), 0.0);
			assertEquals(elements[i], pq.pollElement());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
		// to back tests
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.demote(elements[i], -100));
			assertEquals(-100, pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.isEmpty());
		}
		// to interior tests
		double maxP = 2*(n-1) + 2;
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				if (-p > priorities[i]) {
					assertTrue(pq.promote(elements[i], -p));
				} else {
					assertTrue(pq.demote(elements[i], -p));
				}
				assertEquals(-p, pq.peekPriority(elements[i]), 0.0);
				int j = 0;
				for (; j < n; j++) {
					if (i != j) {
						if (priorities[j] > -p) {
							assertEquals(elements[j], pq.pollElement(), "p,i,j="+p+","+i+","+j);
						} else {
							break;
						}
					}
				}
				assertEquals(elements[i], pq.pollElement(), "p,i,j="+p+","+i+","+j);
				for (; j < n; j++) {
					if (i!=j && priorities[j] < -p) {
						assertEquals(elements[j], pq.pollElement());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// equal change test
		for (int i = 0; i < n; i++) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.promote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			assertFalse(pq.demote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.promote("YYY", -p));
			assertFalse(pq.contains("YYY"));
			assertFalse(pq.demote("YYY", -p));
			assertFalse(pq.contains("YYY"));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	@Test
	public void testDefaultMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((double)elements[i].charAt(0), pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Double<String>("A",(int)'A'), pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(new PriorityQueueNode.Double<String>(expected, (int)('A'-i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
		
		pq = FibonacciHeapDouble.createMaxHeap(new ArrayList<PriorityQueueNode.Double<String>>());
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		 
		final ArrayList<PriorityQueueNode.Double<String>> list2 = new ArrayList<PriorityQueueNode.Double<String>>();
		list2.add(pairs[0]);
		list2.add(pairs[0]);
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> FibonacciHeapDouble.createMaxHeap(list2)
		);
	}
	
	@Test
	public void testListMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeapDouble<String> pq = FibonacciHeapDouble.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Double<String>("A",(int)'A'), pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
		}
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority("hello"), 0.0);
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(new PriorityQueueNode.Double<String>(expected, (int)('A'-i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}	
	
	private String[] createStrings(int n) {
		String[] s = new String[n];
		for (int i = 0; i < n; i++) {
			s[i] = ((char)('A'+i)) + "";
		}
		return s;
	}
	
	private String[] createStringsMaxCase(int n) {
		String[] s = new String[n];
		for (int i = 0; i < n; i++) {
			s[i] = ((char)('A'-i)) + "";
		}
		return s;
	}
	
	private double[] createPriorities(String[] elements) {
		double[] p = new double[elements.length];
		for (int i = 0; i < elements.length; i++) {
			p[i] = (double)elements[i].charAt(0);
		}
		return p;
	}
	
	private String[] createStringsRev(int n) {
		String[] s = new String[n];
		for (int i = 0; i < n; i++) {
			s[n-1-i] = ((char)('A'+i)) + "";
		}
		return s;
	}
	
	private String[] createStringsRevMaxCase(int n) {
		String[] s = new String[n];
		for (int i = 0; i < n; i++) {
			s[n-1-i] = ((char)('A'-i)) + "";
		}
		return s;
	}
	
	private String[] createStringsArbitrary(int n) {
		ArrayList<String> list = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			list.add(((char)('A'+i)) + "");
		}
		shuffle(list, new SplittableRandom(42));
		return list.toArray(new String[n]);
	}
	
	private String[] createStringsArbitraryMaxCase(int n) {
		ArrayList<String> list = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			list.add(((char)('A'-i)) + "");
		}
		shuffle(list, new SplittableRandom(42));
		return list.toArray(new String[n]);
	}
	
	private void shuffle(ArrayList<String> list, SplittableRandom r) {
		for (int i = list.size()-1; i > 0; i--) {
			int j = r.nextInt(i+1);
			if (i!=j) {
				String temp = list.get(i);
				list.set(i, list.get(j));
				list.set(j, temp);
			}
		}
	}
	
	private PriorityQueueNode.Double<String>[] createPairs(String[] elements, double[] priorities) {
		@SuppressWarnings("unchecked")
		PriorityQueueNode.Double<String>[] pairs = (PriorityQueueNode.Double<String>[])new PriorityQueueNode.Double[elements.length];
		for (int i = 0; i < pairs.length; i++) {
			pairs[i] = new PriorityQueueNode.Double<String>(elements[i], priorities[i]);
		}
		return pairs;
	}
}
