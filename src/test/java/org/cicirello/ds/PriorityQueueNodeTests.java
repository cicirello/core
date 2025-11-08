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

import static org.junit.jupiter.api.Assertions.*;

import org.cicirello.util.Copyable;
import org.junit.jupiter.api.*;

/** JUnit tests for the PriorityQueueNode class. */
public class PriorityQueueNodeTests {

  @Test
  public void testIntegerNode() {
    PriorityQueueNode.Integer<String> node1 = new PriorityQueueNode.Integer<String>("hello", 42);
    assertEquals("hello", node1.getElement());
    assertEquals(42, node1.getPriority());
    PriorityQueueNode.Integer<String> node2 = new PriorityQueueNode.Integer<String>("hello", 42);
    assertEquals("hello", node2.getElement());
    assertEquals(42, node2.getPriority());
    PriorityQueueNode.Integer<String> node3 = new PriorityQueueNode.Integer<String>("hello", 43);
    assertEquals("hello", node3.getElement());
    assertEquals(43, node3.getPriority());
    PriorityQueueNode.Integer<String> node4 = new PriorityQueueNode.Integer<String>("world", 42);
    assertEquals("world", node4.getElement());
    assertEquals(42, node4.getPriority());
    assertEquals(node1, node2);
    assertEquals(node1.hashCode(), node2.hashCode());
    assertNotEquals(node1, node3);
    assertNotEquals(node1, node4);
    assertNotEquals(node1.hashCode(), node3.hashCode());
    assertNotEquals(node1.hashCode(), node4.hashCode());
    assertFalse(node1.equals(null));
    assertFalse(node1.equals("hello"));
    assertEquals("hello", node1.getElement());
  }

  @Test
  public void testDoubleNode() {
    PriorityQueueNode.Double<String> node1 = new PriorityQueueNode.Double<String>("hello", 42.0);
    assertEquals("hello", node1.getElement());
    assertEquals(42.0, node1.getPriority(), 0.0);
    PriorityQueueNode.Double<String> node2 = new PriorityQueueNode.Double<String>("hello", 42.0);
    assertEquals("hello", node2.getElement());
    assertEquals(42.0, node2.getPriority(), 0.0);
    PriorityQueueNode.Double<String> node3 = new PriorityQueueNode.Double<String>("hello", 43.0);
    assertEquals("hello", node3.getElement());
    assertEquals(43.0, node3.getPriority(), 0.0);
    PriorityQueueNode.Double<String> node4 = new PriorityQueueNode.Double<String>("world", 42.0);
    assertEquals("world", node4.getElement());
    assertEquals(42.0, node4.getPriority(), 0.0);
    assertEquals(node1, node2);
    assertEquals(node1.hashCode(), node2.hashCode());
    assertNotEquals(node1, node3);
    assertNotEquals(node1, node4);
    assertNotEquals(node1.hashCode(), node3.hashCode());
    assertNotEquals(node1.hashCode(), node4.hashCode());
    assertFalse(node1.equals(null));
    assertFalse(node1.equals("hello"));
    assertEquals("hello", node1.getElement());
  }

  @Test
  public void testIntegerNodeCopy() {
    PriorityQueueNode.Integer<String> node1 = new PriorityQueueNode.Integer<String>("hello", 42);
    PriorityQueueNode.Integer<String> node2 = new PriorityQueueNode.Integer<String>("world", 43);
    PriorityQueueNode.Integer<String> copy1 = node1.copy();
    PriorityQueueNode.Integer<String> copy2 = node2.copy();
    assertEquals(node1, copy1);
    assertEquals(node2, copy2);
    assertNotEquals(node1, copy2);
    assertNotEquals(node2, copy1);
    assertTrue(node1 != copy1);
    assertTrue(node2 != copy2);

    PriorityQueueNode.Integer<TestType> node3 =
        new PriorityQueueNode.Integer<TestType>(new TestType("hello"), 42);
    PriorityQueueNode.Integer<TestType> node4 =
        new PriorityQueueNode.Integer<TestType>(new TestType("world"), 43);
    PriorityQueueNode.Integer<TestType> copy3 = node3.copy();
    PriorityQueueNode.Integer<TestType> copy4 = node4.copy();
    assertEquals(node3, copy3);
    assertEquals(node4, copy4);
    assertNotEquals(node3, copy4);
    assertNotEquals(node4, copy3);
    assertTrue(node3 != copy3);
    assertTrue(node4 != copy4);
  }

  @Test
  public void testDoubleNodeCopy() {
    PriorityQueueNode.Double<String> node1 = new PriorityQueueNode.Double<String>("hello", 42);
    PriorityQueueNode.Double<String> node2 = new PriorityQueueNode.Double<String>("world", 43);
    PriorityQueueNode.Double<String> copy1 = node1.copy();
    PriorityQueueNode.Double<String> copy2 = node2.copy();
    assertEquals(node1, copy1);
    assertEquals(node2, copy2);
    assertNotEquals(node1, copy2);
    assertNotEquals(node2, copy1);
    assertTrue(node1 != copy1);
    assertTrue(node2 != copy2);

    PriorityQueueNode.Double<TestType> node3 =
        new PriorityQueueNode.Double<TestType>(new TestType("hello"), 42);
    PriorityQueueNode.Double<TestType> node4 =
        new PriorityQueueNode.Double<TestType>(new TestType("world"), 43);
    PriorityQueueNode.Double<TestType> copy3 = node3.copy();
    PriorityQueueNode.Double<TestType> copy4 = node4.copy();
    assertEquals(node3, copy3);
    assertEquals(node4, copy4);
    assertNotEquals(node3, copy4);
    assertNotEquals(node4, copy3);
    assertTrue(node3 != copy3);
    assertTrue(node4 != copy4);
  }

  private static class TestType implements Copyable<TestType> {

    private String x;

    public TestType(String x) {
      this.x = x;
    }

    @Override
    public TestType copy() {
      return new TestType(x);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null) return false;
      if (o instanceof TestType) {
        TestType casted = (TestType) o;
        return x.equals(casted.x);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return x.hashCode();
    }
  }
}
