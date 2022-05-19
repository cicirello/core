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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>An implementation of a Binary Heap. An instance of a BinaryHeap
 * contains (element, priority) pairs, such that the elements are distinct.
 * The priority values are of type int.</p> 
 *
 * <p><b>Priority order:</b>
 * BinaryHeap instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the BinaryHeap. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Distinctness:</b>
 * The {@link Object#hashCode} and {@link Object#equals} methods are used
 * to enforce distinctness, so be sure that the class of the elements
 * properly implements these methods, or else behavior is not guaranteed.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods, such as with:</p>
 * <pre><code>
 * BinaryHeap&lt;String&gt; pq = BinaryHeap.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap and m is the size of
 * a Collection parameter where relevant):</p>
 * <ul>
 * <li><b>O(1):</b> {@link #contains}, {@link #createMaxHeap()}, {@link #createMaxHeap(int)},
 *     {@link #createMinHeap()}, {@link #createMinHeap(int)}, {@link #element}, {@link #isEmpty}, {@link #iterator},
 *     {@link #peek}, {@link #peekElement}, {@link #peekPriority()}, {@link #peekPriority(Object)},
 *     {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #add(Object, int)}, {@link #add(PriorityQueueNode.Integer)}, 
 *     {@link #change}, {@link #offer(Object, int)}, {@link #offer(PriorityQueueNode.Integer)},
 *     {@link #poll}, {@link #pollElement}, {@link #remove()}, {@link #remove(Object)}, {@link #removeElement()}</li>
 * <li><b>O(n):</b> {@link #clear}, {@link #ensureCapacity}, {@link #equals}, {@link #hashCode}, 
 *     {@link #toArray()}, {@link #toArray(Object[])}, 
 *     {@link #trimToSize}</li>
 * <li><b>O(m):</b> {@link #containsAll(Collection)}, {@link #createMaxHeap(Collection)}, 
 *     {@link #createMinHeap(Collection)}</li>
 * <li><b>O(m lg n):</b> {@link #removeAll(Collection)}</li>
 * <li><b>O(m lg (n+m)):</b> {@link #addAll(Collection)}</li>
 * <li><b>O(n lg n):</b> {@link #retainAll(Collection)}</li>
 * </ul>
 *
 * @param <E> The type of object contained in the BinaryHeap.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class BinaryHeap<E> implements PriorityQueue<E> {
	
	private PriorityQueueNode.Integer<E>[] buffer;
	private int size;
	private final HashMap<E, java.lang.Integer> index;
	private final PriorityComparator compare;
	private final int extreme;
	
	/**
	 * The default initial capacity.
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty BinaryHeap.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 */
	private BinaryHeap(int initialCapacity) {
		this(initialCapacity, (p1, p2) -> p1 < p2);
	}
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty BinaryHeap.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 */
	private BinaryHeap(int initialCapacity, PriorityComparator compare) {
		this.compare = compare;
		buffer = allocate(initialCapacity);
		index = new HashMap<E, java.lang.Integer>();
		extreme = compare.belongsAbove(0, 1) ? java.lang.Integer.MAX_VALUE : java.lang.Integer.MIN_VALUE;
	}
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes a BinaryHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 *
	 * @throws IllegalArgumentException if initialElements is empty, or if more than
	 * one pair in initialElements contains the same element.
	 */
	private BinaryHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		this(initialElements, (p1, p2) -> p1 < p2);
	}
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes a BinaryHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 *
	 * @throws IllegalArgumentException if initialElements is empty, or if more than
	 * one pair in initialElements contains the same element.
	 */
	private BinaryHeap(Collection<PriorityQueueNode.Integer<E>> initialElements, PriorityComparator compare) {
		this(initialElements.size(), compare);
		for (PriorityQueueNode.Integer<E> element : initialElements) {
			if (index.containsKey(element.element)) {
				throw new IllegalArgumentException("initialElements contains duplicates");
			}
			buffer[size] = element;
			index.put(element.element, size);
			size++;
		}
		buildHeap();
	}
	
	/**
	 * Creates an empty BinaryHeap with the {@link #DEFAULT_INITIAL_CAPACITY}
	 * as the initial capacity, and a minimum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the BinaryHeap.
	 *
	 * @return an empty BinaryHeap with a minimum-priority-first-out priority order
	 */
	public static <E> BinaryHeap<E> createMinHeap() {
		return new BinaryHeap<E>(DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Creates an empty BinaryHeap with a specified
	 * initial capacity, and a minimum-priority-first-out priority order.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 * @param <E> The type of elements contained in the BinaryHeap.
	 *
	 * @return an empty BinaryHeap with a minimum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
	 */
	public static <E> BinaryHeap<E> createMinHeap(int initialCapacity) {
		if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be positive.");
		return new BinaryHeap<E>(initialCapacity);
	}
	
	/**
	 * Creates a BinaryHeap from a collection of (element, priority) pairs,
	 * with a minimum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 * @param <E> The type of elements contained in the BinaryHeap.
	 *
	 * @return an empty BinaryHeap with a minimum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialElements is empty, or if more than
	 * one pair in initialElements contains the same element.
	 */
	public static <E> BinaryHeap<E> createMinHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		if (initialElements.size() < 1) {
			throw new IllegalArgumentException("initialElements is empty");
		}
		return new BinaryHeap<E>(initialElements);
	}
	
	/**
	 * Creates an empty BinaryHeap with the {@link #DEFAULT_INITIAL_CAPACITY}
	 * as the initial capacity, and a maximum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the BinaryHeap.
	 *
	 * @return an empty BinaryHeap with a maximum-priority-first-out priority order
	 */
	public static <E> BinaryHeap<E> createMaxHeap() {
		return new BinaryHeap<E>(DEFAULT_INITIAL_CAPACITY, (p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates an empty BinaryHeap with a specified
	 * initial capacity, and a maximum-priority-first-out priority order.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 * @param <E> The type of elements contained in the BinaryHeap.
	 *
	 * @return an empty BinaryHeap with a maximum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
	 */
	public static <E> BinaryHeap<E> createMaxHeap(int initialCapacity) {
		if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be positive.");
		return new BinaryHeap<E>(initialCapacity, (p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates a BinaryHeap from a collection of (element, priority) pairs,
	 * with a maximum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 * @param <E> The type of elements contained in the BinaryHeap.
	 *
	 * @return an empty BinaryHeap with a maximum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialElements is empty, or if more than
	 * one pair in initialElements contains the same element.
	 */
	public static <E> BinaryHeap<E> createMaxHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		if (initialElements.size() < 1) {
			throw new IllegalArgumentException("initialElements is empty");
		}
		return new BinaryHeap<E>(initialElements, (p1, p2) -> p1 > p2);
	}
	
	@Override
	public final void change(E element, int priority) {
		if (!offer(element, priority)) {
			int i = index.get(element);
			if (compare.belongsAbove(priority, buffer[i].value)) {
				buffer[i].value = priority;
				percolateUp(i);
			} else if (compare.belongsAbove(buffer[i].value, priority)) {
				buffer[i].value = priority;
				percolateDown(i);
			}
		}
	}
	
	@Override
	public final void clear() {
		Arrays.fill(buffer, null);
		size = 0;
		index.clear();
	}
	
	@Override
	public final boolean contains(Object o) {
		if (o instanceof PriorityQueueNode.Integer) {
			PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
			return index.containsKey(pair.element);
		}
		return index.containsKey(o);
	}
	
	/**
	 * Increases the capacity if the capacity is not
	 * already at least the specified minimum. If the capacity
	 * is at or above the requested minimum, then this method
	 * does nothing.
	 *
	 * @param minCapacity The desired minimum capacity.
	 */
	public final void ensureCapacity(int minCapacity) {
		if (buffer.length < minCapacity) {
			internalAdjustCapacity(minCapacity);
		}
	}
	
	/**
	 * Checks if this BinaryHeap contains the same (element, priority)
	 * pairs as another BinaryHeap, including the specific order within
	 * the BinaryHeap, as well as that the priority order is the same.
	 *
	 * @param other The other BinaryHeap.
	 *
	 * @return true if and only if this and other contain the same (element, priority)
	 * pairs, with the same priority order.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof BinaryHeap) {
			@SuppressWarnings("unchecked")
			BinaryHeap<E> casted = (BinaryHeap<E>)other;
			if (size != casted.size) return false;
			if (compare.belongsAbove(0, 1) != casted.compare.belongsAbove(0, 1)) return false;
			for (int i = 0; i < size; i++) {
				if (!buffer[i].element.equals(casted.buffer[i].element)) return false;
				if (casted.buffer[i].value != buffer[i].value) return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Computes a hashCode for the BinaryHeap.
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
	public final boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public final Iterator<PriorityQueueNode.Integer<E>> iterator() {
		return new BinaryHeapIterator();
	}
	
	@Override
	public final boolean offer(E element, int priority) {
		if (contains(element)) {
			return false;
		} 
		return internalOffer(new PriorityQueueNode.Integer<E>(element, priority));
	}
	
	@Override
	public final boolean offer(PriorityQueueNode.Integer<E> pair) {
		if (contains(pair.element)) {
			return false;
		}
		return internalOffer(pair);
	}
	
	@Override
	public final E peekElement() {
		return size > 0 ? buffer[0].element : null;
	}
	
	@Override
	public final PriorityQueueNode.Integer<E> peek() {
		return size > 0 ? buffer[0] : null;
	}
	
	@Override
	public final int peekPriority() {
		return size > 0 ? buffer[0].value: extreme;
	}
	
	@Override
	public final int peekPriority(E element) {
		java.lang.Integer i = index.get(element);
		return i != null ? buffer[i].value : extreme;
	}
	
	@Override
	public final E pollElement() {
		PriorityQueueNode.Integer<E> min = poll();
		return min != null ? min.element : null;
	}
	
	@Override
	public final PriorityQueueNode.Integer<E> poll() {
		if (size > 0) {
			PriorityQueueNode.Integer<E> min = buffer[0];
			index.remove(min.element);
			size--;
			if (size > 0) {
				buffer[0] = buffer[size];
				buffer[size] = null;
				index.put(buffer[0].element, 0);
				percolateDown(0);
			} else {
				buffer[0] = null;
			}
			return min;
		} else {
			return null;
		}
	}
	
	@Override
	public final boolean remove(Object o) {
		java.lang.Integer i = null;
		if (o instanceof PriorityQueueNode.Integer) {
			PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
			i = index.get(pair.element);
		} else {
			i = index.get(o);
		}
		if (i == null) {
			return false;
		}
		index.remove(buffer[i].element);
		size--;
		if (size > 0 && i != size) {
			int removedElementPriority = buffer[i].value;
			buffer[i] = buffer[size];
			buffer[size] = null;
			index.put(buffer[i].element, i);
			// percolate in relevant direction
			if (compare.belongsAbove(buffer[i].value, removedElementPriority)) {
				percolateUp(i);
			} else if (compare.belongsAbove(removedElementPriority, buffer[i].value)) {
				percolateDown(i);
			}
		} else {
			buffer[i] = null;
		}
		return true;
	}
	
	@Override
	public final boolean retainAll(Collection<?> c) {
		HashSet<E> deleteThese = new HashSet<E>();
		for (int i = 0; i < size; i++) {
			deleteThese.add(buffer[i].element);
		}
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Integer) {
				PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
				deleteThese.remove(pair.element);
			} else {
				deleteThese.remove(o);
			}
		}
		boolean changed = false;
		for (E e : deleteThese) {
			remove(e); 
			changed = true;
		}
		return changed;
	}
	
	@Override
	public final int size() {
		return size;
	}
	
	@Override
	public final Object[] toArray() {
		Object[] array = new Object[size];
		for (int i = 0; i < size; i++) {
			array[i] = buffer[i];
		}
		return array;
	}
	
	@Override
	public final <T> T[] toArray(T[] array) {
		@SuppressWarnings("unchecked")
		T[] result = array.length >= size ? array : (T[])Array.newInstance(array.getClass().getComponentType(), size);
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			T nextElement = (T)buffer[i];
			result[i] = nextElement;
		}
		if (result.length > size) {
			result[size] = null;
		}
		return result;
	}
	
	/**
	 * Decreases the capacity to the current size of the BinaryHeap, provided size
	 * is at least 1, and otherwise decreases capacity to 1.
	 * If the size and the capacity are the same, then this method does nothing.
	 */
	public final void trimToSize() {
		if (size < buffer.length) {
			internalAdjustCapacity(size > 0 ? size : 1);
		}
	}
	
	/*
	 * package-private to support testing.
	 * no reason to provide public access to current capacity
	 */
	int capacity() {
		return buffer.length;
	}
	
	/*
	 * package-private to enable overriding in 
	 * nested subclass to support max heaps.
	 */
	private void percolateDown(int i) {
		int left; 
		while ((left = (i << 1) + 1) < size) { 
			int smallest = i;
			if (compare.belongsAbove(buffer[left].value, buffer[i].value)) {
				smallest = left;
			}
			int right = left + 1;
			if (right < size && compare.belongsAbove(buffer[right].value, buffer[smallest].value)) {
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
	
	/*
	 * package-private to enable overriding in 
	 * nested subclass to support max heaps.
	 */
	private void percolateUp(int i) {
		int parent;
		while (i > 0 && compare.belongsAbove(buffer[i].value, buffer[parent = (i-1) >> 1].value)) {
			PriorityQueueNode.Integer<E> temp = buffer[i];
			buffer[i] = buffer[parent];
			buffer[parent] = temp;
			index.put(buffer[i].element, i);
			index.put(buffer[parent].element, parent);
			i = parent;
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
	
	/*
	 * used internally: doesn't check if already contains element
	 */
	private boolean internalOffer(PriorityQueueNode.Integer<E> pair) {
		if (size == buffer.length) {
			internalAdjustCapacity(size << 1);
		}
		buffer[size] = pair;
		index.put(pair.element, size);
		percolateUp(size);
		size++;
		return true;
	}
	
	private void buildHeap() {
		for (int i = (size >> 1) - 1; i >= 0; i--) {
			percolateDown(i);
		}
	}
	
	@FunctionalInterface
	private static interface PriorityComparator {
		
		boolean belongsAbove(int p1, int p2);
	}
	
	private class BinaryHeapIterator implements Iterator<PriorityQueueNode.Integer<E>> {
		
		private int index;
		
		public BinaryHeapIterator() {
			index = 0;
		}
		
		@Override
		public boolean hasNext() {
			return index < size;
		}
		
		@Override
		public PriorityQueueNode.Integer<E> next() {
			if (index >= size) {
				throw new NoSuchElementException("No more elements remain.");
			}
			index++;
			return buffer[index-1];
		}
	}
}
