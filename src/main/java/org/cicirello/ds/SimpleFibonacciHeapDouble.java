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
import java.util.ArrayList;
import java.util.HashSet;

import org.cicirello.util.Copyable;

/**
 * <p>An implementation of a Fibonacci Heap. An instance of a SimpleFibonacciHeapDouble
 * contains (element, priority) pairs, such that the priority values are of type double.</p> 
 *
 * <p><b>Origin:</b> Fibonacci heaps were first introduced in the following article:
 * M. L. Fredman and R. E. Tarjan (1987). Fibonacci Heaps and Their Uses in Improved Network
 * Optimization Algorithms. <i>Journal of the ACM</i>, 34(3): 596-615, July 1987.</p>
 *
 * <p>Consider using the {@link FibonacciHeapDouble} class instead if your application requires 
 * any of the following: distinct elements, efficient containment checks, efficient priority
 * increases or decreases, efficient arbitrary element removals. The {@link FibonacciHeapDouble}
 * class can find an arbitrary element in constant time, making all of those operations faster.</p>
 *
 * <p><b>Priority order:</b>
 * SimpleFibonacciHeapDouble instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the SimpleFibonacciHeapDouble. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods, such as with:</p>
 * <pre><code>
 * SimpleFibonacciHeapDouble&lt;String&gt; pq = SimpleFibonacciHeapDouble.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap and m is the size of
 * a Collection parameter where relevant). Note that in many cases in this list, the
 * runtimes are amortized time and not actual time (see a reference on Fibonacci heaps for
 * details).</p>
 * <ul>
 * <li><b>O(1):</b> {@link #add(Object, double)}, {@link #add(PriorityQueueNode.Double)}, 
 *     {@link #createMaxHeap()}, 
 *     {@link #createMinHeap()}, {@link #element}, {@link #isEmpty}, {@link #iterator},
 *     {@link #merge(SimpleFibonacciHeapDouble)}, {@link #offer(E, double)}, {@link #offer(PriorityQueueNode.Double)},
 *     {@link #peek}, {@link #peekElement}, {@link #peekPriority()},
 *     {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #poll}, {@link #pollElement}, {@link #pollThenAdd(Object, double)}, 
 *      {@link #pollThenAdd(PriorityQueueNode.Double)}, {@link #remove()},
 *      {@link #removeElement()}</li> 
 * <li><b>O(m):</b> {@link #addAll(Collection)},  
 *     {@link #createMaxHeap(Collection)}, {@link #createMinHeap(Collection)}</li>
 * <li><b>O(n):</b> {@link #change}, {@link #clear}, {@link #contains}, {@link #copy()}, {@link #demote}, {@link #equals}, 
 *     {@link #hashCode}, {@link #peekPriority(Object)}, {@link #promote}, {@link #remove(Object)}, 
 *     {@link #toArray()}, {@link #toArray(Object[])}</li>
 * <li><b>O(n + m):</b> {@link #containsAll(Collection)}, {@link #removeAll(Collection)}, {@link #retainAll(Collection)}</li>
 * </ul>
 *
 * @param <E> The type of object contained in the SimpleFibonacciHeapDouble.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class SimpleFibonacciHeapDouble<E> extends AbstractFibonacciHeapDouble<E> implements MergeablePriorityQueueDouble<E, SimpleFibonacciHeapDouble<E>>, Copyable<SimpleFibonacciHeapDouble<E>> {
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty SimpleFibonacciHeapDouble.
	 */
	private SimpleFibonacciHeapDouble() {
		this((p1, p2) -> p1 < p2);
	}
	
	/* 
	 * package private for use by subclass: Use factory methods for creation otherwise.
	 *
	 * Initializes an empty SimpleFibonacciHeapDouble.
	 */
	SimpleFibonacciHeapDouble(PriorityComparator compare) {
		super(compare);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a SimpleFibonacciHeapDouble from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 */
	private SimpleFibonacciHeapDouble(Collection<PriorityQueueNode.Double<E>> initialElements) {
		this(initialElements, (p1, p2) -> p1 < p2);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a SimpleFibonacciHeapDouble from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 */
	private SimpleFibonacciHeapDouble(Collection<PriorityQueueNode.Double<E>> initialElements, PriorityComparator compare) {
		this(compare);
		for (PriorityQueueNode.Double<E> element : initialElements) {
			internalOffer(element.copy());
		}
	}
	
	/*
	 * package private copy constructor to support the copy() method, including in subclass.
	 */
	SimpleFibonacciHeapDouble(SimpleFibonacciHeapDouble<E> other) {
		super(other);
	}
	
	@Override
	public SimpleFibonacciHeapDouble<E> copy() {
		return new SimpleFibonacciHeapDouble<E>(this);
	}
	
	/**
	 * Creates an empty SimpleFibonacciHeapDouble with minimum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the SimpleFibonacciHeapDouble.
	 *
	 * @return an empty SimpleFibonacciHeapDouble with a minimum-priority-first-out priority order
	 */
	public static <E> SimpleFibonacciHeapDouble<E> createMinHeap() {
		return new SimpleFibonacciHeapDouble<E>();
	}
	
	/**
	 * Creates a SimpleFibonacciHeapDouble from a collection of (element, priority) pairs,
	 * with a minimum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the SimpleFibonacciHeapDouble.
	 *
	 * @return a SimpleFibonacciHeapDouble with a minimum-priority-first-out priority order
	 *
	 */
	public static <E> SimpleFibonacciHeapDouble<E> createMinHeap(Collection<PriorityQueueNode.Double<E>> initialElements) {
		return new SimpleFibonacciHeapDouble<E>(initialElements);
	}
	
	/**
	 * Creates an empty SimpleFibonacciHeapDouble with maximum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the SimpleFibonacciHeapDouble.
	 *
	 * @return an empty SimpleFibonacciHeapDouble with a maximum-priority-first-out priority order
	 */
	public static <E> SimpleFibonacciHeapDouble<E> createMaxHeap() {
		return new SimpleFibonacciHeapDouble<E>((p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates a SimpleFibonacciHeapDouble from a collection of (element, priority) pairs,
	 * with a maximum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the SimpleFibonacciHeapDouble.
	 *
	 * @return a SimpleFibonacciHeapDouble with a maximum-priority-first-out priority order
	 *
	 */
	public static <E> SimpleFibonacciHeapDouble<E> createMaxHeap(Collection<PriorityQueueNode.Double<E>> initialElements) {
		return new SimpleFibonacciHeapDouble<E>(initialElements, (p1, p2) -> p1 > p2);
	}
	
	@Override
	public boolean add(E element, double priority) {
		return offer(element, priority);
	}
	
	@Override
	public boolean add(PriorityQueueNode.Double<E> pair) {
		return offer(pair);
	}
	
	@Override
	public final boolean change(E element, double priority) {
		FibonacciHeapDoubleNode<E> node = find(element);
		if (node != null) {
			return internalChange(node, priority);
		}
		return offer(element, priority);
	}
	
	@Override
	public boolean contains(Object o) {
		if (o instanceof PriorityQueueNode.Double) {
			PriorityQueueNode.Double pair = (PriorityQueueNode.Double)o;
			return find(pair.element) != null;
		}
		return find(o) != null;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The runtime of this method is O(n + m) where n is current size
	 * of the heap and m is the size of the Collection c. In general this
	 * is more efficient than calling {@link #contains(Object)} repeatedly.</p>
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		HashSet<E> containsThese = new HashSet<E>();
		for (PriorityQueueNode.Double<E> e : this) {
			containsThese.add(e.element);
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
	
	@Override
	public final boolean demote(E element, double priority) {
		FibonacciHeapDoubleNode<E> node = find(element);
		if (node != null && compare.comesBefore(node.e.value, priority)) {
			internalDemote(node, priority);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if this SimpleFibonacciHeapDouble contains the same (element, priority)
	 * pairs as another SimpleFibonacciHeapDouble, including the specific structure
	 * the SimpleFibonacciHeapDouble, as well as that the priority order is the same.
	 *
	 * @param other The other SimpleFibonacciHeapDouble.
	 *
	 * @return true if and only if this and other contain the same (element, priority)
	 * pairs, with the same priority order.
	 */
	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if this and other have different priority-order (e.g., one is a 
	 * minheap while the other is a maxheap)
	 */
	@Override
	public boolean merge(SimpleFibonacciHeapDouble<E> other) {
		return internalMerge(other);
	}
	
	@Override
	public boolean offer(E element, double priority) {
		internalOffer(new PriorityQueueNode.Double<E>(element, priority));
		return true;
	}
	
	@Override
	public boolean offer(PriorityQueueNode.Double<E> pair) {
		internalOffer(pair.copy());
		return true;
	}
	
	@Override
	public final boolean promote(E element, double priority) {
		FibonacciHeapDoubleNode<E> node = find(element);
		if (node != null && compare.comesBefore(priority, node.e.value)) {
			internalPromote(node, priority);
			return true;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The runtime of this method is O(n + m) where n is current size
	 * of the heap and m is the size of the Collection c. In general this
	 * is more efficient than calling remove repeatedly.</p>
	 */
	@Override
	public final boolean removeAll(Collection<?> c) {
		HashSet<Object> discardThese = toSet(c);
		ArrayList<PriorityQueueNode.Double<E>> keepList = new ArrayList<PriorityQueueNode.Double<E>>();
		for (PriorityQueueNode.Double<E> e : this) {
			if (!discardThese.contains(e.element)) {
				keepList.add(e);
			}
		}
		return from(keepList);
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
		HashSet<Object> keepThese = toSet(c);
		ArrayList<PriorityQueueNode.Double<E>> keepList = new ArrayList<PriorityQueueNode.Double<E>>(keepThese.size());
		for (PriorityQueueNode.Double<E> e : this) {
			if (keepThese.contains(e.element)) {
				keepList.add(e);
			}
		}
		return from(keepList);
	}
	
	private HashSet<Object> toSet(Collection<?> c) {
		HashSet<Object> set = new HashSet<Object>();
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Double) {
				PriorityQueueNode.Double pair = (PriorityQueueNode.Double)o;
				set.add(pair.element);
			} else {
				set.add(o);
			}
		}
		return set;
	}
	
	private boolean from(ArrayList<PriorityQueueNode.Double<E>> keepList) {
		if (keepList.size() < size()) {
			clear();
			for (PriorityQueueNode.Double<E> e : keepList) {
				internalOffer(e);
			}
			return true;
		}
		return false;
	}
}
