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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

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
public interface PriorityQueueDouble<E> extends Queue<PriorityQueueNode.Double<E>> {
	
	/**
	 * Adds an (element, priority) pair to the PriorityQueue with a specified priority,
	 * provided the element is not already in the PriorityQueue.
	 * This method differs from {@link #offer(Object, double)}
	 * in that it throws an exception if the PriorityQueue contains the element,
	 * while the offer method instead returns false.
	 *
	 * @param element The element.
	 * @param priority The priority of the element.
	 *
	 * @return true if the (element, priority) pair was added.
	 *
	 * @throws IllegalArgumentException if the priority queue already contains the element.
	 */
	default boolean add(E element, double priority) {
		if (!offer(element, priority)) {
			throw new IllegalArgumentException("already contains an (element, priority) pair with this element");
		}
		return true;
	}
	
	/**
	 * Adds an (element, priority) pair to the PriorityQueue,
	 * provided the element is not already in the PriorityQueue.
	 * This method differs from {@link #offer(PriorityQueueNode.Double)}
	 * in that it throws an exception if the PriorityQueue contains the element,
	 * while the offer method instead returns false.
	 *
	 * @param pair The (element, priority) pair to add.
	 *
	 * @return true if the (element, priority) pair was added.
	 *
	 * @throws IllegalArgumentException if the priority queue already contains the element.
	 */
	@Override
	default boolean add(PriorityQueueNode.Double<E> pair) {
		if (!offer(pair)) {
			throw new IllegalArgumentException("already contains an (element, priority) pair with this element");
		}
		return true;
	}
	
	/**
	 * Adds all (element, priority) pairs from a Collection to the PriorityQueue,
	 * provided the elements are not already in the PriorityQueue.
	 * The default implementation calls the {@link #add(PriorityQueueNode.Double)} 
	 * for each pair in the Collection. 
	 *
	 * @param c the Collection of (element, priority) pairs to add.
	 *
	 * @return true if the (element, priority) pairs were added.
	 *
	 * @throws IllegalArgumentException if the PriorityQueue already contains any
	 * of the (element, priority) pairs.
	 */
	@Override
	default boolean addAll(Collection<? extends PriorityQueueNode.Double<E>> c) {
		boolean changed = false;
		for (PriorityQueueNode.Double<E> e : c) {
changed = add(e) || changed;
		}
		return changed;
	}
	
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
	 * Clears the PriorityQueue, removing all elements.
	 */
	@Override
	void clear();
	
	/**
	 * Checks if this PriorityQueue contains a given element
	 * or an (element, priority) pair with a given element.
	 *
	 * @param o An element or (element, priority) pair to check
	 *    for containment of the element.
	 *
	 * @return true if and only if this PriorityQueue contains the element.
	 */
	@Override
	boolean contains(Object o);
	
	/**
	 * Checks if this PriorityQueue contains all elements
	 * or (element, priority) pairs from a given Collection.
	 *
	 * @param c A Collection of elements or (element, priority) pairs to check
	 *    for containment.
	 *
	 * @return true if and only if this PriorityQueue contains all of the elements
	 * or (element, priority) pairs in c.
	 */
	@Override
	default boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) return false;
		}
		return true;
	}
	
	/**
	 * <p>Gets the next (element, priority) pair in priority order from this PriorityQueue,
	 * without removing it.</p>
	 * <p>This method differs from {@link #peek()} in that if the PriorityQueue is
	 * empty, this method throws an exception, while {@link #peek()} returns null.</p>
	 * <p>This method serves a different purpose than {@link peekElement()}. The
	 * {@link peekElement()} methods returns only the element of the (element, priority)
	 * pair, while this method returns the (element, priority) pair. This element() method
	 * is included only for full implementation of the superinterface {@link java.util.Queue Queue}.</p>
	 *
	 * @return the next (element, priority) pair in priority order.
	 *
	 * @throws NoSuchElementException if the PriorityQueue is empty
	 */
	@Override
	default PriorityQueueNode.Double<E> element() {
		PriorityQueueNode.Double<E> result = peek();
		if (result == null) {
			throw new NoSuchElementException("PriorityQueue is empty");
		}
		return result;
	}
	
	/**
	 * Checks if the PriorityQueue is empty.
	 *
	 * @return true if and only if it is empty
	 */
	@Override
	boolean isEmpty();
	
	/**
	 * Returns an iterator over the (element, priority) pairs in a
	 * mostly arbitrary order (i.e., you must not assume any particular
	 * order).
	 *
	 * @return an iterator over the (element, priority) pairs
	 */
	@Override
	Iterator<PriorityQueueNode.Double<E>> iterator();
	
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
	@Override
	boolean offer(PriorityQueueNode.Double<E> pair);
	
	/**
	 * Gets the next (element, priority) pair in priority order from this PriorityQueue,
	 * without removing it.
	 *
	 * @return the next (element, priority) pair in priority order, or null if empty.
	 */
	@Override
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
	 * Gets the next element in priority order from this PriorityQueue,
	 * without removing it.
	 *
	 * @return the next element in priority order, or null if empty.
	 */
	E peekElement();
	
	/**
	 * Removes and returns the next (element, priority) pair in priority order from this PriorityQueue.
	 *
	 * @return the next (element, priority) pair in priority order, or null if empty.
	 */
	@Override
	PriorityQueueNode.Double<E> poll();
	
	/**
	 * Removes and returns the next element in priority order from this PriorityQueue.
	 *
	 * @return the next element in priority order, or null if empty.
	 */
	E pollElement();
	
	/**
	 * Removes and returns the next (element, priority) pair in priority order from this PriorityQueue.
	 * This method differs from {@link #poll()} in that if the PriorityQueue is
	 * empty, this method throws an exception, while {@link #poll()} returns null.
	 *
	 * @return the next (element, priority) pair in priority order.
	 *
	 * @throws NoSuchElementException if the PriorityQueue is empty
	 */
	@Override
	default PriorityQueueNode.Double<E> remove() {
		PriorityQueueNode.Double<E> result = poll();
		if (result == null) {
			throw new NoSuchElementException("PriorityQueue is empty");
		}
		return result;
	}
	
	/**
	 * Removes from this PriorityQueue the (element, priority) pair, if present, 
	 * for a specified element or element from a specified (element, priority) pair.
	 *
	 * @param o An element or (element, priority) pair, such that element designates
	 * the desired pair to remove (note that if you pass an (element, priority) pair,
	 * only the element must match to cause removal.
	 *
	 * @return true if and only if an (element, priority) pair was removed as a result
	 * of this method call.
	 */
	@Override
	boolean remove(Object o);
	
	/**
	 * Removes from this PriorityQueue all (element, priority) pairs
	 * such that a given Collection c either contains the element or
	 * contains an (element, priority) pair with the same element.
	 *
	 * @param c A Collection of elements or (element, priority) pairs for removal.
	 *
	 * @return true if and only if this PriorityQueue changed as a result of this method.
	 */
	@Override
	default boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
changed = remove(o) || changed;
		}
		return changed;
	}
	
	/**
	 * Removes and returns the next element in priority order from this PriorityQueue.
	 * This method differs from {@link #pollElement()} in that if the PriorityQueue is
	 * empty, this method throws an exception, while {@link #pollElement()} returns null.
	 *
	 * @return the next element in priority order.
	 *
	 * @throws NoSuchElementException if the PriorityQueue is empty
	 */
	default E removeElement() {
		E result = pollElement();
		if (result == null) {
			throw new NoSuchElementException("PriorityQueue is empty");
		}
		return result;
	}
	
	/**
	 * Removes from this PriorityQueue all (element, priority) pairs
	 * except for the elements or (element, priority) pairs contained in a 
	 * given Collection c.
	 *
	 * @param c A Collection of elements or (element, priority) pairs to keep.
	 *
	 * @return true if and only if this PriorityQueue changed as a result of this method.
	 */
	@Override
	boolean retainAll(Collection<?> c);
	
	/**
	 * Gets the current size of the PriorityQueue, which is the
	 * number of (element, value) pairs that it contains.
	 *
	 * @return the current size of the PriorityQueue.
	 */
	@Override
	int size();
	
	/**
	 * Returns an array containing all of the (element, priority) pairs contained in the
	 * PriorityQueue. The order is not guaranteed. The runtime component type is Object.
	 * The PriorityQueue does not maintain any references to the array that is returned,
	 * instead creating a new array upon each call to the toArray method. The length of the
	 * array that is returned is equal to the current {@link #size()} of the PriorityQueue.
	 *
	 * @return an array, whose runtime component type is Object, containing all of the 
	 * (element, priority) pairs currently in the PriorityQueue.
	 */
	@Override
	Object[] toArray();
	
	/**
	 * Returns an array containing all of the (element, priority) pairs contained in the
	 * PriorityQueue. The order is not guaranteed. The runtime component type is the same
	 * as the array passed to it as a parameter. If the specified array is large enough,
	 * then it is used, otherwise a new array is allocated whose length is equal to 
	 * the current {@link #size()} of the PriorityQueue. If the specified array is larger
	 * than the current size() of the PriorityQueue, the first extra cell is set to null.
	 *
	 * @param array The array in which to place the (element, priority) pairs, if it is
	 * sufficiently large, otherwise a new array of length {@link #size()} is allocated of
	 * the same runtime type as array.
	 *
	 * @param <T> The component type of the array to contain the (element, priority) pairs
	 *
	 * @return The array in which the (element, priority) pairs have been inserted.
	 *
	 * @throws ArrayStoreException if the runtime component type of array is not
	 * compatible with the type of the (element, priority) pairs.
	 *
	 * @throws NullPointerException if array is null
	 */
	@Override
	<T> T[] toArray(T[] array);
}
