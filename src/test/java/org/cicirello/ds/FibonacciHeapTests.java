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
		super(FibonacciHeap::createMinHeap, FibonacciHeap::createMaxHeap, FibonacciHeap::createMinHeap, FibonacciHeap::createMaxHeap);
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
		clear();
	}
	
	@Test
	public void testCopy() {
		copy();
	}
	
	@Test
	public void testCopyEmptyHeap() {
		copyEmptyHeap();
	}
	
	@Test
	public void testAddAll() {
		addAll();
	}
	
	@Test
	public void testEqualsAndHashCode() {
		equalsAndHashCode(SimpleFibonacciHeap::createMinHeap);
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
