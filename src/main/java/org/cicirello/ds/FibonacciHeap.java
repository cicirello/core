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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Deque;
import java.util.ArrayDeque;

import org.cicirello.util.Copyable;

/**
 * <p>An implementation of a Fibonacci Heap. An instance of a FibonacciHeap
 * contains (element, priority) pairs, such that the elements are distinct.
 * The priority values are of type int.</p> 
 *
 * <p><b>Origin:</b> Fibonacci heaps were first introduced in the following article:
 * M. L. Fredman and R. E. Tarjan (1987). Fibonacci Heaps and Their Uses in Improved Network
 * Optimization Algorithms. <i>Journal of the ACM</i>, 34(3): 596-615, July 1987.</p>
 *
 * <p><b>Priority order:</b>
 * FibonacciHeap instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the FibonacciHeap. Methods named <code>createMinHeap</code> produce
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
 * FibonacciHeap&lt;String&gt; pq = FibonacciHeap.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap and m is the size of
 * a Collection parameter where relevant). Note that in many cases in this list, the
 * runtimes are amortized time and not actual time (see a reference on Fibonacci heaps for
 * details).</p>
 * <ul>
 * <li><b>O(1):</b> {@link #add(Object, int)}, {@link #add(PriorityQueueNode.Integer)}, 
 *     {@link #contains}, {@link #createMaxHeap()}, 
 *     {@link #createMinHeap()}, {@link #element}, {@link #isEmpty}, {@link #iterator},
 *     {@link #merge}, {@link #offer(E, int)}, {@link #offer(PriorityQueueNode.Integer)},
 *     {@link #peek}, {@link #peekElement}, {@link #peekPriority()}, {@link #peekPriority(Object)},
 *     {@link #promote}, {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #demote}, {@link #poll}, {@link #pollElement}, 
 *      {@link #pollThenAdd(Object, int)}, {@link #pollThenAdd(PriorityQueueNode.Integer)}, {@link #remove()},
 *      {@link #remove(Object)}, {@link #removeElement()}</li> 
 * <li><b>O(m):</b> {@link #addAll(Collection)}, {@link #containsAll(Collection)}, 
 *     {@link #createMaxHeap(Collection)}, {@link #createMinHeap(Collection)}</li>
 * <li><b>O(n):</b> {@link #clear}, {@link #copy()}, {@link #equals}, {@link #hashCode}, 
 *     {@link #toArray()}, {@link #toArray(Object[])}</li>
 * <li><b>O(n + m):</b> {@link #removeAll(Collection)}, {@link #retainAll(Collection)}</li>
 * </ul>
 * <p>The amortized runtime of {@link #change} depends on the direction of change. If the
 * priority is decreased for a min-heap or increased for a max-heap, the amortized runtime
 * of {@link #change} is O(1); but if the priority is increased for a min-heap or decreased
 * for a max-heap, then the amortized time of {@link #change} is O(lg n).</p>
 *
 * @param <E> The type of object contained in the FibonacciHeap.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class FibonacciHeap<E> extends SimpleFibonacciHeap<E> {
	
	private HashMap<E, FibonacciHeapNode<E>> index;
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty FibonacciHeap.
	 */
	private FibonacciHeap() {
		this((p1, p2) -> p1 < p2);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty FibonacciHeap.
	 */
	private FibonacciHeap(PriorityComparator compare) {
		super(compare);
		index = new HashMap<E, FibonacciHeapNode<E>>();
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a FibonacciHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	private FibonacciHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		this(initialElements, (p1, p2) -> p1 < p2);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a FibonacciHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	private FibonacciHeap(Collection<PriorityQueueNode.Integer<E>> initialElements, PriorityComparator compare) {
		this(compare);
		for (PriorityQueueNode.Integer<E> element : initialElements) {
			if (!offer(element)) {
				throw new IllegalArgumentException("initialElements contains duplicates");
			}
		}
	}
	
	/*
	 * private copy constructor to support the copy() method.
	 */
	private FibonacciHeap(FibonacciHeap<E> other) {
		super(other);
		index = new HashMap<E, FibonacciHeapNode<E>>();
		FibonacciHeapNode.NodeIterator<E> iter = nodeIterator();
		while (iter.hasNext()) {
			FibonacciHeapNode<E> node = iter.next();
			index.put(node.e.element, node);
		}
	}
	
	@Override
	public FibonacciHeap<E> copy() {
		return new FibonacciHeap<E>(this);
	}
	
	/**
	 * Creates an empty FibonacciHeap with minimum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return an empty FibonacciHeap with a minimum-priority-first-out priority order
	 */
	public static <E> FibonacciHeap<E> createMinHeap() {
		return new FibonacciHeap<E>();
	}
	
	/**
	 * Creates a FibonacciHeap from a collection of (element, priority) pairs,
	 * with a minimum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return a FibonacciHeap with a minimum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	public static <E> FibonacciHeap<E> createMinHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		return new FibonacciHeap<E>(initialElements);
	}
	
	/**
	 * Creates an empty FibonacciHeap with maximum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return an empty FibonacciHeap with a maximum-priority-first-out priority order
	 */
	public static <E> FibonacciHeap<E> createMaxHeap() {
		return new FibonacciHeap<E>((p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates a FibonacciHeap from a collection of (element, priority) pairs,
	 * with a maximum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return a FibonacciHeap with a maximum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	public static <E> FibonacciHeap<E> createMaxHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		return new FibonacciHeap<E>(initialElements, (p1, p2) -> p1 > p2);
	}
	
	@Override
	public boolean add(E element, int priority) {
		if (index.containsKey(element)) {
			throw new IllegalArgumentException("already contains an (element, priority) pair with this element");
		}
		return offer(element, priority);
	}
	
	@Override
	public boolean add(PriorityQueueNode.Integer<E> pair) {
		if (index.containsKey(pair.element)) {
			throw new IllegalArgumentException("already contains an (element, priority) pair with this element");
		}
		return offer(pair);
	}
	
	@Override
	public final void clear() {
		super.clear();
		// clear the index... old way: index.clear();
		// instead let garbage collector take care of it, just reinitialize:
		index = new HashMap<E, FibonacciHeapNode<E>>();
		
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
	public final boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Integer) {
				PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
				if (!index.containsKey(pair.element)) {
					return false;
				}
			} else if (!index.containsKey(o)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if this FibonacciHeap contains the same (element, priority)
	 * pairs as another FibonacciHeap, including the specific structure
	 * the FibonacciHeap, as well as that the priority order is the same.
	 *
	 * @param other The other FibonacciHeap.
	 *
	 * @return true if and only if this and other contain the same (element, priority)
	 * pairs, with the same priority order.
	 */
	@Override
	public boolean equals(Object other) {
		return super.equals(other) && other instanceof FibonacciHeap;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if this and other have different priority-order (e.g., one is a 
	 * minheap while the other is a maxheap)
	 */
	@Override
	public boolean merge(SimpleFibonacciHeap<E> other) {
		if (other instanceof FibonacciHeap) {
			@SuppressWarnings("unchecked")
			FibonacciHeap<E> fib = (FibonacciHeap<E>)other;
			index.putAll(fib.index);
		} else {
			FibonacciHeapNode.NodeIterator<E> iter = other.nodeIterator();
			while (iter.hasNext()) {
				FibonacciHeapNode<E> node = iter.next();
				index.put(node.e.element, node);
			}
		}
		return super.merge(other);
	}
	
	@Override
	public final boolean offer(E element, int priority) {
		if (index.containsKey(element)) {
			return false;
		} 
		return super.offer(element, priority);
	}
	
	@Override
	public final boolean offer(PriorityQueueNode.Integer<E> pair) {
		if (index.containsKey(pair.element)) {
			return false;
		}
		return super.offer(pair);
	}
	
	@Override
	public final PriorityQueueNode.Integer<E> poll() {
		PriorityQueueNode.Integer<E> result = super.poll();
		if (result != null) {
			index.remove(result.element);
		}
		return result;
		
	}
	
	/*
	 * package access: overridden with simple index check
	 */
	@Override
	final FibonacciHeapNode<E> find(Object element) {
		return index.get(element);
	}
	
	/*
	 * package access: overridden to record mapping from element to node in index.
	 */
	@Override
	final FibonacciHeapNode<E> internalOffer(PriorityQueueNode.Integer<E> pair) {
		FibonacciHeapNode<E> node = super.internalOffer(pair);
		index.put(pair.element, node);
		return node;
	}
}
