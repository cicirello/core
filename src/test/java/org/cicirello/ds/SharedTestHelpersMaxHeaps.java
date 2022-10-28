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

import java.util.function.Supplier;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.SplittableRandom;

/**
 * Test case functionality shared by the various heap classes, binary heaps, fibonacci heaps,
 * specifically for the max heap cases. Note that the SharedTestHelpersMinHeaps for min heaps
 * includes more extensive tests. This class essentially validates that max heap order is
 * used. Other functionality doesn't need additional testing in both min and max cases.
 */
public abstract class SharedTestHelpersMaxHeaps extends SharedTestHelpersHeaps {
	
	private final Supplier<PriorityQueue<String>> factory;
	
	SharedTestHelpersMaxHeaps(Supplier<PriorityQueue<String>> factory) {
		super(false);
		this.factory = factory;
	}
	
	final void removeViaElementMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// Via element
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void removeViaPairMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// VIA PAIR
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void removeOneLeftMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		PriorityQueue<String> pq = factory.get();
		pq.offer(elements[0], priorities[0]);
		assertTrue(pq.remove(elements[0]));
		assertFalse(pq.contains(elements[0]));
		assertEquals(0, pq.size());
		assertFalse(pq.remove(elements[0]));
		assertEquals(0, pq.size());
	}
	
	final void removeOneLeftViaPairMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		PriorityQueue<String> pq = factory.get();
		pq.offer(elements[0], priorities[0]);
		PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[0], priorities[0]);
		assertTrue(pq.remove(pair));
		assertFalse(pq.contains(pair));
		assertEquals(0, pq.size());
		assertFalse(pq.remove(pair));
		assertEquals(0, pq.size());
	}
	
	final void removeSamePriorityMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
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
	}
	
	final void removeSamePriorityViaPairMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
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
	}
	
	final void removePercolationMaxHeap() {
		int[] p = {0, -3, -1, -7, -4, -5, -2};
		int n = p.length;
		String[] elements = createStrings(n);
		PriorityQueue<String> pq = factory.get();
		populate(pq, elements, p, n);
		assertTrue(pq.remove(elements[3]));
		int[] expectedIndexOrder = {0, 2, 6, 1, 4, 5};
		for (int i = 0; i < expectedIndexOrder.length; i++) {
			assertEquals(elements[expectedIndexOrder[i]], pq.pollElement());
		}
		assertTrue(pq.isEmpty());
		assertEquals(0, pq.size());
	}
	
	final void removePercolationViaPairMaxHeap() {
		int[] p = {0, -3, -1, -7, -4, -5, -2};
		int n = p.length;
		String[] elements = createStrings(n);
		PriorityQueue<String> pq = factory.get();
		populate(pq, elements, p, n);
		PriorityQueueNode.Integer<String> pair = new PriorityQueueNode.Integer<String>(elements[3], p[3]);
		assertTrue(pq.remove(pair));
		int[] expectedIndexOrder = {0, 2, 6, 1, 4, 5};
		for (int i = 0; i < expectedIndexOrder.length; i++) {
			assertEquals(elements[expectedIndexOrder[i]], pq.pollElement());
		}
		assertTrue(pq.isEmpty());
		assertEquals(0, pq.size());
	}
	
	final void changeToFrontMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to front tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void changeToBackMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to back tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void changeToInteriorMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to interior tests
		int maxP = 2*(n-1) + 2;
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				PriorityQueue<String> pq = factory.get();
				populate(pq, elements, priorities, n);
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
	}
	
	final void changeEqualMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// equal change test
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
			assertFalse(pq.change(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	final void changeNewElementMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// new element test
		int maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	
	final void changeNotHigherParentMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// change not higher than parent (in a fib heap)
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
			String maxElement = pq.pollElement();
			if (!maxElement.equals(elements[i])) {
				assertTrue(pq.change(elements[i], priorities[i]+1));
				assertEquals(priorities[i]+1, pq.peekPriority(elements[i]));
			}
			for (int j = 0; j < n; j++) {
				if (!maxElement.equals(elements[j])) {
					assertEquals(elements[j], pq.pollElement());
				}
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	final void changeCascadingCutMaxHeap() {
		// This is primarily a special case for fibonacci heaps, but
		// use for all heap classes.
		int[] priorities = { -1, -5, -2, -11, -4, -13, -17, -15, -8, -31, -40, -37, -14, -100, -50, -70, -30, -45, -60, 0, -33, -99, -16, -97 };
		int[] orderedIndexes = {};
		int n = priorities.length;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					PriorityQueue<String> pq = factory.get();
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
					assertEquals("T", e3);
					assertEquals(n-3, pq.size());
					if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
						assertTrue(pq.change(elements[i], 2));
						assertEquals(2, pq.peekPriority(elements[i]));
						assertEquals(n-3, pq.size());
					}
					if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
						assertTrue(pq.change(elements[k], 1));
						assertEquals(1, pq.peekPriority(elements[k]));
						assertEquals(n-3, pq.size());
					}
					int lastP = 1000;
					int count = 0;
					while (!pq.isEmpty()) {
						PriorityQueueNode.Integer<String> e = pq.poll();
						String msg = "count,e,p,i,k="+count+","+e.element+","+e.value+","+i+","+k;
						assertTrue(e.value <= lastP, msg);
						lastP = e.value;
						int j = Arrays.binarySearch(elements, e.element);
						if (j != i && j != k) {
							assertEquals(priorities[j], e.value);
						} else if (j == i) {
							assertEquals(2, e.value);
						} else if (j == k) {
							assertEquals(1, e.value);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	final void changeMultiLevelMaxHeap() {
		// This is primarily a special case for fibonacci heaps, but
		// use for all heap classes.
		int[] priorities = { -1, -5, -2, -11, -4, -13, -17, -15, -8, -31, -40, -37, -14, -100, -50, -70, -30, -45, -60, 0, -33, -99, -16, -97 };
		int[] orderedIndexes = {};
		int n = priorities.length;
		String[] elements = createStrings(n);
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					PriorityQueue<String> pq = factory.get();
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
					assertEquals("T", e3);
					assertEquals(n-3, pq.size());
					if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
						assertTrue(pq.change(elements[i], -102));
						assertEquals(-102, pq.peekPriority(elements[i]));
						assertEquals(n-3, pq.size());
					}
					if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
						assertTrue(pq.change(elements[k], -95));
						assertEquals(-95, pq.peekPriority(elements[k]));
						assertEquals(n-3, pq.size());
					}
					int lastP = 1000;
					int count = 0;
					while (!pq.isEmpty()) {
						PriorityQueueNode.Integer<String> e = pq.poll();
						String msg = "count,e,p,i,k="+count+","+e.element+","+e.value+","+i+","+k;
						assertTrue(e.value <= lastP, msg);
						lastP = e.value;
						int j = Arrays.binarySearch(elements, e.element);
						if (j != i && j != k) {
							assertEquals(priorities[j], e.value);
						} else if (j == i) {
							assertEquals(-102, e.value);
						} else if (j == k) {
							assertEquals(-95, e.value);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	final void promoteDemoteToFrontMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to front tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void promoteDemoteToBackMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to back tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void promoteDemoteToInteriorMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to interior tests
		int maxP = 2*(n-1) + 2;
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				PriorityQueue<String> pq = factory.get();
				populate(pq, elements, priorities, n);
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
	}
	
	final void promoteDemoteEqualMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// equal change test
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
			assertFalse(pq.promote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			assertFalse(pq.demote(elements[i], priorities[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(elements[j], pq.pollElement());
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	final void promoteDemoteNewElementMaxHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// new element test
		int maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	
	final void defaultMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		PriorityQueue<String> pq = factory.get();
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
			assertFalse(pq.offer(pairs[i]));
			assertFalse(pq.offer(pairs[i].element, pairs[i].value));
			assertEquals(n, pq.size());
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
			assertFalse(pq.contains(pairs[i].element));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	final void defaultReverseMaxHeap() {
		int n = 31;
		String[] elements = createStringsRev(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		PriorityQueue<String> pq = factory.get();
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
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
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
			assertFalse(pq.contains(elements[n-1-i]));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	final void defaultArbitraryMaxHeap() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		PriorityQueue<String> pq = factory.get();
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
			assertFalse(pq.offer(pairs[i]));
			assertEquals(n, pq.size());
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
			assertFalse(pq.contains(expected));
			assertEquals(n-1-i, pq.size());
		}
		assertNull(pq.poll());
	}
	
	final void defaultDuplicatesAllowedMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		PriorityQueue<String> pq = factory.get();
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
	
	final void defaultReverseDuplicatesAllowedMaxHeap() {
		int n = 31;
		String[] elements = createStringsRev(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		PriorityQueue<String> pq = factory.get();
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
	
	final void defaultArbitraryDuplicatesAllowedMaxHeap() {
		int n = 31;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		PriorityQueue<String> pq = factory.get();
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
	
	final void addSimpleMaxHeap() {
		int n = 31;
		String[] elements = createStringsMaxCase(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		PriorityQueue<String> pq = factory.get();
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
}
