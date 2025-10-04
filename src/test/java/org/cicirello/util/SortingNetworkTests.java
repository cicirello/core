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

package org.cicirello.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/** JUnit tests for the SortingNetwork class. */
public class SortingNetworkTests {

  @Test
  public void testSort4Double() {
    double[][] cases4 = {
      {8, 2, 4, 6},
      {8, 2, 6, 4},
      {8, 4, 2, 6},
      {8, 4, 6, 2},
      {8, 6, 4, 2},
      {8, 6, 2, 4},
      {2, 8, 4, 6},
      {2, 8, 6, 4},
      {4, 8, 2, 6},
      {4, 8, 6, 2},
      {6, 8, 4, 2},
      {6, 8, 2, 4},
      {2, 4, 8, 6},
      {2, 6, 8, 4},
      {4, 2, 8, 6},
      {4, 6, 8, 2},
      {6, 4, 8, 2},
      {6, 2, 8, 4},
      {2, 4, 6, 8},
      {2, 6, 4, 8},
      {4, 2, 6, 8},
      {4, 6, 2, 8},
      {6, 4, 2, 8},
      {6, 2, 4, 8}
    };
    double[][] cases9 = {
      {9, 8, 7, 2, 5, 4, 3, 6, 1},
      {9, 8, 7, 2, 5, 6, 3, 4, 1},
      {9, 8, 7, 4, 5, 2, 3, 6, 1},
      {9, 8, 7, 4, 5, 6, 3, 2, 1},
      {9, 8, 7, 6, 5, 4, 3, 2, 1},
      {9, 8, 7, 6, 5, 2, 3, 4, 1},
      {9, 2, 7, 8, 5, 4, 3, 6, 1},
      {9, 2, 7, 8, 5, 6, 3, 4, 1},
      {9, 4, 7, 8, 5, 2, 3, 6, 1},
      {9, 4, 7, 8, 5, 6, 3, 2, 1},
      {9, 6, 7, 8, 5, 4, 3, 2, 1},
      {9, 6, 7, 8, 5, 2, 3, 4, 1},
      {9, 2, 7, 4, 5, 8, 3, 6, 1},
      {9, 2, 7, 6, 5, 8, 3, 4, 1},
      {9, 4, 7, 2, 5, 8, 3, 6, 1},
      {9, 4, 7, 6, 5, 8, 3, 2, 1},
      {9, 6, 7, 4, 5, 8, 3, 2, 1},
      {9, 6, 7, 2, 5, 8, 3, 4, 1},
      {9, 2, 7, 4, 5, 6, 3, 8, 1},
      {9, 2, 7, 6, 5, 4, 3, 8, 1},
      {9, 4, 7, 2, 5, 6, 3, 8, 1},
      {9, 4, 7, 6, 5, 2, 3, 8, 1},
      {9, 6, 7, 4, 5, 2, 3, 8, 1},
      {9, 6, 7, 2, 5, 4, 3, 8, 1}
    };
    double[] forwardExpected4 = {2, 4, 6, 8};
    double[] forwardExpected9 = {9, 2, 7, 4, 5, 6, 3, 8, 1};
    double[] backwardExpected4 = {8, 6, 4, 2};
    double[] backwardExpected9 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
    for (int i = 0; i < cases4.length; i++) {
      double[] copy = cases4[i].clone();
      SortingNetwork.sort(copy, 0, 1, 2, 3);
      assertArrayEquals(forwardExpected4, copy);
      copy = cases9[i].clone();
      SortingNetwork.sort(copy, 1, 3, 5, 7);
      assertArrayEquals(forwardExpected9, copy);
      copy = cases4[i].clone();
      SortingNetwork.sort(copy, 3, 2, 1, 0);
      assertArrayEquals(backwardExpected4, copy);
      copy = cases9[i].clone();
      SortingNetwork.sort(copy, 7, 5, 3, 1);
      assertArrayEquals(backwardExpected9, copy);
    }
  }

  @Test
  public void testSort3Double() {
    double[][] cases = {
      {2, 4, 6},
      {2, 6, 4},
      {4, 2, 6},
      {4, 6, 2},
      {6, 4, 2},
      {6, 2, 4},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 6, 7, 4, 0},
      {9, 4, 8, 2, 7, 6, 0},
      {9, 4, 8, 6, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 2, 7, 4, 0}
    };
    double[][] forwardExpected = {
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0}
    };
    double[][] backwardExpected = {
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0}
    };
    int[] minIndex = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1};
    int[] maxIndex = {2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5, 5};
    int[] medIndex = {1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3};
    for (int i = 0; i < cases.length; i++) {
      double[] copy = cases[i].clone();
      SortingNetwork.sort(copy, minIndex[i], medIndex[i], maxIndex[i]);
      assertArrayEquals(forwardExpected[i], copy);
      copy = cases[i].clone();
      SortingNetwork.sort(copy, maxIndex[i], medIndex[i], minIndex[i]);
      assertArrayEquals(backwardExpected[i], copy);
    }
  }

  @Test
  public void testSort3Int() {
    int[][] cases = {
      {2, 4, 6},
      {2, 6, 4},
      {4, 2, 6},
      {4, 6, 2},
      {6, 4, 2},
      {6, 2, 4},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 6, 7, 4, 0},
      {9, 4, 8, 2, 7, 6, 0},
      {9, 4, 8, 6, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 2, 7, 4, 0}
    };
    int[][] forwardExpected = {
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {2, 4, 6},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0},
      {9, 2, 8, 4, 7, 6, 0}
    };
    int[][] backwardExpected = {
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {6, 4, 2},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0},
      {9, 6, 8, 4, 7, 2, 0}
    };
    int[] minIndex = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1};
    int[] maxIndex = {2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5, 5};
    int[] medIndex = {1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3};
    for (int i = 0; i < cases.length; i++) {
      int[] copy = cases[i].clone();
      SortingNetwork.sort(copy, minIndex[i], medIndex[i], maxIndex[i]);
      assertArrayEquals(forwardExpected[i], copy);
      copy = cases[i].clone();
      SortingNetwork.sort(copy, maxIndex[i], medIndex[i], minIndex[i]);
      assertArrayEquals(backwardExpected[i], copy);
    }
  }

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
