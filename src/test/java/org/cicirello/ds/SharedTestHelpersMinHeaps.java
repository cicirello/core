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
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.jupiter.api.*;

/**
 * Test case functionality shared by the various heap classes, binary heaps, fibonacci heaps,
 * specifically for the min heap cases.
 */
public abstract class SharedTestHelpersMinHeaps extends SharedTestHelpersHeaps {

  private final Supplier<PriorityQueue<String>> factory;
  private final Function<Collection<IntegerPriorityQueueNode<String>>, PriorityQueue<String>>
      fromListFactory;

  SharedTestHelpersMinHeaps(
      Supplier<PriorityQueue<String>> factory,
      Function<Collection<IntegerPriorityQueueNode<String>>, PriorityQueue<String>>
          fromListFactory) {
    super(true);
    this.factory = factory;
    this.fromListFactory = fromListFactory;
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
    assertEquals(n - 1, pq.size());
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
    String s = pq.pollThenAdd(new IntegerPriorityQueueNode<String>("ZZZ", 1)).element();
    assertEquals(n, pq.size());
    assertTrue(pq.contains("ZZZ"));
    assertEquals(1, pq.peekPriority("ZZZ"));
    assertEquals(elements[0], s);
    assertEquals("ZZZ", pq.pollElement());
    s = pq.pollThenAdd(new IntegerPriorityQueueNode<String>("YYY", 7)).element();
    assertEquals(n - 1, pq.size());
    assertTrue(pq.contains("YYY"));
    assertEquals(7, pq.peekPriority("YYY"));
    assertEquals(elements[1], s);
    assertEquals(elements[2], pq.pollElement());
    assertEquals("YYY", pq.pollElement());
    for (int i = 3; i < n; i++) {
      assertEquals(elements[i], pq.pollElement());
    }
    assertEquals(0, pq.size());
    assertNull(pq.pollThenAdd(new IntegerPriorityQueueNode<String>("XXX", 9)));
    assertEquals(1, pq.size());
    assertTrue(pq.contains("XXX"));
    assertEquals(9, pq.peekPriority("XXX"));
    assertEquals(9, pq.peekPriority());
    assertEquals("XXX", pq.peekElement());
    s = pq.pollThenAdd(new IntegerPriorityQueueNode<String>("XXX", 3)).element();
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
      assertEquals(n - 1, pq.size());
      assertFalse(pq.remove(elements[i]));
      assertEquals(n - 1, pq.size());
      for (int j = 0; j < n; j++) {
        if (i != j) {
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
      IntegerPriorityQueueNode<String> pair =
          new IntegerPriorityQueueNode<String>(elements[i], priorities[i]);
      assertTrue(pq.remove(pair));
      assertFalse(pq.contains(pair));
      assertEquals(n - 1, pq.size());
      assertFalse(pq.remove(pair));
      assertEquals(n - 1, pq.size());
      for (int j = 0; j < n; j++) {
        if (i != j) {
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
    IntegerPriorityQueueNode<String> pair =
        new IntegerPriorityQueueNode<String>(elements[0], priorities[0]);
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
      assertEquals(n - 1, pq.size());
      assertFalse(pq.remove(elements[i]));
      assertEquals(n - 1, pq.size());
      for (int j = 0; j < n; j++) {
        if (i != j) {
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
      IntegerPriorityQueueNode<String> pair = new IntegerPriorityQueueNode<String>(elements[i], 42);
      assertTrue(pq.remove(pair));
      assertFalse(pq.contains(pair));
      assertEquals(n - 1, pq.size());
      assertFalse(pq.remove(pair));
      assertEquals(n - 1, pq.size());
      for (int j = 0; j < n; j++) {
        if (i != j) {
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
    IntegerPriorityQueueNode<String> pair = new IntegerPriorityQueueNode<String>(elements[3], p[3]);
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
        if (i != j) {
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
        if (i != j) {
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
    int maxP = 2 * (n - 1) + 2;
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
              assertEquals(elements[j], pq.pollElement(), "p,i,j=" + p + "," + i + "," + j);
            } else {
              break;
            }
          }
        }
        assertEquals(elements[i], pq.pollElement(), "p,i,j=" + p + "," + i + "," + j);
        for (; j < n; j++) {
          if (i != j && priorities[j] > p) {
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
    int maxP = 2 * (n - 1) + 3;
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

  final void changeNotLowerParentMinHeap() {
    int n = 15;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(n);
    // change not lower than parent (in a fib heap)
    for (int i = 0; i < n; i++) {
      PriorityQueue<String> pq = factory.get();
      populate(pq, elements, priorities, n);
      String minElement = pq.pollElement();
      if (!minElement.equals(elements[i])) {
        assertTrue(pq.change(elements[i], priorities[i] - 1));
        assertEquals(priorities[i] - 1, pq.peekPriority(elements[i]));
      }
      for (int j = 0; j < n; j++) {
        if (!minElement.equals(elements[j])) {
          assertEquals(elements[j], pq.pollElement());
        }
      }
      assertTrue(pq.isEmpty());
    }
  }

  final void changeCascadingCutMinHeap() {
    // This is primarily a special case for fibonacci heaps, but
    // use for all heap classes.
    int[] priorities = {
      1, 5, 2, 11, 4, 13, 17, 15, 8, 31, 40, 37, 14, 100, 50, 70, 30, 45, 60, 0, 33, 99, 16, 97
    };
    int[] orderedIndexes = {};
    int n = priorities.length;
    String[] elements = createStrings(n);
    for (int i = 0; i < n; i++) {
      for (int k = 0; k < n; k++) {
        if (i != k) {
          PriorityQueue<String> pq = factory.get();
          for (int j = 0; j < n / 3; j++) {
            pq.offer(elements[j], priorities[j]);
          }
          String e1 = pq.pollElement();
          int index = Arrays.binarySearch(elements, e1);
          if (index == i || index == k) {
            continue;
          }
          assertEquals("A", e1);
          assertEquals(n / 3 - 1, pq.size());
          for (int j = n / 3; j < 2 * n / 3; j++) {
            pq.offer(elements[j], priorities[j]);
          }
          String e2 = pq.pollElement();
          index = Arrays.binarySearch(elements, e2);
          if (index == i || index == k) {
            continue;
          }
          assertEquals("C", e2);
          assertEquals(2 * n / 3 - 2, pq.size());
          for (int j = 2 * n / 3; j < n; j++) {
            pq.offer(elements[j], priorities[j]);
          }
          String e3 = pq.pollElement();
          index = Arrays.binarySearch(elements, e3);
          if (index == i || index == k) {
            continue;
          }
          int p3 = priorities[Arrays.binarySearch(elements, e3)];
          assertEquals("T", e3);
          assertEquals(n - 3, pq.size());
          if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
            assertTrue(pq.change(elements[i], -1));
            assertEquals(-1, pq.peekPriority(elements[i]));
            assertEquals(n - 3, pq.size());
          }
          if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
            assertTrue(pq.change(elements[k], -2));
            assertEquals(-2, pq.peekPriority(elements[k]));
            assertEquals(n - 3, pq.size());
          }
          int lastP = -1000;
          int count = 0;
          while (!pq.isEmpty()) {
            IntegerPriorityQueueNode<String> e = pq.poll();
            String msg =
                "count,e,p,i,k="
                    + count
                    + ","
                    + e.element()
                    + ","
                    + e.priority()
                    + ","
                    + i
                    + ","
                    + k;
            assertTrue(e.priority() >= lastP, msg);
            lastP = e.priority();
            int j = Arrays.binarySearch(elements, e.element());
            if (j != i && j != k) {
              assertEquals(priorities[j], e.priority());
            } else if (j == i) {
              assertEquals(-1, e.priority());
            } else if (j == k) {
              assertEquals(-2, e.priority());
            }
            count++;
            assertEquals(n - 3 - count, pq.size());
          }
          assertEquals(n - 3, count);
        }
      }
    }
  }

  final void changeMultiLevelMinHeap() {
    // This is primarily a special case for fibonacci heaps, but
    // use for all heap classes.
    int[] priorities = {
      1, 5, 2, 11, 4, 13, 17, 15, 8, 31, 40, 37, 14, 100, 50, 70, 30, 45, 60, 0, 33, 99, 16, 97
    };
    int[] orderedIndexes = {};
    int n = priorities.length;
    String[] elements = createStrings(n);
    for (int i = 0; i < n; i++) {
      for (int k = 0; k < n; k++) {
        if (i != k) {
          PriorityQueue<String> pq = factory.get();
          for (int j = 0; j < n / 3; j++) {
            pq.offer(elements[j], priorities[j]);
          }
          String e1 = pq.pollElement();
          int index = Arrays.binarySearch(elements, e1);
          if (index == i || index == k) {
            continue;
          }
          assertEquals("A", e1);
          assertEquals(n / 3 - 1, pq.size());
          for (int j = n / 3; j < 2 * n / 3; j++) {
            pq.offer(elements[j], priorities[j]);
          }
          String e2 = pq.pollElement();
          index = Arrays.binarySearch(elements, e2);
          if (index == i || index == k) {
            continue;
          }
          assertEquals("C", e2);
          assertEquals(2 * n / 3 - 2, pq.size());
          for (int j = 2 * n / 3; j < n; j++) {
            pq.offer(elements[j], priorities[j]);
          }
          String e3 = pq.pollElement();
          index = Arrays.binarySearch(elements, e3);
          if (index == i || index == k) {
            continue;
          }
          int p3 = priorities[Arrays.binarySearch(elements, e3)];
          assertEquals("T", e3);
          assertEquals(n - 3, pq.size());
          if (!e1.equals(elements[i]) && !e2.equals(elements[i]) && !e3.equals(elements[i])) {
            assertTrue(pq.change(elements[i], 95));
            assertEquals(95, pq.peekPriority(elements[i]));
            assertEquals(n - 3, pq.size());
          }
          if (!e1.equals(elements[k]) && !e2.equals(elements[k]) && !e3.equals(elements[k])) {
            assertTrue(pq.change(elements[k], 102));
            assertEquals(102, pq.peekPriority(elements[k]));
            assertEquals(n - 3, pq.size());
          }
          int lastP = -1000;
          int count = 0;
          while (!pq.isEmpty()) {
            IntegerPriorityQueueNode<String> e = pq.poll();
            String msg =
                "count,e,p,i,k="
                    + count
                    + ","
                    + e.element()
                    + ","
                    + e.priority()
                    + ","
                    + i
                    + ","
                    + k;
            assertTrue(e.priority() >= lastP, msg);
            lastP = e.priority();
            int j = Arrays.binarySearch(elements, e.element());
            if (j != i && j != k) {
              assertEquals(priorities[j], e.priority());
            } else if (j == i) {
              assertEquals(95, e.priority());
            } else if (j == k) {
              assertEquals(102, e.priority());
            }
            count++;
            assertEquals(n - 3 - count, pq.size());
          }
          assertEquals(n - 3, count);
        }
      }
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
        if (i != j) {
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
        if (i != j) {
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
    int maxP = 2 * (n - 1) + 2;
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
              assertEquals(elements[j], pq.pollElement(), "p,i,j=" + p + "," + i + "," + j);
            } else {
              break;
            }
          }
        }
        assertEquals(elements[i], pq.pollElement(), "p,i,j=" + p + "," + i + "," + j);
        for (; j < n; j++) {
          if (i != j && priorities[j] > p) {
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
    int maxP = 2 * (n - 1) + 3;
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

  final void defaultMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(pairs[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(pairs[i]));
      assertFalse(pq.offer(pairs[i].element(), pairs[i].priority()));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[i], pq.poll());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.poll());
  }

  final void defaultDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(pairs[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(pairs[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[i], pq.poll());
      assertTrue(pq.contains(pairs[i].element()));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(pairs[i], pq.poll());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.poll());
  }

  final void defaultReverseMinHeap() {
    int n = 31;
    String[] elements = createStringsRev(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(pairs[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals(elements[i], pq.peekElement());
      assertEquals(pairs[i], pq.peek());
      assertEquals((int) elements[i].charAt(0), pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(pairs[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[n - 1], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[n - 1 - i], pq.poll());
      assertFalse(pq.contains(elements[n - 1 - i]));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.poll());
  }

  final void defaultReverseDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStringsRev(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(pairs[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals(elements[i], pq.peekElement());
      assertEquals(pairs[i], pq.peek());
      assertEquals((int) elements[i].charAt(0), pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(pairs[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[n - 1], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[n - 1 - i], pq.poll());
      assertTrue(pq.contains(elements[n - 1 - i]));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(pairs[n - 1 - i], pq.poll());
      assertFalse(pq.contains(elements[n - 1 - i]));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.poll());
  }

  final void defaultArbitraryMinHeap() {
    int n = 31;
    String[] elements = createStringsArbitrary(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(pairs[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(pairs[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(new IntegerPriorityQueueNode<String>("A", (int) 'A'), pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      String expected = "" + ((char) ('A' + i));
      assertEquals(new IntegerPriorityQueueNode<String>(expected, (int) ('A' + i)), pq.poll());
      assertFalse(pq.contains(expected));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.poll());
  }

  final void defaultArbitraryDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStringsArbitrary(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(pairs[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(pairs[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(new IntegerPriorityQueueNode<String>("A", (int) 'A'), pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      String expected = "" + ((char) ('A' + i));
      assertEquals(new IntegerPriorityQueueNode<String>(expected, (int) ('A' + i)), pq.poll());
      assertTrue(pq.contains(expected));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(new IntegerPriorityQueueNode<String>(expected, (int) ('A' + i)), pq.poll());
      assertFalse(pq.contains(expected));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.poll());
  }

  final void addSimpleMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.add(pairs[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.add(pairs[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[i], pq.poll());
      assertTrue(pq.contains(pairs[i].element()));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(pairs[i], pq.poll());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.poll());
  }

  final void listMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (IntegerPriorityQueueNode<String> next : pairs) {
      list.add(next);
    }
    PriorityQueue<String> pq = fromListFactory.apply(list);
    assertEquals(n, pq.size());
    assertFalse(pq.isEmpty());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(pairs[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[i], pq.poll());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.poll());

    final ArrayList<IntegerPriorityQueueNode<String>> list2 =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    list2.add(pairs[0]);
    list2.add(pairs[0]);
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> fromListFactory.apply(list2));
  }

  final void listEmptyMinHeap() {
    PriorityQueue<String> pq =
        fromListFactory.apply(new ArrayList<IntegerPriorityQueueNode<String>>());
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
  }

  final void listEmptyExceptionMinHeap() {
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> fromListFactory.apply(new ArrayList<IntegerPriorityQueueNode<String>>()));
  }

  final void listDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (IntegerPriorityQueueNode<String> next : pairs) {
      list.add(next);
    }
    PriorityQueue<String> pq = fromListFactory.apply(list);
    assertEquals(n, pq.size());
    assertFalse(pq.isEmpty());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(pairs[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[i], pq.poll());
      assertTrue(pq.contains(pairs[i].element()));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(pairs[i], pq.poll());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.poll());
  }

  final void listReverseMinHeap() {
    int n = 31;
    String[] elements = createStringsRev(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (IntegerPriorityQueueNode<String> next : pairs) {
      list.add(next);
    }
    PriorityQueue<String> pq = fromListFactory.apply(list);
    assertEquals(n, pq.size());
    assertFalse(pq.isEmpty());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(pairs[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[n - 1], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[n - 1 - i], pq.poll());
      assertFalse(pq.contains(elements[n - 1 - i]));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.poll());
  }

  final void listReverseDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStringsRev(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (IntegerPriorityQueueNode<String> next : pairs) {
      list.add(next);
    }
    PriorityQueue<String> pq = fromListFactory.apply(list);
    assertEquals(n, pq.size());
    assertFalse(pq.isEmpty());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(pairs[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[n - 1], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(pairs[n - 1 - i], pq.poll());
      assertTrue(pq.contains(elements[n - 1 - i]));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(pairs[n - 1 - i], pq.poll());
      assertFalse(pq.contains(elements[n - 1 - i]));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.poll());
  }

  final void listArbitraryMinHeap() {
    int n = 31;
    String[] elements = createStringsArbitrary(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (IntegerPriorityQueueNode<String> next : pairs) {
      list.add(next);
    }
    PriorityQueue<String> pq = fromListFactory.apply(list);
    assertEquals(n, pq.size());
    assertFalse(pq.isEmpty());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(pairs[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(new IntegerPriorityQueueNode<String>("A", (int) 'A'), pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      String expected = "" + ((char) ('A' + i));
      assertEquals(new IntegerPriorityQueueNode<String>(expected, (int) ('A' + i)), pq.poll());
      assertFalse(pq.contains(expected));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.poll());
  }

  final void listArbitraryDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStringsArbitrary(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    ArrayList<IntegerPriorityQueueNode<String>> list =
        new ArrayList<IntegerPriorityQueueNode<String>>();
    for (IntegerPriorityQueueNode<String> next : pairs) {
      list.add(next);
    }
    PriorityQueue<String> pq = fromListFactory.apply(list);
    assertEquals(n, pq.size());
    assertFalse(pq.isEmpty());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(pairs[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(new IntegerPriorityQueueNode<String>("A", (int) 'A'), pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      String expected = "" + ((char) ('A' + i));
      assertEquals(new IntegerPriorityQueueNode<String>(expected, (int) ('A' + i)), pq.poll());
      assertTrue(pq.contains(expected));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(new IntegerPriorityQueueNode<String>(expected, (int) ('A' + i)), pq.poll());
      assertFalse(pq.contains(expected));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.poll());
  }

  final void offerSeparateMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(elements[i], priorities[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(elements[i], pq.pollElement());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.pollElement());
  }

  final void offerSeparateReverseMinHeap() {
    int n = 31;
    String[] elements = createStringsRev(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals(elements[i], pq.peekElement());
      assertEquals(pairs[i], pq.peek());
      assertEquals((int) elements[i].charAt(0), pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(elements[i], priorities[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[n - 1], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(elements[n - 1 - i], pq.pollElement());
      assertFalse(pq.contains(elements[n - 1 - i]));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.pollElement());
  }

  final void offerSeparateArbitraryMinHeap() {
    int n = 31;
    String[] elements = createStringsArbitrary(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertFalse(pq.offer(elements[i], priorities[i]));
      assertEquals(n, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(new IntegerPriorityQueueNode<String>("A", (int) 'A'), pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      String expected = "" + ((char) ('A' + i));
      assertEquals(expected, pq.pollElement());
      assertFalse(pq.contains(expected));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.pollElement());
  }

  final void offerSeparateDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(elements[i], pq.pollElement());
      assertTrue(pq.contains(pairs[i].element()));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(elements[i], pq.pollElement());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.pollElement());
  }

  final void offerSeparateReverseDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStringsRev(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals(elements[i], pq.peekElement());
      assertEquals(pairs[i], pq.peek());
      assertEquals((int) elements[i].charAt(0), pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[n - 1], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(elements[n - 1 - i], pq.pollElement());
      assertTrue(pq.contains(elements[n - 1 - i]));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(elements[n - 1 - i], pq.pollElement());
      assertFalse(pq.contains(elements[n - 1 - i]));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.pollElement());
  }

  final void offerSeparateArbitraryDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStringsArbitrary(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.offer(elements[i], priorities[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(new IntegerPriorityQueueNode<String>("A", (int) 'A'), pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      String expected = "" + ((char) ('A' + i));
      assertEquals(expected, pq.pollElement());
      assertTrue(pq.contains(expected));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(expected, pq.pollElement());
      assertFalse(pq.contains(expected));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.pollElement());
  }

  final void addSeparateMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.add(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(elements[i], pq.pollElement());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(n - 1 - i, pq.size());
    }
    assertNull(pq.pollElement());
  }

  final void addSeparateDuplicatesAllowedMinHeap() {
    int n = 31;
    String[] elements = createStrings(n);
    int[] priorities = createPriorities(elements);
    IntegerPriorityQueueNode<String>[] pairs = createPairs(elements, priorities);
    PriorityQueue<String> pq = factory.get();
    assertEquals(0, pq.size());
    assertTrue(pq.isEmpty());
    assertNull(pq.peekElement());
    assertNull(pq.peek());
    assertEquals(Integer.MAX_VALUE, pq.peekPriority());
    for (int i = 0; i < n; i++) {
      assertTrue(pq.add(elements[i], priorities[i]));
      assertEquals(i + 1, pq.size());
      assertFalse(pq.isEmpty());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertTrue(pq.contains(elements[i]));
      assertTrue(pq.contains(pairs[i]));
      assertTrue(pq.add(elements[i], priorities[i]));
      assertEquals(n + i + 1, pq.size());
      assertEquals("A", pq.peekElement());
      assertEquals(pairs[0], pq.peek());
      assertEquals((int) 'A', pq.peekPriority());
    }
    for (int i = 0; i < n; i++) {
      assertEquals(priorities[i], pq.peekPriority(elements[i]));
    }
    assertEquals(Integer.MAX_VALUE, pq.peekPriority("hello"));
    for (int i = 0; i < n; i++) {
      assertEquals(elements[i], pq.pollElement());
      assertTrue(pq.contains(pairs[i].element()));
      assertEquals(2 * n - 1 - 2 * i, pq.size());
      assertEquals(elements[i], pq.pollElement());
      assertFalse(pq.contains(pairs[i].element()));
      assertEquals(2 * (n - 1 - i), pq.size());
    }
    assertNull(pq.pollElement());
  }
}
