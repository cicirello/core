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

/**
 * Test case functionality shared by the various heap classes, binary heaps, fibonacci heaps,
 * specifically for the min heap cases.
 */
public abstract class SharedTestHelpersMinHeaps {
	
	private final Supplier<PriorityQueue<String>> factory;
	
	SharedTestHelpersMinHeaps(Supplier<PriorityQueue<String>> factory) {
		this.factory = factory;
	}
	
	final void elementPollThenAddMinHeap() {
		int n = 7;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		PriorityQueue<String> pq = factory.get();
		populate(pq, elements, priorities, n);
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
	
	final void pollThenAddMinHeap() {
		int n = 7;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		PriorityQueue<String> pq = factory.get();
		populate(pq, elements, priorities, n);
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
	
	final void removeViaElementMinHeap() {
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
	
	final void removeViaPairMinHeap() {
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
	
	final void removeOneLeftMinHeap() {
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
	
	final void removeOneLeftViaPairMinHeap() {
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
	
	final void removeSamePriorityMinHeap() {
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
	
	final void removeSamePriorityViaPairMinHeap() {
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
	
	final void removePercolationMinHeap() {
		int[] p = {0, 3, 1, 7, 4, 5, 2};
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
	
	final void removePercolationViaPairMinHeap() {
		int[] p = {0, 3, 1, 7, 4, 5, 2};
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
	
	final void changeToFrontMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to front tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void changeToBackMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to back tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void changeToInteriorMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to interior tests
		int maxP = 2*(n-1) + 2;
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				PriorityQueue<String> pq = factory.get();
				populate(pq, elements, priorities, n);
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
	}
	
	final void changeEqualMinHeap() {
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
	
	final void changeNewElementMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// new element test
		int maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	
	final void promoteDemoteToFrontMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to front tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void promoteDemoteToBackMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to back tests
		for (int i = 0; i < n; i++) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	}
	
	final void promoteDemoteToInteriorMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// to interior tests
		int maxP = 2*(n-1) + 2;
		for (int p = 3; p <= maxP; p += 2) {
			for (int i = 0; i < n; i++) {
				PriorityQueue<String> pq = factory.get();
				populate(pq, elements, priorities, n);
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
	}
	
	final void promoteDemoteEqualMinHeap() {
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
	
	final void promoteDemoteNewElementMinHeap() {
		int n = 15;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(n);
		// new element test
		int maxP = 2*(n-1) + 3;
		for (int p = 1; p <= maxP; p += 2) {
			PriorityQueue<String> pq = factory.get();
			populate(pq, elements, priorities, n);
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
	
	private void populate(PriorityQueue<String> pq, String[] elements, int[] priorities, int n) {
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
	}
	
	private String[] createStrings(int n) {
		String[] s = new String[n];
		for (int i = 0; i < n; i++) {
			s[i] = ((char)('A'+i)) + "";
		}
		return s;
	}
	
	private int[] createPriorities(int n) {
		int[] priorities = new int[n];
		for (int i = 0; i < n; i++) {
			priorities[i] = 2 + 2*i;
		}
		return priorities;
	}
}
