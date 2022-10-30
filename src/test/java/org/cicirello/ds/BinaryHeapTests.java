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
 * JUnit tests for the BinaryHeap class.
 */
public class BinaryHeapTests extends SharedTestCommonHelpersHeaps {
	
	public BinaryHeapTests() {
		super(BinaryHeap::createMinHeap, BinaryHeap::createMinHeap);
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
		BinaryHeap<String> pq = BinaryHeap.createMinHeap(list);
		assertEquals(n, pq.size());
		pq.clear();
		assertEquals(0, pq.size());
		for (int i = 0; i < n; i++) {
			assertFalse(pq.contains(pairs[i].element));
		}
	}
	
	@Test
	public void testCopy() {
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
		}
		for (int i = 0; i < n; i++) {
			list3.add(new PriorityQueueNode.Integer<String>(elements[i], 42));
			list4.add(new PriorityQueueNode.Integer<String>(elements[i], 42));
		}
		BinaryHeap<String> pq1 = BinaryHeap.createMinHeap(list1);
		BinaryHeap<String> pq2 = BinaryHeap.createMaxHeap(list2);
		BinaryHeap<String> pq3 = BinaryHeap.createMinHeap(list3);
		BinaryHeap<String> pq4 = BinaryHeap.createMaxHeap(list4);
		BinaryHeap<String> copy1 = pq1.copy();
		BinaryHeap<String> copy2 = pq2.copy();
		BinaryHeap<String> copy3 = pq3.copy();
		BinaryHeap<String> copy4 = pq4.copy();
		assertEquals(pq1, copy1);
		assertEquals(pq2, copy2);
		assertEquals(pq3, copy3);
		assertEquals(pq4, copy4);
		assertTrue(pq1 != copy1);
		assertTrue(pq2 != copy2);
		assertTrue(pq3 != copy3);
		assertTrue(pq4 != copy4);
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
		BinaryHeap<String> pq1 = BinaryHeap.createMinHeap(list1);
		BinaryHeap<String> pq2 = BinaryHeap.createMinHeap(list2);
		BinaryHeap<String> pq3 = BinaryHeap.createMaxHeap(list3);
		BinaryHeap<String> pq4 = BinaryHeap.createMaxHeap(list4);
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
		for (int i = 0; i < 2*n; i+=2) {
			elements1[i/2] = "A" + i;
			elements2[i/2] = "A" + (i+1);
			priorities1[i/2] = i;
			priorities2[i/2] = i+1;
			list1.add(new PriorityQueueNode.Integer<String>(elements1[i/2], priorities1[i/2]));
			list2.add(new PriorityQueueNode.Integer<String>(elements2[i/2], priorities2[i/2]));
		}
		final BinaryHeap<String> pq1 = BinaryHeap.createMinHeap(list1);
		final BinaryHeap<String> pq2 = BinaryHeap.createMinHeap(list2);
		assertFalse(pq1.merge(BinaryHeap.createMinHeap()));
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
			() -> pq1.merge(BinaryHeap.createMaxHeap())
		);
	}
	
	@Test
	public void testAddAll() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		final int INITIAL_CAPACITY = 6;
		final BinaryHeap<String> pq = BinaryHeap.createMinHeap(INITIAL_CAPACITY);
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
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> pq.addAll(list)
		);
	}
	
	@Test
	public void testCapacity() {
		BinaryHeap<String> pq = BinaryHeap.createMinHeap();
		assertEquals(BinaryHeap.DEFAULT_INITIAL_CAPACITY, pq.capacity());
		assertEquals(0, pq.size());
		for (int i = 1; i <= 5; i++) {
			pq = BinaryHeap.createMinHeap(i);
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
		pq = BinaryHeap.createMinHeap(list);
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
	public void testExceptionForAddingDuplicate() {
		int n = 7;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2*i+2;
		}
		BinaryHeap<String> pq = BinaryHeap.createMinHeap();
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
		BinaryHeap<String> pq = BinaryHeap.createMinHeap();
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
	
	@Test
	public void testExceptionsCapacityZero() {
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMinHeap(0)
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMaxHeap(0)
		);
	}
}
