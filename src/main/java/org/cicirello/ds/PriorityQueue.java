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
 * a priority queue. All PriorityQueue implementations enforce distinct
 * elements, and use the
 * {@link Object#hashCode} and {@link Object#equals} methods to
 * to enforce distinctness, so be sure that the class of the elements
 * properly implements these methods, or else behavior is not guaranteed.</p>
 *
 * <p>There are two nested subinterfaces, one for integer valued priority
 * values, and the other for double valued priorities.</p>
 *
 * @param <E> The type of object contained in the PriorityQueue.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public interface PriorityQueue<E> {
	
	/**
	 * Clears the PriorityQueue, removing all elements.
	 */
	void clear();
	
	/**
	 * Checks if this PriorityQueue contains a given element.
	 *
	 * @param element The element to check.
	 *
	 * @return true if and only if this PriorityQueue contains the element.
	 */
	boolean contains(E element);
	
	/**
	 * Checks if the PriorityQueue is empty.
	 *
	 * @return true if and only if it is empty
	 */
	boolean isEmpty();
	
	/**
	 * Gets the next element in priority order from this PriorityQueue,
	 * without removing it.
	 *
	 * @return the next element in priority order, or null if empty.
	 */
	E peekElement();
	
	/**
	 * Removes and returns the next element in priority order from this PriorityQueue.
	 *
	 * @return the next element in priority order, or null if empty.
	 */
	E pollElement();
	
	/**
	 * Gets the current size of the PriorityQueue, which is the
	 * number of (element, value) pairs that it contains.
	 *
	 * @return the current size of the PriorityQueue.
	 */
	int size();
	
	
	/**
	 * <p>Interface common to the classes that provide implementations of
	 * a priority queue with integer valued priorities. All PriorityQueue 
	 * implementations enforce distinct elements, and use the
	 * {@link Object#hashCode} and {@link Object#equals} methods to
	 * to enforce distinctness, so be sure that the class of the elements
	 * properly implements these methods, or else behavior is not guaranteed.</p>
	 *
	 * @param <E> The type of object contained in the PriorityQueue.
	 *
	 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
	 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
	 */
	public static interface Integer<E> extends PriorityQueue<E>, Iterable<PriorityQueueNode.Integer<E>> {
		
		/**
		 * Changes the priority of an element if the element is
		 * present in the PriorityQueue, and otherwise adds the
		 * (element, priority) pair to the PriorityQueue.
		 *
		 * @param element The element whose priority is to change.
		 * @param priority Its new priority.
		 */
		void change(E element, int priority);
		
		/**
		 * Adds an (element, priority) pair to the PriorityQueue with a specified priority,
		 * provided the element is not already in the PriorityQueue.
		 *
		 * @param element The element.
		 * @param priority The priority of the element.
		 *
		 * @return true if the (element, priority) pair was added, and false if the
		 * PriorityQueue already contained the element.
		 */
		boolean offer(E element, int priority);
		
		/**
		 * Adds an (element, priority) pair to the PriorityQueue,
		 * provided the element is not already in the PriorityQueue.
		 *
		 * @param pair The (element, priority) pair to add.
		 *
		 * @return true if the (element, priority) pair was added, and false if the
		 * PriorityQueue already contained the element.
		 */
		boolean offer(PriorityQueueNode.Integer<E> pair);
		
		/**
		 * Gets the next (element, priority) pair in priority order from this PriorityQueue,
		 * without removing it.
		 *
		 * @return the next (element, priority) pair in priority order, or null if empty.
		 */
		PriorityQueueNode.Integer<E> peek();
		
		/**
		 * Gets the priority of the next element in priority order in the PriorityQueue.
		 *
		 * @return the priority of the next element in priority order.
		 */
		int peekPriority();
		
		/**
		 * Gets the priority of a specified element if it is present in the PriorityQueue.
		 * This interface does not define the behavior when the element is not present.
		 * Implementations may define the behavior when the element is not present.
		 *
		 * @param element The element whose priority is returned.
		 *
		 * @return the priority of a specified element.
		 */
		int peekPriority(E element);
		
		/**
		 * Removes and returns the next (element, priority) pair in priority order from this PriorityQueue.
		 *
		 * @return the next (element, priority) pair in priority order, or null if empty.
		 */
		PriorityQueueNode.Integer<E> poll();
	}
	
	/**
	 * <p>Interface common to the classes that provide implementations of
	 * a priority queue with double valued priorities. All PriorityQueue 
	 * implementations enforce distinct elements, and use the
	 * {@link Object#hashCode} and {@link Object#equals} methods to
	 * to enforce distinctness, so be sure that the class of the elements
	 * properly implements these methods, or else behavior is not guaranteed.</p>
	 *
	 * @param <E> The type of object contained in the PriorityQueue.
	 *
	 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
	 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
	 */
	public static interface Double<E> extends PriorityQueue<E>, Iterable<PriorityQueueNode.Double<E>> {
		
		/**
		 * Changes the priority of an element if the element is
		 * present in the PriorityQueue, and otherwise adds the
		 * (element, priority) pair to the PriorityQueue.
		 *
		 * @param element The element whose priority is to change.
		 * @param priority Its new priority.
		 */
		void change(E element, double priority);
		
		/**
		 * Adds an (element, priority) pair to the PriorityQueue with a specified priority,
		 * provided the element is not already in the PriorityQueue.
		 *
		 * @param element The element.
		 * @param priority The priority of the element.
		 *
		 * @return true if the (element, priority) pair was added, and false if the
		 * PriorityQueue already contained the element.
		 */
		boolean offer(E element, double priority);
		
		/**
		 * Adds an (element, priority) pair to the PriorityQueue,
		 * provided the element is not already in the PriorityQueue.
		 *
		 * @param pair The (element, priority) pair to add.
		 *
		 * @return true if the (element, priority) pair was added, and false if the
		 * PriorityQueue already contained the element.
		 */
		boolean offer(PriorityQueueNode.Double<E> pair);
		
		/**
		 * Gets the next (element, priority) pair in priority order from this PriorityQueue,
		 * without removing it.
		 *
		 * @return the next (element, priority) pair in priority order, or null if empty.
		 */
		PriorityQueueNode.Double<E> peek();
		
		/**
		 * Gets the priority of the next element in priority order in the PriorityQueue.
		 *
		 * @return the priority of the next element in priority order.
		 */
		double peekPriority();
		
		/**
		 * Gets the priority of a specified element if it is present in the PriorityQueue.
		 * This interface does not define the behavior when the element is not present.
		 * Implementations may define the behavior when the element is not present.
		 *
		 * @param element The element whose priority is returned.
		 *
		 * @return the priority of a specified element.
		 */
		double peekPriority(E element);
		
		/**
		 * Removes and returns the next (element, priority) pair in priority order from this PriorityQueue.
		 *
		 * @return the next (element, priority) pair in priority order, or null if empty.
		 */
		PriorityQueueNode.Double<E> poll();
	}
}
