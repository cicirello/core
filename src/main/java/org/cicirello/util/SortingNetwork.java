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
   * of order.
   *
   * @param array the array to apply the compare-exchange
   * @param minTargetIndex the target index for the minimum of the two elements
   * @param maxTargetIndex the target index for the maximum of the two elements
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
   * of order.
   *
   * @param array the array to apply the compare-exchange
   * @param minTargetIndex the target index for the minimum of the two elements
   * @param maxTargetIndex the target index for the maximum of the two elements
   */
  public static void compareExchange(int[] array, int minTargetIndex, int maxTargetIndex) {
    if (array[minTargetIndex] > array[maxTargetIndex]) {
      int temp = array[minTargetIndex];
      array[minTargetIndex] = array[maxTargetIndex];
      array[maxTargetIndex] = temp;
    }
  }
}
