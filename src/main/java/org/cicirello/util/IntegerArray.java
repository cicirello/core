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

import java.util.Arrays;
import org.cicirello.util.internal.SuppressFBWarnings;

/**
 * An object of this class wraps an array of primitive ints in such a way as to provide some
 * ArrayList-like operations for a primitive array (e.g., using it as a partially-filled array). It
 * doesn't support growable operations, instead keeping array length fixed. An instance of this
 * class does not strongly encapsulate the array. If initialized from an existing array, it
 * maintains a reference to that array, and regardless also provides an {@link #array} method that
 * exposes a reference to the array.
 *
 * <p>For a strongly encapsulated partially-filled array of primitive ints, including growability,
 * see the {@link IntegerList} class.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class IntegerArray {

  private final int[] array;
  private int size;

  /**
   * Initializes an empty IntegerArray, with a specified length for the internal array. By empty, we
   * mean size() equals 0.
   *
   * @param length the length of the array within the IntegerArray
   * @throws NegativeArraySizeException if length is negative
   */
  public IntegerArray(int length) {
    array = new int[length];
  }

  /**
   * Initializes an IntegerArray so as to wrap an existing array. A reference to this array is
   * stored in the IntegerArray object, and thus changes to the array externally are observable
   * within the IntegerArray; and likewise changes via the methods of this class are observable
   * directly via the array reference.
   *
   * <p>The size() of this IntegerArray is 0, and doesn't depend upon the length of array. However,
   * the length() of this IntegerArray equals array.length.
   *
   * @param array the array to wrap
   */
  @SuppressFBWarnings(value = "EI_EXPOSE_REP")
  public IntegerArray(int[] array) {
    this.array = array;
  }

  /**
   * Adds a new element, provided size() is less than length(); and otherwise throws an exception.
   *
   * @param element The value to add.
   * @throws IndexOutOfBoundsException if size() is equal to length()
   */
  public void add(int element) {
    array[size] = element;
    size++;
  }

  /**
   * Inserts a new element at a specified index, provided size() is less than length(); and
   * otherwise throws an exception.
   *
   * @param index The index for the insertion.
   * @param element The value to add.
   * @throws IndexOutOfBoundsException if size() is equal to length() or if index is greater than
   *     size()
   */
  public void add(int index, int element) {
    if (index > size) {
      throw new IndexOutOfBoundsException("index is out of bounds");
    }
    System.arraycopy(array, index, array, index + 1, array.length - index - 1);
    array[index] = element;
    size++;
  }

  /**
   * Exposes a reference to the wrapped array.
   *
   * @return a reference to the wrapped array
   */
  @SuppressFBWarnings(value = "EI_EXPOSE_REP")
  public int[] array() {
    return array;
  }

  /**
   * Logically clears the array. That is, sets size() to 0, but the contents of the array is
   * otherise untouched.
   */
  public void clear() {
    size = 0;
  }

  /**
   * Checks if the array contains a value within the first size() elements.
   *
   * @param element The element to search for.
   * @return true if and only if the array contains at least one copy of element within the first
   *     size() elements.
   */
  public boolean contains(int element) {
    for (int i = 0; i < size; i++) {
      if (array[i] == element) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the element at a specified index.
   *
   * @param index The index of the desired element.
   * @return The element at the specified index.
   * @throws IndexOutOfBoundsException if the index is out of bounds, such that index is negative or
   *     index is greater than or equal to size().
   */
  public int get(int index) {
    if (index < size) {
      return array[index];
    }
    throw new IndexOutOfBoundsException("index is out of bounds");
  }

  /**
   * Gets the index of the first occurrence of an element from the left, provided it appears within
   * the first size() elements.
   *
   * @param element The element searched for.
   * @return The index of the first occurrence of the element in the array from the left, or -1 if
   *     the element doesn't exist within the first size() elements.
   */
  public int indexOf(int element) {
    for (int i = 0; i < size; i++) {
      if (array[i] == element) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Checks if the array is logically empty.
   *
   * @return true if and only if size() equals 0.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Gets the index of the last occurrence of an element, provided it appears within the first
   * size() elements.
   *
   * @param element The element searched for.
   * @return The index of the last occurrence of the element in the array within the first size()
   *     elements, or -1 if the element doesn't exist.
   */
  public int lastIndexOf(int element) {
    for (int i = size - 1; i >= 0; i--) {
      if (array[i] == element) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Gets the length of the wrapped array.
   *
   * @return the length of the wrapped array
   */
  public int length() {
    return array.length;
  }

  /**
   * Removes the element at a specified index.
   *
   * @param index The index of the element to remove.
   * @return The element that was removed.
   * @throws IndexOutOfBoundsException if the index is logically out of bounds, such that index is
   *     negative or index is greater than or equal to size().
   */
  public int remove(int index) {
    if (index < size) {
      int element = array[index];
      size--;
      System.arraycopy(array, index + 1, array, index, array.length - index - 1);
      return element;
    }
    throw new IndexOutOfBoundsException("index is out of bounds");
  }

  /**
   * Sets an element at a specified index, replacing any value that is currently there.
   *
   * @param index The index.
   * @param element The new element to set at that index.
   * @throws IndexOutOfBoundsException if the index is logically out of bounds, such that index is
   *     negative or index is greater than or equal to size().
   */
  public void set(int index, int element) {
    if (index < size) {
      array[index] = element;
    } else {
      throw new IndexOutOfBoundsException("index is out of bounds");
    }
  }

  /**
   * Gets the size of the array (i.e., the number of elements added via the {@link #add} methods).
   *
   * @return The size of the array, which is the number of elements currently within it.
   */
  public int size() {
    return size;
  }

  /** Sorts the first size() elements of the array into ascending order. */
  public void sort() {
    Arrays.sort(array, 0, size);
  }

  /**
   * Checks if this IntegerArray is equal to another IntegerArray, such that they wrap the same
   * array instance and are of the same size().
   *
   * @param other The other IntegerArray
   * @return true if of the same size() and wraps the same array instances.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) return false;
    if (other instanceof IntegerArray) {
      IntegerArray o = (IntegerArray) other;
      return size == o.size && array == o.array;
    }
    return false;
  }

  /**
   * Computes a hashCode for the IntegerArray.
   *
   * @return a hashCode
   */
  @Override
  public int hashCode() {
    int h = 0;
    for (int i = 0; i < size; i++) {
      h = (31 * h) + array[i];
    }
    return h;
  }

  /**
   * Generate a String representation of the IntegerArray.
   *
   * @return A String representation of the IntegerArray.
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("[");
    if (size > 0) {
      s.append(array[0]);
      for (int i = 1; i < size; i++) {
        s.append(", ");
        s.append(array[i]);
      }
    }
    s.append("]");
    return s.toString();
  }
}
