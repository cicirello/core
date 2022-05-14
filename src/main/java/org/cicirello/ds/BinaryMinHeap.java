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
 * The priority values are of type int, and the priority value is minimum
 * priority first out.</p>
 *
 * <p>The {@link Object#hashCode} and {@link Object#equals} methods are used
 * to enforce distinctness, so be sure that the class of the elements
 * properly implements these methods, or else behavior is not guaranteed.</p>
 *
 * @param <E> The type of object contained in the BinaryMinHeap.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class BinaryMinHeap<E> implements PriorityQueue.Integer<E> {
	
	private PriorityQueueNode.Integer<E>[] buffer;
	private int size;
	private final HashMap<E, java.lang.Integer> index;
	
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
		index = new HashMap<E, java.lang.Integer>();
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
		index = new HashMap<E, java.lang.Integer>();
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
	public BinaryMinHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		this(initialElements.size());
		for (PriorityQueueNode.Integer<E> element : initialElements) {
			if (index.containsKey(element.element)) {
				throw new IllegalArgumentException("initialElements contains duplicates");
			}
			buffer[size] = element;
			index.put(element.element, size);
			size++;
		}
		buildMinHeap();
	}
	
	@Override
	public void change(E element, int priority) {
		if (!offer(element, priority)) {
			int i = index.get(element);
			if (priority < buffer[i].value) {
				buffer[i].value = priority;
				percolateUp(i);
			} else if (priority > buffer[i].value) {
				buffer[i].value = priority;
				percolateDown(i);
			}
		}
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			buffer[i] = null;
		}
		size = 0;
		index.clear();
	}
	
	@Override
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
		// After upgrade to Java 17, change following to: other instanceof BinaryMinHeap<E>
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
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public boolean offer(E element, int priority) {
		if (contains(element)) {
			return false;
		} else {
			return offer(new PriorityQueueNode.Integer<E>(element, priority));
		}
	}
	
	@Override
	public boolean offer(PriorityQueueNode.Integer<E> pair) {
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
	
	@Override
	public E peek() {
		return size > 0 ? buffer[0].element : null;
	}
	
	@Override
	public PriorityQueueNode.Integer<E> peekPair() {
		return size > 0 ? buffer[0] : null;
	}
	
	@Override
	public int peekPriority() {
		return size > 0 ? buffer[0].value: java.lang.Integer.MAX_VALUE;
	}
	
	@Override
	public E poll() {
		PriorityQueueNode.Integer<E> min = pollPair();
		return min != null ? min.element : null;
	}
	
	@Override
	public PriorityQueueNode.Integer<E> pollPair() {
		if (size > 0) {
			PriorityQueueNode.Integer<E> min = buffer[0];
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
	
	@Override
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
	
	
	private PriorityQueueNode.Integer<E>[] allocate(int capacity) {
		@SuppressWarnings("unchecked")
		PriorityQueueNode.Integer<E>[] temp = new PriorityQueueNode.Integer[capacity];
		return temp;
	}
	
	/*
	 * Used internally: ALERT that this will fail with exception if capacity < size ALERT.
	 */
	private void internalAdjustCapacity(int capacity) {
		PriorityQueueNode.Integer<E>[] temp = allocate(capacity);
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
				PriorityQueueNode.Integer<E> temp = buffer[i];
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
			PriorityQueueNode.Integer<E> temp = buffer[i];
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
