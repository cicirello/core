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

/**
 * Implements sorting networks for sorting small subsets of array elements.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class SortingNetwork {

  /** Utility class, so private constructor to prevent instantiation. */
  private SortingNetwork() {}

  /**
   * The compare-exchange operation of sorting networks. Compares two elements, swapping them if out
   * of order. Assumes the indexes are in bounds. No elements other than the two specified elements
   * change.
   *
   * @param array the array to apply the compare-exchange
   * @param minTargetIndex the target index for the minimum of the two elements
   * @param maxTargetIndex the target index for the maximum of the two elements
   * @throws ArrayIndexOutOfBoundsException if either of the indexes are outside the bounds of the
   *     array
   */
  public static void compareExchange(double[] array, int minTargetIndex, int maxTargetIndex) {
    if (array[minTargetIndex] > array[maxTargetIndex]) {
      double temp = array[minTargetIndex];
      array[minTargetIndex] = array[maxTargetIndex];
      array[maxTargetIndex] = temp;
    }
  }

  /**
   * The compare-exchange operation of sorting networks. Compares two elements, swapping them if out
   * of order. Assumes the indexes are in bounds. No elements other than the two specified elements
   * change.
   *
   * @param array the array to apply the compare-exchange
   * @param minTargetIndex the target index for the minimum of the two elements
   * @param maxTargetIndex the target index for the maximum of the two elements
   * @throws ArrayIndexOutOfBoundsException if either of the indexes are outside the bounds of the
   *     array
   */
  public static void compareExchange(int[] array, int minTargetIndex, int maxTargetIndex) {
    if (array[minTargetIndex] > array[maxTargetIndex]) {
      int temp = array[minTargetIndex];
      array[minTargetIndex] = array[maxTargetIndex];
      array[maxTargetIndex] = temp;
    }
  }

  /**
   * 3-element sorting network. Assumes the indexes are different and in bounds. No elements other
   * than the three specified elements change.
   *
   * <p>This sorting network makes 3 comparisons. Assuming all initial orderings are equally likely,
   * the average number of swaps is 1.167. The maximum swaps is 2.
   *
   * <p>This sorting network uses the following sequence of compare-exchange operations, specified
   * by indexes beginning at 0 and consecutive (the methods of this class enable specifying the
   * indexes of the elements to sort): (0,2), (0,1), (1,2). Note that although other 3-element
   * sorting networks are possible with the same number of compare-exchange operations, only those
   * that begin with (0,2) minimize the average number of swaps.
   *
   * @param array the array to apply the compare-exchange
   * @param minTargetIndex the target index for the minimum of the three elements
   * @param medianTargetIndex the target index for the median of the three elements
   * @param maxTargetIndex the target index for the maximum of the three elements
   * @throws ArrayIndexOutOfBoundsException if any of the indexes are outside the bounds of the
   *     array
   */
  public static void sort(
      double[] array, int minTargetIndex, int medianTargetIndex, int maxTargetIndex) {
    compareExchange(array, minTargetIndex, maxTargetIndex);
    compareExchange(array, minTargetIndex, medianTargetIndex);
    compareExchange(array, medianTargetIndex, maxTargetIndex);
  }

  /**
   * 3-element sorting network. Assumes the indexes are different and in bounds. No elements other
   * than the three specified elements change.
   *
   * <p>This sorting network makes 3 comparisons. Assuming all initial orderings are equally likely,
   * the average number of swaps is 1.167. The maximum swaps is 2.
   *
   * <p>This sorting network uses the following sequence of compare-exchange operations, specified
   * by indexes beginning at 0 and consecutive (the methods of this class enable specifying the
   * indexes of the elements to sort): (0,2), (0,1), (1,2). Note that although other 3-element
   * sorting networks are possible with the same number of compare-exchange operations, only those
   * that begin with (0,2) minimize the average number of swaps.
   *
   * @param array the array to apply the compare-exchange
   * @param minTargetIndex the target index for the minimum of the three elements
   * @param medianTargetIndex the target index for the median of the three elements
   * @param maxTargetIndex the target index for the maximum of the three elements
   * @throws ArrayIndexOutOfBoundsException if any of the indexes are outside the bounds of the
   *     array
   */
  public static void sort(
      int[] array, int minTargetIndex, int medianTargetIndex, int maxTargetIndex) {
    compareExchange(array, minTargetIndex, maxTargetIndex);
    compareExchange(array, minTargetIndex, medianTargetIndex);
    compareExchange(array, medianTargetIndex, maxTargetIndex);
  }
}
