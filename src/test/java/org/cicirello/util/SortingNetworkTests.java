/*
 * Module org.cicirello.core
 * Copyright 2019-2024 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

package org.cicirello.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/** JUnit tests for the SortingNetwork class. */
public class SortingNetworkTests {

  @Test
  public void testCompareExchangeDouble() {
    double[][] endCases = {
      {42, 52},
      {52, 42},
      {42, 9, 8, 7, 6, 5, 4, 3, 2, 1, 52},
      {52, 9, 8, 7, 6, 5, 4, 3, 2, 1, 42}
    };
    double min = 42;
    double max = 52;
    for (double[] c : endCases) {
      double[] copy = c.clone();
      SortingNetwork.compareExchange(copy, 0, copy.length - 1);
      assertEquals(min, copy[0]);
      assertEquals(max, copy[copy.length - 1]);
      for (int i = 1; i < copy.length - 1; i++) {
        assertEquals(c[i], copy[i]);
      }
    }
    double[][] interiorCases = {
      {10, 42, 52, 5},
      {10, 52, 42, 5},
    };
    for (double[] c : interiorCases) {
      double[] copy = c.clone();
      SortingNetwork.compareExchange(copy, 1, copy.length - 2);
      assertEquals(min, copy[1]);
      assertEquals(max, copy[copy.length - 2]);
      assertEquals(c[0], copy[0]);
      assertEquals(c[copy.length - 1], copy[copy.length - 1]);
    }
  }

  @Test
  public void testCompareExchangeInt() {
    int[][] endCases = {
      {42, 52},
      {52, 42},
      {42, 9, 8, 7, 6, 5, 4, 3, 2, 1, 52},
      {52, 9, 8, 7, 6, 5, 4, 3, 2, 1, 42}
    };
    int min = 42;
    int max = 52;
    for (int[] c : endCases) {
      int[] copy = c.clone();
      SortingNetwork.compareExchange(copy, 0, copy.length - 1);
      assertEquals(min, copy[0]);
      assertEquals(max, copy[copy.length - 1]);
      for (int i = 1; i < copy.length - 1; i++) {
        assertEquals(c[i], copy[i]);
      }
    }
    int[][] interiorCases = {
      {10, 42, 52, 5},
      {10, 52, 42, 5},
    };
    for (int[] c : interiorCases) {
      int[] copy = c.clone();
      SortingNetwork.compareExchange(copy, 1, copy.length - 2);
      assertEquals(min, copy[1]);
      assertEquals(max, copy[copy.length - 2]);
      assertEquals(c[0], copy[0]);
      assertEquals(c[copy.length - 1], copy[copy.length - 1]);
    }
  }
}
