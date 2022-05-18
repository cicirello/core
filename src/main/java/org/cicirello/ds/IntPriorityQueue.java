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
 
package org.cicirello.ds;

/**
 * <p>Interface common to the classes that provide implementations of
 * a priority queue of (element, priority) pairs, such that the elements
 * are int values in the interval [0, n), and priorities are also ints.
 * All IntPriorityQueue implementations enforce distinct elements.</p>
 *
 * <p>The purpose of such a priority queue implementation is to support
 * implementations of algorithms that require such a specialized case.
 * For example, some graph algorithms such as Dijkstra's algorithm for 
 * single-source shortest paths, and Prim's algorithm for minimum spanning
 * tree, rely on a priority queue of the vertex ids, which are usually
 * ints in some finite range. Although such applications could use the
 * classes that instead implement the {@link PriorityQueue} interface,
 * using Java's wrapper type {@link Integer}, the classes that implement
 * {@link IntPriorityQueue} that specialize the element type to int
 * are optimized for this special case.</p>
 *
 * <p>For a more general purpose priority queue, see the {@link PriorityQueue}
 * interface and the classes that implement it.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public interface IntPriorityQueue {
	
	/**
	 * Changes the priority of an element if the element is
	 * present in the IntPriorityQueue, and otherwise adds the
	 * (element, priority) pair to the IntPriorityQueue.
	 *
	 * @param element The element whose priority is to change.
	 * @param priority Its new priority.
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	void change(int element, int priority);
	
	/**
	 * Clears the IntPriorityQueue, removing all elements.
	 */
	void clear();
	
	/**
	 * Checks if an element is in the IntPriorityQueue.
	 *
	 * @param element The element to check for containment.
	 *
	 * @return true if and only if there exists an (element, priority) pair
	 * for the specified element.
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	boolean contains(int element);
	
	/**
	 * Returns the domain of this IntPriorityQueue. Note that the domain
	 * is not the same thing as the size. The domain defines the elements
	 * that are allowed in the IntPriorityQueue, whether or not they actually appear within it.
	 *
	 * @return the domain of this IntPriorityQueue
	 */
	int domain();
	
	/**
	 * Checks if the IntPriorityQueue is empty.
	 *
	 * @return true if and only if it is empty
	 */
	boolean isEmpty();
	
	/**
	 * Adds an (element, priority) pair to the IntPriorityQueue with a specified priority,
	 * provided the element is not already in the IntPriorityQueue.
	 *
	 * @param element The element.
	 * @param priority The priority of the element.
	 *
	 * @return true if the (element, priority) pair was added, and false if the
	 * IntPriorityQueue already contained the element.
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	boolean offer(int element, int priority);
	
	/**
	 * Gets the next element in priority order from this IntPriorityQueue,
	 * without removing it. Behavior is undefined if it is empty.
	 *
	 * @return the next element in priority order.
	 */
	int peek();
	
	/**
	 * Gets the priority of the next element in priority order in the IntPriorityQueue.
	 * Behavior is undefined if it is empty.
	 *
	 * @return the priority of the next element in priority order.
	 */
	int peekPriority();
	
	/**
	 * Gets the current priority of a specified element in the IntPriorityQueue.
	 * Behavior is undefined if the IntPriorityQueue doesn't contain the element.
	 *
	 * @param element the element whose priority is returned
	 *
	 * @return the current priority of element.
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	int peekPriority(int element);
	
	/**
	 * Gets and removes the next element in priority order from this IntPriorityQueue.
	 * Behavior is undefined if it is empty.
	 *
	 * @return the next element in priority order.
	 */
	int poll();
	
	/**
	 * Gets the current size of the IntPriorityQueue, which is the
	 * number of (element, priority) pairs that it contains.
	 *
	 * @return the current size of the IntPriorityQueue.
	 */
	int size();
}
