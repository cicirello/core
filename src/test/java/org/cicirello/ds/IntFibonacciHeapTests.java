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
import java.util.Arrays;

/**
 * JUnit tests for the IntFibonacciHeap class.
 */
public class IntFibonacciHeapTests {
	
	// Tests not specific to min heap vs max heap
	
	@Test
	public void testCopy() {
		IntFibonacciHeap min0 = IntFibonacciHeap.createMinHeap(10);
		IntFibonacciHeap max0 = IntFibonacciHeap.createMaxHeap(10);
		IntFibonacciHeap min1 = IntFibonacciHeap.createMinHeap(10);
		for (int i = 0; i < 5; i++) {
			min1.offer(i, 42*i);
		}
		IntFibonacciHeap min2 = IntFibonacciHeap.createMinHeap(10);
		for (int i = 0; i < 5; i++) {
			min2.offer(i, 43*i);
		}
		IntFibonacciHeap max1 = IntFibonacciHeap.createMaxHeap(10);
		for (int i = 0; i < 5; i++) {
			max1.offer(i, 42*i);
		}
		IntFibonacciHeap max2 = IntFibonacciHeap.createMaxHeap(10);
		for (int i = 0; i < 5; i++) {
			max2.offer(i, 43*i);
		}
		IntFibonacciHeap minMultiLevel = IntFibonacciHeap.createMinHeap(24);
		IntFibonacciHeap maxMultiLevel = IntFibonacciHeap.createMaxHeap(24);
		int[] p = { 12, 0, 7, 19, 3, 11, 90, 8, 2, -1, 17, 15, 6, 19, 5, 82, 81, 200, -2, -5, 99, 4, 83, 101};
		for (int i = 0; i < 8; i++) {
			minMultiLevel.offer(i, p[i]);
			maxMultiLevel.offer(i, p[i]);
		}
		assertEquals(1, minMultiLevel.poll());
		assertEquals(6, maxMultiLevel.poll());
		for (int i = 8; i < 16; i++) {
			minMultiLevel.offer(i, p[i]);
			maxMultiLevel.offer(i, p[i]);
		}
		assertEquals(9, minMultiLevel.poll());
		assertEquals(15, maxMultiLevel.poll());
		for (int i = 16; i < 24; i++) {
			minMultiLevel.offer(i, p[i]);
			maxMultiLevel.offer(i, p[i]);
		}
		assertEquals(19, minMultiLevel.poll());
		assertEquals(17, maxMultiLevel.poll());
		IntFibonacciHeap minMultiLevelCopy = minMultiLevel.copy();
		IntFibonacciHeap maxMultiLevelCopy = maxMultiLevel.copy();
		double lastMin = -1000;
		double lastMax = 1000;
		for (int i = 0; i < 20; i++) {
			assertEquals(minMultiLevel.peekPriority(), minMultiLevelCopy.peekPriority());
			assertEquals(maxMultiLevel.peekPriority(), maxMultiLevelCopy.peekPriority());
			double minP = minMultiLevel.peekPriority();
			double maxP = maxMultiLevel.peekPriority();
			assertTrue(minP >= lastMin, "i="+i);
			assertTrue(maxP <= lastMax);
			lastMin = minP;
			lastMax = maxP;
			int minE = minMultiLevel.poll();
			int maxE = maxMultiLevel.poll();
			assertEquals(minE, minMultiLevelCopy.poll());
			assertEquals(maxE, maxMultiLevelCopy.poll());
			assertEquals(p[minE], minP, "i="+i);
			assertEquals(p[maxE], maxP);
		}
		
		IntFibonacciHeap minCopy0 = min0.copy();
		IntFibonacciHeap minCopy1 = min1.copy();
		IntFibonacciHeap minCopy2 = min2.copy();
		IntFibonacciHeap maxCopy0 = max0.copy();
		IntFibonacciHeap maxCopy1 = max1.copy();
		IntFibonacciHeap maxCopy2 = max2.copy();
		assertTrue(min0 != minCopy0);
		assertTrue(max0 != maxCopy0);
		assertTrue(min1 != minCopy1);
		assertTrue(min2 != minCopy2);
		assertTrue(max1 != maxCopy1);
		assertTrue(max2 != maxCopy2);
		assertEquals(min1.getClass(), minCopy1.getClass());
		assertEquals(min2.getClass(), minCopy2.getClass());
		assertEquals(max1.getClass(), maxCopy1.getClass());
		assertEquals(max2.getClass(), maxCopy2.getClass());
		assertEquals(min0.getClass(), minCopy0.getClass());
		assertEquals(max0.getClass(), maxCopy0.getClass());
		assertEquals(min1.size(), minCopy1.size());
		assertEquals(min2.size(), minCopy2.size());
		assertEquals(max1.size(), maxCopy1.size());
		assertEquals(max2.size(), maxCopy2.size());
		assertEquals(min0.size(), minCopy0.size());
		assertEquals(max0.size(), maxCopy0.size());
		assertNotEquals(min1.getClass(), maxCopy1.getClass());
		assertNotEquals(min2.getClass(), maxCopy2.getClass());
		assertNotEquals(max1.getClass(), minCopy1.getClass());
		assertNotEquals(max2.getClass(), minCopy2.getClass());
		assertNotEquals(min0.getClass(), maxCopy0.getClass());
		assertNotEquals(max0.getClass(), minCopy0.getClass());
		int n = min1.size();
		for (int i = 0; i < n; i++) {
			assertEquals(min1.peek(), minCopy1.peek());
			assertEquals(min2.peek(), minCopy2.peek());
			assertEquals(max1.peek(), maxCopy1.peek());
			assertEquals(max2.peek(), maxCopy2.peek());
			assertEquals(min1.peekPriority(), minCopy1.peekPriority());
			assertEquals(min2.peekPriority(), minCopy2.peekPriority());
			assertEquals(max1.peekPriority(), maxCopy1.peekPriority());
			assertEquals(max2.peekPriority(), maxCopy2.peekPriority());
			assertEquals(min1.poll(), minCopy1.poll());
			assertEquals(min2.poll(), minCopy2.poll());
			assertEquals(max1.poll(), maxCopy1.poll());
			assertEquals(max2.poll(), maxCopy2.poll());
		}
		assertTrue(min1.isEmpty());
		assertTrue(min2.isEmpty());
		assertTrue(max1.isEmpty());
		assertTrue(max2.isEmpty());
		assertTrue(minCopy1.isEmpty());
		assertTrue(minCopy2.isEmpty());
		assertTrue(maxCopy1.isEmpty());
		assertTrue(maxCopy2.isEmpty());
		assertTrue(min0.isEmpty());
		assertTrue(max0.isEmpty());
		assertTrue(minCopy0.isEmpty());
		assertTrue(maxCopy0.isEmpty());
		assertEquals(0, min0.size());
		assertEquals(0, max0.size());
		assertEquals(0, minCopy0.size());
		assertEquals(0, maxCopy0.size());
		assertEquals(-1, min0.poll());
		assertEquals(-1, max0.poll());
	}
	
	// MIN HEAP TESTS
	
	@Test
	public void testCreateMinHeap() {
		for (int n = 1; n <= 4; n++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			assertEquals(0, pq.size());
			assertTrue(pq.isEmpty());
			assertEquals(n, pq.domain());
			for (int i = 0; i < n; i++) {
				assertFalse(pq.contains(i));
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> IntFibonacciHeap.createMinHeap(0)
		);
	}
	
	@Test
	public void testClearMinHeap() {
		IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(10);
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
		IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
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
		IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
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
		IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
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
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.change(e[i], 1));
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
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.change(e[i], 100));
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
				IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
				for (int j = 0; j < n; j++) {
					pq.offer(e[j], p[j]);
				}
				assertTrue(pq.change(e[i], pNew));
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
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertFalse(pq.change(e[i], p[i]));
			assertEquals(p[i], pq.peekPriority(e[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(e[j], pq.poll());
			}
			assertTrue(pq.isEmpty());
		}
		// same relative order to next best
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.change(e[i], p[i]-1));
				assertEquals(p[i]-1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// same relative order to next worst
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.change(e[i], p[i]+1));
				assertEquals(p[i]+1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int pNew = 1; pNew <= maxP; pNew += 2) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n-1; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.change(e[n-1], pNew));
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
	
	@Test
	public void testMultilevelPromoteDemoteMinHeap() {
		int[] priorities = { 1, 5, 2, 11, 4, 13, 17, 15, 8, 31, 40, 37, 14, 100, 50, 70, 30, 45, 60, 0, 33, 99, 16, 97};
		int n = priorities.length;
		int[] elements = new int[n];
		for (int i = 0; i < n; i++) {
			elements[i] = i;
		}
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
					for (int j = 0; j < n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					int e1 = pq.poll();
					int index = Arrays.binarySearch(elements, e1);
					if (index == i || index == k) {
						continue;
					}
					assertEquals(0, e1);
					assertEquals(n/3-1, pq.size());
					for (int j = n/3; j < 2*n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					int e2 = pq.poll();
					index = Arrays.binarySearch(elements, e2);
					if (index == i || index == k) {
						continue;
					}
					assertEquals(2, e2);
					assertEquals(2*n/3-2, pq.size());
					for (int j = 2*n/3; j < n; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					int e3 = pq.poll();
					index = Arrays.binarySearch(elements, e3);
					if (index == i || index == k) {
						continue;
					}
					int p3 = priorities[Arrays.binarySearch(elements, e3)];
					assertEquals(19, e3);
					assertEquals(n-3, pq.size());
					if (e1 != elements[i] && e2 != elements[i] && e3 != elements[i]) {
						assertTrue(pq.change(elements[i], -1));
						assertEquals(-1, pq.peekPriority(elements[i]));
						assertEquals(n-3, pq.size());
					}
					if (e1 != elements[k] && e2 != elements[k] && e3 != elements[k]) {
						assertTrue(pq.change(elements[k], -2));
						assertEquals(-2, pq.peekPriority(elements[k]));
						assertEquals(n-3, pq.size());
					}
					double lastP = -1000;
					int count = 0;
					while (!pq.isEmpty()) {
						double p = pq.peekPriority();
						int e = pq.poll();
						assertTrue(p >= lastP);
						lastP = p;
						int j = Arrays.binarySearch(elements, e);
						if (j != i && j != k) {
							assertEquals(priorities[j], p);
						} else if (j == i) {
							assertEquals(-1, p);
						} else if (j == k) {
							assertEquals(-2, p);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	@Test
	public void testPromoteDemoteMinHeap() {
		int n = 15;
		int[] e = createElements(n);
		int[] p = orderedArray(n);
		for (int i = 0; i < n; i++) {
			p[i] = 2*p[i] + 2;
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.promote(e[i], 1));
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
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.demote(e[i], 100));
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
				IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
				for (int j = 0; j < n; j++) {
					pq.offer(e[j], p[j]);
				}
				if (pNew < p[i]) {
					assertTrue(pq.promote(e[i], pNew));
				} else {
					assertTrue(pq.demote(e[i], pNew));
				}
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
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertFalse(pq.promote(e[i], p[i]));
			assertEquals(p[i], pq.peekPriority(e[i]));
			assertFalse(pq.demote(e[i], p[i]));
			assertEquals(p[i], pq.peekPriority(e[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(e[j], pq.poll());
			}
			assertTrue(pq.isEmpty());
		}
		// same relative order to next best
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.promote(e[i], p[i]-1));
				assertEquals(p[i]-1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// same relative order to next worst
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.demote(e[i], p[i]+1));
				assertEquals(p[i]+1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int pNew = 1; pNew <= maxP; pNew += 2) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMinHeap(n);
			for (int j = 0; j < n-1; j++) {
				pq.offer(e[j], p[j]);
			}
			assertFalse(pq.promote(e[n-1], pNew));
			assertFalse(pq.contains(e[n-1]));
			assertFalse(pq.demote(e[n-1], pNew));
			assertFalse(pq.contains(e[n-1]));
			for (int j = 0; j < n-1; j++) {
				assertEquals(e[j], pq.poll());
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	// MAX HEAP TESTS
	
	@Test
	public void testCreateMaxHeap() {
		for (int n = 1; n <= 4; n++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			assertEquals(0, pq.size());
			assertTrue(pq.isEmpty());
			assertEquals(n, pq.domain());
			for (int i = 0; i < n; i++) {
				assertFalse(pq.contains(i));
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> IntFibonacciHeap.createMaxHeap(0)
		);
	}
	
	@Test
	public void testClearMaxHeap() {
		IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(10);
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
	public void testIncreasingPriorityMaxHeap() {
		int n = 31;
		int[] e = createElements(n);
		int[] p = reversedArray(n);
		IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
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
	public void testDecreasingPriorityMaxHeap() {
		int n = 31;
		int[] e = createElements(n);
		int[] p = orderedArray(n);
		IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
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
	public void testRandomPriorityMaxHeap() {
		int n = 31;
		int[] e = createElements(n);
		int[] p = shuffle(orderedArray(n));
		IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
		assertEquals(0, pq.size());
		assertTrue(pq.isEmpty());
		assertEquals(n, pq.domain());
		int maxP = Integer.MIN_VALUE;
		int maxE = -1;
		for (int i = 0; i < n; i++) {
			assertTrue(pq.offer(e[i], p[i]));
			assertEquals(i+1, pq.size());
			assertFalse(pq.isEmpty());
			if (p[i] > maxP) {
				maxP = p[i];
				maxE = e[i];
			}
			assertEquals(maxE, pq.peek());
			assertEquals(maxP, pq.peekPriority());
			assertEquals(p[i], pq.peekPriority(e[i]));
			assertTrue(pq.contains(e[i]));
			assertFalse(pq.offer(e[i], p[i]));
		}
		int lastP = 1000;
		int[] expectedP = new int[n];
		for (int i = 0; i < n; i++) {
			expectedP[e[i]] = p[i];
		}
		for (int i = 0; i < n; i++) {
			assertFalse(pq.isEmpty());
			int nextP = pq.peekPriority();
			assertTrue(nextP <= lastP);
			assertEquals(nextP, expectedP[pq.poll()], "p[i],e[i]="+p[i]+","+e[i]);
			lastP = nextP;
			assertEquals(n-1-i, pq.size());
		}
		assertTrue(pq.isEmpty());
	}
	
	@Test
	public void testChangePriorityMaxHeap() {
		int n = 15;
		int[] e = createElements(n);
		int[] p = orderedArray(n);
		for (int i = 0; i < n; i++) {
			p[i] = -(2*p[i] + 2);
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.change(e[i], -1));
			assertEquals(-1, pq.peekPriority(e[i]));
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
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.change(e[i], -100));
			assertEquals(-100, pq.peekPriority(e[i]));
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
				IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
				for (int j = 0; j < n; j++) {
					pq.offer(e[j], p[j]);
				}
				assertTrue(pq.change(e[i], -pNew));
				assertEquals(-pNew, pq.peekPriority(e[i]));
				int j = 0;
				for (; j < n; j++) {
					if (i != j) {
						if (p[j] > -pNew) {
							assertEquals(e[j], pq.poll(), "p,i,j="+pNew+","+i+","+j);
						} else {
							break;
						}
					}
				}
				assertEquals(e[i], pq.poll(), "p,i,j="+pNew+","+i+","+j);
				for (; j < n; j++) {
					if (i!=j && p[j] < -pNew) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// equal change test
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertFalse(pq.change(e[i], p[i]));
			assertEquals(p[i], pq.peekPriority(e[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(e[j], pq.poll());
			}
			assertTrue(pq.isEmpty());
		}
		// same relative order to next worst
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.change(e[i], p[i]-1));
				assertEquals(p[i]-1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// same relative order to next best
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.change(e[i], p[i]+1));
				assertEquals(p[i]+1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int pNew = 1; pNew <= maxP; pNew += 2) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n-1; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.change(e[n-1], -pNew));
			assertEquals(-pNew, pq.peekPriority(e[n-1]));
			int j = 0;
			for (; j < n-1; j++) {
				if (p[j] > -pNew) {
					assertEquals(e[j], pq.poll());
				} else {
					break;
				}
			}
			assertEquals(e[n-1], pq.poll());
			for (; j < n-1; j++) {
				if (p[j] < -pNew) {
					assertEquals(e[j], pq.poll());
				}
			}
			assertTrue(pq.isEmpty());
		}
	}
	
	@Test
	public void testMultilevelPromoteDemoteMaxHeap() {
		int[] priorities = { 1, 5, 2, 11, 4, 13, 17, 15, 8, 31, 40, 37, 14, 100, 50, 70, 30, 45, 60, 0, 33, 99, 16, 97};
		int n = priorities.length;
		int[] elements = new int[n];
		for (int i = 0; i < n; i++) {
			elements[i] = i;
			priorities[i] = -priorities[i];
		}
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < n; k++) {
				if (i!= k) {
					IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
					for (int j = 0; j < n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					int e1 = pq.poll();
					int index = Arrays.binarySearch(elements, e1);
					if (index == i || index == k) {
						continue;
					}
					assertEquals(0, e1);
					assertEquals(n/3-1, pq.size());
					for (int j = n/3; j < 2*n/3; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					int e2 = pq.poll();
					index = Arrays.binarySearch(elements, e2);
					if (index == i || index == k) {
						continue;
					}
					assertEquals(2, e2);
					assertEquals(2*n/3-2, pq.size());
					for (int j = 2*n/3; j < n; j++) {
						pq.offer(elements[j], priorities[j]);
					}
					int e3 = pq.poll();
					index = Arrays.binarySearch(elements, e3);
					if (index == i || index == k) {
						continue;
					}
					int p3 = priorities[Arrays.binarySearch(elements, e3)];
					assertEquals(19, e3);
					assertEquals(n-3, pq.size());
					if (e1 != elements[i] && e2 != elements[i] && e3 != elements[i]) {
						assertTrue(pq.change(elements[i], 1));
						assertEquals(1, pq.peekPriority(elements[i]));
						assertEquals(n-3, pq.size());
					}
					if (e1 != elements[k] && e2 != elements[k] && e3 != elements[k]) {
						assertTrue(pq.change(elements[k], 2));
						assertEquals(2, pq.peekPriority(elements[k]));
						assertEquals(n-3, pq.size());
					}
					double lastP = 1000;
					int count = 0;
					while (!pq.isEmpty()) {
						double p = pq.peekPriority();
						int e = pq.poll();
						assertTrue(p <= lastP);
						lastP = p;
						int j = Arrays.binarySearch(elements, e);
						if (j != i && j != k) {
							assertEquals(priorities[j], p);
						} else if (j == i) {
							assertEquals(1, p);
						} else if (j == k) {
							assertEquals(2, p);
						}
						count++;
						assertEquals(n-3-count, pq.size());
					}
					assertEquals(n-3, count);
				}
			}
		}
	}
	
	@Test
	public void testPromoteDemoteMaxHeap() {
		int n = 15;
		int[] e = createElements(n);
		int[] p = orderedArray(n);
		for (int i = 0; i < n; i++) {
			p[i] = -(2*p[i] + 2);
		}
		// to front tests
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.promote(e[i], -1));
			assertEquals(-1, pq.peekPriority(e[i]));
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
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertTrue(pq.demote(e[i], -100));
			assertEquals(-100, pq.peekPriority(e[i]));
			for (int j = 0; j < n; j++) {
				if (i!=j) {
					assertEquals(e[j], pq.poll());
				}
			}
			assertEquals(e[i], pq.poll());
			assertTrue(pq.isEmpty());
		}
		// to interior tests
		int maxP = (2*(n-1) + 2);
		for (int pNew = 3; pNew <= maxP; pNew += 2) {
			for (int i = 0; i < n; i++) {
				IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
				for (int j = 0; j < n; j++) {
					pq.offer(e[j], p[j]);
				}
				if (-pNew > p[i]) {
					assertTrue(pq.promote(e[i], -pNew));
				} else {
					assertTrue(pq.demote(e[i], -pNew));
				}
				assertEquals(-pNew, pq.peekPriority(e[i]));
				int j = 0;
				for (; j < n; j++) {
					if (i != j) {
						if (p[j] > -pNew) {
							assertEquals(e[j], pq.poll(), "p,i,j="+pNew+","+i+","+j);
						} else {
							break;
						}
					}
				}
				assertEquals(e[i], pq.poll(), "p,i,j="+pNew+","+i+","+j);
				for (; j < n; j++) {
					if (i!=j && p[j] < -pNew) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// equal change test
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
			}
			assertFalse(pq.promote(e[i], p[i]));
			assertEquals(p[i], pq.peekPriority(e[i]));
			assertFalse(pq.demote(e[i], p[i]));
			assertEquals(p[i], pq.peekPriority(e[i]));
			for (int j = 0; j < n; j++) {
				assertEquals(e[j], pq.poll());
			}
			assertTrue(pq.isEmpty());
		}
		// same relative order to next worst
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.demote(e[i], p[i]-1));
				assertEquals(p[i]-1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// same relative order to next best
		for (int i = 0; i < n; i++) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			int e1 = -1;
			int e2 = -1;
			for (int j = 0; j < n; j++) {
				pq.offer(e[j], p[j]);
				if (j == n/3) e1 = pq.poll();
				if (j == 2*n/3) e2 = pq.poll();
			}
			if (e[i] != e1 && e[i] != e2) {
				assertTrue(pq.promote(e[i], p[i]+1));
				assertEquals(p[i]+1, pq.peekPriority(e[i]));
				for (int j = 0; j < n; j++) {
					if (e[j] != e1 && e[j] != e2) {
						assertEquals(e[j], pq.poll());
					}
				}
				assertTrue(pq.isEmpty());
			}
		}
		// new element test
		maxP = 2*(n-1) + 3;
		for (int pNew = 1; pNew <= maxP; pNew += 2) {
			IntFibonacciHeap pq = IntFibonacciHeap.createMaxHeap(n);
			for (int j = 0; j < n-1; j++) {
				pq.offer(e[j], p[j]);
			}
			assertFalse(pq.promote(e[n-1], -pNew));
			assertFalse(pq.contains(e[n-1]));
			assertFalse(pq.demote(e[n-1], -pNew));
			assertFalse(pq.contains(e[n-1]));
			for (int j = 0; j < n-1; j++) {
				assertEquals(e[j], pq.poll());
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
