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

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.*;

/** Test cases for the SimpleSwapper class. */
public class SimpleSwapperTests {

  private final Swapper swapper;

  public SimpleSwapperTests() {
    swapper = new Swapper();
  }

  @Test
  public void testSwapEndsInt() {
    int[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorInt() {
    int[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexInt() {
    int[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoInt() {
    int[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneInt() {
    int[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsLong() {
    long[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorLong() {
    long[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexLong() {
    long[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoLong() {
    long[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneLong() {
    long[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsShort() {
    short[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorShort() {
    short[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexShort() {
    short[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoShort() {
    short[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneShort() {
    short[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsByte() {
    byte[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorByte() {
    byte[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexByte() {
    byte[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoByte() {
    byte[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneByte() {
    byte[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsDouble() {
    double[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorDouble() {
    double[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexDouble() {
    double[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoDouble() {
    double[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneDouble() {
    double[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsFloat() {
    float[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorFloat() {
    float[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexFloat() {
    float[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoFloat() {
    float[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneFloat() {
    float[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsChar() {
    char[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorChar() {
    char[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexChar() {
    char[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoChar() {
    char[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneChar() {
    char[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsObject() {
    Integer[] array = {5, 6, 7, 8};
    swapper.swap(array, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorObject() {
    Integer[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexObject() {
    Integer[] array = {5, 6, 7, 8};
    swapper.swap(array, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoObject() {
    Integer[] array = {5, 6};
    swapper.swap(array, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneObject() {
    Integer[] array = {5};
    swapper.swap(array, 0, 0);
    assertEquals(5, array[0]);
  }

  @Test
  public void testSwapEndsList() {
    Integer[] array = {5, 6, 7, 8};
    List<Integer> list = Arrays.asList(array);
    swapper.swap(list, 0, 3);
    assertEquals(8, array[0]);
    assertEquals(5, array[3]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
  }

  @Test
  public void testSwapInteriorList() {
    Integer[] array = {5, 6, 7, 8};
    List<Integer> list = Arrays.asList(array);
    swapper.swap(list, 1, 2);
    assertEquals(5, array[0]);
    assertEquals(8, array[3]);
    assertEquals(7, array[1]);
    assertEquals(6, array[2]);
  }

  @Test
  public void testSwapSameIndexList() {
    Integer[] array = {5, 6, 7, 8};
    List<Integer> list = Arrays.asList(array);
    swapper.swap(list, 1, 1);
    assertEquals(5, array[0]);
    assertEquals(6, array[1]);
    assertEquals(7, array[2]);
    assertEquals(8, array[3]);
  }

  @Test
  public void testSwapLengthTwoList() {
    Integer[] array = {5, 6};
    List<Integer> list = Arrays.asList(array);
    swapper.swap(list, 0, 1);
    assertEquals(6, array[0]);
    assertEquals(5, array[1]);
  }

  @Test
  public void testSwapLengthOneList() {
    Integer[] array = {5};
    List<Integer> list = Arrays.asList(array);
    swapper.swap(list, 0, 0);
    assertEquals(5, array[0]);
  }

  private final class Swapper {

    private void swap(byte[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private void swap(char[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private void swap(double[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private void swap(float[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private void swap(int[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private void swap(long[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private void swap(short[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private void swap(Object[] array, int i, int j) {
      SimpleSwapper.swap(array, i, j);
    }

    private <T> void swap(List<T> list, int i, int j) {
      SimpleSwapper.swap(list, i, j);
    }
  }
}
