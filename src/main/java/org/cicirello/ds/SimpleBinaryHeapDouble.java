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
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.cicirello.util.Copyable;

/**
 * <p>An implementation of a Binary Heap. An instance of a SimpleBinaryHeapDouble
 * contains (element, priority) pairs, such that the priority values are of type double.</p> 
 *
 * <p>Consider using the {@link BinaryHeapDouble} class instead if your application requires 
 * any of the following: distinct elements, efficient containment checks, efficient priority
 * increases or decreases, efficient arbitrary element removals. The {@link BinaryHeapDouble}
 * class can find an arbitrary element in constant time, making all of those operations faster.</p>
 *
 * <p><b>Priority order:</b>
 * SimpleBinaryHeapDouble instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the SimpleBinaryHeapDouble. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods, such as with:</p>
 * <pre><code>
 * SimpleBinaryHeapDouble&lt;String&gt; pq = SimpleBinaryHeapDouble.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap and m is the size of
 * a Collection parameter where relevant):</p>
 * <ul>
 * <li><b>O(1):</b> {@link #createMaxHeap()}, {@link #createMaxHeap(int)},
 *     {@link #createMinHeap()}, {@link #createMinHeap(int)}, {@link #element}, {@link #isEmpty}, {@link #iterator},
 *     {@link #peek}, {@link #peekElement}, {@link #peekPriority()}, {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #add(Object, double)}, {@link #add(PriorityQueueNode.Double)},
 *     {@link #offer(Object, double)}, {@link #offer(PriorityQueueNode.Double)},
 *     {@link #poll}, {@link #pollElement}, {@link #remove()},  
 *     {@link #removeElement()}</li>
 * <li><b>O(m):</b> {@link #createMaxHeap(Collection)}, 
 *     {@link #createMinHeap(Collection)}</li>
 * <li><b>O(n):</b> {@link #change}, {@link #clear}, {@link #contains}, {@link #copy()}, {@link #demote}, {@link #ensureCapacity}, {@link #equals}, {@link #hashCode}, 
 *     {@link #peekPriority(Object)}, {@link #promote}, {@link #remove(Object)}, {@link #toArray()}, {@link #toArray(Object[])}, 
 *     {@link #trimToSize}</li>
 * <li><b>O(n + m):</b> {@link #addAll(Collection)}, {@link #containsAll(Collection)}, {@link #merge(SimpleBinaryHeapDouble)}, 
 *     {@link #removeAll(Collection)}, {@link #retainAll(Collection)}</li>
 * </ul>
 *
 * @param <E> The type of object contained in the SimpleBinaryHeapDouble.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class SimpleBinaryHeapDouble<E> implements MergeablePriorityQueueDouble<E, SimpleBinaryHeapDouble<E>>, Copyable<SimpleBinaryHeapDouble<E>> {
	
	private PriorityQueueNode.Double<E>[] buffer;
	private int size;
	private final PriorityComparator compare;
	private final double extreme;
	
	/**
	 * The default initial capacity.
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty SimpleBinaryHeapDouble.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 */
	private SimpleBinaryHeapDouble(int initialCapacity) {
		this(initialCapacity, (p1, p2) -> p1 < p2);
	}
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty SimpleBinaryHeapDouble.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 */
	private SimpleBinaryHeapDouble(int initialCapacity, PriorityComparator compare) {
		this.compare = compare;
		buffer = allocate(initialCapacity);
		extreme = compare.belongsAbove(0, 1) ? java.lang.Double.POSITIVE_INFINITY : java.lang.Double.NEGATIVE_INFINITY;
	}
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes a SimpleBinaryHeapDouble from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 *
	 * @throws IllegalArgumentException if initialElements is empty, or if more than
	 * one pair in initialElements contains the same element.
	 */
	private SimpleBinaryHeapDouble(Collection<PriorityQueueNode.Double<E>> initialElements) {
		this(initialElements, (p1, p2) -> p1 < p2);
	}
	
	/* PRIVATE: Use factory methods for creation.
	 *
	 * Initializes a SimpleBinaryHeapDouble from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 *
	 * @throws IllegalArgumentException if initialElements is empty.
	 */
	private SimpleBinaryHeapDouble(Collection<PriorityQueueNode.Double<E>> initialElements, PriorityComparator compare) {
		this(initialElements.size(), compare);
		for (PriorityQueueNode.Double<E> element : initialElements) {
			buffer[size] = element.copy();
			size++;
		}
		buildHeap();
	}
	
	/*
	 * private copy constructor to support the copy() method.
	 */
	private SimpleBinaryHeapDouble(SimpleBinaryHeapDouble<E> other) {
		this(other.capacity(), other.compare);
		size = other.size;
		for (int i = 0; i < size; i++) {
			buffer[i] = other.buffer[i].copy();
		}
	}
	
	@Override
	public SimpleBinaryHeapDouble<E> copy() {
		return new SimpleBinaryHeapDouble<E>(this);
	}
	
	/**
	 * Creates an empty SimpleBinaryHeapDouble with the {@link #DEFAULT_INITIAL_CAPACITY}
	 * as the initial capacity, and a minimum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the SimpleBinaryHeapDouble.
	 *
	 * @return an empty SimpleBinaryHeapDouble with a minimum-priority-first-out priority order
	 */
	public static <E> SimpleBinaryHeapDouble<E> createMinHeap() {
		return new SimpleBinaryHeapDouble<E>(DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Creates an empty SimpleBinaryHeapDouble with a specified
	 * initial capacity, and a minimum-priority-first-out priority order.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 * @param <E> The type of elements contained in the SimpleBinaryHeapDouble.
	 *
	 * @return an empty SimpleBinaryHeapDouble with a minimum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
	 */
	public static <E> SimpleBinaryHeapDouble<E> createMinHeap(int initialCapacity) {
		if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be positive.");
		return new SimpleBinaryHeapDouble<E>(initialCapacity);
	}
	
	/**
	 * Creates a SimpleBinaryHeapDouble from a collection of (element, priority) pairs,
	 * with a minimum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 * @param <E> The type of elements contained in the SimpleBinaryHeapDouble.
	 *
	 * @return a SimpleBinaryHeapDouble with a minimum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialElements is empty.
	 */
	public static <E> SimpleBinaryHeapDouble<E> createMinHeap(Collection<PriorityQueueNode.Double<E>> initialElements) {
		if (initialElements.size() < 1) {
			throw new IllegalArgumentException("initialElements is empty");
		}
		return new SimpleBinaryHeapDouble<E>(initialElements);
	}
	
	/**
	 * Creates an empty SimpleBinaryHeapDouble with the {@link #DEFAULT_INITIAL_CAPACITY}
	 * as the initial capacity, and a maximum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the SimpleBinaryHeapDouble.
	 *
	 * @return an empty SimpleBinaryHeapDouble with a maximum-priority-first-out priority order
	 */
	public static <E> SimpleBinaryHeapDouble<E> createMaxHeap() {
		return new SimpleBinaryHeapDouble<E>(DEFAULT_INITIAL_CAPACITY, (p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates an empty SimpleBinaryHeapDouble with a specified
	 * initial capacity, and a maximum-priority-first-out priority order.
	 *
	 * @param initialCapacity The initial capacity, which must be positive.
	 * @param <E> The type of elements contained in the SimpleBinaryHeapDouble.
	 *
	 * @return an empty SimpleBinaryHeapDouble with a maximum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
	 */
	public static <E> SimpleBinaryHeapDouble<E> createMaxHeap(int initialCapacity) {
		if (initialCapacity <= 0) throw new IllegalArgumentException("Initial capacity must be positive.");
		return new SimpleBinaryHeapDouble<E>(initialCapacity, (p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates a SimpleBinaryHeapDouble from a collection of (element, priority) pairs,
	 * with a maximum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs, which must be 
	 * non-empty.
	 * @param <E> The type of elements contained in the SimpleBinaryHeapDouble.
	 *
	 * @return a SimpleBinaryHeapDouble with a maximum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if initialElements is empty.
	 */
	public static <E> SimpleBinaryHeapDouble<E> createMaxHeap(Collection<PriorityQueueNode.Double<E>> initialElements) {
		if (initialElements.size() < 1) {
			throw new IllegalArgumentException("initialElements is empty");
		}
		return new SimpleBinaryHeapDouble<E>(initialElements, (p1, p2) -> p1 > p2);
	}
	
	/**
	 * <p>Adds an (element, priority) pair to the PriorityQueueDouble with a specified priority.</p>
	 *
	 * @param element The element.
	 * @param priority The priority of the element.
	 *
	 * @return true if the (element, priority) pair was added.
	 */
	@Override
	public final boolean add(E element, double priority) {
		return offer(element, priority);
	}
	
	/**
	 * <p>Adds an (element, priority) pair to the PriorityQueueDouble.</p>
	 *
	 *
	 * @param pair The (element, priority) pair to add.
	 *
	 * @return true if the (element, priority) pair was added.
	 *
	 */
	@Override
	public final boolean add(PriorityQueueNode.Double<E> pair) {
		return offer(pair);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The runtime of this method is O(n + m) where n is current size
	 * of the heap and m is the size of the Collection c. In general this
	 * is more efficient than calling add repeatedly, unless you are
	 * adding a relatively small number of elements, in which case you
	 * should instead call either {@link #offer(PriorityQueueNode.Double)} 
	 * or {@link #add(PriorityQueueNode.Double)} for each 
	 * (element, priority) pair you want to add.</p>
	 */
	@Override
	public final boolean addAll(Collection<? extends PriorityQueueNode.Double<E>> c) {
		if (size + c.size() > buffer.length) {
			internalAdjustCapacity((size + c.size()) << 1);
		}
		boolean changed = false;
		for (PriorityQueueNode.Double<E> e : c) {
			buffer[size] = e.copy();
			size++;
			changed = true;
		}
		if (changed) {
			buildHeap();
		}
		return changed;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>If it contains multiple entries for the element, the specific one
	 * that it chooses to attempt to change is undefined.</p>
	 */
	@Override
	public final boolean change(E element, double priority) {
		int i = find(element);
		if (i >= 0) {
			if (compare.belongsAbove(priority, buffer[i].value)) {
				buffer[i].value = priority;
				percolateUp(i);
				return true;
			} else if (compare.belongsAbove(buffer[i].value, priority)) {
				buffer[i].value = priority;
				percolateDown(i);
				return true;
			}
			return false;
		}
		return offer(element, priority);
	}
	
	@Override
	public final void clear() {
		for (int i = 0; i < size; i++) {
			buffer[i] = null;
		}
		size = 0;
	}
	
	@Override
	public final boolean contains(Object o) {
		if (o instanceof PriorityQueueNode.Double) {
			PriorityQueueNode.Double pair = (PriorityQueueNode.Double)o;
			return find(pair.element) >= 0;
		}
		return find(o) >= 0;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The runtime of this method is O(n + m) where n is current size
	 * of the heap and m is the size of the Collection c. In general this
	 * is more efficient than calling {@link #contains(Object)} repeatedly.</p>
	 */
	@Override
	public final boolean containsAll(Collection<?> c) {
		HashSet<E> containsThese = new HashSet<E>();
		for (int i = 0; i < size; i++) {
			containsThese.add(buffer[i].element);
		}
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Double) {
				PriorityQueueNode.Double pair = (PriorityQueueNode.Double)o;
				if (!containsThese.contains(pair.element)){
					return false;
				}
			} else if (!containsThese.contains(o)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>If it contains multiple entries for the element, the specific one
	 * that it chooses to attempt to demote is undefined.</p>
	 */
	@Override
	public final boolean demote(E element, double priority) {
		int i = find(element);
		if (i >= 0) { 
			if (compare.belongsAbove(buffer[i].value, priority)) {
				buffer[i].value = priority;
				percolateDown(i);
				return true;
			}
		}
		return false;
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
	 * Checks if this SimpleBinaryHeapDouble contains the same (element, priority)
	 * pairs as another SimpleBinaryHeapDouble, including the specific order within
	 * the SimpleBinaryHeapDouble, as well as that the priority order is the same.
	 *
	 * @param other The other SimpleBinaryHeapDouble.
	 *
	 * @return true if and only if this and other contain the same (element, priority)
	 * pairs, with the same priority order.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof SimpleBinaryHeapDouble) {
			@SuppressWarnings("unchecked")
			SimpleBinaryHeapDouble<E> casted = (SimpleBinaryHeapDouble<E>)other;
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
	 * Computes a hashCode for the SimpleBinaryHeapDouble.
	 *
	 * @return a hashCode
	 */
	@Override
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < size; i++) {
			h = 31 * h + java.lang.Double.hashCode(buffer[i].value);
			h = 31 * h + buffer[i].element.hashCode();
		}
		return h;
	}
	
	@Override
	public final boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public final Iterator<PriorityQueueNode.Double<E>> iterator() {
		return new SimpleBinaryHeapDoubleIterator();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if this and other have different priority-order (e.g., one is a 
	 * minheap while the other is a maxheap)
	 */
	@Override
	public boolean merge(SimpleBinaryHeapDouble<E> other) {
		if (compare.belongsAbove(0,1) != other.compare.belongsAbove(0,1)) {
			throw new IllegalArgumentException("this and other follow different priority-order");
		}
		if (size + other.size() > buffer.length) {
			internalAdjustCapacity((size + other.size()) << 1);
		}
		boolean changed = false;
		for (int i = 0; i < other.size; i++) {
			buffer[size] = other.buffer[i];
			size++;
			changed = true;
		}
		if (changed) {
			other.clear();
			buildHeap();
		}
		return changed;
	}
	
	/**
	 * Adds an (element, priority) pair to the SimpleBinaryHeapDouble with a specified priority.
	 *
	 * @param element The element.
	 * @param priority The priority of the element.
	 *
	 * @return true if the (element, priority) pair was added.
	 */
	@Override
	public final boolean offer(E element, double priority) {
		return internalOffer(new PriorityQueueNode.Double<E>(element, priority));
	}
	
	/**
	 * Adds an (element, priority) pair to the SimpleBinaryHeapDouble.
	 *
	 * @param pair The (element, priority) pair to add.
	 *
	 * @return true if the (element, priority) pair was added.
	 */
	@Override
	public final boolean offer(PriorityQueueNode.Double<E> pair) {
		return internalOffer(pair.copy());
	}
	
	@Override
	public final E peekElement() {
		return size > 0 ? buffer[0].element : null;
	}
	
	@Override
	public final PriorityQueueNode.Double<E> peek() {
		return size > 0 ? buffer[0] : null;
	}
	
	@Override
	public final double peekPriority() {
		return size > 0 ? buffer[0].value: extreme;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>If it contains multiple entries for the element, it returns the
	 * priority of one of them, but doesn't define which is chosen.</p>
	 */
	@Override
	public final double peekPriority(E element) {
		int i = find(element);
		return i >= 0 ? buffer[i].value : extreme;
	}
	
	@Override
	public final E pollElement() {
		PriorityQueueNode.Double<E> min = poll();
		return min != null ? min.element : null;
	}
	
	@Override
	public final PriorityQueueNode.Double<E> poll() {
		if (size > 0) {
			PriorityQueueNode.Double<E> min = buffer[0];
			size--;
			if (size > 0) {
				buffer[0] = buffer[size];
				buffer[size] = null;
				percolateDown(0);
			} else {
				buffer[0] = null;
			}
			return min;
		} else {
			return null;
		}
	}
	
	/**
	 * Removes and returns the next (element, priority) pair in priority order from this PriorityQueueDouble,
	 * adding a new (element, priority) pair prior to returning.
	 *
	 * @param pair The (element, priority) pair to add.
	 *
	 * @return the next (element, priority) pair in priority order, or null if empty prior to the call.
	 */
	@Override
	public final PriorityQueueNode.Double<E> pollThenAdd(PriorityQueueNode.Double<E> pair) {
		PriorityQueueNode.Double<E> min = size > 0 ? buffer[0] : null;
		buffer[0] = pair.copy();
		if (size <= 0) {
			size = 1;
		} else {
			percolateDown(0);
		}
		return min;
	}
	
	/**
	 * Removes and returns the next element in priority order from this PriorityQueueDouble,
	 * adding a new (element, priority) pair to the PriorityQueueDouble with a specified priority.
	 *
	 * @param element The new element.
	 * @param priority The priority of the new element.
	 *
	 * @return the next element in priority order, or null if empty.
	 */
	@Override
	public final E pollThenAdd(E element, double priority) {
		E min = size > 0 ? buffer[0].element : null;
		buffer[0] = new PriorityQueueNode.Double<E>(element, priority);
		if (size <= 0) {
			size = 1;
		} else {
			percolateDown(0);
		}
		return min;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>If it contains multiple entries for the element, the specific one
	 * that it chooses to attempt to promote is undefined.</p>
	 */
	@Override
	public final boolean promote(E element, double priority) {
		int i = find(element);
		if (i >= 0) { 
			if (compare.belongsAbove(priority, buffer[i].value)) {
				buffer[i].value = priority;
				percolateUp(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>If it contains multiple entries for the element, it removes
	 * one of them, but doesn't define which is removed.</p>
	 */
	@Override
	public final boolean remove(Object o) {
		int i = -1;
		if (o instanceof PriorityQueueNode.Double) {
			PriorityQueueNode.Double pair = (PriorityQueueNode.Double)o;
			i = find(pair.element);
		} else {
			i = find(o);
		}
		if (i < 0) {
			return false;
		}
		size--;
		if (size > 0 && i != size) {
			double removedElementPriority = buffer[i].value;
			buffer[i] = buffer[size];
			buffer[size] = null;
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
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>Unlike the {@link remove(Object)} method, which removes one instance of the Object
	 * in cases where it appears multiple times, this method removes all instances of all objects
	 * in the Collection.</p>
	 *
	 * <p>The runtime of this method is O(n + m) where n is current size
	 * of the heap and m is the size of the Collection c. In general this
	 * is more efficient than calling remove repeatedly.</p>
	 */
	@Override
	public final boolean removeAll(Collection<?> c) {
		HashSet<Object> discardThese = new HashSet<Object>();
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Double) {
				PriorityQueueNode.Double pair = (PriorityQueueNode.Double)o;
				discardThese.add(pair.element);
			} else {
				discardThese.add(o);
			}
		}
		boolean changed = false;
		for (int i = size-1; i >= 0; i--) {
			if (discardThese.contains(buffer[i].element)) {
				changed = true;
				size--;
				if (i != size) {
					buffer[i] = buffer[size];
				}
				buffer[size] = null;
			}
		}
		if (changed) {
			buildHeap();
		}
		return changed;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The runtime of this method is O(n + m) where n is current size
	 * of the heap and m is the size of the Collection c. In general this
	 * is more efficient than calling remove repeatedly.</p>
	 */
	@Override
	public final boolean retainAll(Collection<?> c) {
		HashSet<Object> keepThese = new HashSet<Object>();
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Double) {
				PriorityQueueNode.Double pair = (PriorityQueueNode.Double)o;
				keepThese.add(pair.element);
			} else {
				keepThese.add(o);
			}
		}
		boolean changed = false;
		for (int i = size-1; i >= 0; i--) {
			if (!keepThese.contains(buffer[i].element)) {
				changed = true;
				size--;
				if (i != size) {
					buffer[i] = buffer[size];
				}
				buffer[size] = null;
			}
		}
		if (changed) {
			buildHeap();
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
	 * Decreases the capacity to the current size of the SimpleBinaryHeapDouble, provided size
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
	
	private final int find(Object element) {
		for (int i = 0; i < size; i++) {
			if (buffer[i].element.equals(element)) {
				return i;
			}
		}
		return -1;
	}
	
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
				PriorityQueueNode.Double<E> temp = buffer[i];
				buffer[i] = buffer[smallest];
				buffer[smallest] = temp;
				i = smallest; 
			} else {
				break;
			}
		}
	}
	
	private void percolateUp(int i) {
		int parent;
		while (i > 0 && compare.belongsAbove(buffer[i].value, buffer[parent = (i-1) >> 1].value)) {
			PriorityQueueNode.Double<E> temp = buffer[i];
			buffer[i] = buffer[parent];
			buffer[parent] = temp;
			i = parent;
		}
	}
	 
	private PriorityQueueNode.Double<E>[] allocate(int capacity) {
		@SuppressWarnings("unchecked")
		PriorityQueueNode.Double<E>[] temp = new PriorityQueueNode.Double[capacity];
		return temp;
	}
	
	/*
	 * Used internally: ALERT that this will fail with exception if capacity < size ALERT.
	 */
	private void internalAdjustCapacity(int capacity) {
		PriorityQueueNode.Double<E>[] temp = allocate(capacity);
		System.arraycopy(buffer, 0, temp, 0, size);
		buffer = temp;
	}
	
	/*
	 * used internally: doesn't check if already contains element
	 */
	private boolean internalOffer(PriorityQueueNode.Double<E> pair) {
		if (size == buffer.length) {
			internalAdjustCapacity(size << 1);
		}
		buffer[size] = pair;
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
		boolean belongsAbove(double p1, double p2);
	}
	
	private class SimpleBinaryHeapDoubleIterator implements Iterator<PriorityQueueNode.Double<E>> {
		
		private int index;
		
		public SimpleBinaryHeapDoubleIterator() {
			index = 0;
		}
		
		@Override
		public boolean hasNext() {
			return index < size;
		}
		
		@Override
		public PriorityQueueNode.Double<E> next() {
			if (index >= size) {
				throw new NoSuchElementException("No more elements remain.");
			}
			index++;
			return buffer[index-1];
		}
	}
}
