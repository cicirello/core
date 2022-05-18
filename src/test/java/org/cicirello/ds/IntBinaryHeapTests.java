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

import java.util.concurrent.ThreadLocalRandom;

/**
 * JUnit tests for the IntBinaryHeap class.
 */
public class IntBinaryHeapTests {
	
	// MIN HEAP TESTS
	
	@Test
	public void testCreateMinHeap() {
		for (int n = 1; n <= 4; n++) {
			IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
			assertEquals(0, pq.size());
			assertTrue(pq.isEmpty());
			assertEquals(n, pq.domain());
			for (int i = 0; i < n; i++) {
				assertFalse(pq.contains(i));
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> IntBinaryHeap.createMinHeap(0)
		);
	}
	
	@Test
	public void testClearMinHeap() {
		IntBinaryHeap pq = IntBinaryHeap.createMinHeap(10);
		for (int i = 0; i < 5; i++) {
			pq.offer(i, 42*i);
		}
		assertEquals(5, pq.size());
		pq.clear();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertEquals(10, pq.domain());
		pq.clear();
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertEquals(10, pq.domain());
	}
	
	@Test
	public void testIncreasingPriorityMinHeap() {
		int n = 31;
		int[] e = createElements(n);
		int[] p = orderedArray(n);
		IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertEquals(n, pq.domain());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(e[i], p[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(e[0], pq.peek());
			assertEquals(p[0], pq.peekPriority());
			assertEquals(p[i], pq.peekPriority(e[i]));
			assertTrue(pq.contains(e[i]));
			assertFalse(pq.offer(e[i], p[i]));
		}
		for (int i = 0; i < n; i++) {
			assertFalse(pq.isEmpty());
			assertEquals(e[i], pq.poll(), "p[i],e[i]="+p[i]+","+e[i]);
			assertEquals(n-1-i, pq.size());
			assertFalse(pq.contains(e[i]));
		}
		assertTrue(pq.isEmpty());
	}
	
	@Test
	public void testDecreasingPriorityMinHeap() {
		int n = 31;
		int[] e = createElements(n);
		int[] p = reversedArray(n);
		IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertEquals(n, pq.domain());
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(e[i], p[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			assertEquals(e[i], pq.peek());
			assertEquals(p[i], pq.peekPriority());
			assertEquals(p[i], pq.peekPriority(e[i]));
			assertTrue(pq.contains(e[i]));
			assertFalse(pq.offer(e[i], p[i]));
		}
		for (int i = 0; i < n; i++) {
			assertFalse(pq.isEmpty());
			assertEquals(e[n-1-i], pq.poll(), "p[i],e[i]="+p[i]+","+e[i]);
			assertEquals(n-1-i, pq.size());
			assertFalse(pq.contains(e[n-1-i]));
		}
		assertTrue(pq.isEmpty());
	}
	
	@Test
	public void testRandomPriorityMinHeap() {
		int n = 31;
		int[] e = createElements(n);
		int[] p = shuffle(orderedArray(n));
		IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertEquals(n, pq.domain());
		int minP = Integer.MAX_VALUE;
		int minE = -1;
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(e[i], p[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			if (p[i] < minP) {
				minP = p[i];
				minE = e[i];
			}
			assertEquals(minE, pq.peek());
			assertEquals(minP, pq.peekPriority());
			assertEquals(p[i], pq.peekPriority(e[i]));
			assertTrue(pq.contains(e[i]));
			assertFalse(pq.offer(e[i], p[i]));
		}
		int lastP = -1000;
		int[] expectedP = new int[n];
		for (int i = 0; i < n; i++) {
			expectedP[e[i]] = p[i];
		}
		for (int i = 0; i < n; i++) {
			assertFalse(pq.isEmpty());
			int nextP = pq.peekPriority();
			assertTrue(nextP >= lastP);
			assertEquals(nextP, expectedP[pq.poll()], "p[i],e[i]="+p[i]+","+e[i]);
			lastP = nextP;
			assertEquals(n-1-i, pq.size());
		}
		assertTrue(pq.isEmpty());
	}
	
	@Test
	public void testChangePriorityMinHeap() {
		int n = 15;
		int[] e = createElements(n);
		int[] p = orderedArray(n);
		for (int i = 0; i < n; i++) {
			p[i] = 2*p[i] + 2;
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			pq.change(e[i], 1);
			assertEquals(1, pq.peekPriority(e[i]));
			assertEquals(e[i], pq.poll());
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(e[j], pq.poll());
				}
			}
			assertTrue(pq.isEmpty());
		}
		// to back tests
		for (int i = 0; i < n; i++) {
			IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			pq.change(e[i], 100);
			assertEquals(100, pq.peekPriority(e[i]));
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(e[j], pq.poll());
				}
			}
			assertEquals(e[i], pq.poll());
			assertTrue(pq.isEmpty());
		}
		// to interior tests
		int maxP = 2*(n-1) + 2;
		for (int pNew = 3; pNew <= maxP; pNew += 2) {
			for (int i = 0; i < n; i++) {
				IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
				for (int j = 0; j < n; j++) {
					pq.offer(e[j], p[j]);
				}
				pq.change(e[i], pNew);
				assertEquals(pNew, pq.peekPriority(e[i]));
				int j = 0;
				for (; j < n; j++) {
					if (i != j) {
						if (p[j] < pNew) {
							assertEquals(e[j], pq.poll(), "p,i,j="+pNew+","+i+","+j);
						} else {
							break;
						}
					}
				}
				assertEquals(e[i], pq.poll(), "p,i,j="+pNew+","+i+","+j);
				for (; j < n; j++) {
					if (i!=j && p[j] > pNew) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// equal change test
		for (int i = 0; i < n; i++) {
			IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			pq.change(e[i], p[i]);
			assertEquals(p[i], pq.peekPriority(e[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(e[j], pq.poll());
			}
			assertTrue(pq.isEmpty());
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int pNew = 1; pNew <= maxP; pNew += 2) {
			IntBinaryHeap pq = IntBinaryHeap.createMinHeap(n);
			for (int j = 0; j < n-1; j++) {
				pq.offer(e[j], p[j]);
			}
			pq.change(e[n-1], pNew);
			assertEquals(pNew, pq.peekPriority(e[n-1]));
			int j = 0;
			for (; j < n-1; j++) {
				if (p[j] < pNew) {
					assertEquals(e[j], pq.poll());
				} else {
					break;
				}
			}
			assertEquals(e[n-1], pq.poll());
			for (; j < n-1; j++) {
				if (p[j] > pNew) {
					assertEquals(e[j], pq.poll());
				}
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	private int[] createElements(int n) {
		return shuffle(orderedArray(n));
	}
	
	private int[] orderedArray(int n) {
		int[] e = new int[n];
		for (int i = 0; i < n; i++) {
			e[i] = i;
		}
		return e;
	}
	
	private int[] reversedArray(int n) {
		int[] e = orderedArray(n);
		for (int i = 0, j = n-1; i < j; i++, j--) {
			int temp = e[i];
			e[i] = e[j];
			e[j] = temp;
		}
		return e;
	}
	
	private int[] shuffle(int[] e) {
		for (int i = e.length - 1; i > 0; i--) {
			int j = ThreadLocalRandom.current().nextInt(i+1);
			if (i != j) {
				int temp = e[i];
				e[i] = e[j];
				e[j] = temp;
			}
		}
		return e;
	}
}
