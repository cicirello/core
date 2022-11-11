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
 * Utility class for checking and enforcing that an array has a length equal to a target length.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class ArrayLengthEnforcer {

  /*
   * Utility class with no internal state, private constructor to prevent instantiation.
   */
  private ArrayLengthEnforcer() {}

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static int[] enforce(int[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new int[targetLength];
    }
    return array;
  }

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static long[] enforce(long[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new long[targetLength];
    }
    return array;
  }

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static short[] enforce(short[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new short[targetLength];
    }
    return array;
  }

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static byte[] enforce(byte[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new byte[targetLength];
    }
    return array;
  }

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static double[] enforce(double[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new double[targetLength];
    }
    return array;
  }

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static float[] enforce(float[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new float[targetLength];
    }
    return array;
  }

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static char[] enforce(char[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new char[targetLength];
    }
    return array;
  }

  /**
   * Validates the length of an array and that it is non-null, checking that it is equal to a
   * specified target length.
   *
   * @param array The array to check.
   * @param targetLength The target length.
   * @return The original array if it is non-null and length equal to the targetLength, and
   *     otherwise returns a new array of the targetLength.
   */
  public static boolean[] enforce(boolean[] array, int targetLength) {
    if (array == null || array.length != targetLength) {
      return new boolean[targetLength];
    }
    return array;
  }
}
