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

/** JUnit tests for the IntegerArray class. */
public class IntegerArrayTests {

  @Test
  public void testConstructor1() {
    for (int n = 0; n < 4; n++) {
      IntegerArray a = new IntegerArray(n);
      assertEquals(n, a.length());
      assertEquals(n, a.array().length);
      assertEquals(0, a.size());
      assertTrue(a.isEmpty());
      assertNotEquals(a, new IntegerArray(n));
    }
  }

  @Test
  public void testConstructor2() {
    int[][] arrays = {{}, {5}, {5, 7}, {5, 7, 9}, {5, 7, 9, 11}};
    for (int[] array : arrays) {
      int[] originalContents = array.clone();
      IntegerArray a = new IntegerArray(array);
      assertEquals(array.length, a.length());
      assertSame(array, a.array());
      assertEquals(0, a.size());
      assertTrue(a.isEmpty());
      assertEquals(a, new IntegerArray(array));
      assertEquals(a.hashCode(), new IntegerArray(array).hashCode());
      assertArrayEquals(originalContents, a.array());
      if (array.length > 0) {
        a.add(array[0]);
        assertFalse(a.isEmpty());
        assertNotEquals(a, new IntegerArray(array));
      }
      a.clear();
      assertEquals(a, new IntegerArray(array));
      assertEquals(a.hashCode(), new IntegerArray(array).hashCode());
    }
  }

  @Test
  public void testAdd() {
    int[] array = {9, 9, 9, 9, 9};
    int[] expectedContents = array.clone();
    final IntegerArray a = new IntegerArray(array);
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
    int[] array = {5, 6, 7, 8, 9, 10};
    final IntegerArray a = new IntegerArray(array);
    a.add(0, 100);
    assertArrayEquals(new int[] {100, 5, 6, 7, 8, 9}, array);
    a.add(1, 200);
    assertArrayEquals(new int[] {100, 200, 5, 6, 7, 8}, array);
    a.add(1, 300);
    assertArrayEquals(new int[] {100, 300, 200, 5, 6, 7}, array);
    assertEquals(3, a.size());
    a.set(2, 500);
    assertArrayEquals(new int[] {100, 300, 500, 5, 6, 7}, array);
    assertEquals(3, a.size());
    a.set(1, 600);
    assertArrayEquals(new int[] {100, 600, 500, 5, 6, 7}, array);
    assertEquals(3, a.size());
    a.set(0, 700);
    assertArrayEquals(new int[] {700, 600, 500, 5, 6, 7}, array);
    assertEquals(3, a.size());

    IndexOutOfBoundsException thrown =
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(4, 900));
    thrown = assertThrows(IndexOutOfBoundsException.class, () -> a.set(3, 900));
    thrown = assertThrows(IndexOutOfBoundsException.class, () -> a.get(4));

    a.remove(1);
    assertArrayEquals(new int[] {700, 500, 5, 6, 7, 7}, array);
    assertEquals(2, a.size());

    a.remove(0);
    assertArrayEquals(new int[] {500, 5, 6, 7, 7, 7}, array);
    assertEquals(1, a.size());

    thrown = assertThrows(IndexOutOfBoundsException.class, () -> a.remove(1));
    assertArrayEquals(new int[] {500, 5, 6, 7, 7, 7}, array);
    assertEquals(1, a.size());
  }

  @Test
  public void testIndexOf() {
    IntegerArray a = new IntegerArray(6);
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
    int[] array = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    int[] expected0 = array.clone();
    IntegerArray a = new IntegerArray(array);
    a.sort();
    assertArrayEquals(expected0, array);
    for (int i = 0; i < 5; i++) {
      a.add(array[i]);
    }
    a.sort();
    int[] expected1 = {5, 6, 7, 8, 9, 4, 3, 2, 1, 0};
    assertArrayEquals(expected1, array);
    for (int i = 5; i < array.length; i++) {
      a.add(array[i]);
    }
    a.sort();
    int[] expected2 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    assertArrayEquals(expected2, array);
  }

  @Test
  public void testEqualsHashCode() {
    int[] array = new int[5];
    IntegerArray a1 = new IntegerArray(array);
    IntegerArray a2 = new IntegerArray(array);
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
    int[] array = {1, 2, 3};
    IntegerArray a = new IntegerArray(array);
    assertEquals("[]", a.toString());
    a.add(1);
    assertEquals("[1]", a.toString());
    a.add(2);
    assertEquals("[1, 2]", a.toString());
    a.add(3);
    assertEquals("[1, 2, 3]", a.toString());
  }
}
