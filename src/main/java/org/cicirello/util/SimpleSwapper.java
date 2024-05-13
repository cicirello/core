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

import java.util.List;

/**
 * A utility class for performing element swaps. The methods of this class assume that indexes are
 * different without verifying, although this will function correctly regardless. If your
 * application is likely to frequently pass identical indexes, then use {@link ValidatedSwapper}
 * instead. However, if your application is such that the indexes are significantly more likely
 * different, then just use this SimpleSwapper class to avoid unnecessary comparisons. As an example
 * of an application where SimpleSwapper is likely preferred, consider the case of randomizing the
 * ordering of the elements of an array. The expected number of fixed points in a random permutation
 * is 1, so the majority of randomly chosen swaps in such an application that lead to identical
 * indexes will be very low.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class SimpleSwapper {

  private SimpleSwapper() {}

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(byte[] array, int i, int j) {
    byte temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(char[] array, int i, int j) {
    char temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(double[] array, int i, int j) {
    double temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(float[] array, int i, int j) {
    float temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(long[] array, int i, int j) {
    long temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(short[] array, int i, int j) {
    short temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an array.
   *
   * @param array the array within which to swap elements
   * @param i one index
   * @param j another index
   */
  public static void swap(Object[] array, int i, int j) {
    Object temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Swaps the elements at two indexes of an list.
   *
   * @param list the list within which to swap elements
   * @param i one index
   * @param j another index
   * @param <T> the type of elements in the List
   */
  public static <T> void swap(List<T> list, int i, int j) {
    T temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
  }
}
