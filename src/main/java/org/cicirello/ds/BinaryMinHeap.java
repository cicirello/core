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
import java.util.HashMap;

/**
 * <p>An implementation of a Binary Min Heap. An instance of a BinaryMinHeap
 * contains (element, priority) pairs, such that the elements are distinct.
 * The {@link Object#hashCode} and {@link Object#equals} methods are used
 * to enforce distinctness, so be sure that the class of the elements
 * properly implements these methods, or else behavior is not guaranteed.</p>
 *
 * @param <E> The type of object contained in the BinaryMinHeap.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class BinaryMinHeap<E> {
	
	private ObjectIntegerPair<E>[] buffer;
	private int size;
	private final HashMap<E, Integer> index;
	
	/**
	 * The default initial capacity.
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/**
	 * Initializes an empty BinaryMinHeap with the {@link #DEFAULT_INITIAL_CAPACITY}
	 * as the initial capacity.
	 */
	public BinaryMinHeap() {
		buffer = allocate(DEFAULT_INITIAL_CAPACITY);
		index = new HashMap<E, Integer>();
	}
	
	/**
	 * Initializes an empty BinaryMinHeap.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 *
	 * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
	 */
	public BinaryMinHeap(int initialCapacity) {
		if (initialCapacity <= 0) throw new IllegalArgumentException();
		buffer = allocate(initialCapacity);
		index = new HashMap<E, Integer>();
	}
	
	/**
	 * Initializes a BinaryMinHeap from a collection of (object, value) pairs.
	 *
	 * @param initialElements The initial collection of (object, value) pairs, which must be 
	 * non-empty.
	 *
	 * @throws IllegalArgumentException if initialElements is empty, or if more than
	 * one pair in initialElements contains the same object.
	 */
	public BinaryMinHeap(Collection<ObjectIntegerPair<E>> initialElements) {
		this(initialElements.size());
		for (ObjectIntegerPair<E> element : initialElements) {
			if (index.containsKey(element.element)) {
				throw new IllegalArgumentException("initialElements contains duplicates");
			}
			buffer[size] = element;
			index.put(element.element, size);
			size++;
		}
		buildMinHeap();
	}		
	
	/**
	 * Clears the BinaryMinHeap, removing all elements.
	 */
	public void clear() {
		for (int i = 0; i < size; i++) {
			buffer[i] = null;
		}
		size = 0;
		index.clear();
	}
	
	/**
	 * Checks if this BinaryMinHeap contains a given element.
	 *
	 * @param element The element to check.
	 *
	 * @return true if and only if this BinaryMinHeap contains the element.
	 */
	public boolean contains(E element) {
		return index.containsKey(element);
	}
	
	/**
	 * Increases the capacity if the capacity is not
	 * already at least the specified minimum. If the capacity
	 * is at or above the requested minimum, then this method
	 * does nothing.
	 *
	 * @param minCapacity The desired minimum capacity.
	 */
	public void ensureCapacity(int minCapacity) {
		if (buffer.length < minCapacity) {
			internalAdjustCapacity(minCapacity);
		}
	}
	
	/**
	 * Checks if this BinaryMinHeap contains the same (element, priority)
	 * pairs as another BinaryMinHeap, including the specific order within
	 * the BinaryMinHeap.
	 *
	 * @param other The other BinaryMinHeap.
	 *
	 * @return true if and only if this and other contain the same (element, priority)
	 * pairs.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof BinaryMinHeap) {
			@SuppressWarnings("unchecked")
			BinaryMinHeap<E> casted = (BinaryMinHeap<E>)other;
			if (size != casted.size) return false;
			for (int i = 0; i < size; i++) {
				if (!contains(casted.buffer[i].element)) return false;
				if (casted.buffer[i].value != buffer[i].value) return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Computes a hashCode for the BinaryMinHeap.
	 *
	 * @return a hashCode
	 */
	@Override
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < size; i++) {
			h = 31 * h + buffer[i].value;
			h = 31 * h + buffer[i].element.hashCode();
		}
		return h;
	}
	
	/**
	 * Checks if the BinaryMinHeap is empty.
	 *
	 * @return true if and only if it is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Adds an element to the BinaryMinHeap with a specified priority,
	 * provided the element is not already in the BinaryMinHeap.
	 *
	 * @param element The element.
	 * @param priority The priority of the element.
	 *
	 * @return true if the (element, priority) pair was added, and false if the
	 * BinaryMinHeap already contained the element.
	 */
	public boolean offer(E element, int priority) {
		if (contains(element)) {
			return false;
		} else {
			return offer(new ObjectIntegerPair<E>(element, priority));
		}
	}
	
	/**
	 * Adds an (element, priority) pair to the BinaryMinHeap,
	 * provided the element is not already in the BinaryMinHeap.
	 *
	 * @param pair The (element, priority) pair to add.
	 *
	 * @return true if the (element, priority) pair was added, and false if the
	 * BinaryMinHeap already contained the element.
	 */
	public boolean offer(ObjectIntegerPair<E> pair) {
		if (contains(pair.element)) {
			return false;
		}
		if (size == buffer.length) {
			internalAdjustCapacity(size << 1);
		}
		buffer[size] = pair;
		index.put(pair.element, size);
		percolateUp(size);
		size++;
		return true;
	}
	
	/**
	 * Gets the element with minimum priority value from this BinaryMinHeap,
	 * without removing it.
	 *
	 * @return the element with the minimum priority value, or null if empty.
	 */
	public E peek() {
		return size > 0 ? buffer[0].element : null;
	}
	
	/**
	 * Gets the minimum priority of the elements in the BinaryMinHeap.
	 *
	 * @return the minimum priority of all elements in the BinaryMinHeap, or
	 * {@link Integer#MAX_VALUE} if empty.
	 */
	public int peekPriority() {
		return size > 0 ? buffer[0].value: Integer.MAX_VALUE;
	}
	
	/**
	 * Removes and returns the element with minimum priority value from this BinaryMinHeap.
	 *
	 * @return the element with the minimum priority value, or null if empty.
	 */
	public E poll() {
		ObjectIntegerPair<E> min = pollPair();
		return min != null ? min.element : null;
	}
	
	/**
	 * Removes and returns the element with minimum priority value from this BinaryMinHeap,
	 * returning the (element, priority) pair.
	 *
	 * @return the (element, priority) pair with the minimum priority value, or null if empty.
	 */
	public ObjectIntegerPair<E> pollPair() {
		if (size > 0) {
			ObjectIntegerPair<E> min = buffer[0];
			index.remove(min.element);
			size--;
			buffer[0] = buffer[size];
			index.put(buffer[0].element, 0);
			percolateDown(0);
			return min;
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the current size of the BinaryMinHeap, which is the
	 * number of (object, value) pairs that it contains.
	 *
	 * @return the current size of the BinaryMinHeap.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Decreases the capacity to the current size of the BinaryMinHeap.
	 * If the size and the capacity are the same, then this method does nothing.
	 */
	public void trimToSize() {
		if (size < buffer.length) {
			internalAdjustCapacity(size);
		}
	}
	
	
	private ObjectIntegerPair<E>[] allocate(int capacity) {
		@SuppressWarnings("unchecked")
		ObjectIntegerPair<E>[] temp = new ObjectIntegerPair[capacity];
		return temp;
	}
	
	/*
	 * Used internally: ALERT that this will fail with exception if capacity < size ALERT.
	 */
	private void internalAdjustCapacity(int capacity) {
		ObjectIntegerPair<E>[] temp = allocate(capacity);
		System.arraycopy(buffer, 0, temp, 0, size);
		buffer = temp;
	}
	
	private void buildMinHeap() {
		for (int i = (size >> 1) - 1; i >= 0; i--) {
			percolateDown(i);
		}
	}
	
	private void percolateDown(int i) {
		int left; 
		while ((left = left(i)) < size) { 
			int smallest = i;
			if (buffer[left].value < buffer[i].value) {
				smallest = left;
			}
			int right = left + 1;
			if (right < size && buffer[right].value < buffer[smallest].value) {
				smallest = right;
			}
			if (smallest != i) {
				ObjectIntegerPair<E> temp = buffer[i];
				buffer[i] = buffer[smallest];
				buffer[smallest] = temp;
				index.put(buffer[i].element, i);
				index.put(buffer[smallest].element, smallest);
				i = smallest; 
			} else {
				break;
			}
		}
	}
	
	private void percolateUp(int i) {
		int parent;
		while (i > 0 && buffer[parent = (i-1) >> 1].value > buffer[i].value) {
			ObjectIntegerPair<E> temp = buffer[i];
			buffer[i] = buffer[parent];
			buffer[parent] = temp;
			index.put(buffer[i].element, i);
			index.put(buffer[parent].element, parent);
			i = parent;
		}
	}
	
	private int left(int i) {
		return (i << 1) + 1;
	}
}
