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

package org.cicirello.ds;

/**
 * Interface common to the classes that provide implementations of a priority queue of (element,
 * priority) pairs, such that the elements are int values in the interval [0, n), and priorities are
 * doubles. All IntPriorityQueueDouble implementations enforce distinct elements.
 *
 * <p>The purpose of such a priority queue implementation is to support implementations of
 * algorithms that require such a specialized case. For example, some graph algorithms such as
 * Dijkstra's algorithm for single-source shortest paths, and Prim's algorithm for minimum spanning
 * tree, rely on a priority queue of the vertex ids, which are usually ints in some finite range.
 * Although such applications could use the classes that instead implement the {@link
 * PriorityQueueDouble} interface, using Java's wrapper type {@link Integer}, the classes that
 * implement {@link IntPriorityQueueDouble} that specialize the element type to int are optimized
 * for this special case.
 *
 * <p>For a more general purpose priority queue, see the {@link PriorityQueueDouble} interface and
 * the classes that implement it.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public interface IntPriorityQueueDouble {

  /**
   * Changes the priority of an element if the element is present in the IntPriorityQueueDouble, and
   * otherwise adds the (element, priority) pair to the IntPriorityQueueDouble.
   *
   * @param element The element whose priority is to change.
   * @param priority Its new priority.
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   * @return true if and only if the IntPriorityQueueDouble changed as a consequence of this method
   *     call.
   */
  boolean change(int element, double priority);

  /** Clears the IntPriorityQueueDouble, removing all elements. */
  void clear();

  /**
   * Checks if an element is in the IntPriorityQueueDouble.
   *
   * @param element The element to check for containment.
   * @return true if and only if there exists an (element, priority) pair for the specified element.
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  boolean contains(int element);

  /**
   * Demotes an element relative to priority order if the element is present in the
   * IntPriorityQueueDouble. For a min-heap, demotion means increasing the element's priority, while
   * for a max-heap, demotion means decreasing its priority. If the element is not in the
   * IntPriorityQueueDouble, or if its new priority is not a demotion, then this method does
   * nothing.
   *
   * @param element The element whose priority is to change.
   * @param priority Its new priority.
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   * @return true if and only if the IntPriorityQueueDouble changed as a consequence of this method
   *     call.
   */
  boolean demote(int element, double priority);

  /**
   * Returns the domain of this IntPriorityQueueDouble. Note that the domain is not the same thing
   * as the size. The domain defines the elements that are allowed in the IntPriorityQueueDouble,
   * whether or not they actually appear within it.
   *
   * @return the domain of this IntPriorityQueueDouble
   */
  int domain();

  /**
   * Checks if the IntPriorityQueueDouble is empty.
   *
   * @return true if and only if it is empty
   */
  boolean isEmpty();

  /**
   * Adds an (element, priority) pair to the IntPriorityQueueDouble with a specified priority,
   * provided the element is not already in the IntPriorityQueueDouble.
   *
   * @param element The element.
   * @param priority The priority of the element.
   * @return true if the (element, priority) pair was added, and false if the IntPriorityQueueDouble
   *     already contained the element.
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  boolean offer(int element, double priority);

  /**
   * Gets the next element in priority order from this IntPriorityQueueDouble, without removing it.
   * Behavior is undefined if it is empty.
   *
   * @return the next element in priority order.
   */
  int peek();

  /**
   * Gets the priority of the next element in priority order in the IntPriorityQueueDouble. Behavior
   * is undefined if it is empty.
   *
   * @return the priority of the next element in priority order.
   */
  double peekPriority();

  /**
   * Gets the current priority of a specified element in the IntPriorityQueueDouble. Behavior is
   * undefined if the IntPriorityQueueDouble doesn't contain the element.
   *
   * @param element the element whose priority is returned
   * @return the current priority of element.
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  double peekPriority(int element);

  /**
   * Gets and removes the next element in priority order from this IntPriorityQueueDouble. Behavior
   * is undefined if it is empty.
   *
   * @return the next element in priority order.
   */
  int poll();

  /**
   * Promotes an element relative to priority order if the element is present in the
   * IntPriorityQueueDouble. For a min-heap, promotion means decreasing the element's priority,
   * while for a max-heap, promotion means increasing its priority. If the element is not in the
   * IntPriorityQueueDouble, or if its new priority is not a promotion, then this method does
   * nothing.
   *
   * @param element The element whose priority is to change.
   * @param priority Its new priority.
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   * @return true if and only if the IntPriorityQueueDouble changed as a consequence of this method
   *     call.
   */
  boolean promote(int element, double priority);

  /**
   * Gets the current size of the IntPriorityQueueDouble, which is the number of (element, priority)
   * pairs that it contains.
   *
   * @return the current size of the IntPriorityQueueDouble.
   */
  int size();
}
