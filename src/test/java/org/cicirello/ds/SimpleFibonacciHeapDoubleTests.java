/*
 * Module org.cicirello.core
 * Copyright 2019-2023 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.*;

/** JUnit tests for the SimpleFibonacciHeapDouble class. */
public class SimpleFibonacciHeapDoubleTests extends SharedTestCommonHelpersHeapsDouble {

  public SimpleFibonacciHeapDoubleTests() {
    super(
        SimpleFibonacciHeapDouble::createMinHeap,
        SimpleFibonacciHeapDouble::createMaxHeap,
        SimpleFibonacciHeapDouble::createMinHeap,
        SimpleFibonacciHeapDouble::createMaxHeap);
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
    equalsAndHashCode(FibonacciHeapDouble::createMinHeap);
  }

  @Test
  public void testMerge() {
    int n = 24;
    String[] elements1 = new String[n];
    double[] priorities1 = new double[n];
    String[] elements2 = new String[n];
    double[] priorities2 = new double[n];
    ArrayList<PriorityQueueNode.Double<String>> list1 =
        new ArrayList<PriorityQueueNode.Double<String>>();
    ArrayList<PriorityQueueNode.Double<String>> list2 =
        new ArrayList<PriorityQueueNode.Double<String>>();
    for (int i = 0; i < 2 * n; i += 2) {
      elements1[i / 2] = "A" + i;
      elements2[i / 2] = "A" + (i + 1);
      priorities1[i / 2] = i;
      priorities2[i / 2] = i + 1;
      list1.add(new PriorityQueueNode.Double<String>(elements1[i / 2], priorities1[i / 2]));
      list2.add(new PriorityQueueNode.Double<String>(elements2[i / 2], priorities2[i / 2]));
    }
    final SimpleFibonacciHeapDouble<String> pq1 = SimpleFibonacciHeapDouble.createMinHeap(list1);
    final SimpleFibonacciHeapDouble<String> pq2 = SimpleFibonacciHeapDouble.createMinHeap(list2);
    assertFalse(pq1.merge(SimpleFibonacciHeapDouble.createMinHeap()));
    assertTrue(pq1.merge(pq2));
    assertTrue(pq2.isEmpty());
    assertEquals(0, pq2.size());
    assertEquals(2 * n, pq1.size());
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

    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> pq1.merge(SimpleFibonacciHeapDouble.createMaxHeap()));
  }

  @Test
  public void testMergeOtherMinIsSmaller() {
    int n = 24;
    String[] elements1 = new String[n];
    double[] priorities1 = new double[n];
    String[] elements2 = new String[n];
    double[] priorities2 = new double[n];
    ArrayList<PriorityQueueNode.Double<String>> list1 =
        new ArrayList<PriorityQueueNode.Double<String>>();
    ArrayList<PriorityQueueNode.Double<String>> list2 =
        new ArrayList<PriorityQueueNode.Double<String>>();
    for (int i = 0; i < 2 * n; i += 2) {
      elements1[i / 2] = "A" + i;
      elements2[i / 2] = "A" + (i + 1);
      priorities1[i / 2] = i;
      priorities2[i / 2] = i + 1;
      list1.add(new PriorityQueueNode.Double<String>(elements1[i / 2], priorities1[i / 2]));
      list2.add(new PriorityQueueNode.Double<String>(elements2[i / 2], priorities2[i / 2]));
    }
    final SimpleFibonacciHeapDouble<String> pq1 = SimpleFibonacciHeapDouble.createMinHeap(list2);
    final SimpleFibonacciHeapDouble<String> pq2 = SimpleFibonacciHeapDouble.createMinHeap(list1);
    assertFalse(pq1.merge(SimpleFibonacciHeapDouble.createMinHeap()));
    assertTrue(pq1.merge(pq2));
    assertTrue(pq2.isEmpty());
    assertEquals(0, pq2.size());
    assertEquals(2 * n, pq1.size());
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

    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> pq1.merge(SimpleFibonacciHeapDouble.createMaxHeap()));
  }
}
