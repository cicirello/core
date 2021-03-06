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
 * a priority queue with double valued priorities. All PriorityQueueDouble 
 * implementations enforce distinct elements, and use the
 * {@link Object#hashCode} and {@link Object#equals} methods to
 * to enforce distinctness, so be sure that the class of the elements
 * properly implements these methods, or else behavior is not guaranteed.</p>
 *
 * @param <E> The type of object contained in the PriorityQueueDouble.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public interface PriorityQueueDouble<E> extends Queue<PriorityQueueNode.Double<E>> {
	
	/**
	 * Adds an (element, priority) pair to the PriorityQueueDouble with a specified priority,
	 * provided the element is not already in the PriorityQueueDouble.
	 * This method differs from {@link #offer(Object, double)}
	 * in that it throws an exception if the PriorityQueueDouble contains the element,
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
	 * Adds an (element, priority) pair to the PriorityQueueDouble,
	 * provided the element is not already in the PriorityQueueDouble.
	 * This method differs from {@link #offer(PriorityQueueNode.Double)}
	 * in that it throws an exception if the PriorityQueueDouble contains the element,
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
	 * Adds all (element, priority) pairs from a Collection to the PriorityQueueDouble,
	 * provided the elements are not already in the PriorityQueueDouble.
	 * The default implementation calls the {@link #add(PriorityQueueNode.Double)} 
	 * for each pair in the Collection. 
	 *
	 * @param c the Collection of (element, priority) pairs to add.
	 *
	 * @return true if the (element, priority) pairs were added.
	 *
	 * @throws IllegalArgumentException if the PriorityQueueDouble already contains any
	 * of the (element, priority) pairs.
	 */
	@Override
	default boolean addAll(Collection<? extends PriorityQueueNode.Double<E>> c) {
		boolean changed = false;
		for (PriorityQueueNode.Double<E> e : c) {
			add(e);
			changed = true;
		}
		return changed;
	}
	
	/**
	 * Changes the priority of an element if the element is
	 * present in the PriorityQueueDouble, and otherwise adds the
	 * (element, priority) pair to the PriorityQueueDouble.
	 *
	 * @param element The element whose priority is to change.
	 * @param priority Its new priority.
	 *
	 * @return true if and only if the PriorityQueueDouble changed
	 * as a consequence of this method call.
	 */
	boolean change(E element, double priority);
	
	/**
	 * Clears the PriorityQueueDouble, removing all elements.
	 */
	@Override
	void clear();
	
	/**
	 * Checks if this PriorityQueueDouble contains a given element
	 * or an (element, priority) pair with a given element.
	 *
	 * @param o An element or (element, priority) pair to check
	 *    for containment of the element.
	 *
	 * @return true if and only if this PriorityQueueDouble contains the element.
	 */
	@Override
	boolean contains(Object o);
	
	/**
	 * Checks if this PriorityQueueDouble contains all elements
	 * or (element, priority) pairs from a given Collection.
	 *
	 * @param c A Collection of elements or (element, priority) pairs to check
	 *    for containment.
	 *
	 * @return true if and only if this PriorityQueueDouble contains all of the elements
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
	 * Demotes an element relative to priority order if the element is
	 * present in the PriorityQueueDouble. For a min-heap, demotion means
	 * increasing the element's priority, while for a max-heap, demotion
	 * means decreasing its priority. If the element is not in the PriorityQueueDouble,
	 * or if its new priority is not a demotion, then this method does nothing.
	 *
	 * @param element The element whose priority is to change.
	 * @param priority Its new priority.
	 *
	 * @return true if and only if the PriorityQueueDouble changed
	 * as a consequence of this method call.
	 */
	boolean demote(E element, double priority);
	
	/**
	 * <p>Gets the next (element, priority) pair in priority order from this PriorityQueueDouble,
	 * without removing it.</p>
	 * <p>This method differs from {@link #peek()} in that if the PriorityQueueDouble is
	 * empty, this method throws an exception, while {@link #peek()} returns null.</p>
	 * <p>This method serves a different purpose than {@link peekElement()}. The
	 * {@link peekElement()} methods returns only the element of the (element, priority)
	 * pair, while this method returns the (element, priority) pair. This element() method
	 * is included only for full implementation of the superinterface {@link java.util.Queue Queue}.</p>
	 *
	 * @return the next (element, priority) pair in priority order.
	 *
	 * @throws NoSuchElementException if the PriorityQueueDouble is empty
	 */
	@Override
	default PriorityQueueNode.Double<E> element() {
		PriorityQueueNode.Double<E> result = peek();
		if (result == null) {
			throw new NoSuchElementException("PriorityQueueDouble is empty");
		}
		return result;
	}
	
	/**
	 * Checks if the PriorityQueueDouble is empty.
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
	 * Adds an (element, priority) pair to the PriorityQueueDouble with a specified priority,
	 * provided the element is not already in the PriorityQueueDouble.
	 *
	 * @param element The element.
	 * @param priority The priority of the element.
	 *
	 * @return true if the (element, priority) pair was added, and false if the
	 * PriorityQueueDouble already contained the element.
	 */
	boolean offer(E element, double priority);
	
	/**
	 * Adds an (element, priority) pair to the PriorityQueueDouble,
	 * provided the element is not already in the PriorityQueueDouble.
	 *
	 * @param pair The (element, priority) pair to add.
	 *
	 * @return true if the (element, priority) pair was added, and false if the
	 * PriorityQueueDouble already contained the element.
	 */
	@Override
	boolean offer(PriorityQueueNode.Double<E> pair);
	
	/**
	 * Gets the next (element, priority) pair in priority order from this PriorityQueueDouble,
	 * without removing it.
	 *
	 * @return the next (element, priority) pair in priority order, or null if empty.
	 */
	@Override
	PriorityQueueNode.Double<E> peek();
	
	/**
	 * Gets the priority of the next element in priority order in the PriorityQueueDouble.
	 *
	 * @return the priority of the next element in priority order.
	 */
	double peekPriority();
	
	/**
	 * Gets the priority of a specified element if it is present in the PriorityQueueDouble.
	 * This interface does not define the behavior when the element is not present.
	 * Implementations may define the behavior when the element is not present.
	 *
	 * @param element The element whose priority is returned.
	 *
	 * @return the priority of a specified element.
	 */
	double peekPriority(E element);
	
	/**
	 * Gets the next element in priority order from this PriorityQueueDouble,
	 * without removing it.
	 *
	 * @return the next element in priority order, or null if empty.
	 */
	E peekElement();
	
	/**
	 * Removes and returns the next (element, priority) pair in priority order from this PriorityQueueDouble.
	 *
	 * @return the next (element, priority) pair in priority order, or null if empty.
	 */
	@Override
	PriorityQueueNode.Double<E> poll();
	
	/**
	 * Removes and returns the next element in priority order from this PriorityQueueDouble.
	 *
	 * @return the next element in priority order, or null if empty.
	 */
	E pollElement();
	
	/**
	 * Promotes an element relative to priority order if the element is
	 * present in the PriorityQueueDouble. For a min-heap, promotion means
	 * decreasing the element's priority, while for a max-heap, promotion
	 * means increasing its priority. If the element is not in the PriorityQueueDouble,
	 * or if its new priority is not a promotion, then this method does nothing.
	 *
	 * @param element The element whose priority is to change.
	 * @param priority Its new priority.
	 *
	 * @return true if and only if the PriorityQueueDouble changed
	 * as a consequence of this method call.
	 */
	boolean promote(E element, double priority);
	
	/**
	 * Removes and returns the next (element, priority) pair in priority order from this PriorityQueueDouble.
	 * This method differs from {@link #poll()} in that if the PriorityQueueDouble is
	 * empty, this method throws an exception, while {@link #poll()} returns null.
	 *
	 * @return the next (element, priority) pair in priority order.
	 *
	 * @throws NoSuchElementException if the PriorityQueueDouble is empty
	 */
	@Override
	default PriorityQueueNode.Double<E> remove() {
		PriorityQueueNode.Double<E> result = poll();
		if (result == null) {
			throw new NoSuchElementException("PriorityQueueDouble is empty");
		}
		return result;
	}
	
	/**
	 * Removes from this PriorityQueueDouble the (element, priority) pair, if present, 
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
	 * Removes from this PriorityQueueDouble all (element, priority) pairs
	 * such that a given Collection c either contains the element or
	 * contains an (element, priority) pair with the same element.
	 *
	 * @param c A Collection of elements or (element, priority) pairs for removal.
	 *
	 * @return true if and only if this PriorityQueueDouble changed as a result of this method.
	 */
	@Override
	boolean removeAll(Collection<?> c);
	
	/**
	 * Removes and returns the next element in priority order from this PriorityQueueDouble.
	 * This method differs from {@link #pollElement()} in that if the PriorityQueueDouble is
	 * empty, this method throws an exception, while {@link #pollElement()} returns null.
	 *
	 * @return the next element in priority order.
	 *
	 * @throws NoSuchElementException if the PriorityQueueDouble is empty
	 */
	default E removeElement() {
		E result = pollElement();
		if (result == null) {
			throw new NoSuchElementException("PriorityQueueDouble is empty");
		}
		return result;
	}
	
	/**
	 * Removes from this PriorityQueueDouble all (element, priority) pairs
	 * except for the elements or (element, priority) pairs contained in a 
	 * given Collection c.
	 *
	 * @param c A Collection of elements or (element, priority) pairs to keep.
	 *
	 * @return true if and only if this PriorityQueueDouble changed as a result of this method.
	 */
	@Override
	boolean retainAll(Collection<?> c);
	
	/**
	 * Gets the current size of the PriorityQueueDouble, which is the
	 * number of (element, value) pairs that it contains.
	 *
	 * @return the current size of the PriorityQueueDouble.
	 */
	@Override
	int size();
	
	/**
	 * Returns an array containing all of the (element, priority) pairs contained in the
	 * PriorityQueueDouble. The order is not guaranteed. The runtime component type is Object.
	 * The PriorityQueueDouble does not maintain any references to the array that is returned,
	 * instead creating a new array upon each call to the toArray method. The length of the
	 * array that is returned is equal to the current {@link #size()} of the PriorityQueueDouble.
	 *
	 * @return an array, whose runtime component type is Object, containing all of the 
	 * (element, priority) pairs currently in the PriorityQueueDouble.
	 */
	@Override
	Object[] toArray();
	
	/**
	 * Returns an array containing all of the (element, priority) pairs contained in the
	 * PriorityQueueDouble. The order is not guaranteed. The runtime component type is the same
	 * as the array passed to it as a parameter. If the specified array is large enough,
	 * then it is used, otherwise a new array is allocated whose length is equal to 
	 * the current {@link #size()} of the PriorityQueueDouble. If the specified array is larger
	 * than the current size() of the PriorityQueueDouble, the first extra cell is set to null.
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
