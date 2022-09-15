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
public class SimpleBinaryHeapTests {
	
	// TESTS THAT ARE NEITHER STRICTLY MIN HEAP TESTS NOW MAX HEAP TESTS
	
	@Test
	public void testContainsAll() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements.length; i++) {
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
		}
		for (int i = 0; i < elements.length; i++) {
			assertFalse(pq.containsAll(list));
			assertTrue(pq.add(elements[i], priorities[i]));
		}
		assertTrue(pq.containsAll(list));
	}
	
	@Test
	public void testContainsAllElements() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < elements.length; i++) {
			list.add(elements[i]);
		}
		for (int i = 0; i < elements.length; i++) {
			assertFalse(pq.containsAll(list));
			assertTrue(pq.add(elements[i], priorities[i]));
		}
		assertTrue(pq.containsAll(list));
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
	public void testAddAll() {
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
	public void testRetainAll() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		final SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
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
		keepThese.add(new PriorityQueueNode.Integer<String>(retain[2], 5));
		keepThese.add(new PriorityQueueNode.Integer<String>(retain[3], 15));
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
		int[] priorities = { 8, 6, 4, 2 };
		final SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements.length; i++) {
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
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
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
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
		int[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			int count = 0;
			for (PriorityQueueNode.Integer<String> e : pq) {
				count++;
			}
			assertEquals(m, count);
			count = 0;
			final Iterator<PriorityQueueNode.Integer<String>> iter = pq.iterator();
			while (iter.hasNext()) {
				PriorityQueueNode.Integer<String> e = iter.next();
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
		int[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			Object[] array = pq.toArray();
			assertEquals(m, array.length);
			int j = 0;
			for (PriorityQueueNode.Integer<String> e : pq) {
				assertEquals(e, (PriorityQueueNode.Integer)array[j]);
				j++;
			}
		}
	}
	
	@Test
	public void testToArrayExistingArray() {
		int n = 4;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		for (int m = 0; m <= n; m++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			PriorityQueueNode.Integer[] a1 = new PriorityQueueNode.Integer[n];
			PriorityQueueNode.Integer[] a2 = pq.toArray(a1);
			assertTrue(a1 == a2);
			int j = 0;
			for (PriorityQueueNode.Integer<String> e : pq) {
				assertEquals(e, a2[j]);
				j++;
			}
			assertEquals(m, j);
			if (m<n) {
				assertNull(a2[j]);
			}
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
		PriorityQueueNode.Integer[] a1 = new PriorityQueueNode.Integer[n-1];
		PriorityQueueNode.Integer[] a2 = pq.toArray(a1);
		assertTrue(a1 != a2);
		assertEquals(n, a2.length);
		int j = 0;
		for (PriorityQueueNode.Integer<String> e : pq) {
			assertEquals(e, a2[j]);
			j++;
		}
		assertEquals(n, j);
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
	public void testClear() {
		int n = 11;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(list);
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
		SimpleBinaryHeap<String> pq1 = SimpleBinaryHeap.createMinHeap(list1);
		SimpleBinaryHeap<String> pq2 = SimpleBinaryHeap.createMaxHeap(list2);
		SimpleBinaryHeap<String> pq3 = SimpleBinaryHeap.createMinHeap(list3);
		SimpleBinaryHeap<String> pq4 = SimpleBinaryHeap.createMaxHeap(list4);
		SimpleBinaryHeap<String> copy1 = pq1.copy();
		SimpleBinaryHeap<String> copy2 = pq2.copy();
		SimpleBinaryHeap<String> copy3 = pq3.copy();
		SimpleBinaryHeap<String> copy4 = pq4.copy();
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
		SimpleBinaryHeap<String> pq1 = SimpleBinaryHeap.createMinHeap(list1);
		SimpleBinaryHeap<String> pq2 = SimpleBinaryHeap.createMinHeap(list2);
		SimpleBinaryHeap<String> pq3 = SimpleBinaryHeap.createMaxHeap(list3);
		SimpleBinaryHeap<String> pq4 = SimpleBinaryHeap.createMaxHeap(list4);
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
	public void testElementPollThenAddMinHeap() {
		int n = 7;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2*i+2;
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
		// At front
		String s = pq.pollThenAdd("ZZZ", 1);
		assertEquals(n, pq.size());
		assertTrue(pq.contains("ZZZ"));
		assertEquals(1, pq.peekPriority("ZZZ"));
		assertEquals(elements[0], s);
		assertEquals("ZZZ", pq.pollElement());
		s = pq.pollThenAdd("YYY", 7);
		assertEquals(n-1, pq.size());
		assertTrue(pq.contains("YYY"));
		assertEquals(7, pq.peekPriority("YYY"));
		assertEquals(elements[1], s);
		assertEquals(elements[2], pq.pollElement());
		assertEquals("YYY", pq.pollElement());
		for (int i = 3; i < n; i++) {
			assertEquals(elements[i], pq.pollElement());
		}
		assertEquals(0, pq.size());
		s = pq.pollThenAdd("XXX", 9);
		assertNull(s);
		assertEquals(1, pq.size());
		assertTrue(pq.contains("XXX"));
		assertEquals(9, pq.peekPriority("XXX"));
		assertEquals(9, pq.peekPriority());
		assertEquals("XXX", pq.peekElement());
		s = pq.pollThenAdd("XXX", 3);
		assertEquals("XXX", s);
		assertEquals(1, pq.size());
		assertTrue(pq.contains("XXX"));
		assertEquals(3, pq.peekPriority("XXX"));
		assertEquals(3, pq.peekPriority());
		assertEquals("XXX", pq.peekElement());
		pq.offer("QQQ", 1);
	}
	
	@Test
	public void testPollThenAddMinHeap() {
		int n = 7;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2*i+2;
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
		// At front
		String s = pq.pollThenAdd(new PriorityQueueNode.Integer<String>("ZZZ", 1)).getElement();
		assertEquals(n, pq.size());
		assertTrue(pq.contains("ZZZ"));
		assertEquals(1, pq.peekPriority("ZZZ"));
		assertEquals(elements[0], s);
		assertEquals("ZZZ", pq.pollElement());
		s = pq.pollThenAdd(new PriorityQueueNode.Integer<String>("YYY", 7)).getElement();
		assertEquals(n-1, pq.size());
		assertTrue(pq.contains("YYY"));
		assertEquals(7, pq.peekPriority("YYY"));
		assertEquals(elements[1], s);
		assertEquals(elements[2], pq.pollElement());
		assertEquals("YYY", pq.pollElement());
		for (int i = 3; i < n; i++) {
			assertEquals(elements[i], pq.pollElement());
		}
		assertEquals(0, pq.size());
		assertNull(pq.pollThenAdd(new PriorityQueueNode.Integer<String>("XXX", 9)));
		assertEquals(1, pq.size());
		assertTrue(pq.contains("XXX"));
		assertEquals(9, pq.peekPriority("XXX"));
		assertEquals(9, pq.peekPriority());
		assertEquals("XXX", pq.peekElement());
		s = pq.pollThenAdd(new PriorityQueueNode.Integer<String>("XXX", 3)).getElement();
		assertEquals("XXX", s);
		assertEquals(1, pq.size());
		assertTrue(pq.contains("XXX"));
		assertEquals(3, pq.peekPriority("XXX"));
		assertEquals(3, pq.peekPriority());
		assertEquals("XXX", pq.peekElement());
		pq.offer("QQQ", 1);
	}
	
	@Test
	public void testRemoveMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = (2 + 2*i);
		}
		// Via element
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			pq.offer(elements[0], priorities[0]);
			assertTrue(pq.remove(elements[0]));
			assertFalse(pq.contains(elements[0]));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(elements[0]));
			assertEquals(0, pq.size());
		}
		// Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			int[] p = {0, 3, 1, 7, 4, 5, 2};
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[i], priorities[i]);
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			pq.offer(elements[0], priorities[0]);
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[0], priorities[0]);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(0, pq.size());
		}
		// Via pair: Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], 42);
			}
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[i], 42);
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			int[] p = {0, 3, 1, 7, 4, 5, 2};
			for (int i = 0; i < p.length; i++) {
				pq.offer(elements[i], p[i]);
			}
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[3], p[3]);
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
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2 + 2*i;
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], 1));
			assertEquals(1, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], 100));
			assertEquals(100, pq.peekPriority(elements[i]));
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
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				assertTrue(pq.change(elements[i], p));
				assertEquals(p, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.change(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change("YYY", p));
			assertEquals(p, pq.peekPriority("YYY"));
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
	public void testPromoteDemoteMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2 + 2*i;
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.promote(elements[i], 1));
			assertEquals(1, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.demote(elements[i], 100));
			assertEquals(100, pq.peekPriority(elements[i]));
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
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				if (p < priorities[i]) {
					assertTrue(pq.promote(elements[i], p));
				} else {
					assertTrue(pq.demote(elements[i], p));
				}
				assertEquals(p, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.promote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			assertFalse(pq.demote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
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
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testMinHeapAdd() {
		int n = 31;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.add(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.add(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertTrue(pq.contains(elements[n-1-i]));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'+i)), pq.poll());
			assertTrue(pq.contains(expected));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'+i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> SimpleBinaryHeap.createMinHeap(new ArrayList<PriorityQueueNode.Integer<String>>())
		);
	}
	
	@Test
	public void testListMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertTrue(pq.contains(elements[n-1-i]));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'+i)), pq.poll());
			assertTrue(pq.contains(expected));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'+i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(elements[i], pq.pollElement());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.pollElement());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> SimpleBinaryHeap.createMinHeap(0)
		);
	}
	
	@Test
	public void testMinHeapAdd2() {
		int n = 31;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.add(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.add(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(elements[i], pq.pollElement());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.pollElement());
	}
	
	@Test
	public void testMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[n-1-i], pq.pollElement());
			assertTrue(pq.contains(elements[n-1-i]));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(elements[n-1-i], pq.pollElement());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.pollElement());
	}
	
	@Test
	public void testMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(expected, pq.pollElement());
			assertTrue(pq.contains(expected));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(expected, pq.pollElement());
			assertFalse(pq.contains(expected));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.pollElement());
	}
	
	
	// MAX HEAP TESTS
	
	@Test
	public void testRemoveMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = -(2 + 2*i);
		}
		// Via element
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			pq.offer(elements[0], priorities[0]);
			assertTrue(pq.remove(elements[0]));
			assertFalse(pq.contains(elements[0]));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(elements[0]));
			assertEquals(0, pq.size());
		}
		// Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			int[] p = {0, -3, -1, -7, -4, -5, -2};
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[i], priorities[i]);
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			pq.offer(elements[0], priorities[0]);
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[0], priorities[0]);
			assertTrue(pq.remove(pair));
			assertFalse(pq.contains(pair));
			assertEquals(0, pq.size());
			assertFalse(pq.remove(pair));
			assertEquals(0, pq.size());
		}
		// Via pair: Same priorities: no percolation needed
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], 42);
			}
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[i], 42);
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			int[] p = {0, -3, -1, -7, -4, -5, -2};
			for (int i = 0; i < p.length; i++) {
				pq.offer(elements[i], p[i]);
			}
			PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[3], p[3]);
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
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = -(2 + 2*i);
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], -1));
			assertEquals(-1, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change(elements[i], -100));
			assertEquals(-100, pq.peekPriority(elements[i]));
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
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				assertTrue(pq.change(elements[i], -p));
				assertEquals(-p, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.change(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.change("YYY", -p));
			assertEquals(-p, pq.peekPriority("YYY"));
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
	public void testPromoteDemoteMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = -(2 + 2*i);
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.promote(elements[i], -1));
			assertEquals(-1, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertTrue(pq.demote(elements[i], -100));
			assertEquals(-100, pq.peekPriority(elements[i]));
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
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
				for (int j = 0; j < n; j++) {
					pq.offer(elements[j], priorities[j]);
				}
				if (-p > priorities[i]) {
					assertTrue(pq.promote(elements[i], -p));
				} else {
					assertTrue(pq.demote(elements[i], -p));
				}
				assertEquals(-p, pq.peekPriority(elements[i]));
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
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
			for (int j = 0; j < n; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			assertFalse(pq.promote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			assertFalse(pq.demote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
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
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*n-2-2*i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testMaxHeapAdd() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.add(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.add(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*n-2-2*i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertTrue(pq.contains(elements[n-1-i]));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testDefaultMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'-i)), pq.poll());
			assertTrue(pq.contains(expected));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'-i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.poll());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> SimpleBinaryHeap.createMaxHeap(new ArrayList<PriorityQueueNode.Integer<String>>())
		);
	}
	
	@Test
	public void testListMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.poll());
			assertTrue(pq.contains(elements[n-1-i]));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(pairs[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testListMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(pairs[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'-i)), pq.poll());
			assertTrue(pq.contains(expected));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'-i)), pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(elements[i], pq.pollElement());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.pollElement());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> SimpleBinaryHeap.createMaxHeap(0)
		);
	}
	
	@Test
	public void testMaxHeapAdd2() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.add(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.add(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[0], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[i], pq.pollElement());
			assertTrue(pq.contains(pairs[i].element));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(elements[i], pq.pollElement());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.pollElement());
	}
	
	@Test
	public void testMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peekElement());
			assertEquals(pairs[i], pq.peek());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(pairs[n-1], pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[n-1-i], pq.pollElement());
			assertTrue(pq.contains(elements[n-1-i]));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(elements[n-1-i], pq.pollElement());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(2*(n-1-i), pq.size());
		}
		assertNull(pq.pollElement());
	}
	
	@Test
	public void testMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		SimpleBinaryHeap<String> pq = SimpleBinaryHeap.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peekElement());
		assertNull(pq.peek());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertTrue(pq.contains(pairs[i]));
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(n+i+1, pq.size());
			assertEquals("A", pq.peekElement());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peek());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(expected, pq.pollElement());
			assertTrue(pq.contains(expected));
			assertEquals(2*n-1-2*i, pq.size());
			assertEquals(expected, pq.pollElement());
			assertFalse(pq.contains(expected));
			assertEquals(2*(n-1-i), pq.size());
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
	
	private int[] createPriorities(String[] elements) {
		int[] p = new int[elements.length];
		for (int i = 0; i < elements.length; i++) {
			p[i] = (int)elements[i].charAt(0);
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
	
	private PriorityQueueNode.Integer<String>[] createPairs(String[] elements, int[] priorities) {
		@SuppressWarnings("unchecked")
		PriorityQueueNode.Integer<String>[] pairs = (PriorityQueueNode.Integer<String>[])new PriorityQueueNode.Integer[elements.length];
		for (int i = 0; i < pairs.length; i++) {
			pairs[i] = new PriorityQueueNode.Integer<String>(elements[i], priorities[i]);
		}
		return pairs;
	}
}
