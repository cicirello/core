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

/** JUnit tests for the DoubleArray class. */
public class DoubleArrayTests {

  @Test
  public void testConstructor1() {
    for (int n = 0; n < 4; n++) {
      DoubleArray a = new DoubleArray(n);
      assertEquals(n, a.length());
      assertEquals(n, a.array().length);
      assertEquals(0, a.size());
      assertTrue(a.isEmpty());
      assertNotEquals(a, new DoubleArray(n));
    }
  }

  @Test
  public void testConstructor2() {
    double[][] arrays = {{}, {5}, {5, 7}, {5, 7, 9}, {5, 7, 9, 11}};
    for (double[] array : arrays) {
      double[] originalContents = array.clone();
      DoubleArray a = new DoubleArray(array);
      assertEquals(array.length, a.length());
      assertSame(array, a.array());
      assertEquals(0, a.size());
      assertTrue(a.isEmpty());
      assertEquals(a, new DoubleArray(array));
      assertEquals(a.hashCode(), new DoubleArray(array).hashCode());
      assertArrayEquals(originalContents, a.array());
      if (array.length > 0) {
        a.add(array[0]);
        assertFalse(a.isEmpty());
        assertNotEquals(a, new DoubleArray(array));
      }
      a.clear();
      assertEquals(a, new DoubleArray(array));
      assertEquals(a.hashCode(), new DoubleArray(array).hashCode());
    }
  }

  @Test
  public void testAdd() {
    double[] array = {9, 9, 9, 9, 9};
    double[] expectedContents = array.clone();
    DoubleArray a = new DoubleArray(array);
    for (int i = 0; i < array.length; i++) {
      a.add(10 + i);
      expectedContents[i] = 10 + i;
      assertArrayEquals(expectedContents, array);
      assertEquals(i + 1, a.size());
      for (int j = 0; j <= i; j++) {
        assertEquals(10 + j, a.get(j));
      }
      assertTrue(a.contains(10 + i));
      assertFalse(a.contains(9));
    }
  }
}
