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
 * JUnit tests for the SimpleBinaryHeap class.
 */
public class SimpleBinaryHeapTests extends SharedTestCommonHelpersHeaps {
	
	public SimpleBinaryHeapTests() {
		super(SimpleBinaryHeap::createMinHeap, SimpleBinaryHeap::createMaxHeap, SimpleBinaryHeap::createMinHeap, SimpleBinaryHeap::createMaxHeap);
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
		equalsAndHashCode(BinaryHeap::createMinHeap);
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
		for (int i = 0; i < 2*n; i+=2) {
			elements1[i/2] = "A" + i;
			elements2[i/2] = "A" + (i+1);
			priorities1[i/2] = i;
			priorities2[i/2] = i+1;
			list1.add(new PriorityQueueNode.Integer<String>(elements1[i/2], priorities1[i/2]));
			list2.add(new PriorityQueueNode.Integer<String>(elements2[i/2], priorities2[i/2]));
		}
		final SimpleBinaryHeap<String> pq1 = SimpleBinaryHeap.createMinHeap(list1);
		final SimpleBinaryHeap<String> pq2 = SimpleBinaryHeap.createMinHeap(list2);
		assertFalse(pq1.merge(SimpleBinaryHeap.createMinHeap()));
		assertTrue(pq1.merge(pq2));
		assertEquals(4*n, pq1.capacity());
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
			() -> pq1.merge(SimpleBinaryHeap.createMaxHeap())
		);
	}
	
	@Test
	public void testAddAllCapacity() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		final int INITIAL_CAPACITY = 6;
		final SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(INITIAL_CAPACITY);
		assertEquals(INITIAL_CAPACITY, pq.capacity());
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements.length; i++) {
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
		}
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		assertEquals(INITIAL_CAPACITY, pq.capacity());
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.contains(elements[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		
		String[] elements2 = {"E", "F", "G", "H", "I"};
		int[] priorities2 = { 7, 3, 1, 5, 9 };
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements2.length; i++) {
			list2.add(new PriorityQueueNode.Integer<String>(elements2[i], priorities2[i]));
		}
		assertTrue(pq.addAll(list2));
		assertEquals(elements.length + elements2.length, pq.size());
		assertEquals((elements.length + elements2.length)*2, pq.capacity());
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.contains(elements[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		for (int i = 0; i < elements2.length; i++) {
			assertTrue(pq.contains(elements2[i]));
			assertEquals(priorities2[i], pq.peekPriority(elements2[i]));
		}
		
		assertFalse(pq.addAll(new ArrayList<PriorityQueueNode.Integer<String>>()));
		assertEquals(elements.length + elements2.length, pq.size());
		assertEquals((elements.length + elements2.length)*2, pq.capacity());
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.contains(elements[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		for (int i = 0; i < elements2.length; i++) {
			assertTrue(pq.contains(elements2[i]));
			assertEquals(priorities2[i], pq.peekPriority(elements2[i]));
		}
	}
	
	@Test
	public void testCapacity() {
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		assertEquals(SimpleBinaryHeap.DEFAULT_INITIAL_CAPACITY, pq.capacity());
		assertEquals(0, pq.size());
		for (int i = 1; i <= 5; i++) {
			pq = SimpleBinaryHeap.createMinHeap(i);
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
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		pq = SimpleBinaryHeap.createMinHeap(list);
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
	public void testExceptionsCapacityZero() {
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> SimpleBinaryHeap.createMinHeap(0)
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> SimpleBinaryHeap.createMaxHeap(0)
		);
	}
}
