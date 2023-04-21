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
    final DoubleArray a = new DoubleArray(array);
    for (int i = 0; i < array.length; i++) {
      a.add(10 + i);
      expectedContents[i] = 10 + i;
      assertArrayEquals(expectedContents, array);
      assertEquals(i + 1, a.size());
      for (int j = 0; j <= i; j++) {
        assertEquals(10 + j, a.get(j));
        assertEquals(j, a.indexOf(10 + j));
        assertEquals(j, a.lastIndexOf(10 + j));
      }
      assertTrue(a.contains(10 + i));
      assertFalse(a.contains(9));
      assertEquals(-1, a.indexOf(9));
      assertEquals(-1, a.lastIndexOf(9));
    }
  }

  @Test
  public void testAddSetRemove() {
    double[] array = {5, 6, 7, 8, 9, 10};
    final DoubleArray a = new DoubleArray(array);
    a.add(0, 100);
    assertArrayEquals(new double[] {100, 5, 6, 7, 8, 9}, array);
    a.add(1, 200);
    assertArrayEquals(new double[] {100, 200, 5, 6, 7, 8}, array);
    a.add(1, 300);
    assertArrayEquals(new double[] {100, 300, 200, 5, 6, 7}, array);
    assertEquals(3, a.size());
    a.set(2, 500);
    assertArrayEquals(new double[] {100, 300, 500, 5, 6, 7}, array);
    assertEquals(3, a.size());
    a.set(1, 600);
    assertArrayEquals(new double[] {100, 600, 500, 5, 6, 7}, array);
    assertEquals(3, a.size());
    a.set(0, 700);
    assertArrayEquals(new double[] {700, 600, 500, 5, 6, 7}, array);
    assertEquals(3, a.size());

    IndexOutOfBoundsException thrown =
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(4, 900));
    thrown = assertThrows(IndexOutOfBoundsException.class, () -> a.set(3, 900));
    thrown = assertThrows(IndexOutOfBoundsException.class, () -> a.get(4));

    a.remove(1);
    assertArrayEquals(new double[] {700, 500, 5, 6, 7, 7}, array);
    assertEquals(2, a.size());

    a.remove(0);
    assertArrayEquals(new double[] {500, 5, 6, 7, 7, 7}, array);
    assertEquals(1, a.size());

    thrown = assertThrows(IndexOutOfBoundsException.class, () -> a.remove(1));
    assertArrayEquals(new double[] {500, 5, 6, 7, 7, 7}, array);
    assertEquals(1, a.size());
  }

  @Test
  public void testIndexOf() {
    DoubleArray a = new DoubleArray(6);
    a.add(5);
    a.add(6);
    a.add(6);
    a.add(7);
    a.add(7);
    a.add(8);
    assertEquals(1, a.indexOf(6));
    assertEquals(2, a.lastIndexOf(6));
    assertEquals(3, a.indexOf(7));
    assertEquals(4, a.lastIndexOf(7));
  }

  @Test
  public void testSort() {
    double[] array = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    double[] expected0 = array.clone();
    DoubleArray a = new DoubleArray(array);
    a.sort();
    assertArrayEquals(expected0, array);
    for (int i = 0; i < 5; i++) {
      a.add(array[i]);
    }
    a.sort();
    double[] expected1 = {5, 6, 7, 8, 9, 4, 3, 2, 1, 0};
    assertArrayEquals(expected1, array);
    for (int i = 5; i < array.length; i++) {
      a.add(array[i]);
    }
    a.sort();
    double[] expected2 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    assertArrayEquals(expected2, array);
  }

  @Test
  public void testEqualsHashCode() {
    double[] array = new double[5];
    DoubleArray a1 = new DoubleArray(array);
    DoubleArray a2 = new DoubleArray(array);
    assertEquals(a1, a2);
    for (int i = 0; i < array.length; i++) {
      a1.add(12 + i);
      assertNotEquals(a1, a2);
      a2.add(12 + i);
      assertEquals(a1, a2);
      assertEquals(a1.hashCode(), a2.hashCode());
    }
    assertNotEquals(a1, null);
    assertNotEquals(a1, array);
  }

  @Test
  public void testToString() {
    double[] array = {1, 2, 3};
    DoubleArray a = new DoubleArray(array);
    assertEquals("[]", a.toString());
    a.add(1);
    assertEquals("[1.0]", a.toString());
    a.add(2);
    assertEquals("[1.0, 2.0]", a.toString());
    a.add(3);
    assertEquals("[1.0, 2.0, 3.0]", a.toString());
  }
}
