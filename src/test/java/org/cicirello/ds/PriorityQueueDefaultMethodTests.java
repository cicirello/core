/*
 * Module org.cicirello.core
 * Copyright 2019-2025 Vincent A. Cicirello, <https://www.cicirello.org/>.
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
import java.util.NoSuchElementException;
import org.junit.jupiter.api.*;

/** JUnit tests for the default methods of the PriorityQueue interface. */
public class PriorityQueueDefaultMethodTests {

  @Test
  public void testAdd() {
    String[] elements = {"A", "B", "C", "D"};
    int[] priorities = {8, 6, 4, 2};
    final BinaryHeap<String> pq = BinaryHeap.createMinHeap();
    for (int i = 0; i < elements.length; i++) {
      assertTrue(pq.add(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertTrue(pq.contains(elements[i]));
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> pq.add(elements[0], 5));
  }

  @Test
  public void testAddPair() {
    String[] elements = {"A", "B", "C", "D"};
    int[] priorities = {8, 6, 4, 2};
    final BinaryHeap<String> pq = BinaryHeap.createMinHeap();
    for (int i = 0; i < elements.length; i++) {
      assertTrue(pq.add(new IntegerPriorityQueueNode<String>(elements[i], priorities[i])));
      assertEquals(i + 1, pq.size());
      assertTrue(pq.contains(elements[i]));
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> pq.add(new IntegerPriorityQueueNode<String>(elements[0], 5)));
  }

  @Test
  public void testAddAll() {
    String[] elements = {"A", "B", "C", "D"};
    int[] priorities = {8, 6, 4, 2};
    FibonacciHeap<String> pq = FibonacciHeap.createMinHeap();
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (int i = 0; i < elements.length; i++) {
      list.add(new IntegerPriorityQueueNode<String>(elements[i], priorities[i]));
    }
    assertTrue(pq.addAll(list));
    assertEquals(elements.length, pq.size());
    for (int i = 0; i < elements.length; i++) {
      assertTrue(pq.contains(elements[i]));
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
  }

  @Test
  public void testContainsAll() {
    String[] elements = {"A", "B", "C", "D"};
    int[] priorities = {8, 6, 4, 2};
    BinaryHeap<String> pq = BinaryHeap.createMinHeap();
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (int i = 0; i < elements.length; i++) {
      list.add(new IntegerPriorityQueueNode<String>(elements[i], priorities[i]));
    }
    for (int i = 0; i < elements.length; i++) {
      assertFalse(pq.containsAll(list));
      assertTrue(pq.add(elements[i], priorities[i]));
    }
    assertTrue(pq.containsAll(list));
  }

  @Test
  public void testElement() {
    String[] elements = {"A", "B", "C", "D"};
    int[] priorities = {2, 4, 6, 8};
    IntegerPriorityQueueNode<String> first =
        new IntegerPriorityQueueNode<String>(elements[0], priorities[0]);
    final BinaryHeap<String> pq = BinaryHeap.createMinHeap();
    NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> pq.element());
    for (int i = 0; i < elements.length; i++) {
      assertTrue(pq.add(elements[i], priorities[i]));
      assertEquals(first, pq.element());
    }
    pq.clear();
    for (int i = 0; i < elements.length; i++) {
      assertTrue(pq.add(elements[elements.length - 1 - i], priorities[elements.length - 1 - i]));
      assertEquals(
          new IntegerPriorityQueueNode<String>(
              elements[elements.length - 1 - i], priorities[elements.length - 1 - i]),
          pq.element());
    }
  }

  @Test
  public void testRemove() {
    String[] elements = {"A", "B", "C", "D"};
    int[] priorities = {8, 6, 4, 2};
    final BinaryHeap<String> pq = BinaryHeap.createMinHeap();
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (int i = 0; i < elements.length; i++) {
      list.add(new IntegerPriorityQueueNode<String>(elements[i], priorities[i]));
    }
    assertTrue(pq.addAll(list));
    assertEquals(elements.length, pq.size());
    for (int i = 0; i < elements.length; i++) {
      assertEquals(
          new IntegerPriorityQueueNode<String>(
              elements[elements.length - 1 - i], priorities[elements.length - 1 - i]),
          pq.remove());
      assertEquals(elements.length - i - 1, pq.size());
    }
    NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> pq.remove());
  }

  @Test
  public void testRemoveElement() {
    String[] elements = {"A", "B", "C", "D"};
    int[] priorities = {8, 6, 4, 2};
    final BinaryHeap<String> pq = BinaryHeap.createMinHeap();
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (int i = 0; i < elements.length; i++) {
      list.add(new IntegerPriorityQueueNode<String>(elements[i], priorities[i]));
    }
    assertTrue(pq.addAll(list));
    assertEquals(elements.length, pq.size());
    for (int i = 0; i < elements.length; i++) {
      assertEquals(elements[elements.length - 1 - i], pq.removeElement());
      assertEquals(elements.length - i - 1, pq.size());
    }
    NoSuchElementException thrown =
        assertThrows(NoSuchElementException.class, () -> pq.removeElement());
  }
}
