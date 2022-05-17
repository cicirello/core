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

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * JUnit tests for the BinaryHeapDouble class.
 */
public class BinaryHeapDoubleTests {
	
	// TESTS THAT ARE NEITHER STRICTLY MIN HEAP TESTS NOW MAX HEAP TESTS
	
	@Test
	public void testRetainAll() {
		String[] elements = {"A", "B", "C", "D"};
		double[] priorities = { 8, 6, 4, 2 };
		final BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
		}
		assertEquals(elements.length, pq.size());
		String[] retain = {"A", "E", "C", "F"};
		ArrayList<Object> keepThese = new ArrayList<Object>();
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
	}
	
	@Test
	public void testIterator() {
		int n = 4;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
	public void testToArray() {
		int n = 4;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
	public void testCapacity() {
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
		assertEquals(BinaryHeapDouble.DEFAULT_INITIAL_CAPACITY, pq.capacity());
		assertEquals(0, pq.size());
		for (int i = 1; i <= 5; i++) {
			pq = BinaryHeapDouble.createMinHeap(i);
			assertEquals(i, pq.capacity());
			assertEquals(0, pq.size());
		}
		pq.ensureCapacity(100);
		assertEquals(100, pq.capacity());
		assertEquals(0, pq.size());
		pq.ensureCapacity(50);
		assertEquals(100, pq.capacity());
		assertEquals(0, pq.size());
		pq.trimToSize();
		assertEquals(1, pq.capacity());
		assertEquals(0, pq.size());
		int n = 11;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Double<String>> list = new ArrayList<PriorityQueueNode.Double<String>>();
		for (PriorityQueueNode.Double<String> next : pairs) {
			list.add(next);
		}
		pq = BinaryHeapDouble.createMinHeap(list);
		assertEquals(n, pq.capacity());
		assertEquals(n, pq.size());
		pq.trimToSize();
		assertEquals(n, pq.capacity());
		assertEquals(n, pq.size());
		pq.ensureCapacity(55);
		assertEquals(55, pq.capacity());
		assertEquals(n, pq.size());
		pq.trimToSize();
		assertEquals(n, pq.capacity());
		assertEquals(n, pq.size());
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
		assertEquals(n, pq.capacity());
		assertEquals(0, pq.size());
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap(list);
		assertEquals(n, pq.size());
		pq.clear();
		assertEquals(0, pq.size());
		for (int i = 0; i < n; i++) {
			assertFalse(pq.contains(pairs[i].element));
		}
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
		BinaryHeapDouble<String> pq1 = BinaryHeapDouble.createMinHeap(list1);
		BinaryHeapDouble<String> pq2 = BinaryHeapDouble.createMinHeap(list2);
		BinaryHeapDouble<String> pq3 = BinaryHeapDouble.createMaxHeap(list3);
		BinaryHeapDouble<String> pq4 = BinaryHeapDouble.createMaxHeap(list4);
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
			pq.offer(elements[0], priorities[0]);
			assertTrue(pq.remove(elements[0]));
			assertFalse(pq.contains(elements[0]));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(elements[0]));
			assertEquals(0, pq.size());
		}
		// Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
	public void testChangePriorityMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2 + 2*i;
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change(elements[i], 1);
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change(elements[i], 100);
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
				BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				pq.change(elements[i], p);
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change(elements[i], priorities[i]);
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (double p = 1; p <= maxP; p += 2.0) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change("YYY", p);
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
	}
	
	@Test
	public void testDefaultMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap();
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap(list);
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
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeapDouble.createMinHeap(new ArrayList<PriorityQueueNode.Double<String>>())
		);
		final ArrayList<PriorityQueueNode.Double<String>> list2 = new ArrayList<PriorityQueueNode.Double<String>>();
		list2.add(pairs[0]);
		list2.add(pairs[0]);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeapDouble.createMinHeap(list2)
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap(list);
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap(list);
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
	
	@Test
	public void testMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
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
			assertEquals(elements[i], pq.pollElement());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollElement());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeapDouble.createMinHeap(0)
		);
	}
	
	@Test
	public void testMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((double)elements[i].charAt(0), pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
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
			assertEquals(elements[n-1-i], pq.pollElement());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollElement());
	}
	
	@Test
	public void testMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.POSITIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
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
			assertEquals(expected, pq.pollElement());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollElement());
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
			pq.offer(elements[0], priorities[0]);
			assertTrue(pq.remove(elements[0]));
			assertFalse(pq.contains(elements[0]));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(elements[0]));
			assertEquals(0, pq.size());
		}
		// Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
	public void testChangePriorityMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		double[] priorities = new double[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = -(2 + 2*i);
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change(elements[i], -1);
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change(elements[i], -100);
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
				BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				pq.change(elements[i], -p);
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
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change(elements[i], priorities[i]);
			assertEquals(priorities[i], pq.peekPriority(elements[i]), 0.0);
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (double p = 1; p <= maxP; p += 2.0) {
			BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			pq.change("YYY", -p);
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
	}
	
	@Test
	public void testDefaultMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap();
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap(list);
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
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeapDouble.createMaxHeap(new ArrayList<PriorityQueueNode.Double<String>>())
		);
		final ArrayList<PriorityQueueNode.Double<String>> list2 = new ArrayList<PriorityQueueNode.Double<String>>();
		list2.add(pairs[0]);
		list2.add(pairs[0]);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeapDouble.createMaxHeap(list2)
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap(list);
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
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap(list);
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
	
	@Test
	public void testMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((double)'A', pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
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
			assertEquals(elements[i], pq.pollElement());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollElement());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeapDouble.createMaxHeap(0)
		);
	}
	
	@Test
	public void testMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((double)elements[i].charAt(0), pq.peekPriority(), 0.0);
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
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
			assertEquals(elements[n-1-i], pq.pollElement());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollElement());
	}
	
	@Test
	public void testMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		double[] priorities = createPriorities(elements);
		PriorityQueueNode.Double<String>[] pairs = createPairs(elements, priorities);
		BinaryHeapDouble<String> pq = BinaryHeapDouble.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Double.NEGATIVE_INFINITY, pq.peekPriority(), 0.0);
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
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
			assertEquals(expected, pq.pollElement());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollElement());
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
		Collections.shuffle(list);
		return list.toArray(new String[n]);
	}
	
	private String[] createStringsArbitraryMaxCase(int n) {
		ArrayList<String> list = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			list.add(((char)('A'-i)) + "");
		}
		Collections.shuffle(list);
		return list.toArray(new String[n]);
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
