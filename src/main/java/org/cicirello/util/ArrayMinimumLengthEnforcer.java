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

/**
 * Utility class for checking and enforcing that the length of an array is at least some target
 * minimum length.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class ArrayMinimumLengthEnforcer {

  /*
   * Utility class with no internal state, private constructor to prevent instantiation.
   */
  private ArrayMinimumLengthEnforcer() {}

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static int[] enforce(int[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new int[minLength];
    }
    return array;
  }

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static long[] enforce(long[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new long[minLength];
    }
    return array;
  }

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static short[] enforce(short[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new short[minLength];
    }
    return array;
  }

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static byte[] enforce(byte[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new byte[minLength];
    }
    return array;
  }

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static double[] enforce(double[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new double[minLength];
    }
    return array;
  }

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static float[] enforce(float[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new float[minLength];
    }
    return array;
  }

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static char[] enforce(char[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new char[minLength];
    }
    return array;
  }

  /**
   * Validates that an array is non-null and has a length at least that of a target minimum.
   *
   * @param array The array to check.
   * @param minLength The target minimum length.
   * @return The original array if it is non-null and length at least minLength, and otherwise
   *     returns a new array of the minLength.
   */
  public static boolean[] enforce(boolean[] array, int minLength) {
    if (array == null || array.length < minLength) {
      return new boolean[minLength];
    }
    return array;
  }
}
