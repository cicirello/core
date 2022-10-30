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
 * JUnit tests for the FibonacciHeap class.
 */
public class FibonacciHeapTests extends SharedTestCommonHelpersHeaps {
	
	public FibonacciHeapTests() {
		super(FibonacciHeap::createMinHeap, FibonacciHeap::createMinHeap);
	}
	
	// TESTS THAT ARE NEITHER STRICTLY MIN HEAP TESTS NOW MAX HEAP TESTS
	
	@Test
	public void testContainsAll() {
		containsAll();
	}
	
	@Test
	public void testContainsAllElements() {
		containsAllElements();
	}
	
	@Test
	public void testRetainAll() {
		retainAll();
	}
	
	@Test
	public void testRemoveAll() {
		removeAll();
	}
	
	@Test
	public void testIterator() {
		iterator();
	}
	
	@Test
	public void testIteratorWithChildren() {
		iteratorWithChildren();
	}
	
	@Test
	public void testToArray() {
		toArray();
	}
	
	@Test
	public void testToArrayExistingArray() {
		toArrayExistingArray();
	}
		
	@Test
	public void testClear() {
		int n = 11;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		FibonacciHeap<String> pq = FibonacciHeap.createMinHeap(list);
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
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list3 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list4 = new ArrayList<PriorityQueueNode.Integer<String>>();
		FibonacciHeap<String> pq5 = FibonacciHeap.createMinHeap();
		FibonacciHeap<String> pq6 = FibonacciHeap.createMaxHeap();
		int iter = 0;
		for (PriorityQueueNode.Integer<String> next : pairs) {
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
			list3.add(new PriorityQueueNode.Integer<String>(elements[i], 42));
			list4.add(new PriorityQueueNode.Integer<String>(elements[i], 42));
		}
		FibonacciHeap<String> pq1 = FibonacciHeap.createMinHeap(list1);
		FibonacciHeap<String> pq2 = FibonacciHeap.createMaxHeap(list2);
		FibonacciHeap<String> pq3 = FibonacciHeap.createMinHeap(list3);
		FibonacciHeap<String> pq4 = FibonacciHeap.createMaxHeap(list4);
		FibonacciHeap<String> copy1 = pq1.copy();
		FibonacciHeap<String> copy2 = pq2.copy();
		FibonacciHeap<String> copy3 = pq3.copy();
		FibonacciHeap<String> copy4 = pq4.copy();
		FibonacciHeap<String> copy5 = pq5.copy();
		FibonacciHeap<String> copy6 = pq6.copy();
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
		FibonacciHeap<String> pqEmptyMin = FibonacciHeap.createMinHeap();
		FibonacciHeap<String> pqEmptyMax = FibonacciHeap.createMaxHeap();
		FibonacciHeap<String> pqEmptyMinCopy = pqEmptyMin.copy();
		FibonacciHeap<String> pqEmptyMaxCopy = pqEmptyMax.copy();
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
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list3 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list4 = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list1.add(next);
			list2.add(next);
			list3.add(next);
			list4.add(next);
		}
		FibonacciHeap<String> pq1 = FibonacciHeap.createMinHeap(list1);
		FibonacciHeap<String> pq2 = FibonacciHeap.createMinHeap(list2);
		FibonacciHeap<String> pq3 = FibonacciHeap.createMaxHeap(list3);
		FibonacciHeap<String> pq4 = FibonacciHeap.createMaxHeap(list4);
		SimpleFibonacciHeap<String> pqSimple = SimpleFibonacciHeap.createMinHeap(list1);
		assertNotEquals(pq1, pqSimple);
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
	
	@Test
	public void testMerge() {
		int n = 24;
		String[] elements1 = new String[n];
		int[] priorities1 = new int[n];
		String[] elements2 = new String[n];
		int[] priorities2 = new int[n];
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		final FibonacciHeap<String> pq1 = FibonacciHeap.createMinHeap();
		final FibonacciHeap<String> pq2 = FibonacciHeap.createMinHeap();
		int count = 0;
		for (int i = 0; i < 2*n; i+=2) {
			count++;
			elements1[i/2] = "A" + i;
			elements2[i/2] = "A" + (i+1);
			priorities1[i/2] = i;
			priorities2[i/2] = i+1;
			PriorityQueueNode.Integer<String> node1 = new PriorityQueueNode.Integer<String>(elements1[i/2], priorities1[i/2]);
			PriorityQueueNode.Integer<String> node2 = new PriorityQueueNode.Integer<String>(elements2[i/2], priorities2[i/2]);
			list1.add(node1);
			list2.add(node2);
			pq1.offer(node1);
			pq2.offer(node2);
			if (count == 8 || count == 16) {
				// ensure multilevel structure
				pq1.offer("XYZ", -100);
				pq2.offer("XYZ", -100);
				pq1.poll();
				pq2.poll();
			}
		}
		assertFalse(pq1.merge(FibonacciHeap.createMinHeap()));
		assertTrue(pq1.merge(pq2));
		assertTrue(pq2.isEmpty());
		assertEquals(0, pq2.size());
		assertEquals(2*n, pq1.size());
		for (int i = 0; i < n; i++) {
			assertTrue(pq1.contains(elements1[i]));
			assertTrue(pq1.contains(elements2[i]));
			assertEquals(priorities1[i], pq1.peekPriority(elements1[i]));
			assertEquals(priorities2[i], pq1.peekPriority(elements2[i]));
		}
		for (int i = 0; i < n; i++) {
			assertEquals(list1.get(i), pq1.poll());
			assertEquals(list2.get(i), pq1.poll());
		}
		assertTrue(pq1.isEmpty());
		assertEquals(0, pq1.size());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq1.merge(FibonacciHeap.createMaxHeap())
		);
	}
	
	@Test
	public void testMergeWithSimple() {
		int n = 24;
		String[] elements1 = new String[n];
		int[] priorities1 = new int[n];
		String[] elements2 = new String[n];
		int[] priorities2 = new int[n];
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		final FibonacciHeap<String> pq1 = FibonacciHeap.createMinHeap();
		final SimpleFibonacciHeap<String> pq2 = SimpleFibonacciHeap.createMinHeap();
		int count = 0;
		for (int i = 0; i < 2*n; i+=2) {
			count++;
			elements1[i/2] = "A" + i;
			elements2[i/2] = "A" + (i+1);
			priorities1[i/2] = i;
			priorities2[i/2] = i+1;
			PriorityQueueNode.Integer<String> node1 = new PriorityQueueNode.Integer<String>(elements1[i/2], priorities1[i/2]);
			PriorityQueueNode.Integer<String> node2 = new PriorityQueueNode.Integer<String>(elements2[i/2], priorities2[i/2]);
			list1.add(node1);
			list2.add(node2);
			pq1.offer(node1);
			pq2.offer(node2);
			if (count == 8 || count == 16) {
				// ensure multilevel structure
				pq1.offer("XYZ", -100);
				pq2.offer("XYZ", -100);
				pq1.poll();
				pq2.poll();
			}
		}
		assertFalse(pq1.merge(SimpleFibonacciHeap.createMinHeap()));
		assertTrue(pq1.merge(pq2));
		assertTrue(pq2.isEmpty());
		assertEquals(0, pq2.size());
		assertEquals(2*n, pq1.size());
		for (int i = 0; i < n; i++) {
			assertTrue(pq1.contains(elements1[i]));
			assertTrue(pq1.contains(elements2[i]));
			assertEquals(priorities1[i], pq1.peekPriority(elements1[i]));
			assertEquals(priorities2[i], pq1.peekPriority(elements2[i]));
		}
		for (int i = 0; i < n; i++) {
			assertEquals(list1.get(i), pq1.poll());
			assertEquals(list2.get(i), pq1.poll());
		}
		assertTrue(pq1.isEmpty());
		assertEquals(0, pq1.size());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq1.merge(SimpleFibonacciHeap.createMaxHeap())
		);
	}
	
	@Test
	public void testMergeOtherBetterMin() {
		int n = 24;
		String[] elements1 = new String[n];
		int[] priorities1 = new int[n];
		String[] elements2 = new String[n];
		int[] priorities2 = new int[n];
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		final FibonacciHeap<String> pq1 = FibonacciHeap.createMinHeap();
		final FibonacciHeap<String> pq2 = FibonacciHeap.createMinHeap();
		int count = 0;
		for (int i = 0; i < 2*n; i+=2) {
			count++;
			elements1[i/2] = "A" + i;
			elements2[i/2] = "A" + (i+1);
			priorities1[i/2] = i;
			priorities2[i/2] = i+1;
			PriorityQueueNode.Integer<String> node1 = new PriorityQueueNode.Integer<String>(elements1[i/2], priorities1[i/2]);
			PriorityQueueNode.Integer<String> node2 = new PriorityQueueNode.Integer<String>(elements2[i/2], priorities2[i/2]);
			list1.add(node1);
			list2.add(node2);
			pq2.offer(node1);
			pq1.offer(node2);
			if (count == 8 || count == 16) {
				// ensure multilevel structure
				pq1.offer("XYZ", -100);
				pq2.offer("XYZ", -100);
				pq1.poll();
				pq2.poll();
			}
		}
		assertFalse(pq1.merge(FibonacciHeap.createMinHeap()));
		assertTrue(pq1.merge(pq2));
		assertTrue(pq2.isEmpty());
		assertEquals(0, pq2.size());
		assertEquals(2*n, pq1.size());
		for (int i = 0; i < n; i++) {
			assertTrue(pq1.contains(elements1[i]));
			assertTrue(pq1.contains(elements2[i]));
			assertEquals(priorities1[i], pq1.peekPriority(elements1[i]));
			assertEquals(priorities2[i], pq1.peekPriority(elements2[i]));
		}
		for (int i = 0; i < n; i++) {
			assertEquals(list1.get(i), pq1.poll());
			assertEquals(list2.get(i), pq1.poll());
		}
		assertTrue(pq1.isEmpty());
		assertEquals(0, pq1.size());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq1.merge(FibonacciHeap.createMaxHeap())
		);
	}
	
	@Test
	public void testMergeOtherBetterMinWithSimple() {
		int n = 24;
		String[] elements1 = new String[n];
		int[] priorities1 = new int[n];
		String[] elements2 = new String[n];
		int[] priorities2 = new int[n];
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		final FibonacciHeap<String> pq1 = FibonacciHeap.createMinHeap();
		final SimpleFibonacciHeap<String> pq2 = SimpleFibonacciHeap.createMinHeap();
		int count = 0;
		for (int i = 0; i < 2*n; i+=2) {
			count++;
			elements1[i/2] = "A" + i;
			elements2[i/2] = "A" + (i+1);
			priorities1[i/2] = i;
			priorities2[i/2] = i+1;
			PriorityQueueNode.Integer<String> node1 = new PriorityQueueNode.Integer<String>(elements1[i/2], priorities1[i/2]);
			PriorityQueueNode.Integer<String> node2 = new PriorityQueueNode.Integer<String>(elements2[i/2], priorities2[i/2]);
			list1.add(node1);
			list2.add(node2);
			pq2.offer(node1);
			pq1.offer(node2);
			if (count == 8 || count == 16) {
				// ensure multilevel structure
				pq1.offer("XYZ", -100);
				pq2.offer("XYZ", -100);
				pq1.poll();
				pq2.poll();
			}
		}
		assertFalse(pq1.merge(SimpleFibonacciHeap.createMinHeap()));
		assertTrue(pq1.merge(pq2));
		assertTrue(pq2.isEmpty());
		assertEquals(0, pq2.size());
		assertEquals(2*n, pq1.size());
		for (int i = 0; i < n; i++) {
			assertTrue(pq1.contains(elements1[i]));
			assertTrue(pq1.contains(elements2[i]));
			assertEquals(priorities1[i], pq1.peekPriority(elements1[i]));
			assertEquals(priorities2[i], pq1.peekPriority(elements2[i]));
		}
		for (int i = 0; i < n; i++) {
			assertEquals(list1.get(i), pq1.poll());
			assertEquals(list2.get(i), pq1.poll());
		}
		assertTrue(pq1.isEmpty());
		assertEquals(0, pq1.size());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq1.merge(SimpleFibonacciHeap.createMaxHeap())
		);
	}
	
	@Test
	public void testExceptionForAddingDuplicate() {
		int n = 7;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2*i+2;
		}
		FibonacciHeap<String> pq = FibonacciHeap.createMinHeap();
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq.add(elements[n/2], 3)
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq.pollThenAdd(elements[n/2], 3)
		);
	}
	
	@Test
	public void testExceptionForAddingDuplicatePair() {
		int n = 7;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2*i+2;
		}
		FibonacciHeap<String> pq = FibonacciHeap.createMinHeap();
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq.add(new PriorityQueueNode.Integer<String>(elements[n/2], 3))
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq.pollThenAdd(new PriorityQueueNode.Integer<String>(elements[n/2], 3))
		);
	}
}
