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

package org.cicirello.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/** JUnit tests for the ArrayFiller class. */
public class ArrayFillerTests {

  @Test
  public void testCreate() {
    assertEquals(0, ArrayFiller.create(0).length);
    int[][] expected = {{0}, {0, 1}, {0, 1, 2}, {0, 1, 2, 3}};
    for (int i = 0; i < expected.length; i++) {
      assertArrayEquals(expected[i], ArrayFiller.create(i + 1));
    }
  }

  @Test
  public void testCreateOffset() {
    assertEquals(0, ArrayFiller.create(0, 5).length);
    int[][] expected = {{5}, {5, 6}, {5, 6, 7}, {5, 6, 7, 8}};
    for (int i = 0; i < expected.length; i++) {
      assertArrayEquals(expected[i], ArrayFiller.create(i + 1, 5));
    }
  }

  @Test
  public void testFill() {
    int[][] expected = {{0}, {0, 1}, {0, 1, 2}, {0, 1, 2, 3}};
    for (int i = 0; i < expected.length; i++) {
      int[] array = new int[i + 1];
      ArrayFiller.fill(array);
      assertArrayEquals(expected[i], array);
    }
  }

  @Test
  public void testFillOffset() {
    int[][] expected = {{5}, {5, 6}, {5, 6, 7}, {5, 6, 7, 8}};
    for (int i = 0; i < expected.length; i++) {
      int[] array = new int[i + 1];
      ArrayFiller.fill(array, 5);
      assertArrayEquals(expected[i], array);
    }
  }

  @Test
  public void testCreateReverse() {
    assertEquals(0, ArrayFiller.createReverse(0).length);
    int[][] expected = {{0}, {1, 0}, {2, 1, 0}, {3, 2, 1, 0}};
    for (int i = 0; i < expected.length; i++) {
      assertArrayEquals(expected[i], ArrayFiller.createReverse(i + 1));
    }
  }

  @Test
  public void testCreateReverseOffset() {
    assertEquals(0, ArrayFiller.createReverse(0, 5).length);
    int[][] expected = {{5}, {6, 5}, {7, 6, 5}, {8, 7, 6, 5}};
    for (int i = 0; i < expected.length; i++) {
      assertArrayEquals(expected[i], ArrayFiller.createReverse(i + 1, 5));
    }
  }

  @Test
  public void testReverse() {
    int[][] expected = {{0}, {1, 0}, {2, 1, 0}, {3, 2, 1, 0}};
    for (int i = 0; i < expected.length; i++) {
      int[] array = new int[i + 1];
      ArrayFiller.reverse(array);
      assertArrayEquals(expected[i], array);
    }
  }

  @Test
  public void testReverseOffset() {
    int[][] expected = {{5}, {6, 5}, {7, 6, 5}, {8, 7, 6, 5}};
    for (int i = 0; i < expected.length; i++) {
      int[] array = new int[i + 1];
      ArrayFiller.reverse(array, 5);
      assertArrayEquals(expected[i], array);
    }
  }

  @Test
  public void testFillPartial() {
    int[][] expected = {{0, 0, 0, 0}, {0, 1, 0, 0, 0}, {0, 1, 2, 0, 0, 0}, {0, 1, 2, 3, 0, 0, 0}};
    for (int i = 0; i < expected.length; i++) {
      int[] array = new int[i + 1 + 3];
      ArrayFiller.fillPartial(array, i + 1);
      assertArrayEquals(expected[i], array);
    }
  }

  @Test
  public void testFillPartialOffset() {
    int[][] expected = {{5, 0, 0, 0}, {5, 6, 0, 0, 0}, {5, 6, 7, 0, 0, 0}, {5, 6, 7, 8, 0, 0, 0}};
    for (int i = 0; i < expected.length; i++) {
      int[] array = new int[i + 1 + 3];
      ArrayFiller.fillPartial(array, i + 1, 5);
      assertArrayEquals(expected[i], array);
    }
  }
}
