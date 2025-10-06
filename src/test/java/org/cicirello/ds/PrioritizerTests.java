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

import org.junit.jupiter.api.*;

/** JUnit tests for the classes that implement Prioritizer. */
public class PrioritizerTests {

  @Test
  public void testMinOrderInt() {
    IntegerMinOrder order = new IntegerMinOrder();
    int p1 = 1;
    int p2 = 2;
    assertTrue(order.comesBefore(p1, p2));
    assertFalse(order.comesBefore(p2, p1));
    assertFalse(order.comesBefore(p1, p1));
  }

  @Test
  public void testMinOrderDouble() {
    DoubleMinOrder order = new DoubleMinOrder();
    double p1 = 1;
    double p2 = 2;
    assertTrue(order.comesBefore(p1, p2));
    assertFalse(order.comesBefore(p2, p1));
    assertFalse(order.comesBefore(p1, p1));
  }

  @Test
  public void testMaxOrderInt() {
    IntegerMaxOrder order = new IntegerMaxOrder();
    int p1 = 2;
    int p2 = 1;
    assertTrue(order.comesBefore(p1, p2));
    assertFalse(order.comesBefore(p2, p1));
    assertFalse(order.comesBefore(p1, p1));
  }

  @Test
  public void testMaxOrderDouble() {
    DoubleMaxOrder order = new DoubleMaxOrder();
    double p1 = 2;
    double p2 = 1;
    assertTrue(order.comesBefore(p1, p2));
    assertFalse(order.comesBefore(p2, p1));
    assertFalse(order.comesBefore(p1, p1));
  }
}
