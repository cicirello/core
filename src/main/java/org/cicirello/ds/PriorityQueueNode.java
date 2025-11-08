/*
 * Module org.cicirello.core
 * Copyright 2019-2025 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

package org.cicirello.ds;

import org.cicirello.util.Copyable;

/**
 * An instances of the nested subclasses of this class encapsulate an (element, priority) pair for
 * use by the various priority queue classes of the library.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class PriorityQueueNode {

  private PriorityQueueNode() {}

  /**
   * An instance of this class encapsulates an (element, priority) pair for an integer valued
   * priority. This class is used by the various priority queue classes of the library.
   *
   * @param <E> The type of element contained in the PriorityQueueNode.
   * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
   *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
   */
  @SuppressWarnings("JavaLangClash")
  public static final class Integer<E> implements Copyable<Integer<E>> {

    // package-private on purpose for use by
    // priority queue classes in the package.
    int value;
    final E element;

    /**
     * Initializes the PriorityQueueNode.Integer.
     *
     * @param element The object.
     * @param value The value of the object.
     */
    public Integer(E element, int value) {
      this.element = element;
      this.value = value;
    }

    /**
     * {@inheritDoc}
     *
     * <p>During the copy, if the element is of a type that also implements {@link Copyable}, then
     * the element is copied. Otherwise, the copy will contain a reference to the same element as
     * the PriorityQueueNode that was copied.
     */
    @Override
    public Integer<E> copy() {
      if (element instanceof Copyable) {
        Copyable copyable = (Copyable) element;
        @SuppressWarnings("unchecked")
        E casted = (E) copyable.copy();
        return new Integer<E>(casted, value);
      } else {
        return new Integer<E>(element, value);
      }
    }

    /**
     * Checks if another PriorityQueueNode.Integer is equal to this one.
     *
     * @param other The other PriorityQueueNode.Integer.
     * @return true if and only if they contain an identical object, determined by the equals method
     *     of the encapsulated object, as well as the same priority.
     */
    @Override
    public boolean equals(Object other) {
      if (other == null) return false;
      if (other instanceof PriorityQueueNode.Integer) {
        @SuppressWarnings("unchecked")
        PriorityQueueNode.Integer<E> casted = (PriorityQueueNode.Integer<E>) other;
        return value == casted.value && element.equals(casted.element);
      }
      return false;
    }

    /**
     * Gets the encapsulated object.
     *
     * @return The object within the pair.
     */
    public final E getElement() {
      return element;
    }

    /**
     * Computes the hashCode of the PriorityQueueNode.Integer.
     *
     * @return a hashCode
     */
    @Override
    public int hashCode() {
      return element.hashCode() * 31 + value;
    }

    /**
     * Gets the priority value.
     *
     * @return the priority value.
     */
    public int getPriority() {
      return value;
    }
  }

  /**
   * An instance of this class encapsulates an (element, priority) pair for a double valued
   * priority. This class is used by the various priority queue classes of the library.
   *
   * @param <E> The type of element contained in the PriorityQueueNode.
   * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
   *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
   */
  @SuppressWarnings("JavaLangClash")
  public static final class Double<E> implements Copyable<Double<E>> {

    // package-private on purpose for use by
    // priority queue classes in the package.
    double value;
    final E element;

    /**
     * Initializes the PriorityQueueNode.Double.
     *
     * @param element The object.
     * @param value The value of the object.
     */
    public Double(E element, double value) {
      this.element = element;
      this.value = value;
    }

    /**
     * {@inheritDoc}
     *
     * <p>During the copy, if the element is of a type that also implements {@link Copyable}, then
     * the element is copied. Otherwise, the copy will contain a reference to the same element as
     * the PriorityQueueNode that was copied.
     */
    @Override
    public Double<E> copy() {
      if (element instanceof Copyable) {
        Copyable copyable = (Copyable) element;
        @SuppressWarnings("unchecked")
        E casted = (E) copyable.copy();
        return new Double<E>(casted, value);
      } else {
        return new Double<E>(element, value);
      }
    }

    /**
     * Checks if another PriorityQueueNode.Double is equal to this one.
     *
     * @param other The other PriorityQueueNode.Double.
     * @return true if and only if they contain an identical object, determined by the equals method
     *     of the encapsulated object, as well as the same priority.
     */
    @Override
    public boolean equals(Object other) {
      if (other == null) return false;
      if (other instanceof PriorityQueueNode.Double) {
        @SuppressWarnings("unchecked")
        PriorityQueueNode.Double<E> casted = (PriorityQueueNode.Double<E>) other;
        return value == casted.value && element.equals(casted.element);
      }
      return false;
    }

    /**
     * Gets the encapsulated object.
     *
     * @return The object within the pair.
     */
    public final E getElement() {
      return element;
    }

    /**
     * Computes the hashCode of the PriorityQueueNode.Double.
     *
     * @return a hashCode
     */
    @Override
    public int hashCode() {
      return element.hashCode() * 31 + java.lang.Double.hashCode(value);
    }

    /**
     * Gets the priority value.
     *
     * @return the priority value.
     */
    public double getPriority() {
      return value;
    }
  }
}
