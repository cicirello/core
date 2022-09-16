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
 * <p>An implementation of a Fibonacci Heap. An instance of a SimpleFibonacciHeap
 * contains (element, priority) pairs, such that the priority values are of type int.</p> 
 *
 * <p><b>Origin:</b> Fibonacci heaps were first introduced in the following article:
 * M. L. Fredman and R. E. Tarjan (1987). Fibonacci Heaps and Their Uses in Improved Network
 * Optimization Algorithms. <i>Journal of the ACM</i>, 34(3): 596-615, July 1987.</p>
 *
 * <p>Consider using the {@link FibonacciHeap} class instead if your application requires 
 * any of the following: distinct elements, efficient containment checks, efficient priority
 * increases or decreases, efficient arbitrary element removals. The {@link FibonacciHeap}
 * class can find an arbitrary element in constant time, making all of those operations faster.</p>
 *
 * <p><b>Priority order:</b>
 * SimpleFibonacciHeap instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the SimpleFibonacciHeap. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods, such as with:</p>
 * <pre><code>
 * SimpleFibonacciHeap&lt;String&gt; pq = SimpleFibonacciHeap.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap and m is the size of
 * a Collection parameter where relevant). Note that in many cases in this list, the
 * runtimes are amortized time and not actual time (see a reference on Fibonacci heaps for
 * details).</p>
 * <ul>
 * <li><b>O(1):</b> {@link #add(Object, int)}, {@link #add(PriorityQueueNode.Integer)}, 
 *     {@link #createMaxHeap()}, 
 *     {@link #createMinHeap()}, {@link #element}, {@link #isEmpty}, {@link #iterator},
 *     {@link #merge(SimpleFibonacciHeap)}, {@link #offer(E, int)}, {@link #offer(PriorityQueueNode.Integer)},
 *     {@link #peek}, {@link #peekElement}, {@link #peekPriority()},
 *     {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #poll}, {@link #pollElement}, {@link #pollThenAdd(Object, int)}, 
 *      {@link #pollThenAdd(PriorityQueueNode.Integer)}, {@link #remove()},
 *      {@link #removeElement()}</li> 
 * <li><b>O(m):</b> {@link #addAll(Collection)},  
 *     {@link #createMaxHeap(Collection)}, {@link #createMinHeap(Collection)}</li>
 * <li><b>O(n):</b> {@link #change}, {@link #clear}, {@link #contains}, {@link #copy()}, {@link #demote}, {@link #equals}, 
 *     {@link #hashCode}, {@link #peekPriority(E)}, {@link #promote}, {@link #remove(Object)}, 
 *     {@link #toArray()}, {@link #toArray(Object[])}</li>
 * <li><b>O(n + m):</b> {@link #containsAll(Collection)}, {@link #removeAll(Collection)}, {@link #retainAll(Collection)}</li>
 * </ul>
 *
 * @param <E> The type of object contained in the SimpleFibonacciHeap.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class SimpleFibonacciHeap<E> implements MergeablePriorityQueue<E, SimpleFibonacciHeap<E>>, Copyable<SimpleFibonacciHeap<E>> {
	
	private final PriorityComparator compare;
	private final int extreme;
	
	private int size;
	private Node<E> min;
	
	// This array is what is referred to in CLRS description
	// of algorithm as A in the method consolidate. As an optimization
	// we construct the array once, and reuse it on all calls to consolidate.
	private final Node<E>[] rootsByDegrees;
	
	private final static double INV_LOG_GOLDEN_RATIO = 2.0780869212350273;
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty SimpleFibonacciHeap.
	 */
	private SimpleFibonacciHeap() {
		this((p1, p2) -> p1 < p2);
	}
	
	/* 
	 * package private for use by subclass: Use factory methods for creation otherwise.
	 *
	 * Initializes an empty SimpleFibonacciHeap.
	 */
	SimpleFibonacciHeap(PriorityComparator compare) {
		this.compare = compare;
		extreme = compare.comesBefore(0, 1) ? java.lang.Integer.MAX_VALUE : java.lang.Integer.MIN_VALUE;
		// length of array used by consolidate is initialized to 45 as follows:
		// 1) since size is an int, the implicit limit on capacity is Integer.MAX_VALUE.
		// 2) Thus, the highest that D(n) can be for a call to consolidate is:
		//    floor(log(Integer.MAX_VALUE) / log((1+sqrt(5))/2)) = 44.
		// 3) Array must be of length 1+D(n), so longest array must be is 45.
		// 4) consolidate computes the actual D(n) for a specific call, and uses only
		//    part of this array.
		rootsByDegrees = nodeArrayAllocate(45);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a SimpleFibonacciHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 */
	private SimpleFibonacciHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		this(initialElements, (p1, p2) -> p1 < p2);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a SimpleFibonacciHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 */
	private SimpleFibonacciHeap(Collection<PriorityQueueNode.Integer<E>> initialElements, PriorityComparator compare) {
		this(compare);
		for (PriorityQueueNode.Integer<E> element : initialElements) {
			internalOffer(element.copy());
		}
	}
	
	/*
	 * package private copy constructor to support the copy() method, including in subclass.
	 */
	SimpleFibonacciHeap(SimpleFibonacciHeap<E> other) {
		this(other.compare);
		size = other.size;
		min = other.min != null ? other.min.copy() : null;
	}
	
	@Override
	public SimpleFibonacciHeap<E> copy() {
		return new SimpleFibonacciHeap<E>(this);
	}
	
	/**
	 * Creates an empty SimpleFibonacciHeap with minimum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the SimpleFibonacciHeap.
	 *
	 * @return an empty SimpleFibonacciHeap with a minimum-priority-first-out priority order
	 */
	public static <E> SimpleFibonacciHeap<E> createMinHeap() {
		return new SimpleFibonacciHeap<E>();
	}
	
	/**
	 * Creates a SimpleFibonacciHeap from a collection of (element, priority) pairs,
	 * with a minimum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the SimpleFibonacciHeap.
	 *
	 * @return a SimpleFibonacciHeap with a minimum-priority-first-out priority order
	 *
	 */
	public static <E> SimpleFibonacciHeap<E> createMinHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		return new SimpleFibonacciHeap<E>(initialElements);
	}
	
	/**
	 * Creates an empty SimpleFibonacciHeap with maximum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the SimpleFibonacciHeap.
	 *
	 * @return an empty SimpleFibonacciHeap with a maximum-priority-first-out priority order
	 */
	public static <E> SimpleFibonacciHeap<E> createMaxHeap() {
		return new SimpleFibonacciHeap<E>((p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates a SimpleFibonacciHeap from a collection of (element, priority) pairs,
	 * with a maximum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the SimpleFibonacciHeap.
	 *
	 * @return a SimpleFibonacciHeap with a maximum-priority-first-out priority order
	 *
	 */
	public static <E> SimpleFibonacciHeap<E> createMaxHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		return new SimpleFibonacciHeap<E>(initialElements, (p1, p2) -> p1 > p2);
	}
	
	@Override
	public boolean add(E element, int priority) {
		return offer(element, priority);
	}
	
	@Override
	public boolean add(PriorityQueueNode.Integer<E> pair) {
		return offer(pair);
	}
	
	@Override
	public final boolean change(E element, int priority) {
		Node<E> node = find(element);
		if (node != null) {
			if (compare.comesBefore(priority, node.e.value)) {
				internalPromote(node, priority);
				return true;
			} else if (compare.comesBefore(node.e.value, priority)) {
				internalDemote(node, priority);
				return true;
			}
			return false;
		}
		return offer(element, priority);
	}
	
	@Override
	public void clear() {
		size = 0;
		// set min to null which should cause garbage collection
		// of entire fibonacci heap (impossible to have references to Nodes
		// external from this class.
		min = null;
	}
	
	@Override
	public boolean contains(Object o) {
		if (o instanceof PriorityQueueNode.Integer) {
			PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
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
		for (PriorityQueueNode.Integer<E> e : this) {
			containsThese.add(e.element);
		}
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Integer) {
				PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
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
	public final boolean demote(E element, int priority) {
		Node<E> node = find(element);
		if (node != null && compare.comesBefore(node.e.value, priority)) {
			internalDemote(node, priority);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if this SimpleFibonacciHeap contains the same (element, priority)
	 * pairs as another SimpleFibonacciHeap, including the specific structure
	 * the SimpleFibonacciHeap, as well as that the priority order is the same.
	 *
	 * @param other The other SimpleFibonacciHeap.
	 *
	 * @return true if and only if this and other contain the same (element, priority)
	 * pairs, with the same priority order.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof SimpleFibonacciHeap) {
			@SuppressWarnings("unchecked")
			SimpleFibonacciHeap<E> casted = (SimpleFibonacciHeap<E>)other;
			if (size != casted.size) return false;
			if (compare.comesBefore(0, 1) != casted.compare.comesBefore(0, 1)) return false;
			Iterator<PriorityQueueNode.Integer<E>> iter = iterator();
			Iterator<PriorityQueueNode.Integer<E>> otherIter = casted.iterator();
			while (iter.hasNext()) {
				if (!iter.next().equals(otherIter.next())) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Computes a hashCode.
	 *
	 * @return a hashCode
	 */
	@Override
	public int hashCode() {
		int h = 0;
		for (PriorityQueueNode.Integer<E> e : this) {
			h = 31 * h + java.lang.Integer.hashCode(e.value);
			h = 31 * h + e.element.hashCode();
		}
		return h;
	}
	
	@Override
	public final boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public final Iterator<PriorityQueueNode.Integer<E>> iterator() {
		return new FibonacciHeapIterator();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if this and other have different priority-order (e.g., one is a 
	 * minheap while the other is a maxheap)
	 */
	@Override
	public boolean merge(SimpleFibonacciHeap<E> other) {
		if (compare.comesBefore(0,1) != other.compare.comesBefore(0,1)) {
			throw new IllegalArgumentException("this and other follow different priority-order");
		}
		if (other.size > 0) {
			other.min.insertListInto(min);
			if (compare.comesBefore(other.min.e.value, min.e.value)) {
				min = other.min;
			}
			size += other.size;
			other.clear();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean offer(E element, int priority) {
		return internalOffer(new PriorityQueueNode.Integer<E>(element, priority));
	}
	
	@Override
	public boolean offer(PriorityQueueNode.Integer<E> pair) {
		return internalOffer(pair.copy());
	}
	
	@Override
	public final E peekElement() {
		return min != null ? min.e.element : null;
	}
	
	@Override
	public final PriorityQueueNode.Integer<E> peek() {
		return min != null ? min.e : null;
	}
	
	@Override
	public final int peekPriority() {
		return min != null ? min.e.value : extreme;
	}
	
	@Override
	public final int peekPriority(E element) {
		Node<E> node = find(element);
		return node != null ? node.e.value : extreme;
	}
	
	@Override
	public final E pollElement() {
		PriorityQueueNode.Integer<E> min = poll();
		return min != null ? min.element : null;
	}
	
	@Override
	public PriorityQueueNode.Integer<E> poll() {
		if (size == 1) {
			PriorityQueueNode.Integer<E> pair = min.e;
			min = null;
			size = 0;
			return pair;
		} else if (size > 1) {
			Node<E> z = min;
			if (z.child != null) {
				z.child.clearParentReferences();
				z.child.insertListInto(min);
			}
			min = min.right;
			z.left.right = min;
			min.left  = z.left;
			consolidate();
			size--;
			return z.e;
		}
		return null;
	}
	
	@Override
	public final boolean promote(E element, int priority) {
		Node<E> node = find(element);
		if (node != null && compare.comesBefore(priority, node.e.value)) {
			internalPromote(node, priority);
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean remove(Object o) {
		Node<E> node = null;
		if (o instanceof PriorityQueueNode.Integer) {
			PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
			node = find(pair.element);
		} else {
			node = find(o);
		}
		if (node == null) {
			return false;
		}
		internalPromote(node, compare.comesBefore(min.e.value-1, min.e.value) ? min.e.value-1 : min.e.value+1);
		poll();
		return true;
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
		HashSet<Object> discardThese = new HashSet<Object>();
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Integer) {
				PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
				discardThese.add(pair.element);
			} else {
				discardThese.add(o);
			}
		}
		ArrayList<PriorityQueueNode.Integer<E>> keepList = new ArrayList<PriorityQueueNode.Integer<E>>();
		for (PriorityQueueNode.Integer<E> e : this) {
			if (!discardThese.contains(e.element)) {
				keepList.add(e);
			}
		}
		if (keepList.size() < size) {
			clear();
			for (PriorityQueueNode.Integer<E> e : keepList) {
				internalOffer(e);
			}
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
	public final boolean retainAll(Collection<?> c) {
		HashSet<Object> keepThese = new HashSet<Object>();
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Integer) {
				PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
				keepThese.add(pair.element);
			} else {
				keepThese.add(o);
			}
		}
		ArrayList<PriorityQueueNode.Integer<E>> keepList = new ArrayList<PriorityQueueNode.Integer<E>>(keepThese.size());
		for (PriorityQueueNode.Integer<E> e : this) {
			if (keepThese.contains(e.element)) {
				keepList.add(e);
			}
		}
		if (keepList.size() < size) {
			clear();
			for (PriorityQueueNode.Integer<E> e : keepList) {
				internalOffer(e);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public final int size() {
		return size;
	}
	
	@Override
	public final Object[] toArray() {
		Object[] array = new Object[size];
		int i = 0;
		for (PriorityQueueNode.Integer<E> e : this) {
			array[i] = e;
			i++;
		}
		return array;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws ArrayStoreException if the runtime component type of array is not
	 * compatible with the type of the (element, priority) pairs.
	 *
	 * @throws NullPointerException if array is null
	 */
	@Override
	public final <T> T[] toArray(T[] array) {
		@SuppressWarnings("unchecked")
		T[] result = array.length >= size ? array : (T[])Array.newInstance(array.getClass().getComponentType(), size);
		int i = 0;
		for (PriorityQueueNode.Integer<E> e : this) {
			@SuppressWarnings("unchecked")
			T nextElement = (T)e;
			result[i] = nextElement;
			i++;
		}
		if (result.length > size) {
			result[size] = null;
		}
		return result;
	}
	
	/*
	 * package access to enable sublcass overriding with simple index check/
	 */
	Node<E> find(Object element) {
		NodeIterator iter = new NodeIterator();
		while (iter.hasNext()) {
			Node<E> n  = iter.next();
			if (n.e.element.equals(element)) {
				return n;
			}
		}
		return null;
	}
	
	void record(E element, Node<E> node) {}
	
	/*
	 * used internally: doesn't check if already contains element
	 */
	private boolean internalOffer(PriorityQueueNode.Integer<E> pair) {
		if (min == null) {
			min = new Node<E>(pair);
			record(pair.element, min);
			size = 1;
		} else {
			Node<E> added = new Node<E>(pair, min);
			record(pair.element, added);
			if (compare.comesBefore(pair.value, min.e.value)) {
				min = added;
			}
			size++;
		}
		return true;
	}
	
	private void internalPromote(Node<E> x, int priority) {
		// only called if priority decreased for a minheap (increased for a maxheap)
		// so no checks needed here.
		x.e.value = priority;
		Node<E> y = x.parent;
		if (y != null && compare.comesBefore(priority, y.e.value)) {
			cut(x, y);
			cascadingCut(y);			
		}
		if (compare.comesBefore(priority, min.e.value)) {
			min = x;
		}
	}
	
	private void internalDemote(Node<E> x, int priority) {
		// only called if priority increased for a minheap (decreased for a maxheap)
		// so no checks needed here.
		
		// 1. promote (opposite) to front
		internalPromote(x, compare.comesBefore(min.e.value-1, min.e.value) ? min.e.value-1 : min.e.value+1);
		// 2. poll() to remove
		poll();
		// 3. reinsert with new priority
		x.e.value = priority;
		internalOffer(x.e);
	}
	
	private void cascadingCut(Node<E> y) {
		Node<E> z = y.parent;
		if (z != null) {
			if (!y.mark) {
				y.mark = true;
			} else {
				cut(y, z);
				cascadingCut(z);
			}
		}
	}
	
	private void cut(Node<E> x, Node<E> y) {
		// 1. remove x from child list of y, decrementing degree of y
		if (y.degree > 1) {
			// ensure y's child reference isn't x
			y.child = x.right;
			// link x's left and right neighbors to remove x
			x.left.right = x.right;
			x.right.left = x.left;
			y.degree--;
		} else {
			y.child = null;
			y.degree = 0;
		}
		// 2. add x to the root list
		x.insertInto(min);
		x.parent = null;
		x.mark = false;
	}
	
	private void consolidate() {
		int dn = (int)(Math.log(size) * INV_LOG_GOLDEN_RATIO);
		
		// first node of iteration
		Node<E> w = min;
		// disconnect from left to enable detecting end of list
		w.left.right = null;
		do{
			Node<E> x = w;
			// prepare for next iteration
			w = w.right;
			// disconnect x from root list
			x.left = x.right = null;
			
			int d = x.degree;
			while (rootsByDegrees[d] != null) {
				Node<E> y = rootsByDegrees[d];
				if (compare.comesBefore(y.e.value, x.e.value)) {
					Node<E> temp = x;
					x = y;
					y = temp;
				}
				fibHeapLink(y, x);
				rootsByDegrees[d] = null;
				d++;
			}
			rootsByDegrees[d] = x;
		} while (w != null);
		
		min = null;
		for (int i = 0; i <= dn; i++) {
			if (rootsByDegrees[i] != null) {
				if (min == null) {
					rootsByDegrees[i].singletonList();
					min = rootsByDegrees[i];
				} else {
					rootsByDegrees[i].insertInto(min);
					if (compare.comesBefore(rootsByDegrees[i].e.value, min.e.value)) {
						min = rootsByDegrees[i];
					}
				}
				// need this since this array is shared by all calls to consolidate
				rootsByDegrees[i] = null;
			}
		}
	}
	
	private void fibHeapLink(Node<E> y, Node<E> x) {
		// 1. Remove y from root list step.
		//    This is not needed because I'm instead
		//    dismantling root list in consolidate
		//    before rebuilding it.
		// 2. Make y a child of x
		if (x.degree > 0) {
			y.insertInto(x.child);
			y.parent = x;
			x.degree++;
		} else {
			y.singletonList();
			x.child = y;
			y.parent = x;
			x.degree = 1;
		}
		y.mark = false;
	}
	
	private Node<E>[] nodeArrayAllocate(int n) {
		@SuppressWarnings("unchecked")
		Node<E>[] array = new Node[n];
		return array;
	}
	
	@FunctionalInterface
	static interface PriorityComparator {
		boolean comesBefore(int p1, int p2);
	}
	
	NodeIterator nodeIterator() {
		return new NodeIterator();
	}
	
	class Node<E2> {
		PriorityQueueNode.Integer<E2> e;
		private Node<E2> parent;
		private Node<E2> child;
		private Node<E2> left;
		private Node<E2> right;
		private int degree;
		private boolean mark;
		
		/*
		 * new root list (i.e., called to create new top-level list when empty
		 */
		public Node(PriorityQueueNode.Integer<E2> e) {
			this.e = e;
			singletonList();
		}
		
		/*
		 * adds newly constructed node to root list
		 */
		public Node(PriorityQueueNode.Integer<E2> e, Node<E2> list) {
			this.e = e;
			insertInto(list);
		}
		
		private Node(Node<E2> other) {
			e = other.e.copy();
			degree = other.degree;
			mark = other.mark;
		}
		
		private Node(Node<E2> other, Node<E2> toTheLeft) {
			this(other);
			left = toTheLeft;
		}
		
		private Node<E2> copy() {
			return copyList(this, null);
		}
		
		private Node<E2> copyList(Node<E2> x, Node<E2> p) {
			Node<E2> y = new Node<E2>(x);
			y.parent = p;
			if (x.child != null) {
				y.child = copyList(x.child, y);
			}
			Node<E2> rightOf = y;
			for (Node<E2> next = x.right; next != x; next = next.right, rightOf = rightOf.right) {
				rightOf.right = new Node<E2>(next, rightOf);
				rightOf.right.parent = p;
				if (next.child != null) {
					rightOf.right.child = copyList(next.child, rightOf.right);
				}
			}
			rightOf.right = y;
			y.left = rightOf;
			return y;
		}
		
		private void singletonList() {
			left = right = this;
		}

		private void insertInto(Node<E2> list) {
			right = list.right;
			left = list;
			list.right = list.right.left = this;
		}
		
		private void insertListInto(Node<E2> list) {
			list.right.left = left;
			left.right = list.right;
			list.right = this;
			left = list;
		}
		
		private void clearParentReferences() {
			for (Node<E2> next = this; next.parent != null; next = next.right) {
				next.parent = null;
			}
		}
	}
	
	private class FibonacciHeapIterator implements Iterator<PriorityQueueNode.Integer<E>> {
		
		private final Deque<Node<E>> stack;
		
		public FibonacciHeapIterator() {
			stack = new ArrayDeque<Node<E>>(size);
			if (size > 0) {
				stack.push(min);
				for (Node<E> next = min.right; next != min; next = next.right) {
					stack.push(next);
				}
			}				
		}
		
		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}
		
		@Override
		public PriorityQueueNode.Integer<E> next() {
			// normally, the next method of an Iterator is
			// required to throw NoSuchElementException is caled when empty.
			// The pop() method of the ArrayDeque does this though. So no need
			// for explicit check.
			Node<E> current = stack.pop();
			if (current.degree > 0) {
				stack.push(current.child);
				Node<E> next = current.child.right;
				for (int i = 1; i < current.degree; i++, next = next.right) {
					stack.push(next);
				}
			}
			return current.e;
		}
	}
	
	class NodeIterator implements Iterator<Node<E>> {
		
		private final Deque<Node<E>> stack;
		
		public NodeIterator() {
			stack = new ArrayDeque<Node<E>>(size);
			if (size > 0) {
				stack.push(min);
				for (Node<E> next = min.right; next != min; next = next.right) {
					stack.push(next);
				}
			}				
		}
		
		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}
		
		@Override
		public Node<E> next() {
			// normally, the next method of an Iterator is
			// required to throw NoSuchElementException is called when empty.
			// The pop() method of the ArrayDeque does this though. So no need
			// for explicit check.
			Node<E> current = stack.pop();
			if (current.degree > 0) {
				stack.push(current.child);
				Node<E> next = current.child.right;
				for (int i = 1; i < current.degree; i++, next = next.right) {
					stack.push(next);
				}
			}
			return current;
		}
	}
}
