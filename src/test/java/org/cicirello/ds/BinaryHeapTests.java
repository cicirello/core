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

/**
 * JUnit tests for the BinaryHeap class.
 */
public class BinaryHeapTests {
	
	// MIN HEAP TESTS
	
	@Test
	public void testDefaultMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.pollPair());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
	}
	
	@Test
	public void testDefaultMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peek());
			assertEquals(pairs[i], pq.peekPair());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[n-1], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.pollPair());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
	}
	
	@Test
	public void testDefaultMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMinHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'+i)), pq.pollPair());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
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
		BinaryHeap<String> pq = BinaryHeap.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.pollPair());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMinHeap(new ArrayList<PriorityQueueNode.Integer<String>>())
		);
		final ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		list2.add(pairs[0]);
		list2.add(pairs[0]);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMinHeap(list2)
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
		BinaryHeap<String> pq = BinaryHeap.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[n-1], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.pollPair());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
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
		BinaryHeap<String> pq = BinaryHeap.createMinHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'+i)), pq.pollPair());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
	}
	
	@Test
	public void testMinHeap() {
		int n = 31;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMinHeap(0)
		);
	}
	
	@Test
	public void testMinHeapReverse() {
		int n = 31;
		String[] elements = createStringsRev(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peek());
			assertEquals(pairs[i], pq.peekPair());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[n-1], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testMinHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMinHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MAX_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'+i));
			assertEquals(expected, pq.poll());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	
	// MAX HEAP TESTS
	
	@Test
	public void testDefaultMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.pollPair());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
	}
	
	@Test
	public void testDefaultMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peek());
			assertEquals(pairs[i], pq.peekPair());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[n-1], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.pollPair());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
	}
	
	@Test
	public void testDefaultMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(pairs[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'-i)), pq.pollPair());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
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
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[i], pq.pollPair());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMaxHeap(new ArrayList<PriorityQueueNode.Integer<String>>())
		);
		final ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		list2.add(pairs[0]);
		list2.add(pairs[0]);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMaxHeap(list2)
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
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[n-1], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(pairs[n-1-i], pq.pollPair());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
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
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap(list);
		assertEquals(n, pq.size());
		assertFalse(pq.isEmpty());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(new PriorityQueueNode.Integer<String>(expected, (int)('A'-i)), pq.pollPair());
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.pollPair());
	}
	
	@Test
	public void testMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[0], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[i], pq.poll());
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
		
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> BinaryHeap.createMaxHeap(0)
		);
	}
	
	@Test
	public void testMaxHeapReverse() {
		int n = 31;
		String[] elements = createStringsRevMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(elements[i], pq.peek());
			assertEquals(pairs[i], pq.peekPair());
			assertEquals((int)elements[i].charAt(0), pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(pairs[n-1], pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			assertEquals(elements[n-1-i], pq.poll());
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	@Test
	public void testMaxHeapArbitrary() {
		int n = 31;
		String[] elements = createStringsArbitraryMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		BinaryHeap<String> pq = BinaryHeap.createMaxHeap(8);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertNull(pq.peek());
		assertNull(pq.peekPair());
		assertEquals(Integer.MIN_VALUE, pq.peekPriority());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
		}
		for (int i = 0; i < n; i++) {
			assertTrue(pq.contains(elements[i]));
			assertFalse(pq.offer(elements[i], priorities[i]));
			assertEquals(n, pq.size());
			assertEquals("A", pq.peek());
			assertEquals(new PriorityQueueNode.Integer<String>("A",(int)'A'), pq.peekPair());
			assertEquals((int)'A', pq.peekPriority());
		}
		for (int i = 0; i < n; i++) {
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		assertEquals(Integer.MIN_VALUE, pq.peekPriority("hello"));
		for (int i = 0; i < n; i++) {
			String expected = ""+((char)('A'-i));
			assertEquals(expected, pq.poll());
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
